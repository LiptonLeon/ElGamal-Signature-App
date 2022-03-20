package egs.alg;

import egs.alg.util.BigNatural;
import egs.alg.util.FileIO;

import java.io.IOException;
import java.net.URL;

public class Main_terminal {
    public static void main(String[] args) throws IOException {
        URL uri = Main_terminal.class.getResource("file1.txt");
        byte[] dane = FileIO.getFileContent(uri.getPath());
        for(int i = 0; i < dane.length; i++) {
            System.out.print((char)(dane[i]));

        }
        System.out.println("");
        System.out.println("toString 1: " + BigNatural.one);
        BigNatural some = new BigNatural(1234567L);
        System.out.println("toString 1234567 in hex: " + some);

        BigNatural a = new BigNatural("100ff");
        BigNatural b = new BigNatural("100");
        System.out.printf("is %s > %s: %b\n", a, b, a.gt(b));
        System.out.printf("%s + %s = %s\n", a, b, a.add(b));
        System.out.printf("%s - %s = %s\n", a, b, a.subtract(b));
        System.out.printf("%s - %s = %s\n", b, a, b.subtract(a));
        System.out.printf("%s * %s = %s\n", a, b, a.multiply(b));
        System.out.printf("%s / %s = %s\n", a, b, a.divide(b));
        System.out.printf("%s / %s = %s\n", b, a, b.divide(a));
        System.out.printf("%s %% %s = %s\n", a, b, a.mod(b));
        System.out.printf("%s %% %s = %s\n", a, b, b.mod(a));
        System.out.println("divide with reminder:");
        BigNatural r = new BigNatural(1);
        System.out.println("a / b = " + a.divide(b, r) + " r = " + r);
        System.out.println("b / a = " + b.divide(a, r) + " r = " + r);


        BigNatural rand = BigNatural.getRandom(16); // 16 * 16 = 256 bit
        System.out.println("random: " + rand);
//        BigNatural p = BigNatural.probablePrime(16);
//        System.out.println("p = propable prime: " + p);
        // convert to base10: https://www.rapidtables.com/convert/number/hex-to-decimal.html
        // check if prime: https://onlinemathtools.com/test-prime-number
    }
}
