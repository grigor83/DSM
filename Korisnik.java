import java.io.Serializable;

public class Korisnik implements Serializable {
	private static final long serialVersionUID = 1L;
	String ime, korisnickoIme, lozinka;
	int brojPrijava;
	Boolean premijum;
	
	public Korisnik(String s, String s1, String s2)
	{
		ime=s; korisnickoIme=s1; lozinka=s2; brojPrijava=0; premijum=false;
	}
	
	public String toString() {
        return new StringBuffer("Ime: ").append(this.ime)
                .append(" Korisnicko: ").append(this.korisnickoIme).append(" Lozinka: ").append(this.lozinka).
                append(" vrsta: ").append(premijum).toString();
    }
	
	@Override
	public int hashCode() {
		return 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Korisnik k=(Korisnik)obj;
		if (korisnickoIme.compareTo(k.korisnickoIme)==0) {
			return true;
		}
		else {
			return false;
		}
	}
}
