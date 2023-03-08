# M233_Backend-YaSei-StLau-SaLus

Dieses Repository enthält das Backend einer Multi User Applikation.

Das Frontend finden sie hier:

[https://github.com/yannisseiler06/M223_Frontend-YaSei-StLau-SaLus/](https://github.com/yannisseiler06/M223_Frontend-YaSei-StLau-SaLus/)

## Voraussetzungen

Bevor Sie beginnen, stellen Sie sicher, dass Folgendes auf Ihrem Computer installiert ist:

- JDK 18
- PostgreSQL
- IntelliJ
- Docker Desktop

## Installation

1. Klonen Sie das Repository mit Git auf Ihren lokalen Computer:

```bash
git clone https://github.com/vilichtSarina/M223_Backend-YaSei-StLau-SaLus
```

Dadurch wird eine Kopie des Repositorys in einem Verzeichnis auf Ihrem Computer erstellt.

2. Öffnen Sie das Projekt mit IntelliJ

3. Öffnen Sie die Projektstruktur Einstellungen und stellen Sie sicher das Java 18 verwendet wird

4. Nachdem Sie die Einstellungen überprüft haben, müssen Sie einen Docker Container erstellen:

Damit die Applikation läuft, braucht sie eine Datenbank diese wird mittels eines Docker Containers erstellt.

Um den Container zu erstellen, müssen Sie folgenden Befehl in einem Terminal ausführen:

```bash
docker run --name postgres_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
```

Nachdem der Container erstellt wurde, müssen Sie sichergehen das der Container läuft.

7. Nachdem Sie dies erledigt haben können Sie in dem Gradle Tab auf BootRun drücken um das Backend zu starten

Sie sollten jetzt ein funktionierendes Backend mit PostgreSQL Datenbank auf Ihrem Computer eingerichtet haben.

---

## Test

1. Die Postmantests können mithilfe vom JSON-File in /tests/postman in Postman integriert und folglich ausgeführt werden. Dies funktioniert folgendermassen:

- Postman öffnen, zu Collections navigieren, unter der Navbar auf den Import-Button clicken und danach das JSON File auswählen.
