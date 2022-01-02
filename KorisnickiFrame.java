import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class KorisnickiFrame extends JFrame {
	JFrame parent; File mojFolder; Korisnik k;
	JTextPane tekst; JScrollPane skrol;
	JPanel p; JButton newFolder, newTXT, kopiraj, odjavi;
	
	public KorisnickiFrame(JFrame parent, File mojFolder,Korisnik k)
	{		
		this.parent=parent; this.mojFolder=mojFolder; this.k=k; 
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
	                    if(red.equals(mojFolder.getName()+">") || red.equals("Vaš folder je prazan") || red.equals("Pregled sadržaja vašeg foldera"))
	                    	return;
	                    
	                    int x = JOptionPane.showConfirmDialog(null, "Da li želite obrisati fajl/folder?","Brisanje fajla/foldera",2);
	                    if (x==0) {
		                    obrisi(mojFolder.listFiles(),red);
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
			
		izlistaj(mojFolder.listFiles(),mojFolder,0);		
		skrol=new JScrollPane(tekst);
		p=new JPanel();  p.setLayout(new GridLayout(0,4)); p.add(newFolder=new JButton("New folder")); p.add(newTXT=new JButton("New .txt file"));
		p.add(kopiraj=new JButton("Kopiraj fajl")); p.add(odjavi=new JButton("Odjavi se"));
		newFolder.setBackground(new Color(255,255,0)); newTXT.setBackground(new Color(255,255,0));
		kopiraj.setBackground(new Color(255,255,0)); odjavi.setBackground(new Color(255,255,0));
		newFolder.setFont(new Font("Plain", Font.PLAIN, 20)); newTXT.setFont(new Font("Plain", Font.PLAIN, 20));
		kopiraj.setFont(new Font("Plain", Font.PLAIN, 20)); odjavi.setFont(new Font("Plain", Font.PLAIN, 20));
		setTitle(k.ime); setSize(800,400); add(skrol,BorderLayout.CENTER); add(p,BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent evt) {
			     onExit();
			   }
			  });
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); setLocationRelativeTo(null); 
		parent.setVisible(false); setVisible(true);
	}
	
	private void izlistaj(File[] files, File parent, int a)
	{
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
				dokument.insertString(dokument.getLength(),"Pregled sadržaja vašeg foldera\n" , centrirano);
				StyleConstants.setBold(left, true); 
				dokument.setParagraphAttributes(dokument.getLength(), 1, left, false);
				dokument.insertString(dokument.getLength(),mojFolder.getName()+">\n" , left);
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
	                	ime=ime+"| - - " + file.getName(); 
	        			dokument.insertString(dokument.getLength(),ime + "\n", left);
	                    izlistaj(file.listFiles(),file,a+1);
	                } else {
	                	ime=ime+"| - - " + file.getName(); 
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
	
	private void obrisi(File[] files, String naziv)
	{
		if (files != null) {
            for (File file : files) {
            	if (file.getName().equals(naziv)) {
            		if (!file.isDirectory() && !file.getName().contains(".txt"))
                    {
                    	JOptionPane.showMessageDialog(null, "Ne možete obrisati ovaj tip fajla!"); return;
                    }
            		deleteDir(file);
            		tekst.setText("");
            		izlistaj(mojFolder.listFiles(),mojFolder,0);		
            		tekst.revalidate();
                    tekst.repaint();
            		break;
            	}
            	if (file.isDirectory()) {
            		obrisi(file.listFiles(),naziv);
            	}
            }
		}
	}
	
	private void deleteDir(File dir)
	{
		File[] files=dir.listFiles();
		if (files!=null) {
			for (File f : files) 
				deleteDir(f);
		}
        dir.delete();
	}
	
	private void onExit() {
		this.dispose();
		parent.setVisible(true);
	}
}
