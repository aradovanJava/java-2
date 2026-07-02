package hr.java.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.security.auth.Subject;
import java.io.IOException;
import java.security.Principal;
import java.security.Security;
import java.util.Map;

import static java.lang.Long.parseLong;

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

        if(req.isUserInRole( "admin" )) {
            req.setAttribute("userRole", "admin");
        }

        // REQUEST scope: model za pogled (lista korisnika)
        Map<String, Object> model = Map.of("korisnici", servis().dohvatiSve());
        render(req, res, "korisnici", model);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if(req.isUserInRole( "admin" )) {
            req.setAttribute("userRole", "admin");
            String korisnikId = req.getParameter("id");
            servis().obrisi(parseLong(korisnikId));
        }

        resp.sendRedirect(req.getContextPath() + "/korisnici");
    }
}
