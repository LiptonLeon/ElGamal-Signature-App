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
        String s = "ac16253";
        System.out.println("Given s: " + s);
        BigNatural a = new BigNatural(s);
        System.out.println("toString a: " + a);

    }
}
