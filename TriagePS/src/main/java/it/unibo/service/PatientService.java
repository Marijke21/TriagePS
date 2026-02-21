package it.unibo.service;

import it.unibo.repository.PatientRepository;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.Optional;

public class PatientService {

    private final PatientRepository repository;

    public PatientService() {
        this.repository = new PatientRepository();
    }

    public Optional<Patient> findByCodiceFiscale(String cf) {
        return repository.findByCodiceFiscale(cf);
    }

    public Patient registerWithCodiceFiscale(String cf, String nome, String cognome,
                                              Date dataNascita, String sesso) {
        Patient patient = buildPatient(nome, cognome, dataNascita, sesso);
        patient.addIdentifier()
                .setSystem("http://hl7.it/fhir/sid/codiceFiscale")
                .setValue(cf);
        return repository.save(patient);
    }

    public Patient registerForeigner(String nome, String cognome, Date dataNascita,
                                      String sesso, String tipoDoc, String numeroDoc,
                                      String paeseDoc, String nazionalita,
                                      String telefono, String email) {
        Patient patient = buildPatient(nome, cognome, dataNascita, sesso);

        if (tipoDoc != null && !tipoDoc.isBlank() && numeroDoc != null && !numeroDoc.isBlank()) {
            patient.addIdentifier()
                    .setSystem("http://pronto.local/documento/" + tipoDoc.toLowerCase())
                    .setValue(numeroDoc)
                    .getType().addCoding()
                    .setSystem("http://pronto.local/tipoDocumento")
                    .setCode(tipoDoc)
                    .setDisplay(paeseDoc);
        }

        if (nazionalita != null && !nazionalita.isBlank()) {
            Extension ext = new Extension();
            ext.setUrl("http://hl7.org/fhir/StructureDefinition/patient-nationality");
            ext.setValue(new StringType(nazionalita));
            patient.addExtension(ext);
        }

        if (telefono != null && !telefono.isBlank()) {
            patient.addTelecom()
                    .setSystem(ContactPoint.ContactPointSystem.PHONE)
                    .setValue(telefono);
        }
        if (email != null && !email.isBlank()) {
            patient.addTelecom()
                    .setSystem(ContactPoint.ContactPointSystem.EMAIL)
                    .setValue(email);
        }

        return repository.save(patient);
    }

    private Patient buildPatient(String nome, String cognome, Date dataNascita, String sesso) {
        Patient patient = new Patient();

        HumanName name = new HumanName();
        name.setFamily(cognome);
        name.addGiven(nome);
        patient.addName(name);

        if (dataNascita != null) {
            patient.setBirthDate(dataNascita);
        }

        if (sesso != null) {
            switch (sesso.toUpperCase()) {
                case "M" -> patient.setGender(Enumerations.AdministrativeGender.MALE);
                case "F" -> patient.setGender(Enumerations.AdministrativeGender.FEMALE);
                default -> patient.setGender(Enumerations.AdministrativeGender.OTHER);
            }
        }

        return patient;
    }

    public String getNomeCompleto(Patient patient) {
        if (patient.hasName()) {
            HumanName name = patient.getNameFirstRep();
            return name.getGivenAsSingleString() + " " + name.getFamily();
        }
        return "Paziente sconosciuto";
    }
}
