package classicCipher;

public class ByteWiseShiftCipherTest {
    public static void main(String[] args) {

        int key = 6;
        int plaintext = 8;
        int cipher = plaintext ^ key;
        System.out.println("Dec: " + cipher + " Hex: " + Integer.toHexString(cipher).toUpperCase());

        // convert both the hex key and the plaintext character into decimal
        System.out.println(Integer.parseInt("0A", 16));
        int n = Character.getNumericValue('P');
        System.out.println(n);
        int result = (n ^ Integer.parseInt("0A", 16)) % 255;
        System.out.println(result);
        System.out.println(Integer.toHexString(result).toUpperCase());
    }
}
