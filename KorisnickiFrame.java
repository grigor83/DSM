import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class KorisnickiFrame extends JFrame {
	JFrame parent; File mojFolder; Korisnik k;
	JTextPane tekst; JScrollPane skrol;
	
	public KorisnickiFrame(JFrame parent, File mojFolder,Korisnik k)
	{		
		this.parent=parent; this.mojFolder=mojFolder; this.k=k;
		parent.dispose();
		tekst=new JTextPane(); tekst.setBackground(new Color(0,255,51)); tekst.setEditable(false); 
		tekst.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    System.out.println(tekst.getSelectedText());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub			
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub			
			}		
		});
		
		
		
		izlistaj(mojFolder.listFiles(),true,0);		
		skrol=new JScrollPane(tekst);
		setTitle(k.ime); setSize(800,400); add(skrol,BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null); setVisible(true);
	}
	
	private void izlistaj(File[] files, Boolean parent, int a)
	{
		StyledDocument dokument = tekst.getStyledDocument();
		SimpleAttributeSet centrirano = new SimpleAttributeSet();
		StyleConstants.setAlignment(centrirano, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(centrirano, true); StyleConstants.setFontSize(centrirano, 20);
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);	
		StyleConstants.setBold(left, false);  StyleConstants.setFontSize(left, 15);
		
		if (parent)
		{
			try {
				dokument.setParagraphAttributes(dokument.getLength(), 1, centrirano, false);
				dokument.insertString(dokument.getLength(),"Pregled sadržaja vašeg foldera\n" , centrirano);
				StyleConstants.setBold(left, true); 
				dokument.setParagraphAttributes(dokument.getLength(), 1, left, false);
				dokument.insertString(dokument.getLength(),mojFolder.getName()+"\n" , left);
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
	        			dokument.insertString(dokument.getLength(),ime + " (size in bytes: " + file.length()+")\n", left);
	                    izlistaj(file.listFiles(),false,a+1);
	                } else {
	                	ime=ime+"| - - " + file.getName(); 
	        			dokument.insertString(dokument.getLength(),ime + " (size in bytes: " + file.length()+")\n", left);
	                }
	            }
	        }
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
