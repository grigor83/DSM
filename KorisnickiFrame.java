import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class KorisnickiFrame extends JFrame {
	JFrame parent; Korisnik k;
	JTextPane tekst; JScrollPane skrol;
	JPanel p; JButton newFolder, newTXT, kopiraj, odjavi;
	String info;
	
	public KorisnickiFrame(JFrame parent, File mojFolder,Korisnik k)
	{		
		this.parent=parent; this.k=k; this.k.mojFolder=mojFolder;  k.fajlovi=new HashMap<>();
		if (k.premijum)
			info="Pregled sadržaja glavnog foldera aplikacije";
		else
			info="Pregled sadržaja vašeg foldera";
		tekst=new JTextPane(); tekst.setBackground(new Color(0,255,51)); tekst.setEditable(false); 
		tekst.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume(); 
                    try {
                        int offset= tekst.getSelectionStart();
						int rowStart = Utilities.getRowStart(tekst, offset);
	                    int rowEnd = Utilities.getRowEnd(tekst, offset);
	                    tekst.setSelectionStart(rowStart); tekst.setSelectionEnd(rowEnd);
	                    String red=tekst.getText(rowStart, rowEnd-rowStart);
	                    red= red.replace("|", "").replace("-", ""); red=red.strip();
	                    if(red.equals(mojFolder.getAbsolutePath()+">") || red.equals("Vaš folder je prazan") || red.equals(info))
	                    	return;
	                    int x = JOptionPane.showConfirmDialog(null, "Da li želite obrisati fajl/folder?","Brisanje fajla/foldera",2);
	                    if (x==0) {
	                    	String pravoIme=k.fajlovi.get(rowStart);
		                    k.obrisi(tekst,k.mojFolder.listFiles(),pravoIme);
	                    }
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}		
		});
			
		k.izlistaj(tekst,mojFolder.listFiles(),mojFolder,0);		
		skrol=new JScrollPane(tekst);
		p=new JPanel();  p.setLayout(new GridLayout(0,4)); p.add(newFolder=new JButton("New folder")); p.add(newTXT=new JButton("New .txt file"));
		p.add(kopiraj=new JButton("Kopiraj fajl")); p.add(odjavi=new JButton("Odjavi se"));
		if (k.premijum)
			kopiraj.setText("Kopiraj");
		newFolder.setBackground(new Color(255,255,0)); newTXT.setBackground(new Color(255,255,0));
		kopiraj.setBackground(new Color(255,255,0)); odjavi.setBackground(new Color(255,255,0));
		newFolder.setFont(new Font("Plain", Font.PLAIN, 20)); newTXT.setFont(new Font("Plain", Font.PLAIN, 20));
		kopiraj.setFont(new Font("Plain", Font.PLAIN, 20)); odjavi.setFont(new Font("Plain", Font.PLAIN, 20));
		newFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				k.kreirajFolder(tekst);
			}
		});
		newTXT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				k.kreirajTXT(tekst);
			}
		});
		kopiraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!k.premijum)
					k.kopirajFajl(tekst);
				else
					k.kopirajGlavniFolder(tekst);
			}
		});
		odjavi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onExit();
			}
		});
		
		setTitle(k.ime); setSize(800,400); add(skrol,BorderLayout.CENTER); add(p,BorderLayout.SOUTH); setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); parent.setVisible(false); setVisible(true);
	}	
	
	private void onExit() {
		this.dispose();
		parent.setVisible(true);
	}
}
