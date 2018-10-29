//Colin Spratt
//Project 5
//4-15-18

import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RsaSign
{
	public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, IOException
	{
		if(args.length != 2)
		{
            System.out.println("Please use appropriate flags.");
            return;
        }
        String flag = args[0];
        String filename = args[1];

        if(!(flag.equals("s") || flag.equals("v")))
        {
            System.out.println("Please use s or v for flags");
            return;
        }

        if(flag.equals("s")) {
            sign(filename);
        }
        else if(flag.equals("v"))
        {
            verify(filename);
        }
        else
        {
            System.out.println("Something went wrong.");
        }
	}
    public static void sign(String file) throws FileNotFoundException, NoSuchAlgorithmException, IOException
    {
        System.out.println("Signing file " + file);
        File fileIn = new File(file);
        Scanner scanIn = new Scanner(fileIn);
        ArrayList<String> fileLines = new ArrayList<String>();
        String fileString = "";

        while(scanIn.hasNext())
        {
            String stringLine = scanIn.nextLine();
            fileLines.add(stringLine);
            fileString += stringLine;
        }

        MessageDigest m = MessageDigest.getInstance("SHA-256");

        LargeInteger hash = new LargeInteger(m.digest(fileString.getBytes()));

        try
        {
            File priv = new File("privkey.rsa");
            scanIn = new Scanner(priv);
            String bArray1 = scanIn.nextLine();
            String bArray2 = scanIn.nextLine();
            scanIn.close();

            Scanner sc = new Scanner(bArray1);
            Scanner sc2 = new Scanner(bArray2);
            byte[] val1 = new byte[65];
            byte[] val2 = new byte[65];
            int i = 0;
            while(sc.hasNextByte()){
              val1[i++] = sc.nextByte();
            }
            i = 0;
            while(sc2.hasNextByte()){
              val2[i++] = sc2.nextByte();
            }

            LargeInteger d = new LargeInteger(val1);
            LargeInteger n = new LargeInteger(val2);

            LargeInteger decrypt = hash.modularExp(d, n);

            FileWriter toSign = new FileWriter(file + ".sig");
            toSign.write(decrypt.toString() + "\n");
            for (String line : fileLines)
            {
                toSign.write(line + "\n");
            }
            toSign.close();
            System.out.println("File signature saved to " + file + ".sig" );
        }
        catch (FileNotFoundException e)
        {
            System.out.println("privkey.rsa not found.");
            return;
        }
    }

    public static void verify(String file) throws FileNotFoundException, NoSuchAlgorithmException
    {
        System.out.println("Verifying file " + file);

        if(!file.endsWith(".sig"))
        {
            System.out.println("The file to verify must have a .sig extension.");
            return;
        }

        File fileIn = new File(file);
        Scanner scanIn = new Scanner(fileIn);
        ArrayList<String> fileLines = new ArrayList<String>();
        String dArr = scanIn.nextLine();
        String fileString = "";

        //ArrayList to Byte array to byte array cause wrapper classes are weird
        ArrayList<Byte> newArr = new ArrayList<Byte>();
        while(scanIn.hasNextByte()){
          newArr.add(scanIn.nextByte());
        }
        Byte[] wrapAr = new Byte[newArr.size()];
        wrapAr = newArr.toArray(wrapAr);
        byte[] finalAr = new byte[wrapAr.length];
        int j = 0;
        for(Byte b: wrapAr)
          finalAr[j++] = b.byteValue();
        LargeInteger decrypted = new LargeInteger(finalAr);


        while(scanIn.hasNext())
        {
            String str = scanIn.nextLine();
            fileLines.add(str);
            fileString += str;
        }

        MessageDigest m = MessageDigest.getInstance("SHA-256");

        LargeInteger hash = new LargeInteger(m.digest(fileString.getBytes()));

        try
        {
            File pub = new File("pubkey.rsa");
            Scanner scan = new Scanner(pub);
            String s1 = scan.nextLine();
            String s2 = scan.nextLine();
            scan.close();
            Scanner sc1 = new Scanner(s1);
            Scanner sc2 = new Scanner(s2);
            byte[] ar1 = new byte[65];
            byte[] ar2 = new byte[65];
            int k = 0;
            while(sc1.hasNextByte()){
              ar1[k++] = sc1.nextByte();
            }
            k = 0;
            while(sc2.hasNextByte()){
              ar2[k++] = sc2.nextByte();
            }
            LargeInteger e = new LargeInteger(ar1);
            LargeInteger n = new LargeInteger(ar2);

            LargeInteger encrypted = decrypted.modularExp(e, n);

            if(hash.compareTo(encrypted) == 0)
            {
                System.out.println("Signature Valid");
            }
            else
            {
                System.out.println("Signature Invalid");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("pubkey.rsa not found.");
            return;
        }


    }
}
