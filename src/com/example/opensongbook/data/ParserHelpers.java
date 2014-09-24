package com.example.opensongbook.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ParserHelpers {
    public static ArrayList <String> readFile (String path) throws IOException {
	    File file = new File(path);
	    ArrayList <String>fileContents = new ArrayList<String>();
	    Scanner scanner = new Scanner(file, "UTF-8");
	    try {
	        while(scanner.hasNextLine()) {     
	        	fileContents.add(scanner.nextLine());
	        }
	        return fileContents;
	    } finally {
	        scanner.close();
	    }
	}
}
