package hr.java.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Dodavanje korisnika — demonstrira obradu OBRASCA i obrazac PRG.
 *
 *  - doGet: prikaže prazan obrazac
 *  - doPost: pročita parametre (getParameter), validira, doda u servis,
 *            te napravi redirect na /korisnici (Post-Redirect-Get)
 */
@WebServlet("/korisnici/dodaj")
public class DodajKorisnikaServlet extends BazniServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        render(req, res, "dodaj", Map.of());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String ime = req.getParameter("ime");
        String email = req.getParameter("email");
        String grad = req.getParameter("grad");

        // jednostavna validacija
        if (ime == null || ime.isBlank() || email == null || email.isBlank()) {
            Map<String, Object> model = new HashMap<>();
            model.put("greska", "Ime i e-mail su obavezni.");
            model.put("ime", ime);
            model.put("email", email);
            model.put("grad", grad);
            render(req, res, "dodaj", model);
            return;
        }

        servis().dodaj(ime.trim(), email.trim(), grad == null ? "" : grad.trim());

        // PRG: redirect na popis nakon uspješnog dodavanja
        res.sendRedirect(req.getContextPath() + "/korisnici");
    }
}
