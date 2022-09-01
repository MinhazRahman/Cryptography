package classicCipher;

import java.util.HashMap;
import java.util.Map;

public class ShiftCipher {
    
    // Given a ciphertext, try decrypt with every possible key
    // Only one possibility will “make sense”
    public static void decryptCipherText(Map<Character, Integer> letters, String cipherText){
        // key space K = {0...25}
        int[] keySpace = new int[26];
        for (int key = 0; key < 26; key++){
            keySpace[key] = key;
        }

        // decrypt the ciphertext using all the possible 26 keys
        for (int key: keySpace){
            String message = decrypt(letters, key, cipherText);
            System.out.println("key: " + key + " message: " + message);
        }
    }
    public static String decrypt(Map<Character, Integer> letters, int key, String cipherText){
        StringBuilder message = new StringBuilder();

        // shift each letter of the ciphertext backward by k position
        for (char ch: cipherText.toCharArray()){
            int complement = letters.get(ch) - key;
            if (complement < 0){ // if the complement is negative add 26 to it.
                complement += 26;
            }
            int position = complement % 26;
           char messageLetter = getCipherLetter(position, letters);
            message.append(messageLetter);
        }
        return message.toString();
    }

    public static String encrypt(int key, String message, Map<Character, Integer> letters){
        StringBuilder cipherText = new StringBuilder();

        // iterate through the message and shift each letter by k position
        for (char ch: message.toCharArray()){
            int shiftedPosition = (letters.get(ch) + key) % 26;
            char cipherLetter = getCipherLetter(shiftedPosition, letters);
            cipherText.append(cipherLetter);
        }

        return cipherText.toString();
    }

    public static Character getCipherLetter(int val, Map<Character, Integer> letters){
        for (Map.Entry<Character, Integer> entry: letters.entrySet()){
          if (entry.getValue() == val) return entry.getKey();
        }
        return 0;
    }
    public static void main(String[] args) {
        Map<Character, Integer> letters = new HashMap<>();
        int i = 0;
        for (char ch = 'a'; ch <= 'z'; ch++){
            letters.put(ch, i);
            i++;
        }

        String message = "begintheattacknow";
        // call the encryption method
        System.out.println("Cipher Text: " + encrypt(3, message, letters));

        // call the decryption method
        /** Ciphertext example:
         * ehjlqwkhdwwdfnqrz
         * uryybjbeyq
         * */
        decryptCipherText(letters, "uryybjbeyq");
    }
}
