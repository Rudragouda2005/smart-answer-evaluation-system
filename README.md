# Smart Answer Evaluation System using LCS Algorithm

A full-stack web application that compares a **teacher's model answer** with a **student's answer** using the **Longest Common Subsequence (LCS)** algorithm and **Dynamic Programming**. Built for colleges and schools to demonstrate automatic answer checking.

---

## Problem Statement

Manual answer checking is slow and inconsistent. This system tokenizes both answers into words, finds the longest common subsequence of matching words, and computes a **similarity percentage** and **grade** automatically.

---

## Features

| Feature | Description |
|--------|-------------|
| Automatic evaluation | Compare answers with one click |
| Similarity % | `(2 × LCS length) / (teacher words + student words) × 100` |
| Matching words | Highlighted in both answers |
| DP table visualization | Full dynamic programming matrix |
| Step-by-step LCS | Each DP cell fill explained |
| Real-time mode | Debounced comparison while typing |
| PDF download | Export report via jsPDF |
| Dark mode | Toggle with persistence |
| Sample demos | Pre-loaded science & programming answers |
| Grading | Excellent (90+), Good (70–89), Average (50–69), Poor (&lt;50) |

---

## Technology Stack

| Layer | Technology |
|-------|------------|
| Frontend | HTML5, CSS3, JavaScript |
| Backend | Java 17, Spring Boot 3 |
| Algorithm | LCS with Dynamic Programming (word-level) |

---

## Project Structure

```
ADA_Project/
├── README.md
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── style.css
│   └── js/
│       └── app.js
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/lcs/evaluation/
│       ├── LcsEvaluationApplication.java
│       ├── controller/EvaluationController.java
│       ├── service/LcsService.java
│       ├── model/...
│       └── config/...
└── screenshots/
    └── (add your screenshots here)
```

---

## Prerequisites

- **Java JDK 17** or higher — [Download](https://adoptium.net/)
- **Apache Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)
- A modern web browser (Chrome, Edge, Firefox)
- Optional: VS Code Live Server or any static file server for the frontend

Verify installations:

```bash
java -version
mvn -version
```

---

## Setup Instructions

### Step 1: Start the Java Backend

Open a terminal in the `backend` folder:

```bash
cd backend
mvn spring-boot:run
```

Wait until you see: `Started LcsEvaluationApplication`

The API runs at: **http://localhost:8080**

Test health:

```bash
curl http://localhost:8080/api/health
```

### Step 2: Open the Frontend

**Option A — Direct file (simplest)**  
Open `frontend/index.html` in your browser.  
*(Some browsers block API calls from `file://`; use Option B if compare fails.)*

**Option B — Local HTTP server (recommended)**

Using Python:

```bash
cd frontend
python -m http.server 5500
```

Then open: **http://localhost:5500**

Using VS Code: Install **Live Server** extension → right-click `index.html` → **Open with Live Server**.

### Step 3: Use the Application

1. Enter or load sample teacher and student answers.
2. Click **Compare Answers** (or enable **Real-time comparison**).
3. View similarity, grade, LCS sequence, DP table, and steps.
4. Click **Download Result as PDF** to save the report.

---

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/health` | Backend status |
| POST | `/api/compare` | Compare two answers |

**Request body (JSON):**

```json
{
  "teacherAnswer": "Your model answer text",
  "studentAnswer": "Student answer text"
}
```

**Response (JSON):** `lcsLength`, `lcsSequence`, `similarityPercentage`, `grade`, `evaluationResult`, `matchingWords`, `dpTable`, `algorithmSteps`, etc.

---

## Deployment Steps

### Backend (JAR)

```bash
cd backend
mvn clean package -DskipTests
java -jar target/smart-answer-evaluation-1.0.0.jar
```

Deploy the JAR to any server with Java 17 (AWS EC2, college lab machine, etc.). Open port **8080** in the firewall.

### Frontend

1. Upload the `frontend` folder to static hosting (Netlify, GitHub Pages, college web server).
2. Edit `API_BASE` in `frontend/js/app.js` to your deployed backend URL:

```javascript
const API_BASE = "https://your-server.com/api";
```

3. Ensure CORS is allowed (already configured in `WebConfig.java`).

### Combined deployment (optional)

Copy `frontend/*` into `backend/src/main/resources/static/` and rebuild — Spring Boot will serve the UI and API from one port.

---

## Screenshots

Add screenshots of your running app in the `screenshots/` folder:

1. Dashboard with teacher and student inputs  
2. Evaluation summary with similarity ring  
3. DP table visualization  
4. Dark mode view  

Example filenames: `dashboard.png`, `results.png`, `dp-table.png`, `dark-mode.png`

---

## LCS Algorithm (Simple Explanation)

Given two sequences of words **A** (teacher) and **B** (student):

1. Build a table `dp[i][j]` = LCS length of first `i` words of A and first `j` words of B.
2. If `A[i-1] == B[j-1]`: `dp[i][j] = dp[i-1][j-1] + 1`
3. Else: `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`
4. Answer is `dp[m][n]`; backtrack to get the actual LCS words.

**Time complexity:** O(m × n)  
**Space complexity:** O(m × n)

---

## Viva Questions & Answers

**Q1. What is LCS?**  
Longest Common Subsequence is the longest sequence of elements that appear in the same order in both strings (not necessarily consecutive).

**Q2. Why use LCS for answer evaluation?**  
It measures how much of the model answer's key vocabulary and order appears in the student answer, which suits descriptive exam responses.

**Q3. What is Dynamic Programming here?**  
We store subproblem results in `dp[i][j]` to avoid recomputing LCS for smaller prefixes — optimal substructure + overlapping subproblems.

**Q4. How is similarity calculated?**  
`(2 × LCS length) / (teacher word count + student word count) × 100` — a symmetric score between 0 and 100.

**Q5. Word-level vs character-level LCS?**  
Word-level is better for essays; character-level is stricter and penalizes minor spelling differences more.

**Q6. Time complexity of the algorithm?**  
O(m × n) where m and n are word counts in teacher and student answers.

**Q7. What are real-world applications?**  
Plagiarism detection, diff tools, bioinformatics (DNA sequences), and automated grading assistants.

**Q8. What is the difference between LCS and substring?**  
Substring must be contiguous; subsequence allows gaps between matched elements.

---

## Running Tests

```bash
cd backend
mvn test
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| API Offline | Start backend with `mvn spring-boot:run` |
| CORS / fetch failed | Serve frontend via `http://` not `file://` |
| Port 8080 in use | Change `server.port` in `application.properties` |
| Empty input error | Fill both text areas |

---

## Authors

College ADA (Algorithms) Project — beginner-friendly implementation for viva and demonstration.

---

## License

Educational use — free to modify for coursework and presentations.
