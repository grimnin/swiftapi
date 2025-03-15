## 📌 Wymagania

Przed uruchomieniem projektu upewnij się, że masz zainstalowane:

- **Docker** i **Docker Compose**  
- **Java 17+**  
- **Maven** (opcjonalnie, jeśli chcesz budować aplikację ręcznie)  

---

## 🛠️ Uruchomienie projektu

### 🔹 1️⃣ Uruchomienie za pomocą Docker Compose (Zalecane)

Najprostszy sposób na uruchomienie aplikacji to skorzystanie z **Docker Compose**. W katalogu głównym projektu wykonaj komendę:

```sh
docker-compose up --build
```

Spowoduje to uruchomienie dwóch kontenerów:

✅ **MySQL** - baza danych `swift_db`  
✅ **Swift API** - serwis backendowy na porcie **8080**  

Aby sprawdzić status uruchomionych kontenerów, użyj:

```sh
docker ps
```

Aby zatrzymać kontenery:

```sh
docker-compose down
```

---

### 🔹 2️⃣ Ręczne uruchomienie (Lokalnie, bez Dockera)

#### 🔹 Skonfiguruj bazę danych:

Upewnij się, że masz lokalnie uruchomioną instancję **MySQL**, a następnie stwórz bazę danych:

```sql
CREATE DATABASE swift_db;
```

Upewnij się, że użytkownik **root** ma dostęp do tej bazy.

#### 🔹 Zmień konfigurację:

Edytuj plik `src/main/resources/application.properties`, aby wskazać lokalną bazę danych:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/swift_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

#### 🔹 Zbuduj i uruchom aplikację:

```sh
mvn clean package
java -jar target/swiftapi.jar
```

---

## 📬 API Endpointy

Po uruchomieniu aplikacji, API będzie dostępne pod adresem:  
👉 **http://localhost:8080**

Możesz testować je za pomocą **Postmana** lub **cURL**.

| Metoda | Endpoint         | Opis                        |
|--------|-----------------|----------------------------|
| **GET**  | `/api/swift`      | Pobierz wszystkie kody SWIFT |
| **POST** | `/api/swift`      | Dodaj nowy kod SWIFT        |
| **GET**  | `/api/swift/{id}` | Pobierz kod SWIFT o podanym ID |
| **DELETE** | `/api/swift/{id}` | Usuń kod SWIFT o podanym ID |

### 📌 Przykładowe zapytanie `POST`:

```json
{
  "swiftCode": "ABCDPLPWXXX",
  "bankName": "Example Bank",
  "address": "123 Bank Street",
  "country": "PL"
}
```

Dostępna jest również dokumentacja API w **Swagger UI**:  
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## ✅ Testowanie

Aby uruchomić testy jednostkowe, użyj:

```sh
mvn test
```

Testy korzystają z **H2 Database** jako bazy danych w pamięci.

---

## 🔗 Repozytorium GitHub

Udostępnij swoje rozwiązanie w publicznym repozytorium **GitHub**.  
**Plik `README.md` zawiera szczegółowe instrukcje dotyczące uruchamiania i testowania projektu.**

---

## 📌 Technologie użyte w projekcie:

- **Java 17**  
- **Spring Boot**  
- **Spring Data JPA**  
- **MySQL**  
- **Docker & Docker Compose**  
- **JUnit & Mockito**  

---

## 💡 Kontakt

Jeśli masz pytania lub uwagi, skontaktuj się poprzez **GitHub** lub otwórz issue w repozytorium. 🚀
```

---

📥 **Czy chcesz, żebym od razu wygenerował ten plik `README.md`, abyś mógł go pobrać?**