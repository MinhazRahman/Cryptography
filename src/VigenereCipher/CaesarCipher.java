package VigenereCipher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class used for operations concerning the Caesar cipher - encryption, decryption, cracking
 */
public class CaesarCipher {

    public static void main(String[] args) throws Exception {

        String plaintext = "ILOVECAKE";
        int shift = 3;
        String cipherText = CaesarCipher.encrypt(plaintext, 3);

        System.out.println("Plaintex: " + plaintext);
        System.out.println("Shift: " + shift);
        System.out.println("Ciphertext: " + cipherText);

        String decryptedText = CaesarCipher.decrypt(cipherText, 3);
        System.out.println("Decryption result: " + decryptedText);

        System.out.println("\nBrute Force Attack");
        CaesarCipher.bruteForceAttack(cipherText);

        System.out.println("FREQUENCY ANALYSIS");
        System.out.println("Encrypting The Restaurant at the End of the Universe...");
        String bigText = CaesarCipher.readFile(CaesarCipher.class.getResource("/hitch2.txt"));
        String bigCipherText = CaesarCipher.encrypt(bigText, 3);
        System.out.println(bigCipherText.substring(0, 200) + "...");

        System.out.println("Performing frequency analysis...");
        String decryptedBigCipherText = CaesarCipher.frequencyAnalysis(bigCipherText);
        System.out.println(decryptedBigCipherText.substring(0, 200) + "...");

    }

    /**
     * boring stuff - returns the string of data held in the file
     */
    public static String readFile(URL url) throws IOException, URISyntaxException {
        byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
        return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
    }

    /**
     * Encrypts the plaintext by shifting each character by the value of shift
     *
     * @param plaintext the message to be encrypted
     * @param shift     the value to shift each character
     * @return the encyrpted string
     */
    public static String encrypt(String plaintext, int shift) {
        // only interested in the alphabet
        plaintext = plaintext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        StringBuilder ciphertext = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            // all upper case chars are in the ascii range 65-90.
            // Subtracting A (65) from from the character gives us a value in the range of 0 25
            int newPos = c - 'A';
            // add the shift to the position
            newPos += shift;
            // perform the modulo to make sure the result is in the range of 0-25
            newPos = Math.floorMod(newPos, LetterFrequencyUtils.ALPHABET_COUNT);
            // add A (65) to the value to get the uppercase character
            newPos += 'A';
            ciphertext.append((char) newPos);
        }
        return ciphertext.toString();
    }

    /**
     * Decrypts the ciphertext by shifting each character in the ciphertext by the negated value of shift.
     * <p>
     * <p>e.g. if the shift value used to encrypt was 3, to decrypt, the shift value must be -3 (3 + -3 = 0)
     * <p>Note: Could call {@code encrypt(ciphertext, -shift)} for same result. I'm just being explicit here for learning purposes.
     *
     * @param ciphertext
     * @param shift      the value to subtract from each character in the ciphertext
     * @return
     */
    public static String decrypt(String ciphertext, int shift) {
        // only interested in the alphabet
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        StringBuilder plaintext = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            // all upper case chars are in the ascii range 65-90.
            // Subtracting A (65) from from the character gives us a value in the range of 0 25
            int newPos = c - 'A';
            // subtract the shift from the position
            newPos -= shift;
            // perform the modulo to make sure the result is in the range of 0-25
            newPos = Math.floorMod(newPos, LetterFrequencyUtils.ALPHABET_COUNT);
            // add A (65) to the value to get the uppercase character
            newPos += 'A';
            plaintext.append((char) newPos);
        }
        return plaintext.toString();
    }

    /**
     * Applies all possible shift values to the ciphertext and prints result
     * <p>
     * Requires human to check results
     *
     * @param ciphertext the ciphertext to decrypt
     */
    public static void bruteForceAttack(String ciphertext) {
        // shift of 0 or 26 would result in no change
        for (int shift = 1; shift < LetterFrequencyUtils.ALPHABET_COUNT; shift++)
            System.out.println("shift: " + shift + ", " + decrypt(ciphertext, shift));
    }

    /**
     * Looks at the frequency of characters in the ciphertext and compares them with the frequencies of characters in the
     * english language.
     * <p>
     * It then calculates the most likely shift value by finding the shift that results in similar frequencies to
     * the english language.
     *
     * @param ciphertext the encrypted message
     * @return best guess at plaintext
     */
    public static String frequencyAnalysis(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");
        int bestShift = calculateShift(ciphertext);
        return decrypt(ciphertext, bestShift);
    }

    /**
     * Uses the {@link LetterFrequencyUtils#chiSquareAgainstEnglish(String)}
     *
     * @param ciphertext the ciphertext to analyse
     * @return a best guess at the shift value used, or 0 if one can't be determined.
     */
    public static int calculateShift(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");
        int shift = 0;
        double fitness = Integer.MAX_VALUE;
        for (int i = 0; i < LetterFrequencyUtils.ALPHABET_COUNT; i++) {
            // shift the ciphertext by i characters and compute the chi-square for the result
            double tempFitness = LetterFrequencyUtils.chiSquareAgainstEnglish(CaesarCipher.decrypt(ciphertext, i));
            // if the chi-square was lower than the previous value, make a note of it
            if (tempFitness < fitness) {
                fitness = tempFitness;
                shift = i;
            }
        }
        return shift;
    }
}
