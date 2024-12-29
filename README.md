# Chelsea FC Trivia App

## Overview

Test your knowledge of Chelsea FC players with this interactive trivia app! Choose between answering 10, 20, or 30 questions in either timed or non-timed mode to discover your level of Chelsea expertise.

## General Features

- **Timed and Non-Timed Modes**: Play at your own pace or challenge yourself against the clock.
- **Player Database**: Questions are based on Chelsea FC players from 1907 to the present.
- **Dynamic Questions**: Randomized and unique questions for each session.

---

## Backend

### Backend Technologies Used

- **Java**
- **Spring Boot**
- **Jsoup**

### Database

- **MongoDB**

### Data Scraping

Data is scraped from Transfermarkt to gather comprehensive stats for Chelsea players since 1907. The scraped attributes include:

- `fullName`
- `shirtNumbers`
- `profileImageUrl`
- `positions`
- `activeAgesAtClub`
- `nationalities`
- `appearances`
- `goals`
- `assists`
- `yellowCardsReceived`
- `redCardsReceived`
- `minutesPlayed`

### API Endpoints

The backend provides the following RESTful endpoints:

#### `GET /api/v1/player`

Returns all player data.

#### `GET /api/v1/player/random`

- Accepts an `attribute` parameter corresponding to a player attribute.
- Returns 3 random player records based on the provided attribute.

#### `GET /api/v1/player/most`

- Accepts an `attribute` parameter for numeric attributes (e.g., goals, assists, etc.).
- Returns the player with the highest value for the specified attribute along with 3 random players ensuring no duplicate values.

---

## Frontend

### Frontend Technologies Used

- **TypeScript**
- **Tailwind CSS**
- **React**
- **Vite**

### Frontend Features

- **Main View**: Displays instructions and modes of play.
- **Dynamic Question Generation**: Randomized questions pulled from the backend.
- **Interactive Gameplay**: Questions adapt dynamically based on randomized selections and backend data.

---

## How It Works

1. The backend scrapes data from Transfermarkt and stores it in MongoDB.
2. The frontend fetches data from backend endpoints to generate trivia questions.
3. Users play trivia by answering randomized questions, with results reflecting their Chelsea FC knowledge level.
