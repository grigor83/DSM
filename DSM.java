import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DSM {
	static String path;
	static int n;
	
	public static void main(String[] args) {
		Frame okvir=new Frame();
		createDSM();
		okvir.path=path;
		okvir.n=n;
	}
	
	public static void createDSM()
	{
		Scanner ulaz = null; // ulazni tok 
		try {
			ulaz = new Scanner(new FileReader ("configFile.txt"));			
			path=ulaz.nextLine();
			n=ulaz.nextInt();
			File DSMfolder=new File(path);
			if (!DSMfolder.exists())
				DSMfolder.mkdir();
		}
		catch (FileNotFoundException e) {
			System.out.println("takva datoteka ne postoji");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ulaz.close();
	}

}
