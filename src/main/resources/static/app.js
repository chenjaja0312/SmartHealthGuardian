const form = document.getElementById('logForm');
const logDate = document.getElementById('logDate');
const sleepHours = document.getElementById('sleepHours');
const steps = document.getElementById('steps');
const moodScore = document.getElementById('moodScore');
const moodValue = document.getElementById('moodValue');
const formMessage = document.getElementById('formMessage');
const logTable = document.getElementById('logTable');
const riskBadge = document.getElementById('riskBadge');
const riskMessage = document.getElementById('riskMessage');
const decisionPath = document.getElementById('decisionPath');
const refreshBtn = document.getElementById('refreshBtn');
const gainBtn = document.getElementById('gainBtn');
const gainSummary = document.getElementById('gainSummary');
const gainCards = document.getElementById('gainCards');

logDate.valueAsDate = new Date();
moodScore.addEventListener('input', () => moodValue.textContent = moodScore.value);
refreshBtn.addEventListener('click', loadAll);
gainBtn.addEventListener('click', loadInformationGain);

form.addEventListener('submit', async (event) => {
    event.preventDefault();
    const payload = {
        logDate: logDate.value,
        sleepHours: Number(sleepHours.value),
        steps: Number(steps.value),
        moodScore: Number(moodScore.value)
    };

    try {
        const response = await fetch('/health-logs', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to save health log');
        }
        formMessage.textContent = 'Saved! The risk level was calculated by the decision tree.';
        form.reset();
        logDate.valueAsDate = new Date();
        moodScore.value = 6;
        moodValue.textContent = 6;
        await loadAll();
    } catch (error) {
        formMessage.textContent = error.message;
    }
});

async function loadAll() {
    await Promise.all([loadLogs(), loadRisk(), loadInformationGain()]);
}

async function loadLogs() {
    const response = await fetch('/health-logs');
    const logs = await response.json();
    logTable.innerHTML = logs.map(log => `
        <tr>
            <td>${log.logDate}</td>
            <td>${log.sleepHours} h</td>
            <td>${log.steps.toLocaleString()}</td>
            <td>${log.moodScore}/10</td>
            <td><span class="pill ${riskClass(log.riskLevel)}">${log.riskLevel}</span></td>
            <td><button class="delete-btn" onclick="deleteLog(${log.id})">Delete</button></td>
        </tr>
    `).join('');
}

async function loadRisk() {
    const response = await fetch('/health-logs/risk');
    if (!response.ok) {
        riskBadge.textContent = 'No Data';
        riskBadge.className = 'risk-badge neutral';
        return;
    }
    const risk = await response.json();
    riskBadge.textContent = `${risk.riskLevel} Risk`;
    riskBadge.className = `risk-badge ${riskClass(risk.riskLevel)}`;
    riskMessage.textContent = risk.message;
    decisionPath.innerHTML = risk.decisionPath.map(step => `<li>${step}</li>`).join('');
}

async function loadInformationGain() {
    const response = await fetch('/health-logs/information-gain');
    if (!response.ok) {
        gainSummary.textContent = 'Information gain analysis is not available yet.';
        return;
    }
    const analysis = await response.json();
    gainSummary.textContent = `${analysis.conclusion} Base entropy = ${analysis.baseEntropy}. Total records = ${analysis.totalRecords}.`;
    gainCards.innerHTML = analysis.featureGains.map((gain, index) => `
        <article class="gain-card ${index === 0 ? 'best' : ''}">
            <p class="gain-rank">${index === 0 ? 'Best Root Feature' : 'Candidate Feature'}</p>
            <h3>${gain.featureName}</h3>
            <p><strong>Best threshold:</strong> ${formatThreshold(gain.featureName, gain.bestThreshold)}</p>
            <p><strong>Information Gain:</strong> ${gain.informationGain}</p>
            <p class="gain-note">${gain.explanation}</p>
        </article>
    `).join('');
}

async function deleteLog(id) {
    if (!confirm('Delete this health log?')) return;
    await fetch(`/health-logs/${id}`, { method: 'DELETE' });
    await loadAll();
}

function riskClass(level) {
    return String(level || '').toLowerCase();
}

function formatThreshold(featureName, threshold) {
    if (featureName === 'steps') {
        return `${Math.round(threshold).toLocaleString()} steps`;
    }
    if (featureName === 'sleep_hours') {
        return `${threshold} hours`;
    }
    return `${threshold}/10`;
}

loadAll();
