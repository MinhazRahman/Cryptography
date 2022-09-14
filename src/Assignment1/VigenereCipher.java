package Assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VigenereCipher {

    public static int ALPHABET_COUNT = 26;

    /* The index of coincidence bounds text should have if it is in the english language */
    public static double ENG_IC_LOWER_BOUND = 0.061;
    public static double ENG_IC_UPPER_BOUND = 0.071;
    public static double ENG_IC = 0.0686;

    // Frequencies of characters in the english alphabet
    public static double A = 8.167;
    public static double B = 1.492;
    public static double C = 2.782;
    public static double D = 4.253;
    public static double E = 12.702;
    public static double F = 2.228;
    public static double G = 2.015;
    public static double H = 6.094;
    public static double I = 6.966;
    public static double J = 0.153;
    public static double K = 0.772;
    public static double L = 4.025;
    public static double M = 2.406;
    public static double N = 6.749;
    public static double O = 7.507;
    public static double P = 1.929;
    public static double Q = 0.095;
    public static double R = 5.987;
    public static double S = 6.327;
    public static double T = 9.056;
    public static double U = 2.758;
    public static double V = 0.978;
    public static double W = 2.360;
    public static double X = 0.150;
    public static double Y = 1.974;
    public static double Z = 0.074;

    public static double[] FREQUENCIES = {
            A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    };

    /**
     * Decrypts the ciphertext using the Vigenère cipher with the key provided.
     * ciphertext: the text to be decrypted
     * key: the key to decrypt the ciphertext- must be characters matching [a-z]
     */
    public static String decryptCipher(String ciphertext, String key) {
        // key must be an alphabetic string
        if (key == null || !key.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException("Invalid key - must be one or more characters in range a...z");

        // only interested in the alphabet
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "").toUpperCase();

        // make key same length as plaintext
        key = repeatString(key, ciphertext.length()).toUpperCase();

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
     * ciphertext: the text to be decrypted
     * shift: the value to subtract from each character in the ciphertext
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
            newPos = Math.floorMod(newPos, ALPHABET_COUNT);
            // add A (65) to the value to get the uppercase character
            newPos += 'A';
            plaintext.append((char) newPos);
        }
        return plaintext.toString();
    }

    /**
     * Calculates a best guess at the length of the key used to encrypt the ciphertext.
     *
     * Done by calculating the index of coincidence for substrings of the ciphertext for increasing lengths and returning
     * the length value if it produces an index of coincidence value "close" to that of english text.
     *
     * If a best guess for the length cannot be determined, -1 is returned.
     *
     * ciphertext: the text to analyse
     * returns a best guess key length for ciphertext
     */
    public static int calculateBestGuessKeyLength(String ciphertext) {
        if (ciphertext == null || ciphertext.length() == 0)
            return -1;
        // ciphertext is restricted to A-Z
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");

        // set the max key length to 12 or the length of the ciphertext if it's too short
        int maxKeyLen = 12;
        maxKeyLen = Math.min(maxKeyLen, ciphertext.length());

        for (int i = 2; i < maxKeyLen; i++) {
            // get each Caesar cipher's encrypted text for a key of length i
            List<String> stringsAtInterval = getAllStringsAtInterval(ciphertext, i);
            // calculate the avg Ic
            double tempIc = stringsAtInterval.stream()
                    // calculate the index of coincidence for every string
                    .mapToDouble(VigenereCipher::indexOfCoincidence)
                    // get the average index of coincidence
                    .average().getAsDouble();
            // if the avg Ic for each Caesar cipher's ciphertext is "close" to the english Ic, we've found our length
            if (closeToEnglish(tempIc))
                return i;
        }

        return -1;
    }

    /**
     * Calculates the key used to encrypt plaintext using the vigenère cipher.
     * First determines the length of the key to be N, then solves N caesar ciphers to get the key value
     * ciphertext: the ciphertext to analyse
     * returns the key used to encrypt the plaintext
     */
    public static String calculateKey(String ciphertext) {
        int length = calculateBestGuessKeyLength(ciphertext);
        // couldn't determine the length
        if (length == -1) return null;

        // we know the length of the key
        // now we need to solve 'length' many caesar ciphers

        // get every character spaced by key length many characters
        // "abcdefgh" with length 3 would be ["adg", "beh", "cf"]
        List<String> caesarCipherStrings = getAllStringsAtInterval(ciphertext, length);
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
     * ciphertext: the ciphertext
     * returns a best guess at the shift value used, or 0 if one can't be determined.
     */
    public static int calculateShift(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");
        int shift = 0;
        double fitness = Integer.MAX_VALUE;
        for (int i = 0; i < ALPHABET_COUNT; i++) {
            // shift the ciphertext by i characters and compute the chi-square for the result
            double tempFitness = chiSquareAgainstEnglish(decrypt(ciphertext, i));
            // if the chi-square was lower than the previous value, make a note of it
            if (tempFitness < fitness) {
                fitness = tempFitness;
                shift = i;
            }
        }
        return shift;
    }

    /**
     * Repeats a given String so its length matches that of the length argument.
     * inStr:  the inStr to be repeated
     * length: the required length
     * returns the repeated inStr
     */
    public static String repeatString(String inStr, int length) {
        if (inStr == null || length <= 0) return "";
        if (inStr.length() == length) return inStr;
        if (inStr.length() > length) return inStr.substring(0, length);

        // need to repeat the inStr
        int keysInLen = length / inStr.length();
        // if the inStr doesn't fit into the length exactly, a truncated version will be used at the end
        int remainder = length - (inStr.length() * keysInLen);

        StringBuilder newKey = new StringBuilder();
        for (int i = 0; i < keysInLen; i++)
            newKey.append(inStr);
        newKey.append(inStr.substring(0, remainder));
        return newKey.toString();
    }

    /**
     * Returns a string consisting of characters spaced by the interval starting with the character at position offset.
     * inStr: the string to split
     * interval: the interval to extract characters
     * offset: the position to start extracting strings
     * returns the string containing every nth character, where n is the interval
     */
    public static String splitStringAtInterval(String inStr, int interval, int offset) {
        if (interval <= 0 || offset < 0) return inStr;
        return IntStream.range(0, inStr.length())
                // get each index that's a multiple of the interval
                .filter(i -> i % interval == 0)
                // get the character at index i + offset
                .mapToObj(i -> i + offset < inStr.length() ? inStr.charAt(i + offset) + "" : "")
                // join them all up to get the resulting string
                .collect(Collectors.joining());
    }

    /**
     * Returns a list of strings that contains characters from {@code inStr} spaced at the passed interval, from an initial offset of 0 up
     * to the size of the interval.
     * inStr: the string to extract substrings from
     * interval: the interval to extract characters
     * returns a list of strings containing characters spaced by the interval
     */
    public static List<String> getAllStringsAtInterval(String inStr, int interval) {
        List<String> strings = new ArrayList<>();
        // iterate up to the interval value or the length of the string
        int len = Math.min(inStr.length(), interval);
        for (int i = 0; i < len; i++)
            strings.add(splitStringAtInterval(inStr, interval, i));
        return strings;
    }

    public static int[] countCharacters(String message) {
        message = message.replaceAll("[^a-zA-Z]", "").toUpperCase();
        int[] counts = new int[ALPHABET_COUNT];
        for (char c : message.toCharArray())
            counts[c - 'A']++;
        return counts;
    }

    /**
     * calculates the expected number of times each character in the alphabet should occur based on
     * the values in FREQUENCIES
     * length: the length of the ciphertext
     * returns the expected number of total of occurrences for each character
     */
    public static double[] expectedCharacterCounts(int length) {
        double[] expected = new double[ALPHABET_COUNT];
        for (int i = 0; i < ALPHABET_COUNT; i++) {
            // length * P(i)
            expected[i] = (length * (FREQUENCIES[i] / 100));
        }
        return expected;
    }

    /**
     * Determines whether the passed index of coincidence is within a tolerance value of the Index of Coincidence of the
     * english language
     * The passed index of coincidence must be greater than ENG_IC_LOWER_BOUND and lower than
     * ENG_IC_UPPER_BOUND to be considered "close" to the english language.
     */
    public static boolean closeToEnglish(double indexOfCoincidence) {
        return ENG_IC_LOWER_BOUND < indexOfCoincidence && indexOfCoincidence < ENG_IC_UPPER_BOUND;
    }

    /**
     * Index of Coincidence is (Σ Fi * (Fi - 1))/N * (N - 1), where Fi is the frequency of the ith character
     * of the alphabet in the ciphertext and N is the length of the input.
     *
     * text: the text to analyse
     * returns index of coincidence
     */
    public static double indexOfCoincidence(String text) {

        // ignore anything other than the alphabet
        text = text.replaceAll("[^a-zA-Z]", "").toUpperCase();
        if (text.length() < 1) return -1;
        // get the probability of each character occurring in the text
        int[] counts = countCharacters(text);

        double sum = 0.0;
        // sum of Fi * (Fi - 1)
        for (int i = 0; i < ALPHABET_COUNT; i++) {
            double fi = counts[i];
            if (fi > 0.0)
                sum += fi * (fi - 1.0);
        }
        // divide by N * (N - 1)
        return sum / (text.length() * (text.length() - 1));
    }

    /**
     * Compares frequencies of characters in the ciphertext with those expected in the english language using
     * a chi-squared test.
     * chiSq = Sum of ((Ci - Ei)^2)/Ei
     * where Ci is the observed number of occurrences of the ith letter of the alphabet in the ciphertext,
     * and Ei is the expected number of occurrences of the ith letter of the alphabet in an english string
     * with the length of ciphertext, based on expectedCharacterCounts(int)
     *
     * ciphertext: the ciphertext to analyse
     * returns a fitness value indicating whether the ciphertext is english
     */
    public static double chiSquareAgainstEnglish(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "");
        // an array containing the total number of times each character occurred in the ciphertext
        int[] characterCounts = countCharacters(ciphertext);
        // an array containing the expected number of times each character should occur in text the length of the
        // ciphertext
        double[] expectedCharacterCounts = expectedCharacterCounts(ciphertext.length());

        double fitness = 0.0;
        for (int i = 0; i < ALPHABET_COUNT; i++) {
            //((Ci - Ei)^2)/Ei
            fitness += Math.pow(characterCounts[i] - expectedCharacterCounts[i], 2) / expectedCharacterCounts[i];
        }
        return fitness;
    }

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

        // Remove all the spaces from the cipher text
        String encrypted = cipherText.replaceAll("\\s+", "");

        int keyLength = calculateBestGuessKeyLength(encrypted);
        System.out.println("Length of the key: " + keyLength);

        String key = calculateKey(encrypted);
        System.out.println("Encryption Key: " + key);

        System.out.println("Decrypted message: ");
        System.out.println(decryptCipher(encrypted, key));
    }

}
