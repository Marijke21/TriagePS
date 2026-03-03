package it.unibo.util;

import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import it.unibo.config.FhirClientConfig;

/**
 * DataSeederPatient - Simula un nodo esterno (es. FSE, anagrafe sanitaria regionale)
 * che trasmette al server FHIR del PS i dati anagrafici di 30 pazienti.
 * Questi pazienti NON hanno ancora Encounter ne Observation: rappresentano
 * persone registrate nel sistema sanitario nazionale che potrebbero accedere
 * al pronto soccorso. Quando si presentano allo sportello, l'operatore cerca
 * il loro codice fiscale e avvia il triage.
 *
 * Questo scenario dimostra concretamente l'interoperabilita FHIR tra sistemi
 * eterogenei: il PS riceve risorse Patient gia strutturate da un sistema esterno
 * e le utilizza senza necessita di re-inserimento manuale dei dati.
 *
 * Eseguire una sola volta con Docker attivo su localhost:8080.
 */
public class DataSeederPatient {

    private static IGenericClient client;

    public static void main(String[] args) {
        System.out.println("=== Simulazione nodo esterno: importazione 30 pazienti ===");
        client = FhirClientConfig.getClient();

        // Italiani - 20 pazienti con codice fiscale
        seedItaliano("BNCMRC80A01H501Q", "Marco",     "Bianchi",    "1980-01-01", "male");
        seedItaliano("VRDLGI75B41L219R", "Giulia",    "Verdi",      "1975-02-01", "female");
        seedItaliano("RSSFNC92C14F205S", "Francesco", "Rossi",      "1992-03-14", "male");
        seedItaliano("MRRGVN88D41H501T", "Giovanna",  "Marri",      "1988-04-01", "female");
        seedItaliano("CLDPTR65E01L219U", "Pietro",    "Caldi",      "1965-05-01", "male");
        seedItaliano("FNTSMN70F41F205V", "Simona",    "Fanti",      "1970-06-01", "female");
        seedItaliano("GRZMTT85G01H501W", "Matteo",    "Grazi",      "1985-07-01", "male");
        seedItaliano("PLMBRT90H41L219X", "Roberta",   "Palmi",      "1990-08-01", "female");
        seedItaliano("TRNLCA55I01F205Y", "Luca",      "Trani",      "1955-09-01", "male");
        seedItaliano("SLVCST78L41H501Z", "Cristina",  "Salvi",      "1978-10-01", "female");
        seedItaliano("MNZLRT62M01L219A", "Lorenzo",   "Monzi",      "1962-11-01", "male");
        seedItaliano("CPRFNC95N41F205B", "Federica",  "Capri",      "1995-12-01", "female");
        seedItaliano("BRNNDR83P01H501C", "Andrea",    "Bruni",      "1983-01-15", "male");
        seedItaliano("GLLVLR69Q41L219D", "Valeria",   "Galli",      "1969-02-20", "female");
        seedItaliano("FRRPQL77R01F205E", "Pasquale",  "Ferraro",    "1977-03-10", "male");
        seedItaliano("NGRLSN91S41H501F", "Alessia",   "Negri",      "1991-04-05", "female");
        seedItaliano("CSTDNL58T01L219G", "Daniele",   "Castelli",   "1958-05-22", "male");
        seedItaliano("MRNNNA86U41F205H", "Anna",      "Morini",     "1986-06-18", "female");
        seedItaliano("PLLSDR73V01H501I", "Sandro",    "Pollini",    "1973-07-30", "male");
        seedItaliano("BNCLRA97W41L219L", "Laura",     "Benci",      "1997-08-12", "female");

        // Stranieri - 10 pazienti con documento di identita estero
        seedStraniero("Thomas",   "Mueller",    "1982-04-12", "male",
            "Passaporto", "DE44556677", "Germania",    "Tedesca",
            "+49 15112345678", "t.mueller@email.de");

        seedStraniero("Sophie",   "Leblanc",    "1994-08-23", "female",
            "Passaporto", "FR33221100", "Francia",     "Francese",
            "+33 698765432",  "s.leblanc@email.fr");

        seedStraniero("Hiroshi",  "Yamamoto",   "1971-12-01", "male",
            "Passaporto", "JP99001122", "Giappone",    "Giapponese",
            "+81 8098765432", "h.yamamoto@email.jp");

        seedStraniero("Ana",      "Rodriguez",  "1988-03-17", "female",
            "Passaporto", "ES77889900", "Spagna",      "Spagnola",
            "+34 612345678",  "a.rodriguez@email.es");

        seedStraniero("William",  "Johnson",    "1965-07-04", "male",
            "Passaporto", "US11223344", "Stati Uniti", "Americana",
            "+1 2025551234",  "w.johnson@email.us");

        seedStraniero("Fatima",   "Al-Mansouri","1990-10-30", "female",
            "Passaporto", "AE55443322", "Emirati Arabi","Emiratina",
            "+971 509876543", "f.almansouri@email.ae");

        seedStraniero("Ivan",     "Petrov",     "1979-02-14", "male",
            "Passaporto", "RU66778899", "Russia",      "Russa",
            "+7 9161234567",  "i.petrov@email.ru");

        seedStraniero("Mei",      "Chen",       "2001-06-25", "female",
            "Passaporto", "CN44332211", "Cina",        "Cinese",
            "+86 13812345678","m.chen@email.cn");

        seedStraniero("Emeka",    "Okafor",     "1985-09-08", "male",
            "Passaporto", "NG11004455", "Nigeria",     "Nigeriana",
            "+234 8012345678","e.okafor@email.ng");

        seedStraniero("Catalina", "Popescu",    "1993-11-19", "female",
            "Passaporto", "RO22334455", "Romania",     "Rumena",
            "+40 721234567",  "c.popescu@email.ro");

        System.out.println("=== Importazione completata: 30 pazienti registrati ===");
        System.out.println("Questi pazienti sono ora disponibili nel sistema.");
        System.out.println("Cerca un CF o documento dalla schermata principale per avviare il triage.");
    }

    // =========================================================
    // METODI DI CREAZIONE
    // =========================================================

    private static void seedItaliano(String cf, String nome, String cognome,
                                      String birth, String gender) {
        Patient p = new Patient();
        p.addIdentifier()
            .setSystem("http://hl7.it/fhir/sid/codiceFiscale")
            .setValue(cf);
        p.addName().setFamily(cognome).addGiven(nome);
        p.setBirthDateElement(new DateType(birth));
        p.setGender("male".equals(gender)
            ? Enumerations.AdministrativeGender.MALE
            : Enumerations.AdministrativeGender.FEMALE);

        MethodOutcome outcome = client.create().resource(p).execute();
        System.out.println("Italiano: " + nome + " " + cognome
            + " (CF: " + cf + ", ID: " + outcome.getId().getIdPart() + ")");
    }

    private static void seedStraniero(String nome, String cognome, String birth,
                                       String gender, String tipoDoc, String numDoc,
                                       String paese, String nazionalita,
                                       String telefono, String email) {
        Patient p = new Patient();
        p.addIdentifier()
            .setSystem("http://pronto.local/documento/" + tipoDoc.toLowerCase())
            .setValue(numDoc)
            .getType().addCoding()
            .setSystem("http://pronto.local/tipoDocumento")
            .setCode(tipoDoc)
            .setDisplay(paese);
        p.addName().setFamily(cognome).addGiven(nome);
        p.setBirthDateElement(new DateType(birth));
        p.setGender("male".equals(gender)
            ? Enumerations.AdministrativeGender.MALE
            : Enumerations.AdministrativeGender.FEMALE);
        p.addExtension()
            .setUrl("http://hl7.org/fhir/StructureDefinition/patient-nationality")
            .setValue(new StringType(nazionalita));
        p.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setValue(telefono);
        p.addTelecom()
            .setSystem(ContactPoint.ContactPointSystem.EMAIL)
            .setValue(email);

        MethodOutcome outcome = client.create().resource(p).execute();
        System.out.println("Straniero: " + nome + " " + cognome
            + " (" + tipoDoc + ": " + numDoc + ", ID: " + outcome.getId().getIdPart() + ")");
    }
}
