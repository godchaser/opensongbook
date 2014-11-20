package org.duckdns.valci.opensongbook.data;

public class ChordTransposerOld {

    // C, C#, D, D#, E, F, F#, G, G#, A, A# B

    public static void transpose(String chordLine) {
        String[] chords = chordLine.split("\\s+");
        for (String chord : chords) {
            System.out.println(chord);
            switch (chord.toLowerCase().substring(0, 1)) {
            case "c":
                System.out.println("transposing");
                break;
            case "d":
                System.out.println("transposing");
                break;
            case "e":
                System.out.println("transposing");
                break;
            case "f":
                System.out.println("transposing");
                break;
            case "g":
                System.out.println("transposing");
                break;
            case "a":
                System.out.println("transposing");
                break;
            case "h":
                System.out.println("transposing");
                break;
            case "b":
                System.out.println("transposing");
                break;
            }
        }
    }

    public static String transposeChord(String chord, int ammount) {
        String[] scale = { "C", "Cb", "C#", "D", "Db", "D#", "E", "Eb", "E#", "F", "Fb", "F#", "G", "Gb", "G#", "A",
                "Ab", "A#", "B", "Bb", "B#" };
        String[] trans = { "Cb", "C", "C#", "Bb", "Cb", "C", "C", "C#", "D", "Db", "D", "D#", "C", "Db", "D", "D",
                "D#", "E", "Eb", "E", "F", "D", "Eb", "E", "E", "E#", "F#", "E", "F", "F#", "Eb", "Fb", "F", "F", "F#",
                "G", "Gb", "G", "G#", "F", "Gb", "G", "G", "G#", "A", "Ab", "A", "A#", "G", "Ab", "A", "A", "A#", "B",
                "Bb", "B", "C", "A", "Bb", "B", "B", "B#", "C#" };
        String[] subst = chord.split("/[^b#][#b]?/g");
        for (String ax : subst) {
            // if scale contains subst[ax]
        }

        return chord;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String chordLine = "Am E7  Am E7   Am      Em      Am  G7";
        ChordTransposerOld.transpose(chordLine);
    }

}
