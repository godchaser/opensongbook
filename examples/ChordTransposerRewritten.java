package org.duckdns.valci.opensongbook.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChordTransposerRewritten {
    static String[] majorArray = { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" };
    static String[] flatArray = { "A", "Ab", "B", "C", "Cb", "D", "Db", "E", "F", "Fb", "G", "Gb" };

    public static void transpose(String chord, int ammount) {
        // first check if is dual chord
        if (chord.contains("/")) {
            int chordSeparatorIdx = chord.indexOf("/");
            String chord1 = chord.substring(0, chordSeparatorIdx);
            System.out.println("chord1 " + chord1);
            String chord2 = chord.substring(chordSeparatorIdx + 1);
            System.out.println("chord2 " + chord2);
        } else {
            // by default we are using major scale
            String[] scaleArray = ((chord.contains("b")) ? flatArray : majorArray);
            ArrayList<String> scale = new ArrayList<String>(Arrays.asList(scaleArray));
            System.out.println("Used scale " + scale);
            String transposedKey = null;
            for (String scaleKey : scale) {
                if (chord.equalsIgnoreCase(scaleKey)) {
                    int idx = scale.indexOf(scaleKey);
                    transposedKey = scale.get(idx + ammount);
                    break;
                }
            }
            System.out.println("transposed key is " + transposedKey);
        }

    }

    public static void transposeChord(String chord, int ammount) {

        String[] scaleArray = { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" };
        ArrayList<String> scale = new ArrayList<String>(Arrays.asList(scaleArray));
        Pattern pattern = Pattern.compile("[^b#][#b]?");
        Matcher matcher = pattern.matcher(chord);
        System.out.println("here we are 1 ");
        ArrayList<String> chordComposition = new ArrayList<String>();
        while (matcher.find()) {
            // System.out.println("here we are 2 ");
            // Do something with the matched text
            // System.out.println(matcher.group(0));
            chordComposition.add(matcher.group(0));
        }
        int indexTransposed = (scale.indexOf(chordComposition.get(0) + ammount) % scale.size());
        String newKey = scale.get(indexTransposed);
        System.out.println(newKey);
        // var newKey = keys[(keys.indexOf(key) + transpose) % keys.length];
        // var newBass = keys[(keys.indexOf(bass) + transpose) % keys.length];

        /*
         * var keys = ["A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"];
         * 
         * Parse your chord with a regular expression:
         * 
         * var matches = /([A-G]#?)([^\/]*)(?:\/([A-G]#?))?/.exec(chord); var key = matches[1]; var descriptor =
         * matches[2]; var bass = matches[3];
         * 
         * Do a little math to get the new key:
         * 
         * var newKey = keys[(keys.indexOf(key) + transpose) % keys.length]; var newBass = keys[(keys.indexOf(bass) +
         * transpose) % keys.length];
         * 
         * Put it all back together again:
         * 
         * var newChord = newKey + descriptor; if (newBass) { newChord += "/" + newBass; } return newChord;
         */
    }

    /*
     * String [] scaleArray = {"C","Cb","C#","D","Db","D#","E","Eb","E#","F","Fb","F#","G","Gb","G#",
     * "A","Ab","A#","B","Bb","B#"}; ArrayList <String> scale = new ArrayList<String>(Arrays.asList(scaleArray)); String
     * [] transpArray = {"Cb","C","C#","Bb","Cb","C","C","C#","D","Db","D","D#","C","Db","D",
     * "D","D#","E","Eb","E","F","D","Eb","E", "E","E#","F#", "E","F","F#",
     * "Eb","Fb","F","F","F#","G","Gb","G","G#","F","Gb","G", "G","G#","A",
     * "Ab","A","A#","G","Ab","A","A","A#","B","Bb","B","C","A","Bb","B", "B","B#","C#"}; ArrayList <String> transp =
     * new ArrayList<String>(Arrays.asList(transpArray));
     * 
     * System.out.println(scale); System.out.println(transp);
     * 
     * Pattern pattern = Pattern.compile("[^b#][#b]?"); Matcher matcher = pattern.matcher(chord);
     * System.out.println(chord); ArrayList <String> chordParticles = new ArrayList<String>(); while (matcher.find() ) {
     * System.out.println("here we are"); // Do something with the matched text chordParticles.add(matcher.group(0)); }
     * 
     * System.out.println(chordParticles);
     * 
     * for (String chordParticle : chordParticles){ if(scale.indexOf(chordParticle)!=-1){ if(ammount>0){ for (int
     * i=0;i<ammount;i++){ int pos = scale.indexOf(chordParticles); //String transpos = 3*pos-2+3;
     * 
     * } } } } }
     */
    /*
     * function transposechord(chord, amount){ var scale =
     * ["C","Cb","C#","D","Db","D#","E","Eb","E#","F","Fb","F#","G","Gb","G#", "A","Ab","A#","B","Bb","B#"]; var transp
     * = ["Cb","C","C#","Bb","Cb","C","C","C#","D","Db","D","D#","C","Db","D", "D","D#","E","Eb","E","F","D","Eb","E",
     * "E","E#","F#", "E","F","F#", "Eb","Fb","F","F","F#","G","Gb","G","G#","F","Gb","G", "G","G#","A",
     * "Ab","A","A#","G","Ab","A","A","A#","B","Bb","B","C","A","Bb","B", "B","B#","C#"]; //dobijem D , / , E# var subst
     * = chord.match(/[^b#][#b]?/g); // za svaki element u polju for(var ax in subst){ // ako se nalazi u skali
     * if(scale.indexOf(subst[ax])!==-1){ if(amount>0){ for(ix=0;ix<amount;ix++){ var pos = scale.indexOf(subst[ax]);
     * var transpos = 3*pos-2+3; subst[ax] = transp[transpos+1]; } } if(amount<0){ for(ix=0;ix>amount;ix--){ var pos =
     * scale.indexOf(subst[ax]); var transpos = 3*pos-2+3; subst[ax] = transp[transpos-1]; } } } } chord=subst.join("");
     * }
     */

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // ChordTransposerRewritten test = new ChordTransposerRewritten();
        ChordTransposerRewritten.transpose("D/G#", 3);
        ChordTransposerRewritten.transpose("D#", 3);
    }

}
