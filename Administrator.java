import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Administrator extends JFrame {
	JTextField polje;
	JPanel p;
	JScrollPane skrol;
	JButton pregled, brisanje;
	LinkedList<JButton> lista;
	
	public Administrator()
	{
		p=new JPanel(); 
		p.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 1;
		cons.gridx = 0;	
		ucitajObjekte();	
		while(!lista.isEmpty()) {
			p.add(lista.remove(),cons);
		}
		skrol=new JScrollPane(p);
		
		polje=new JTextField("Primili ste sljedeće zahtjeve za registraciju:"); polje.setHorizontalAlignment(JTextField. CENTER); 
		polje.setBackground(new Color(0,255,51)); polje.setFont(new Font("Italic", Font.ITALIC+Font.BOLD, 30)); polje.setEditable(false);
		pregled=new JButton("Pregled naloga");   brisanje=new JButton("Brisanje naloga");
		setTitle("ADMINISTRATOR"); setSize(800,400); setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setLocationRelativeTo(null); setLayout(new BorderLayout()); 
		add(polje,BorderLayout.NORTH); add(skrol,BorderLayout.CENTER); 
		add(pregled,BorderLayout.WEST);  add(brisanje,BorderLayout.EAST); setVisible(true);
	}
	
	
	public static void main(String[] args) {
		Administrator admin=new Administrator();
	}
	
	public void ucitajZahtjeve()
	{
		Scanner ulaz = null; // ulazni tok 
		String path;
		File zahtjevi;

		try {
			ulaz = new Scanner(new FileReader ("configFile.txt"));			
			path=ulaz.nextLine()+"\\korisnickiZahtjevi.txt";
			ulaz.close();
			zahtjevi=new File(path);
			if(!zahtjevi.exists())
				return;
			
			ulaz = new Scanner(new FileReader (zahtjevi));	
			lista=new LinkedList<>();
			while (ulaz.hasNextLine())
				lista.add(new JButton(ulaz.nextLine()));
			ulaz.close();		
		}
		catch (IOException e) {
			System.out.println("takva datoteka ne postoji");		
		}
	}
	
	public void ucitajObjekte()
	{
		String path;
		File zahtjevi;
		ObjectInputStream ois=null;
		Korisnik k = null;
		
		try {
			Scanner ulaz = new Scanner(new FileReader ("configFile.txt"));			
			path=ulaz.nextLine()+"\\objekti.txt";
			ulaz.close();
			zahtjevi=new File(path);
			if(!zahtjevi.exists())
				return;			
			
			lista=new LinkedList<>();
            FileInputStream fin = new FileInputStream(zahtjevi);
            ObjectInputStream oIn = new ObjectInputStream(fin);
            try {
                while (true) {
                    k = (Korisnik) oIn.readObject();
                    System.out.println(k);
                    lista.add(new JButton(k.toString()));
                }
            } catch (Exception e) {
            }
            fin.close();
            oIn.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.err.println("failed to read : " + e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("failed to read2 : " + e);
        }
	}
}
