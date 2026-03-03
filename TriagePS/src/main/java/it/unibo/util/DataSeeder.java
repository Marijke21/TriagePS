package it.unibo.util;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.config.FhirClientConfig;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DataSeeder - Popola il server FHIR con 25 pazienti di test gia in carico al PS.
 * Ogni paziente ha: Patient + Encounter (in-progress con orario diverso) + Observation.
 * Eseguire una sola volta con Docker attivo su localhost:8080.
 */
public class DataSeeder {

    private static IGenericClient client;

    public static void main(String[] args) {
        System.out.println("=== Avvio popolamento database FHIR ===");
        client = FhirClientConfig.getClient();

        // ROSSO - arrivati 8, 15, 22, 31, 43 minuti fa
        seedRosso1(8);   seedRosso2(15);  seedRosso3(22);
        seedRosso4(31);  seedRosso5(43);

        // GIALLO - arrivati 12, 29, 47, 66, 85 minuti fa
        seedGiallo1(12); seedGiallo2(29); seedGiallo3(47);
        seedGiallo4(66); seedGiallo5(85);

        // VERDE - arrivati 35, 58, 74, 97, 110 minuti fa
        seedVerde1(35);  seedVerde2(58);  seedVerde3(74);
        seedVerde4(97);  seedVerde5(110);

        // BIANCO - arrivati 55, 83, 105, 132, 168 minuti fa
        seedBianco1(55); seedBianco2(83); seedBianco3(105);
        seedBianco4(132); seedBianco5(168);

        // STRANIERI - arrivati 19, 52, 11, 145, 71 minuti fa
        seedStraniero1(19); seedStraniero2(52); seedStraniero3(11);
        seedStraniero4(145); seedStraniero5(71);

        System.out.println("=== Popolamento completato: 25 pazienti inseriti ===");
    }

    private static Date minutiFa(int minuti) {
        return new Date(System.currentTimeMillis() - (long) minuti * 60 * 1000);
    }

    // =========================================================
    // ROSSO
    // =========================================================

    private static void seedRosso1(int min) {
        String pid = savePatient(cf("MRTNTN70A01H501X","Antonio","Martini","1970-01-01","male"), "Martini Antonio");
        String eid = saveEncounter(pid, "ROSSO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",130,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",85,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",50,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",88,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",28,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",36.2,"grC","Cel");
        saveObs(pid,eid,"9269-2","Glasgow coma score",10,"score","{score}");
        saveObs(pid,eid,"72514-3","Pain severity NRS",9,"score","{score}");
        saveAllergy(pid,"ASA - Acido acetilsalicilico");
        saveMed(pid,"Ramipril 5mg"); saveMed(pid,"Atorvastatina 20mg");
    }

    private static void seedRosso2(int min) {
        String pid = savePatient(cf("GLLMRA65C41F205Z","Maria","Gallo","1965-03-01","female"), "Gallo Maria");
        String eid = saveEncounter(pid, "ROSSO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",145,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",210,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",120,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",91,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",24,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",37.0,"grC","Cel");
        saveObs(pid,eid,"9269-2","Glasgow coma score",12,"score","{score}");
        saveObs(pid,eid,"72514-3","Pain severity NRS",8,"score","{score}");
        saveAllergy(pid,"Penicillina");
        saveMed(pid,"Amlodipina 10mg"); saveMed(pid,"Metoprololo 50mg");
    }

    private static void seedRosso3(int min) {
        String pid = savePatient(cf("BNCLCA55M01L219K","Luca","Bianchi","1955-08-01","male"), "Bianchi Luca");
        String eid = saveEncounter(pid, "ROSSO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",35,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",70,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",40,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",82,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",6,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",35.1,"grC","Cel");
        saveObs(pid,eid,"9269-2","Glasgow coma score",6,"score","{score}");
        saveObs(pid,eid,"72514-3","Pain severity NRS",10,"score","{score}");
        saveMed(pid,"Warfarin 5mg"); saveMed(pid,"Digossina 0.25mg");
    }

    private static void seedRosso4(int min) {
        String pid = savePatient(cf("FRRSFN80E20H501W","Sofia","Ferrari","1980-05-20","female"), "Ferrari Sofia");
        String eid = saveEncounter(pid, "ROSSO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",155,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",75,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",45,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",85,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",32,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",39.8,"grC","Cel");
        saveObs(pid,eid,"9269-2","Glasgow coma score",8,"score","{score}");
        saveObs(pid,eid,"72514-3","Pain severity NRS",9,"score","{score}");
        saveAllergy(pid,"Lattice"); saveAllergy(pid,"Sulfamidici");
        saveMed(pid,"Insulina Glargine 20UI");
    }

    private static void seedRosso5(int min) {
        String pid = savePatient(cf("CSTMRC62D01G224F","Marco","Costa","1962-04-01","male"), "Costa Marco");
        String eid = saveEncounter(pid, "ROSSO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",120,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",80,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",55,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",86,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",26,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",40.2,"grC","Cel");
        saveObs(pid,eid,"9269-2","Glasgow coma score",7,"score","{score}");
        saveObs(pid,eid,"72514-3","Pain severity NRS",8,"score","{score}");
        saveObs(pid,eid,"2339-0","Glucose",420,"mg/dL","mg/dL");
        saveMed(pid,"Metformina 1000mg"); saveMed(pid,"Furosemide 25mg");
    }

    // =========================================================
    // GIALLO
    // =========================================================

    private static void seedGiallo1(int min) {
        String pid = savePatient(cf("RSSMRA85M01H501Z","Mario","Rossi","1985-08-01","male"), "Rossi Mario");
        String eid = saveEncounter(pid, "GIALLO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",105,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",155,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",95,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",94,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",20,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",38.5,"grC","Cel");
        saveObs(pid,eid,"9269-2","Glasgow coma score",15,"score","{score}");
        saveObs(pid,eid,"72514-3","Pain severity NRS",6,"score","{score}");
        saveObs(pid,eid,"2339-0","Glucose",280,"mg/dL","mg/dL");
        saveAllergy(pid,"Penicillina"); saveMed(pid,"Metformina 500mg");
    }

    private static void seedGiallo2(int min) {
        String pid = savePatient(cf("VRDLRA90D41L219P","Laura","Verdi","1990-04-01","female"), "Verdi Laura");
        String eid = saveEncounter(pid, "GIALLO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",98,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",130,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",85,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",95,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",18,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",39.2,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",5,"score","{score}");
        saveMed(pid,"Ibuprofene 400mg");
    }

    private static void seedGiallo3(int min) {
        String pid = savePatient(cf("NRGPTR75H01F205T","Pietro","Neri","1975-06-01","male"), "Neri Pietro");
        String eid = saveEncounter(pid, "GIALLO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",110,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",145,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",90,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",93,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",22,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",37.8,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",7,"score","{score}");
        saveAllergy(pid,"FANS"); saveMed(pid,"Cardioaspirina 100mg");
    }

    private static void seedGiallo4(int min) {
        String pid = savePatient(cf("CNTANNA68L41H501R","Anna","Conti","1968-07-01","female"), "Conti Anna");
        String eid = saveEncounter(pid, "GIALLO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",92,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",160,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",100,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",96,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",17,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",37.2,"grC","Cel");
        saveObs(pid,eid,"2339-0","Glucose",195,"mg/dL","mg/dL");
        saveMed(pid,"Levotiroxina 75mcg"); saveMed(pid,"Candesartan 8mg");
    }

    private static void seedGiallo5(int min) {
        String pid = savePatient(cf("LMBGNN82R01L219H","Giovanni","Lombardi","1982-10-01","male"), "Lombardi Giovanni");
        String eid = saveEncounter(pid, "GIALLO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",115,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",140,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",88,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",92,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",23,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",38.9,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",7,"score","{score}");
        saveAllergy(pid,"Cefalosporine"); saveMed(pid,"Salbutamolo spray");
    }

    // =========================================================
    // VERDE
    // =========================================================

    private static void seedVerde1(int min) {
        String pid = savePatient(cf("BRNGPP95C14H501A","Giuseppe","Bruno","1995-03-14","male"), "Bruno Giuseppe");
        String eid = saveEncounter(pid, "VERDE", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",88,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",125,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",80,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",98,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",16,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",37.5,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",4,"score","{score}");
        saveAllergy(pid,"Penicillina");
    }

    private static void seedVerde2(int min) {
        String pid = savePatient(cf("MNZFNC88H41L219B","Francesca","Monza","1988-06-01","female"), "Monza Francesca");
        String eid = saveEncounter(pid, "VERDE", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",82,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",118,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",75,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",99,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",38.1,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",3,"score","{score}");
        saveMed(pid,"Acido folico 400mcg");
    }

    private static void seedVerde3(int min) {
        String pid = savePatient(cf("SRNCRL92A01F205C","Carlo","Sorrentino","1992-01-01","male"), "Sorrentino Carlo");
        String eid = saveEncounter(pid, "VERDE", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",76,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",122,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",78,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",98,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",36.8,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",4,"score","{score}");
        saveObs(pid,eid,"29463-7","Body weight",72,"kg","kg");
    }

    private static void seedVerde4(int min) {
        String pid = savePatient(cf("RSTELN70D41H501D","Elena","Russo","1970-04-01","female"), "Russo Elena");
        String eid = saveEncounter(pid, "VERDE", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",80,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",128,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",82,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",97,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",37.0,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",3,"score","{score}");
        saveAllergy(pid,"Nichel"); saveMed(pid,"Loratadina 10mg");
    }

    private static void seedVerde5(int min) {
        String pid = savePatient(cf("MRNNDR00A01H501E","Andrea","Marini","2000-01-01","male"), "Marini Andrea");
        String eid = saveEncounter(pid, "VERDE", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",90,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",120,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",76,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",98,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",37.3,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",2,"score","{score}");
        saveObs(pid,eid,"29463-7","Body weight",70,"kg","kg");
        saveObs(pid,eid,"8302-2","Body height",175,"cm","cm");
    }

    // =========================================================
    // BIANCO
    // =========================================================

    private static void seedBianco1(int min) {
        String pid = savePatient(cf("FLPPTR55D01L219F","Pietro","Filippi","1955-04-01","male"), "Filippi Pietro");
        String eid = saveEncounter(pid, "BIANCO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",72,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",130,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",80,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",98,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",36.6,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",1,"score","{score}");
        saveMed(pid,"Ramipril 2.5mg");
    }

    private static void seedBianco2(int min) {
        String pid = savePatient(cf("GRNMRA78L41F205G","Maria","Grandi","1978-07-01","female"), "Grandi Maria");
        String eid = saveEncounter(pid, "BIANCO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",68,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",115,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",72,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",99,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",36.5,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",1,"score","{score}");
        saveObs(pid,eid,"29463-7","Body weight",58,"kg","kg");
    }

    private static void seedBianco3(int min) {
        String pid = savePatient(cf("PLLMRC65H01H501I","Marco","Palladino","1965-06-01","male"), "Palladino Marco");
        String eid = saveEncounter(pid, "BIANCO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",74,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",125,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",78,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",98,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",36.7,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",2,"score","{score}");
        saveMed(pid,"Omeprazolo 20mg");
    }

    private static void seedBianco4(int min) {
        String pid = savePatient(cf("TRRSVL92C41L219L","Silvia","Torresi","1992-03-01","female"), "Torresi Silvia");
        String eid = saveEncounter(pid, "BIANCO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",70,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",110,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",70,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",99,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",36.4,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",1,"score","{score}");
    }

    private static void seedBianco5(int min) {
        String pid = savePatient(cf("DMNCSR88B01F205M","Cesare","Damiani","1988-02-01","male"), "Damiani Cesare");
        String eid = saveEncounter(pid, "BIANCO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",66,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",118,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",74,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",99,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",36.6,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",1,"score","{score}");
        saveObs(pid,eid,"29463-7","Body weight",80,"kg","kg");
    }

    // =========================================================
    // STRANIERI
    // =========================================================

    private static void seedStraniero1(int min) {
        String pid = savePatient(straniero("James","Smith","1990-03-15","male",
            "Passaporto","GB12345678","Regno Unito","Britannica","+44 7911123456","j.smith@email.co.uk"), "Smith James");
        String eid = saveEncounter(pid, "GIALLO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",95,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",138,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",88,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",96,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",38.3,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",5,"score","{score}");
        saveAllergy(pid,"Penicillina");
    }

    private static void seedStraniero2(int min) {
        String pid = savePatient(straniero("Marie","Dupont","1985-07-22","female",
            "Passaporto","FR98765432","Francia","Francese","+33 612345678","m.dupont@email.fr"), "Dupont Marie");
        String eid = saveEncounter(pid, "VERDE", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",80,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",120,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",98,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",37.1,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",3,"score","{score}");
    }

    private static void seedStraniero3(int min) {
        String pid = savePatient(straniero("Ahmed","Al-Rashid","1978-11-05","male",
            "Passaporto","AE11223344","Emirati Arabi","Emiratina","+971 501234567","a.alrashid@email.ae"), "Al-Rashid Ahmed");
        String eid = saveEncounter(pid, "ROSSO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",135,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",90,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",60,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",87,"pct","%");
        saveObs(pid,eid,"9279-1","Respiratory rate",28,"atti/min","/min");
        saveObs(pid,eid,"8310-5","Body temperature",39.5,"grC","Cel");
        saveObs(pid,eid,"9269-2","Glasgow coma score",11,"score","{score}");
        saveObs(pid,eid,"72514-3","Pain severity NRS",8,"score","{score}");
        saveObs(pid,eid,"2339-0","Glucose",380,"mg/dL","mg/dL");
        saveAllergy(pid,"Sulfamidici"); saveMed(pid,"Insulina Aspart");
    }

    private static void seedStraniero4(int min) {
        String pid = savePatient(straniero("Yuki","Tanaka","2000-05-18","female",
            "Passaporto","JP55667788","Giappone","Giapponese","+81 9012345678","y.tanaka@email.jp"), "Tanaka Yuki");
        String eid = saveEncounter(pid, "BIANCO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",65,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",108,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",99,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",36.5,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",2,"score","{score}");
        saveObs(pid,eid,"29463-7","Body weight",50,"kg","kg");
    }

    private static void seedStraniero5(int min) {
        String pid = savePatient(straniero("Carlos","Garcia","1972-09-30","male",
            "Passaporto","MX99887766","Messico","Messicana","+52 5512345678","c.garcia@email.mx"), "Garcia Carlos");
        String eid = saveEncounter(pid, "GIALLO", minutiFa(min));
        saveObs(pid,eid,"8867-4","Heart rate",100,"bpm","/min");
        saveObs(pid,eid,"8480-6","Systolic blood pressure",148,"mmHg","mm[Hg]");
        saveObs(pid,eid,"8462-4","Diastolic blood pressure",92,"mmHg","mm[Hg]");
        saveObs(pid,eid,"59408-5","Oxygen saturation",94,"pct","%");
        saveObs(pid,eid,"8310-5","Body temperature",38.7,"grC","Cel");
        saveObs(pid,eid,"72514-3","Pain severity NRS",6,"score","{score}");
        saveAllergy(pid,"Lattice"); saveMed(pid,"Enalapril 10mg");
    }

    // =========================================================
    // METODI DI SUPPORTO
    // =========================================================

    private static Patient cf(String cf, String nome, String cognome,
                               String birth, String gender) {
        Patient p = new Patient();
        p.addIdentifier().setSystem("http://hl7.it/fhir/sid/codiceFiscale").setValue(cf);
        p.addName().setFamily(cognome).addGiven(nome);
        p.setBirthDateElement(new DateType(birth));
        p.setGender("male".equals(gender)
            ? Enumerations.AdministrativeGender.MALE
            : Enumerations.AdministrativeGender.FEMALE);
        return p;
    }

    private static Patient straniero(String nome, String cognome, String birth, String gender,
                                      String tipoDoc, String numDoc, String paese, String naz,
                                      String tel, String email) {
        Patient p = new Patient();
        p.addIdentifier()
            .setSystem("http://pronto.local/documento/" + tipoDoc.toLowerCase())
            .setValue(numDoc)
            .getType().addCoding()
            .setSystem("http://pronto.local/tipoDocumento")
            .setCode(tipoDoc).setDisplay(paese);
        p.addName().setFamily(cognome).addGiven(nome);
        p.setBirthDateElement(new DateType(birth));
        p.setGender("male".equals(gender)
            ? Enumerations.AdministrativeGender.MALE
            : Enumerations.AdministrativeGender.FEMALE);
        p.addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/patient-nationality")
            .setValue(new StringType(naz));
        p.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(tel);
        p.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(email);
        return p;
    }

    private static String savePatient(Patient p, String nome) {
        MethodOutcome o = client.create().resource(p).execute();
        String id = o.getId().getIdPart();
        System.out.println("Paziente: " + nome + " (ID: " + id + ")");
        return id;
    }

    private static String saveEncounter(String pid, String triage, Date start) {
        Encounter e = new Encounter();
        e.setStatus(Encounter.EncounterStatus.INPROGRESS);
        e.setClass_(new Coding()
            .setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode")
            .setCode("EMER").setDisplay("Emergency"));
        e.setSubject(new Reference("Patient/" + pid));
        e.setPeriod(new Period().setStart(start));
        e.setPriority(new CodeableConcept().addCoding(new Coding()
            .setSystem("http://pronto.local/triage")
            .setCode(triage.toLowerCase())
            .setDisplay("Codice " + capitalize(triage))));
        MethodOutcome o = client.create().resource(e).execute();
        String id = o.getId().getIdPart();
        System.out.println("  -> Encounter " + triage + " (ID: " + id + ", " + start + ")");
        return id;
    }

    private static void saveObs(String pid, String eid, String loinc, String display,
                                  double value, String unit, String ucum) {
        Observation o = new Observation();
        o.setStatus(Observation.ObservationStatus.FINAL);
        o.addCategory().addCoding()
            .setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
            .setCode("vital-signs");
        o.getCode().addCoding().setSystem("http://loinc.org").setCode(loinc).setDisplay(display);
        o.setSubject(new Reference("Patient/" + pid));
        o.setEncounter(new Reference("Encounter/" + eid));
        o.setEffective(new DateTimeType(new Date()));
        o.setValue(new Quantity().setValue(BigDecimal.valueOf(value))
            .setUnit(unit).setSystem("http://unitsofmeasure.org").setCode(ucum));
        client.create().resource(o).execute();
    }

    private static void saveAllergy(String pid, String text) {
        AllergyIntolerance a = new AllergyIntolerance();
        a.setClinicalStatus(new CodeableConcept().addCoding(new Coding()
            .setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
            .setCode("active")));
        a.setType(AllergyIntolerance.AllergyIntoleranceType.ALLERGY);
        a.getCode().setText(text);
        a.setPatient(new Reference("Patient/" + pid));
        client.create().resource(a).execute();
    }

    private static void saveMed(String pid, String text) {
        MedicationStatement m = new MedicationStatement();
        m.setStatus(MedicationStatement.MedicationStatementStatus.ACTIVE);
        m.setMedication(new CodeableConcept().setText(text));
        m.setSubject(new Reference("Patient/" + pid));
        client.create().resource(m).execute();
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }
}