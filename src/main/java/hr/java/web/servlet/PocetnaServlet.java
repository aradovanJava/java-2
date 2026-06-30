package hr.java.web.servlet;

import hr.java.web.config.ThymeleafConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Početna stranica.
 *
 * Demonstrira:
 *  - životni ciklus: init() i destroy()
 *  - APPLICATION scope: zajednički brojač posjeta u ServletContextu
 *  - SESSION scope: čitanje imena posjetitelja (ako se predstavio)
 */
@WebServlet("/pocetna")
public class PocetnaServlet extends BazniServlet {

    @Override
    public void init() {
        getServletContext().log("PocetnaServlet.init() — servlet spreman.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // APPLICATION scope: povećaj zajednički brojač posjeta (thread-safe blok)
        int posjeti;
        synchronized (getServletContext()) {
            Integer trenutno = (Integer) getServletContext().getAttribute(ThymeleafConfig.BROJ_POSJETA);
            posjeti = (trenutno == null ? 0 : trenutno) + 1;
            getServletContext().setAttribute(ThymeleafConfig.BROJ_POSJETA, posjeti);
        }

        // SESSION scope: ako se posjetitelj predstavio, pozdravi ga imenom
        String posjetitelj = null;
        HttpSession sesija = req.getSession(false); // ne stvaraj sesiju bez potrebe
        if (sesija != null) {
            posjetitelj = (String) sesija.getAttribute("posjetitelj");
        }

        Map<String, Object> model = new HashMap<>();
        model.put("posjeti", posjeti);
        model.put("posjetitelj", posjetitelj);
        model.put("brojKorisnika", servis().broj());

        render(req, res, "pocetna", model);
    }

    @Override
    public void destroy() {
        getServletContext().log("PocetnaServlet.destroy() — oslobađanje resursa.");
    }
}
