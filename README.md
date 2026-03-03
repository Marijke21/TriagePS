# TriagePS — Sistema di Triage per Pronto Soccorso

Applicazione prototipale per la gestione del triage in Pronto Soccorso, sviluppata come parte di una tesi di laurea sull'interoperabilità sanitaria tramite lo standard **HL7 FHIR R4**.

---

## Descrizione

TriagePS dimostra come lo standard FHIR possa supportare lo scambio strutturato e interoperabile di informazioni cliniche in un contesto sanitario reale. L'applicazione modella i dati clinici tramite risorse FHIR (Patient, Encounter, Observation, AllergyIntolerance, MedicationStatement) e si interfaccia con un server HAPI FHIR locale.

---

## Tecnologie utilizzate

| Componente | Tecnologia |
|---|---|
| Linguaggio | Java 17 |
| UI | JavaFX 17.0.6 |
| Server FHIR | HAPI FHIR R4 (Docker) |
| Standard | HL7 FHIR R4 |
| Build | Maven |
| IDE consigliato | Eclipse IDE |

---

## Prerequisiti

- **JDK 17** installato
- **Maven** installato
- **Docker Desktop** installato e avviato
- **Eclipse IDE** (o altro IDE con supporto Maven)

---

## Avvio del server FHIR

Prima di avviare l'applicazione è necessario avere il server HAPI FHIR attivo su `localhost:8080`.

```bash
docker run -p 8080:8080 hapiproject/hapi:latest
```

Attendere 30-60 secondi fino a che il server sia raggiungibile all'indirizzo:
```
http://localhost:8080
```

---

## Avvio dell'applicazione

```bash
mvn javafx:run
```

Oppure da Eclipse: tasto destro sul progetto → **Run As** → **Maven Build** → goal `javafx:run`.

---

## Popolamento del database

Il progetto include due utility di seeding nella cartella `it.unibo.util`:

### DataSeeder
Popola il server FHIR con **25 pazienti già in carico al PS**, ognuno con:
- Risorsa `Patient` (italiano con CF o straniero con documento)
- Risorsa `Encounter` in stato `in-progress` con codice triage assegnato
- Risorse `Observation` con parametri vitali (frequenza cardiaca, pressione, SpO2, ecc.)
- Risorse `AllergyIntolerance` e `MedicationStatement` dove presenti

### DataSeederPatient
Simula un **nodo esterno** (es. FSE, anagrafe sanitaria regionale) che trasmette al PS i dati anagrafici di **30 pazienti** senza Encounter né Observation. Questi pazienti rappresentano persone registrate nel sistema sanitario nazionale che potrebbero accedere al pronto soccorso: quando si presentano allo sportello, l'operatore cerca il loro CF e avvia il triage.

> ⚠️ Eseguire i seeder una sola volta. Per resettare il database, eliminare e ricreare il container Docker.

---

## Struttura del progetto

```
TriagePS/
├── src/main/java/it/unibo/
│   ├── App.java                          # Entry point JavaFX
│   ├── ClinicalData.java                 # Wrapper Patient + Encounter
│   ├── config/
│   │   └── FhirClientConfig.java         # Configurazione client FHIR
│   ├── controller/
│   │   ├── MainController.java           # Schermata ricerca paziente
│   │   ├── RegistrazioneController.java  # Registrazione nuovo paziente
│   │   ├── ClinicalController.java       # Inserimento dati clinici e triage
│   │   └── DashboardController.java      # Dashboard pazienti in carico
│   ├── service/
│   │   ├── PatientService.java
│   │   └── ClinicalService.java
│   ├── repository/
│   │   ├── EncounterRepository.java
│   │   ├── ObservationRepository.java
│   │   ├── AllergyRepository.java
│   │   └── MedicationRepository.java
│   └── util/
│       ├── DataSeeder.java
│       └── DataSeederPatient.java
└── src/main/resources/
    ├── fxml/
    │   ├── main.fxml
    │   ├── registrazione.fxml
    │   ├── clinical.fxml
    │   └── dashboard.fxml
    └── css/
        └── style.css
```

---

## Funzionalità principali

- **Ricerca paziente** per codice fiscale (italiani) o documento (stranieri)
- **Registrazione nuovo paziente** con supporto sia per cittadini italiani (CF) che stranieri (documento di identità estero, nazionalità, contatti)
- **Inserimento dati clinici**: parametri vitali, GCS, NRS, glicemia, peso, altezza, allergie, farmaci
- **Assegnazione codice triage** (BIANCO / VERDE / GIALLO / ROSSO)
- **Dashboard** in tempo reale con pazienti suddivisi per codice triage e tempo di attesa
- **Modifica dati** di un paziente già in carico con pre-popolamento automatico del form

---

## Risorse FHIR utilizzate

| Risorsa FHIR | Utilizzo |
|---|---|
| `Patient` | Anagrafica paziente (italiano o straniero) |
| `Encounter` | Accesso al PS con codice triage e orario di arrivo |
| `Observation` | Parametri vitali (LOINC codes) |
| `AllergyIntolerance` | Allergie del paziente |
| `MedicationStatement` | Terapia farmacologica in corso |

---

## Autore

Sviluppato come prototipo applicativo per la tesi di laurea triennale in Ingegneria e Scienze Informatiche — Università di Bologna.
