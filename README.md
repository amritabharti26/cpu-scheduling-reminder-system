# 🚀 CPU Scheduling Reminder System

An Android-based **Alarm & Reminder System** that implements **CPU Scheduling Algorithms** to efficiently manage reminder execution and task scheduling. This project demonstrates the practical implementation of **Operating System concepts** such as **FCFS, SJF, Priority Scheduling, and Round Robin** in a real-world mobile application.

---

## 📌 Project Overview

Traditional reminder applications work on simple time-based triggers and do not efficiently handle multiple reminders scheduled simultaneously. This project introduces an intelligent reminder system where reminder tasks are treated as **CPU processes** and executed according to selected scheduling algorithms.

The application allows users to:

- Add reminder tasks
- Select scheduling algorithms
- Execute reminders in optimized order
- Analyze scheduling performance

This project bridges the gap between **Operating System theory** and **real-world Android development**.

---

## ✨ Features

✅ Add reminders with:

- Task Name  
- Arrival Time  
- Duration Time  
- Priority (for Priority Scheduling)  
- Time Quantum (for Round Robin)

✅ Supports multiple CPU Scheduling Algorithms:

- **FCFS (First Come First Serve)**
- **SJF (Shortest Job First)**
- **Priority Scheduling**
- **Round Robin**

✅ Queue management system

✅ Reminder execution simulation

✅ Waiting Time & Turnaround Time calculation

✅ Real-time reminder notification system

✅ User-friendly Android UI

---

## 🧠 CPU Scheduling Algorithms Implemented

### 1. FCFS (First Come First Serve)

Processes reminders in the order they arrive.

**Execution Example:**

```text
R1 → R2 → R3 → R4
```

---

### 2. SJF (Shortest Job First)

Executes the reminder with the shortest duration first to reduce waiting time.

**Execution Example:**

```text
R2 → R1 → R4 → R3
```

---

### 3. Priority Scheduling

Executes reminders based on assigned priority levels.

- Higher priority task executes first
- Useful for urgent reminders

---

### 4. Round Robin

Executes reminders cyclically using a fixed **time quantum**.

Example:

```text
R1 → R2 → R3 → R4 → R1 → R3
```

Ensures fairness among all reminder tasks.

---

## 🛠️ Tech Stack

### Frontend
- Android XML

### Backend Logic
- Java

### IDE
- Android Studio

### Concepts Used
- Operating System Scheduling
- Queue Data Structure
- Android UI Components
- Handlers & Notifications

---

## 📱 Application Screenshots

### Main Interface
Add reminder tasks and select scheduling algorithm.

### FCFS Scheduling
Execute tasks based on arrival order.

### SJF Scheduling
Shortest duration reminders are executed first.

### Priority Scheduling
Tasks execute according to priority level.

### Round Robin Scheduling
Fair time-sharing execution using time quantum.

> Screenshots available in the project report / repository.

---

## 📂 Project Structure

```text
CPU-Scheduling-Reminder-System/
│── app/
│── MainActivity.java
│── Reminder.java
│── FCFSAlgorithm.java
│── SJFAlgorithm.java
│── PriorityAlgorithm.java
│── RoundRobinAlgorithm.java
│── activity_main.xml
│── README.md
```

---

## ⚙️ Installation & Setup

### Clone the Repository

```bash
git clone https://github.com/your-username/cpu-scheduling-reminder-system.git
```

### Open in Android Studio

1. Open Android Studio  
2. Select **Open Project**  
3. Choose the cloned repository folder  
4. Sync Gradle

### Run the App

- Connect Android device / Emulator
- Click **Run ▶**

---

## 📊 Example Test Case

| Reminder | Arrival Time | Duration | Priority |
|----------|--------------|----------|----------|
| R1 | 0 | 5 | 2 |
| R2 | 1 | 3 | 1 |
| R3 | 2 | 8 | 3 |
| R4 | 3 | 6 | 2 |

---

## 🎯 Objectives

- Implement CPU scheduling concepts practically
- Improve reminder management efficiency
- Reduce waiting time
- Demonstrate real-world OS applications
- Build an Android-based scheduling system

---

## 🔮 Future Scope

- AI-based smart reminder prioritization
- Cloud synchronization
- Voice assistant integration
- Push notifications
- Calendar integration
- IoT device support

---

## 👩‍💻 Developers

**Amrita Bharti**  
B.Tech CSE
CGC College of Engineering, Landran

---

## 📚 References

- *Operating System Concepts* – Abraham Silberschatz
- Android Developer Documentation
- Oracle Java Documentation
- GeeksforGeeks – CPU Scheduling Algorithms

---

⭐ If you found this project useful, consider giving it a star!
