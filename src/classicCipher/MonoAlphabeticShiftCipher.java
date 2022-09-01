package classicCipher;

import java.util.HashMap;
import java.util.Map;

/** An example of a Ciphertext encrypted using Mono-alphabetic shift cipher
 * JGRMQOYGHMVBJWRWQFPWHGFFDQGFPFZRKBEEBJIZQQOCIBZKLFAFGQVFZFWWE
 * OGWOPFGFHWOLPHLRLOLFDMFGQWBLWBWQOLKFWBYLBLYLFSFLJGRMQBOLWJVFP
 * FWQVHQWFFPQOQVFPQOCFPOGFWFJIGFQVHLHLROQVFGWJVFPFOLFHGQVQVFILE
 * OGQILHQFQGIQVVOSFAFGBWQVHQWIJVWJVFPFWHGFIWIHZZRQGBABHZQOCGFHX
 *
 *
 * round double value to 2 decimal places: Math.round(value * 100.0) / 100.0
 * truncate double value to 2 decimal places: Math.floor(value * 100) / 100;
 *
 * */

public class MonoAlphabeticShiftCipher {

    public static Map<Character, Double> generateLetterFrequencyMap(String ciphertext){
        Map<Character, Integer> letterFrequencyMap = new HashMap<>();
        int n = ciphertext.length();

        for (char letter: ciphertext.toCharArray()){
            letterFrequencyMap.put(letter, letterFrequencyMap.getOrDefault(letter, 0) + 1);
        }

        Map<Character, Double> letterAvgFrequencyMap = new HashMap<>();
        for (char ch = 'A'; ch <= 'Z'; ch++){
            if (letterFrequencyMap.containsKey(ch)){
                double avg = (double) (letterFrequencyMap.get(ch) * 100) / n;
                letterAvgFrequencyMap.put(ch, Math.floor(avg * 10) / 10); // truncate avg to 1 decimal place
            }
        }

        return letterAvgFrequencyMap;
    }

    public static Map<Character, Double> getKnownLetterAvgFrequencyMap(){
        Map<Character, Double> letterAvgFrequencyMap = new HashMap<>();
        letterAvgFrequencyMap.put('a', 8.2);
        letterAvgFrequencyMap.put('b', 1.5);
        letterAvgFrequencyMap.put('c', 2.8);
        letterAvgFrequencyMap.put('d', 4.3);
        letterAvgFrequencyMap.put('e', 12.7);
        letterAvgFrequencyMap.put('f', 2.2);
        letterAvgFrequencyMap.put('g', 2.0);
        letterAvgFrequencyMap.put('h', 6.1);
        letterAvgFrequencyMap.put('i', 7.0);
        letterAvgFrequencyMap.put('j', 0.2);
        letterAvgFrequencyMap.put('k', 0.8);
        letterAvgFrequencyMap.put('l', 4.0);
        letterAvgFrequencyMap.put('m', 2.4);
        letterAvgFrequencyMap.put('n', 6.7);
        letterAvgFrequencyMap.put('o', 7.5);
        letterAvgFrequencyMap.put('p', 1.9);
        letterAvgFrequencyMap.put('q', 0.1);
        letterAvgFrequencyMap.put('r', 6.0);
        letterAvgFrequencyMap.put('s', 6.3);
        letterAvgFrequencyMap.put('t', 9.1);
        letterAvgFrequencyMap.put('u', 2.8);
        letterAvgFrequencyMap.put('v', 1.0);
        letterAvgFrequencyMap.put('w', 2.4);
        letterAvgFrequencyMap.put('x', 0.2);
        letterAvgFrequencyMap.put('y', 2.0);
        letterAvgFrequencyMap.put('z', 0.1);

        return letterAvgFrequencyMap;
    }

    public static void main(String[] args) {
        String cipherText = "JGRMQOYGHMVBJWRWQFPWHGFFDQGFPFZRKBEEBJIZQQOCIBZKLFAFGQVFZFWWE"
                            + "OGWOPFGFHWOLPHLRLOLFDMFGQWBLWBWQOLKFWBYLBLYLFSFLJGRMQBOLWJVFP"
                            + "FWQVHQWFFPQOQVFPQOCFPOGFWFJIGFQVHLHLROQVFGWJVFPFOLFHGQVQVFILE"
                            + "OGQILHQFQGIQVVOSFAFGBWQVHQWIJVWJVFPFWHGFIWIHZZRQGBABHZQOCGFHX";

        Map<Character, Double> letterAvgFrequencyMap = generateLetterFrequencyMap(cipherText);
        for (char ch = 'A'; ch <= 'Z'; ch++){
            if (letterAvgFrequencyMap.containsKey(ch)){
                System.out.println(ch + ": " + letterAvgFrequencyMap.get(ch));
            }
        }
    }
}
