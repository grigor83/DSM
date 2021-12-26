package projekat;

import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.awt.*;

class  AppendableOOS extends ObjectOutputStream
{    
    public AppendableOOS(OutputStream out) throws IOException {
        // TODO Auto-generated constructor stub
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
		JOptionPane.showMessageDialog(this, "Vaš zahtjev je poslat administratoru!"); 
	}
	
	private void unesiPodatke(Boolean snimi)
	{
		String imePrezime=JOptionPane.showInputDialog(this, "Unesite vaše ime i prezime:");
		String korisnickoIme = JOptionPane.showInputDialog(this, "Unesite korisničko ime:");
		String lozinka = JOptionPane.showInputDialog(this, "Unesite lozinku:");
		if(snimi)
			snimiZahtjevKaoObjekat(imePrezime, korisnickoIme, lozinka);
	}
	
	private void snimiZahtjev(String s, String korisnickoIme, String lozinka)
	{
		File zahtjevi=new File(path+"\\korisnickiZahtjevi.txt");
		try {
			if (!zahtjevi.exists())
				zahtjevi.createNewFile();
			PrintWriter izlaz = new PrintWriter (new FileWriter (zahtjevi,true));
            izlaz.println(s+" "+korisnickoIme+" "+lozinka);
            izlaz.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void snimiZahtjevKaoObjekat(String s, String korisnickoIme, String lozinka)
	{
		Korisnik k=new Korisnik (s,korisnickoIme,lozinka);
		File zahtjevi=new File(path+"\\objekti.txt");
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
			
	        izlaz.writeObject(k);
	        izlaz.close();	
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}


