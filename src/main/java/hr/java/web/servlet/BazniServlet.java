package hr.java.web.servlet;

import hr.java.web.config.ThymeleafConfig;
import hr.java.web.servis.KorisnikServis;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Map;

/**
 * Bazni servlet — sav rad s Thymeleafom izoliran je ovdje.
 * Ostali servleti nasljeđuju ovu klasu i zovu render(...).
 */
public abstract class BazniServlet extends HttpServlet {

    /** Renderira Thymeleaf predložak s danim modelom u tijelo odgovora. */
    protected void render(HttpServletRequest req, HttpServletResponse res,
                          String predlozak, Map<String, Object> model)
            throws ServletException, IOException {

        ServletContext ctx = getServletContext();
        TemplateEngine engine = (TemplateEngine) ctx.getAttribute(ThymeleafConfig.ENGINE);
        JakartaServletWebApplication app =
                (JakartaServletWebApplication) ctx.getAttribute(ThymeleafConfig.WEB_APP);

        res.setContentType("text/html;charset=UTF-8");

        IWebExchange exchange = app.buildExchange(req, res);
        WebContext context = new WebContext(exchange, req.getLocale());
        if (model != null) {
            model.forEach(context::setVariable);
        }
        engine.process(predlozak, context, res.getWriter());
    }

    /** Pristup dijeljenom servisu iz application scopea. */
    protected KorisnikServis servis() {
        return (KorisnikServis) getServletContext().getAttribute(ThymeleafConfig.SERVIS);
    }
}
