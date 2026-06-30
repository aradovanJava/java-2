package hr.java.web.servis;

import hr.java.web.model.Korisnik;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Jednostavan servis s korisnicima u memoriji.
 *
 * Namjerno bez baze podataka — fokus je na servletima i scopeovima.
 * Vježba: zamijeniti ovu implementaciju pravom bazom (JDBC / JPA).
 *
 * Thread-safe: više zahtjeva (niti) može istovremeno čitati/pisati,
 * pa koristimo CopyOnWriteArrayList i AtomicLong.
 */
public class KorisnikServis {

    private final List<Korisnik> korisnici = new CopyOnWriteArrayList<>();
    private final AtomicLong brojac = new AtomicLong(0);

    public KorisnikServis() {
        // početni (demo) podaci
        dodaj("Ana Anić", "ana@example.com", "Zagreb");
        dodaj("Borna Babić", "borna@example.com", "Split");
        dodaj("Carla Carić", "carla@example.com", "Rijeka");
    }

    public List<Korisnik> dohvatiSve() {
        return List.copyOf(korisnici);
    }

    public Korisnik dodaj(String ime, String email, String grad) {
        Korisnik k = new Korisnik(brojac.incrementAndGet(), ime, email, grad);
        korisnici.add(k);
        return k;
    }

    public Optional<Korisnik> pronadji(Long id) {
        return korisnici.stream().filter(k -> k.getId().equals(id)).findFirst();
    }

    public int broj() {
        return korisnici.size();
    }

    public Boolean obrisi(Long id) {
        Optional<Korisnik> korisnikZaBrisanje = pronadji(id);
        if(korisnikZaBrisanje.isPresent()) {
            korisnici.remove(korisnikZaBrisanje.get());
            return Boolean.TRUE;
        }

        return false;
    }
}
