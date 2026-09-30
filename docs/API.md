# API Reference

Base URL locale: `http://localhost:8080`. Gli endpoint funzionali non richiedono autenticazione nell'ambiente di
esercitazione. Le date sono in formato ISO `yyyy-MM-dd`; gli endpoint di scrittura accettano JSON.

## Health

| Metodo | Percorso           | Risposta                                   |
|--------|--------------------|--------------------------------------------|
| GET    | `/actuator/health` | Stato applicazione e datasource PostgreSQL |

## Dipendenti

Campi richiesti: `firstName`, `lastName`, `email`, `team`, `office`, `birthDate`, `active`. Il campo `active` deve
essere presente e booleano; `false` crea/aggiorna un dipendente inattivo. Il campo email deve essere valido e univoco;
`birthDate` deve essere nel passato.

| Metodo | Percorso              | Descrizione                        | Risposta attesa    |
|--------|-----------------------|------------------------------------|--------------------|
| GET    | `/api/employees`      | Elenco ordinato per cognome e nome | 200                |
| GET    | `/api/employees/{id}` | Dettaglio dipendente               | 200, 404           |
| POST   | `/api/employees`      | Crea dipendente                    | 201, 400, 409      |
| PUT    | `/api/employees/{id}` | Sostituisce i dati dipendente      | 200, 400, 404, 409 |
| DELETE | `/api/employees/{id}` | Elimina dipendente                 | 204, 404           |

Esempio body:

```json
{
  "firstName": "Luca",
  "lastName": "Neri",
  "email": "luca.neri@example.com",
  "team": "Platform",
  "office": "Firenze",
  "birthDate": "1992-06-15",
  "active": true
}
```

## Invio e storico notifiche

| Metodo | Percorso                                     | Descrizione                                  | Risposta attesa |
|--------|----------------------------------------------|----------------------------------------------|-----------------|
| POST   | `/api/notifications/send`                    | Esegue per la data corrente in `Europe/Rome` | 200             |
| POST   | `/api/notifications/send?date=2026-09-30`    | Esegue per la data indicata                  | 200, 400        |
| GET    | `/api/notifications/history`                 | Storico completo, dal più recente            | 200             |
| GET    | `/api/notifications/history?date=2026-09-30` | Storico per data                             | 200, 400        |

L'invio è simulato: ogni messaggio appare nei log applicativi come `MOCK EMAIL`. Lo storico contiene un record per
destinatario. Se la newsletter è disabilitata, il trigger restituisce `status: DISABLED` e non registra invii. Ogni
chiamata manuale invia nuovamente anche se è già stato eseguito un invio per la stessa data; non è attiva una protezione
dai duplicati.

## Impostazioni

| Metodo | Percorso        | Descrizione                                | Risposta attesa |
|--------|-----------------|--------------------------------------------|-----------------|
| GET    | `/api/settings` | Legge oggetto, corpo e stato newsletter    | 200             |
| PUT    | `/api/settings` | Aggiorna oggetto, corpo e stato newsletter | 200, 400        |

Campi richiesti: `subjectTemplate` (massimo 200 caratteri), `bodyTemplate` (massimo 5000 caratteri), `enabled`.

Segnaposto supportati nel testo: `{team}`, `{birthdays}`, `{date}`. `{birthdays}` include nome, cognome ed età.

## Regole di invio

- Solo dipendenti attivi sono considerati.
- I destinatari sono i dipendenti attivi dello stesso team, esclusi i festeggiati.
- Per una nascita il 29 febbraio, negli anni non bisestili si usa il 28 febbraio.
- Lo scheduler usa `NEWSLETTER_CRON` e `NEWSLETTER_ZONE`; i valori predefiniti sono `0 0 8 * * *` e `Europe/Rome`.
