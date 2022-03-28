package egs.alg.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIO {
    public static byte[] getFileContentBytes(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        int toRead = fis.available();
        byte[] dane = new byte[toRead];
        int read = fis.read(dane);
        fis.close();
        if(read != toRead) {
            throw new IOException("Read incorrect number of bytes from file");
        }
        return dane;
    }

    public static String getFileContentString(String filePath) throws IOException {
        if(filePath.charAt(0) == '/') { // idk why that happens
            filePath = filePath.substring(1);
        }
        Path fileName = Path.of(filePath);
        return Files.readString(fileName);
    }
}
