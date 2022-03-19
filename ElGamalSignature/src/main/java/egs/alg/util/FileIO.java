package egs.alg.util;

import java.io.FileInputStream;
import java.io.IOException;

public class FileIO {
    public static byte[] getFileContent(String filePath) throws IOException {
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
}
