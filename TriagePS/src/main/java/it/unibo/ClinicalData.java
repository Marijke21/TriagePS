package it.unibo;

import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;

/**
 * Contenitore passato a ClinicalController.
 * Se encounter != null si sta modificando un accesso esistente.
 * Se encounter == null e' un nuovo accesso.
 */
public class ClinicalData {

    private final Patient  patient;
    private final Encounter encounter;

    public ClinicalData(Patient patient, Encounter encounter) {
        this.patient   = patient;
        this.encounter = encounter;
    }

    public ClinicalData(Patient patient) {
        this(patient, null);
    }

    public Patient  getPatient()   { return patient; }
    public Encounter getEncounter() { return encounter; }
    public boolean  isModifica()   { return encounter != null; }
}
