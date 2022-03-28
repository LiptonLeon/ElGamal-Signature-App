package egs.alg.util;

import eu.hansolo.tilesfx.Tile;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BigNaturalTest {
    @Test
    void addTestSerial() {
        BigNatural a;
        BigNatural b;
        BigNatural result;
        long toa;
        long tob;
        Random rng = new Random();
        long bound = 0x0fffffff;
        for(int i = 0; i < 1000; i++) {
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
        BigNatural a;
        BigNatural b;
        BigNatural result;
        long toa;
        long tob;
        Random rng = new Random();
        long bound = 0x0fffffff;
        for(int i = 0; i < 1000; i++) {
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
        BigNatural a;
        BigNatural b;
        BigNatural result;
        long toa;
        long tob;
        Random rng = new Random();
        long bound = 0x0000ffff;
        for(int i = 0; i < 1000; i++) {
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
        BigNatural a;
        BigNatural b;
        BigNatural r = new BigNatural(0);
        BigNatural result;
        BigNatural reminder;
        long toa;
        long tob;
        Random rng = new Random();
        for(int i = 0; i < 1000; i++) {
            toa = rng.nextLong(0x0fffffff);
            a = new BigNatural(toa);
            tob = rng.nextLong(0x000fffff);
            b = new BigNatural(tob);
            result = new BigNatural(toa / tob);
            reminder = new BigNatural(toa % tob);
            System.out.printf("a: %s, b: %s, a/b = %s\n", a, b, a.divide(b));
            assertEquals(result, a.divide(b, r));
            assertEquals(reminder, r);
        }
    }
}