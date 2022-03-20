package egs.alg.util;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.Random;

public class BigNatural {
    public static BigNatural zero = new BigNatural(0);
    public static BigNatural one = new BigNatural(1);
    public static BigNatural two = new BigNatural(2);

    public byte[] mag;

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
        // idxStr is always even at this point
        while(true) {
            if(idxStr + 2 <= s.length()) {
                String byteStr = s.substring(idxStr, idxStr + 2);
                int i = Integer.parseInt(byteStr, 16);
                mag[idxMag] = (byte) (i);
                idxMag++;
                idxStr += 2;
            } else {
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

    public BigNatural(long number) {
        if(number < 0) {
            mag = new byte[0];
            return;
        }
        int byteLen = 0;
        while(number >= (1L << (byteLen * 8))) {
            byteLen++;
        }
        mag = new byte[byteLen];
        for(int i = byteLen - 1; i >= 0; i--) {
            mag[i] = (byte) (number >>> ((mag.length - i - 1) * 8));
        }
    }

    public static BigNatural getRandom(int length) {
        byte[] new_mag = new byte[length];
        Random rng = new Random();
        rng.nextBytes(new_mag);
        while(new_mag[0] == 0) { // ensure that returned number does not have leading zeros
            new_mag[0] = (byte) (rng.nextInt());
        }
        return new BigNatural(new_mag);
    }

    private static int[] first_primes_list = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29,
            31, 37, 41, 43, 47, 53, 59, 61, 67,
            71, 73, 79, 83, 89, 97, 101, 103,
            107, 109, 113, 127, 131, 137, 139,
            149, 151, 157, 163, 167, 173, 179,
            181, 191, 193, 197, 199, 211, 223,
            227, 229, 233, 239, 241, 251, 257,
            263, 269, 271, 277, 281, 283, 293,
            307, 311, 313, 317, 331, 337, 347, 349};

    // https://www.geeksforgeeks.org/how-to-generate-large-prime-numbers-for-rsa-algorithm/
    public static BigNatural probablePrime(int length) {
        return null;
    }

    public BigNatural nextProbablePrime() {
        return null;
    }

    // operations
    public BigNatural add(BigNatural val) {
        if(mag.length == 0) {
            return val;
        }
        if(val.mag.length == 0) {
            return this;
        }
        byte[] x = this.mag;
        byte[] y = val.mag;
        if(x.length < y.length) {
            byte[] tmp = x;
            x = y;
            y = tmp;
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

            if(sum >= (1 << 8)) {
                carry = true;
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

    public BigNatural subtract(BigNatural val) {
        return null;
    }

    public BigNatural multiply(BigNatural val) {
        if(mag.length == 0 || val.mag.length == 0) {
            return zero;
        }
        byte[] x = this.mag;
        byte[] y = val.mag;
        if(x.length < y.length) {
            byte[] tmp = x;
            x = y;
            y = tmp;
        }
        int xIdx = x.length - 1;
        int yIdx = y.length - 1;
        int[][] temp = new int[y.length][];
        byte[] result = new byte[x.length + y.length];
        int tIdx = 0;
        int tiIdx;
        while(yIdx >= 0) {
            int yInt = Byte.toUnsignedInt(y[yIdx]);
            temp[tIdx] = new int[x.length];
            tiIdx = 0;
            while(xIdx >= 0) {
                int xInt = Byte.toUnsignedInt(x[xIdx]);
                temp[tIdx][tiIdx] = xInt * yInt;
                tiIdx++;
                xIdx--;
            }
            tIdx++;
            xIdx = x.length - 1;
            yIdx--;
        }
        int carry = 0;
        int toResult;
        int rIdx = result.length - 1;
        for(int i = 0; i < rIdx; i++) {
            toResult = carry;
            int j = i;
            for(int k = 0; k < x.length; j--, k++) {
                if(j < y.length && j >= 0) {
                    toResult += temp[j][k];
                }
            }
            if(toResult > 0xff) {
                carry = (toResult >>> 8);
            }
            result[rIdx - i] = (byte) (toResult);
        }
        // delete leading zeros
        int idxNotZero = 0;
        while(result[idxNotZero] == 0) {
            idxNotZero++;
        }
        if(idxNotZero != 0) {
            byte[] resized = new byte[result.length - idxNotZero];
            System.arraycopy(result, idxNotZero, resized, 0, resized.length);
            return new BigNatural(resized);
        }
        return new BigNatural(result);
    }

    public BigNatural mod(BigNatural mod) {
        return null;
    }

    public BigNatural modPow(BigNatural exp, BigNatural mod) {
        return null;
    }

    public BigNatural gcd(BigNatural val) {
        return null;
    }

    public boolean equals(BigNatural val) {
        return Arrays.equals(mag, val.mag);
    }

    /**
     * gt - short for 'greater than'
     * True if this > val.
     *
     * @param val
     * @return
     */
    public boolean gt(BigNatural val) {
        return false;
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

    private String byteToStr(byte b) {
        return Integer.toString(Byte.toUnsignedInt(b), 16);
    }
}
