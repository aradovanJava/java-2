package hr.java.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * "Odjava" — gasi sesiju (SESSION scope nestaje) i vraća na početnu.
 */
@WebServlet("/odjava")
public class OdjavaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession sesija = req.getSession(false);
        if (sesija != null) {
            sesija.invalidate(); // uništi sesiju i sve njezine atribute
        }
        res.sendRedirect(req.getContextPath() + "/pocetna");
    }
}
