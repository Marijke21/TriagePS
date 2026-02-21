package it.unibo.service;

import it.unibo.repository.*;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ClinicalService {

    private final EncounterRepository encounterRepo;
    private final ObservationRepository observationRepo;
    private final AllergyRepository allergyRepo;
    private final MedicationRepository medicationRepo;

    public ClinicalService() {
        this.encounterRepo = new EncounterRepository();
        this.observationRepo = new ObservationRepository();
        this.allergyRepo = new AllergyRepository();
        this.medicationRepo = new MedicationRepository();
    }

    // ---- ENCOUNTER ----

    public Encounter createEncounter(Patient patient, String codice) {
        Encounter encounter = new Encounter();
        encounter.setStatus(Encounter.EncounterStatus.INPROGRESS);

        encounter.setClass_(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode")
                .setCode("EMER")
                .setDisplay("Emergency"));

        encounter.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPart()));

        Period period = new Period();
        period.setStart(new Date());
        encounter.setPeriod(period);

        CodeableConcept priority = new CodeableConcept();
        priority.addCoding()
                .setSystem("http://pronto.local/triage")
                .setCode(codice.toLowerCase())
                .setDisplay("Codice " + capitalize(codice));
        encounter.setPriority(priority);

        return encounterRepo.save(encounter);
    }

    // ---- OBSERVATION ----

    public void saveObservation(Patient patient, Encounter encounter,
                                 String loincCode, String displayName,
                                 double value, String unit, String ucumCode) {
        Observation obs = new Observation();
        obs.setStatus(Observation.ObservationStatus.FINAL);

        obs.addCategory().addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
                .setCode("vital-signs")
                .setDisplay("Vital Signs");

        obs.getCode().addCoding()
                .setSystem("http://loinc.org")
                .setCode(loincCode)
                .setDisplay(displayName);

        obs.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPart()));
        obs.setEncounter(new Reference("Encounter/" + encounter.getIdElement().getIdPart()));
        obs.setEffective(new DateTimeType(new Date()));

        Quantity qty = new Quantity();
        qty.setValue(BigDecimal.valueOf(value));
        qty.setUnit(unit);
        qty.setSystem("http://unitsofmeasure.org");
        qty.setCode(ucumCode);
        obs.setValue(qty);

        observationRepo.save(obs);
    }

    public List<Observation> getObservations(String encounterId) {
        return observationRepo.findByEncounterId(encounterId);
    }

    // ---- ALLERGY ----

    public void saveAllergy(Patient patient, String allergyText) {
        AllergyIntolerance allergy = new AllergyIntolerance();

        allergy.setClinicalStatus(new CodeableConcept().addCoding(
                new Coding()
                        .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
                        .setCode("active")));

        allergy.setType(AllergyIntolerance.AllergyIntoleranceType.ALLERGY);
        allergy.getCode().setText(allergyText);
        allergy.setPatient(new Reference("Patient/" + patient.getIdElement().getIdPart()));

        allergyRepo.save(allergy);
    }

    // ---- MEDICATION ----

    public void saveMedication(Patient patient, String medicationText) {
        MedicationStatement med = new MedicationStatement();
        med.setStatus(MedicationStatement.MedicationStatementStatus.ACTIVE);

        CodeableConcept medCode = new CodeableConcept();
        medCode.setText(medicationText);
        med.setMedication(medCode);

        med.setSubject(new Reference("Patient/" + patient.getIdElement().getIdPart()));

        medicationRepo.save(med);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
