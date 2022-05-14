package egs.alg.util;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static egs.alg.util.BigNoLongerNatural.zero;
import static org.junit.jupiter.api.Assertions.*;

class BigNoLongerNaturalTest {
    int ITER_NUM = 10000000;

    BigNoLongerNatural a;
    BigNoLongerNatural b;
    BigNoLongerNatural r = new BigNoLongerNatural(0);
    BigNoLongerNatural result;
    BigNoLongerNatural reminder;
    long toa;
    long tob;
    Random rng = new Random();

    @Test
    void constructFromStringTest() {
        String[] inputs = new String[]{
                "-abcdef",
                "7831268375192843471524981209834619284561826",
                "b1461acb2df34",
                "-121"
        };
        for(String input : inputs) {
            BigNoLongerNatural a = new BigNoLongerNatural(input);
            assertEquals(input, a.toString());
        }
    }

    @Test
    void naturalIntegerBasicOpsTest() {
        BigNoLongerNatural[] n = new BigNoLongerNatural[4];
        BigInteger[] i = new BigInteger[4];
        for(int j = 0; j < 1000; j++) {
            int bitLength = rng.nextInt(2, 256);
            i[0] = BigInteger.probablePrime(bitLength, rng);
            bitLength = rng.nextInt(2, 256);
            i[1] = BigInteger.probablePrime(bitLength, rng);
            i[2] = i[0].subtract(i[1]);
            i[3] = i[1].subtract(i[0]);
            n[0] = new BigNoLongerNatural(i[0].toString(16));
            n[1] = new BigNoLongerNatural(i[1].toString(16));
            n[2] = new BigNoLongerNatural(i[2].toString(16));
            n[3] = new BigNoLongerNatural(i[3].toString(16));

            System.out.printf("nums: ----- \ni0: %s\ni1: %s\ni2: %s\ni3: %s\n",
                    i[0].toString(16),
                    i[1].toString(16),
                    i[2].toString(16),
                    i[3].toString(16));

            String si, sn;
            for(int i1 = 0; i1 < 4; i1++) {
                for(int i2 = 0; i2 < 4; i2++) {
                    System.out.printf("%d, %d\n", i1, i2);
                    // equals
                    si = i[i2].toString(16);
                    sn = n[i2].toString();
                    assertEquals(si, sn);
                    // add
                    assertEquals(i[i1].add(i[i2]).toString(16), n[i1].add(n[i2]).toString());
                    // subtract
                    assertEquals(i[i1].subtract(i[i2]).toString(16), n[i1].subtract(n[i2]).toString());
                    // multiply
                    assertEquals(i[i1].multiply(i[i2]).toString(16), n[i1].multiply(n[i2]).toString());
                    // divide
                    assertEquals(i[i1].divide(i[i2]).toString(16), n[i1].divide(n[i2]).toString());
                    if(n[i2].geq(zero)) { // mod >= 0
                        // mod
                        assertEquals(i[i1].mod(i[i2]).toString(16), n[i1].mod(n[i2]).toString());
                        // gcd
                        assertEquals(i[i1].gcd(i[i2]).toString(16), n[i1].gcd(n[i2]).toString());
                    }
                }
            }
            // modInverse
            assertEquals(i[0].modInverse(i[1]).toString(16), n[0].modInverse(n[1]).toString());
            assertEquals(i[1].modInverse(i[0]).toString(16), n[1].modInverse(n[0]).toString());
        }
    }

    @Test
    void singleNaturalInteger() {
        BigInteger bigI1 = new BigInteger("107", 16);  // 263
        BigInteger bigI2 = new BigInteger( "10f", 16); // 271
        BigInteger bigI3 = new BigInteger( "1f3", 16); // 499
        BigNoLongerNatural bigN1 = new BigNoLongerNatural(bigI1.toString(16));
        BigNoLongerNatural bigN2 = new BigNoLongerNatural(bigI2.toString(16));
        BigNoLongerNatural bigN3 = new BigNoLongerNatural(bigI3.toString(16));

//        assertEquals(bigI1.subtract(bigI2).toString(16), .subtract(bigN2).toString());
        assertEquals(bigI1.modPow(bigI2, bigI3).toString(16), bigN1.modPow(bigN2, bigN3).toString());
    }

    @Test
    void naturalIntegerModPowTest() {
        BigNoLongerNatural[] n = new BigNoLongerNatural[3];
        BigInteger[] i = new BigInteger[3];
        for(int j = 0; j < 1000; j++) {
            int bitLength = rng.nextInt(2, 256);
            i[0] = BigInteger.probablePrime(bitLength, rng);
            bitLength = rng.nextInt(2, 256);
            i[1] = BigInteger.probablePrime(bitLength, rng);
            bitLength = rng.nextInt(2, 256);
            i[2] = BigInteger.probablePrime(bitLength, rng);
            n[0] = new BigNoLongerNatural(i[0].toString(16));
            n[1] = new BigNoLongerNatural(i[1].toString(16));
            n[2] = new BigNoLongerNatural(i[2].toString(16));

            System.out.printf("nums: ----- \ni0: %s\ni1: %s\ni2: %s\n",
                    i[0].toString(16),
                    i[1].toString(16),
                    i[2].toString(16));

            for(int i1 = 0; i1 < 3; i1++) {
                for(int i2 = 0; i2 < 3; i2++) {
                    for(int i3 = 0; i3 < 3; i3++) {
                        System.out.printf("%d %d %d\n", i1, i2, i3);
                        // modPow
                        assertEquals(i[i1].modPow(i[i2], i[i3]).toString(16),
                                n[i1].modPow(n[i2], n[i3]).toString());
                    }
                }
            }
        }
    }


    @Test
    void powTestSerial() {

        fail("pow is unimplemented!");

        BigInteger bigI;
        BigNoLongerNatural bigN;
        BigNoLongerNatural expN;
        int bitLength = 64;
        for(int i = 0; i < 1000; i++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNoLongerNatural(bigI.toString(16));
            expN = new BigNoLongerNatural(i);
            assertEquals(bigI.pow(i).toString(16), bigN.pow(expN).toString());
        }
    }

    @Test
    void naturalIntegerBinaryOpsTest() {
        BigNoLongerNatural bigN;
        BigInteger bigI;
        int bitLength = rng.nextInt(256);
        for(int i = 0; i < 1000; i ++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNoLongerNatural(bigI.toString(16));
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
//                System.out.println("bit: " + j);
                assertEquals(bigI.testBit(j), bigN.testBit(j));
            }
        }
    }



    @Test
    void singleBitTest() {
        BigInteger bigI = new BigInteger("87", 16);
        BigNoLongerNatural bigN = new BigNoLongerNatural(bigI.toString(16));

        System.out.println(bigI.toString(2));
        System.out.println(bigN.isOdd());

        assertEquals(bigI.bitLength(), bigN.bitLength());
        for(int j = 0; j < 57; j++) {
            System.out.println("bit: " + j);
            assertEquals(bigI.testBit(j), bigN.testBit(j));
        }
    }



    @Test
    void compareTestSerial() {
        for(int i = 0; i < ITER_NUM; i++) {
            a = BigNoLongerNatural.getRandom(100);
            b = a.shiftLeft(1);

            assertFalse(b.isOdd());

            assertNotEquals(a, b);

            assertTrue(b.gt(a));
            assertFalse(a.gt(b));

//            assertTrue(a.lt(b));
//            assertFalse(b.lt(a));

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
        BigNoLongerNatural bigN;
        BigInteger modI;
        BigNoLongerNatural modN;
        BigInteger expI;
        BigNoLongerNatural expN;
        int bitLength = 64;
        for(int i = 0; i < 1000; i++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNoLongerNatural(bigI.toString(16));
            modI = BigInteger.probablePrime(bitLength, rng);
            modN = new BigNoLongerNatural(modI.toString(16));
            expI = BigInteger.probablePrime(bitLength, rng);
            expN = new BigNoLongerNatural(expI.toString(16));

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
        BigNoLongerNatural bigN;
        BigInteger modI;
        BigNoLongerNatural modN;
        BigInteger expI;
        BigNoLongerNatural expN;
        int bitLength = 64;
        for(int i = 0; i < 1000; i++) {
            bigI = BigInteger.probablePrime(bitLength, rng);
            bigN = new BigNoLongerNatural(bigI.toString(16));
            modI = BigInteger.probablePrime(bitLength, rng);
            modN = new BigNoLongerNatural(modI.toString(16));
            expI = BigInteger.probablePrime(bitLength, rng);
            expN = new BigNoLongerNatural(expI.toString(16));

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