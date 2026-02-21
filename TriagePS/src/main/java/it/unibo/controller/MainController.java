package it.unibo.controller;

import it.unibo.App;
import it.unibo.service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.hl7.fhir.r4.model.Patient;

import java.util.Optional;

public class MainController {

    @FXML private TextField cfField;

    private final PatientService patientService = new PatientService();

    @FXML
    private void onConferma() {
        String cf = cfField.getText().trim().toUpperCase();

        if (cf.isBlank()) {
            showAlert("Campo obbligatorio", "Inserisci il codice fiscale del paziente.");
            return;
        }

        try {
            Optional<Patient> found = patientService.findByCodiceFiscale(cf);
            if (found.isPresent()) {
                App.navigateTo("clinical", found.get());
            } else {
                showAlert("Paziente non trovato",
                        "Nessun paziente trovato con questo codice fiscale.\n" +
                        "Usa il pulsante REGISTRA per registrare un nuovo paziente.");
            }
        } catch (Exception e) {
            showAlert("Errore di connessione",
                    "Impossibile connettersi al server FHIR.\n" +
                    "Assicurati che Docker sia avviato con HAPI FHIR su localhost:8080.");
        }
    }

    @FXML
    private void onRegistra() {
        App.navigateTo("registrazione");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
