//Colin Spratt
//Project 5
//4-15-18

import java.io.*;
import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class RsaKeyGen{
  public static void main(String[] args){
    //Pick p and q to be random primes of an appropriate size to generate a 512-bit key
    LargeInteger p = new LargeInteger(512, new Random());
    LargeInteger q = new LargeInteger(512, new Random());
    LargeInteger e = new LargeInteger(512, new Random());

    //Calculate n as p*q
    LargeInteger n = p.multiply(q);
    LargeInteger one = new LargeInteger(new byte[]{(byte) 1});

    //Calculate φ(n) as (p-1)*(q-1)
    LargeInteger pMo = p.subtract(one);
    LargeInteger qMo = q.subtract(one);
    LargeInteger phi = qMo.multiply(pMo);

    //Choose an e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1 (e must not share a factor with φ(n))
    while(phi.compareTo(e) != 1 || phi.XGCD(e)[0].compareTo(one) != 0){
      e = new LargeInteger(512, new Random());
    }

    LargeInteger d = e.modInverse(phi);
    try{
      File pubKey = new File("pubkey.rsa");
      File privKey = new File("privkey.rsa");
      FileWriter pubFile = new FileWriter(pubKey);
      pubFile.write(e.toString() + "\n");
      pubFile.write(n.toString());
      pubFile.close();

      FileWriter privFile = new FileWriter(privKey);
      privFile.write(d.toString() + "\n");
      privFile.write(n.toString());
      privFile.close();
    }
    catch(Exception ex){
      System.out.println(ex.toString());
    }
  }
}
