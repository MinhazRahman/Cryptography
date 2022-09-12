package classicCipher;

public class ByteWiseShiftCipher {
    public static int KEY_COUNT = 256;

    public static String encrypt(String plaintext, String hexKey){
        StringBuilder hexStringCiphertext = new StringBuilder();

        // convert the hexKey string into base 16
        int key = Integer.parseInt(hexKey, 16);
        System.out.println("Hex Key: " + hexKey);
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Encrypted text: ");

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

    public static String decrypt(String ciphertext, String hexKey){
        StringBuilder sbPlaintext = new StringBuilder();

        // convert the hexKey string into decimal value
        int key = Integer.parseInt(hexKey, 16);
        System.out.println("Hex Key: " + hexKey);
        System.out.println("Ciphertext: " + ciphertext);
        System.out.println("Decrypted text: ");

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
    
    public static void main(String[] args) {
        String hexKey = "11";
        String plaintext = "HelloWorld";

        String ciphertext = encrypt(plaintext, hexKey);
        System.out.println(ciphertext);

        System.out.println();
        System.out.println(decrypt(ciphertext, hexKey));
    }
}
