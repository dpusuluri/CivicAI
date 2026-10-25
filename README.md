# CivicAI — Civic Issue Reporting and Officer Workflow

**Academic full-stack application** based on the attached 12-page CivicAI case study. Java 21 / Spring Boot 3 backend, Angular 19 UI, PostgreSQL database. Includes a **clearly labeled rule-based triage demo**, not a trained AI or image recognition model.

## Features

- Citizen issue submission with description, GPS coordinates, optional private photo (JPEG/PNG/WebP ≤ 5 MB).
- Case number and **secret tracking token** returned on submission; citizen follows status timeline using both.
- Rule-based category suggestion (pothole, garbage, streetlight, water leak, drainage), demo department mapping and priority suggestion.
- Officer-only queue and authenticated status workflow with transition validation, reasons and immutable-by-API event history.
- Private photo download endpoints; officer-generated text report; basic case counts.
- Responsive Angular citizen report, tracking, and officer pages.

## Prerequisites

- Docker Desktop and Docker Compose; Node.js 20.11+ or 22.x; npm.
- Alternative: Java 21, Maven 3.9+, PostgreSQL 16, Node.js.

## Run locally

```bash
cp .env.example .env
# Edit .env and set a strong ADMIN_PASSWORD
# Run database and backend
docker compose up --build -d
# Run Angular in a separate terminal
cd frontend
npm install
npm start
```

Open **http://localhost:4200**. Officer portal is `/officer` with username from `.env` (default `officer`) and password set in `.env`. **Do not publish demo credentials or deploy this as a public service.**

`docker compose logs -f backend` displays startup problems. To stop: `docker compose down` (add `-v` only if you want to delete database and photos).

## Demo procedure

1. Citizen enters `Large pothole near the school entrance`, valid latitude/longitude, and optionally uploads a photo.
2. Submit and save the private tracking link. Submission begins in `VALIDATION`, not automatically approved.
3. Open `/officer`, sign in, review demo suggestion and update one step at a time: `VALIDATION → ROUTED → ACKNOWLEDGED → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED`.
4. For `RESOLVED`, include a resolution note. `REOPENED` can follow `RESOLVED` or `CLOSED`.
5. Officer can generate an authority report. Citizen tracking link displays the audit timeline.

## API

| Method | Route | Auth | Purpose |
|---|---|---|---|
| POST | `/api/cases` | public | `multipart/form-data`: `case` application/json `{description,latitude,longitude}` and optional `photo` |
| GET | `/api/cases/track/{code}?token={uuid}` | code + secret | Citizen tracking |
| GET | `/api/cases/track/{code}/photo?token={uuid}` | code + secret | Citizen image download |
| GET | `/api/officer/cases` | HTTP Basic officer | Officer list |
| PUT | `/api/officer/cases/{uuid}` | HTTP Basic officer | Update `{status,reason,resolutionNote,department}` |
| GET | `/api/officer/cases/{uuid}/report` | HTTP Basic officer | Draft report text |
| GET | `/api/officer/cases/{uuid}/photo` | HTTP Basic officer | Image bytes |

### Sample API upload

```bash
curl -X POST http://localhost:8080/api/cases \
  -F 'case={"description":"Large pothole near school entrance","latitude":38.9696,"longitude":-77.3861};type=application/json' \
  -F 'photo=@/path/to/your-photo.jpg;type=image/jpeg'
```

## Architecture

```text
Angular 19 SPA → /api proxy → Spring Boot 3 REST API → PostgreSQL 16
                                  ├─ Demo text triage + mapping
                                  ├─ Officer-approved state machine + audit events
                                  └─ Private local photo store
```

**Not implemented / future enhancements**: actual multimodal AI, calibrated confidence, PostGIS jurisdiction polygon routing, spatial duplicate matching, production-ready email delivery, background SLA timers, public map UI, workload assignment to crews, image sanitization/malware scanning, full RBAC/SSO, password recovery, rate limiting, migrations, production observability. These are recommended in the PDF, but **must not be claimed as completed**. Current department mapping uses keywords, not authoritative local boundaries. The `aiConfidence` figure is an illustrative demo indicator. No images are analyzed and no emails are sent.

### Deployment and data security

This project is for local academic demonstration only. Backend defaults are **unsafe for public hosting**. Before public deployment, implement OIDC/OAuth2 authorization, TLS, per-user ownership, rate limiting, file malware scanning, retention and consent, secure object storage and access links, migrations, CSRF-aware cookie sessions (if applicable), secrets management, accessibility testing, and production audits. The current tracking token is a **bearer secret**: anyone with the full link can access the record; do not publish it to GitHub. GPS coordinates and photos are sensitive. HTTP Basic credentials should only be transmitted over HTTPS outside localhost. Do not expose PostgreSQL's port publicly.
