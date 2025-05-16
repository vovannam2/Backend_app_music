# 🎧 Spotify Clone – Backend API (User Features)

This is the backend REST API (Spring Boot) for a Spotify-like music app. It supports user authentication, music browsing, playlist management, and profile settings for listeners.

---
## 👨‍💻 Team Information

- **Supervisor**: ThS. Nguyễn Hữu Trung
- **Semester**: 2 – Academic Year: 2024–2025
- **Team Members**:
  - Võ Văn Nam – 22110379
  - Châu Văn Thân – 22110425

---

## 🛠 Tech Stack

- Spring Boot + Spring Security
- MySQL + Spring Data JPA
- JWT Authentication
- Java Mail (OTP via Gmail)
- Maven, Lombok

---

## 🔐 User Features (API Endpoints)

### Authentication
- `POST /api/auth/register` – Register with OTP email verification  
- `POST /api/auth/verify` – Confirm OTP  
- `POST /api/auth/login` – Login with email & password  
- `POST /api/auth/reset` – Reset password via OTP  

### Music Browsing
- `GET /api/songs` – View all songs  
- `GET /api/songs/{id}` – View song details  
- `GET /api/artists/{id}` – View artist profile  

### Playlists & Favorites
- `POST /api/playlists` – Create a new playlist  
- `GET /api/playlists/me` – View user playlists  
- `POST /api/favorites/{songId}` – Add to favorites  
- `GET /api/favorites/me` – View favorite songs  

### User Profile
- `GET /api/users/me` – Get user info  
- `PUT /api/users/me` – Update profile info  

---

## 🧪 How to Run

```bash
git clone https://github.com/yourusername/spotify-api.git
cd spotify-api

# Configure MySQL credentials in application.properties

mvn spring-boot:run
