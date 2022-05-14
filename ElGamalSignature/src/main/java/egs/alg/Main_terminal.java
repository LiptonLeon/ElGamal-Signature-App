package egs.alg;

import egs.alg.util.BigNoLongerNatural;
import egs.alg.util.FileIO;

import java.io.IOException;
import java.net.URL;

public class Main_terminal {
    public static void main(String[] args) throws IOException {
        URL uri = Main_terminal.class.getResource("file1.txt");
        String path = uri.getPath();

        byte[] data = FileIO.getFileContentBytes(path);
        BigNoLongerNatural fromFileBytes = new BigNoLongerNatural(data);



        String str = FileIO.getFileContentString(path);
        BigNoLongerNatural fromFileString = new BigNoLongerNatural(str);

        System.out.println("number read from file1.txt by byte: " + fromFileBytes);
        System.out.println("number read from file1.txt by string: " + fromFileString);

    }
}
