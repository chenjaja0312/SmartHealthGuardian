# Smart Health Guardian

## Project Topic

**Topic A: Smart Health Log and Risk Assessment System**

This project is a full-stack health self-management web application. Users can record daily health data including sleep hours, steps, and mood score. The backend uses a multi-layer decision tree to classify the user's current health risk level as **LOW**, **MEDIUM**, or **HIGH**.

## Features

- Add a daily health log through the frontend form
- Save health logs into a SQLite database
- Automatically classify health risk level with a decision tree
- Show the latest risk level with a colored badge
- Display the full decision path so the model logic is visible
- Show all historical health logs in a table
- Delete existing health logs
- Seed 90 days of meaningful health data with clear patterns
- Bonus: calculate Information Gain from seed data to find the strongest first split and best thresholds

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java 17, Spring Boot |
| API | RESTful API |
| Database | SQLite |
| DM / ML | Multi-layer Decision Tree Classification + Information Gain Analysis |
| Deployment | Render with Docker |

## System Architecture

```text
User Browser
   ↓
HTML / CSS / JavaScript Frontend
   ↓ fetch()
Spring Boot REST API
   ↓
Decision Tree Service
   ↓
SQLite Database: health_logs
```

## Data Flow

1. The user enters sleep hours, steps, and mood score in the frontend.
2. The frontend sends a `POST /health-logs` request to the Spring Boot backend.
3. The backend validates the input data.
4. The backend sends the three features into the decision tree:
   - sleep hours
   - steps
   - mood score
5. The decision tree returns a risk level: `LOW`, `MEDIUM`, or `HIGH`.
6. The backend writes the health log and calculated risk level into SQLite.
7. The frontend reloads the history table and latest risk badge.

## Database Design

Table: `health_logs`

| Column | Type | Description |
|---|---|---|
| id | INTEGER PRIMARY KEY AUTOINCREMENT | Unique log ID |
| log_date | DATE NOT NULL | Log date, format YYYY-MM-DD |
| sleep_hours | REAL NOT NULL | Daily sleep hours |
| steps | INTEGER NOT NULL | Daily step count |
| mood_score | INTEGER NOT NULL | Mood score from 1 to 10 |
| risk_level | TEXT | Risk level calculated by the decision tree |

## Decision Tree Logic

This project intentionally uses a multi-layer decision tree instead of a single if statement.

```text
Layer 1: Sleep Hours
├── sleep < 6.0
│   └── Layer 2: Steps
│       ├── steps < 4000
│       │   └── Layer 3: Mood
│       │       ├── mood <= 4 → HIGH
│       │       └── mood > 4  → MEDIUM
│       └── steps >= 4000
│           └── Layer 3: Mood
│               ├── mood <= 4 → MEDIUM
│               └── mood > 4  → MEDIUM
└── sleep >= 6.0
    └── Layer 2: Steps
        ├── steps < 4000
        │   └── Layer 3: Mood
        │       ├── mood <= 4 → MEDIUM
        │       └── mood > 4  → LOW
        └── steps >= 4000
            └── Layer 3: Mood
                ├── mood <= 4 → MEDIUM
                └── mood > 4  → LOW
```

## API Endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | `/health-logs` | Get all health logs |
| POST | `/health-logs` | Create a new health log and calculate risk |
| PUT | `/health-logs/{id}` | Update a health log and recalculate risk |
| DELETE | `/health-logs/{id}` | Delete a health log |
| GET | `/health-logs/risk` | Get the latest risk result and decision path |
| GET | `/health-logs/information-gain` | Bonus endpoint: calculate entropy, information gain, best feature, and best threshold |
| GET | `/api/health` | Health check for deployment |


## Bonus: Information Gain Calculation

The topic document says the advanced version can calculate Information Gain from the seed data to find the real best split feature and threshold. This project includes that bonus feature.

The backend endpoint is:

```text
GET /health-logs/information-gain
```

It calculates:

- Base entropy of the current `health_logs` records
- Best threshold for `sleep_hours`
- Best threshold for `steps`
- Best threshold for `mood_score`
- Information Gain for each feature
- The recommended first split of the decision tree

The frontend also displays these results in a **Bonus: Information Gain** section. This makes the ML part more than a hard-coded rule because the app checks the generated seed data and explains which feature gives the strongest split.

## Seed Data Design

The system creates 90 days of seed data automatically when the database is empty.

The data is intentionally patterned, not random:

- 25 high-risk examples: low sleep, low steps, low mood
- 40 medium mixed examples: mixed values and middle cases
- 25 low-risk examples: enough sleep, many steps, good mood

This ensures that each decision tree branch has meaningful examples.

## Local Run Instructions

### 1. Open the project folder in VS Code

Open the folder named `SmartHealthGuardian`.

### 2. Run the project

In the VS Code terminal, run:

```bash
mvn spring-boot:run
```

### 3. Open the website

Go to:

```text
http://localhost:8080
```

## Render Deployment Instructions

This project includes a `Dockerfile`, so Render can build and run it directly.

### GitHub Upload Structure

Upload these files and folders to GitHub:

```text
SmartHealthGuardian/
├── pom.xml
├── Dockerfile
├── render.yaml
├── README.md
├── PROMPT_HISTORY.md
├── .gitignore
└── src/
```

Do **not** upload:

```text
target/
data/
.vscode/
.idea/
```

### Render Settings

- New Web Service
- Connect GitHub repository
- Environment: Docker
- Health Check Path: `/api/health`

Render will automatically use the Dockerfile.

## Project Structure

```text
SmartHealthGuardian/
├── pom.xml
├── Dockerfile
├── render.yaml
├── README.md
├── PROMPT_HISTORY.md
├── .gitignore
└── src/
    └── main/
        ├── java/com/example/smarthealth/
        │   ├── SmartHealthGuardianApplication.java
        │   ├── HealthLog.java
        │   ├── HealthLogController.java
        │   ├── HealthLogRepository.java
        │   ├── RiskDecisionTree.java
        │   ├── RiskResult.java
        │   ├── RiskResponse.java
        │   ├── InformationGainService.java
        │   ├── InformationGainResponse.java
        │   ├── FeatureGain.java
        │   └── DatabaseInitializer.java
        └── resources/
            ├── application.properties
            └── static/
                ├── index.html
                ├── style.css
                └── app.js
```

## Data Quality Self-Check

Before submission, check these points:

- The database contains enough seed records.
- The three risk levels all appear in the data.
- The frontend correctly calls `POST /health-logs`.
- The backend writes data into `health_logs`.
- The latest risk badge updates after a new log is submitted.
- The decision path shows sleep → steps → mood.
