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

        String s1 = "e47";
        System.out.println("Given s: " + s1);
        String s2 = "d58";
        System.out.println("Given s: " + s2);
        BigNatural a = new BigNatural(s1);
        BigNatural b = new BigNatural(s2);
        System.out.println("toString a: " + a);
        System.out.println("toString b: " + b);
        BigNatural c = a.add(b);
        System.out.println("toString c = a + b: " + c);
        BigNatural d = a.multiply(b);
        System.out.println("toString d = a * b: " + d);
        BigNatural r = BigNatural.getRandom(16); // 16 * 16 = 256 bit
        System.out.println("toString random: " + r);
        BigNatural p = BigNatural.probablePrime(16);
//        System.out.println("toString propable prime: " + p + " and in base10: " + p.toString(10));
    }
}
