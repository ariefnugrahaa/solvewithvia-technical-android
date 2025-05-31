# Technical test for SolveWithVia - Android Request Processing App

Home
![image](https://github.com/user-attachments/assets/e6f10d57-6e87-44dc-a578-fa733dd09101)

Create Request
![image](https://github.com/user-attachments/assets/8c4916ce-0c7f-4174-8185-bef51f006388)

Request List
![image](https://github.com/user-attachments/assets/674ee7a2-4458-441b-b59a-a39c95549bee)

Dark Mode & Font Size Setting
![image](https://github.com/user-attachments/assets/ed3183e5-275d-4d8c-beda-00beb810bce0)

<details>
<summary>📱 App Screenshots</summary>

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/e6f10d57-6e87-44dc-a578-fa733dd09101" width="300" alt="Home">
        <br><b>🏠 Home</b>
      </td>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/8c4916ce-0c7f-4174-8185-bef51f006388" width="300" alt="Create Request">
        <br><b>➕ Create Request</b>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/674ee7a2-4458-441b-b59a-a39c95549bee" width="300" alt="Request List">
        <br><b>📋 Request List</b>
      </td>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/ed3183e5-275d-4d8c-beda-00beb810bce0" width="300" alt="Settings">
        <br><b>⚙️ Dark Mode & Font Size Setting</b>
      </td>
    </tr>
  </table>
</div>

</details>

## 📱 Overview

TestArief is a modern Android application built for the **SolveWithVia** technical interview. This project demonstrates a simplified request processing flow with clean architecture principles, multi-module structure, and comprehensive code quality tools.

### Key Screens
- **Request List Screen**: Displays a button to initiate new requests with success/error message handling
- **Request Detail Screen**: Shows mock request data with Reject button and Approve slider

## 🏗️ Architecture

### UI Framework
**Jetpack Compose** - Modern, declarative UI development for Android

### Design Pattern
**Clean Architecture with MVVM** - Ensuring separation of concerns and testability

### Multi-Module Structure
```
TestArief/
├── app/                    # Main application module
├── feature/
│   └── request/           # Request feature module
├── core/                  # Shared core components
└── buildSrc/             # Build configuration
```

## 🚀 Features

### Core Functionality
✅ **Request Management**
- Request list with intuitive button interactions
- Detailed request view with approval/rejection controls
- Seamless navigation between screens

✅ **User Experience**
- Success/error message handling
- Responsive UI with Material 3 design
- Smooth state transitions

### Technical Features
✅ **Architecture & Quality**
- Multi-module architecture for scalability
- Dependency injection with Hilt
- Comprehensive test coverage with JaCoCo
- Static code analysis with Detekt
- Modern UI built with Jetpack Compose

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Architecture** | Clean Architecture + MVVM |
| **Dependency Injection** | Hilt |
| **Navigation** | Navigation Compose |
| **Build System** | Gradle with Kotlin DSL |
| **Code Quality** | Detekt, KtLint |
| **Testing** | JUnit, MockK, Turbine |
| **Coverage** | JaCoCo |

## 📋 Requirements

- **Android Studio**: Hedgehog | 2023.1.1 or later
- **JDK**: 17
- **Android SDK**: API 34
- **Minimum SDK**: API 24 (Android 7.0)

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/TestArief.git
cd TestArief
```

### 2. Open in Android Studio
1. Launch Android Studio
2. Select **"Open an existing project"**
3. Navigate to the cloned directory and select it
4. Wait for Gradle sync to complete

### 3. Run the Application
- Connect your Android device or start an emulator
- Click the **Run** button or press `Ctrl+R`

## 🔧 Development Commands

### Code Quality Analysis
```bash
# Run Detekt on all modules
./gradlew detektAll

# Run Detekt on specific module
./gradlew :app:detekt

# View Detekt reports (generated in build/reports/detekt/)
# - detekt.html (HTML report)
# - detekt.xml (XML report)
# - detekt.sarif (SARIF report)
```

### Testing
```bash
# Run unit tests
./gradlew test


### Test Coverage
```bash
# Generate coverage report

# For Core Module
.\gradlew :core:data:test :core:domain:test :core:data:jacocoTestReport :core:domain:jacocoTestReport

#For Feature Module
.\gradlew :feature:request:test :feature:request:jacocoTestReport

# View coverage reports at:
# - app/build/reports/jacoco/jacocoTestReport/html/index.html
# - feature/request/build/reports/jacoco/jacocoTestReport/html/index.html
# - core/data/build/reports/jacoco/jacocoTestReport/html/index.html
# - core/domain/build/reports/jacoco/jacocoTestReport/html/index.html
```

### Build Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean
```

## 📊 Code Quality Metrics

### Detekt Configuration
The project uses comprehensive Detekt configuration (`detekt.yml`) with:

- **Complexity Analysis**: Monitors cyclomatic complexity, method length, and class size
- **Code Style**: Enforces consistent formatting and naming conventions
- **Best Practices**: Identifies potential bugs and performance issues
- **Custom Rules**: Tailored rules for Android development

### Coverage Targets
- **Unit Test Coverage**: >80% line coverage
- **Integration Test Coverage**: Critical user flows covered
- **UI Test Coverage**: Key user interactions validated

## 🔄 CI/CD Integration

The project is configured for easy CI/CD integration:

```bash
# Complete quality check pipeline
./gradlew clean detektAll test jacocoTestReport assembleDebug
```

## 📝 Development Workflow

1. **Code Quality Check**: Always run `./gradlew detektAll` before committing
2. **Testing**: Ensure all tests pass with `./gradlew test`
3. **Coverage**: Generate coverage reports to maintain quality standards
4. **Build Verification**: Test both debug and release builds
