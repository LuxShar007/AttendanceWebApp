// Safe audio loader — won't crash if bundler or browser blocks it
let notifyAudio = null;
try {
  const notifySfx = new URL('./src/assets/antic_ios_17.mp3', import.meta.url).href;
  notifyAudio = new Audio(notifySfx);
  notifyAudio.volume = 0.6;
} catch(e) {
  console.warn('Audio not available:', e);
}

document.addEventListener('DOMContentLoaded', () => {
  // SPA Layers
  const introLayer = document.getElementById('intro-layer');
  const loginLayer = document.getElementById('login-layer');
  const dashboardLayer = document.getElementById('dashboard-layer');

  const axiomText = document.getElementById('axiom-text');
  const axiomI = document.getElementById('axiom-i');
  const loginForm = document.getElementById('login-form');
  
  let time = 0;

  function startAxiomSequence(callback) {
    // Reset states
    axiomText.classList.remove('magenta', 'zoom');
    axiomText.style.transformOrigin = '50% 50%';
    axiomText.style.transform = '';
    axiomText.style.opacity = '1';
    introLayer.classList.remove('zoom-bg'); // Reset illusion
    
    // Show liquid CSS background swirls
    const liquidBg = document.querySelector('.liquid-bg');
    if (liquidBg) liquidBg.classList.add('show');

    // 1. Turn Magenta
    setTimeout(() => {
      axiomText.classList.add('magenta');
    }, 500);

    // 2. Prepare Zoom Origin
    setTimeout(() => {
      // Calculate exact center of the dot of 'i' relative to the h1 container
      const textRect = axiomText.getBoundingClientRect();
      const iRect = axiomI.getBoundingClientRect();
      
      const originX = (iRect.left - textRect.left) + (iRect.width / 2);
      // Dot is near the top of 'i'
      const originY = (iRect.top - textRect.top) + (iRect.height * 0.15); 
      
      axiomText.style.transformOrigin = `${originX}px ${originY}px`;
      
      // Fade out background swirls for a clean zoom
      if (liquidBg) liquidBg.classList.remove('show');
      
      // Trigger illusion background flush
      introLayer.classList.add('zoom-bg');

      // Trigger Zoom
      axiomText.classList.add('zoom');
    }, 2500);

    // 3. Callback (route to next page)
    setTimeout(() => {
      callback();
    }, 4000); // 1.5s after zoom starts
  }

  // Initial Boot Sequence
  startAxiomSequence(() => {
    introLayer.classList.remove('active');
    loginLayer.classList.add('active');
    
    // Revive the liquid background for the Glassmorphic login!
    const liquidBg = document.querySelector('.liquid-bg');
    if (liquidBg) liquidBg.classList.add('show');
  });

  // Logout
  document.getElementById('logout-btn').addEventListener('click', () => {
    // Clear dashboard & reset login fields
    dashboardLayer.classList.remove('active');
    document.getElementById('access-id').value = '';
    document.getElementById('passcode').value = '';
    document.getElementById('login-error').style.display = 'none';
    document.getElementById('welcome-heading').textContent = 'Welcome to Axiom';
    document.getElementById('toast-container').innerHTML = '';

    // Replay intro then show login
    introLayer.classList.add('active');
    startAxiomSequence(() => {
      introLayer.classList.remove('active');
      loginLayer.classList.add('active');
      const liquidBg = document.querySelector('.liquid-bg');
      if (liquidBg) liquidBg.classList.add('show');
    });
  });

  // About Modal
  const aboutModal = document.getElementById('about-modal');
  document.getElementById('about-btn').addEventListener('click', () => {
    aboutModal.classList.remove('hidden');
  });
  document.getElementById('about-close').addEventListener('click', () => {
    aboutModal.classList.add('hidden');
  });
  document.querySelector('.about-backdrop').addEventListener('click', () => {
    aboutModal.classList.add('hidden');
  });

  // Valid users from prompt
  const authUsers = [
    { username: '125bt041016', password: '125BT041016', name: 'Sharvin Mhatre',    branch: 'Electronics & Computer Science Engineering', prn: '125BT041016' },
    { username: '125bt041007', password: '125BT041007', name: 'Sharvani Jorwekar', branch: 'Electronics & Computer Science Engineering', prn: '125BT041007' }
  ];

  // Dynamic Login Welcome Text
  const welcomeHeading = document.getElementById('welcome-heading');
  const accessIdInput = document.getElementById('access-id');

  accessIdInput.addEventListener('input', (e) => {
    const val = e.target.value.trim().toLowerCase();
    const userMatch = authUsers.find(u => u.username.toLowerCase() === val);
    
    if (userMatch) {
      const firstName = userMatch.name.split(' ')[0];
      
      // Build the span fresh each time to retrigger the CSS animation
      const nameSpan = document.createElement('span');
      nameSpan.className = 'user-name';
      nameSpan.textContent = firstName;

      welcomeHeading.innerHTML = 'Welcome ';
      welcomeHeading.appendChild(nameSpan);
      welcomeHeading.appendChild(document.createTextNode(' to Axiom'));
    } else {
      welcomeHeading.textContent = 'Welcome to Axiom';
    }
  });

  // Login handler
  loginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    
    const accessIdVal = document.getElementById('access-id').value.trim().toLowerCase();
    const passcodeVal = document.getElementById('passcode').value.trim();
    const errorMsg = document.getElementById('login-error');

    // Support both lowercase matching and exact casing from system
    const validUser = authUsers.find(u => 
      u.username.toLowerCase() === accessIdVal && 
      u.password === passcodeVal
    );

    if (validUser) {
      errorMsg.style.display = 'none';
      document.getElementById('user-display-name').innerHTML =
        `${validUser.name} &nbsp;·&nbsp; <span style="color:var(--text-secondary);font-weight:400">${validUser.branch} &nbsp;·&nbsp; PRN: ${validUser.prn}</span>`;
      
      loginLayer.classList.remove('active');
      introLayer.classList.add('active');

      // Load specific user data
      const userScope = userData[validUser.password];
      document.getElementById('overall-attendance').textContent = userScope.overall;

      // Run Post-Login Sequence
      startAxiomSequence(() => {
        introLayer.classList.remove('active');
        dashboardLayer.classList.add('active');
        initDashboard(userScope.subjects);
        
        // Keep blobs alive for Dashboard too! (Optional but looks cool)
        // Actually, dashboard has a black background `#dashboard-layer`
        // so it hides the blobs, which is good for readability.
      });
    } else {
      errorMsg.style.display = 'block';
    }
  });

  // ============================================
  // DASHBOARD LOGIC
  // ============================================

  const userData = {
    '125BT041016': { // Sharvin Mhatre
      overall: '90.94%',
      subjects: [
        { name: 'AM-II', total: 39, attended: 35 },
        { name: 'AC', total: 35, attended: 32 },
        { name: 'EG', total: 25, attended: 23 },
        { name: 'DSD', total: 36, attended: 33 },
        { name: 'PCT', total: 23, attended: 21 },
        { name: 'ACL', total: 10, attended: 10 },
        { name: 'EGL', total: 9, attended: 9 },
        { name: 'DSDL', total: 9, attended: 9 },
        { name: 'PCTL', total: 13, attended: 10 },
        { name: 'OOPML (Lab)', total: 20, attended: 20 },
        { name: 'OOPML (Theory)', total: 23, attended: 19 },
        { name: 'EW-II', total: 9, attended: 9 },
        { name: 'IKS', total: 25, attended: 21 }
      ]
    },
    '125BT041007': { // Sharvani Jorwekar
      overall: '91.26%',
      subjects: [
        { name: 'AM-II', total: 39, attended: 35 },
        { name: 'AC', total: 35, attended: 33 },
        { name: 'EG', total: 26, attended: 23 },
        { name: 'DSD', total: 38, attended: 34 },
        { name: 'PCT', total: 23, attended: 20 },
        { name: 'ACL', total: 10, attended: 10 },
        { name: 'EGL', total: 11, attended: 10 },
        { name: 'DSDL', total: 9, attended: 9 },
        { name: 'PCTL', total: 13, attended: 11 },
        { name: 'OOPML (Lab)', total: 22, attended: 20 },
        { name: 'OOPML (Theory)', total: 23, attended: 21 },
        { name: 'EW-II', total: 11, attended: 11 },
        { name: 'IKS', total: 26, attended: 24 }
      ]
    }
  };

  function initDashboard(subjectsArray) {
    const container = document.getElementById('subjects-container');
    const template = document.getElementById('subject-card-template');
    container.innerHTML = '';

    subjectsArray.forEach(sub => {
      const clone = template.content.cloneNode(true);
      const card = clone.querySelector('.subject-card');
      
      card.querySelector('.subject-name').textContent = sub.name;
      
      let currentTotal = sub.total;
      let currentAttended = sub.attended;

      const btnBunk = card.querySelector('.sim-bunk');
      const btnAttend = card.querySelector('.sim-attend');

      const refresh = () => {
        updateCardStats(card, currentTotal, currentAttended);
      };

      btnBunk.addEventListener('click', () => {
        currentTotal++; 
        refresh();
      });

      btnAttend.addEventListener('click', () => {
        currentTotal++;
        currentAttended++;
        refresh();
      });

      refresh();
      container.appendChild(clone);
    });

    // Run AI Global Notification Scan
    analyzeOverallStatus(subjectsArray);

    // Render ISE Calendar & fire exam reminders
    renderIseCalendar();
    checkIseReminders();
  }

  // ============================================
  // ISE CALENDAR
  // ============================================

  // Admin-fed ISE Schedule
  const iseSchedule = [
    { subject: 'EG',   type: 'ISE',   date: '2026-04-08' },  // Past
    { subject: 'DSD',  type: 'ISE',   date: '2026-04-09' },  // Past
    { subject: 'AC',   type: 'ISE-I', date: '2026-04-09' },  // Past
    { subject: 'AC',   type: 'PPT',   date: '2026-04-16' },  // In 2 days ⚠️
    { subject: 'AM-II',type: 'ISE-II',date: '2026-04-17' },  // In 3 days ⚠️
  ];

  function renderIseCalendar() {
    const grid = document.getElementById('ise-grid');
    if (!grid) return;
    grid.innerHTML = '';

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    iseSchedule.forEach(exam => {
      const examDate = new Date(exam.date);
      examDate.setHours(0, 0, 0, 0);
      const diffDays = Math.round((examDate - today) / (1000 * 60 * 60 * 24));

      const card = document.createElement('div');
      card.className = 'ise-card';

      let statusClass = '';
      let countdownText = '';

      if (diffDays < 0) {
        statusClass = 'done';
        countdownText = 'Completed';
      } else if (diffDays === 0) {
        statusClass = 'urgent';
        countdownText = '⚡ TODAY';
      } else if (diffDays <= 3) {
        statusClass = 'urgent';
        countdownText = `🔴 In ${diffDays} day${diffDays > 1 ? 's' : ''}`;
      } else if (diffDays <= 7) {
        statusClass = 'soon';
        countdownText = `🟡 In ${diffDays} days`;
      } else {
        countdownText = `In ${diffDays} days`;
      }

      card.classList.add(statusClass);
      card.innerHTML = `
        <span class="ise-card-type">${exam.type}</span>
        <span class="ise-card-subject">${exam.subject}</span>
        <span class="ise-card-date">${examDate.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' })}</span>
        <span class="ise-card-countdown">${countdownText}</span>
      `;

      grid.appendChild(card);
    });
  }

  function checkIseReminders() {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const urgent = iseSchedule.filter(e => {
      const d = new Date(e.date);
      d.setHours(0, 0, 0, 0);
      const diff = Math.round((d - today) / (1000 * 60 * 60 * 24));
      return diff >= 0 && diff <= 3;
    });

    const soon = iseSchedule.filter(e => {
      const d = new Date(e.date);
      d.setHours(0, 0, 0, 0);
      const diff = Math.round((d - today) / (1000 * 60 * 60 * 24));
      return diff > 3 && diff <= 7;
    });

    let delay = 1800;

    if (urgent.length > 0) {
      setTimeout(() => {
        showToast('danger',
          `⚠️ ISE Alert: ${urgent.length} Exam${urgent.length > 1 ? 's' : ''} This Week!`,
          urgent.map(e => `${e.subject} (${e.type}) — ${new Date(e.date).toLocaleDateString('en-IN', { day: 'numeric', month: 'short' })}`).join(' | ')
        );
      }, delay);
      delay += 3000;
    }

    if (soon.length > 0) {
      setTimeout(() => {
        showToast('info',
          `📅 Upcoming ISE: ${soon.length} exam${soon.length > 1 ? 's' : ''} within 7 days`,
          soon.map(e => `${e.subject} — ${new Date(e.date).toLocaleDateString('en-IN', { day: 'numeric', month: 'short' })}`).join(' | ')
        );
      }, delay);
    }
  }

  // ============================================
  // GLOBAL TOAST SYSTEM & LOGIC
  // ============================================

  function showToast(type, title, message) {
    const container = document.getElementById('toast-container');
    if (!container) return;
    
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    let icon = '';
    if(type === 'danger') icon = '⚠️ ';
    if(type === 'upgrade') icon = '💡 ';
    
    toast.innerHTML = `
      <h4>${icon}${title}</h4>
      <p>${message}</p>
    `;
    
    container.appendChild(toast);
    
    // Play notification sound
    if (notifyAudio) {
      notifyAudio.currentTime = 0;
      notifyAudio.play().catch(() => {});
    }
    
    // Smooth intro animation
    requestAnimationFrame(() => {
      requestAnimationFrame(() => toast.classList.add('show'));
    });
    
    // Auto removal
    setTimeout(() => {
      toast.classList.remove('show');
      setTimeout(() => toast.remove(), 500);
    }, 6000);
  }

  function analyzeOverallStatus(subjectsDataArray) {
    let dangerCount = 0;
    let upgradeCount = 0;

    subjectsDataArray.forEach(sub => {
      if (sub.total === 0) return;
      const p = (sub.attended / sub.total) * 100;
      
      if (p < 75) {
        dangerCount++;
      } else {
        const tgt = getNextTarget(p);
        if (tgt && (tgt - p) <= 2.0 && (tgt - p) >= 0) {
          upgradeCount++;
        }
      }
    });

    if (dangerCount > 0) {
      setTimeout(() => {
        showToast('danger', 'Danger Zone Alert', `You have ${dangerCount} subject(s) with attendance below 75%. Prioritize recovery immediately.`);
      }, 1000);
    }

    if (upgradeCount > 0) {
      setTimeout(() => {
        showToast('upgrade', 'Upgrade Potential', `You are extremely close to a higher mark bracket in ${upgradeCount} subject(s).`);
      }, dangerCount > 0 ? 3000 : 1000);
    }
  }

  // Mathematical logic from previous iterations
  function getMarks(p) {
    if (p >= 95) return 5;
    if (p >= 90) return 4;
    if (p >= 85) return 3;
    if (p >= 80) return 2;
    if (p >= 75) return 1;
    return 0;
  }

  function getCurrentTarget(p) {
    if (p >= 95) return 95;
    if (p >= 90) return 90;
    if (p >= 85) return 85;
    if (p >= 80) return 80;
    if (p >= 75) return 75;
    return null; 
  }

  function getNextTarget(p) {
    if (p < 75) return 75;
    if (p < 80) return 80;
    if (p < 85) return 85;
    if (p < 90) return 90;
    if (p < 95) return 95;
    return null;
  }

  function calcL(t, a, tgt) {
    if (!tgt) return 0;
    return Math.floor((100 * a / tgt) - t);
  }

  function calcR(t, a, tgt) {
    if (!tgt) return 0;
    return Math.ceil(((tgt * t) - (100 * a)) / (100 - tgt));
  }

  function updateCardStats(card, total, attended) {
    const pDisp = card.querySelector('.percentage-display');
    const mDisp = card.querySelector('.marks-display');
    const badge = card.querySelector('.smart-notification');
    const advice = card.querySelector('.tactical-advice');
    
    card.querySelector('.attended-val').textContent = attended;
    card.querySelector('.total-val').textContent = total;

    card.classList.remove('status-red', 'status-mandatory', 'status-upgrade', 'status-safe', 'status-neutral');

    if (total === 0) return;

    const p = (attended / total) * 100;
    const marks = getMarks(p);
    
    pDisp.textContent = `${p.toFixed(1)}%`;
    mDisp.textContent = `${marks}/5`;

    const nextTarget = getNextTarget(p);
    const currTarget = getCurrentTarget(p);

    let l = currTarget ? calcL(total, attended, currTarget) : 0;
    let r = nextTarget ? calcR(total, attended, nextTarget) : 0;

    if (p < 75) {
      card.classList.add('status-red');
      badge.textContent = 'RED ALERT';
      advice.textContent = `Must attend next ${r} classes consecutively to hit 75%.`;
    } 
    else if (nextTarget && (nextTarget - p) <= 2.0 && (nextTarget - p) >= 0) {
      card.classList.add('status-upgrade');
      badge.textContent = 'UPGRADE POTENTIAL';
      advice.textContent = `Within margin! Attend next ${r} class(es) to jump bracket.`;
    }
    else if (l === 0) {
      card.classList.add('status-mandatory');
      badge.textContent = 'MANDATORY';
      advice.textContent = `Zero Buffer. You must attend to maintain ${marks} marks.`;
    }
    else {
      card.classList.add('status-safe');
      badge.textContent = 'SAFE ZONE';
      if (nextTarget) {
        advice.textContent = `Safe to take ${l} leaves.`;
      } else {
        advice.textContent = `Max marks secured. Safe to bunk ${l} classes.`;
      }
    }
  }
});
