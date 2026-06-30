package hr.java.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

/**
 * "Predstavi se" — demonstrira SESSION scope.
 *
 *  - doGet: prikaže obrazac
 *  - doPost: spremi ime u sesiju (HttpSession) i preusmjeri na početnu (PRG)
 *
 * Ime ostaje zapamćeno kroz više zahtjeva istog korisnika dok sesija traje.
 */
@WebServlet("/predstavi-se")
public class PredstaviSeServlet extends BazniServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        render(req, res, "predstavi-se", Map.of());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String ime = req.getParameter("ime");

        if (ime == null || ime.isBlank()) {
            // vrati obrazac s porukom o grešci (request scope atribut)
            render(req, res, "predstavi-se", Map.of("greska", "Ime je obavezno."));
            return;
        }

        // SESSION scope: spremi ime; getSession() stvara sesiju ako ne postoji
        HttpSession sesija = req.getSession();
        sesija.setAttribute("posjetitelj", ime.trim());

        // PRG: nakon POST-a radimo redirect (sprječava dvostruko slanje obrasca)
        res.sendRedirect(req.getContextPath() + "/pocetna");
    }
}
