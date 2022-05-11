package egs.alg.util;

import java.math.BigInteger;
import java.util.*;

import static java.lang.Math.*;

public class BigNatural {
    public static BigNatural zero = new BigNatural(0);
    public static BigNatural one = new BigNatural(1);
    public static BigNatural two = new BigNatural(2);
    public static Random rng = new Random();

    private byte[] mag;

    // ---------- CONSTRUCTORS ---------- //

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

    public BigNatural(byte[] mag) {
        this.mag = deleteLeadingZeros(mag);
    }

    private BigNatural(LinkedList<Byte> nmag) {
        if(nmag.size() == 0) {
            mag = new byte[0];
            return;
        }
        int zeroIdx = 0;
        while(nmag.get(zeroIdx) == 0) {
            zeroIdx++;
            if(zeroIdx == nmag.size()) {
                mag = new byte[0];
                return; // one case???
            }
        }
        mag = new byte[nmag.size() - zeroIdx];

        for (int i = 0; i < mag.length; i++, zeroIdx++) {
            mag[i] = nmag.get(zeroIdx); // O(nlogn) fix!!
        }
    }

    public BigNatural(long number) {
        if(number == 0) {
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

    // ---------- RANDOM ---------- //

    public static BigNatural getRandom(int length) {
        byte[] new_mag = getRandMag(length);
        return new BigNatural(new_mag);
    }

    public static BigNatural getRandomOdd(int length) {
        byte[] new_mag = getRandMag(length);
        while(new_mag[length-1] % 2 == 0) {
            new_mag[length-1] = (byte) (rng.nextInt());
        }
        return new BigNatural(new_mag);
    }

    public static BigNatural getRandom(BigNatural bound) {
        byte[] new_mag = getRandMag(bound.mag.length);
        while(true) {
            for(int idx = 0; idx < bound.mag.length; idx++) {
                int a = toInt(new_mag[idx]), b = toInt(bound.mag[idx]);
                if(a > b) {
                    if(b != 0) {
                        new_mag[idx] = (byte) (rng.nextInt(b));
                        return new BigNatural(new_mag);
                    }
                } else if (a < b) {
                    return new BigNatural(new_mag);
                } else {
                    new_mag[idx] = 0;
                }
            }
            // wylosowano dokładnie taką samą liczbę jak n
            new_mag = getRandMag(bound.mag.length);
        }
    }

    private static byte[] getRandMag(int length) {
        byte[] new_mag = new byte[length];
        rng.nextBytes(new_mag);
        while(new_mag[0] == 0) { // ensure that returned number does not have leading zeros
            new_mag[0] = (byte) (rng.nextInt());
        }
        return new_mag;
    }

    // ---------- PRIMES ---------- //

    // https://www.geeksforgeeks.org/how-to-generate-large-prime-numbers-for-rsa-algorithm/
    public static BigNatural probablePrime(int length) {
        if(length <= 2) {
            int rand = rng.nextInt(0xffff);
            while(!isIntPrime(rand)) {
                rand = rng.nextInt(0xffff);
            }
            return new BigNatural(rand);
        }
        BigNatural p = getLowLevelPrime(length);
        while(!millerRabinPassed(p, 5)) {
            p = getLowLevelPrime(length);
        }
        return p;
    }

    // todo
    public BigNatural nextProbablePrime() {
        return null;
    }

    private static final int[] firstPrimes = {
            3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67,
            71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139,
            149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223,
            227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293,
            307, 311, 313, 317, 331, 337, 347, 349
    };

    public static BigNatural getLowLevelPrime(int length) {
        BigNatural p = null;
        boolean generated = false;
        while(!generated) {
            p = getRandomOdd(length);
            generated = true;
            for(int q : firstPrimes) {
                if(p.mod(new BigNatural(q)).equals(zero)) {
                    generated = false;
                }
            }
        }
        return p;
    }

    private static boolean isIntPrime(int x) {
        double bound = sqrt(x);
        for(int i = 2; i < bound; i++) {
            if(x % i == 0) return false;
        }
        return true;
    }

    private static boolean millerRabinPassed(BigNatural n, int iterations) {
        BigNatural d = n.subtract(one); // d = n - 1

        while (d.mod(two).isOdd())  //  while d % 2 == 0
            d = d.divide(two);      //      d /= 2

        for(int i = 0; i < iterations; i++) {
            if(!millerTest(n, d))
                return false;
        }
        return true;
    }

    // https://www.geeksforgeeks.org/how-to-generate-large-prime-numbers-for-rsa-algorithm/
    // https://www.geeksforgeeks.org/primality-test-set-3-miller-rabin/

    // po zaimplementowaniu tego nadal nie mam pojęcia jak działa

    // This function is called for all k trials.
    // It returns false if n is composite and
    // returns false if n is probably prime.
    // d is an odd number such that d*2<sup>r</sup>
    // = n-1 for some r >= 1
    private static boolean millerTest(BigNatural n, BigNatural d) {
        // a = random(2, n-2)
        BigNatural a = getRandom( n.subtract(two) ); // a = random(0, n-2)
        while(a.lt(two))                        // while a < 2
            a = getRandom( n.subtract(two) );   //     a = random(0, n-2)


        BigNatural x = a.modPow(d, n); // x = a^d % n
        BigNatural nMinOne = n.subtract(one);
        if (x.equals(one) || x.equals(nMinOne)) // if (x == 1 || x == n - 1)
            return true;

        // Keep squaring x while one of the
        // following doesn't happen
        // (i) d does not reach n-1
        // (ii) (x^2) % n is not 1
        // (iii) (x^2) % n is not n-1
        while (d.equals(nMinOne)) {
            x = x.multiply(x).mod(n);   // x = x^2 % n
            d = d.multiply(two);        // d *= 2

            if (x.equals(one)) // if (x == 1)
                return false;
            if (x.equals(nMinOne)) // if (x == n - 1)
                return true;
        }
        return false;
    }

    // ---------- MATH OPERATIONS ---------- //

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
            sum += toInt(x[xIdx]);
            if(yIdx >= 0) {
                sum += toInt(y[yIdx]);
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
            toResult = toInt(mag[i]);
            if(carry) {
                toResult -= 1;
                carry = false;
            }
            if(toResult < 0) {
                carry = true;
                toResult += 0x100;
            }
            if(iVal >= 0) {
                if(toResult < toInt(val.mag[iVal])) {
                    carry = true;
                    toResult += 0x100;
                }
                toResult -= toInt(val.mag[iVal]);
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
        int tIdx = 0;
        int tiIdx;
        // fill temp
        while(yIdx >= 0) {
            int yInt = toInt(y[yIdx]);
            temp[tIdx] = new int[x.length];
            tiIdx = 0;
            while(xIdx >= 0) {
                int xInt = toInt(x[xIdx]);
                temp[tIdx][tiIdx] = xInt * yInt;
                tiIdx++;
                xIdx--;
            }
            tIdx++;
            xIdx = x.length - 1;
            yIdx--;
        }

        // temp to result
        int carry = 0;
        byte[] result = new byte[x.length + y.length];
        int toResult;
        int rIdx = result.length - 1;
        for(int i = 0; i < result.length; i++) {
            toResult = carry;
            int j = i;
            for(int k = 0; k < x.length; j--, k++) {
                if(j < y.length && j >= 0) {
                    toResult += temp[j][k];
                }
            }
            if(toResult > 0xff) {
                carry = (toResult >>> 8);
            } else {
                carry = 0;
            }
            result[rIdx - i] = (byte) (toResult);
        }
        BigNatural t = new BigNatural(result);
        result = deleteLeadingZeros(result);
        return new BigNatural(result);
    }

    public BigNatural divide(BigNatural val) {
        return divide(val, new BigNatural(1));
    }

    public BigNatural mod(BigNatural mod) {
        if(mod.equals(one)) {
            return zero;
        }
        BigNatural reminder = new BigNatural(1);
        divide(mod, reminder);
        return reminder;
    }

    public BigNatural divide(BigNatural val, BigNatural reminder) {
        if(val.mag.length == 0) {
            throw new ArithmeticException("Division by zero!");
        }
        if(val.gt(this)) {
            reminder.mag = this.mag;
            return zero;
        }
        LinkedList<Byte> current = new LinkedList<>();
        LinkedList<Byte> result = new LinkedList<>();
        byte toResult;

        for(byte value : this.mag) {
            current.addLast(value);
            toResult = 0;
            if(current.size() >= val.mag.length) {
                BigNatural temp = new BigNatural(current);
                while(temp.geq(val)) {
                    temp = temp.subtract(val);
                    toResult += 1;
                }
                if(toResult != 0) {
                    current.clear();
                    for (int i = 0; i < temp.mag.length; i++) {
                        current.add(temp.mag[i]);
                    }
                }
            }
            result.add(toResult);
        }
        BigNatural newReminder = new BigNatural(current);
        reminder.mag = newReminder.mag;

        byte[] ret = new byte[this.mag.length];
        int retIdx = 0;
        for (Byte b: result) {
            ret[retIdx] = b;
            retIdx++;
        }
        ret = deleteLeadingZeros(ret);
        return new BigNatural(ret);

    }

    // todo
    public BigNatural pow(BigNatural val) {
        return zero;
    }

    // montgomery modular multiplication
    public BigNatural modPow(BigNatural exp, BigNatural mod) {
        BigNatural base = (this.gt(mod) ? this : this.mod(mod));
        if(mod.isOdd()) {
            return base.oddModPow(exp, mod);
        } else {
            // adopded from BigInteger
            int p = mod.getLowestSetBit();
            BigNatural m1 = mod.shiftLeft(p); // m/2^p
            BigNatural m2 = one.shiftLeft(p); // 2^p
            BigNatural base2 = (this.geq(m1) ? this.mod(m1) : this);
            BigNatural a1 = (m1.equals(one) ? zero : base2.oddModPow(exp, m1));
            BigNatural a2 = base.modPow2(exp, p);

//            BigNatural y1 = m2.modInverse(m1);
//            BigNatural y2 = m1.modInverse(m2);
//            return a1.multiply(m2).multiply(y1).add(a2.multiply(m1).multiply(y2)).mod(mod);
        }
        return one;
    }

    public BigNatural oddModPow(BigNatural exp, BigNatural mod) {
        return one;
    }

    private BigNatural modPow2(BigNatural exp, int p) {
        BigNatural result = one;
//        BigNatural baseToPow2 = this.mod2(p);
        int expOffset = 0;
        return one;
    }

    public int getLowestSetBit() {
        int ret = 0;
        for(int i = mag.length - 1; i >= 0; i--) {
            byte b = mag[i];
            for(int j = 0; j < 8; j++) {
                if(((b >>> j) & 1) == 1) {
                    return ret;
                }
                ret++;
            }
        }
        return ret; // mag == 0
    }

    public BigNatural gcd(BigNatural val) {
        if(this == zero) {
            return val;
        }
        if(val == zero) {
            return this;
        }
        BigNatural a = new BigNatural((mag));
        BigNatural b = new BigNatural(val.mag);
        BigNatural r = a.mod(b);
        while(r.gt(zero)) {
            a.mag = b.mag;
            b.mag = r.mag;
            r = a.mod(b);
        }
        return b;
    }

    public BigNatural shiftRight(int n) {
        int retLen = mag.length - floorDiv(n, 8);
        byte[] newMag = new byte[retLen];
        int mod8 = n % 8;
        int negMod8 = (8 - mod8) % 8;

        for(int i = 0; i < retLen; i++) {
            newMag[i] = (byte) (toInt(mag[i]) >> mod8);
            if(i > 0 && mod8 != 0) {
                newMag[i] ^= (byte) (toInt(mag[i - 1]) << negMod8);
            }
        }
        return new BigNatural(newMag);
    }

    public BigNatural shiftLeft(int n) {
        int retLen = mag.length + (int) ceil(n / 8.f);
        byte[] newMag = new byte[retLen];
        int mod8 = n % 8;
        int negMod8 = (8 - mod8) % 8;

        for(int i = 0; i < retLen; i++) {
            if(i < mag.length) {
                newMag[i] = (byte) (toInt(mag[i]) >> negMod8);
            }
            if(i > 0 && (i - 1) < mag.length && mod8 != 0) {
                newMag[i] ^= (byte) (toInt(mag[i - 1]) << mod8);
            }
        }
        return new BigNatural(newMag);
    }

    // od najmniej znaczącego bitu, tj OD PRAWEJ DO LEWEJ
    public boolean testBit(int n) {
        int idx = mag.length - floorDiv(n, 8) - 1;
        if(idx < 0)
            return false; // mb throw
        byte b = mag[idx];
        return ((b & (1 << (n % 8))) != 0);
    }

    private boolean testBitFromLeft(int n) {
        int leadingZeros = 0;
        while((mag[0] & (1 << 7 - leadingZeros)) == 0 && leadingZeros < 8) {
            leadingZeros++;
        }
        n += leadingZeros;
        byte b = mag[floorDiv(n, 8)];
//        System.out.println(toInt(b) + " n mod 8" + (n % 8));
        return ((b & (0b10000000L >> (n % 8))) != 0);
    }

    public int bitLength() {
        int ret = (mag.length - 1) * 8;
        for(int i = 7; i >= 0; i--) {
            if((mag[0] & (1 << i)) != 0) {
                return ret + i + 1;
            }
        }
        return ret;
    }

    // ---------- CONDITIONS ---------- //

    public boolean isOdd() {
        return this.mag[this.mag.length - 1] % 2 == 0;
    }

    public boolean equals(Object o) {
        if(o instanceof BigNatural val) {
            return Arrays.equals(mag, val.mag);
        }
        return false;
    }

    public boolean gt(BigNatural val) {
        Boolean ret = greater(val);
        return Objects.requireNonNullElse(ret, false);
    }

    public boolean geq(BigNatural val) {
        Boolean ret = greater(val);
        return Objects.requireNonNullElse(ret, true);
    }

    // if equals returns null
    private Boolean greater(BigNatural val){
        if(this.mag.length > val.mag.length) {
            return true;
        }
        if(this.mag.length < val.mag.length) {
            return false;
        }
        for(int i = 0; i < this.mag.length; i++) {
            if(toInt(this.mag[i]) > toInt(val.mag[i])) {
                return true;
            } else if(toInt(this.mag[i]) < toInt(val.mag[i])) {
                return false;
            }
        }
        return null;
    }

    public boolean lt(BigNatural val) {
        if(this.mag.length < val.mag.length) {
            return true;
        }
        if(this.mag.length > val.mag.length) {
            return false;
        }
        for(int i = 0; i < this.mag.length; i++) {
            if(toInt(this.mag[i]) < toInt(val.mag[i])) {
                return true;
            } else if(toInt(this.mag[i]) > toInt(val.mag[i])) {
                return false;
            }
        }
        return false; // equal
    }

    // ---------- DISPLAYING ---------- //

    public String toString() {
        return magToStr(mag);
    }

    private static String magToStr(byte[] mag) {
        if(mag.length == 0) {
            return "0";
        }
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < mag.length; i++) {
            int uns = toInt(mag[i]);
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
        return Integer.toString(toInt(b), 16);
    }

    // ---------- VARIOUS COMMON FUNCTIONS ---------- //

    private byte[] deleteLeadingZeros(byte[] arr) {
        int idxNotZero = 0;
        while(arr[idxNotZero] == 0) {
            idxNotZero++;
            if(idxNotZero == arr.length) {
                return new byte[0];
            }
        }
        if(idxNotZero != 0) {
            byte[] resized = new byte[arr.length - idxNotZero];
            System.arraycopy(arr, idxNotZero, resized, 0, resized.length);
            return resized;
        }
        return arr;
    }
    
    private static int toInt(byte b) {
        return Byte.toUnsignedInt(b);
    }
}
