package projekat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DSM {
	static String path;
	public static void main(String[] args) {
		Frame okvir=new Frame();
		createDSM();
		okvir.path=path;
	}
	
	public static void createDSM()
	{
		int n = 0;
		Scanner ulaz = null; // ulazni tok 
		try {
			ulaz = new Scanner(new FileReader ("configFile.txt"));			
			path=ulaz.nextLine();
			File DSMfolder=new File(path);
			if (!DSMfolder.exists())
				DSMfolder.mkdir();
			n=ulaz.nextInt();
			File nFajl=new File(path+"\\n.txt");
			nFajl.createNewFile();
		}
		catch (FileNotFoundException e) {
			System.out.println("takva datoteka ne postoji");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ulaz.close();
		
		PrintWriter izlaz; // izlazni tok 
		try {
			izlaz = new PrintWriter (new FileWriter (path+"\\n.txt"));
			izlaz.println(n);
			izlaz.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 		
	}

}
