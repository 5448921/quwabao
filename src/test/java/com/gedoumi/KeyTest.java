package com.gedoumi;

import com.gedoumi.util.CipherUtils;
import com.gedoumi.util.NumberUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tools.ant.taskdefs.Classloader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * Created by nelson on 11/2/17.
 */

public class KeyTest {

    static Logger logger = LoggerFactory.getLogger(KeyTest.class);
    /**
     * 获取公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey() throws Exception {
        File f = ResourceUtils.getFile("classpath:pub");
        byte[] fileBytes = FileUtils.readFileToByteArray(f);
//        File f = new File(filename);
//        FileInputStream fis = new FileInputStream(f);
//        DataInputStream dis = new DataInputStream(fis);
//        byte[] keyBytes = new byte[(int)f.length()];
//        dis.readFully(keyBytes);
//        dis.close();
        byte[] keyBytes = Base64.getDecoder().decode(fileBytes);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }


    /**
     * 获取私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey()throws Exception {
        File f = ResourceUtils.getFile("classpath:pri");
        byte[] fileBytes = FileUtils.readFileToByteArray(f);
//        File f = new File(filename);
//        FileInputStream fis = new FileInputStream(f);
//        DataInputStream dis = new DataInputStream(fis);
//        byte[] keyBytes = new byte[(int)f.length()];
//        dis.readFully(keyBytes);
//        dis.close();
        byte[] keyBytes = Base64.getDecoder().decode(fileBytes);
        PKCS8EncodedKeySpec spec =new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Test
    public void testKey() {
//        geration();

        String input = "{\"phone\":18011112222,\"seer\":100}";
        RSAPublicKey pubKey;
        RSAPrivateKey privKey;
        byte[] cipherText;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            pubKey = (RSAPublicKey) getPublicKey();
            privKey = (RSAPrivateKey) getPrivateKey();

            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            cipherText = cipher.doFinal(input.getBytes());
            //加密后的东西
            logger.info("==============");
//            logger.info("cipher: {}" , new String(cipherText));
            //传输过程中，使用的encodeString
            String ecodeString = new String(Base64.getEncoder().encode(cipherText));
            logger.info("cipherText: {}" ,cipherText);
            logger.info("ecodeString: {}" ,ecodeString);
            //开始解密
            byte[] orginText = Base64.getDecoder().decode(ecodeString);
            String decodeString = new String(orginText);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
//            byte[] plainText = cipher.doFinal(cipherText);
//            logger.info("decodeString: {}" , decodeString);
//            logger.info("plain : {}" , new String(plainText));
            logger.info("decode plain text: {}" , new String(cipher.doFinal(orginText)));
            logger.info("pubkey : {}" , new String(Base64.getEncoder().encode(pubKey.getEncoded())));
            logger.info("prikey : {}" , new String(Base64.getEncoder().encode(privKey.getEncoded())));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void testCipherUtil(){
        String inputString = "{this is a test}";
        String sign = CipherUtils.generateSign(inputString);
        logger.info("sign = {}", sign);

        logger.info("verify = {}", CipherUtils.verify(sign, inputString));
    }

    @Test
    public void testRandom(){
        for (int i=0;i<100;i++) {
            logger.info("random = {}", NumberUtil.randomInt(100,120));
        }
    }
}
