📅 Custom Calendar Android Application

A comprehensive mobile calendar application designed to help users efficiently manage their time, schedule meetings, and track daily activities. Built natively for Android using Java, this application offers a secure and intuitive interface for personal schedule management.

📖 Table of Contents

About the Project

Key Features

Screenshots

Technologies Used

Architecture & Design

Getting Started

Future Improvements

📝 About the Project

This project was developed to address the need for a specific, customizable time management tool. Unlike standard calendar apps, this solution focuses on specific user-defined categories, visual cues for event density (colored stars), and secure local data storage. It allows users to seamlessly switch between different temporal views (Month, Week, Day) and manage recurring events with ease.

✨ Key Features

🔐 User Authentication

Secure Login/Signup: Users must create an account to access their private calendar.

Security: Passwords are hashed using SHA-256 encryption before storage, ensuring user data privacy.

Input Validation: Robust validation for username length and password strength (regex).

🗓️ Versatile Calendar Views

Monthly View: High-level overview of the month with navigation buttons.

Weekly View: 7-day layout to plan the week ahead.

Daily View: Hour-by-hour breakdown for precise scheduling.

Upcoming Events: A dedicated list view sorting all future events chronologically.

⚡ Event Management

CRUD Operations: Create, Read, Update, and Delete events.

Recurring Events: Functionality to set events that repeat daily, weekly, monthly, or annually.

Categories: Assign categories (e.g., Work, Family, Health, Sports) to events.

Color Coding: Events change color based on their assigned category for quick visual recognition.

🎨 Visual Indicators

Event Stars: Dates with events are marked with a star icon.

Dynamic Styling: The color of the star changes based on the number of events that day:

1 Event: Magenta

2 Events: Purple

3 Events: Ocean Green

4+ Events: Golden

📷 Screenshots

(Placeholder: Add screenshots of your app here)

Login Screen

Monthly View

Add Event







🛠️ Technologies Used

Core Tech

Language: Java (JDK 8+)

Environment: Android Studio

Database: SQLite (Local storage for User and Event data)

UI: XML (Extensible Markup Language)

Key Libraries & Components

Android SDK: Core Android components (Activity, Fragment, Intent).

Layouts: LinearLayout, RelativeLayout, FrameLayout, TextInputLayout.

Widgets: RecyclerView, ListView, DatePicker, TimePicker, Spinner.

Java Util: ArrayList, Collections (for sorting events), java.time (LocalDate, LocalTime).

🏗️ Architecture & Design

The application follows standard Android development practices:

Object-Oriented Programming: Extensive use of Classes, Encapsulation, and Constructors.

MVC Pattern: Separation of logic (Java classes), data (SQLite), and presentation (XML).

Data Persistence: - SQLiteDBHandler: Manages database creation and version management.

Tables: Users (stores credentials) and Events (stores event details).

🚀 Getting Started

Prerequisites

Android Studio installed on your machine.

Java Development Kit (JDK).

An Android device or Emulator (AVD).

Installation

Clone the repository

git clone [https://github.com/yourusername/calendar-app.git](https://github.com/yourusername/calendar-app.git)


Open in Android Studio

Launch Android Studio.

Select "Open an Existing Project" and navigate to the cloned folder.

Build the Project

Wait for Gradle to sync dependencies.

Run the App

Connect your device or launch the Emulator.

Click the "Run" button (Green arrow).

🔮 Future Improvements

Based on user evaluation, future updates may include:

Search & Filter: Advanced filtering options for the Upcoming Events page.

Notifications: Push notifications/reminders set for specific times before an event.

Visual Assets: Ability to attach photos or icons to specific events.

Theming: More color themes for the user interface.

Created by [Your Name]
