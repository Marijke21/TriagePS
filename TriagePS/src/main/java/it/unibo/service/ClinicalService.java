package it.unibo.service;

import it.unibo.config.FhirClientConfig;
import it.unibo.repository.*;
import org.hl7.fhir.r4.model.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClinicalService {

    private final EncounterRepository    encounterRepo;
    private final ObservationRepository  observationRepo;
    private final AllergyRepository      allergyRepo;
    private final MedicationRepository   medicationRepo;

    public ClinicalService() {
        encounterRepo   = new EncounterRepository();
        observationRepo = new ObservationRepository();
        allergyRepo     = new AllergyRepository();
        medicationRepo  = new MedicationRepository();
    }

    // ---- ENCOUNTER ----

    public Encounter createEncounter(Patient patient, String codice) {
        Encounter enc = new Encounter();
        enc.setStatus(Encounter.EncounterStatus.INPROGRESS);
        enc.setClass_(new Coding()
            .setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode")
            .setCode("EMER").setDisplay("Emergency"));
        enc.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPart()));
        enc.setPeriod(new Period().setStart(new Date()));
        enc.setPriority(buildTriageConcept(codice));
        return encounterRepo.save(enc);
    }

    /** Aggiorna il codice triage su un Encounter esistente. */
    public void aggiornaTriage(Encounter encounter, String nuovoTriage) {
        encounter.setPriority(buildTriageConcept(nuovoTriage));
        FhirClientConfig.getClient().update().resource(encounter).execute();
    }

    /** Cancella tutte le Observation di un Encounter per poterle riscrivere. */
    public void cancellaObservations(String encounterId) {
        IGenericClient client = FhirClientConfig.getClient();
        for (Observation obs : observationRepo.findByEncounterId(encounterId)) {
            try { client.delete().resource(obs).execute(); }
            catch (Exception ignored) {}
        }
    }

    private CodeableConcept buildTriageConcept(String codice) {
        return new CodeableConcept().addCoding(new Coding()
            .setSystem("http://pronto.local/triage")
            .setCode(codice.toLowerCase())
            .setDisplay("Codice " + capitalize(codice)));
    }

    // ---- OBSERVATION ----

    public void saveObservation(Patient patient, String encounterId,
                                 String loincCode, String displayName,
                                 double value, String unit, String ucumCode) {
        Observation obs = new Observation();
        obs.setStatus(Observation.ObservationStatus.FINAL);
        obs.addCategory().addCoding()
            .setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
            .setCode("vital-signs").setDisplay("Vital Signs");
        obs.getCode().addCoding()
            .setSystem("http://loinc.org").setCode(loincCode).setDisplay(displayName);
        obs.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPart()));
        if (encounterId != null && !encounterId.isBlank())
            obs.setEncounter(new Reference("Encounter/" + encounterId));
        obs.setEffective(new DateTimeType(new Date()));
        obs.setValue(new Quantity()
            .setValue(BigDecimal.valueOf(value))
            .setUnit(unit)
            .setSystem("http://unitsofmeasure.org")
            .setCode(ucumCode));
        observationRepo.save(obs);
    }

    // Overload con oggetto Encounter (compatibilita)
    public void saveObservation(Patient patient, Encounter encounter,
                                 String loincCode, String displayName,
                                 double value, String unit, String ucumCode) {
        saveObservation(patient, encounter.getIdElement().getIdPart(),
            loincCode, displayName, value, unit, ucumCode);
    }

    public List<Observation> getObservations(String encounterId) {
        return observationRepo.findByEncounterId(encounterId);
    }

    // ---- ALLERGY ----

    public void saveAllergy(Patient patient, String text) {
        AllergyIntolerance a = new AllergyIntolerance();
        a.setClinicalStatus(new CodeableConcept().addCoding(new Coding()
            .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
            .setCode("active")));
        a.setType(AllergyIntolerance.AllergyIntoleranceType.ALLERGY);
        a.getCode().setText(text);
        a.setPatient(new Reference("Patient/" + patient.getIdElement().getIdPart()));
        allergyRepo.save(a);
    }

    public List<AllergyIntolerance> getAllergie(String patientId) {
        IGenericClient client = FhirClientConfig.getClient();
        Bundle b = client.search()
            .forResource(AllergyIntolerance.class)
            .where(AllergyIntolerance.PATIENT.hasId(patientId))
            .returnBundle(Bundle.class).execute();
        List<AllergyIntolerance> result = new ArrayList<>();
        for (Bundle.BundleEntryComponent e : b.getEntry())
            if (e.getResource() instanceof AllergyIntolerance ai) result.add(ai);
        return result;
    }

    // ---- MEDICATION ----

    public void saveMedication(Patient patient, String text) {
        MedicationStatement m = new MedicationStatement();
        m.setStatus(MedicationStatement.MedicationStatementStatus.ACTIVE);
        m.setMedication(new CodeableConcept().setText(text));
        m.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPart()));
        medicationRepo.save(m);
    }

    public List<MedicationStatement> getFarmaci(String patientId) {
        IGenericClient client = FhirClientConfig.getClient();
        Bundle b = client.search()
            .forResource(MedicationStatement.class)
            .where(MedicationStatement.PATIENT.hasId(patientId))
            .returnBundle(Bundle.class).execute();
        List<MedicationStatement> result = new ArrayList<>();
        for (Bundle.BundleEntryComponent e : b.getEntry())
            if (e.getResource() instanceof MedicationStatement ms) result.add(ms);
        return result;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}