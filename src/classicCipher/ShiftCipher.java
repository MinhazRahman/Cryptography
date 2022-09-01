package classicCipher;

import java.util.HashMap;
import java.util.Map;

public class ShiftCipher {
    // Given a ciphertext, try decrypt with every possible key
    // Only one possibility will “make sense”
    public static void decryptCipherText(String cipherText){
        // key space K = {0...25}
        int[] keySpace = new int[26];
        for (int key = 0; key < 26; key++){
            keySpace[key] = key;
        }
        // convert the cipher text to lower case
        String lowerCaseCipherText = cipherText.toLowerCase();

        // decrypt the ciphertext using all the possible 26 keys
        for (int key: keySpace){
            String message = decrypt(key, lowerCaseCipherText);
            System.out.println("key: " + key + " message: " + message);
        }
    }
    public static String decrypt(int key, String cipherText){
        Map<Character, Integer> letterPositionMap = getLetterPositionMap();
        StringBuilder message = new StringBuilder();

        // shift each letter of the ciphertext backward by k position
        for (char ch: cipherText.toCharArray()){
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

    public static String encrypt(int key, String message){
        Map<Character, Integer> letterPositionMap = getLetterPositionMap();
        StringBuilder cipherText = new StringBuilder();

        // iterate through the message and shift each letter by k position
        for (char ch: message.toCharArray()){
            int shiftedPosition = (letterPositionMap.get(ch) + key) % 26;
            char cipherLetter = getLetterAfterShiftingPosition(shiftedPosition);
            cipherText.append(cipherLetter);
        }
        return cipherText.toString();
    }

    public static Character getLetterAfterShiftingPosition(int position){
        Map<Character, Integer> letterPositionMap = getLetterPositionMap();

        for (Map.Entry<Character, Integer> entry: letterPositionMap.entrySet()){
          if (entry.getValue() == position) return entry.getKey();
        }
        return 0;
    }

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
        String message = "begintheattacknow";
        // call the encryption method
        System.out.println("Cipher Text: " + encrypt(3, message));

        // call the decryption method
        /** Ciphertext example:
         * ehjlqwkhdwwdfnqrz
         * uryybjbeyq
         * OVDTHUFWVZZPISLRLFZHYLAOLYL
         * */
        // only one possible outcome will make sense
        decryptCipherText("OVDTHUFWVZZPISLRLFZHYLAOLYL");
    }
}
