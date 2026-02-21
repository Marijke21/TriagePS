package it.unibo.util;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.config.FhirClientConfig;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * DataSeeder - Popola il server FHIR con 25 pazienti di test.
 * 5 scenari x 5 pazienti:
 *   - ROSSO:   emergenze critiche (infarto, arresto respiratorio, ecc.)
 *   - GIALLO:  urgenze moderate (diabete scompensato, trauma, ecc.)
 *   - VERDE:   urgenze lievi (trauma minore, allergia, ecc.)
 *   - BIANCO:  accessi non urgenti (routine, controllo, ecc.)
 *   - STRANIERI: pazienti senza codice fiscale
 */
public class DataSeeder {

    private static IGenericClient client;

    public static void main(String[] args) {
        System.out.println("=== Avvio popolamento database FHIR ===");
        client = FhirClientConfig.getClient();

        // ROSSO - 5 pazienti critici
        seedRosso1();
        seedRosso2();
        seedRosso3();
        seedRosso4();
        seedRosso5();

        // GIALLO - 5 pazienti urgenti
        seedGiallo1();
        seedGiallo2();
        seedGiallo3();
        seedGiallo4();
        seedGiallo5();

        // VERDE - 5 pazienti semi-urgenti
        seedVerde1();
        seedVerde2();
        seedVerde3();
        seedVerde4();
        seedVerde5();

        // BIANCO - 5 pazienti non urgenti
        seedBianco1();
        seedBianco2();
        seedBianco3();
        seedBianco4();
        seedBianco5();

        // STRANIERI - 5 pazienti senza CF
        seedStraniero1();
        seedStraniero2();
        seedStraniero3();
        seedStraniero4();
        seedStraniero5();

        System.out.println("=== Popolamento completato: 25 pazienti inseriti ===");
    }

    // =========================================================
    // CODICE ROSSO - Emergenze critiche
    // =========================================================

    private static void seedRosso1() {
        Patient p = buildPatientCF("MRTNTN70A01H501X", "Antonio", "Martini", "1970-01-01", "male");
        String pid = savePatient(p, "Martini Antonio");
        String eid = saveEncounter(pid, "ROSSO");
        saveObs(pid, eid, "8867-4", "Heart rate", 130, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 85, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 50, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 88, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 28, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.2, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 10, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 9, "score", "{score}");
        saveAllergy(pid, "ASA - Acido acetilsalicilico");
        saveMedication(pid, "Ramipril 5mg");
        saveMedication(pid, "Atorvastatina 20mg");
    }

    private static void seedRosso2() {
        Patient p = buildPatientCF("GLLMRA65C41F205Z", "Maria", "Gallo", "1965-03-01", "female");
        String pid = savePatient(p, "Gallo Maria");
        String eid = saveEncounter(pid, "ROSSO");
        saveObs(pid, eid, "8867-4", "Heart rate", 145, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 210, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 120, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 91, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 24, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 37.0, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 12, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 8, "score", "{score}");
        saveAllergy(pid, "Penicillina");
        saveMedication(pid, "Amlodipina 10mg");
        saveMedication(pid, "Metoprololo 50mg");
    }

    private static void seedRosso3() {
        Patient p = buildPatientCF("BNCLCA55M01L219K", "Luca", "Bianchi", "1955-08-01", "male");
        String pid = savePatient(p, "Bianchi Luca");
        String eid = saveEncounter(pid, "ROSSO");
        saveObs(pid, eid, "8867-4", "Heart rate", 35, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 70, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 40, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 82, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 6, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 35.1, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 6, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 10, "score", "{score}");
        saveMedication(pid, "Warfarin 5mg");
        saveMedication(pid, "Digossina 0.25mg");
    }

    private static void seedRosso4() {
        Patient p = buildPatientCF("FRRSFN80E20H501W", "Sofia", "Ferrari", "1980-05-20", "female");
        String pid = savePatient(p, "Ferrari Sofia");
        String eid = saveEncounter(pid, "ROSSO");
        saveObs(pid, eid, "8867-4", "Heart rate", 155, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 75, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 45, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 85, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 32, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 39.8, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 8, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 9, "score", "{score}");
        saveAllergy(pid, "Lattice");
        saveAllergy(pid, "Sulfamidici");
        saveMedication(pid, "Insulina Glargine 20UI");
    }

    private static void seedRosso5() {
        Patient p = buildPatientCF("CSTMRC62D01G224F", "Marco", "Costa", "1962-04-01", "male");
        String pid = savePatient(p, "Costa Marco");
        String eid = saveEncounter(pid, "ROSSO");
        saveObs(pid, eid, "8867-4", "Heart rate", 120, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 80, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 55, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 86, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 26, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 40.2, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 7, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 8, "score", "{score}");
        saveObs(pid, eid, "2339-0", "Glucose", 420, "mg/dL", "mg/dL");
        saveMedication(pid, "Metformina 1000mg");
        saveMedication(pid, "Furosemide 25mg");
    }

    // =========================================================
    // CODICE GIALLO - Urgenze moderate
    // =========================================================

    private static void seedGiallo1() {
        Patient p = buildPatientCF("RSSMRA85M01H501Z", "Mario", "Rossi", "1985-08-01", "male");
        String pid = savePatient(p, "Rossi Mario");
        String eid = saveEncounter(pid, "GIALLO");
        saveObs(pid, eid, "8867-4", "Heart rate", 105, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 155, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 95, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 94, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 20, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 38.5, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 15, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 6, "score", "{score}");
        saveObs(pid, eid, "2339-0", "Glucose", 280, "mg/dL", "mg/dL");
        saveObs(pid, eid, "29463-7", "Body weight", 92, "kg", "kg");
        saveAllergy(pid, "Penicillina");
        saveMedication(pid, "Metformina 500mg");
        saveMedication(pid, "Lisinopril 10mg");
    }

    private static void seedGiallo2() {
        Patient p = buildPatientCF("VRDLRA90D41L219P", "Laura", "Verdi", "1990-04-01", "female");
        String pid = savePatient(p, "Verdi Laura");
        String eid = saveEncounter(pid, "GIALLO");
        saveObs(pid, eid, "8867-4", "Heart rate", 98, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 130, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 85, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 95, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 18, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 39.2, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 15, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 5, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 62, "kg", "kg");
        saveObs(pid, eid, "8302-2", "Body height", 165, "cm", "cm");
        saveMedication(pid, "Ibuprofene 400mg");
    }

    private static void seedGiallo3() {
        Patient p = buildPatientCF("NRGPTR75H01F205T", "Pietro", "Neri", "1975-06-01", "male");
        String pid = savePatient(p, "Neri Pietro");
        String eid = saveEncounter(pid, "GIALLO");
        saveObs(pid, eid, "8867-4", "Heart rate", 110, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 145, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 90, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 93, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 22, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 37.8, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 14, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 7, "score", "{score}");
        saveAllergy(pid, "FANS");
        saveMedication(pid, "Pantoprazolo 40mg");
        saveMedication(pid, "Cardioaspirina 100mg");
    }

    private static void seedGiallo4() {
        Patient p = buildPatientCF("CNTANNA68L41H501R", "Anna", "Conti", "1968-07-01", "female");
        String pid = savePatient(p, "Conti Anna");
        String eid = saveEncounter(pid, "GIALLO");
        saveObs(pid, eid, "8867-4", "Heart rate", 92, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 160, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 100, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 96, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 17, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 37.2, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 15, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 6, "score", "{score}");
        saveObs(pid, eid, "2339-0", "Glucose", 195, "mg/dL", "mg/dL");
        saveMedication(pid, "Levotiroxina 75mcg");
        saveMedication(pid, "Candesartan 8mg");
    }

    private static void seedGiallo5() {
        Patient p = buildPatientCF("LMBGNN82R01L219H", "Giovanni", "Lombardi", "1982-10-01", "male");
        String pid = savePatient(p, "Lombardi Giovanni");
        String eid = saveEncounter(pid, "GIALLO");
        saveObs(pid, eid, "8867-4", "Heart rate", 115, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 140, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 88, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 92, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 23, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 38.9, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 13, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 7, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 85, "kg", "kg");
        saveAllergy(pid, "Cefalosporine");
        saveMedication(pid, "Salbutamolo spray");
        saveMedication(pid, "Beclometasone spray");
    }

    // =========================================================
    // CODICE VERDE - Urgenze lievi
    // =========================================================

    private static void seedVerde1() {
        Patient p = buildPatientCF("BRNGPP95C14H501A", "Giuseppe", "Bruno", "1995-03-14", "male");
        String pid = savePatient(p, "Bruno Giuseppe");
        String eid = saveEncounter(pid, "VERDE");
        saveObs(pid, eid, "8867-4", "Heart rate", 88, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 125, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 80, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 98, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 16, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 37.5, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 4, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 78, "kg", "kg");
        saveObs(pid, eid, "8302-2", "Body height", 180, "cm", "cm");
        saveAllergy(pid, "Penicillina");
    }

    private static void seedVerde2() {
        Patient p = buildPatientCF("MNZFNC88H41L219B", "Francesca", "Monza", "1988-06-01", "female");
        String pid = savePatient(p, "Monza Francesca");
        String eid = saveEncounter(pid, "VERDE");
        saveObs(pid, eid, "8867-4", "Heart rate", 82, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 118, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 75, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 99, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 15, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 38.1, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 3, "score", "{score}");
        saveMedication(pid, "Acido folico 400mcg");
    }

    private static void seedVerde3() {
        Patient p = buildPatientCF("SRNCRL92A01F205C", "Carlo", "Sorrentino", "1992-01-01", "male");
        String pid = savePatient(p, "Sorrentino Carlo");
        String eid = saveEncounter(pid, "VERDE");
        saveObs(pid, eid, "8867-4", "Heart rate", 76, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 122, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 78, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 98, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 14, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.8, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 4, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 72, "kg", "kg");
    }

    private static void seedVerde4() {
        Patient p = buildPatientCF("RSTELN70D41H501D", "Elena", "Russo", "1970-04-01", "female");
        String pid = savePatient(p, "Russo Elena");
        String eid = saveEncounter(pid, "VERDE");
        saveObs(pid, eid, "8867-4", "Heart rate", 80, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 128, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 82, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 97, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 16, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 37.0, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 3, "score", "{score}");
        saveAllergy(pid, "Nichel");
        saveMedication(pid, "Loratadina 10mg");
    }

    private static void seedVerde5() {
        Patient p = buildPatientCF("MRNNDR00A01H501E", "Andrea", "Marini", "2000-01-01", "male");
        String pid = savePatient(p, "Marini Andrea");
        String eid = saveEncounter(pid, "VERDE");
        saveObs(pid, eid, "8867-4", "Heart rate", 90, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 120, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 76, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 98, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 15, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 37.3, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 2, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 70, "kg", "kg");
        saveObs(pid, eid, "8302-2", "Body height", 175, "cm", "cm");
    }

    // =========================================================
    // CODICE BIANCO - Accessi non urgenti
    // =========================================================

    private static void seedBianco1() {
        Patient p = buildPatientCF("FLPPTR55D01L219F", "Pietro", "Filippi", "1955-04-01", "male");
        String pid = savePatient(p, "Filippi Pietro");
        String eid = saveEncounter(pid, "BIANCO");
        saveObs(pid, eid, "8867-4", "Heart rate", 72, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 130, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 80, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 98, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 14, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.6, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 1, "score", "{score}");
        saveMedication(pid, "Ramipril 2.5mg");
    }

    private static void seedBianco2() {
        Patient p = buildPatientCF("GRNMRA78L41F205G", "Maria", "Grandi", "1978-07-01", "female");
        String pid = savePatient(p, "Grandi Maria");
        String eid = saveEncounter(pid, "BIANCO");
        saveObs(pid, eid, "8867-4", "Heart rate", 68, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 115, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 72, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 99, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 13, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.5, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 1, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 58, "kg", "kg");
        saveObs(pid, eid, "8302-2", "Body height", 162, "cm", "cm");
    }

    private static void seedBianco3() {
        Patient p = buildPatientCF("PLLMRC65H01H501I", "Marco", "Palladino", "1965-06-01", "male");
        String pid = savePatient(p, "Palladino Marco");
        String eid = saveEncounter(pid, "BIANCO");
        saveObs(pid, eid, "8867-4", "Heart rate", 74, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 125, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 78, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 98, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 14, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.7, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 2, "score", "{score}");
        saveMedication(pid, "Omeprazolo 20mg");
    }

    private static void seedBianco4() {
        Patient p = buildPatientCF("TRRSVL92C41L219L", "Silvia", "Torresi", "1992-03-01", "female");
        String pid = savePatient(p, "Torresi Silvia");
        String eid = saveEncounter(pid, "BIANCO");
        saveObs(pid, eid, "8867-4", "Heart rate", 70, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 110, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 70, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 99, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 13, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.4, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 1, "score", "{score}");
    }

    private static void seedBianco5() {
        Patient p = buildPatientCF("DMNCSR88B01F205M", "Cesare", "Damiani", "1988-02-01", "male");
        String pid = savePatient(p, "Damiani Cesare");
        String eid = saveEncounter(pid, "BIANCO");
        saveObs(pid, eid, "8867-4", "Heart rate", 66, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 118, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 74, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 99, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 13, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.6, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 1, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 80, "kg", "kg");
        saveObs(pid, eid, "8302-2", "Body height", 178, "cm", "cm");
    }

    // =========================================================
    // STRANIERI - Pazienti senza codice fiscale
    // =========================================================

    private static void seedStraniero1() {
        Patient p = buildPatientStraniero("James", "Smith", "1990-03-15", "male",
                "Passaporto", "GB12345678", "Regno Unito", "Britannica", "+44 7911123456", "james.smith@email.co.uk");
        String pid = savePatient(p, "Smith James");
        String eid = saveEncounter(pid, "GIALLO");
        saveObs(pid, eid, "8867-4", "Heart rate", 95, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 138, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 88, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 96, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 18, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 38.3, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 5, "score", "{score}");
        saveAllergy(pid, "Penicillina");
    }

    private static void seedStraniero2() {
        Patient p = buildPatientStraniero("Marie", "Dupont", "1985-07-22", "female",
                "Passaporto", "FR98765432", "Francia", "Francese", "+33 612345678", "marie.dupont@email.fr");
        String pid = savePatient(p, "Dupont Marie");
        String eid = saveEncounter(pid, "VERDE");
        saveObs(pid, eid, "8867-4", "Heart rate", 80, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 120, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 76, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 98, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 15, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 37.1, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 3, "score", "{score}");
        saveMedication(pid, "Contraceptif oral");
    }

    private static void seedStraniero3() {
        Patient p = buildPatientStraniero("Ahmed", "Al-Rashid", "1978-11-05", "male",
                "Passaporto", "AE11223344", "Emirati Arabi", "Emiratina", "+971 501234567", "ahmed.alrashid@email.ae");
        String pid = savePatient(p, "Al-Rashid Ahmed");
        String eid = saveEncounter(pid, "ROSSO");
        saveObs(pid, eid, "8867-4", "Heart rate", 135, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 90, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 60, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 87, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 28, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 39.5, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 11, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 8, "score", "{score}");
        saveObs(pid, eid, "2339-0", "Glucose", 380, "mg/dL", "mg/dL");
        saveMedication(pid, "Insulina Aspart");
        saveAllergy(pid, "Sulfamidici");
    }

    private static void seedStraniero4() {
        Patient p = buildPatientStraniero("Yuki", "Tanaka", "2000-05-18", "female",
                "Passaporto", "JP55667788", "Giappone", "Giapponese", "+81 9012345678", "yuki.tanaka@email.jp");
        String pid = savePatient(p, "Tanaka Yuki");
        String eid = saveEncounter(pid, "BIANCO");
        saveObs(pid, eid, "8867-4", "Heart rate", 65, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 108, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 68, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 99, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 13, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 36.5, "°C", "Cel");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 2, "score", "{score}");
        saveObs(pid, eid, "29463-7", "Body weight", 50, "kg", "kg");
        saveObs(pid, eid, "8302-2", "Body height", 158, "cm", "cm");
    }

    private static void seedStraniero5() {
        Patient p = buildPatientStraniero("Carlos", "Garcia", "1972-09-30", "male",
                "Passaporto", "MX99887766", "Messico", "Messicana", "+52 5512345678", "carlos.garcia@email.mx");
        String pid = savePatient(p, "Garcia Carlos");
        String eid = saveEncounter(pid, "GIALLO");
        saveObs(pid, eid, "8867-4", "Heart rate", 100, "bpm", "/min");
        saveObs(pid, eid, "8480-6", "Systolic blood pressure", 148, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "8462-4", "Diastolic blood pressure", 92, "mmHg", "mm[Hg]");
        saveObs(pid, eid, "59408-5", "Oxygen saturation", 94, "%", "%");
        saveObs(pid, eid, "9279-1", "Respiratory rate", 20, "atti/min", "/min");
        saveObs(pid, eid, "8310-5", "Body temperature", 38.7, "°C", "Cel");
        saveObs(pid, eid, "9269-2", "Glasgow coma score", 15, "score", "{score}");
        saveObs(pid, eid, "72514-3", "Pain severity NRS", 6, "score", "{score}");
        saveAllergy(pid, "Lattice");
        saveMedication(pid, "Enalapril 10mg");
        saveMedication(pid, "Metformina 850mg");
    }

    // =========================================================
    // METODI DI SUPPORTO
    // =========================================================

    private static Patient buildPatientCF(String cf, String nome, String cognome,
                                           String birthDate, String gender) {
        Patient p = new Patient();
        p.addIdentifier()
                .setSystem("http://hl7.it/fhir/sid/codiceFiscale")
                .setValue(cf);
        p.addName().setFamily(cognome).addGiven(nome);
        p.setBirthDateElement(new DateType(birthDate));
        p.setGender(gender.equals("male")
                ? Enumerations.AdministrativeGender.MALE
                : Enumerations.AdministrativeGender.FEMALE);
        return p;
    }

    private static Patient buildPatientStraniero(String nome, String cognome, String birthDate,
                                                   String gender, String tipoDoc, String numeroDoc,
                                                   String paeseDoc, String nazionalita,
                                                   String telefono, String email) {
        Patient p = new Patient();
        p.addIdentifier()
                .setSystem("http://pronto.local/documento/" + tipoDoc.toLowerCase())
                .setValue(numeroDoc)
                .getType().addCoding()
                .setSystem("http://pronto.local/tipoDocumento")
                .setCode(tipoDoc)
                .setDisplay(paeseDoc);
        p.addName().setFamily(cognome).addGiven(nome);
        p.setBirthDateElement(new DateType(birthDate));
        p.setGender(gender.equals("male")
                ? Enumerations.AdministrativeGender.MALE
                : Enumerations.AdministrativeGender.FEMALE);

        Extension ext = new Extension();
        ext.setUrl("http://hl7.org/fhir/StructureDefinition/patient-nationality");
        ext.setValue(new StringType(nazionalita));
        p.addExtension(ext);

        p.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(telefono);
        p.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(email);
        return p;
    }

    private static String savePatient(Patient patient, String nome) {
        MethodOutcome outcome = client.create().resource(patient).execute();
        String id = outcome.getId().getIdPart();
        System.out.println("✔ Paziente creato: " + nome + " (ID: " + id + ")");
        return id;
    }

    private static String saveEncounter(String patientId, String triage) {
        Encounter enc = new Encounter();
        enc.setStatus(Encounter.EncounterStatus.INPROGRESS);
        enc.setClass_(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode")
                .setCode("EMER").setDisplay("Emergency"));
        enc.setSubject(new Reference("Patient/" + patientId));
        enc.setPeriod(new Period().setStart(new Date()));
        enc.setPriority(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://pronto.local/triage")
                .setCode(triage.toLowerCase())
                .setDisplay("Codice " + capitalize(triage))));

        MethodOutcome outcome = client.create().resource(enc).execute();
        String id = outcome.getId().getIdPart();
        System.out.println("  → Encounter creato: Codice " + triage + " (ID: " + id + ")");
        return id;
    }

    private static void saveObs(String patientId, String encounterId,
                                 String loinc, String display, double value,
                                 String unit, String ucum) {
        Observation obs = new Observation();
        obs.setStatus(Observation.ObservationStatus.FINAL);
        obs.addCategory().addCoding()
                .setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
                .setCode("vital-signs");
        obs.getCode().addCoding()
                .setSystem("http://loinc.org").setCode(loinc).setDisplay(display);
        obs.setSubject(new Reference("Patient/" + patientId));
        obs.setEncounter(new Reference("Encounter/" + encounterId));
        obs.setEffective(new DateTimeType(new Date()));
        obs.setValue(new Quantity()
                .setValue(BigDecimal.valueOf(value))
                .setUnit(unit)
                .setSystem("http://unitsofmeasure.org")
                .setCode(ucum));
        client.create().resource(obs).execute();
    }

    private static void saveAllergy(String patientId, String text) {
        AllergyIntolerance allergy = new AllergyIntolerance();
        allergy.setClinicalStatus(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
                .setCode("active")));
        allergy.setType(AllergyIntolerance.AllergyIntoleranceType.ALLERGY);
        allergy.getCode().setText(text);
        allergy.setPatient(new Reference("Patient/" + patientId));
        client.create().resource(allergy).execute();
    }

    private static void saveMedication(String patientId, String text) {
        MedicationStatement med = new MedicationStatement();
        med.setStatus(MedicationStatement.MedicationStatementStatus.ACTIVE);
        med.setMedication(new CodeableConcept().setText(text));
        med.setSubject(new Reference("Patient/" + patientId));
        client.create().resource(med).execute();
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
