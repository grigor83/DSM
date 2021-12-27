import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MojeDugme extends JButton implements ActionListener
{
	static String path;
	static File korisnici=null;
	static Boolean isNewFile;
	JPanel p;
	Korisnik k;
	
	public MojeDugme(JPanel p, Korisnik k, String s)
	{
		super(s);
		addActionListener(this);
		this.p=p; this.k=k;
		if (korisnici==null)
			kreirajFajl();
	}
	
	public void actionPerformed(ActionEvent e){
		if (getText().equals("Pregled naloga")) {
			ucitajKorisnike();
			return;
		}
		
		String[] options = {"Odobrite zahtjev", "Odobrite zahtjev kao premijum", "Odbijte zahtjev"};
		int x = JOptionPane.showOptionDialog(null, "Odobrite zahtjev za registraciju",
                "Click a button",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    
        if(x==0 || x==1) {
        	p.remove(this);
            p.revalidate();
            p.repaint();
        	snimiKorisnika();
        }
  	}
	
	private void kreirajFajl()
	{
		Scanner ulaz = null; // ulazni tok 
		try {
			ulaz = new Scanner(new FileReader ("configFile.txt"));			
			path=ulaz.nextLine();
			File DSMfolder=new File(path);
			if (!DSMfolder.exists())
				DSMfolder.mkdir();
			korisnici=new File(path+"\\korisnici");
			if (!korisnici.exists())
				korisnici.createNewFile();
			isNewFile=true;
		}
		catch (FileNotFoundException e) {
			System.out.println("takva datoteka ne postoji");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ulaz!=null)
			ulaz.close();
	}
	
	private void snimiKorisnika()
	{
		FileOutputStream fajl=null;
		ObjectOutputStream izlaz=null;
		
		try {			
			fajl = new FileOutputStream(korisnici,true);
			
			if(isNewFile) {
	            izlaz = new ObjectOutputStream(fajl);
	            isNewFile=false;
			}
			else
				izlaz=new AppendableOOS(fajl);
			
			izlaz.writeObject(k);
	        izlaz.close();	
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void ucitajKorisnike()
	{
		if (korisnici==null)
			return;
		
		try {
			FileInputStream fin = new FileInputStream(korisnici);
	        ObjectInputStream oIn = new ObjectInputStream(fin);
	        try {
	            while (true) {
	                k = (Korisnik) oIn.readObject();
	                System.out.println(k);
	            }
	        } catch (Exception e) {
	        }
	        fin.close();
	        oIn.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Nema registrovanih korisnika!"); 
        } 
	}
}
