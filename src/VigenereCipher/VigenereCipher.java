package VigenereCipher;

import java.util.List;

public class VigenereCipher {

    public static void main(String[] args) {
        String cipherText =
                        "VPTHP DQVSA VVGEP ZMEVM CAEKK DTIPF UADTX YGLPF"
                        + "WNGZT SSEIM GVJKG VTUHZ POLPX YOIVU MWKKT UXVXM"
                        + "CPRXU WUJSI TCVHX VFXXU OJMQT ZXYGP JUXZP OHLEJ"
                        + "QVLHW FXMGD MKJPD BRUUI CKKLP AEBXR YINMS IUQMT"
                        + "SEVPH ALVXQ CLCRT LHDII GJJZC RIIXU EJVPT DICNW"
                        + "GNEEK HTKJR TUTYW KTMPA IUVPT PVMKV TZEEF BWLQF"
                        + "TMAHG BCLPP WZEIA UIZIP QVVJJ CGYMV FBDKS KJMEY"
                        + "YEKVV ALVAA WVYCF PPCIU QVTPR EQDTT FVT";

        String encrypted = cipherText.replaceAll("\\s+", "");

        int keyLength = calculateBestGuessKeyLen(encrypted);
        System.out.println("Ken length: " + keyLength);

        String key = calculateKey(encrypted);
        System.out.println("Key: " + key);

        System.out.println("Decrypted message: ");
        System.out.println(decryptCipher(encrypted, key));
    }

    /**
     * Decrypts the {@code ciphertext} using the Vigenère cipher with the {@code key} provided.
     * <p>See {@link StringUtils#repeatString} for more details on how the key is used.
     *
     * @param ciphertext the text to be decrypted
     * @param key        the key to decrypt the ciphertext- must be characters matching [a-z]
     * @return
     */
    public static String decryptCipher(String ciphertext, String key) {
        // key must be an alphabetic string
        if (key == null || !key.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException("Invalid key - must be one or more characters in range a...z");

        // only interested in the alphabet
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "").toUpperCase();

        // make key same length as plaintext
        key = StringUtils.repeatString(key, ciphertext.length()).toUpperCase();

        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            // get the character at index i
            String toDecrypt = String.valueOf(ciphertext.charAt(i));
            // the shift is equal to the char at index i of the key
            // subtracting 'A' (65) to have a value in range 0-25
            int shift = key.charAt(i) - 'A';
            plaintext.append(decrypt(toDecrypt, shift));
        }
        return plaintext.toString();
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
            // Subtracting A (65) from the character gives us a value in the range of 0 25
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
     * Calculates a best guess at the length of the key used to encrypt the ciphertext.
     * <p>
     * Done by calculating the index of coincidence for substrings of the ciphertext for increasing lengths and returning
     * the length value if it produces an index of coincidence value "close" to that of english text.
     * <p>
     * If a best guess for the length cannot be determined, -1 is returned.
     *
     * @param ciphertext the text to analyse
     * @return a best guess key length for {@code ciphertext}
     */
    public static int calculateBestGuessKeyLen(String ciphertext) {
        if (ciphertext == null || ciphertext.length() == 0)
            return -1;
        // ciphertext is restricted to A-Z
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");

        // set the max key length to 12 or the length of the ciphertext if it's too short
        int maxKeyLen = 12;
        maxKeyLen = maxKeyLen > ciphertext.length() ? ciphertext.length() : maxKeyLen;

        for (int i = 2; i < maxKeyLen; i++) {
            // get each Caesar cipher's encrypted text for a key of length i
            List<String> stringsAtInterval = StringUtils.getAllStringsAtInterval(ciphertext, i);
            // calculate the avg Ic
            double tempIc = stringsAtInterval.stream()
                    // calculate the index of coincidence for every string
                    .mapToDouble(LetterFrequencyUtils::indexOfCoincidence)
                    // get the average index of coincidence
                    .average().getAsDouble();
            // if the avg Ic for each Caesar cipher's ciphertext is "close" to the english Ic, we've found our length
            if (LetterFrequencyUtils.closeToEng(tempIc))
                return i;
        }

        return -1;
    }

    /**
     * Calculates the key used to encrypt plaintext using the vigenère cipher.
     * <p>
     * First determines the length of the key to be N, then solves N caesar ciphers to get the key value
     *
     * @param ciphertext the ciphertext to analyse
     * @return the key used to encrypt the plaintext
     */
    public static String calculateKey(String ciphertext) {
        int length = calculateBestGuessKeyLen(ciphertext);
        // couldn't determine the length - give up
        if (length == -1) return null;

        // we know the length of the key
        // now we need to solve 'length' many caesar ciphers - easy!

        // get every character spaced by key length many characters
        // "abcdefgh" with length 3 would be ["adg", "beh", "cf"]
        List<String> caesarCipherStrings = StringUtils.getAllStringsAtInterval(ciphertext, length);
        StringBuilder key = new StringBuilder();
        for (String caesarCipherTxt : caesarCipherStrings) {
            // crack the caesar cipher text
            int shift = calculateShift(caesarCipherTxt);
            // add 'A' (65) to the shift value to get an ASCII character in the range A-Z
            char charVal = (char) (shift + 'A');
            // add the character to the key string
            key.append(charVal);
        }

        return key.toString();
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
            double tempFitness = LetterFrequencyUtils.chiSquareAgainstEnglish(decrypt(ciphertext, i));
            // if the chi-square was lower than the previous value, make a note of it
            if (tempFitness < fitness) {
                fitness = tempFitness;
                shift = i;
            }
        }
        return shift;
    }
}
