package egs.alg;

import egs.alg.util.BigNoLongerNatural;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static egs.alg.util.BigNoLongerNatural.one;
import static egs.alg.util.BigNoLongerNatural.zero;

public class ElGamal {

    public int bitLength;
    public BigNoLongerNatural p, g, h; // public
    public BigNoLongerNatural a; // private
    private BigNoLongerNatural pMinOne;

    MessageDigest md;

    ElGamal() {
        bitLength = 256;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // ignore
        }
        generateKeys(bitLength);
    }

    // getRandom(bound) zwraca liczbę z przedziału 1 <= r < bound

    //https://cryptography.fandom.com/wiki/ElGamal_signature_scheme
    //https://stackoverflow.com/questions/47025054/el-gamal-digital-signature-construction-inexplicably-failing

    public BigNoLongerNatural[] sign(byte[] data) {
        md.update(data);
        BigNoLongerNatural digest = new BigNoLongerNatural(md.digest());
        BigNoLongerNatural r = BigNoLongerNatural.getRandom(p); // 1 <= r <= p - 1
        while(!r.gcd(pMinOne).equals(one)) {
            r = BigNoLongerNatural.getRandom(p);
        }
        BigNoLongerNatural rInv = r.modInverse(pMinOne);
        BigNoLongerNatural[] ret = new BigNoLongerNatural[2];

        ret[0] = g.modPow(r, p);
        ret[1] = digest.subtract(a.multiply(ret[0])).multiply(rInv).mod(pMinOne);
        return ret;
    }

    boolean verify(byte[] data, BigNoLongerNatural[] key) {
        md.update(data);
        BigNoLongerNatural digest = new BigNoLongerNatural(md.digest());
        BigNoLongerNatural res1 = g.modPow(digest, p);
        BigNoLongerNatural res2 = h.modPow(key[0], p).multiply(key[0].modPow(key[1], p)).mod(p);
        return res1.equals(res2);
    }

    void generateKeys() {
        generateKeys(64);
    }

    void generateKeys(int bitLength) {

        p = BigNoLongerNatural.probablePrime(bitLength);
        pMinOne = p.subtract(one);
        g = BigNoLongerNatural.getRandom(pMinOne); // 1 < g < p - 1
        while(g.equals(one)) {
            g = BigNoLongerNatural.getRandom(pMinOne);
        }

        a = BigNoLongerNatural.getRandom(pMinOne); // 1 < a < p - 1
        while(a.equals(one)) {
            a = BigNoLongerNatural.getRandom(pMinOne);
        }
        h = g.modPow(a, p); // h = g^a mod p
    }
}
