/**
 * Smart Answer Evaluation System - Frontend Application
 * Connects to Java backend LCS API and renders results with visualizations.
 */

// Production backend on Render (update URL after deploy if your service name differs)
const PRODUCTION_API = "https://smart-answer-evaluation-api.onrender.com/api";

/**
 * Uses local backend on localhost; uses Render URL on GitHub Pages.
 */
function getApiBase() {
    const host = window.location.hostname;
    if (host === "localhost" || host === "127.0.0.1" || host === "") {
        return "http://localhost:8080/api";
    }
    if (host.includes("github.io")) {
        return PRODUCTION_API;
    }
    return PRODUCTION_API;
}

const API_BASE = getApiBase();

const SAMPLE_1 = {
    teacher: "Photosynthesis is the process by which green plants convert light energy into chemical energy. Chlorophyll in leaves absorbs sunlight and uses carbon dioxide and water to produce glucose and oxygen.",
    student: "Photosynthesis is a process where plants use sunlight to make food. Chlorophyll absorbs light and combines carbon dioxide and water to create glucose and release oxygen."
};

const SAMPLE_2 = {
    teacher: "Java is an object oriented programming language that supports encapsulation inheritance and polymorphism. It runs on the Java Virtual Machine and is platform independent.",
    student: "Java is a popular object oriented language used for building applications. It uses JVM for platform independence and supports classes objects and inheritance."
};

let lastResult = null;
let debounceTimer = null;

const els = {
    teacherAnswer: document.getElementById("teacherAnswer"),
    studentAnswer: document.getElementById("studentAnswer"),
    teacherWordCount: document.getElementById("teacherWordCount"),
    studentWordCount: document.getElementById("studentWordCount"),
    compareBtn: document.getElementById("compareBtn"),
    realtimeCompare: document.getElementById("realtimeCompare"),
    errorBanner: document.getElementById("errorBanner"),
    resultsSection: document.getElementById("resultsSection"),
    loadingOverlay: document.getElementById("loadingOverlay"),
    backendStatus: document.getElementById("backendStatus"),
    themeToggle: document.getElementById("themeToggle"),
    similarityValue: document.getElementById("similarityValue"),
    progressCircle: document.getElementById("progressCircle"),
    gradeBadge: document.getElementById("gradeBadge"),
    gradeCard: document.getElementById("gradeCard"),
    lcsLength: document.getElementById("lcsLength"),
    matchingCount: document.getElementById("matchingCount"),
    evaluationText: document.getElementById("evaluationText"),
    lcsSequence: document.getElementById("lcsSequence"),
    teacherHighlight: document.getElementById("teacherHighlight"),
    studentHighlight: document.getElementById("studentHighlight"),
    matchingList: document.getElementById("matchingList"),
    dpTableWrap: document.getElementById("dpTableWrap"),
    stepsList: document.getElementById("stepsList"),
    downloadPdf: document.getElementById("downloadPdf"),
    loadSample1: document.getElementById("loadSample1"),
    loadSample2: document.getElementById("loadSample2"),
    clearAll: document.getElementById("clearAll")
};

const CIRCUMFERENCE = 2 * Math.PI * 52;

/**
 * Initializes theme, events, and backend health check on page load.
 */
document.addEventListener("DOMContentLoaded", () => {
    initTheme();
    bindEvents();
    checkBackendHealth();
    updateWordCounts();
});

function bindEvents() {
    els.compareBtn.addEventListener("click", () => compareAnswers(false));
    els.themeToggle.addEventListener("click", toggleTheme);
    els.downloadPdf.addEventListener("click", downloadPdfReport);

    els.loadSample1.addEventListener("click", () => loadSample(SAMPLE_1));
    els.loadSample2.addEventListener("click", () => loadSample(SAMPLE_2));
    els.clearAll.addEventListener("click", clearAll);

    [els.teacherAnswer, els.studentAnswer].forEach((ta) => {
        ta.addEventListener("input", () => {
            updateWordCounts();
            if (els.realtimeCompare.checked) {
                scheduleRealtimeCompare();
            }
        });
    });

    els.realtimeCompare.addEventListener("change", () => {
        if (els.realtimeCompare.checked) {
            scheduleRealtimeCompare();
        }
    });
}

function initTheme() {
    const saved = localStorage.getItem("theme") || "light";
    document.documentElement.setAttribute("data-theme", saved);
}

function toggleTheme() {
    const current = document.documentElement.getAttribute("data-theme") || "light";
    const next = current === "dark" ? "light" : "dark";
    document.documentElement.setAttribute("data-theme", next);
    localStorage.setItem("theme", next);
}

/**
 * Checks if the Java backend is reachable.
 */
async function checkBackendHealth() {
    try {
        const res = await fetch(`${API_BASE}/health`, { method: "GET" });
        if (res.ok) {
            els.backendStatus.textContent = "API Online";
            els.backendStatus.classList.add("online");
            els.backendStatus.classList.remove("offline");
        } else {
            setOfflineStatus();
        }
    } catch {
        setOfflineStatus();
    }
}

function setOfflineStatus() {
    els.backendStatus.textContent = "API Offline — start backend";
    els.backendStatus.classList.add("offline");
    els.backendStatus.classList.remove("online");
}

function updateWordCounts() {
    const t = countWords(els.teacherAnswer.value);
    const s = countWords(els.studentAnswer.value);
    els.teacherWordCount.textContent = `${t} words`;
    els.studentWordCount.textContent = `${s} words`;
}

function countWords(text) {
    if (!text || !text.trim()) return 0;
    return text.trim().split(/\s+/).length;
}

function loadSample(sample) {
    els.teacherAnswer.value = sample.teacher;
    els.studentAnswer.value = sample.student;
    updateWordCounts();
    compareAnswers(false);
}

function clearAll() {
    els.teacherAnswer.value = "";
    els.studentAnswer.value = "";
    hideError();
    els.resultsSection.hidden = true;
    lastResult = null;
    updateWordCounts();
}

function scheduleRealtimeCompare() {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
        const t = els.teacherAnswer.value.trim();
        const s = els.studentAnswer.value.trim();
        if (t && s) {
            compareAnswers(true);
        }
    }, 600);
}

/**
 * Validates inputs and calls the backend compare API.
 */
async function compareAnswers(isRealtime) {
    const teacher = els.teacherAnswer.value.trim();
    const student = els.studentAnswer.value.trim();

    hideError();

    if (!teacher || !student) {
        showError("Please enter both teacher and student answers before comparing.");
        els.resultsSection.hidden = true;
        return;
    }

    if (!isRealtime) {
        setLoading(true);
    }

    try {
        const res = await fetch(`${API_BASE}/compare`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ teacherAnswer: teacher, studentAnswer: student })
        });

        const data = await res.json();

        if (!res.ok) {
            const msg = data.error || data.message || "Comparison failed. Check your inputs.";
            showError(msg);
            els.resultsSection.hidden = true;
            return;
        }

        lastResult = data;
        renderResults(data);
        els.resultsSection.hidden = false;
        els.resultsSection.scrollIntoView({ behavior: "smooth", block: "start" });
    } catch (err) {
        showError(
            "Cannot reach the backend server. Start the Java backend on port 8080 (see README)."
        );
        els.resultsSection.hidden = true;
        setOfflineStatus();
    } finally {
        setLoading(false);
    }
}

function setLoading(loading) {
    els.loadingOverlay.hidden = !loading;
    els.compareBtn.disabled = loading;
    const loader = els.compareBtn.querySelector(".btn-loader");
    const text = els.compareBtn.querySelector(".btn-text");
    if (loader) loader.hidden = !loading;
    if (text) text.hidden = loading;
}

function showError(message) {
    els.errorBanner.textContent = message;
    els.errorBanner.hidden = false;
}

function hideError() {
    els.errorBanner.hidden = true;
}

/**
 * Renders all result sections from the API response.
 */
function renderResults(data) {
    const pct = data.similarityPercentage;
    els.similarityValue.textContent = `${pct.toFixed(1)}%`;
    updateProgressRing(pct);

    els.gradeBadge.textContent = data.grade;
    els.gradeBadge.className = `grade-badge ${gradeClass(data.grade)}`;

    els.lcsLength.textContent = data.lcsLength;
    els.matchingCount.textContent = data.matchingWords?.length ?? 0;
    els.evaluationText.textContent = data.evaluationResult;
    els.lcsSequence.textContent = data.lcsSequence || "(empty)";

    renderHighlights(data);
    renderMatchingList(data.matchingWords || []);
    renderDpTable(data);
    renderSteps(data.algorithmSteps || []);
}

function gradeClass(grade) {
    const map = {
        Excellent: "grade-excellent",
        Good: "grade-good",
        Average: "grade-average",
        Poor: "grade-poor"
    };
    return map[grade] || "";
}

function updateProgressRing(percent) {
    const offset = CIRCUMFERENCE - (percent / 100) * CIRCUMFERENCE;
    els.progressCircle.style.strokeDasharray = CIRCUMFERENCE;
    els.progressCircle.style.strokeDashoffset = offset;
}

/**
 * Highlights words that appear in the LCS matching set.
 */
function renderHighlights(data) {
    const matchSet = new Set((data.matchingWords || []).map((w) => w.toLowerCase()));
    els.teacherHighlight.innerHTML = highlightTokens(data.teacherTokens || [], matchSet);
    els.studentHighlight.innerHTML = highlightTokens(data.studentTokens || [], matchSet);
}

function highlightTokens(tokens, matchSet) {
  if (!tokens.length) return "<em>No words</em>";
  return tokens
    .map((word) => {
      const normalized = word.toLowerCase().replace(/[^a-z0-9]/g, "");
      if (matchSet.has(normalized) || matchSet.has(word.toLowerCase())) {
        return `<span class="match">${escapeHtml(word)}</span>`;
      }
      return escapeHtml(word);
    })
    .join(" ");
}

function escapeHtml(str) {
    const el = document.createElement("div");
    el.textContent = str;
    return el.innerHTML;
}

function renderMatchingList(words) {
    els.matchingList.innerHTML = words
        .map((w, i) => `<li style="--i:${i}">${escapeHtml(w)}</li>`)
        .join("");
}

/**
 * Builds an HTML table for the DP matrix with row/column labels.
 */
function renderDpTable(data) {
    const dp = data.dpTable;
    const teacher = data.teacherTokens || [];
    const student = data.studentTokens || [];

    if (!dp || !dp.length) {
        els.dpTableWrap.innerHTML = "<p>No DP table data.</p>";
        return;
    }

    const maxVal = dp[dp.length - 1][dp[0].length - 1];
    let html = '<table class="dp-table"><thead><tr><th class="corner">DP</th>';
    html += '<th>∅</th>';
    student.forEach((w) => {
        html += `<th>${escapeHtml(w)}</th>`;
    });
    html += "</tr></thead><tbody>";

    for (let i = 0; i < dp.length; i++) {
        html += "<tr>";
        if (i === 0) {
            html += '<th class="row-header">∅</th>';
        } else {
            html += `<th class="row-header">${escapeHtml(teacher[i - 1])}</th>`;
        }
        for (let j = 0; j < dp[i].length; j++) {
            const val = dp[i][j];
            const isMax = i === dp.length - 1 && j === dp[i].length - 1 && val === maxVal;
            html += `<td class="dp-cell${isMax ? " dp-max" : ""}">${val}</td>`;
        }
        html += "</tr>";
    }
    html += "</tbody></table>";
    els.dpTableWrap.innerHTML = html;
}

function renderSteps(steps) {
    els.stepsList.innerHTML = steps
        .map(
            (s) =>
                `<li><strong>Step ${s.stepNumber}:</strong> ${escapeHtml(s.description)}</li>`
        )
        .join("");
}

/**
 * Generates a PDF report of the latest comparison using jsPDF.
 */
function downloadPdfReport() {
    if (!lastResult) {
        showError("Run a comparison first before downloading the PDF.");
        return;
    }

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    const margin = 20;
    let y = margin;

    doc.setFontSize(18);
    doc.text("Smart Answer Evaluation Report", margin, y);
    y += 12;

    doc.setFontSize(11);
    doc.text(`Similarity: ${lastResult.similarityPercentage}%`, margin, y);
    y += 7;
    doc.text(`Grade: ${lastResult.grade}`, margin, y);
    y += 7;
    doc.text(`LCS Length: ${lastResult.lcsLength}`, margin, y);
    y += 7;
    doc.text(`LCS Sequence: ${lastResult.lcsSequence}`, margin, y);
    y += 12;

    doc.setFontSize(12);
    doc.text("Teacher Answer:", margin, y);
    y += 6;
    doc.setFontSize(10);
    const teacherLines = doc.splitTextToSize(els.teacherAnswer.value, 170);
    doc.text(teacherLines, margin, y);
    y += teacherLines.length * 5 + 8;

    doc.setFontSize(12);
    doc.text("Student Answer:", margin, y);
    y += 6;
    doc.setFontSize(10);
    const studentLines = doc.splitTextToSize(els.studentAnswer.value, 170);
    doc.text(studentLines, margin, y);
    y += studentLines.length * 5 + 8;

    doc.setFontSize(11);
    const evalLines = doc.splitTextToSize(lastResult.evaluationResult, 170);
    doc.text(evalLines, margin, y);

    doc.save("lcs-evaluation-report.pdf");
}
