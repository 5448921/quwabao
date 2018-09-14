package com.gedoumi.api.gateway;

import com.alibaba.fastjson.JSON;
import com.gedoumi.api.gateway.vo.QueryVO;
import com.gedoumi.api.gateway.vo.RechargeVO;
import com.gedoumi.api.gateway.vo.WithDrawVO;
import com.gedoumi.asset.vo.AppWithDrawVO;
import com.gedoumi.api.face.vo.FaceVO;
import com.gedoumi.common.Constants;
import com.gedoumi.common.base.ResponseObject;
import com.gedoumi.common.utils.HttpClientUtils;
import com.gedoumi.util.PropertiesUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TransApi {

    static Logger logger = LoggerFactory.getLogger(TransApi.class);

    public static String test_url = "http://192.168.12.41:8090";
    public static String url = "http://175.25.17.53:8098";
    public static String path = "/api/v1/pfc/recharge";
    public static String path_get_address = "/api/v1/pfc_eth/query";
    public static String path_bind_address = "/api/v1/pfc_eth/bind";
    public static String path_withdraw = "/api/v1/pfc/withdraw";

    public static String getUrl(){
        String value = PropertiesUtils.getInstance().getValue("gateway.url");
        if(StringUtils.isNotEmpty(value)){
            return value;
        }
        return url;
    }

    public static String getTestUrl(){
        String testValue = PropertiesUtils.getInstance().getValue("test.url");
        if(StringUtils.isNotEmpty(testValue)){
            return testValue;
        }
        return test_url;
    }


    public static ApiResponse getEthAddress(QueryVO queryVO) throws Exception{
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("pfc_account", queryVO.getPfc_account()));
        params.add(new BasicNameValuePair("ts", String.valueOf(queryVO.getTs())));
        params.add(new BasicNameValuePair("sig", queryVO.getSig()));
        String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        logger.info("paramStr = {}", paramStr);
        HttpGet httpGet = new HttpGet(getUrl() + path_get_address +"?" + paramStr);

        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpGet);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        logger.info("call result = {}", result);
        return JSON.parseObject(result, ApiResponse.class);
    }

    public static ApiBindResponse postBindEthAddress(QueryVO queryVO) throws Exception{
        HttpPost httpPost=new HttpPost(getUrl() + path_bind_address);
//        String result = getPostResult(httpPost, JSON.toJSONString(queryVO));
        String result = getFormString(httpPost, getFormEntity(queryVO));
        return JSON.parseObject(result, ApiBindResponse.class);
    }

    private static String getPostResult(HttpPost httpPost, String jsonString) throws IOException {
        httpPost.setHeader("Content-Type", "application/json");
        logger.info("json = {}", jsonString);
        httpPost.setEntity(new StringEntity(jsonString));

//        httpPost.setEntity(getFormEntity(rechargeVO));
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        logger.info("call result = {}", result);
        return result;
    }


    private static String getFormString(HttpPost httpPost, HttpEntity formEntity) throws IOException {
        logger.info("formEntity = {}", formEntity);
//        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(formEntity);
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        logger.info("call result = {}", result);
        return result;
    }



    public static ApiResponse postWithDrawToGateWay(WithDrawVO withDrawVO) throws Exception{
        logger.info("withDrawVO = {}", JSON.toJSON(withDrawVO));
        HttpPost httpPost=new HttpPost(getUrl() + path_withdraw);
//        String result = getPostResult(httpPost, JSON.toJSONString(withDrawVO));
        String result = getFormString(httpPost, getFormEntity(withDrawVO));
        return JSON.parseObject(result, ApiResponse.class);
    }

    public static ApiResponse postRechargeToPFC(RechargeVO rechargeVO) throws Exception {
        logger.info("rechargeVO {}", JSON.toJSON(rechargeVO));
        HttpPost httpPost=new HttpPost(getTestUrl() + path);
//        String result = getPostResult(httpPost, JSON.toJSONString(rechargeVO));
        String result = getFormString(httpPost, getFormEntity(rechargeVO));
        return JSON.parseObject(result, ApiResponse.class);
    }

    public static ResponseObject postWithDrawToPFC(AppWithDrawVO appWithDrawVO) throws Exception {
        logger.info("appWithDrawVO {}", JSON.toJSON(appWithDrawVO));
        HttpPost httpPost=new HttpPost("http://127.0.0.1:8090/v1/uasset/withdraw");
        httpPost.setHeader("auth-token","fc958585-fd80-4300-a475-5cdc596dac54");
        httpPost.setHeader("deviceid","999999999999");
        String result = getPostResult(httpPost, JSON.toJSONString(appWithDrawVO));
//        String result = getFormString(httpPost, getFormEntity(rechargeVO));
        return JSON.parseObject(result, ResponseObject.class);
    }


    public static HttpEntity getFormEntity(RechargeVO rechargeVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("amount", rechargeVO.getAmount()));
        formParams.add(new BasicNameValuePair("asset_name", rechargeVO.getAsset_name()));
        formParams.add(new BasicNameValuePair("pfc_account", rechargeVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("seq", rechargeVO.getSeq()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(rechargeVO.getTs())));
        formParams.add(new BasicNameValuePair("sig", rechargeVO.getSig()));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }

    public static HttpEntity getFormEntity(QueryVO queryVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("pfc_account", queryVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(queryVO.getTs())));
        formParams.add(new BasicNameValuePair("sig", queryVO.getSig()));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }

    public static HttpEntity getFormEntity(WithDrawVO withDrawVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("pfc_account", withDrawVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(withDrawVO.getTs())));
        formParams.add(new BasicNameValuePair("amount", withDrawVO.getAmount()));
        formParams.add(new BasicNameValuePair("asset_name", withDrawVO.getAsset_name()));
        formParams.add(new BasicNameValuePair("memo", withDrawVO.getMemo()));
        formParams.add(new BasicNameValuePair("sig", withDrawVO.getSig()));
        formParams.add(new BasicNameValuePair("seq", String.valueOf(withDrawVO.getSeq())));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }


//
//
//    public static ResponseObject getPfcEthAddress() throws Exception {
//        HttpPost httpPost=new HttpPost(url);
//        httpPost.setHeader("auth-token","123");
//        httpPost.setHeader("Content-Type","application/json");
//
//        RentVO rentVO = new RentVO();
//        rentVO.setRentType(1);
//
//        httpPost.setEntity(new StringEntity(JSON.toJSONString(rentVO)));
//
//
//        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
//        HttpEntity entity = resp.getEntity();
//        String result = EntityUtils.toString(entity, "UTF-8");
//        logger.info("call result = {}", result);
//        return JSON.parseObject(result, ResponseObject.class);
//    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
//        for (int i=0;i<1;i++){
//            ApiResponse responseObject = null;
//            RechargeVO rechargeVO = new RechargeVO();
//            rechargeVO.setAmount("500000");
//            rechargeVO.setAsset_name(Constants.ASSET_NAME);
//            rechargeVO.setPfc_account("13716941005");
//            rechargeVO.setSeq("1022");
//            rechargeVO.setTs(System.currentTimeMillis()/1000);
//
//            String sign = rechargeVO.generateSign(path);
//            rechargeVO.setSig(sign);
//            try {
//                responseObject = TransApi.postRechargeToPFC(rechargeVO);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println(JSON.toJSON(responseObject));
//        }
//        int count = 3;
//        final CountDownLatch latch = new CountDownLatch(count);
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                5,
//                10,
//                200,
//                TimeUnit.MILLISECONDS,
//                new ArrayBlockingQueue<Runnable>(5));
//        for (int i=0;i<3;i++){
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    ResponseObject responseObject = null;
//                    AppWithDrawVO appWithDrawVO = new AppWithDrawVO();
//                    appWithDrawVO.setAmount("10.05");
//                    appWithDrawVO.setPswd("111111");
//                    try {
//                        responseObject = TransApi.postWithDrawToPFC(appWithDrawVO);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(JSON.toJSON(responseObject));
//                    latch.countDown();
//                }
//            });
//        }
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        FaceVO faceVO = new FaceVO();
//        faceVO.setIdcard("513901199005033020");
//        faceVO.setMall_id("111381");
//        faceVO.setRealname("秦琴");
//        faceVO.setTm(String.valueOf(System.currentTimeMillis()));
//        faceVO.setImage(Base64Img.getImageStrFromUrl("file:///C:\\Users\\sai_m\\Pictures\\qq2.jpg"));
//        faceVO.setSign(faceVO.generateSign());
//        try {
//             TransApi.testFace(faceVO);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        ApiResponse responseObject = null;
//        QueryVO queryVO = new QueryVO();
//        queryVO.setPfc_account("188106");
////        queryVO.setTs(System.currentTimeMillis()/1000);
//        queryVO.setTs(1534521749);
//
//        String sign = queryVO.generateSign(path_get_address);
//        queryVO.setSig(sign);
//        try {
//            responseObject = TransApi.getEthAddress(queryVO);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(JSON.toJSON(responseObject));

//        ApiResponse responseObject = null;
//        QueryVO queryVO = new QueryVO();
//        queryVO.setPfc_account("18620885643");
//        queryVO.setTs(System.currentTimeMillis()/1000);
////        queryVO.setTs(1534521749);
//
//        String sign = queryVO.generateSign(path_bind_address);
//        queryVO.setSig(sign);
//        try {
//            responseObject = TransApi.postBindEthAddress(queryVO);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(JSON.toJSON(responseObject));


        ApiResponse responseObject = null;
        WithDrawVO withDrawVO = new WithDrawVO();
//        withDrawVO.setPfc_account("123123121");
        withDrawVO.setPfc_account("18620885643");
        withDrawVO.setTs(System.currentTimeMillis()/1000);
//        withDrawVO.setTs(1534920585);
        withDrawVO.setAsset_name("pfc");
        withDrawVO.setMemo("erc20#0xfa0b873c38689E3CCf13B64E54EaB2c14541E333");
//        withDrawVO.setMemo("erc20#0x183191fB90a736f656ea2FF5DAB1f3fcBfaC8ADF");
//        withDrawVO.setMemo("erc20#0x2E33db3817c495beb698D9a63fFf0Fb210c18fc3");
//        withDrawVO.setMemo("erc201111111");
        withDrawVO.setAmount("5.5");
//        withDrawVO.setSeq(11132);
        withDrawVO.setSeq(1);
//        queryVO.setTs(1534521749);

        String sign = withDrawVO.generateSign(path_withdraw);
        withDrawVO.setSig(sign);
        try {
            responseObject = TransApi.postWithDrawToGateWay(withDrawVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSON(responseObject));
        long end = System.currentTimeMillis();
        System.out.println("process times  : " + (end - start) + "ms");
        System.exit(-1);
    }



}
