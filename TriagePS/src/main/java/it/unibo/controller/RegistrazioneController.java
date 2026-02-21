package it.unibo.controller;

import it.unibo.App;
import it.unibo.service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hl7.fhir.r4.model.Patient;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RegistrazioneController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private DatePicker dataNascitaPicker;
    @FXML private ToggleGroup sessoGroup;
    @FXML private RadioButton sessoM;
    @FXML private RadioButton sessoF;
    @FXML private RadioButton sessoAltro;

    @FXML private TextField tipoDocField;
    @FXML private TextField numeroDocField;
    @FXML private TextField paeseDocField;
    @FXML private TextField nazionalitaField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private TextField cfField;

    private final PatientService patientService = new PatientService();

    @FXML
    private void onRegistra() {
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        LocalDate dataNascita = dataNascitaPicker.getValue();

        if (nome.isBlank() || cognome.isBlank() || dataNascita == null || sessoGroup.getSelectedToggle() == null) {
            showAlert("Campi obbligatori", "Compila tutti i campi obbligatori: nome, cognome, data di nascita e sesso.");
            return;
        }

        String sesso = ((RadioButton) sessoGroup.getSelectedToggle()).getText();
        Date dataNascitaDate = Date.from(dataNascita.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String cf         = cfField != null ? cfField.getText().trim().toUpperCase() : "";
        String tipoDoc    = tipoDocField.getText().trim();
        String numeroDoc  = numeroDocField.getText().trim();
        String paeseDoc   = paeseDocField.getText().trim();
        String nazionalita = nazionalitaField.getText().trim();
        String telefono   = telefonoField.getText().trim();
        String email      = emailField.getText().trim();

        try {
            Patient patient;
            if (!cf.isBlank()) {
                patient = patientService.registerWithCodiceFiscale(cf, nome, cognome, dataNascitaDate, sesso);
            } else {
                patient = patientService.registerForeigner(nome, cognome, dataNascitaDate, sesso,
                        tipoDoc, numeroDoc, paeseDoc, nazionalita, telefono, email);
            }
            // Vai direttamente alla schermata clinica
            App.navigateTo("clinical", patient);
        } catch (Exception e) {
            showAlert("Errore", "Errore durante la registrazione: " + e.getMessage());
        }
    }

    @FXML
    private void onAnnulla() {
        App.navigateTo("main");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
