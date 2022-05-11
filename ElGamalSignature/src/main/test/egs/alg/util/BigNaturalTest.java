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
    void powTestSerial() {

        fail("pow is unimplemented!");

        BigInteger bigI;
        BigNatural bigN;
        BigNatural expN;
        int bitLength = 64;
        for(int i = 0; i < 1000; i++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNatural(bigI.toString(16));
            expN = new BigNatural(i);
            assertEquals(bigI.pow(i).toString(16), bigN.pow(expN).toString());
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

    @Test
    void compareTestSerial() {
        for(int i = 0; i < ITER_NUM; i++) {
            a = BigNatural.getRandom(100);
            b = a.shiftLeft(1);

            assertFalse(b.isOdd());

            assertNotEquals(a, b);

            assertTrue(b.gt(a));
            assertFalse(a.gt(b));

            assertTrue(a.lt(b));
            assertFalse(b.lt(a));

            assertTrue(b.geq(a));
            assertFalse(a.geq(b));

            b = a.shiftLeft(0); // Clone

            assertEquals(a, b);

            assertTrue(a.geq(b));
            assertTrue(b.geq(a));
        }
    }


    /*
     * todo
     * Fails powMod, always actual: 1
     * Also check out next test!
     */
    @Test
    void modTestSerial() {
        BigInteger bigI;
        BigNatural bigN;
        BigInteger modI;
        BigNatural modN;
        BigInteger expI;
        BigNatural expN;
        int bitLength = 64;
        for(int i = 0; i < 1000; i++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNatural(bigI.toString(16));
            modI = BigInteger.probablePrime(bitLength, rng);
            modN = new BigNatural(modI.toString(16));
            expI = BigInteger.probablePrime(bitLength, rng);
            expN = new BigNatural(expI.toString(16));

            assertEquals(bigI.mod(modI).toString(16), bigN.mod(modN).toString());
            assertEquals(bigI.modPow(expI, modI).toString(16), bigN.modPow(expN, modN).toString());
        }
    }

    /*
     * todo
     * When you make separate test for powMod, then it throws exception instead of failing assert.
     * Both assertions are the same. WHAT IS GOING ON???
     */
    @Test
    void powModTestSerial() {
        BigInteger bigI;
        BigNatural bigN;
        BigInteger modI;
        BigNatural modN;
        BigInteger expI;
        BigNatural expN;
        int bitLength = 64;
        for(int i = 0; i < 1000; i++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNatural(bigI.toString(16));
            modI = BigInteger.probablePrime(bitLength, rng);
            modN = new BigNatural(modI.toString(16));
            expI = BigInteger.probablePrime(bitLength, rng);
            expN = new BigNatural(expI.toString(16));

            assertEquals(bigI.modPow(expI, modI).toString(16), bigN.modPow(expN, modN).toString());
        }
    }

    /*
    @Test
    void test() {
        int bitLength = 64;
        BigInteger bigI = BigInteger.probablePrime(bitLength);
        bigI.mod
    }*/
    /*
    @Test
    void probablePrimeTestSerial() {
        for(int i = 0; i < ITER_NUM; i++) {
            BigNatural bigN = BigNatural.probablePrime(100);

            for(int i = new BigNatural(0), )
        }
    }*/
}