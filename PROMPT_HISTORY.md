# AI Chat History / Prompt Record

This file is prepared for the hackathon submission requirement. It records the AI collaboration process used to design and debug the project.

## Prompt 1: Choose Topic

**User Prompt**

I have three topics A, B, and C. Please help me choose the topic that is most suitable for a 3-hour individual hackathon using Java, database, frontend, and DM/ML.

**AI Output Summary**

Topic A was selected because it has one simple database table, clear frontend input fields, and a direct multi-layer decision tree using sleep hours, steps, and mood score.

## Prompt 2: Generate Project

**User Prompt**

Please generate a complete Spring Boot project for Topic A: Smart Health Log and Risk Assessment System. It must include Java backend, SQLite database, frontend page, REST API, multi-layer decision tree, README, Dockerfile, and Render deployment support.

**AI Output Summary**

The AI generated a full-stack project with:

- Spring Boot backend
- SQLite database table `health_logs`
- CRUD APIs
- Decision tree service
- HTML / CSS / JS frontend
- Seed data generator
- Dockerfile
- README

## Prompt 3: Decision Tree Requirement

**User Prompt**

Make sure the decision tree is not only a single if statement. It must clearly show sleep → steps → mood as a multi-layer classification path.

**AI Output Summary**

The AI implemented `RiskDecisionTree.java` with three layers:

1. Sleep hours
2. Step count
3. Mood score

The API also returns the decision path, allowing the frontend to display how the risk level was classified.

## Prompt 4: Seed Data Quality

**User Prompt**

The seed data cannot be purely random. Please generate meaningful data patterns so the decision tree can classify low, medium, and high risk cases.

**AI Output Summary**

The AI created 90 days of seed data:

- 25 high-risk records with low sleep, low steps, low mood
- 40 medium mixed records
- 25 low-risk records with enough sleep, many steps, good mood

## Prompt 5: Deployment Debugging

**User Prompt**

Make sure the project can be uploaded to GitHub and deployed to Render without main class naming errors or startup command errors.

**AI Output Summary**

The AI checked these deployment details:

- Main class: `SmartHealthGuardianApplication`
- Package: `com.example.smarthealth`
- Maven start-class matches the main class
- Dockerfile builds the Maven project and runs the packaged jar
- Render health check path is `/api/health`
- `target/`, `.vscode/`, `.idea/`, and `data/` should not be uploaded

## Final Self-Check

- Frontend → API → DB data flow is clear.
- Decision tree result is embedded into the backend business logic.
- Risk level is saved into the database.
- The frontend shows the risk badge and decision path.
- README explains architecture, database, ML module, and deployment.


## Prompt 6: Add Bonus Information Gain

**User Prompt**

The file mentions a bonus advanced method. Please add the bonus part for Topic A: calculate Information Gain from seed data and show the best split feature and threshold.

**AI Output Summary**

The AI added an Information Gain module:

- New backend service: `InformationGainService.java`
- New DTOs: `FeatureGain.java`, `InformationGainResponse.java`
- New endpoint: `GET /health-logs/information-gain`
- Frontend bonus section showing base entropy, feature gains, best threshold, and best root feature
- README updated to explain the C4.5-style bonus method

This directly responds to the advanced requirement: use the seed data to calculate information gain rather than only using manually chosen thresholds.
