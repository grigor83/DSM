import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Korisnik implements Serializable {
	private static final long serialVersionUID = 1L;
	KorisnickiFrame kf;
	String ime, korisnickoIme, lozinka, imeFajla;
	int brojPrijava;
	Boolean premijum;
	File mojFolder;
	JTextArea papir;
	HashMap<Integer, String> fajlovi;
	
	public Korisnik(String s, String s1, String s2)
	{
		ime=s; korisnickoIme=s1; lozinka=s2; brojPrijava=0; premijum=false; 
	}
	
	public String toString() {
        return new StringBuffer("IME: "+this.ime+"        ").append(" KORISNIČKO IME: "+this.korisnickoIme+"        ")
        		.append(" LOZINKA: "+this.lozinka+"        ").toString();
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
	
	public void obrisi(JTextPane tekst, File[] files, String naziv)
	{
		if (files != null) {
            for (File file : files) {
            	if (file.getAbsolutePath().equals(naziv)) {
            		if (!file.isDirectory() && !file.getName().contains(".txt"))
                    {
                    	JOptionPane.showMessageDialog(null, "Ne možete obrisati ovaj tip fajla!"); 
                    	kf.addStatistics(file,1); return;
                    }
            		deleteDir(file); 
            		tekst.setText("");
            		izlistaj(tekst, mojFolder.listFiles(),mojFolder,0);		
            		tekst.revalidate();
                    tekst.repaint();
            		break;
            	}
            	if (file.isDirectory()) {
            		obrisi(tekst,file.listFiles(),naziv);
            	}
            }
		}
	}
	
	private void deleteDir(File dir)
	{
		LinkedList<Statistika> lista=new LinkedList<Statistika>();
		Statistika s;	Boolean prazna=false;
		//ucitavam listu
		try {
			if(!kf.statistika.exists()) {
				prazna=true;
			}
			if(!prazna) {
				FileInputStream fin = new FileInputStream(kf.statistika);
	            ObjectInputStream oIn = new ObjectInputStream(fin);
	            try {
	                while (true) {
	                	s = (Statistika) oIn.readObject();
	                    lista.add(s);
	                }
	            } catch (Exception e) {
	            }
	            fin.close();
	            oIn.close();
			}
			
            //sad brisem fajlove
            File[] files=dir.listFiles();
    		if (files!=null) {
    			for (File f : files) {
    				deleteDir(f);
    				for (Statistika temp: lista)
    	            	if (temp.naziv.equals(f.getAbsolutePath())) {
    	            		lista.remove(temp); break;
    	            	}
    			}
    		}
            dir.delete();
            for (Statistika temp: lista)
            	if (temp.naziv.equals(dir.getAbsolutePath())) {
            		lista.remove(temp); break;
            	}
            if(!prazna)
            {
            	//sad treba snimiti listu statistike			
                FileOutputStream fajl=null;
        		ObjectOutputStream izlaz=null;
        		new FileOutputStream(kf.statistika).close();		//prvo brise sadrzaj cijelog fajla i odmah ga zatvara
    			fajl = new FileOutputStream(kf.statistika,true);
                izlaz = new ObjectOutputStream(fajl);
                for (Statistika temp: lista)
                	izlaz.writeObject(temp);
                izlaz.close();		
            }		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void izlistaj(JTextPane tekst, File[] files, File parent, int a)
	{
		String info;
		if (premijum)
			info="Pregled sadržaja glavnog foldera aplikacije";
		else
			info="Pregled sadržaja vašeg foldera";
		StyledDocument dokument = tekst.getStyledDocument(); 
		SimpleAttributeSet centrirano = new SimpleAttributeSet();
		StyleConstants.setAlignment(centrirano, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(centrirano, true); StyleConstants.setFontSize(centrirano, 20);
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);	
		StyleConstants.setBold(left, false);  StyleConstants.setFontSize(left, 15);
		
		if (parent.getAbsolutePath().equals(mojFolder.getAbsolutePath()))
		{
			try {
				dokument.setParagraphAttributes(dokument.getLength(), 1, centrirano, false);
				dokument.insertString(dokument.getLength(),info+"\n" , centrirano);
				StyleConstants.setBold(left, true); 
				dokument.setParagraphAttributes(dokument.getLength(), 1, left, false);
				dokument.insertString(dokument.getLength(),mojFolder.getAbsolutePath()+">\n" , left);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
		try {
			StyleConstants.setBold(left, false);
			dokument.setParagraphAttributes(dokument.getLength(), 1, left, false);
			if (files != null && files.length > 0) {
	            for (File file : files) {
	            	String ime="";
	            	for (int i=0;i<a;i++)
	            		ime=ime+"      ";
	                if (file.isDirectory()) {
	                	ime=ime+"- - - " + file.getName(); 
	                	fajlovi.put(dokument.getLength(), file.getAbsolutePath());
	                	StyleConstants.setBold(left, true);
	        			dokument.insertString(dokument.getLength(),ime + "\n", left);
	        			StyleConstants.setBold(left, false);
	                    izlistaj(tekst,file.listFiles(),file,a+1);
	                } else {
	                	ime=ime+"| - - " + file.getName(); 
	                	fajlovi.put(dokument.getLength(), file.getAbsolutePath());
	        			dokument.insertString(dokument.getLength(),ime + "\n", left);
	                }
	            }
	        }
			else if (parent.getAbsolutePath().equals(mojFolder.getAbsolutePath()))
				dokument.insertString(dokument.getLength(),"\tVaš folder je prazan", left);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public File kreirajFolder(JTextPane tekst)
	{
		String naziv;
		do
		{
			naziv=JOptionPane.showInputDialog(null, "Unesite naziv novog foldera:");			
			if (naziv==null)
				break;
		} while (naziv.length()==0);
		if (naziv!=null) {
			File newFolder=new File(mojFolder+"\\"+naziv);
			if (!newFolder.exists()) {
				newFolder.mkdirs();
				JOptionPane.showMessageDialog(null, "Novi folder je kreiran!"); 
				tekst.setText("");
				izlistaj(tekst, mojFolder.listFiles(),mojFolder,0);		
				tekst.revalidate(); tekst.repaint();	
				return newFolder;
			}
			else {
				JOptionPane.showMessageDialog(null, "Folder sa tim nazivom već postoji!"); 
				kf.addStatistics(newFolder, 1);
				return null;
			}
		}
		else
			return null;
	}
	
	public void kreirajTXT(JTextPane tekst)
	{
		FileSystemView fsv = new SingleRootFileSystemView(mojFolder);
		JFileChooser prozorIzbora = new JFileChooser(fsv);
		prozorIzbora.setApproveButtonText("izaberi");
		prozorIzbora.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		prozorIzbora.setDialogTitle("Izaberite folder u kojem želite da kreirate novi fajl!");
		//prozorIzbora.setSelectedFile(mojFolder);
		int izbor=prozorIzbora.showOpenDialog(null);
		if(izbor!=JFileChooser.APPROVE_OPTION)
			return;		
		File lokacija=prozorIzbora.getSelectedFile();
		if (lokacija.exists())
			kf.addStatistics(lokacija,1);
		papir=new JTextArea(20,80); papir.setBackground(Color.lightGray); JScrollPane skrol = new JScrollPane(papir); 
		JButton sacuvaj=new JButton("Sačuvaj fajl"); sacuvaj.setFont(new Font("Plain", Font.PLAIN, 20)); sacuvaj.setBackground(new Color(0,255,51));
		JFrame frame=new JFrame("Unesite tekst"); frame.add(skrol,BorderLayout.CENTER); frame.add(sacuvaj,BorderLayout.SOUTH); 
		frame.pack(); frame.setLocationRelativeTo(null);  frame.setVisible(true); frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		sacuvaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File novi=sacuvajFajl(frame,lokacija,tekst);
				if(novi!=null)
					kf.addStatistics(novi,0);
			}
		});
	}
	
	private File sacuvajFajl(JFrame frame,File lokacija, JTextPane pane)
	{
		imeFajla=JOptionPane.showInputDialog(null, "Unesite ime fajla:");
		if(imeFajla!=null) {
			int indeks=imeFajla.indexOf(".");
			if(indeks==-1)
				imeFajla=lokacija.getAbsolutePath()+"\\"+ imeFajla+".txt";
			else {
				imeFajla=imeFajla.substring(0,indeks); 
				imeFajla=lokacija.getAbsolutePath()+"\\"+imeFajla+".txt";
			}
		}	
		if(imeFajla==null)
			return null;
		
		File noviFajl=new File(imeFajla);
		if (noviFajl.exists()) {
			JOptionPane.showMessageDialog(null, "Fajl sa tim nazivom već postoji!"); return noviFajl;
		}
		String tekst=papir.getText();
		PrintWriter izlaz; // izlazni tok 
		try {
			izlaz = new PrintWriter (new FileWriter (imeFajla));
			izlaz.print(tekst);
			izlaz.close();
			Path path=Paths.get(noviFajl.getAbsolutePath());
			try {
			    FileTime creationTime = (FileTime) Files.getAttribute(path, "creationTime");
			    Date datum=new Date (creationTime.toMillis());
			    String pattern = "HH:mm:ss yyyy-MM-dd";
			    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				JOptionPane.showMessageDialog(null, "Fajl je uspješno snimljen!\n"
						+ "Vrijeme kreiranja fajla: " + simpleDateFormat.format(datum)+
				"\nVelicina fajla: "+noviFajl.length()+" bajtova"); 
			} catch (IOException ex) {
			    // handle exception
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} 	
		frame.dispose();
		pane.setText("");
		izlistaj(pane, mojFolder.listFiles(),mojFolder,0);		
		pane.revalidate(); pane.repaint();
		return noviFajl;
	}
	
	public void kopirajFajl(JTextPane pane)
	{
		JFileChooser prozorIzbora = new JFileChooser(mojFolder);
		prozorIzbora.setApproveButtonText("kopiraj");
		prozorIzbora.setDialogTitle("Izaberite fajl koji želite da kopirate!");
		int izbor=prozorIzbora.showOpenDialog(null);
		if(izbor!=JFileChooser.APPROVE_OPTION)
			return;		
		File fajl=prozorIzbora.getSelectedFile();
		if (!fajl.exists())
			return;
		if (fajl.isDirectory()) {
			JOptionPane.showMessageDialog(null, "Ne možete da kopirate folder!"); 
			return;
		}
		File novi=new File(mojFolder.getAbsolutePath()+"\\"+fajl.getName());
		if (novi.exists()) {
			int n = JOptionPane.showConfirmDialog(null, "Ako nastavite, obrisaćete fajl!", null,JOptionPane.YES_NO_OPTION);	
			if (n==JOptionPane.NO_OPTION || n==JOptionPane.CLOSED_OPTION)
				return;
		}

		BufferedInputStream original;
		BufferedOutputStream kopija;
		try {
			original=new BufferedInputStream(new FileInputStream(fajl));
			kopija=new BufferedOutputStream(new FileOutputStream(novi));
			int bajt;
			while ((bajt=original.read())!=-1)
				kopija.write(bajt);
			kopija.flush();
			JOptionPane.showMessageDialog(null, "Fajl je uspješno kopiran!"); 
			original.close(); kopija.close();
			pane.setText("");
			izlistaj(pane, mojFolder.listFiles(),mojFolder,0);		
			pane.revalidate(); pane.repaint();
			kf.addStatistics(novi,0);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Kopiranje fajla nije uspjelo!"); 
		}
	}

	public void kopirajGlavniFolder(JTextPane pane) {
		int n = JOptionPane.showConfirmDialog(null, "Da li želite da kopirate glavni folder aplikacije?", null,JOptionPane.YES_NO_OPTION);	
		if (n==JOptionPane.NO_OPTION || n==JOptionPane.CLOSED_OPTION) {
			kopirajFajl(pane); 			return;
		}
		
		JFileChooser prozorIzbora = new JFileChooser(mojFolder);
		prozorIzbora.setApproveButtonText("izaberi");
		prozorIzbora.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		prozorIzbora.setDialogTitle("Izaberite lokaciju na koju želite da kopirate cjelokupan folder aplikacije!");
		int izbor=prozorIzbora.showOpenDialog(null);
		if(izbor!=JFileChooser.APPROVE_OPTION)
			return;		
		File lokacija=prozorIzbora.getSelectedFile();
		if (lokacija.equals(mojFolder)) {
			JOptionPane.showMessageDialog(null, "Izabrali ste svoj folder kao lokaciju za kopiranje! Prekidam kopiranje!"); 
			kf.addStatistics(lokacija,1); return;
		}
		lokacija=new File(lokacija.getAbsolutePath()+"\\"+"DSM");
		lokacija.mkdirs();		
		if (kopirajSve(lokacija,mojFolder.listFiles()))
			JOptionPane.showMessageDialog(null, "Kopiranje foldera i svih fajlova je uspjelo!");		
	}
	
	private Boolean kopirajSve(File lokacija, File[] files)
	{
		for (File file : files) {
            if (file.isDirectory()) {
            	File folder=new File(lokacija.getAbsoluteFile()+"\\"+file.getName());
            	folder.mkdir();
            	kf.addStatistics(folder,0);
            	kopirajSve(folder,file.listFiles());
            } else {
            	File fajl=new File(lokacija.getAbsoluteFile()+"\\"+file.getName());
            	BufferedInputStream original;
        		BufferedOutputStream kopija;
        		try {
        			original=new BufferedInputStream(new FileInputStream(file));
        			kopija=new BufferedOutputStream(new FileOutputStream(fajl));
        			int bajt;
        			while ((bajt=original.read())!=-1)
        				kopija.write(bajt);
        			kopija.flush();
        			original.close(); kopija.close();
        			kf.addStatistics(fajl,0);
        		}
        		catch (IOException e) {
        			JOptionPane.showMessageDialog(null, "Kopiranje foldera i svih fajlova nije uspjelo!"); 
        			return false;
        		}        	
            }
        }
		return true;
	}
	
	public void pregledStatistike()
	{
		if (!kf.statistika.exists()) {
			JOptionPane.showMessageDialog(null, "Nema dostupne statistike!"); 
			return;
		}
		Statistika s;
		LinkedList<Statistika> lista=new LinkedList<Statistika>();
		
		try {
			FileInputStream fin = new FileInputStream(kf.statistika);
            ObjectInputStream oIn = new ObjectInputStream(fin);
            try {
                while (true) {
                	s = (Statistika) oIn.readObject();
                    lista.add(s);
                }
            } catch (Exception e) {
            }
            fin.close();
            oIn.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DefaultTableModel model=new DefaultTableModel();
		model.addColumn("Naziv fajla"); model.addColumn("Broj pristupa"); 
		Object podaci[] = new Object[2];
		for (int i=0;i<lista.size();i++) {
			podaci[0]=lista.get(i).naziv;
			podaci[1]=lista.get(i).brojPristupa;
			model.addRow(podaci);
		}
		
		JTable tabela=new JTable(model); tabela.setDefaultEditor(Object.class, null); 
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumn column = tabela.getColumnModel().getColumn(1);
        column.setMinWidth(80);
        column.setMaxWidth(80);
        column.setPreferredWidth(80);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        column.setCellRenderer( centerRenderer );
		JScrollPane skrol=new JScrollPane(tabela); tabela.setBackground(new Color(0,255,51));
		JFrame okvir=new JFrame("PREGLED STATISTIKE");
		okvir.setSize(800,400); okvir.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		okvir.setLocationRelativeTo(null); 
		okvir.add(skrol,BorderLayout.CENTER); okvir.setVisible(true);
	}
}
