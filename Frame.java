package DSM;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class Frame extends JFrame{
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
				unesiPodatke();
			}
		});
	}
	
	private void posaljiZahtjev()
	{
		unesiPodatke();
		JOptionPane.showMessageDialog(this, "Vaš zahtjev je poslat administratoru!"); 
	}
	
	private void unesiPodatke()
	{
		String korisnickoIme = JOptionPane.showInputDialog(this, "Unesite korisničko ime:");
        if(korisnickoIme.isEmpty())
        	System.out.println("Accept a client!");
		String lozinka = JOptionPane.showInputDialog(this, "Unesite lozinku:");
	}
	
	public static void main(String[] args) {
		Frame okvir=new Frame();
	}
}
