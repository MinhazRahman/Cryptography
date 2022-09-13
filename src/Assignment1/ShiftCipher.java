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

public class ShiftCipher {
    public static int ALPHABET_COUNT = 26;
    public static double SUM_OF_FREQUENCY_SQUARED = 0.065;

    /** Generate frequency of characters of english alphabet in cipher text
     * @param ciphertext: the text to be analyzed
     * returns character average frequency map in the ciphertext
    */
    public static Map<Character, Double> generateCipherTextCharacterFrequencyMap(String ciphertext){
        Map<Character, Integer> characterFrequencyMap = new HashMap<>();
        int n = ciphertext.length();

        // convert the cipher text into lower case
        String lowerCasedCipherText = ciphertext.toLowerCase();

        for (char letter: lowerCasedCipherText.toCharArray()){
            characterFrequencyMap.put(letter, characterFrequencyMap.getOrDefault(letter, 0) + 1);
        }

        Map<Character, Double> characterAvgFrequencyMap = new HashMap<>();
        for (char ch = 'a'; ch <= 'z'; ch++){
            if (characterFrequencyMap.containsKey(ch)){
                double avg = (double) (characterFrequencyMap.get(ch) * 100) / n;
                characterAvgFrequencyMap.put(ch, Math.floor((avg/100) * 1000) / 1000); // truncate avg to 3 decimal places
            }
        }

        return characterAvgFrequencyMap;
    }

    /** Get the frequency of characters in English alphabet
     * let Pi, denote the frequency of the ith letter in the normal English text and 0 <= Pi <= 1,
     * For example, for i = 0, the frequency of letter 'a', P0 = 0.082 according to the Figure 1.3 in the text book.
     * returns the character average frequency map in the English text
     * */
    public static Map<Character, Double> getPlainTextCharacterFrequencyMap(){
        Map<Character, Double> characterAvgFrequencyMap = new HashMap<>();
        // Frequency of characters in English alphabet
        characterAvgFrequencyMap.put('a', 0.082);
        characterAvgFrequencyMap.put('b', 0.015);
        characterAvgFrequencyMap.put('c', 0.028);
        characterAvgFrequencyMap.put('d', 0.043);
        characterAvgFrequencyMap.put('e', 0.127);
        characterAvgFrequencyMap.put('f', 0.022);
        characterAvgFrequencyMap.put('g', 0.020);
        characterAvgFrequencyMap.put('h', 0.061);
        characterAvgFrequencyMap.put('i', 0.070);
        characterAvgFrequencyMap.put('j', 0.002);
        characterAvgFrequencyMap.put('k', 0.008);
        characterAvgFrequencyMap.put('l', 0.040);
        characterAvgFrequencyMap.put('m', 0.024);
        characterAvgFrequencyMap.put('n', 0.067);
        characterAvgFrequencyMap.put('o', 0.075);
        characterAvgFrequencyMap.put('p', 0.019);
        characterAvgFrequencyMap.put('q', 0.001);
        characterAvgFrequencyMap.put('r', 0.060);
        characterAvgFrequencyMap.put('s', 0.063);
        characterAvgFrequencyMap.put('t', 0.091);
        characterAvgFrequencyMap.put('u', 0.028);
        characterAvgFrequencyMap.put('v', 0.010);
        characterAvgFrequencyMap.put('w', 0.024);
        characterAvgFrequencyMap.put('x', 0.002);
        characterAvgFrequencyMap.put('y', 0.020);
        characterAvgFrequencyMap.put('z', 0.001);

        return characterAvgFrequencyMap;
    }

    /** Calculate the sum of the frequency squared using Figure 1.3
     * Pi^2 == 0.065 where i is the position of each letter in the English alphabet and 0 <= i <= 25
     * returns the sum of the frequency squared
     * */
    public static double getSumOfFrequencySquared(){
        Map<Character, Double> characterAvgFrequencyMap = getPlainTextCharacterFrequencyMap();
        double sum  = 0.0;
        for (Map.Entry<Character, Double> entry: characterAvgFrequencyMap.entrySet()){
            sum += Math.pow(entry.getValue(), 2);
        }
        return sum; // Math.floor(sum * 100) / 1000
    }

    /** Associate the letters of the English alphabet a,...,z with 0,...,25.
     * returns the character position map
     * */
    public static Map<Character, Integer> getCharacterPositionMap(){
        Map<Character, Integer> characterPositionMap = new HashMap<>();
        int position = 0;
        for (char ch = 'a'; ch <= 'z'; ch++){
            characterPositionMap.put(ch, position);
            position++;
        }
        return characterPositionMap;
    }

    /** Get the cipher text character for the new position
     * @param position: the new shifted position of a character
     * returns the shifted character or zero
     * */

    public static Character getCharacterAfterShiftingPosition(int position){
        Map<Character, Integer> characterPositionMap = getCharacterPositionMap();

        for (Map.Entry<Character, Integer> entry: characterPositionMap.entrySet()){
            if (entry.getValue() == position) return entry.getKey();
        }
        return 0;
    }

    /** Compute the sum of multiplication the frequency of the ith letter in the normal text and
     * the frequency of the (i+j)th letter in the cipher text.
     *
     * Compute sum of (Pi)*(Qi+j) where i is an integer and 0 <= i <= 25 for every integer j
     * where 0 <= j <= 25
     * @param cipherText: ciphertext to be analyzed
     * returns a map of type <Ij><Double> where Ij is the key and value is of Double type
     * */
    public static Map<Integer, Double> computeIj(String cipherText){
        List<Double> doubleList = new ArrayList<>();
        Map<Integer, Double> map = new HashMap<>();

        Map<Character, Integer> characterPositionMap = getCharacterPositionMap();
        double Ij = 0.0;
        // iterate over from character 'a' to 'z'
        for (char ch = 'a'; ch <= 'z'; ch++){
            Ij = computePQ(ch, cipherText);
            System.out.println("I" + characterPositionMap.get(ch)  + ": "+ Ij);
            doubleList.add(Ij);
            map.put(characterPositionMap.get(ch), Ij);
        }
        System.out.println("Closest Ij of SUM_OF_FREQUENCY_SQUARED(0.065) is: " + getClosestValue(doubleList, SUM_OF_FREQUENCY_SQUARED));
        System.out.println("The Key is: " + getKey(map, SUM_OF_FREQUENCY_SQUARED));

        return map;
    }

    /** Compute PQ where P is the frequency of the normal text and
     * Q is the frequency of cipher text
     * @param chj: character at the jth position in the character position map
     * @param cipherText: cipher text to be analyzed
     * returns the sum of Pi*Q(i+j) for 0 <= i <= 25 for a particular value of j
     * */
    public static double computePQ(char chj, String cipherText){
        Map<Character, Integer> characterPositionMap = getCharacterPositionMap();
        Map<Character, Double> p = getPlainTextCharacterFrequencyMap();
        Map<Character, Double> q = generateCipherTextCharacterFrequencyMap(cipherText);
        double sum = 0.0;
        for (char chi = 'a'; chi <= 'z'; chi++){
            int pos = (characterPositionMap.get(chi)+characterPositionMap.get(chj)) % 26;
            char ch = getCharacterAfterShiftingPosition(pos);
            if (p.containsKey(chi) && q.containsKey(ch)){
                sum += p.get(chi) * q.get(ch);
            }
        }
        return sum;
    }

    /** Get the key by comparing the values of Ij with SUM_OF_FREQUENCY_SQUARED = 0.065.
     * The value j is the key for which Ij is closest to SUM_OF_FREQUENCY_SQUARED = 0.065
     * @param characterPositionIjMap: a map of the format <characterPosition><Ij>
     * @param target: double value we want to compare with
     * */
    public static int getKey(Map<Integer, Double> characterPositionIjMap, double target){
        double closestValue = 0.0;
        int key = 0;
        double current = Double.MAX_VALUE;
        for (Map.Entry<Integer, Double> entry: characterPositionIjMap.entrySet()) {
            Double value = entry.getValue();
            if (Math.abs(value - target) < current) {
                closestValue = value;
                key = entry.getKey();
                current = Math.abs(value - target);
            }
        }
        return key;
    }

    /** Get the closest value of target from a given list of values
     * @param list: contains a list of value of type double
     * @param target: a given value that we want to compare with
     * */
    public static Double getClosestValue(List<Double> list, double target){
        double answer = list.get(0);
        double current = Double.MAX_VALUE;
        for (Double value : list) {
            if (Math.abs(value - target) < current) {
                answer = value;
                current = Math.abs(value - target);
            }
        }

        return answer;
    }

    /** Decrypts the ciphertext using the Shift cipher with the key provided.
     * @param cipherText: the text to be decrypted
     * @param key: the key to decrypt the ciphertext-must be from 0 to 25.
     * */
    public static String decrypt(String cipherText, int key){
        Map<Character, Integer> characterPositionMap = getCharacterPositionMap();
        StringBuilder plaintext = new StringBuilder();

        // convert the cipher text to lower case
        String lowerCasedCipherText = cipherText.toLowerCase();

        // shift each letter of the ciphertext backward by k position
        for (char ch: lowerCasedCipherText.toCharArray()){
            int complement = characterPositionMap.get(ch) - key;
            if (complement < 0){ // if the complement is negative add 26 to it.
                complement += ALPHABET_COUNT;
            }
            int newPosition = complement % ALPHABET_COUNT;
            char plaintextChar = getCharacterAfterShiftingPosition(newPosition);
            plaintext.append(plaintextChar);
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        String cipherText = "KWSVVSYXKSBOKRKBNRKDKXNKNBEXUKBOKDKLKBGROXDROIQODDROSBLOOBC"
                            + "DROIXYDSMOKPVISXOKMRWEQDROWSVVSYXKSBOZYVSDOVIKCUCDROLKBDOXN"
                            + "OBPYBKXYDROBLOOBDROXZBYMOONCDYCSZSDDRORKBNRKDCZSVVCYEDTECDO"
                            + "XYEQRDYQODBSNYPDROPVIKXNAEKPPCDROBOCDSDCXYGDRONBEXUCDEBXROC"
                            + "DSMUCRSCRKXNSXDYDROLOOBQBKLCDROPVILIDROGSXQCKXNCRYEDCCZSDSD"
                            + "YEDCZSDSDYEDBOKNOBCNSQOCDPOLBEKBIDGYDRYECKXNDOX";

        System.out.println("Plain text character frequency mapping: ");
        Map<Character, Double> characterFrequencyMap = generateCipherTextCharacterFrequencyMap(cipherText);
        for (char ch = 'a'; ch <= 'z'; ch++){
            if (characterFrequencyMap.containsKey(ch)){
                System.out.println(ch + ": " + characterFrequencyMap.get(ch));
            }
        }
        // get the sum of the frequency squared
        System.out.println("Sum of the frequency squared: " + getSumOfFrequencySquared());

        System.out.println("Ij (I0...I25) values: ");
        Map<Integer, Double> map = computeIj(cipherText);

        int key = getKey(map, SUM_OF_FREQUENCY_SQUARED);

        System.out.println("Decrypted Plain Text: ");
        System.out.println(decrypt(cipherText, key));
    }
}
