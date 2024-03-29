package com.gedoumi.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.security.Security;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理
 * 对原始数据进行AES加密后，在进行Base64编码转化；
 * 正确
 */
public class AesCBC {
    /*已确认
     * 加密用的Key 可以用26个字母和数字组成
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static String sKey="J5Ecs7kv9f37WGppWfUcLhuijyUfVdt+SJOgwSmnml4=";
    private static AesCBC instance=null;
    private static final String PRIVATE_KEY_PATH2 = "privateKey";
    static int BLOCK_SIZE = 32;
    static Charset CHARSET = Charset.forName("utf-8");
    //private static
    private AesCBC(){

    }
    public static AesCBC getInstance(){
        if (instance==null)
            instance= new AesCBC();
        return instance;
    }
    // 加密
    public static String encrypt(String sSrc) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        File f = ResourceUtils.getFile("classpath:" + PRIVATE_KEY_PATH2);
        byte[] fileBytes = FileUtils.readFileToByteArray(f);
        byte[] raw = Base64.decodeBase64(fileBytes);

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        byte[] ivp = new byte[16];
        System.arraycopy(raw,0,ivp,0,16);
        IvParameterSpec iv = new IvParameterSpec(ivp);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] padding = pKCS7Encode(sSrc.getBytes(CHARSET).length);
        byte[] unEncrypted = ArrayUtils.addAll(sSrc.getBytes(CHARSET), padding);
//        System.out.println(new String(unEncrypted));
//        System.out.println(new String(unEncrypted).length());
        byte[] encrypted = cipher.doFinal(unEncrypted);
//        byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET));
        return DigestUtils.sha1Hex(encrypted);
    }

    /**
     * 获得对明文进行补位填充的字节.
     *
     * @param count
     *            需要进行填充补位操作的明文字节个数
     * @return 补齐用的字节数组
     */
    static byte[] pKCS7Encode(int count) {
        // 计算需要填充的位数
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE;
        }
        // 获得补位所用的字符
        char padChr = chr(amountToPad);
        String tmp = new String();
        for (int index = 0; index < amountToPad; index++) {

            tmp += padChr;
        }
        System.out.println(tmp);
        return tmp.getBytes(CHARSET);
    }



    /**
     * 将数字转化成ASCII码对应的字符，用于对明文进行补码
     *
     * @param a
     *            需要转化的数字
     * @return 转化得到的字符
     */
    static char chr(int a) {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }


    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        String cSrc = "/api/v1/pfc_eth/queryaddress0xD10fbC84D1884015fd67255ED6446F45747DE520seq092713ts1534521749";
        System.out.println("加密前的字串是："+cSrc);
        // 加密
        String enString = AesCBC.encrypt(cSrc);
        System.out.println("加密后的字串是："+ enString);

        System.out.println("a51a9fe8cd20c32b9d5932fcb306cf21d5eda52c".endsWith(enString));

    }
}