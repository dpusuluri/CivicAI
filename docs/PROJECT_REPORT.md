# CivicAI — Academic Project Report

## Abstract
CivicAI is a prototype civic-issue intake system that turns citizen descriptions, optional evidence and coordinates into a trackable case. It supports an officer-controlled process for validating, routing and resolving issues. It demonstrates the **AI + rules + human oversight** design outlined in the source case study while replacing the proposed multimodal AI with a transparent deterministic keyword demonstration.

## Problem statement
Potholes, illegal dumping, water leaks, drainage blockages and broken streetlights are often reported through fragmented channels. Residents lack tracking, and municipal teams must interpret free-form reports and manage manual hand-offs.

## Objectives
1. Capture issue description, geolocation and optional photo.
2. Generate a private case reference and status history.
3. Suggest issue category and department using a demonstrative deterministic classifier.
4. Restrict operational decisions to authenticated officers.
5. Track status changes and prepare a department report.

## Use cases
- **Citizen**: submit complaint; securely retrieve submitted case; follow case history.
- **Officer**: log in; list cases; inspect suggestions; update status; generate authority report.

## Modules
- Angular standalone page components and HTTP service.
- Spring MVC REST controllers, domain service and validation.
- Spring Data JPA entities `CivicCase` and `CaseEvent`.
- PostgreSQL persistence; filesystem photo bytes, filenames in database.
- Spring Security for officer Basic Auth (demonstration).
- DemoTriage category/priority suggestion.

## Workflow
`SUBMITTED (API receipt) → VALIDATION → ROUTED → ACKNOWLEDGED → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED`. `REOPENED` and `ESCALATED` are controlled exception statuses. Demo starts at `VALIDATION` after submission and records its event.

## Data model
`civic_cases`: UUID primary key, tracking code, secret access token, description, coordinates, category, department, status, priority, triage observation/confidence, photo filename, timestamps, resolution note.

`case_event`: UUID primary key, case foreign key, timestamp, actor, status, reason.

## Suggested test cases
| Test | Expected result |
|---|---|
| Submit pothole near school | Category POTHOLE, priority P1, status VALIDATION |
| Submit without required description | 400 validation response |
| Upload PDF masquerading as image | 400 rejection based on signature |
| Track with incorrect secret | 404 response |
| Officer list without authentication | 401 response |
| Jump VALIDATION directly to CLOSED | 400 invalid transition |
| Resolve without resolution note | 400 rejection |
| Valid sequential workflow | Audit events recorded in order |

## Limitations and future work
Source material proposes governed multimodal image/text AI, PostGIS routing, duplicate matching, SLA clocks, managed private object storage, controlled notification and analytics dashboards. These are future scope, **not features of the delivered prototype**. Before public use, implement full production security/privacy governance and verification of jurisdiction-specific rules.

## Sources
Project requirements derived from user-supplied *CivicAI — How an AI-Assisted Civic Issue Platform Can Help the Public and Government Resolve Everyday Civic Problems*, pages 1–12; especially sections 3, 10–15, 19–23 and 29.
