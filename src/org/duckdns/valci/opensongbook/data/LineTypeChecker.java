/*
 * This file is part of Quelea, free projection software for churches.
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.duckdns.valci.opensongbook.data;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks the type of the line.
 * 
 * @author Michael
 */
public class LineTypeChecker {

    static final Logger LOG = LoggerFactory.getLogger(LineTypeChecker.class);

    /**
     * Check whether this line is a line containing only chords.
     * 
     * @return true if it's a chord line, false otherwise.
     */
    public static boolean isChordLine(String line) {
        // to check if string has only whitespaces
        if (StringUtils.isBlank(line)) {
            return false;
        }
        // this is case where there is only . in line
        if (line.contains(".") && line.length() < 2) {
            return false;
        }
        if (line.toLowerCase().endsWith("//chords")) {
            return true;
        }
        if (line.toLowerCase().endsWith("//lyrics")) {
            return false;
        }
        String checkLine = line.replace('-', ' ');
        checkLine = checkLine.replace('–', ' ');
        checkLine = checkLine.replace('.', ' ');
        checkLine = checkLine.replace(',', ' ');
        checkLine = checkLine.replace('/', ' ');
        checkLine = checkLine.replace('(', ' ');
        checkLine = checkLine.replace(')', ' ');
        checkLine = checkLine.replaceAll("[xX][0-9]+", "");
        checkLine = checkLine.replaceAll("[0-9]+[xX]", "");
        for (String s : checkLine.split("\\s")) {
            if (s.trim().isEmpty()) {
                continue;
            }
            if (!s.matches("([a-hA-H](#|b)?[0-9]*((sus|dim|maj|dom|min|m|aug|add)?[0-9]*){3}(#|b)?[0-9]*)(/([a-hA-H](#|b)?[0-9]*((sus|dim|maj|dom|min|m|aug|add)?[0-9]*){3}(#|b)?[0-9]*))?")) {
                System.out.println("This prevents this string to be chord: " + s);
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        String[] testChords = { "C         D          G   -/F#   Em", "E H/D# C#m   E/H   A     E/H H E  H",
                ".Am          D         G  Dm – G",
                ".(E,A,E,B7,E)", 
                ".     Bm7    G#dimg  G    A   D",
                ".F   Em7        Asus  C/D   Dm7 C/E  F    Gm7    F/A  " };
        for (String chordLine:testChords){
            LOG.trace("now evaluaing this line: " + chordLine);
            boolean isValidChordLine = LineTypeChecker.isChordLine(chordLine);
            if (!isValidChordLine){
                LOG.error("Not valid chord line: " + chordLine);
            }
        }
    }
}
