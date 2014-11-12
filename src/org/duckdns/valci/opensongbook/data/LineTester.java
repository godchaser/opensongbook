package org.duckdns.valci.opensongbook.data;

public class LineTester {

    /**
     * Check whether this line is a line containing only chords.
     * 
     * @return true if it's a chord line, false otherwise.
     */
    public static boolean checkChords(String line) {
        if (line.isEmpty()) {
            return false;
        }
        if (line.toLowerCase().endsWith("//chords")) {
            return true;
        }
        if (line.toLowerCase().endsWith("//lyrics")) {
            return false;
        }
        String checkLine = line.replace('-', ' ');
        checkLine = checkLine.replace('(', ' ');
        checkLine = checkLine.replace(')', ' ');
        checkLine = checkLine.replaceAll("[xX][0-9]+", "");
        checkLine = checkLine.replaceAll("[0-9]+[xX]", "");
        for (String s : checkLine.split("\\s")) {
            if (s.trim().isEmpty()) {
                continue;
            }
            if (!s.matches("([a-gA-G](#|b)?[0-9]*((sus|dim|maj|dom|min|m|aug|add)?[0-9]*){3}(#|b)?[0-9]*)(/([a-gA-G](#|b)?[0-9]*((sus|dim|maj|dom|min|m|aug|add)?[0-9]*){3}(#|b)?[0-9]*))?")) {
                System.out.println("this is problem:" + s);
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // boolean chord = LineTester.checkChords(" A B C");
        // System.out.print(chord);
        // C D G -/F# Em
        boolean chord2 = LineTester.checkChords("C         D          G   -/F#   Em");
        System.out.print(chord2);
    }

}
