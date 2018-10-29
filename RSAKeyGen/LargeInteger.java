//Colin Spratt
//Project 5
//4-15-18

import java.util.Random;
import java.math.BigInteger;
import java.nio.*;

public class LargeInteger {

	private final byte[] ONE = {(byte) 1};

	private byte[] val;

	/**
	 * Construct the LargeInteger from a given byte array
	 * @param b the byte array that this LargeInteger should represent
	 */
	public LargeInteger(byte[] b) {
		val = b;
	}

	/**
	 * Construct the LargeInteger by generatin a random n-bit number that is
	 * probably prime (2^-100 chance of being composite).
	 * @param n the bitlength of the requested integer
	 * @param rnd instance of java.util.Random to use in prime generation
	 */
	public LargeInteger(int n, Random rnd) {
		val = BigInteger.probablePrime(n, rnd).toByteArray();
	}

	/**
	 * Return this LargeInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other LargeInteger to sum with this
	 */
	public LargeInteger add(LargeInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		LargeInteger res_li = new LargeInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public LargeInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		LargeInteger neg_li = new LargeInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new LargeInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other LargeInteger to subtract from this
	 * @return difference of this and other
	 */
	public LargeInteger subtract(LargeInteger other) {
		LargeInteger ret = this.add(other.negate());
		//System.out.println("\n--First Length: " + ret.length());
		int length = ret.length();
		boolean zero = true;
		for(int i = 0; i < (length/2); i++){
			if(ret.getVal()[i] != ((byte)0)){
				//System.out.println("\n    There's a one");
				zero = false;
			}
			else{
				//System.out.println("\n    There's a zero");
			}
		}

		if(zero){
			//System.out.println("\nHalf is empty, so lets resize.");
			byte[] newAr = new byte[length/2];
			for(int i = (length/2), j = 0; i < length && j < (length/2); i++, j++){
				newAr[j] = ret.getVal()[i];
			}
			ret = new LargeInteger(newAr);
			ret.shiftRight(length/2);
		}
		//System.out.println("\n---End Length: " + ret.length());
		return ret;
	}

	/**
	 * Compute the product of this and other
	 * @param other LargeInteger to multiply by this
	 * @return product of this and other
	 */

	public LargeInteger multiply(LargeInteger other) {

		// YOUR CODE HERE (replace the return, too...)
		//Find the size, and replace it with the roof half size
		int size = other.length();
		//System.out.println("----- Size: " + size);
		if(size == 1 || this.length() == 1){
			//ystem.out.println("-----Performing Base Case Mult");
			ByteBuffer b = ByteBuffer.allocate(4);
			int sample = (int)val[0] * (int)other.getVal()[0];
			b.putInt(sample);
			LargeInteger result = new LargeInteger(b.array());
			return result;
		}
		if(size == 2){ size = 1; }
		else{
			size /= 2;
			size += size % 2;
		}

		//System.out.println("------ Size after split: " + size);

		//populate new half size arrays
		LargeInteger xHigh = this.shiftRight(size);
		//System.out.println("-xHigh size: " + xHigh.length());

		LargeInteger xLow = this.subtract(xHigh.shiftLeft(size));
		//System.out.println("--xLow size: " + xLow.length());

		LargeInteger yHigh = other.shiftRight(size);
		//System.out.println("---yHigh size: " + yHigh.length());

		LargeInteger yLow = other.subtract(yHigh.shiftLeft(size));
		//System.out.println("---yLow size: " + yLow.length());



		LargeInteger xLyL = xLow.multiply(yLow);
		LargeInteger xHyH = xHigh.multiply(yHigh);
		LargeInteger foil = (xLow.add(xHigh)).multiply(yLow.add(yHigh));
		//--------------------------------------------------------------------


		//Put everything together
		LargeInteger answer = xLyL.add(foil.subtract(xLyL).subtract(xHyH).shiftLeft(size)).add(xHyH.shiftLeft(2 * size));

		return answer;
	}

	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another LargeInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public LargeInteger[] XGCD(LargeInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		//Since we can't do mod or divide, need to use subtraction based GCD
		LargeInteger a = new LargeInteger(val);
		LargeInteger b = new LargeInteger(other.getVal());

		return gcd(a, b);
	 }

	 /**
	  * Compute the result of raising this to the power of y mod n
	  * @param y exponent to raise this to
	  * @param n modulus value to use
	  * @return this^y mod n
	  */
	 public LargeInteger modularExp(LargeInteger y, LargeInteger n) {
		// YOUR CODE HERE (replace the return, too...)
		LargeInteger x = new LargeInteger(val);
		LargeInteger zero = new LargeInteger(new byte[]{(byte) 0});
		LargeInteger two = new LargeInteger(new byte[]{(byte) 2});

		LargeInteger result = new LargeInteger(new byte[]{(byte) 1});


		x = x.mod(n);

		while(y.compareTo(zero) > 0){
			//If y is odd, multiply x w/ result
			if(y.mod(two).compareTo(zero) != 0)
				result = result.multiply(x).mod(n);

			y = y.divide(two);
			x = x.multiply(x).mod(n);
		}
		return result;
	 }

	 //Added shiftRight and shiftLeft to assist with the mult function
	 private LargeInteger shiftRight(int N){
		 //System.out.println("-----Size before right shift: " + val.length);
		 byte[] newNum = new byte[val.length - N];
		 for(int i = 0; i < newNum.length; i++)
			 newNum[i] = val[i];
		 //System.out.println("-----Size after right shift: " + newNum.length);
		 LargeInteger ret = new LargeInteger(newNum);
		 return ret;
	 }

	 private LargeInteger shiftLeft(int N){
		 byte[] newNum = new byte[this.length() + N];
		 for(int i = 0; i < newNum.length; i++){
			 if(i < this.length()){
				 newNum[i] = val[i];
			 }
			 else{
				 newNum[i] = (byte) 0;
			 }
		 }
		 LargeInteger ret = new LargeInteger(newNum);
		 return ret;
	 }

	 //compareTo method, returns -1 if less than, 1 if greater than, or 0 if equal to
	 public int compareTo(LargeInteger other){
		 byte[] x = this.getVal();
		 byte[] y = other.getVal();

		 if(x.length > y.length)
		 	return 1;
		 else if(x.length < y.length)
		 	return -1;
		 else{
			 for(int i = 0; i < x.length; i++){
				 if(x[i] > y[i]){
					 return 1;
				 }
				 else if(x[i] < y[i]){
					 return -1;
				 }
			 }
			 return 0;
		 }
	 }

	 //Division method for XGCD
	 public LargeInteger divide(LargeInteger b){
		 LargeInteger a = new LargeInteger(val);
		 while(a.compareTo(b) == 1){
			 a = a.subtract(b);
		 }
		 return a.add(b);
	 }

	 //Modulus method, needed for XGCD
	 public LargeInteger mod(LargeInteger b){
		 LargeInteger a = new LargeInteger(val);
		 while(a.compareTo(b) == 1){
			 a = a.subtract(b);
		 }
		 return a;
	 }

	 //Calculate modinverse
	 public LargeInteger modInverse(LargeInteger b){
		 LargeInteger a = new LargeInteger(val);
		 LargeInteger one = new LargeInteger(new byte[]{(byte) 1});
		 LargeInteger i = new LargeInteger(new byte[]{(byte) 0});

		 a = a.mod(b);
		 while(i.compareTo(b) < 0){
			if(a.multiply(i).mod(b).compareTo(one) == 0){
			 	return i;
			}
			i.add(one);
		 }
		 return i;
	 }

	 //Recursive algorithm doing the work for XGCD
	 public LargeInteger[] gcd(LargeInteger a, LargeInteger b){
		 byte[] zero = new byte[]{(byte) 0};
		 byte[] one = new byte[]{(byte) 1};
		 LargeInteger[] answer = new LargeInteger[3];

		 if(b.compareTo(new LargeInteger(zero)) == 0){
			 answer[0] = a;
			 answer[1] = new LargeInteger(one);
			 answer[2] = new LargeInteger(zero);
		 }

		 answer = gcd(b, a.mod(b));
		 LargeInteger d = answer[0];
		 LargeInteger s = answer[2];
		 LargeInteger t = answer[1].subtract(a.divide(b).multiply(answer[2]));
		 answer[0] = d;
		 answer[1] = s;
		 answer[2] = t;
		 return answer;
	 }

	 public String toString(){
		 String ret = "";
		 for(byte b: val){
			 ret += b + " ";
		 }
		 return ret;
	 }
}
