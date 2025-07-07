# 📸 Custom Gallery App

A high-performance custom gallery Android application built using **Jetpack Compose**, that displays images and videos grouped by folders (Albums) with smooth scrolling, modern UI, and clean architecture.

---

## 🚀 Features

- 📁 Album View by folder name on device storage
- 🖼 All Images & All Videos special folders
- 🔢 Album media count display
- 🔄 Toggle between Grid & List view
- 🧭 Navigation between album and detail screens
- 🖼 Fast image loading using **Coil**
- 🧪 Unit-tested ViewModels and UseCases

---

## 🧱 Tech Stack

| Layer       | Tech                                      |
|-------------|-------------------------------------------|
| UI          | Jetpack Compose                           |
| Navigation  | Jetpack Navigation Compose                |
| Image Loading | Coil                                    |
| Architecture| MVVM + Clean Architecture                 |
| DI          | Hilt                                      |
| State Mgmt  | ViewModel + StateFlow + Coroutines        |
| Testing     | JUnit, Mockito, Coroutine Test, Turbine   |

---

## 📲 How to Run

### Prerequisites
- Android Studio Hedgehog (or newer)
- Android SDK 33+
- A real Android device (recommended for media access)

### Steps
```bash
# 1. Clone the repo
git clone https://github.com/your-username/custom-gallery-app.git
cd custom-gallery-app

# 2. Open in Android Studio

# 3. Connect a real device or emulator

# 4. Run the app (Grant media permissions)

⚡ Performance Tip: Use Release Mode
To experience the app's true performance — especially smooth scrolling in LazyVerticalGrid — please run the app in Release mode. The Debug build may cause noticeable lag due to extra overhead from inspection tools.

👉 In Android Studio:
Open the Build Variants panel (bottom-left in Android Studio).
Set the app module's build variant to release.
