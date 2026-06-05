# Contributing to Apexium.id

First off, thank you for considering contributing to Apexium.id! It's people like you that make this tool better for everyone.

## Code of Conduct

By participating in this project, you are expected to uphold our Code of Conduct. Please be respectful and professional in all interactions.

## How Can I Contribute?

### Reporting Bugs
*   Check the Issues tab to see if the bug has already been reported.
*   If not, open a new issue with a clear title, description, and steps to reproduce.

### Suggesting Enhancements
*   Open an issue to discuss your idea before implementing it.

### Pull Requests
1.  Fork the repository.
2.  Create a new branch (`git checkout -b feature/amazing-feature`).
3.  Commit your changes (`git commit -m 'Add some amazing feature'`).
4.  Push to the branch (`git push origin feature/amazing-feature`).
5.  Open a Pull Request.

## Development Setup

### Backend (Kotlin & Spring Boot)
- **Requirements**: JDK 21, Maven.
- **Database**: PostgreSQL 17.
- **Cache**: Redis.
- **Object Storage**: MinIO.
- **Environment**: Copy `.env.example` to `.env` and fill in your Salesforce credentials.

Run the backend:
```bash
./mvnw spring-boot:run
```

### Frontend (React & TypeScript)
- **Requirements**: Node.js (Latest LTS).
- **Location**: `/frontend`

Setup:
```bash
cd frontend
npm install
npm run dev
```

## Standards & Style
- Follow existing Kotlin and React patterns.
- Ensure all new features are accompanied by relevant tests.
- Run `npm run lint` for frontend changes.

## Testing
- **Backend**: Run `./mvnw test`.
- **Frontend**: Tests are integrated into the build process where applicable.

---
*Thank you for your contributions!*
