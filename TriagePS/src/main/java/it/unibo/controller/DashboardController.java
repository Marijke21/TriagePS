package it.unibo.controller;

import it.unibo.App;
import it.unibo.ClinicalData;
import it.unibo.config.FhirClientConfig;
import it.unibo.service.PatientService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.hl7.fhir.r4.model.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController {

    @FXML private VBox colRosso;
    @FXML private VBox colGiallo;
    @FXML private VBox colVerde;
    @FXML private VBox colBianco;
    @FXML private Label statusLabel;

    private final PatientService patientService = new PatientService();
    private ScheduledExecutorService scheduler;

    @FXML
    public void initialize() {
        caricaDashboard();
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(
            () -> Platform.runLater(this::caricaDashboard),
            60, 60, TimeUnit.SECONDS);
    }

    @FXML private void onAggiorna()      { caricaDashboard(); }
    @FXML private void onNuovoPaziente() { App.navigateTo("main"); }

    private void caricaDashboard() {
        statusLabel.setText("Caricamento in corso...");
        colRosso.getChildren().clear();
        colGiallo.getChildren().clear();
        colVerde.getChildren().clear();
        colBianco.getChildren().clear();

        try {
            IGenericClient client = FhirClientConfig.getClient();

            Bundle bundle = client.search()
                    .forResource(Encounter.class)
                    .where(Encounter.STATUS.exactly().code("in-progress"))
                    .count(200)
                    .include(Encounter.INCLUDE_PATIENT)
                    .returnBundle(Bundle.class)
                    .execute();

            Map<String, Patient> patientMap = new HashMap<>();
            List<Encounter> encounters      = new ArrayList<>();

            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                if (entry.getResource() instanceof Encounter enc) {
                    encounters.add(enc);
                } else if (entry.getResource() instanceof Patient pat) {
                    patientMap.put("Patient/" + pat.getIdElement().getIdPart(), pat);
                }
            }

            // Recupera pazienti non arrivati con _include
            for (Encounter enc : encounters) {
                String ref = enc.getSubject().getReference();
                if (!patientMap.containsKey(ref)) {
                    try {
                        String pid = ref.replace("Patient/", "");
                        Patient pat = client.read().resource(Patient.class).withId(pid).execute();
                        patientMap.put(ref, pat);
                    } catch (Exception ignored) {}
                }
            }

            // Ordina: piu vecchi prima (attesa maggiore in cima)
            encounters.sort(Comparator.comparing(enc -> {
                if (enc.hasPeriod() && enc.getPeriod().hasStart())
                    return enc.getPeriod().getStart();
                return new Date();
            }));

            int totale = 0;
            for (Encounter enc : encounters) {
                Patient patient = patientMap.get(enc.getSubject().getReference());
                if (patient == null) continue;
                String triage = getTriage(enc);
                VBox card = buildCard(patient, enc);
                switch (triage) {
                    case "ROSSO"  -> colRosso.getChildren().add(card);
                    case "GIALLO" -> colGiallo.getChildren().add(card);
                    case "VERDE"  -> colVerde.getChildren().add(card);
                    default       -> colBianco.getChildren().add(card);
                }
                totale++;
            }

            statusLabel.setText("Pazienti in attesa: " + totale
                + "  |  Aggiornato: "
                + new SimpleDateFormat("HH:mm:ss").format(new Date()));

        } catch (Exception e) {
            statusLabel.setText("Errore FHIR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getTriage(Encounter enc) {
        if (enc.hasPriority())
            for (Coding c : enc.getPriority().getCoding())
                if ("http://pronto.local/triage".equals(c.getSystem()))
                    return c.getCode().toUpperCase();
        return "BIANCO";
    }

    private VBox buildCard(Patient patient, Encounter encounter) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(10));
        card.getStyleClass().add("patient-card");

        Label nomeLabel = new Label(patientService.getNomeCompleto(patient));
        nomeLabel.getStyleClass().add("card-nome");

        Label cfLabel = new Label("CF: " + getCF(patient));
        cfLabel.getStyleClass().add("card-cf");

        String dn = patient.hasBirthDate()
            ? new SimpleDateFormat("dd/MM/yyyy").format(patient.getBirthDate()) : "N/D";
        Label nascitaLabel = new Label("Nato il: " + dn);
        nascitaLabel.getStyleClass().add("card-info");

        Label attesaLabel = new Label("In attesa da: " + calcolaAttesa(encounter));
        attesaLabel.getStyleClass().add("card-attesa");

        Button btn = new Button("Modifica dati");
        btn.getStyleClass().add("btn-small");
        btn.setMaxWidth(Double.MAX_VALUE);
        // Passa sia il Patient sia l'Encounter a ClinicalController
        btn.setOnAction(e -> App.navigateTo("clinical", new ClinicalData(patient, encounter)));

        card.getChildren().addAll(nomeLabel, cfLabel, nascitaLabel, attesaLabel, btn);
        return card;
    }

    private String getCF(Patient patient) {
        for (Identifier id : patient.getIdentifier())
            if ("http://hl7.it/fhir/sid/codiceFiscale".equals(id.getSystem()))
                return id.getValue();
        for (Identifier id : patient.getIdentifier())
            if (id.getSystem() != null && id.getSystem().startsWith("http://pronto.local/documento/")) {
                String tipo = id.getType().hasCoding()
                    ? id.getType().getCodingFirstRep().getCode() : "Doc";
                return tipo + ": " + id.getValue();
            }
        return "N/D";
    }

    private String calcolaAttesa(Encounter enc) {
        if (!enc.hasPeriod() || !enc.getPeriod().hasStart()) return "N/D";
        long min = (new Date().getTime() - enc.getPeriod().getStart().getTime()) / 60000;
        if (min < 60) return min + " min";
        return (min / 60) + "h " + (min % 60) + "min";
    }
}