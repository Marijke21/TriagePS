package it.unibo.controller;

import it.unibo.App;
import it.unibo.DataReceiver;
import it.unibo.service.ClinicalService;
import it.unibo.service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TriageController implements DataReceiver {

    @FXML private Label nomeLabel;
    @FXML private Label accessoLabel;

    private Patient currentPatient;
    private final PatientService patientService = new PatientService();
    private final ClinicalService clinicalService = new ClinicalService();

    @Override
    public void receiveData(Object data) {
        if (data instanceof Patient patient) {
            this.currentPatient = patient;
            nomeLabel.setText(patientService.getNomeCompleto(patient));
            accessoLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
    }

    @FXML private void onBianco() { apriEncounter("BIANCO"); }
    @FXML private void onVerde()  { apriEncounter("VERDE"); }
    @FXML private void onGiallo() { apriEncounter("GIALLO"); }
    @FXML private void onRosso()  { apriEncounter("ROSSO"); }

    private void apriEncounter(String codice) {
    	if (currentPatient == null) {
            System.out.println("ALT - currentPatient è NULL");
            return;
        }
        try {
            Encounter encounter = clinicalService.createEncounter(currentPatient, codice);
            Map<String, Object> data = new HashMap<>();
            data.put("patient", currentPatient);
            data.put("encounter", encounter);
            data.put("triage", codice);
            App.navigateTo("clinical", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
