package com.gedoumi.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

public class WXBizDataCrypt {
	private byte[] aesKey;
	private byte[] aesIv;
	private byte[] aesCipher;

	public Map<String, Object> decryptData(String sessionKey, String encryptedData, String iv) {

		Map<String, Object> map = new HashMap<>();
		try {
			if (StringUtils.isEmpty(sessionKey) || sessionKey.length() != 24) {
				map.put("status", "0");
				map.put("msg", "解密失败[sessionKey error]");
				return map;
			}
			aesKey = Base64.decodeBase64(sessionKey);

			if (StringUtils.isEmpty(iv) || iv.length() != 24) {
				map.put("status", "0");
				map.put("msg", "解密失败[iv error]");
				return map;
			}
			aesIv = Base64.decodeBase64(iv);
			aesCipher = Base64.decodeBase64(encryptedData);
			byte[] result = AES.decrypt(aesCipher, aesKey, aesIv);
			if (null != result && result.length > 0) {
				String userInfo = new String(result, "UTF-8");
				System.out.println(userInfo);
				map.put("status", "1");
				map.put("msg", "解密成功");
				map.put("userInfo", userInfo);
			} else {
				map.put("status", "0");
				map.put("msg", "解密失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
