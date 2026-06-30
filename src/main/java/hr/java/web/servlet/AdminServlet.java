package hr.java.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Administracija — demonstrira SIGURNOST iz kontejnera.
 *
 * Putanja /admin/* zaštićena je u web.xml (security-constraint + BASIC prijava),
 * a korisnici i role definirani su u Tomcatovom conf/tomcat-users.xml.
 *
 * Do ovog servleta dolaze samo prijavljeni korisnici s rolom "admin".
 * Tu možemo pročitati tko je prijavljen i koje role ima.
 */
@WebServlet("/admin/pregled")
public class AdminServlet extends BazniServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // podaci o prijavljenom korisniku dolaze iz kontejnera (Tomcat)
        Map<String, Object> model = Map.of(
                "korisnik", req.getRemoteUser() == null ? "?" : req.getRemoteUser(),
                "jeAdmin", req.isUserInRole("admin"),
                "brojKorisnika", servis().broj()
        );
        render(req, res, "admin", model);
    }
}
