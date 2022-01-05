import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.awt.*;

class  AppendableOOS extends ObjectOutputStream
{    
    public AppendableOOS(OutputStream out) throws IOException {
        super(out);
    }
     
    @Override
    protected void writeStreamHeader() throws IOException {
        //super.writeStreamHeader();
        //reset();
    }  
}

public class Frame extends JFrame{
	String path;
	int n;
	JPanel p;
	JTextField polje;
	JButton registruj, uloguj;
	
	public Frame ()
	{
		registruj=new JButton("register");  uloguj=new JButton("log in"); registruj.setBackground(new Color(255,255,0)); 
		uloguj.setBackground(new Color(255,255,0)); 
		registruj.setFont(new Font("Plain", Font.PLAIN, 20)); uloguj.setFont(new Font("Plain", Font.PLAIN, 20));
		polje=new JTextField("DMS (Document Management Softver)"); polje.setHorizontalAlignment(JTextField. CENTER); polje.setBackground(new Color(0,255,51)); 
		polje.setFont(new Font("Italic", Font.ITALIC+Font.BOLD, 30)); polje.setEditable(false);
		p=new JPanel();  p.setLayout(new GridLayout(0,2)); p.add(registruj); p.add(uloguj);
		setTitle("DMS"); setSize(800,400); setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null); 
		add(p,BorderLayout.SOUTH); add(polje,BorderLayout.CENTER); setVisible(true);
		
		registruj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				posaljiZahtjev();
			}
		});
		uloguj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unesiPodatke(false);
			}
		});
	}
	
	private void posaljiZahtjev()
	{
		unesiPodatke(true);
	}
	
	private void unesiPodatke(Boolean snimi)
	{
		String ime,korisnickoIme,lozinka;
		ime=JOptionPane.showInputDialog(null, "Unesite vaše ime i prezime:");
		korisnickoIme = JOptionPane.showInputDialog(null, "Unesite korisničko ime:");
		do
		{
			lozinka = JOptionPane.showInputDialog(null, "Unesite lozinku:");
			if (lozinka==null)
				break;
		} while (lozinka.length()==0);
		if(ime==null || korisnickoIme==null || lozinka==null)
			return;
		if(snimi)
			snimiZahtjev(ime, korisnickoIme, lozinka);
		else
			ulogujSe(ime,korisnickoIme,lozinka);
	}
	
	private void snimiZahtjev(String s, String korisnickoIme, String lozinka)
	{
		Korisnik k=new Korisnik (s,korisnickoIme,lozinka);
		File zahtjevi=new File(path +"\\korisnickiZahtjevi");
		HashSet<Korisnik> set = ucitajZahtjeve(zahtjevi);
		FileOutputStream fajl=null;
		ObjectOutputStream izlaz=null;
		Boolean isNewFile=false;

		try {
			if (!zahtjevi.exists()) {
				zahtjevi.createNewFile();
				isNewFile=true;
			}
			fajl = new FileOutputStream(zahtjevi,true);
			
			if(isNewFile)
	            izlaz = new ObjectOutputStream(fajl);
			else
				izlaz=new AppendableOOS(fajl);
			
			while(true) {
				if (set.add(k)) {
					izlaz.writeObject(k);
					JOptionPane.showMessageDialog(null, "Vaš zahtjev je poslat administratoru!"); 
					break;
				}
				else {
					JOptionPane.showMessageDialog(null, "Korisnicko ime ja zauzeto! Unesite novo!"); 
					s=JOptionPane.showInputDialog(null, "Unesite vaše ime i prezime:");
					korisnickoIme = JOptionPane.showInputDialog(null, "Unesite korisničko ime:");
					lozinka = JOptionPane.showInputDialog(null, "Unesite lozinku:");
					k=new Korisnik (s,korisnickoIme,lozinka);
					if (s==null || korisnickoIme==null || lozinka==null)
						break;
				}
			}
	        izlaz.close();	
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	//Koristi se set da bi se izbjeglo slanje zahtjeva sa istim korisnickim imenom
	private HashSet<Korisnik> ucitajZahtjeve(File f)
	{
		HashSet<Korisnik> set=new HashSet<>();
		if (!f.exists())
			return set;
		Korisnik k;
		try {
			FileInputStream fin = new FileInputStream(f);
	        ObjectInputStream oIn = new ObjectInputStream(fin);
	        try {
	            while (true) {
	                k = (Korisnik) oIn.readObject();
	                set.add(k);
	            }
	        } catch (Exception e) {
	        }
	        fin.close();
	        oIn.close();
		} catch (FileNotFoundException e) {
            System.err.println("failed to read : " + e);
        } catch (IOException e) {
            System.err.println("failed to read2 : " + e);
        }
		return set;
	}
	
	private void ulogujSe(String ime,String korisnickoIme, String lozinka)
	{
		Boolean registrovan=false;
		Korisnik k = null;
        File korisnici=new File(path+"\\korisnici");
        if (!korisnici.exists()) {
			JOptionPane.showMessageDialog(null, "Niste registrovani na sistem!"); 
			return;
        }
        LinkedList<Korisnik> lista=new LinkedList<>();
		try {
			FileInputStream fin = new FileInputStream(korisnici);
	        ObjectInputStream oIn = new ObjectInputStream(fin);
	        try {
	            while (true)
	                lista.add((Korisnik) oIn.readObject());
	        } catch (Exception e) {
	        }
	        fin.close();
	        oIn.close();
		} catch (IOException e) {
			//JOptionPane.showMessageDialog(null, "Nema registrovanih korisnika!"); 
        } 
		if (lista.size()==0) {
			JOptionPane.showMessageDialog(null, "Niste registrovani na sistem!"); 
			return;
		}
		for (Korisnik temp: lista)
			if (temp.ime.equals(ime) && temp.korisnickoIme.equals(korisnickoIme) && temp.lozinka.equals(lozinka) ) {
				registrovan=true; k=temp; break;
			}
		if (!registrovan) {
			JOptionPane.showMessageDialog(null, "Niste registrovani na sistem!"); 
			return;
		}
		
		JOptionPane.showMessageDialog(null, "Uspješno ste se prijavili na sistem!"); 
		if (k.brojPrijava==n)
			k=promijeniLozinku(k);
		if(k==null)
			return;
		azurirajKorisnika(korisnici,lista,k);	
		File korisnickiFolder=new File(path+"\\"+k.korisnickoIme);
		if (!korisnickiFolder.exists())
				korisnickiFolder.mkdirs();
		new KorisnickiFrame(this,path,korisnickiFolder,k);
	}
	
	private Korisnik promijeniLozinku(Korisnik k)
	{
		do
		{
			k.lozinka = JOptionPane.showInputDialog(null, "Promijenite lozinku:");
			if (k.lozinka==null)
				return null;
		} while (k.lozinka.length()==0);
		k.brojPrijava=0;
		return k;
	}
	
	private void azurirajKorisnika(File korisnici, LinkedList<Korisnik> lista, Korisnik k)
	{
		k.brojPrijava++; 
		FileOutputStream fajl=null;
		ObjectOutputStream izlaz=null;
		
		try {
			new FileOutputStream(korisnici).close();		//prvo brise sadrzaj cijelog fajla i odmah ga zatvara
			fajl = new FileOutputStream(korisnici,true);
            izlaz = new ObjectOutputStream(fajl);
            for (Korisnik temp: lista)
            	izlaz.writeObject(temp);
            izlaz.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


