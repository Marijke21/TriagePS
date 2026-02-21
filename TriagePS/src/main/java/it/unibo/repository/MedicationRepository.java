package it.unibo.repository;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.config.FhirClientConfig;
import org.hl7.fhir.r4.model.MedicationStatement;

public class MedicationRepository {

    private final IGenericClient client;

    public MedicationRepository() {
        this.client = FhirClientConfig.getClient();
    }

    public MedicationStatement save(MedicationStatement medication) {
        MethodOutcome outcome = client.create().resource(medication).execute();
        medication.setId(outcome.getId().getIdPart());
        return medication;
    }
}
