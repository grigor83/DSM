import java.io.Serializable;

public class Statistika implements Serializable {
	int brojPristupa;
	String naziv;
	
	public Statistika(String naziv)
	{
		this.naziv=naziv; brojPristupa=0;
	}
	
	public String toString() {
        return new StringBuffer("Naziv fajla: "+naziv+"        ").append(" Broj pristupa: "+brojPristupa).toString();
    }
}
