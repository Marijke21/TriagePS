package it.unibo.repository;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.config.FhirClientConfig;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class ObservationRepository {

    private final IGenericClient client;

    public ObservationRepository() {
        this.client = FhirClientConfig.getClient();
    }

    public Observation save(Observation observation) {
        MethodOutcome outcome = client.create().resource(observation).execute();
        observation.setId(outcome.getId().getIdPart());
        return observation;
    }

    public List<Observation> findByEncounterId(String encounterId) {
        Bundle bundle = client.search()
                .forResource(Observation.class)
                .where(Observation.ENCOUNTER.hasId(encounterId))
                .returnBundle(Bundle.class)
                .execute();

        List<Observation> observations = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Resource resource = entry.getResource();
            if (resource instanceof Observation) {
                observations.add((Observation) resource);
            }
        }
        return observations;
    }
}
