package it.unibo.repository;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.config.FhirClientConfig;
import org.hl7.fhir.r4.model.Encounter;

public class EncounterRepository {

    private final IGenericClient client;

    public EncounterRepository() {
        this.client = FhirClientConfig.getClient();
    }

    public Encounter save(Encounter encounter) {
        MethodOutcome outcome = client.create().resource(encounter).execute();
        encounter.setId(outcome.getId().getIdPart());
        return encounter;
    }
}
