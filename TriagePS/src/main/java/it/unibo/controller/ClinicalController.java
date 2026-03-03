package it.unibo.controller;

import it.unibo.App;
import it.unibo.DataReceiver;
import it.unibo.service.ClinicalService;
import it.unibo.service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;

import java.util.Map;

public class ClinicalController implements DataReceiver {

    // Header
    @FXML private Label pazienteLabel;

    // Parametri vitali
    @FXML private TextField hrField;
    @FXML private TextField pasSisField;
    @FXML private TextField pasDiaField;
    @FXML private TextField spo2Field;
    @FXML private TextField frField;
    @FXML private TextField tempField;

    // Parametri aggiuntivi
    @FXML private TextField gcsField;
    @FXML private TextField nrsField;
    @FXML private TextField glicemiaField;
    @FXML private TextField pesoField;
    @FXML private TextField altezzaField;

    // Info cliniche
    @FXML private TextField allergiaInput;
    @FXML private ListView<String> allergiaList;
    @FXML private TextField farmacoInput;
    @FXML private ListView<String> farmacoList;
    @FXML private TextArea patologieArea;
    @FXML private HBox gravidanzaBox;
    @FXML private RadioButton gravNa;
    @FXML private RadioButton gravSi;
    @FXML private RadioButton gravSospetta;
    @FXML private TextArea disabilitaArea;
    @FXML private TextArea eventiArea;
    @FXML private TextArea rischioArea;

    // Riepilogo
    @FXML private TableView<ObsRow> riepilogoTable;
    @FXML private TableColumn<ObsRow, String> colParametro;
    @FXML private TableColumn<ObsRow, String> colValore;
    @FXML private TableColumn<ObsRow, String> colUnita;

    private Patient currentPatient;

    private final ObservableList<String> allergie = FXCollections.observableArrayList();
    private final ObservableList<String> farmaci = FXCollections.observableArrayList();
    private final ClinicalService clinicalService = new ClinicalService();
    private final PatientService patientService = new PatientService();

    @FXML
    public void initialize() {
        // Liste dinamiche
        if (allergiaList != null) allergiaList.setItems(allergie);
        if (farmacoList != null) farmacoList.setItems(farmaci);

        // ToggleGroup per gravidanza gestito via codice
        if (gravNa != null && gravSi != null && gravSospetta != null) {
            ToggleGroup gravidanzaGroup = new ToggleGroup();
            gravNa.setToggleGroup(gravidanzaGroup);
            gravSi.setToggleGroup(gravidanzaGroup);
            gravSospetta.setToggleGroup(gravidanzaGroup);
            gravNa.setSelected(true);
        }

        // Colonne tabella riepilogo
        if (colParametro != null) {
            colParametro.setCellValueFactory(new PropertyValueFactory<>("parametro"));
            colValore.setCellValueFactory(new PropertyValueFactory<>("valore"));
            colUnita.setCellValueFactory(new PropertyValueFactory<>("unita"));
        }
    }

    @Override
    public void receiveData(Object data) {
        if (data instanceof Patient patient) {
            this.currentPatient = patient;
            if (pazienteLabel != null)
                pazienteLabel.setText(patientService.getNomeCompleto(patient));
        }
    }

    // ---- Allergie ----

    @FXML
    private void onAggiungiAllergia() {
        String val = allergiaInput.getText().trim();
        if (!val.isBlank()) {
            allergie.add(val);
            allergiaInput.clear();
        }
    }

    @FXML
    private void onRimuoviAllergia() {
        String sel = allergiaList.getSelectionModel().getSelectedItem();
        if (sel != null) allergie.remove(sel);
    }

    // ---- Farmaci ----

    @FXML
    private void onAggiungiFarmaco() {
        String val = farmacoInput.getText().trim();
        if (!val.isBlank()) {
            farmaci.add(val);
            farmacoInput.clear();
        }
    }

    @FXML
    private void onRimuoviFarmaco() {
        String sel = farmacoList.getSelectionModel().getSelectedItem();
        if (sel != null) farmaci.remove(sel);
    }

    // ---- Riepilogo ----

    @FXML
    private void onAggiornaRiepilogo() {
        if (riepilogoTable == null) return;
        ObservableList<ObsRow> rows = FXCollections.observableArrayList();
        addRow(rows, "Freq. cardiaca", hrField, "bpm");
        addRow(rows, "PA Sistolica", pasSisField, "mmHg");
        addRow(rows, "PA Diastolica", pasDiaField, "mmHg");
        addRow(rows, "SpO₂", spo2Field, "%");
        addRow(rows, "Freq. respiratoria", frField, "atti/min");
        addRow(rows, "Temperatura", tempField, "°C");
        addRow(rows, "GCS", gcsField, "/15");
        addRow(rows, "NRS", nrsField, "/10");
        addRow(rows, "Glicemia", glicemiaField, "mg/dL");
        addRow(rows, "Peso", pesoField, "kg");
        addRow(rows, "Altezza", altezzaField, "cm");
        riepilogoTable.setItems(rows);
    }

    private void addRow(ObservableList<ObsRow> rows, String nome, TextField field, String unita) {
        if (field != null && !field.getText().isBlank()) {
            rows.add(new ObsRow(nome, field.getText().trim(), unita));
        }
    }

    // ---- Salvataggio con triage ----

    @FXML private void onSalvaBianco() { salva("BIANCO"); }
    @FXML private void onSalvaVerde()  { salva("VERDE"); }
    @FXML private void onSalvaGiallo() { salva("GIALLO"); }
    @FXML private void onSalvaRosso()  { salva("ROSSO"); }

    @FXML
    private void onAnnulla() {
        App.navigateTo("dashboard");
    }

    private void salva(String triage) {
        try {
            // 1. Crea Encounter con codice triage
            Encounter encounter = clinicalService.createEncounter(currentPatient, triage);
            String encounterId = encounter.getIdElement().getIdPart();

            // 2. Salva parametri vitali
            saveObsIfPresent(hrField,     encounterId, "8867-4", "Heart rate",              "bpm",      "/min");
            saveObsIfPresent(pasSisField, encounterId, "8480-6", "Systolic blood pressure", "mmHg",     "mm[Hg]");
            saveObsIfPresent(pasDiaField, encounterId, "8462-4", "Diastolic blood pressure","mmHg",     "mm[Hg]");
            saveObsIfPresent(spo2Field,   encounterId, "59408-5","Oxygen saturation",       "pct",      "%");
            saveObsIfPresent(frField,     encounterId, "9279-1", "Respiratory rate",        "atti/min", "/min");
            saveObsIfPresent(tempField,   encounterId, "8310-5", "Body temperature",        "grC",      "Cel");

            // 3. Salva parametri aggiuntivi
            saveObsIfPresent(gcsField,      encounterId, "9269-2", "Glasgow coma score", "score", "{score}");
            saveObsIfPresent(nrsField,      encounterId, "72514-3","Pain severity NRS",  "score", "{score}");
            saveObsIfPresent(glicemiaField, encounterId, "2339-0", "Glucose",            "mg/dL", "mg/dL");
            saveObsIfPresent(pesoField,     encounterId, "29463-7","Body weight",        "kg",    "kg");
            saveObsIfPresent(altezzaField,  encounterId, "8302-2", "Body height",        "cm",    "cm");

            // 4. Salva allergie
            for (String a : allergie) {
                clinicalService.saveAllergy(currentPatient, a);
            }

            // 5. Salva farmaci
            for (String f : farmaci) {
                clinicalService.saveMedication(currentPatient, f);
            }

            showAlert(Alert.AlertType.INFORMATION, "Salvataggio completato",
                    "Accesso salvato con codice " + triage + ".\nTutti i dati sono stati inviati al server FHIR.");

            App.navigateTo("dashboard");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Errore durante il salvataggio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveObsIfPresent(TextField field, String encounterId,
    								String loinc, String display, String unit, String ucum) {
		if (field == null || field.getText().isBlank()) return;
		try {
			double val = Double.parseDouble(field.getText().trim().replace(",", "."));
			clinicalService.saveObservation(currentPatient, encounterId, loinc, display, val, unit, ucum);
		} catch (NumberFormatException ignored) {}
	}

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ---- Classe riga tabella ----

    public static class ObsRow {
        private final String parametro;
        private final String valore;
        private final String unita;

        public ObsRow(String parametro, String valore, String unita) {
            this.parametro = parametro;
            this.valore = valore;
            this.unita = unita;
        }

        public String getParametro() { return parametro; }
        public String getValore()    { return valore; }
        public String getUnita()     { return unita; }
    }
}