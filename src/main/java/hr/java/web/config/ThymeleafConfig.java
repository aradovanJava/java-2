package hr.java.web.config;

import hr.java.web.servis.KorisnikServis;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

/**
 * Inicijalizacija aplikacije pri pokretanju (ServletContextListener).
 *
 * Pokazuje APPLICATION scope: TemplateEngine, JakartaServletWebApplication i
 * KorisnikServis stvaramo JEDNOM i spremamo u ServletContext kako bi ih svi
 * servleti dijelili tijekom cijelog rada aplikacije.
 *
 * Ovdje također postavljamo zajednički brojač posjeta na 0.
 */
@WebListener
public class ThymeleafConfig implements ServletContextListener {

    public static final String ENGINE = "thymeleafEngine";
    public static final String WEB_APP = "thymeleafWebApp";
    public static final String SERVIS = "korisnikServis";
    public static final String BROJ_POSJETA = "brojPosjeta";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // 1) Thymeleaf web aplikacija (Jakarta varijanta)
        JakartaServletWebApplication app = JakartaServletWebApplication.buildApplication(ctx);

        // 2) Resolver: predlošci u /WEB-INF/views/*.html
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(app);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); // tijekom razvoja: bez keširanja predložaka

        // 3) Engine
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        // 4) Spremi u APPLICATION scope (dijeljeno svima)
        ctx.setAttribute(ENGINE, engine);
        ctx.setAttribute(WEB_APP, app);
        ctx.setAttribute(SERVIS, new KorisnikServis());
        ctx.setAttribute(BROJ_POSJETA, 0);

        ctx.log("Aplikacija inicijalizirana: Thymeleaf engine i servis spremni.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("Aplikacija se gasi.");
    }
}
