# Deploy Java Backend to Render (Free)

This connects your **GitHub Pages frontend** to a live **Spring Boot API** so **Compare Answers** works online.

---

## Your URLs after setup

| Service | URL |
|---------|-----|
| **Frontend** (GitHub Pages) | https://rudragouda2005.github.io/smart-answer-evaluation-system/ |
| **Backend** (Render) | https://smart-answer-evaluation-api.onrender.com |
| **Health check** | https://smart-answer-evaluation-api.onrender.com/api/health |

---

## Step 1 — Push latest code to GitHub

In PowerShell:

```powershell
cd C:\Users\Rudragouda\OneDrive\Desktop\ADA_Project
git add .
git commit -m "Add Render backend deployment config"
git push
```

---

## Step 2 — Create Render account

1. Go to [https://render.com](https://render.com)
2. Sign up with **GitHub** (same account: `Rudragouda2005`)
3. Authorize Render to access your repositories

---

## Step 3 — Deploy with Blueprint (easiest)

1. Open [https://dashboard.render.com/blueprints](https://dashboard.render.com/blueprints)
2. Click **New Blueprint Instance**
3. Connect repository: **`smart-answer-evaluation-system`**
4. Render reads `render.yaml` automatically
5. Click **Apply** / **Deploy**
6. Wait 5–10 minutes for the first build (Docker + Maven)

When status is **Live**, copy your service URL (should end with `.onrender.com`).

---

## Step 3 (alternative) — Manual Web Service

If Blueprint does not work:

1. [https://dashboard.render.com](https://dashboard.render.com) → **New +** → **Web Service**
2. Connect **`Rudragouda2005/smart-answer-evaluation-system`**
3. Settings:

   | Field | Value |
   |-------|--------|
   | **Name** | `smart-answer-evaluation-api` |
   | **Region** | Singapore (or closest to you) |
   | **Branch** | `main` |
   | **Root Directory** | `backend` |
   | **Runtime** | **Docker** |
   | **Dockerfile Path** | `./Dockerfile` |
   | **Instance Type** | Free |

4. **Advanced** → Health Check Path: `/api/health`
5. Click **Create Web Service**

---

## Step 4 — Update frontend API URL (if needed)

If your Render URL is **different** from  
`https://smart-answer-evaluation-api.onrender.com`, edit `frontend/js/app.js`:

```javascript
const PRODUCTION_API = "https://YOUR-SERVICE-NAME.onrender.com/api";
```

Then push again:

```powershell
git add frontend/js/app.js
git commit -m "Update production API URL"
git push
```

GitHub Pages will redeploy in 1–2 minutes.

---

## Step 5 — Test

1. Open: https://smart-answer-evaluation-api.onrender.com/api/health  
   - Should show: `{"status":"UP",...}`
2. Open your GitHub Pages site
3. Status badge should show **API Online**
4. Load a sample answer → **Compare Answers**

---

## Free tier notes

- Render **free** services **sleep after 15 minutes** of no traffic
- First request after sleep may take **30–60 seconds** (cold start)
- For college demos, open the health URL once before presenting

---

## Troubleshooting

| Problem | Fix |
|---------|-----|
| Build failed on Render | Check **Logs** tab; ensure `backend/Dockerfile` exists on GitHub |
| API Offline on Pages | Wrong URL in `PRODUCTION_API`; verify health URL in browser |
| CORS error | Backend already allows all origins; redeploy backend |
| 502 / timeout | Wait for cold start; upgrade plan or keep service warm |
| Compare works locally only | Push `app.js` changes; clear browser cache |

---

## Redeploy after code changes

```powershell
git add .
git commit -m "Backend update"
git push
```

Render auto-redeploys on every push to `main`.
