package egs.alg.util;

import eu.hansolo.tilesfx.Tile;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BigNaturalTest {
    int ITER_NUM = 10000000;

    BigNatural a;
    BigNatural b;
    BigNatural r = new BigNatural(0);
    BigNatural result;
    BigNatural reminder;
    long toa;
    long tob;
    Random rng = new Random();

    @Test
    void addTestSerial() {
        long bound = 0x0fffffff;
        for(int i = 0; i < ITER_NUM; i++) {
            toa = rng.nextLong(bound);
            a = new BigNatural(toa);
            tob = rng.nextLong(bound);
            b = new BigNatural(tob);
            result = new BigNatural(toa+tob);
            assertEquals(result, a.add(b));
        }
    }

    @Test
    void subTestSerial() {
        long bound = 0x0fffffff;
        for(int i = 0; i < ITER_NUM; i++) {
            toa = rng.nextLong(bound);
            a = new BigNatural(toa);
            tob = rng.nextLong(bound);
            b = new BigNatural(tob);
            result = new BigNatural(toa-tob);
            assertEquals(result, a.subtract(b));
        }
    }

    @Test
    void mulTestSerial() {
        long bound = 0x0000ffff;
        for(int i = 0; i < ITER_NUM; i++) {
            toa = rng.nextLong(bound);
            a = new BigNatural(toa);
            tob = rng.nextLong(bound);
            b = new BigNatural(tob);
            result = new BigNatural(toa * tob);
            assertEquals(result, a.multiply(b));
        }
    }

    @Test
    void divTestSerial() {
        for(int i = 0; i < ITER_NUM; i++) {
            toa = rng.nextLong(0x0fffffff);
            a = new BigNatural(toa);
            tob = rng.nextLong(0x000fffff);
            b = new BigNatural(tob);
            if(tob == 0) {
                assertThrows(ArithmeticException.class, () -> {
                    a.divide(b, r);
                });
                continue;
            }
            result = new BigNatural(toa / tob);
            reminder = new BigNatural(toa % tob);
//            System.out.printf("a: %s, b: %s, a/b = %s\n", a, b, a.divide(b));
            assertEquals(result, a.divide(b, r));
            assertEquals(reminder, r);
        }
    }

    @Test
    void naturalIntegerBinaryOpsTest() {
        BigNatural bigN;
        BigInteger bigI;
        int bitLength = rng.nextInt(256);
        for(int i = 0; i < 1000; i ++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNatural(bigI.toString(16));
            int p = rng.nextInt(bitLength/2);
            assertEquals(bigI.toString(16), bigN.toString());

            System.out.println(bigI.toString(16) + " p: " + p);
//            System.out.println(bigI.toString(2));

            assertEquals(bigI.getLowestSetBit(), bigN.getLowestSetBit());
            assertEquals(bigI.shiftRight(p).toString(16), bigN.shiftRight(p).toString());
            assertEquals(bigI.shiftLeft(p).toString(16), bigN.shiftLeft(p).toString());
            assertEquals(bigI.bitLength(), bigN.bitLength());
            assertEquals(bigI.getLowestSetBit(), bigN.getLowestSetBit());
            for(int j = 0; j < bitLength; j++) {
                System.out.println("bit: " + j);
                assertEquals(bigI.testBit(j), bigN.testBit(j));
            }
        }
    }

    @Test
    void singleBitTest() {
        BigInteger bigI = new BigInteger("1bbbfaa758e49b1", 16);
        BigNatural bigN = new BigNatural(bigI.toString(16));

        System.out.println(bigI.toString(2));

        assertEquals(bigI.bitLength(), bigN.bitLength());
        for(int j = 0; j < 57; j++) {
            System.out.println("bit: " + j);
            assertEquals(bigI.testBit(j), bigN.testBit(j));
        }
    }

    @Test
    void singleNaturalInteger() {
        BigInteger bigI = new BigInteger("be83fa33", 16);
        BigNatural bigN = new BigNatural(bigI.toString(16));
        int p = 15;
        assertEquals(bigI.shiftRight(p).toString(16), bigN.shiftRight(p).toString());
        assertEquals(bigI.shiftLeft(p).toString(16), bigN.shiftLeft(p).toString());


    }
}