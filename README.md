# 3 Number Challenge

An Android logic puzzle game where players solve a hidden **3-digit mystery number** using deduction and feedback.

The objective is to determine the mystery number within **6 guesses**.

After every guess the game reports:

- Number of correct digits
- Number of digits in the correct position

Classic Mode also includes an optional hint system for players who get stuck.

Available on Google Play:

https://play.google.com/store/apps/details?id=com.connor.newapplicationsummer2019

---

## Gameplay

Example mystery number:

```
482
```

Player guess:

```
412
```

Feedback:

- Correct digits: `2`
- Correct positions: `2`

Use information from previous guesses to eliminate possibilities and solve the puzzle.

---

## Features

- Randomly generated 3-digit puzzles
- Six-attempt challenge format
- Correct digit tracking
- Correct position tracking
- In-game timer
- Optional hint system
- Rewarded hint unlocks
- Mobile-friendly Android interface
- Ad-supported release build

---

## Hint System

Players can unlock a hint by watching a rewarded advertisement.

Current hint behavior:

- Reveals whether the mystery number is:
    - Greater than `500`
    - Less than `500`

Hints may only be used once per round.

---

## Game Rules

- Mystery numbers contain **unique digits**
- The first digit is never `0`
- Players receive up to **6 guesses**
- Winning early ends the round immediately

---

## Project Structure

```text
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── test/
├── build.gradle
└── ...
```

---

## Tech Stack

- Java
- Android SDK
- Android Studio
- Gradle
- Firebase
- Google Mobile Ads SDK

---

## Building From Source

Clone the repository:

```bash
git clone https://github.com/CoderCCM/3-Number-Challenge.git
cd 3-Number-Challenge
```

Open the project in Android Studio and allow Gradle synchronization.

### Firebase Setup Required

This repository intentionally does **not** include Firebase configuration files.

Create:

```text
app/google-services.json
```

using your own Firebase project before building.

The project uses:

- Firebase Analytics
- Firebase Realtime Database
- Google Mobile Ads

---

## Running

1. Open project in Android Studio
2. Add Firebase configuration
3. Sync Gradle
4. Launch emulator or physical device
5. Run the application

---

## Current Status

Implemented:

- Classic Mode
- Hint system
- Ads integration
- Guess history table
- Timer support

In progress / disabled:

- Daily Puzzle mode

---

## Author

Created by **Connor Magnuson**

GitHub:

https://github.com/CoderCCM

Google Play:

https://play.google.com/store/apps/details?id=com.connor.newapplicationsummer2019