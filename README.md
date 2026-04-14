<div align="center">

# ✦ Axiom
### Intelligence & Attendance Control System

*A premium, full-stack Student Attendance Dashboard built for SIES Graduate School of Technology*

![Version](https://img.shields.io/badge/version-1.0.0-e81cff?style=for-the-badge)
![Built With](https://img.shields.io/badge/built%20with-Vite%20%2B%20Vanilla%20JS-646cff?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-10a37f?style=for-the-badge)
![Deployed On](https://img.shields.io/badge/deployed%20on-Vercel-black?style=for-the-badge&logo=vercel)

</div>

---

## 📌 Overview

**Axiom** is a production-grade web application that replaces manual attendance calculations with an intelligent, real-time simulation engine. Built as a Mini Project for **FEL205 — Object Oriented Programming Methodology Lab**, it demonstrates all core OOP concepts from the syllabus through a live, globally-deployed web experience.

Students can simulate future attendance decisions, check ISE exam schedules, and receive smart notifications about their academic standing — all through a minimalist, premium dark-mode interface.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🌊 **144Hz Liquid Intro** | Hardware-accelerated CSS glassmorphism blobs orbit behind a frosted glass overlay on launch |
| 🔐 **Secure Multi-User Auth** | PRN-based login with real-time welcome animation as credentials are typed |
| 📊 **Pre-Populated Dashboard** | All 13 subjects auto-loaded from SIES ERP data for each student profile |
| 🎮 **Simulation Engine** | `[+ Attend]` & `[- Bunk]` buttons simulate future classes and instantly recalculate all stats |
| 🔔 **Smart Notifications** | Toast alerts with iOS sound for RED ALERT, MANDATORY, and UPGRADE POTENTIAL states |
| 📅 **ISE Exam Calendar** | Live countdown cards for all Internal Exam dates with urgency color-coding |
| 🚪 **Logout Flow** | Full session reset with Axiom intro replay on logout |

---

## 🖥️ Demo

> **Live URL:** *(coming soon — Vercel deployment)*

**Login Credentials for Testing:**

| Student | Access ID (PRN) | Passcode (PRN) |
|---|---|---|
| Sharvin Mhatre | `125BT041016` | `125BT041016` |
| Sharvani Jorwekar | `125BT041007` | `125BT041007` |

---

## 🧠 OOP Concepts Demonstrated (FEL205 Syllabus)

This project maps to all 6 modules of the FEL205 syllabus:

```
Module 1 — OOP Fundamentals        → Classes, Objects, Encapsulation, Abstraction, Inheritance, Polymorphism
Module 2 — Classes & I/O           → Constructors, this keyword, static members, access modifiers
Module 3 — Arrays & Strings        → Subject data arrays, String manipulation for notification messages
Module 4 — Inheritance & Abstract  → AttendanceGUI extends JFrame, method overriding via ActionListener
Module 5 — Exception Handling      → Try/catch for invalid input, user-defined validation logic
Module 6 — GUI Programming         → Java Swing (JFrame, JPanel, JButton, JTextField, GridBagLayout)
```

The web frontend translates all the same OOP logic into ES6+ JavaScript classes and the module pattern, proving the universality of Object-Oriented design across languages.

---

## 📐 Core Formulae

```
Attendance %    =  (Attended / Total) × 100

Leave Buffer    =  ⌊ (100 × Attended / TargetPct) − Total ⌋

Recovery Needed =  ⌈ (TargetPct × Total − 100 × Attended) / (100 − TargetPct) ⌉
```

**Marks Bracket:**
| Attendance | Internal Marks |
|---|---|
| ≥ 95% | 5 / 5 |
| ≥ 90% | 4 / 5 |
| ≥ 85% | 3 / 5 |
| ≥ 80% | 2 / 5 |
| ≥ 75% | 1 / 5 |
| < 75% | 0 / 5 ⚠️ |

---

## 🛠️ Tech Stack

```
Frontend       → HTML5, CSS3 (CSS Variables, Flexbox, Grid, Backdrop Filter)
Logic          → Vanilla ES6+ JavaScript (Module Pattern, Closures, DOM API)
Animation      → CSS @keyframes, cubic-bezier easing, requestAnimationFrame
Audio          → Web Audio API
Build Tool     → Vite 8.x
Deployment     → Vercel (Global CDN)
Version Control → Git + GitHub
```

---

## 🚀 Getting Started

### Prerequisites
- Node.js 18+
- npm

### Installation & Local Dev

```bash
# Clone the repository
git clone https://github.com/<your-username>/axiom-attendance-dashboard.git

# Navigate into the project
cd axiom-attendance-dashboard

# Install dependencies
npm install

# Start development server
npm run dev
```

The app will be live at `http://localhost:5173`

### Production Build

```bash
npm run build
```

Output is generated in the `/dist` folder, ready for any static host.

---

## 📁 Project Structure

```
axiom-attendance-dashboard/
├── index.html              # SPA shell — Intro, Login, Dashboard layers
├── main.js                 # App router, auth, simulation engine, notifications
├── style.css               # Full design system — dark mode, glassmorphism, animations
├── src/
│   └── assets/
│       └── antic_ios_17.mp3   # Notification sound
├── public/
│   └── favicon.svg
└── package.json
```

---

## 📋 ISE Exam Schedule (Semester II — 2025-26)

| Subject | Type | Date | Status |
|---|---|---|---|
| EG | ISE | 08-Apr-2026 | ✅ Done |
| DSD | ISE | 09-Apr-2026 | ✅ Done |
| AC | ISE-I | 09-Apr-2026 | ✅ Done |
| AC | PPT | 16-Apr-2026 | 🔴 Urgent |
| AM-II | ISE-II | 17-Apr-2026 | 🔴 Urgent |

---

## 👥 Team

| Avatar | Name | Role | Contribution |
|---|---|---|---|
| **SM** | **Sharvin Mhatre** `125BT041016` | 🏗️ Architect & Lead Developer | Conceptualized the entire project. Built the full web application — SPA routing, 144Hz liquid animations, secure auth, simulation engine, smart notifications, ISE calendar, audio integration, and Netlify deployment. |
| **SJ** | **Sharvani Jorwekar** `125BT041007` | 💡 Feature Contributor | Provided real-world attendance data from SIES ERP and shaped the notification and leave simulation features. |
| **KK** | **Kashvi Kurkute** | 📋 Implementation Planner | Contributed to the project roadmap, module planning, and structural design of the attendance logic flow. |

**Institution:** SIES Graduate School of Technology, Nerul, Navi Mumbai  
**Branch:** Electronics & Computer Science Engineering  
**Academic Year:** 2025–26 | Semester II

---

## 📄 License

This project is open source under the [MIT License](LICENSE).

---

<div align="center">

*Built with precision for FEL205 Mini Project — SIES GST*

**Axiom** — *Intelligence & Attendance Control System*

</div>
