package hr.java.web.model;

/**
 * Jednostavan model korisnika (JavaBean: get/set metode kako bi ih
 * Thymeleaf mogao čitati izrazom ${k.ime}).
 */
public class Korisnik {

    private Long id;
    private String ime;
    private String email;
    private String grad;

    public Korisnik() {
    }

    public Korisnik(Long id, String ime, String email, String grad) {
        this.id = id;
        this.ime = ime;
        this.email = email;
        this.grad = grad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGrad() { return grad; }
    public void setGrad(String grad) { this.grad = grad; }

    @Override
    public String toString() {
        return "Korisnik{id=" + id + ", ime='" + ime + "', email='" + email + "', grad='" + grad + "'}";
    }
}
