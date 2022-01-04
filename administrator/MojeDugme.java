import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class MojeDugme extends JButton implements ActionListener
{
	static LinkedList<Korisnik> lista=null;
	static String path;
	static File korisnici,zahtjevi;
	static Boolean isNewFile=false;
	JPanel p;
	Korisnik k;
	
	public MojeDugme(JPanel p, Korisnik k, String s)
	{
		super(s);
		addActionListener(this);
		this.p=p; this.k=k;
		if (lista==null)
			lista=new LinkedList<>();
		if (k!=null)
			lista.add(k);
		setBackground(new Color(255,255,0)); 
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
    
        if(x==0 || x==1 || x==2) {
        	p.remove(this);
            p.revalidate();
            p.repaint();
            obrisiZahtjev();
            if (x!=2)
            	snimiKorisnika(x);
        }
  	}
	
	private void kreirajFajl()
	{
		try {
			File DSMfolder=new File(path);
			if (!DSMfolder.exists())
				DSMfolder.mkdir();
			korisnici.createNewFile();
			isNewFile=true;
		}
		catch (FileNotFoundException e) {
			System.out.println("takva datoteka ne postoji");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void snimiKorisnika(int izbor)
	{
		if (!korisnici.exists())
			kreirajFajl();
		
		if (izbor==1)
			k.premijum=true;		
		FileOutputStream fajl=null;
		ObjectOutputStream izlaz=null;
		
		try {			
			fajl = new FileOutputStream(korisnici,true);
			if(isNewFile) {
	            izlaz = new ObjectOutputStream(fajl);
	            isNewFile=false;
			}
			else {
				izlaz=new AppendableOOS(fajl);
			}
			
			izlaz.writeObject(k);
	        izlaz.close();	
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void ucitajKorisnike()
	{
		if (!korisnici.exists()) {
			JOptionPane.showMessageDialog(null, "Nema registrovanih korisnika!"); 
			return;
		}
		
		LinkedList<Korisnik> lista=new LinkedList<>();
		try {
			FileInputStream fin = new FileInputStream(korisnici);
	        ObjectInputStream oIn = new ObjectInputStream(fin);
	        try {
	            while (true) {
	                k = (Korisnik) oIn.readObject();
	                lista.add(k);
	            }
	        } catch (Exception e) {
	        }
	        fin.close();
	        oIn.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Nema registrovanih korisnika!"); 
        } 
		if (lista.size()==0) {
			JOptionPane.showMessageDialog(null, "Nema registrovanih korisnika!"); 
			return;
		}
		
		DefaultTableModel model=new DefaultTableModel();
		model.addColumn("Broj korisnika"); model.addColumn("Ime i prezime korisnika"); model.addColumn("Korisničko ime"); 
		model.addColumn("Lozinka"); model.addColumn("Vrsta naloga");
		Object podaci[] = new Object[5];
		for (int i=0;i<lista.size();i++) {
			podaci[0]=i+1;
			podaci[1]=lista.get(i).ime;
			podaci[2]=lista.get(i).korisnickoIme;
			podaci[3]=lista.get(i).lozinka;
			podaci[4]=lista.get(i).premijum ? "premijum nalog":"osnovni nalog";
			model.addRow(podaci);
		}
		JTable tabela=new JTable(model); tabela.setDefaultEditor(Object.class, null); 
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane skrol=new JScrollPane(tabela); tabela.setBackground(new Color(0,255,51));
		JButton delete=new JButton("Obriši nalog"); delete.setBackground(new Color(0,255,51));
		delete.addActionListener(new ActionListener() 
	      {
	         @Override
	         public void actionPerformed(ActionEvent ae) 
	         {
	            if(tabela.getSelectedRow() != -1) 
	            {
	            	String korisnickoIme=(String) tabela.getValueAt(tabela.getSelectedRow(), 2);
	            	obrisiNalog(lista,korisnickoIme);
	                model.removeRow(tabela.getSelectedRow());
	                JOptionPane.showMessageDialog(null, "Nalog je uspješno obrisan!");
	            }
	         }
	      });
		JFrame okvir=new JFrame("PREGLED KORISNIKA");
		okvir.setSize(800,400); okvir.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		okvir.setLocationRelativeTo(null); okvir.setLayout(new BorderLayout());
		okvir.add(skrol,BorderLayout.CENTER); okvir.add(delete,BorderLayout.SOUTH); okvir.setVisible(true);
	}
	
	private void obrisiZahtjev()
	{
		//U stvari, ovaj metod brise sve korisnicke zahtjeve iz fajla i onda upisuje azuriranu listu
        lista.remove(k);
        FileOutputStream fajl=null;
		ObjectOutputStream izlaz=null;
		
		try {
			new FileOutputStream(zahtjevi).close();		//prvo brise sadrzaj cijelog fajla i odmah ga zatvara
			fajl = new FileOutputStream(zahtjevi,true);
            izlaz = new ObjectOutputStream(fajl);
            for (Korisnik temp: lista)
            	izlaz.writeObject(temp);
            izlaz.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void obrisiNalog(LinkedList<Korisnik> l, String korisnickoIme)
	{
		for (Korisnik temp:l)
			if(temp.korisnickoIme.equals(korisnickoIme)) {
				l.remove(temp);
				break;
			}
		//Sad snimam cijelu listu bez obrisanog naloga u fajl
		FileOutputStream fajl=null;
		ObjectOutputStream izlaz=null;
		
		try {
			new FileOutputStream(korisnici).close();		//prvo brise sadrzaj cijelog fajla i odmah ga zatvara
			fajl = new FileOutputStream(korisnici,true);
            izlaz = new ObjectOutputStream(fajl);
            for (Korisnik temp: l)
            	izlaz.writeObject(temp);
            izlaz.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
