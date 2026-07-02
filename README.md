# LookLost – Lost & Found Management System

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Java-orange)
![Database](https://img.shields.io/badge/Database-Firebase-yellow)
![Status](https://img.shields.io/badge/Status-Final%20Year%20Project-blue)

## Overview
LookLost is an Android-based Lost and Found Management System developed as a Final Year Project (FYP). The application helps users report, search, and recover lost items and missing persons through a centralized digital platform.
The system allows users to upload lost or found items, report missing or found persons, communicate through an in-app messaging system, and search records using filters and image-based searching.

---

## Objectives
- Digitize the traditional Lost & Found process.
- Help users report lost or found items quickly.
- Provide a platform for reporting missing persons.
- Enable direct communication between users.
- Improve the chances of recovering lost belongings and missing individuals.

---

# Features

### User Authentication
- User Registration
- Login
- Firebase Authentication
- Secure User Accounts

---

### Home Dashboard
- Personalized Welcome Screen
- Bottom Navigation
- Floating Action Button
- Tab Layout Navigation

---

### Lost & Found Items
- Add Lost Item
- Add Found Item
- Upload Item Image
- Search Items
- Filter by Category
- Item Details Screen
- Contact Owner

---

### Missing Persons
- Report Missing Person
- Report Found Person
- Upload Person Image
- Search by Name
- Search by Image
- View Person Details

---

### Messaging System
- One-to-One Chat
- Real-time Messages
- Conversation List
- Message Timestamp
- Unread Message Counter

---

### Profile
- User Information
- My Items
- My Persons
- Share Application
- Logout

---

### Additional Features
- Category Filter
- Security Questions
- Image Upload
- RecyclerView Lists
- Firebase Realtime Database
- Firebase Storage

---

# Technologies Used
| Technology | Purpose |
|------------|---------|
| Java | Android Development |
| XML | User Interface |
| Android Studio | IDE |
| Firebase Authentication | User Login |
| Firebase Realtime Database | Data Storage |
| Firebase Storage | Image Storage |
| RecyclerView | Dynamic Lists |
| Material Design | UI Components |
| ConstraintLayout | Responsive UI |
| CardView | Modern Cards |

---

# Screens
- Splash Screen
- Login
- Sign Up
- Home
- Items
- Persons
- Item Details
- Person Details
- Chat
- Profile
- My Items
- My Persons
- Security Question Dialog

---

# Project Structure

---
LookLost
│
├── app
│   ├── java
│   │   ├── Activities
│   │   ├── Fragments
│   │   ├── Adapters
│   │   ├── Models
│   │   ├── Firebase
│   │   └── Utilities
│   │
│   ├── res
│   │   ├── drawable
│   │   ├── layout
│   │   ├── values
│   │   ├── mipmap
│   │   └── font
│   │
│   └── AndroidManifest.xml
│
├── gradle
├── build.gradle
├── settings.gradle
└── README.md
---

---
# Firebase Integration

The project uses Firebase services:

- Firebase Authentication
- Firebase Realtime Database
- Firebase Storage
  
Before running the project:

1. Create a Firebase Project.
2. Register your Android App.
3. Download `google-services.json`.
4. Place it inside:
```
app/google-services.json
```
5. Enable:

- Email Authentication
- Realtime Database
- Firebase Storage

---

# Installation

## Clone Repository
```bash
git clone https://github.com/munsif4u/LookLost-FYP.git
```

Open using Android Studio.

---

## Requirements
- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK
- Firebase Account

---

## Build
Sync Gradle.

Run the application on:
- Android Emulator
- Physical Android Device

---

# Future Improvements
- Face Recognition 
- Google Maps Integration
- Push Notifications
- QR Code Verification
- AI-based Image Matching
- Dark Mode
- Admin Dashboard
- Multi-language Support

---

# Screenshots

## 📱 Application Screenshots

### Splash Screen

![Splash Screen](splashscreen.png)

### Home Screen

![Home Screen](homescreen.png)

### Person Screen

![Person Screen](personscreen.png)

### Person Screen

![Person Details](persondetails.png)

### Person Screen

![Items Screen](itemsscreen.png)

---

# Author

**Munsif Khan**
Android Developer
GitHub:
https://github.com/munsif4u

---

# License
This project is developed for academic and educational purposes as a Final Year Project (FYP).

---

## Support
If you found this project helpful, please consider giving it a ⭐ on GitHub.

Thank you!
