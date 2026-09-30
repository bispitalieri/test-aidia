# Birthday Newsletter

Prototipo REST per la newsletter interna dei compleanni. Stack: Java 17, Spring Boot 3, Maven, Spring Data JPA e
PostgreSQL. L'invio email è simulato e stampato nei log del servizio, quindi non richiede un server SMTP.

## Avvio

Servono Docker e Docker Compose. Dalla directory del progetto:

```bash
docker compose up --build
```

Per personalizzare porte, credenziali e scheduler, copia `.env.example` in `.env` e modifica i valori prima dell'avvio.
`.env` è escluso da Git.

L'applicazione ascolta su `http://localhost:8080`; PostgreSQL su `localhost:5432`. Al primo avvio vengono creati tre
dipendenti di esempio (se il database è vuoto), incluso un compleanno odierno nel team Platform.

Verifica applicazione e database:

```bash
curl http://localhost:8080/actuator/health
```

La build Docker esegue anche i test automatici. Se Maven è installato localmente, si possono lanciare separatamente con
`mvn test`.

Per osservare le email simulate e i log:

```bash
docker compose logs -f app
```

La schedulazione parte ogni giorno alle 08:00 nel fuso `Europe/Rome`. Per fermare i servizi: `docker compose down`. I
dati restano nel volume `postgres_data`; `docker compose down -v` elimina anche il database persistito.

## Modello e regole

- `Employee`: anagrafica, email univoca, team, sede, data di nascita e stato attivo.
- `NewsletterSettings`: oggetto e corpo del template, stato abilitato/disabilitato.
- `NotificationLog`: un record per ciascun destinatario simulato, con data di riferimento, contenuto, stato e timestamp.
- Ricevono l'email simulata solo i dipendenti attivi dello stesso team del festeggiato; il festeggiato non riceve la
  propria notifica. I dipendenti inattivi non partecipano né come festeggiati né come destinatari.
- Per i nati il 29 febbraio, negli anni non bisestili il compleanno è considerato il 28 febbraio.
- Trigger manuali ripetuti per la stessa data generano un nuovo invio e un nuovo record nello storico per ogni chiamata;
  al momento non è previsto un controllo.
- Il trigger manuale accetta una data opzionale, utile per provare date diverse da oggi. Le email simulate si vedono nei
  log; lo storico si consulta via API.

Lo schema viene aggiornato automaticamente da Hibernate (`ddl-auto: update`), scelta semplice per l'esercizio. Per
un'applicazione reale è preferibile introdurre migrazioni versionate (ad esempio Flyway), secret gestite e un provider
email.

## API REST

È disponibile la collection Postman `Birthday-Newsletter.postman_collection.json`. In Postman seleziona **Import** e
scegli il file. La variabile `baseUrl` è già impostata su `http://localhost:8080`; `sendDate` può essere modificata per
provare il trigger manuale su una data specifica. La richiesta **Crea dipendente** salva automaticamente l'id restituito
nella variabile `employeeId`.

La descrizione di tutti gli endpoint, campi e regole è in [docs/API.md](docs/API.md).

### Dipendenti

```bash
# Elenco
curl http://localhost:8080/api/employees

# Crea (salva l'id restituito per update/delete)
curl -X POST http://localhost:8080/api/employees \
  -H 'Content-Type: application/json' \
  -d '{"firstName":"Luca","lastName":"Neri","email":"luca.neri@example.com","team":"Platform","office":"Firenze","birthDate":"1992-06-15","active":true}'

# Lettura singolo
curl http://localhost:8080/api/employees/1

# Aggiornamento completo
curl -X PUT http://localhost:8080/api/employees/1 \
  -H 'Content-Type: application/json' \
  -d '{"firstName":"Luca","lastName":"Neri","email":"luca.neri@example.com","team":"Platform","office":"Firenze","birthDate":"1992-06-15","active":false}'

# Eliminazione
curl -X DELETE http://localhost:8080/api/employees/1
```

### Trigger e storico

```bash
# Invio per la data odierna (Europe/Rome)
curl -X POST http://localhost:8080/api/notifications/send

# Simula un invio per una data specifica
curl -X POST 'http://localhost:8080/api/notifications/send?date=2026-09-30'

# Storico completo o filtrato per giorno
curl http://localhost:8080/api/notifications/history
curl 'http://localhost:8080/api/notifications/history?date=2026-09-30'
```

### Configurazione template

Segnaposto supportati nel template: `{team}`, `{birthdays}` e `{date}`. `{birthdays}` viene sostituito con nome e anni
compiuti.

```bash
curl http://localhost:8080/api/settings
curl -X PUT http://localhost:8080/api/settings \
  -H 'Content-Type: application/json' \
  -d '{"subjectTemplate":"Compleanni del team {team}","bodyTemplate":"Oggi ({date}) festeggiamo: {birthdays}.","enabled":true}'
```

## Configurazione ambiente

I valori locali predefiniti sono definiti in Compose. È possibile sovrascrivere `POSTGRES_DB`, `POSTGRES_USER`,
`POSTGRES_PASSWORD`, `POSTGRES_PORT`, `APP_PORT`, `NEWSLETTER_CRON` e `NEWSLETTER_ZONE` con variabili d'ambiente o un
file `.env` locale (ignorato da Git).

## Struttura

`controller`, `service`, `repository`, `entity`, `dto` e `config` separano API, logica applicativa, persistenza, modello
e configurazioni. `application.yml` contiene le impostazioni Spring e dello scheduler.

```text
src/main/java/it/aidia/birthdaynewsletter/
├── config/       # Dati demo e configurazioni Spring
├── controller/   # Endpoint REST
├── dto/          # Modelli request/response e validazione
├── entity/       # Entità JPA
├── repository/   # Accesso a PostgreSQL tramite Spring Data
└── service/      # Regole, invii simulati e scheduler
```

`Dockerfile` contiene la build multi-stage; `.dockerignore` evita di copiare file locali non necessari nel contesto
Docker.

## Miglioramenti possibili

- Migrazioni Flyway, test unitari e integrazione automatizzati.
- Invio reale con Spring Mail e gestione retry/fallimenti, mantenendo lo storico per ogni tentativo.
- Paginazione/filtri per anagrafica e storico, autenticazione e autorizzazione.
- Configurazione dei destinatari e della schedulazione gestibile via API, metriche e audit.
