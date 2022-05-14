package egs.alg;

//https://stackoverflow.com/questions/47025054/el-gamal-digital-signature-construction-inexplicably-failing
//https://www.geeksforgeeks.org/elgamal-encryption-algorithm/



import egs.alg.util.BigNoLongerNatural;

public class ElGamal {
    void sign(byte[] data, BigNoLongerNatural secKey, BigNoLongerNatural pubKey) {
        /*
        // opis algorytmu:
            https://www.brainkart.com/article/The-El-Gamal-and-Digital-Signature-Algorithms_9757/
            http://web4.uwindsor.ca/users/e/erfani/main.nsf/0/6c0f05cb51a0e3de85256db900529639/$FILE/El%20Gamal%20and%20Digital%20Sign.pdf
        generowanie kluczy:
        p - liczba pierwsza
        a, x - liczby naturalne spełniające warunki:
            a < p, x < p
        obliczyć:
            y = a^x % p
        "The prime p should be chosen so that (p - 1) has a large prime factor, q" - cokolwiek to znaczy
        klucz prywatny : x
        klucz publiczny: y
        publiczne są też parametry p i a (ale jak???)

        podpisywanie:
        wybrać przypadkowy int k taki że:
        0 < k < p - 1
        oraz gdc(k, p-1) = 1 (są względnie pierwsze)
        obliczyć:
        r = a^k mod p
        s = ??
        profit
         */
    }
}
