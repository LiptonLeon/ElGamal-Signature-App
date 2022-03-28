package egs.alg.util;

import eu.hansolo.tilesfx.Tile;
import org.junit.jupiter.api.Test;

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
}