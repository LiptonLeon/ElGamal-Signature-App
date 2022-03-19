package egs.alg.util;

public class BigNatural {
    public static BigNatural zero = new BigNatural(0);
    public static BigNatural one = new BigNatural(1);
    public static BigNatural two = new BigNatural(2);

    public byte[] mag;

    // some constructors

    /**
     * Translates string in hex to BigNatural.
     * xxx Podjebane w większości z BigInteger, spójż sobie tam
     *
     * @param s
     */
    public BigNatural(String s) {
        int idxStr = 0;
        // Skip leading zeros and compute number of digits in magnitude
        while (idxStr < s.length() && Character.digit(s.charAt(idxStr), 16) == 0) {
            idxStr++;
        }
        if(idxStr == s.length()) { // given string is a zero
            mag = new byte[0];
            return;
        }
        // cursor = number of zeros
        int magLength = (int)Math.ceil((s.length() - idxStr) / 2.0f); // two chars on each byte
        mag = new byte[magLength];
        int idxMag = 0;
        while(true) {
            if(idxStr + 2 <= s.length()) {
                String byteStr = s.substring(idxStr, idxStr + 2);
                int i = Integer.parseInt(byteStr, 16);
                mag[idxMag] = (byte) (i);
                idxMag++;
                idxStr += 2;
            } else if(idxStr + 1 == s.length()){
                String byteStr = s.substring(idxStr, idxStr + 1);
                byte val = (byte)Integer.parseInt(byteStr, 16);
                mag[idxMag] = (val); // why shifting causes casting from byte to int???
                idxMag++;
                break;
            } else {
                break;
            }
        }
        if(idxMag != magLength) {
            System.out.println("something bad had happened");
        }
    }

    private BigNatural(int number) {
        if(number == 0) {
            mag = new byte[0];
        }
        if(number < (1 << 8)) {
            mag = new byte[1];
            mag[0] = (byte) (number);
        } else {
            System.out.println("out of range, must be < 255!\n Implement yourself if you need this");
            mag = new byte[0];
        }

    }

    public BigNatural modPow(BigNatural exp, BigNatural mod) {
        return null;
    }

    public BigNatural gcd(BigNatural val) {
        return null;
    }

    public BigNatural probablePrime(int length) {
        return null;
    }

    public BigNatural nextProbablePrime() {
        return null;
    }

    public BigNatural multiply(BigNatural val) {
        return null;
    }

    public BigNatural add(BigNatural val) {
        return null;
    }

    public boolean equals(BigNatural val) {
        return false;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < mag.length; i++) {
            ret.append(Integer.toString(Byte.toUnsignedInt(mag[i]), 16));
        }
        return ret.toString();
    }

    private char getChar(byte b) {
        if(b < 10) {
            return (char) (48 + b);
        } else if(b < 17) {
            return (char) (97 + b - 10);
        } else {
            return '!';
        }
    }
}
