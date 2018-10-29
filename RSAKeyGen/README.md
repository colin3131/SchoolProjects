# CS/COE 1501 Assignment 5

## Goal:

To get hands on experience with algorithms to perform mathematical operations on large integers, using RSA as an example.

Note that the result of this project should NEVER be used for any security applications.  It is purely instructive.  Always use trusted and tested crypto libraries.

## High-level description:
You will be writing two programs.  The first will generate a 512-bit RSA keypair and store the public and private keys in files named `pubkey.rsa` and `privkey.rsa`, respectively.
The second will generate and verify digital signatures using a SHA-256 hash.  You will use Java's [MessageDigest](https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html) class to complete this project.
In order for either of these programs to work, however, you will need to complete an implementation of a class to process large integers.
