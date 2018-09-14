package com.gedoumi;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Created by md on 11/2/17.
 */

public class KeyTest2 {

    static Logger logger = LoggerFactory.getLogger(KeyTest2.class);
    private static final String PRIVATE_KEY_PATH = "C:\\Users\\sai_m\\IdeaProjects\\pfc\\src\\main\\resources\\pri.cer";
    private static final String PUBLIC_KEY_PATH = "C:\\Users\\sai_m\\IdeaProjects\\pfc\\src\\main\\resources\\pub.cer";
    /**
     *生成私钥  公钥
     */
    public static void geration(){
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_PATH);
            fos.write(publicKeyBytes);
            fos.close();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            fos = new FileOutputStream(PRIVATE_KEY_PATH);
            fos.write(privateKeyBytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公钥
     * @param filename
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }


    /**
     * 获取私钥
     * @param filename
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename)throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec =new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Test
    public void testKey() {
        geration();

        String input = "phone=18011112222&seer=100";
        RSAPublicKey pubKey;
        RSAPrivateKey privKey;
        byte[] cipherText;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");
            pubKey = (RSAPublicKey) getPublicKey(PUBLIC_KEY_PATH);
            privKey = (RSAPrivateKey) getPrivateKey(PRIVATE_KEY_PATH);

            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            cipherText = cipher.doFinal(input.getBytes());
            //加密后的东西
            logger.info("==============");
            logger.info("cipher: {}" , new String(cipherText));
            //开始解密
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainText = cipher.doFinal(cipherText);
            String ecodeString = new String(Base64.getEncoder().encode(cipherText));
            byte[] orginText = Base64.getDecoder().decode(ecodeString);
            String decodeString = new String(orginText);
            logger.info("ecodeString: {}" ,ecodeString);
            logger.info("decodeString: {}" , decodeString);
            logger.info("plain : {}" , new String(plainText));
            logger.info("plain : {}" , new String(cipher.doFinal(orginText)));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
