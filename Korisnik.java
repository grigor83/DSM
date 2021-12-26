import java.io.Serializable;

public class Korisnik implements Serializable {
	String ime, korisnickoIme, lozinka;
	
	public Korisnik(String s, String s1, String s2)
	{
		ime=s; korisnickoIme=s1; lozinka=s2;
	}
	
	public String toString() {
        return new StringBuffer("Ime: ").append(this.ime)
                .append(" Korisnicko: ").append(this.korisnickoIme).append(" Lozinka: ").append(this.lozinka).toString();
    }
}
