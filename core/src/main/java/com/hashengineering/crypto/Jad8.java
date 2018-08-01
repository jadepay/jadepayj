package com.hashengineering.crypto;

import org.bitcoinj.core.Sha256Hash;

import fr.cryptohash.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hash Engineering on 4/24/14 for the Jad8 algorithm
 */
public class Jad8 {

    private static final Logger log = LoggerFactory.getLogger(Jad8.class);
    private static boolean native_library_loaded = false;

    static {

        try {
            log.info("Loading jad8 native library...");
            System.loadLibrary("jad8");
            native_library_loaded = true;
            log.info("Loaded jad8 successfully.");
        }
        catch(UnsatisfiedLinkError x)
        {
            log.info("Loading jad8 failed: " + x.getMessage());
        }
        catch(Exception e)
        {
            native_library_loaded = false;
            log.info("Loading jad8 failed: " + e.getMessage());
        }
    }

    public static byte[] jad8Digest(byte[] input, int offset, int length)
    {
        byte [] buf = new byte[length];
        for(int i = 0; i < length; ++i)
        {
            buf[i] = input[offset + i];
        }
        return jad8Digest(buf);
    }

    public static byte[] jad8Digest(byte[] input) {
        //long start = System.currentTimeMillis();
        try {
            return native_library_loaded ? jad8_native(input) : jad8(input);
            /*long start = System.currentTimeMillis();
            byte [] result = jad8_native(input);
            long end1 = System.currentTimeMillis();
            byte [] result2 = jad8(input);
            long end2 = System.currentTimeMillis();
            log.info("jad8: native {} / java {}", end1-start, end2-end1);
            return result;*/
        } catch (Exception e) {
            return null;
        }
        finally {
            //long time = System.currentTimeMillis()-start;
            //log.info("Jad8 Hash time: {} ms per block", time);
        }
    }

    static native byte [] jad8_native(byte [] input);


    static byte [] jad8(byte header[])
    {
        //Initialize
        Sha512Hash[] hash = new Sha512Hash[8];

        //Run the chain of algorithms
        Skein512 skein = new Skein512();
        hash[0] = new Sha512Hash(skein.digest(header));

        JH512 jh = new JH512();
        hash[1] = new Sha512Hash(jh.digest(hash[0].getBytes()));

        Keccak512 keccak = new Keccak512();
        hash[2] = new Sha512Hash(keccak.digest(hash[1].getBytes()));

        BMW512 bmw = new BMW512();
        hash[3] = new Sha512Hash(bmw.digest(hash[2].getBytes()));

        Luffa512 luffa = new Luffa512();
        hash[4] = new Sha512Hash(luffa.digest(hash[3].getBytes()));

        ECHO512 echo = new ECHO512();
        hash[5] = new Sha512Hash(echo.digest(hash[4].getBytes()));

        SIMD512 simd = new SIMD512();
        hash[6] = new Sha512Hash(simd.digest(hash[5].getBytes()));

        SHAvite512 shavite = new SHAvite512();
        hash[7] = new Sha512Hash(shavite.digest(hash[6].getBytes()));

        return hash[7].trim256().getBytes();
    }
}
