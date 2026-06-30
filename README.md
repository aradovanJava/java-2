# Servleti web primjer

Praktična Java web aplikacija **isključivo sa servletima** (bez Springa), s Thymeleaf pogledima, koja prati koncepte iz predavanja *„Razvoj Java web aplikacija sa Servletima”*. Namijenjena je polaznicima kao temelj za proučavanje i proširivanje.

> U sljedećim predavanjima ovu istu aplikaciju refaktorirat ćemo na **Spring MVC**, a zatim na **Spring Boot**.

## Što je pokriveno (koncept → datoteka)

| Koncept iz prezentacije | Gdje u kodu |
|---|---|
| `HttpServlet`, `doGet`, `doPost`, `init`, `destroy` | `servlet/PocetnaServlet.java`, `DodajKorisnikaServlet.java` |
| `@WebServlet` mapiranje | sve klase u `servlet/` |
| `HttpServletRequest` (getParameter, getSession, getRemoteUser…) | `PredstaviSeServlet`, `DodajKorisnikaServlet`, `AdminServlet` |
| `HttpServletResponse` (setContentType, sendRedirect…) | `BazniServlet`, `OdjavaServlet` |
| Thymeleaf integracija (TemplateEngine, WebContext, process) | `config/ThymeleafConfig.java`, `servlet/BazniServlet.java` |
| Thymeleaf pogledi (`th:text`, `th:each`, `th:if`, `@{...}`) | `webapp/WEB-INF/views/*.html` |
| **request** scope | `KorisniciServlet` → `korisnici.html` |
| **session** scope | `PredstaviSeServlet`, `OdjavaServlet`, brojač u zaglavlju |
| **application** scope | `ThymeleafConfig` (engine + servis), brojač posjeta u `PocetnaServlet` |
| **page** scope | objašnjeno u komentarima (specifično za JSP; ova app koristi Thymeleaf) |
| forward vs redirect (PRG) | `DodajKorisnikaServlet` (redirect), `BazniServlet` (render) |
| `web.xml` descriptor | `webapp/WEB-INF/web.xml` |
| sigurnost (security-constraint, login-config, security-role) | `web.xml` (zaštita `/admin/*`, BASIC) |
| `tomcat-users.xml` | `tomcat-users-primjer.xml` (kopirati u Tomcat) |

## Preduvjeti

- **JDK 17+**
- **Apache Tomcat 10.1+** (Jakarta Servlet 6.0 — `jakarta.*` namespace)
- **IntelliJ IDEA** (Maven je ugrađen)

> Važno: Tomcat **10.1+** je obavezan jer aplikacija koristi `jakarta.servlet` pakete.
> Tomcat 9 (stari `javax.servlet`) neće raditi.

## Otvaranje i izgradnja

1. `File` → `Open…` → odaberi mapu `servleti-web-primjer` (onu s `pom.xml`).
2. IntelliJ povuče ovisnosti (Thymeleaf, Jakarta Servlet API).
3. Izgradi WAR:
   ```bash
   mvn clean package
   ```
   Rezultat: `target/servleti-web-primjer.war`.

## Pokretanje na Tomcatu

### A) Ručno (radi u svakom okruženju)
1. Kopiraj `target/servleti-web-primjer.war` u `<TOMCAT>/webapps/`.
2. Kopiraj role i korisnike iz `tomcat-users-primjer.xml` **unutar**
   `<tomcat-users> … </tomcat-users>` u `<TOMCAT>/conf/tomcat-users.xml`.
3. Pokreni Tomcat: `bin/startup.sh` (Linux/macOS) ili `bin\startup.bat` (Windows).
4. Otvori: `http://localhost:8080/servleti-web-primjer/`

### B) IntelliJ IDEA Ultimate
`Run` → `Edit Configurations…` → `+` → `Tomcat Server` → `Local` →
postavi Application server (Tomcat home), pa u kartici **Deployment** dodaj
artefakt `servleti-web-primjer:war exploded`. Pokreni (Shift+F10).

### C) IntelliJ IDEA Community
Nema ugrađenu Tomcat integraciju — instaliraj dodatak **Smart Tomcat**
(`Settings` → `Plugins`) i konfiguriraj ga na svoj Tomcat, ili koristi opciju A.

## Ekrani

- `/pocetna` — početna; prikazuje brojač posjeta (**application**) i pozdrav (**session**).
- `/predstavi-se` — obrazac koji sprema ime u **session** (POST → redirect).
- `/korisnici` — popis korisnika (**request** scope + Thymeleaf `th:each`).
- `/korisnici/dodaj` — dodavanje korisnika (obrazac, validacija, **PRG** redirect).
- `/admin/pregled` — zaštićena zona (**BASIC** prijava, rola `admin`).
- `/odjava` — gasi sesiju (`invalidate`).

**Prijava za /admin/** (iz `tomcat-users-primjer.xml`):
`ana` / `tajna` (admin) — ima pristup; `ivo` / `123` (korisnik) — nema pristup (403).

## Struktura

```
servleti-web-primjer/
├── pom.xml                         WAR build (Jakarta Servlet provided, Thymeleaf)
├── tomcat-users-primjer.xml        primjer korisnika za Tomcat
└── src/main/
    ├── java/hr/radovan/web/
    │   ├── model/Korisnik.java
    │   ├── servis/KorisnikServis.java     podaci u memoriji (thread-safe)
    │   ├── config/ThymeleafConfig.java    listener: engine + servis (application scope)
    │   └── servlet/
    │       ├── BazniServlet.java          render() — izolirana Thymeleaf logika
    │       ├── PocetnaServlet.java
    │       ├── PredstaviSeServlet.java
    │       ├── OdjavaServlet.java
    │       ├── KorisniciServlet.java
    │       ├── DodajKorisnikaServlet.java
    │       └── AdminServlet.java
    └── webapp/
        ├── index.html                     welcome → /pocetna
        ├── css/stil.css
        └── WEB-INF/
            ├── web.xml                    mapiranja, sigurnost, error-page, session
            └── views/                     Thymeleaf predlošci
                ├── pocetna.html
                ├── predstavi-se.html
                ├── korisnici.html
                ├── dodaj.html
                ├── admin.html
                └── greska.html
```

## Ideje za vježbu (proširivanje)

1. Dodaj **brisanje** i **uređivanje** korisnika (doPost/doPut + nove rute).
2. Zamijeni `KorisnikServis` pravom **bazom** (JDBC ili JPA).
3. Dodaj **filter** (`jakarta.servlet.Filter`) za logiranje svih zahtjeva.
4. Prebaci prijavu na **FORM** autentikaciju (vlastiti `prijava.html`).
5. Dodaj validaciju e-maila i prikaz poruka po polju.
6. Uvedi **page** scope primjer ako prijeđeš na JSP poglede.

---
doc. dr. sc. Aleksander Radovan, prof. struč. stud.
