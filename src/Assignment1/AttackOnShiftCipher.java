package Assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static Map<Character, Double> generateCipherTextLetterFrequencyMap(String ciphertext){
        Map<Character, Integer> letterFrequencyMap = new HashMap<>();
        int n = ciphertext.length();

        // convert the cipher text into lower case
        String lowerCasedCipherText = ciphertext.toLowerCase();

        for (char letter: lowerCasedCipherText.toCharArray()){
            letterFrequencyMap.put(letter, letterFrequencyMap.getOrDefault(letter, 0) + 1);
        }

        Map<Character, Double> letterAvgFrequencyMap = new HashMap<>();
        for (char ch = 'a'; ch <= 'z'; ch++){
            if (letterFrequencyMap.containsKey(ch)){
                double avg = (double) (letterFrequencyMap.get(ch) * 100) / n;
                letterAvgFrequencyMap.put(ch, Math.floor((avg/100) * 1000) / 1000); // truncate avg to 3 decimal places
            }
        }

        return letterAvgFrequencyMap;
    }

    public static Map<Integer, Double> generateLetterPositionFrequencyMap(String ciphertext){
        Map<Character, Integer> letterPositionMap = getLetterPositionMap();
        Map<Character, Integer> letterFrequencyMap = new HashMap<>();
        int n = ciphertext.length();

        // convert the cipher text into lower case
        String lowerCasedCipherText = ciphertext.toLowerCase();

        for (char letter: lowerCasedCipherText.toCharArray()){
            letterFrequencyMap.put(letter, letterFrequencyMap.getOrDefault(letter, 0) + 1);
        }

        Map<Integer, Double> letterPositionAvgFrequencyMap = new HashMap<>();
        for (int ch = 'a'; ch <= 'z'; ch++){
            if (letterFrequencyMap.containsKey(ch)){
                double avg = (double) (letterFrequencyMap.get(ch) * 100) / n;
                int position = letterPositionMap.get(ch);
                letterPositionAvgFrequencyMap.put(position, avg); // truncate avg to 3 decimal places
                // Math.floor((avg/100) * 1000) / 1000
            }
        }

        return letterPositionAvgFrequencyMap;
    }

    // let Pi, denote the frequency of the ith letter in the normal English text and 0 <= Pi <= 1,
    // For example, for i = 0, the frequency of letter 'a', P0 = 0.082 according to the Figure 1.3 in the text book.
    public static Map<Character, Double> getPlainTextLetterFrequencyMap(){
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

    public static Map<Integer, Double> getPlainTextPositionAvgFrequencyMap(){
        Map<Integer, Double> positionAvgFrequencyMap = new HashMap<>();
        positionAvgFrequencyMap.put(0, 0.082);
        positionAvgFrequencyMap.put(1, 0.015);
        positionAvgFrequencyMap.put(2, 0.028);
        positionAvgFrequencyMap.put(3, 0.043);
        positionAvgFrequencyMap.put(4, 0.127);
        positionAvgFrequencyMap.put(5, 0.022);
        positionAvgFrequencyMap.put(6, 0.020);
        positionAvgFrequencyMap.put(7, 0.061);
        positionAvgFrequencyMap.put(8, 0.070);
        positionAvgFrequencyMap.put(9, 0.002);
        positionAvgFrequencyMap.put(10, 0.008);
        positionAvgFrequencyMap.put(11, 0.040);
        positionAvgFrequencyMap.put(12, 0.024);
        positionAvgFrequencyMap.put(13, 0.067);
        positionAvgFrequencyMap.put(14, 0.075);
        positionAvgFrequencyMap.put(15, 0.019);
        positionAvgFrequencyMap.put(16, 0.001);
        positionAvgFrequencyMap.put(17, 0.060);
        positionAvgFrequencyMap.put(18, 0.063);
        positionAvgFrequencyMap.put(19, 0.091);
        positionAvgFrequencyMap.put(20, 0.028);
        positionAvgFrequencyMap.put(21, 0.010);
        positionAvgFrequencyMap.put(22, 0.024);
        positionAvgFrequencyMap.put(23, 0.002);
        positionAvgFrequencyMap.put(24, 0.020);
        positionAvgFrequencyMap.put(25, 0.001);

        return positionAvgFrequencyMap;
    }

    // Calculate the sum of the frequency squared using Figure 1.3
    // Pi^2 == 0.065 where i is the position of each letter in the English alphabet and 0 <= i <= 25
    public static double getSumOfFrequencySquared(){
        Map<Character, Double> letterAvgFrequencyMap = getPlainTextLetterFrequencyMap();
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

    public static Character getLetterAfterShiftingPosition(int position){
        Map<Character, Integer> letterPositionMap = getLetterPositionMap();

        for (Map.Entry<Character, Integer> entry: letterPositionMap.entrySet()){
            if (entry.getValue() == position) return entry.getKey();
        }
        return 0;
    }

    // compute the sum of multiplication the frequency of the ith letter in the normal text and
    // the frequency of the (i+j)th letter in the cipher text.
    // compute sum of Pi*Qi+j where i is an integer and 0 <= i <= 25 for every integer j
    // where 0 <= j <= 25
    public static Map<Integer, Double> computeIj(String cipherText){
        List<Double> doubleList = new ArrayList<>();
        Map<Integer, Double> map = new HashMap<>();

        Map<Character, Integer> letterPositionMap = getLetterPositionMap();
        double Ij = 0.0;
        for (char ch = 'a'; ch <= 'z'; ch++){
            Ij = computePQ(ch, cipherText);
            System.out.println("I" + letterPositionMap.get(ch)  + ": "+ Ij);
            doubleList.add(Ij);
            map.put(letterPositionMap.get(ch), Ij);
        }
        System.out.println("Closest Ij of 0.065 is: " + getClosestValue(doubleList, 0.065));
        System.out.println("For j which is the Key is: " + getKeyValue(map, 0.065));

        return map;
    }

    public static double computePQ(char chj, String cipherText){
        Map<Character, Integer> letterPositionMap = getLetterPositionMap();
        Map<Character, Double> p = getPlainTextLetterFrequencyMap();
        Map<Character, Double> q = generateCipherTextLetterFrequencyMap(cipherText);
        double sum = 0.0;
        for (char chi = 'a'; chi <= 'z'; chi++){
            int pos = (letterPositionMap.get(chi)+letterPositionMap.get(chj)) % 26;
            char ch = getLetterAfterShiftingPosition(pos);
            if (p.containsKey(chi) && q.containsKey(ch)){
                sum += p.get(chi) * q.get(ch);
            }
        }
        return sum;
    }

    public static String decrypt(int key, String cipherText){
        Map<Character, Integer> letterPositionMap = getLetterPositionMap();
        StringBuilder message = new StringBuilder();

        // convert the cipher text to lower case
        String lowerCasedCipherText = cipherText.toLowerCase();

        // shift each letter of the ciphertext backward by k position
        for (char ch: lowerCasedCipherText.toCharArray()){
            int complement = letterPositionMap.get(ch) - key;
            if (complement < 0){ // if the complement is negative add 26 to it.
                complement += 26;
            }
            int position = complement % 26;
            char messageLetter = getLetterAfterShiftingPosition(position);
            message.append(messageLetter);
        }
        return message.toString();
    }

    // get the key by comparing the values of Ij with 0.065.
    // The value j is the key for which Ij is closest to 0.065
    public static int getKeyValue(Map<Integer, Double> map, double x){
        double closestValue = 0.0;
        int key = 0;
        double current = Double.MAX_VALUE;
        for (Map.Entry<Integer, Double> entry: map.entrySet()) {
            Double value = entry.getValue();
            if (Math.abs(value - x) < current) {
                closestValue = value;
                key = entry.getKey();
                current = Math.abs(value - x);
            }
        }
        return key;
    }

    public static Double getClosestValue(List<Double> list, double x){
        double answer = list.get(0);
        double current = Double.MAX_VALUE;
        for (Double value : list) {
            if (Math.abs(value - x) < current) {
                answer = value;
                current = Math.abs(value - x);
            }
        }

        return answer;
    }

    public static void main(String[] args) {
        String cipherText = "KWSVVSYXKSBOKRKBNRKDKXNKNBEXUKBOKDKLKBGROXDROIQODDROSBLOOBC"
                            + "DROIXYDSMOKPVISXOKMRWEQDROWSVVSYXKSBOZYVSDOVIKCUCDROLKBDOXN"
                            + "OBPYBKXYDROBLOOBDROXZBYMOONCDYCSZSDDRORKBNRKDCZSVVCYEDTECDO"
                            + "XYEQRDYQODBSNYPDROPVIKXNAEKPPCDROBOCDSDCXYGDRONBEXUCDEBXROC"
                            + "DSMUCRSCRKXNSXDYDROLOOBQBKLCDROPVILIDROGSXQCKXNCRYEDCCZSDSD"
                            + "YEDCZSDSDYEDBOKNOBCNSQOCDPOLBEKBIDGYDRYECKXNDOX";

        System.out.println("Plain text character frequency mapping: ");
        Map<Character, Double> letterAvgFrequencyMap = generateCipherTextLetterFrequencyMap(cipherText);
        for (char ch = 'a'; ch <= 'z'; ch++){
            if (letterAvgFrequencyMap.containsKey(ch)){
                System.out.println(ch + ": " + letterAvgFrequencyMap.get(ch));
            }
        }
        // get the sum of the frequency squared
        System.out.println("Sum of the frequency squared: " + getSumOfFrequencySquared());

        System.out.println("Ij (I0...I25) values: ");
        Map<Integer, Double> map = computeIj(cipherText);

        int key = getKeyValue(map, 0.065);

        System.out.println("Decrypted Plain Text: ");
        System.out.println(decrypt(key, cipherText));
    }
}
