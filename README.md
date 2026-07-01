# MiniPoSApplication
# 🎫 Ready App Clone — Queue Display System

A mobile POS queue display system built with **Kotlin** for Android.  
Developed as a learning project under **Monakom Technology**.

---

## 📱 Features

- 🔐 **Login** with client credentials
- 🏢 **Select Company & Terminal** dynamically from API
- 🎫 **Real-time ticket display** via MQTT v5 (Paho)
- 🗂️ **Tab filtering** by order type (Counter, Pickup, Delivery...)
- 🌐 **Multi-language** support (English, ខ្មែរ, 中文)
- ⚙️ **Settings** — language switch + logout
- 🔄 **Auto-refresh** every 30 seconds as backup
- 💾 **Session persistence** — auto login if token still valid

---

## 🏗️ Architecture

This project follows **Clean Architecture** with **MVVM** pattern.

```
com.monakom.readyappclone/
├── base/               ← BaseActivity, BaseViewModel
├── data/
│   ├── local/pref/     ← SessionManager (SharedPreferences)
│   ├── mqtt/           ← MqttManager (Paho MQTT v5)
│   ├── remote/
│   │   ├── api/        ← AuthApi, TicketApi, OrderApi
│   │   ├── dto/        ← Request & Response models
│   │   ├── interceptor/← AuthInterceptor (auto token)
│   │   └── RetrofitClient.kt
│   └── repository/     ← AuthRepository, TicketRepository
├── di/                 ← Dependency Injection (Hilt)
├── domain/
│   ├── model/          ← Domain models
│   └── usecase/        ← Business logic
├── ui/
│   ├── home/           ← HomeActivity, HomeViewModel, TicketAdapter
│   ├── login/          ← LoginActivity, LoginViewModel
│   ├── terminal/       ← TerminalActivity, TerminalViewModel
│   ├── setting/        ← SettingActivity
│   └── language/       ← SelectLanguageActivity
└── utils/
    ├── constants/      ← AppConstants
    ├── enum/           ← OrderStatus, OrderType
    └── extensions/     ← Extension functions
```

---

## 🔌 Tech Stack

| Library | Purpose |
|---|---|
| **Kotlin** | Programming language |
| **Retrofit** | REST API calls |
| **OkHttp** | HTTP client + logging |
| **Gson** | JSON parsing |
| **Paho MQTT v5** | Real-time messaging |
| **LiveData + ViewModel** | MVVM architecture |
| **ViewBinding** | View access |
| **SharedPreferences** | Local storage |
| **Coroutines** | Async operations |

---

## 🌐 API Endpoints

### Auth
```
POST example//adm/v1/api/oauth2          ← Login
GET  example//adm/v1/api/user/info        ← Get user info
GET  example//adm/v1/api/user/user-company/{userId}          ← Get companies
GET  example//adm/v1/api/user/user-td-terminal/{userId}      ← Get terminals
```

### Tickets
```
GET example/api/queue_display/tickets/order-type  ← Get tab types
GET example/api/queue_display/tickets/list        ← Get ticket list
```

---

## 🔌 MQTT

Real-time ticket updates via MQTT v5:

```
Broker:  tcp://[host]:[port]
Topic:   TicketService_uat_TicketReadyBroadcast_{terminalId}
```

When a new ticket is created → MQTT message received → ticket list refreshes automatically!

---

## 🎫 Ticket Status

| Status | Display | Button |
|---|---|---|
| `PREPARING` | Ready | 🟢 Green (active) |
| `RE_CALL` | Re-call | 🟡 Yellow (active) |
| `READY` | Called | ⚪ Grey (disabled) |
| `DONE` | Done | ⚪ Grey (disabled) |

---

## 🌐 Languages Supported

| Code | Language |
|---|---|
| `en` | English |
| `km` | ខ្មែរ (Khmer) |
| `zh` | 中文 (Chinese) |

---

## 🔐 Security

- Token stored in `SharedPreferences`
- `AuthInterceptor` auto-attaches token to every request
- MQTT credentials stored in `local.properties` (not in source code)
- Session cleared on logout

---

## 🚀 Setup

**1. Clone the project**
```bash
git clone https://github.com/yourusername/ReadyAppClone.git
```

**2. Create `local.properties`** in root folder:
```properties
sdk.dir=YOUR_SDK_PATH
mqtt.url="YOUR_MQTT_HOST"
mqtt.port=YOUR_MQTT_PORT
mqtt.username="YOUR_MQTT_USERNAME"
mqtt.password="YOUR_MQTT_PASSWORD"
```

**3. Run the project** in Android Studio

---

## 👨‍💻 Developer

**Seng Bunseu**  
Android Developer Intern  
Monakom Technology

---

## 🏢 Powered by

**Monakom Technology**  
Building innovative solutions for modern businesses.
