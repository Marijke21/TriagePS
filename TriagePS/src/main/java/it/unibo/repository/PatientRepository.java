package it.unibo.repository;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import it.unibo.config.FhirClientConfig;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.util.Optional;

public class PatientRepository {

    private final IGenericClient client;

    public PatientRepository() {
        this.client = FhirClientConfig.getClient();
    }

    public Patient save(Patient patient) {
        MethodOutcome outcome = client.create().resource(patient).execute();
        patient.setId(outcome.getId().getIdPart());
        return patient;
    }

    public Optional<Patient> findByCodiceFiscale(String cf) {
        Bundle bundle = client.search()
                .forResource(Patient.class)
                .where(new TokenClientParam("identifier")
                        .exactly()
                        .systemAndCode("http://hl7.it/fhir/sid/codiceFiscale", cf))
                .returnBundle(Bundle.class)
                .execute();

        if (bundle.getEntry().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of((Patient) bundle.getEntry().get(0).getResource());
    }

    public Optional<Patient> findById(String id) {
        try {
            Patient patient = client.read().resource(Patient.class).withId(id).execute();
            return Optional.of(patient);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
