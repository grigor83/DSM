import javax.swing.*;
import java.awt.BorderLayout;
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
		registruj=new JButton("register");  uloguj=new JButton("log in");
		polje=new JTextField("DMS (Document Management Softver)"); polje.setHorizontalAlignment(JTextField. CENTER); polje.setBackground(new Color(0,255,51)); 
		polje.setFont(new Font("Italic", Font.ITALIC+Font.BOLD, 30)); polje.setEditable(false);
		p=new JPanel();  p.setLayout(new BorderLayout());   
		p.add(registruj,BorderLayout.WEST); p.add(uloguj,BorderLayout.EAST); p.add(polje,BorderLayout.CENTER);
		setTitle("DMS"); setSize(800,400); setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null); add(p); setVisible(true);
		
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
		String imePrezime,korisnickoIme,lozinka;
		while(true) {
			imePrezime=JOptionPane.showInputDialog(this, "Unesite vaše ime i prezime:");
			korisnickoIme = JOptionPane.showInputDialog(this, "Unesite korisničko ime:");
			lozinka = JOptionPane.showInputDialog(this, "Unesite lozinku:");
			if (imePrezime!=null && korisnickoIme!=null && lozinka!=null)
				break;
		}
		if(snimi)
			snimiZahtjev(imePrezime, korisnickoIme, lozinka);
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
					JOptionPane.showMessageDialog(this, "Vaš zahtjev je poslat administratoru!"); 
					break;
				}
				else {
					JOptionPane.showMessageDialog(this, "Korisnicko ime ja zauzeto! Unesite novo!"); 
					s=JOptionPane.showInputDialog(this, "Unesite vaše ime i prezime:");
					korisnickoIme = JOptionPane.showInputDialog(this, "Unesite korisničko ime:");
					lozinka = JOptionPane.showInputDialog(this, "Unesite lozinku:");
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
}


