package egs.alg.util;

import java.util.*;

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

    private BigNatural(Byte[] nmag) {
        mag = new byte[nmag.length];
        System.arraycopy(nmag, 0, mag, 0, nmag.length);
    }

    private BigNatural(LinkedList<Byte> nmag) {
        int zeroIdx = 0;
        while(nmag.get(zeroIdx) == 0) {
            zeroIdx++;
        }
        mag = new byte[nmag.size() - zeroIdx];

        for (int i = 0; i < mag.length; i++, zeroIdx++) {
            mag[i] = nmag.get(zeroIdx); // O(nlogn) fix!!
        }
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
        // x > y
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
                sum += Byte.toUnsignedInt(y[yIdx]);
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
        if(val.gt(this)) {
            return zero;
        }
        int i = this.mag.length - 1;
        int iVal = val.mag.length - 1;
        byte[] result = new byte[this.mag.length];
        boolean carry = false;
        int toResult;
        while(i >= 0) {
            toResult = Byte.toUnsignedInt(mag[i]);
            if(carry) {
                toResult -= 1;
                carry = false;
            }
            if(toResult < 0) {
                carry = true;
                toResult += 0x100;
            }
            if(iVal >= 0) {
                if(toResult < Byte.toUnsignedInt(val.mag[iVal])) {
                    carry = true;
                    toResult += 0x100;
                }
                toResult -= Byte.toUnsignedInt(val.mag[iVal]);
                iVal--;
            }
            result[i] = (byte) (toResult);
            i--;
        }
        result = deleteLeadingZeros(result);
        return new BigNatural(result);
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
        result = deleteLeadingZeros(result);
        return new BigNatural(result);
    }

    public BigNatural divide(BigNatural val) {
        return divide(val, new BigNatural(1));
    }

    /**
     * Returns result of this // val and puts remaining value in reminder
     * Caution: very cursed
     *
     * @param val
     * @param reminder
     * @return
     */
    public BigNatural divide(BigNatural val, BigNatural reminder) {
        if(val.mag.length == 0) {
            throw new ArithmeticException("Division by zero!");
        }
        if(val.gt(this)) {
            reminder.mag = this.mag;
            return zero;
        }
        LinkedList<Byte> current = new LinkedList<>();
        LinkedList<BigNatural> result = new LinkedList<>();

        for(byte value : this.mag) {
            current.addLast(value);
            if(current.size() >= val.mag.length) {
                BigNatural temp = new BigNatural(current);
                BigNatural toResult = new BigNatural(0);
                while(temp.gt(val)) {
                    temp = temp.subtract(val);
                    toResult = toResult.add(one); // idk if there's a better way
                }
                if(!toResult.equals(zero)) {
                    current.clear();
                    for (int i = 0; i < temp.mag.length; i++) {
                        current.add(temp.mag[i]);
                    }
                }
                result.add(toResult);
            }
        }
        BigNatural newReminder = new BigNatural(current);
        reminder.mag = newReminder.mag;

        byte[] ret = new byte[this.mag.length];
        int retIdx = 0;
        for (BigNatural b: result) {
            if (b != zero) {
                for (byte bt: b.mag) {
                    ret[retIdx] = bt;
                    retIdx++;
                }
            }
        }
        byte[] retRightSize = new byte[retIdx];
        System.arraycopy(ret, 0, retRightSize, 0, retIdx);
        return new BigNatural(retRightSize);
    }

    public BigNatural mod(BigNatural mod) {
        if(mod.equals(one)) {
            return one;
        }
        BigNatural reminder = new BigNatural(1);
        divide(mod, reminder);
        return reminder;
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
        if(this.mag.length > val.mag.length) {
            return true;
        }
        if(this.mag.length < val.mag.length) {
            return false;
        }
        for(int i = 0; i < this.mag.length; i++) {
            if(Byte.toUnsignedInt(this.mag[i]) > Byte.toUnsignedInt(val.mag[i])) {
                return true;
            } else if(Byte.toUnsignedInt(this.mag[i]) < Byte.toUnsignedInt(val.mag[i])) {
                return false;
            }
        }
        return false; // equal
    }

    public String toString() {
        return magToStr(mag);
    }

    private static String magToStr(byte[] mag) {
        if(mag.length == 0) {
            return "0";
        }
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < mag.length; i++) {
            int uns = Byte.toUnsignedInt(mag[i]);
            if(uns == 0) {
                ret.append("00");
            } else if(i != 0 && uns < 0x10) {
                ret.append("0");
                ret.append(Integer.toString(uns, 16));
            } else {
                ret.append(Integer.toString(uns, 16));
            }
        }
        return ret.toString();
    }

    private String byteToStr(byte b) {
        return Integer.toString(Byte.toUnsignedInt(b), 16);
    }

    private byte[] deleteLeadingZeros(byte[] arr) {
        int idxNotZero = 0;
        while(arr[idxNotZero] == 0) {
            idxNotZero++;
        }
        if(idxNotZero != 0) {
            byte[] resized = new byte[arr.length - idxNotZero];
            System.arraycopy(arr, idxNotZero, resized, 0, resized.length);
            return resized;
        }
        return arr;
    }
}
