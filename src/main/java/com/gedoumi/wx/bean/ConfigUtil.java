package com.gedoumi.wx.bean;

import com.gedoumi.common.utils.ApplicationPath;

public class ConfigUtil {
	/**
	 * 服务号相关信息
	 */

	/**
	 * 正式服务号相关信息
	 */
	// public final static String MCH_ID = "1469878402";// 商户号
	// public final static String API_KEY =
	// "144f4c4974924716b52ac77b3b5a4e54";// API密钥

	// HTTPS证书的本地路径
	// public final static String certLocalPath =
	// ConfigUtil.class.getResource("/").getPath() + "cert/apiclient_cert.p12";
	// HTTPS证书密码，默认密码等于商户号MCHID
	// public final static String certPassword = "1469878402";

	// public final static String DOMAIN = "http://www.tojoycloud.com";
	// 正式结束

	/**
	 * 测试服务号
	 */
	public final static String MCH_ID = ApplicationPath.getParameter("MCH_ID");// 商户号
	public final static String API_KEY = ApplicationPath.getParameter("API_KEY");// API密钥

	// HTTPS证书的本地路径
	public final static String certLocalPath = ConfigUtil.class.getResource("/").getPath()
			+ ApplicationPath.getParameter("certLocalPath");
	// HTTPS证书密码，默认密码等于商户号MCHID
	public final static String certPassword = ApplicationPath.getParameter("certPassword");

	public final static String DOMAIN = ApplicationPath.getParameter("domain");
	// 测试结束

	public final static String SIGN_TYPE = "MD5";// 签名加密方式
	// 微信支付统一接口的回调action 普通订单
	public final static String NOTIFY_URL = DOMAIN + "/wxfront/m/order/notify.do";
	// 微信支付统一接口的回调action 充值订单
	public final static String NOTIFY_WALLET_URL = DOMAIN + "/wxfront/m/my/notify.do";
	// 微信支付统一接口的回调action 活动报名
	public final static String NOTIFY_ACTIVITY_URL = DOMAIN + "/wxfront/m/my/activityupdatesignup.do";
	// 微信支付统一接口的回调action 开通互助宝订单支付
	public final static String NOTIFY_HZB_URL = DOMAIN + "/wxfront/m/hzb/notify.do";
	// 正式end

	/**
	 * 微信基础接口地址
	 */
	// 获取token接口(GET)
	public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// oauth2授权接口(GET)
	public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	// 刷新access_token接口（GET）
	public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	// 菜单创建接口（POST）
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单查询（GET）
	public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// 菜单删除（GET）
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	/**
	 * 微信支付接口地址
	 */
	// 微信支付统一接口(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 微信退款接口(POST)
	public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 订单查询接口(POST)
	public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	// 关闭订单接口(POST)
	public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	// 退款查询接口(POST)
	public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	// 对账单接口(POST)
	public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	// 短链接转换接口(POST)
	public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	// 接口调用上报接口(POST)
	public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
	/**
	 * 企业号付款
	 */
	public final static String TRANSFERS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

	/**
	 * wxUserId转openId
	 */
	public final static String CONVERT_TO_OPENID = "https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=ACCESS_TOKEN";

	public static void main(String[] args) {
		System.out.println(ConfigUtil.class.getResource("/"));
	}
}
