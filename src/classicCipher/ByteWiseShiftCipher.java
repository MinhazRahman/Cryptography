package classicCipher;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Principles of modern cryptography over the past several decades cryptography has developed
 * into more of a science schemes are now developed and analyzed in a more systematic manner
 * with the ultimate goal being to give a rigorous proof that a given construction is secure in
 * order to articulate such proofs we first need formal definitions that pin down exactly what
 * secure means such definitions are useful and interesting in their own right as it turns out
 * most cryptographic proofs rely on currently unproven assumptions about the algorithmic hardness
 * of certain mathematical problems any such assumptions must be made explicit and be stated precisely
 * an emphasis on definitions assumptions and proofs distinguishes modern cryptography from classical
 * cryptography we now discuss these three principles in greater detail
 *
 *
 * */

public class ByteWiseShiftCipher {
    public static int KEY_COUNT = 256;

    public static String encrypt(String plaintext, String hexKey) throws IOException {
        StringBuilder hexStringCiphertext = new StringBuilder();

        // convert the hexKey string into base 16
        int key = Integer.parseInt(hexKey, 16);

        for (char ch: plaintext.toCharArray()){
            // typecast the plaintext character ch into ascii value
            int ascii = ch;
            // XOR the ascii with the key
            int xorResult = (ascii ^ key) % KEY_COUNT;
            // convert the xorResult into a hex string
            String hexString = Integer.toHexString(xorResult).toUpperCase();
            if (hexString.length()  < 2){ // hexString.length() % 2 != 0
                hexString = "0" + hexString;
            }
            hexStringCiphertext.append(hexString);
        }

        return  hexStringCiphertext.toString();
    }

    public static String decrypt(String ciphertext, String hexKey) throws IOException {
        StringBuilder sbPlaintext = new StringBuilder();

        // convert the hexKey string into decimal value
        int key = Integer.parseInt(hexKey, 16);

        // divide the ciphertext into groups of hex strings of length two
        for (int i = 0; i < ciphertext.length(); i += 2){
            // extract two characters from hex string
            String hexString = ciphertext.substring(i, i+2);
            // convert each group of hex string into base 16
            int decimal = Integer.parseInt(hexString, 16);
            // XOR the base 16 value with the key to get the ascii
            int ascii = (decimal ^ key) % KEY_COUNT;
            // typecast as the character
            char character = (char) ascii;
            sbPlaintext.append(character);
        }

        return sbPlaintext.toString();
    }

    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    public static void writeIntoFile(String text, String fileName) throws IOException {
        // Create a FileWriter object
        // to write in the file
        FileWriter fWriter = new FileWriter(fileName);

        // Writing into file
        // The file content is inside the string
        fWriter.write(text);

        // Closing the file writing connection
        fWriter.close();
    }
    
    public static void main(String[] args) throws Exception {
        // read the hex string key from the key.txt file
        String hexKey = readFileAsString("src/classicCipher/key.txt");
        System.out.println("Hex Key: " + hexKey);

        // read the plaintext from plaintext.txt file
        String plaintext = readFileAsString("src/classicCipher/plaintext.txt");
        System.out.println("\nPlaintext: \n" + plaintext);

        // encrypt the plain text
        String ciphertext = encrypt(plaintext, hexKey);
        // write the encrypted text into the ciphertext.txt file
        writeIntoFile(ciphertext, "src/classicCipher/ciphertext.txt");
        // read the ciphertext from the ciphertext.txt file
        ciphertext = readFileAsString("src/classicCipher/ciphertext.txt");
        System.out.println("\nCiphertext in hex format: \n" + ciphertext);

        String decryptedPlaintext = decrypt(ciphertext, hexKey);
        // write the decrypted text into the original_plaintext.txt file
        writeIntoFile(decryptedPlaintext, "src/classicCipher/original_plaintext.txt");
        System.out.println("\nDecrypted original text: \n" + decryptedPlaintext);
    }
}
