package classicCipher;

import java.util.HashMap;
import java.util.Map;

/** An example of a Ciphertext encrypted using Mono-alphabetic shift cipher
 * KWSVVSYXKSBOKRKBNRKDKXNKNBEXUKBOKDKLKBGROXDROIQODDROSBLOOBC
 * DROIXYDSMOKPVISXOKMRWEQDROWSVVSYXKSBOZYVSDOVIKCUCDROLKBDOXN
 * OBPYBKXYDROBLOOBDROXZBYMOONCDYCSZSDDRORKBNRKDCZSVVCYEDTECDO
 * XYEQRDYQODBSNYPDROPVIKXNAEKPPCDROBOCDSDCXYGDRONBEXUCDEBXROC
 * DSMUCRSCRKXNSXDYDROLOOBQBKLCDROPVILIDROGSXQCKXNCRYEDCCZSDSD
 * YEDCZSDSDYEDBOKNOBCNSQOCDPOLBEKBIDGYDRYECKXNDOX
 *
 *
 * round double value to 2 decimal places: Math.round(value * 100.0) / 100.0
 * truncate double value to 2 decimal places: Math.floor(value * 100) / 100;
 *
 * */

public class AttackOnShiftCipher {

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
                letterAvgFrequencyMap.put(ch, Math.floor((avg/100) * 1000) / 1000); // truncate avg to 3 decimal places
            }
        }

        return letterAvgFrequencyMap;
    }

    // let Pi, denote the frequency of the ith letter in the normal English text and 0 <= Pi <= 1,
    // For example, for i = 0, the frequency of letter 'a', P0 = 0.082 according to the Figure 1.3 in the text book.
    public static Map<Character, Double> getKnownLetterAvgFrequencyMap(){
        Map<Character, Double> letterAvgFrequencyMap = new HashMap<>();
        letterAvgFrequencyMap.put('a', 0.082);
        letterAvgFrequencyMap.put('b', 0.015);
        letterAvgFrequencyMap.put('c', 0.028);
        letterAvgFrequencyMap.put('d', 0.043);
        letterAvgFrequencyMap.put('e', 0.127);
        letterAvgFrequencyMap.put('f', 0.022);
        letterAvgFrequencyMap.put('g', 0.020);
        letterAvgFrequencyMap.put('h', 0.061);
        letterAvgFrequencyMap.put('i', 0.070);
        letterAvgFrequencyMap.put('j', 0.002);
        letterAvgFrequencyMap.put('k', 0.008);
        letterAvgFrequencyMap.put('l', 0.040);
        letterAvgFrequencyMap.put('m', 0.024);
        letterAvgFrequencyMap.put('n', 0.067);
        letterAvgFrequencyMap.put('o', 0.075);
        letterAvgFrequencyMap.put('p', 0.019);
        letterAvgFrequencyMap.put('q', 0.001);
        letterAvgFrequencyMap.put('r', 0.060);
        letterAvgFrequencyMap.put('s', 0.063);
        letterAvgFrequencyMap.put('t', 0.091);
        letterAvgFrequencyMap.put('u', 0.028);
        letterAvgFrequencyMap.put('v', 0.010);
        letterAvgFrequencyMap.put('w', 0.024);
        letterAvgFrequencyMap.put('x', 0.002);
        letterAvgFrequencyMap.put('y', 0.020);
        letterAvgFrequencyMap.put('z', 0.001);

        return letterAvgFrequencyMap;
    }

    // Calculate the sum of the frequency squared using Figure 1.3
    // Pi^2 == 0.065 where i is the position of each letter in the English alphabet and 0 <= i <= 25
    public static double getSumOfFrequencySquared(){
        Map<Character, Double> letterAvgFrequencyMap = getKnownLetterAvgFrequencyMap();
        double sum  = 0.0;
        for (Map.Entry<Character, Double> entry: letterAvgFrequencyMap.entrySet()){
            sum += Math.pow(entry.getValue(), 2);
        }
        return sum; // Math.floor(sum * 100) / 1000
    }

    // associate the letters of the English alphabet a,...,z with 0,...,25.
    public static Map<Character, Integer> getLetterPositionMap(){
        Map<Character, Integer> letterPositionMap = new HashMap<>();
        int position = 0;
        for (char ch = 'a'; ch <= 'z'; ch++){
            letterPositionMap.put(ch, position);
            position++;
        }
        return letterPositionMap;
    }

    public static void main(String[] args) {
        String cipherText = "KWSVVSYXKSBOKRKBNRKDKXNKNBEXUKBOKDKLKBGROXDROIQODDROSBLOOBC"
                            + "DROIXYDSMOKPVISXOKMRWEQDROWSVVSYXKSBOZYVSDOVIKCUCDROLKBDOXN"
                            + "OBPYBKXYDROBLOOBDROXZBYMOONCDYCSZSDDRORKBNRKDCZSVVCYEDTECDO"
                            + "XYEQRDYQODBSNYPDROPVIKXNAEKPPCDROBOCDSDCXYGDRONBEXUCDEBXROC"
                            + "DSMUCRSCRKXNSXDYDROLOOBQBKLCDROPVILIDROGSXQCKXNCRYEDCCZSDSD"
                            + "YEDCZSDSDYEDBOKNOBCNSQOCDPOLBEKBIDGYDRYECKXNDOX";

        Map<Character, Double> letterAvgFrequencyMap = generateLetterFrequencyMap(cipherText);
        for (char ch = 'A'; ch <= 'Z'; ch++){
            if (letterAvgFrequencyMap.containsKey(ch)){
                System.out.println(ch + ": " + letterAvgFrequencyMap.get(ch));
            }
        }
        // get the sum of the frequency squared
        System.out.println("Sum of the frequency squared: " + getSumOfFrequencySquared());
    }
}
