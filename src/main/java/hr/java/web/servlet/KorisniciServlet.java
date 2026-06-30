package hr.java.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Popis korisnika — demonstrira REQUEST scope.
 *
 * Servlet dohvati podatke iz servisa i preda ih pogledu kao model
 * (vrijedi samo za ovaj jedan zahtjev).
 */
@WebServlet("/korisnici")
public class KorisniciServlet extends BazniServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // REQUEST scope: model za pogled (lista korisnika)
        Map<String, Object> model = Map.of("korisnici", servis().dohvatiSve());
        render(req, res, "korisnici", model);
    }
}
