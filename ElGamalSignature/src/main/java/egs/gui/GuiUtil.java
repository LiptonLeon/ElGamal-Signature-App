package egs.gui;

public class GuiUtil {

    public static byte[] stringToByteArray(String str) {
        char[] chars = str.toCharArray();
        byte[] ret = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ret[i] = (byte) chars[i];
        }
        return ret;
    }

}
