# 🚀 Sector Connect API (Backend)

Sector Connect API is a Spring Boot 3.4.3 (Java 17) application providing authentication, user management and sector selection
functionality.

### 📡 API Endpoints

| Method   | Endpoint             | Description                                                                        |
|----------|----------------------|------------------------------------------------------------------------------------|
| **POST** | `/api/auth/register` | Registers a new user with a unique username, name and password.                    |
| **POST** | `/api/auth/login`    | Authenticates a user and stores a JWT token in a HttpOnly Cookie.                  |
| **GET**  | `/api/user`          | Retrieves the details of the currently logged-in user, including selected sectors. |
| **POST** | `/api/user`          | Updates the logged-in user's name, sector selections and agreement status.         |
| **GET**  | `/api/sectors`       | Fetches all parent (top-level) sectors along with their sub-sectors.               |

## 🗄️ Setting Up the Database with Docker

Run the following commands to set up PostgreSQL in Docker:
```bash
cd docker/
docker build -t sectorconnect-db .
docker run -d --publish 5432:5432 --name sectorconnect-db sectorconnect-db
```

## 🖥️️ Running the Application

Build the project:
```bash
./gradlew build
```

Before running the application, ensure that the **JWT secret key** is set as an environment variable.
```bash
export JWT_SECRET="your-secure-key-from-secrets-manager"
```

Start the Spring Boot Server:
```bash
./gradlew bootRun
```
The backend will be running at: http://localhost:8080

## 🧪 Running Tests

Run unit tests:
```bash
./gradlew test
```
