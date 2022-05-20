package egs.alg;

import egs.alg.util.BigNoLongerNatural;
import egs.alg.util.FileIO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static egs.alg.util.BigNoLongerNatural.one;
import static org.junit.jupiter.api.Assertions.*;

class ElGamalTest {
    @Test
    void signVerifyTest() throws IOException {
        URL uri = ElGamal.class.getResource("file1.txt");
        String path = uri.getPath();
        byte[] data = FileIO.getFileContentBytes(path);
        ElGamal elGamal = new ElGamal();
        elGamal.generateKeys();

        System.out.printf("publiczne\np: %s\ng: %s\nh: %s\n", elGamal.p, elGamal.g, elGamal.h);
        System.out.printf("prywatne\na: %s\n", elGamal.a);

        BigNoLongerNatural[] signature = elGamal.sign(data);
        System.out.printf("podpis\nc1: %s\nc2: %s\n", signature[0], signature[1]);
        assertTrue(elGamal.verify(data, signature));

        signature[0] = signature[0].subtract(one);
        assertFalse(elGamal.verify(data, signature));

        BigNoLongerNatural[] otherSignature = new BigNoLongerNatural[2];
        otherSignature[0] = BigNoLongerNatural.probablePrime(elGamal.bitLength);
        otherSignature[1] = BigNoLongerNatural.probablePrime(elGamal.bitLength);
        assertFalse(elGamal.verify(data, otherSignature));
    }

    @Test
    void setKeysTest() throws IOException {
        URL uri = ElGamal.class.getResource("file1.txt");
        String path = uri.getPath();
        byte[] data = FileIO.getFileContentBytes(path);
        ElGamal elGamal = new ElGamal();

        elGamal.p = new BigNoLongerNatural("e7ecde6ea703e4d0844659306608e8e8a0a41ced4dfa56249c7b0df0ba33a621");
        elGamal.g = new BigNoLongerNatural("2b14e366c8de047c94f9bfd46c7eaddbd894b6c2ebe8aa6fb2fa4fea785dd16");
        elGamal.h = new BigNoLongerNatural("bfb488803a9a82169913faf83abdbf2b744747294b4a3be84f7966f35aaf1946");
        elGamal.a = new BigNoLongerNatural("56f7d1adac7054a1bbdc9c5dbe04c5345ab9431a59429ec1d9f011ced6e5a3c3");

        BigNoLongerNatural[] snt = new BigNoLongerNatural[2];
        snt[0] = new BigNoLongerNatural("83fd7e32837cdf3243b77069836c6d180558401776ab0d8003ad450ab10d2f3d");
        snt[1] = new BigNoLongerNatural("3cd8c0264e5418574e8ad900d1049f735ed20dc4173cdb3bfdbc2ed65b3a69b2");

        assertTrue(elGamal.verify(data, snt));
    }
}