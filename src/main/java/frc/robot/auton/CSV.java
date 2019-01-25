package frc.robot.auton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSV {
	
	public static Object[][] csv2table(File f) {
		ArrayList<ArrayList<String>> tab = new ArrayList<>();
		try {
			FileReader fRead = new FileReader(f);
			BufferedReader fReadBuff = new BufferedReader(fRead);
			String fLine = fReadBuff.readLine();
			while (fLine != null) {
				ArrayList<String> lineArrLst = new ArrayList<>(Arrays.asList(fLine.split(",")));
				tab.add(lineArrLst);
				fLine = fReadBuff.readLine();
			}			
			fRead.close();
			fReadBuff.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found while looking for CSV file. Check the file name.");
			System.out.println(e + e.getMessage());
		}
		catch (IOException e) {
            System.out.println("IOException");
			System.out.println(e + e.getMessage());
		}
		String[][] tabArr = new String[tab.size()][];
		for (int i=0; i<tab.size(); i++) {
			tabArr[i] = new String[tab.get(i).size()];
			for (int j=0; j<tab.get(i).size(); j++) {
				tabArr[i][j] = tab.get(i).get(j);
			}
		}
		return tabArr;	
	}
	
	public static void table2csv(Object[][] tab, File f) {
		try {
			FileWriter fWrite = new FileWriter(f);
			BufferedWriter fWriteBuff = new BufferedWriter(fWrite);
			for (int i=0; i<tab.length; i++) {
				for (int j=0; j<tab[i].length; j++) {
					fWriteBuff.write((String)tab[i][j]);
					if (j!=tab[i].length-1) {
						fWriteBuff.write(",");
					}
				}
				if (i!=tab.length-1) {
					fWriteBuff.newLine();
				}
			}
			fWriteBuff.close();
			fWrite.close();
		} catch (IOException e) {
			System.out.println("IOException. (I probably couldn't write to the file specified.)");
			System.out.println(e + e.getMessage());
		} catch (NullPointerException e) {
            System.out.println("NullPointerException. Tell Ben if you get this.");
			System.out.println(e + e.getMessage());
		}
    }

    public static void writeLine2csv(Object[] line, File f) {
        try {
			FileWriter fWrite = new FileWriter(f, true);
			BufferedWriter fWriteBuff = new BufferedWriter(fWrite);
			
				for (int j=0; j<line.length; j++) {
					fWriteBuff.write((String)line[j]);
					if (j!=line.length-1) {
						fWriteBuff.write(",");
					}
				}
				
			
			fWriteBuff.close();
			fWrite.close();
		} catch (IOException e) {
			System.out.println("IOException. (I probably couldn't write to the file specified.)");
			System.out.println(e + e.getMessage());
		} catch (NullPointerException e) {
            System.out.println("NullPointerException. Tell Ben if you get this.");
			System.out.println(e + e.getMessage());
		}
    }

	
}