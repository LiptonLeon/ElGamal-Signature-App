package egs.alg.util;

import java.util.Arrays;

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
        if((s.length() - idxStr) % 2 == 1) {
            String byteStr = s.substring(idxStr, idxStr + 1);
            byte val = (byte)Integer.parseInt(byteStr, 16);
            mag[idxMag] = (val); // why shifting causes casting from byte to int???
            idxMag++;
            idxStr++;
        }
        while(true) {
            if(idxStr + 2 <= s.length()) {
                String byteStr = s.substring(idxStr, idxStr + 2);
                int i = Integer.parseInt(byteStr, 16);
                mag[idxMag] = (byte) (i);
                idxMag++;
                idxStr += 2;
            } else {
                System.out.println("error!");
                break;
            }
        }
        if(idxMag != magLength) {
            System.out.println("something bad had happened");
        }
    }

    private BigNatural(byte[] mag) {
        this.mag = mag;
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
        System.out.println("begin");
        if(mag.length == 0) {
            return val;
        }
        if(val.mag.length == 0) {
            return this;
        }
        // x > y
        byte[] x = this.mag;
        byte[] y = val.mag;
        System.out.println("x: " + magToStr(x));
        System.out.println("y: " + magToStr(y));
        if(x.length < y.length) {
            byte[] tmp = x;
            x = y;
            y = tmp;
            System.out.println("swapped");
            System.out.println("x: " + magToStr(x));
            System.out.println("y: " + magToStr(y));
        }
        int xIdx = x.length - 1;
        int yIdx = y.length - 1;
        byte[] result = new byte[x.length];
        boolean carry = false;
        int sum;
        while(xIdx >= 0) {
            if(carry) {
                sum = 1;
                carry = false;
            } else {
                sum = 0;
            }
            sum += Byte.toUnsignedInt(x[xIdx]);
            if(yIdx >= 0) {
                sum +=  Byte.toUnsignedInt(y[yIdx]);
            }
            System.out.println("sum: " + sum);
            System.out.println("treshold: " + ((1 << 8)));

            if(sum >= (1 << 8)) {
                carry = true;
                System.out.println("carry!");
            }
            result[xIdx] = (byte) (sum & 0b11111111);

            xIdx--;
            yIdx--;
        }
        if(carry) { // rewrite if out of index
            byte[] resized = new byte[x.length + 1];
            System.arraycopy(result, 0, resized, 1, result.length);
            resized[0] = 1;
            return new BigNatural(resized);
        }

        return new BigNatural(result);
    }

    public boolean equals(BigNatural val) {
        return Arrays.equals(mag, val.mag);
    }

    public String toString() {
        return magToStr(mag);
    }

    private static String magToStr(byte[] mag) {
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < mag.length; i++) {
            int uns = Byte.toUnsignedInt(mag[i]);
            if(uns == 0) {
                ret.append("00");
            } else {
                ret.append(Integer.toString(uns, 16));
            }
        }
        return ret.toString();
    }
}
