# TriagePS — Gestione Pronto Soccorso FHIR

Applicazione desktop Java/JavaFX per la gestione dei dati clinici in pronto soccorso,
conforme allo standard HL7 FHIR R4.

## Requisiti

- Java 17+
- Maven 3.8+
- Docker (per il server FHIR)

## 1. Avvio del server FHIR (HAPI FHIR)

Prima di avviare l'applicazione, avvia il server FHIR con Docker:

```bash
docker run -p 8080:8080 hapiproject/hapi:latest
```

Attendi che il server sia pronto (circa 30–60 secondi).
Puoi verificarlo aprendo il browser su: http://localhost:8080

## 2. Avvio dell'applicazione

```bash
cd TriagePS
mvn javafx:run
```

Oppure da Eclipse: tasto destro sul progetto → Run As → Maven Build → Goals: `javafx:run`

## 3. Flusso applicativo

1. **Schermata principale** — inserisci il codice fiscale del paziente
2. **Registrazione** — se il paziente non esiste, registralo tramite il pulsante REGISTRA
3. **Triage** — assegna il codice triage (Bianco/Verde/Giallo/Rosso)
4. **Dati clinici** — inserisci parametri vitali, info cliniche e salva

## 4. Struttura del progetto

```
src/main/java/it/unibo/
├── App.java                    # Entry point
├── DataReceiver.java           # Interfaccia per passaggio dati tra schermate
├── config/FhirClientConfig.java
├── repository/                 # Chiamate REST al server FHIR
├── service/                    # Business logic
└── controller/                 # Controller JavaFX

src/main/resources/
├── fxml/                       # Schermate FXML
└── css/style.css               # Stile
```

## 5. Risorse FHIR utilizzate

| Risorsa | Scopo |
|---|---|
| Patient | Anagrafica paziente |
| Encounter | Accesso al pronto soccorso |
| Observation | Parametri vitali e clinici |
| AllergyIntolerance | Allergie note |
| MedicationStatement | Farmaci assunti |

## 6. Verifica dati sul server

Dopo aver salvato un accesso, puoi verificare i dati direttamente sul server FHIR:

- Pazienti: http://localhost:8080/fhir/Patient
- Encounter: http://localhost:8080/fhir/Encounter
- Observation: http://localhost:8080/fhir/Observation
