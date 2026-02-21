package it.unibo.repository;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.config.FhirClientConfig;
import org.hl7.fhir.r4.model.AllergyIntolerance;

public class AllergyRepository {

    private final IGenericClient client;

    public AllergyRepository() {
        this.client = FhirClientConfig.getClient();
    }

    public AllergyIntolerance save(AllergyIntolerance allergy) {
        MethodOutcome outcome = client.create().resource(allergy).execute();
        allergy.setId(outcome.getId().getIdPart());
        return allergy;
    }
}
