package com.gedoumi.common.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 *
 * 类描述：将图片转化为Base64字符串
 * 创建人：
 * 修改备注：
 *
 * @version V1.0
 */
public class Base64Img {
    /**
     * @Title: GetImageStrFromUrl
     * @Description: TODO(将一张网络图片转化成Base64字符串)
     * @param imgURL 网络资源位置
     * @return Base64字符串
     */
    public static String getImageStrFromUrl(String imgURL) {
        byte[] data = null;
        try {
            // 创建URL
            URL url = new URL(imgURL);
            data = FileUtils.readFileToByteArray(FileUtils.toFile(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return new String(Base64.getEncoder().encode(data));
//        return encoder.encode(data);
    }



    /**
     * @Title: GenerateImage
     * @Description: TODO(base64字符串转化成图片)
     * @param imgStr
     * @return
     */
    public static boolean generateImage(String imgStr, String filePath) {
        if (imgStr == null) // 图像数据为空
            return false;
        try {
            FileUtils.writeByteArrayToFile(FileUtils.toFile(new URL(filePath)), Base64.getDecoder().decode(imgStr));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static  void main(String[] args){
        String s = getImageStrFromUrl("file:///C:\\Users\\sai_m\\Pictures\\mada.jpg");
        System.out.println(s);
        System.out.println(s.length());


        generateImage(s, "file:///C:\\Users\\sai_m\\Pictures\\222.jpg");

    }
}
