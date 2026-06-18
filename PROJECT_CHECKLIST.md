# Submission Checklist

## Topic A Requirements

- [x] Health log system
- [x] Sleep hours field
- [x] Steps field
- [x] Mood score field
- [x] Risk level field
- [x] SQLite database table: `health_logs`
- [x] Multi-layer decision tree: sleep → steps → mood
- [x] Risk level shown on frontend
- [x] 90 days seed data with clear patterns
- [x] Bonus: Information Gain calculation from current seed data
- [x] Bonus: frontend displays best split feature and best threshold

## Required API Endpoints

- [x] `GET /health-logs`
- [x] `POST /health-logs`
- [x] `PUT /health-logs/{id}`
- [x] `DELETE /health-logs/{id}`
- [x] `GET /health-logs/risk`
- [x] `GET /health-logs/information-gain`

## Hackathon Submission Items

- [x] Live Demo URL: deploy on Render
- [x] GitHub Repository URL: upload project files
- [x] README.md: included
- [x] Prompt History: included in `PROMPT_HISTORY.md`

## GitHub Upload

Upload:

- `pom.xml`
- `Dockerfile`
- `render.yaml`
- `README.md`
- `PROMPT_HISTORY.md`
- `PROJECT_CHECKLIST.md`
- `.gitignore`
- `src/`

Do not upload:

- `target/`
- `data/`
- `.vscode/`
- `.idea/`
