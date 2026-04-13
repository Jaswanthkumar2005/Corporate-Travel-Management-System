# Corporate Travel Itinerary & Approval Workflow System

A full-stack enterprise solution for managing corporate travel requests, approvals, bookings, and expense reconciliation.

## 🚀 Overview

This system streamlines the travel request process within an organization, involving multiple stakeholders including Employees, Managers, Finance, and Travel Desks. It ensures budget compliance, approval hierarchy enforcement, and transparent tracking of travel lifecycles.

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.4, Spring Data JPA, Hibernate, Maven
- **Database**: PostgreSQL
- **Frontend**: React, Vite, Vanilla CSS, Axios, Lucide Icons
- **Architecture**: RESTful API with Role-Based Access Simulation

## 📋 Core Features

- **Workflow Management**: Tracks requests through `DRAFT` -> `SUBMITTED` -> `MANAGER_APPROVED` -> `FINANCE_APPROVED` -> `BOOKED` -> `COMPLETED`.
- **Role-Based Dashboards**:
  - **Employee**: Create travel requests, track status, and submit expenses.
  - **Manager**: Review team requests and Approve/Reject.
  - **Finance**: Budget verification and final approval.
  - **Travel Desk**: Itinerary management and booking confirmation.
- **Budget Compliance**: Real-time budget tracking per department/cost center. Finance approval is blocked if the estimated cost exceeds the remaining budget.
- **Modern UI**: Sleek dark mode interface with glassmorphism and real-time status updates.

## ⚙️ Setup Instructions

### Prerequisites
- JDK 17 or higher
- Node.js (v18+)
- PostgreSQL 14+

### 1. Database Setup
1. Open your PostgreSQL console or pgAdmin.
2. Create a new database:
   ```sql
   CREATE DATABASE travel_db;
   ```
3. Update `backend/src/main/resources/application.properties` with your local PostgreSQL credentials if different from defaults:
   - `spring.datasource.username=postgres`
   - `spring.datasource.password=password`

### 2. Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   *The system will automatically create tables and load mock data on first run.*

### 3. Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
4. Open your browser to `http://localhost:5173`.

## 🧪 Demo Data (Included)

The application initializes with the following sample users for testing:
- **Alice Employee**: `Role: EMPLOYEE` (Reports to John)
- **John Manager**: `Role: MANAGER`
- **Bob Finance**: `Role: FINANCE`
- **Charlie Travel**: `Role: TRAVEL_DESK`

*You can switch between these users using the dropdown in the header for easy demonstration.*

## 📂 Project Structure

```text
├── backend/
│   ├── src/main/java/com/travel/system/
│   │   ├── controllers/    # API Endpoints
│   │   ├── models/         # JPA Entities & Enums
│   │   ├── repositories/   # Data Access Layer
│   │   └── services/       # Workflow & Budget Logic
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── App.jsx         # Main Dashboard & Logic
│   │   └── index.css       # Design System
│   └── package.json
└── README.md
```

## 📜 Business Rules Implemented
- Managers cannot approve requests for employees outside their team (simulated via managerId).
- Finance approval checks for budget availability in the employee's department and cost center.
- Travel requests must follow a strict status lifecycle.
- Role switching is built into the UI for evaluation and testing purposes.
