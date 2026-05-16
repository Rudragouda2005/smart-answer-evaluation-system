# Deploy to GitHub — Step-by-Step Guide

## Prerequisites

- [Git](https://git-scm.com/download/win) installed
- A [GitHub](https://github.com) account

---

## Part 1: Push code to GitHub

### Step 1 — Create a new repository on GitHub

1. Go to [https://github.com/new](https://github.com/new)
2. **Repository name:** `smart-answer-evaluation-lcs` (or any name you prefer)
3. **Description:** Smart Answer Evaluation System using LCS Algorithm
4. Choose **Public**
5. Do **NOT** check "Add a README" (you already have one)
6. Click **Create repository**

### Step 2 — Push from your PC

Open **PowerShell** in the project folder and run:

```powershell
cd C:\Users\Rudragouda\OneDrive\Desktop\ADA_Project

git init
git add .
git commit -m "Initial commit: Smart Answer Evaluation System with LCS algorithm"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
git push -u origin main
```

Replace `YOUR_USERNAME` and `YOUR_REPO_NAME` with your GitHub username and repo name.

When prompted, sign in with your GitHub account (browser or Personal Access Token).

---

## Part 2: Host the frontend on GitHub Pages (free)

The **frontend** can be published as a static site. The **Java backend** must run on a server (Render, Railway, or your college PC) because GitHub Pages only hosts HTML/CSS/JS.

### Enable GitHub Pages

1. On GitHub, open your repository → **Settings** → **Pages**
2. Under **Build and deployment** → **Source**, choose **GitHub Actions**
3. Push the workflow file included in `.github/workflows/pages.yml` (already in this project)
4. After the workflow runs, your site will be at:

   `https://YOUR_USERNAME.github.io/YOUR_REPO_NAME/`

### Point frontend to your backend

Edit `frontend/js/app.js` and change:

```javascript
const API_BASE = "http://localhost:8080/api";
```

to your deployed backend URL, for example:

```javascript
const API_BASE = "https://your-backend.onrender.com/api";
```

Then commit and push again.

---

## Part 3: Deploy the Java backend (optional)

GitHub Pages **cannot** run Java. Use one of these free options:

| Service | Notes |
|---------|--------|
| [Render](https://render.com) | Free tier, deploy JAR or connect GitHub repo |
| [Railway](https://railway.app) | Free credits for students |
| College server | Run `java -jar target/smart-answer-evaluation-1.0.0.jar` |

Build the JAR locally:

```powershell
cd backend
mvn clean package -DskipTests
java -jar target/smart-answer-evaluation-1.0.0.jar
```

---

## Quick commands reference

```powershell
# Check status
git status

# After editing files
git add .
git commit -m "Describe your changes"
git push

# Clone on another PC
git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `git` not recognized | Install Git, restart terminal |
| Push rejected | `git pull origin main --rebase` then `git push` |
| GitHub login fails | Use a [Personal Access Token](https://github.com/settings/tokens) as password |
| Pages shows 404 | Wait 2–5 min; check Actions tab for build errors |
| API Offline on Pages | Deploy backend separately and update `API_BASE` in `app.js` |
