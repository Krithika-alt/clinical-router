# Clinical Data Ingestion Router

A Spring Boot backend microservice that acts as a digital traffic cop for clinical environments: securely ingesting patient payloads, prioritizing them via a max-heap triage queue, and caching ICD-10 billing codes for automated care routing.

## Features

- **REST API Ingestion** (`IngestionController`) — exposes a `/api/clinical/ingest` endpoint that catches incoming clinical data payloads and instantly validates them using Jakarta Bean Validation, rejecting malformed requests with an HTTP 400 status before they touch the business logic.
- **Priority queue triage** (`TranscriptService`) — manages a `PriorityQueue` (configured as a Max-Heap) that automatically sorts incoming clinical transcripts by an urgency score (Critical > Urgent > Standard), ensuring the highest-risk patients are always polled first.
- **In-Memory Medical Code Lookup** — utilizes a TreeMap (Binary Search Tree) to hold ICD-10 medical billing codes. When a patient is processed, the engine scans their transcript text for known keywords (e.g., "cardiac arrest") and maps the first match to its ICD-10 code, or flags the transcript for manual review if nothing matches.
- **Dynamic routing engine** (`ClinicalService`) — evaluates priority thresholds on the fly, seamlessly directing patient records to either a High-Priority Urgent Care Stream or a Standard Clinical Queue.

## Tech stack

- Java 21, Spring Boot 3.3.3
- Jakarta Bean Validation (Hibernate Validator) for strict payload integrity
- JUnit 5 & Spring MockMvc for integration testing
- Maven (wrapper included)

## Running it

```bash
./mvnw spring-boot:run
```

## Project Structure

```
src/main/java/com/healthtech/clinicalrouter/
  IngestionController.java       REST entrypoint + @Valid request handling
  ClinicalService.java           rule-based priority routing logic
  TranscriptService.java         max-heap triage queue + BST ICD-10 lookup
  ClinicalRecordDto.java         data model + validation constraints
  TranscriptDTO.java             immutable record for clinical text
```

## Testing 

```bash
./mvnw test
```

Covers end-to-end routing behavior using MockMvc. Tests validate the "happy path" (successful HTTP 200 routing of high-priority patients) and the "error path" (HTTP 400 Bad Request triggers when required payload fields are missing). Unit tests (TranscriptServiceTest) also cover priority queue ordering (Critical > Urgent > Routine), case-insensitive ICD-10 keyword matching, the manual-review fallback for unmatched text, and the empty-queue case.

## Notes 

This project is a backend systems engineering demonstration illustrating enterprise integration patterns, in-memory data structures (Max-Heaps, BSTs), and strict API boundary validation. It is designed to showcase how standard clinical data flows are secured and routed in modern healthcare microservices, rather than acting as a fully compliant HIPAA production server.

