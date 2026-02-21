package it.unibo.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

public class FhirClientConfig {

    private static final String SERVER_URL = "http://localhost:8080/fhir";
    private static FhirContext ctx;
    private static IGenericClient client;

    public static FhirContext getContext() {
        if (ctx == null) {
            ctx = FhirContext.forR4();
            ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
            ctx.getRestfulClientFactory().setConnectTimeout(10000);
            ctx.getRestfulClientFactory().setSocketTimeout(15000);
        }
        return ctx;
    }

    public static IGenericClient getClient() {
        if (client == null) {
            client = getContext().newRestfulGenericClient(SERVER_URL);
        }
        return client;
    }

    public static String getServerUrl() {
        return SERVER_URL;
    }
}
