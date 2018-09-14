package com.gedoumi.user.controller;

import com.gedoumi.common.Constants;
import com.gedoumi.common.annotation.PfcLogAspect;
import com.gedoumi.common.base.LoginToken;
import com.gedoumi.common.base.ResponseObject;
import com.gedoumi.common.enums.*;
import com.gedoumi.common.exception.BusinessException;
import com.gedoumi.sys.entity.SysSms;
import com.gedoumi.user.entity.User;
import com.gedoumi.sys.service.SysSmsService;
import com.gedoumi.user.vo.RegVO;
import com.gedoumi.util.CipherUtils;
import com.gedoumi.util.SessionUtil;
import com.gedoumi.util.SmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.gedoumi.user.service.UserService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/v1/login")
public class LoginController {
	static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private UserService userService;

	@Resource
	private SysSmsService smsService;


	@PostMapping(value = "/submit")
	public ResponseObject submit(@RequestBody RegVO regVO){

		String salt = new Md5Hash(regVO.getMobile()).toString();
		String pswd = new Md5Hash(regVO.getPassword(), salt).toString();
		ResponseObject responseObject = new ResponseObject();
		User user = userService.getByMobilePhone(regVO.getMobile());
		if(user == null){
			responseObject.setInfo(CodeEnum.UserLoginError);
			return responseObject;
		}
		if(user.getUserStatus()==UserStatus.Disable.getValue()){
			responseObject.setInfo(CodeEnum.UserLocked);
			return responseObject;
		}
		if(user.getErrorCount() > 3){
			responseObject.setInfo(CodeEnum.LoginTimesError);
			return responseObject;
		}

		if(StringUtils.equals(pswd,user.getPassword())){
			SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
			LoginToken loginToken = new LoginToken();
			loginToken.setUserName(user.getUsername());
			loginToken.setMobilePhone(user.getMobilePhone());
			loginToken.setToken(UUID.randomUUID().toString());
			responseObject.setData(loginToken);
			responseObject.setSuccess();
			user.setLastLoginIp(SessionUtil.getClientIp());
			user.setToken(loginToken.getToken());
			user.setDeviceId(SessionUtil.getDeviceFromHead());
			userService.updateLoginInfo(user);
		}else {
			user.setDeviceId(SessionUtil.getDeviceFromHead());
			userService.updateLoginError(user);
			responseObject.setInfo(CodeEnum.UserLoginError);
		}

		return responseObject;
	}

	@RequestMapping(value = "/check")
	public ResponseObject checkUser(@RequestParam(value = "mobile") String mobile){
		ResponseObject responseObject = new ResponseObject();
		if(!SmsUtil.isPhone(mobile)){
			responseObject.setInfo(CodeEnum.MobileError);
			return responseObject;
		}
		User user = userService.getByMobilePhone(mobile);
		if(user == null){
			responseObject.setData(false);
		}else {
			responseObject.setData(true);
		}
		responseObject.setSuccess();
		return  responseObject;
	}

	@RequestMapping(value = "/checkInviteCode")
	public ResponseObject checkInviteCode(@RequestParam(value = "regInviteCode") String regInviteCode){
		ResponseObject responseObject = new ResponseObject();

		User user = userService.getByInviteCode(regInviteCode);
		if(user == null){
			responseObject.setData(false);
		}else {
			responseObject.setData(true);
		}
		responseObject.setSuccess();
		return  responseObject;
	}

	@RequestMapping(value = "/checkName")
	public ResponseObject checkName(@RequestParam(value = "userName") String userName){
		ResponseObject responseObject = new ResponseObject();

		User user = userService.getByUsername(userName);
		if(user == null){
			responseObject.setData(false);
		}else {
			responseObject.setData(true);
		}
		responseObject.setSuccess();
		return  responseObject;
	}



	@RequestMapping(value = "/validateCode")
	public ResponseObject generateValidateCode(){
		ResponseObject responseObject = new ResponseObject();
		String validateCode = CipherUtils.generateValidateCode();
		responseObject.setData(validateCode);
		responseObject.setSuccess();
		SessionUtil.getSession().setAttribute(Constants.V_CODE_KEY, validateCode);

		return  responseObject;
	}

	@RequestMapping(value = "/getRegSmsCode")
	public ResponseObject getRegSmsCode(@RequestParam(value = "mobile") String mobile,
										@RequestParam(value = "vcode") String vcode){
		LOGGER.info(SessionUtil.getSession().getId());
		ResponseObject responseObject = new ResponseObject();
		if(!SmsUtil.isPhone(mobile)){
			responseObject.setInfo(CodeEnum.MobileError);
			return responseObject;
		}

		Object obj = SessionUtil.getSession().getAttribute(Constants.V_CODE_KEY);
		if(obj == null){
			responseObject.setInfo(CodeEnum.ValidateCodeError);
			return responseObject;
		}
		String validateCode = obj.toString();
		if(!StringUtils.equals(validateCode,vcode.toUpperCase())){
			responseObject.setInfo(CodeEnum.ValidateCodeError);
			return responseObject;
		}

		User user = userService.getByMobilePhone(mobile);

		if(user != null){
			responseObject.setInfo(CodeEnum.MobileExist);
			return responseObject;
		}

		int smsCount = smsService.getCurrentDayCount(mobile);
		if(smsCount>=Constants.SMS_DAY_COUNT){
			responseObject.setInfo(CodeEnum.SmsCountError);
			return responseObject;
		}

		String smsCode = SmsUtil.generateCode();
		try {
			SmsUtil.sendReg(mobile,smsCode);
		} catch (InterruptedException e) {
			e.printStackTrace();
			responseObject.setInfo(CodeEnum.SysError);
			return responseObject;
		}
		SysSms sms = new SysSms();
		sms.setCode(smsCode);
		sms.setSmsStatus(SmsStatus.Enable.getValue());
		sms.setSmsType(SmsType.Register.getValue());
		sms.setMobilePhone(mobile);
		Date now = new Date();
		sms.setCreateTime(now);
		sms.setUpdateTime(now);
		smsService.add(sms);
		responseObject.setSuccess();
		return  responseObject;
	}

	@RequestMapping(value = "/getRpSmsCode")
	public ResponseObject getRpSmsCode(@RequestParam(value = "mobile") String mobile,
										@RequestParam(value = "vcode") String vcode){
		ResponseObject responseObject = new ResponseObject();
		if(!SmsUtil.isPhone(mobile)){
			responseObject.setInfo(CodeEnum.MobileError);
			return responseObject;
		}

		Object obj = SessionUtil.getSession().getAttribute(Constants.V_CODE_KEY);
		if(obj == null){
			responseObject.setInfo(CodeEnum.ValidateCodeError);
			return responseObject;
		}
		String validateCode = obj.toString();
		if(!StringUtils.equals(validateCode,vcode)){
			responseObject.setInfo(CodeEnum.ValidateCodeError);
			return responseObject;
		}

		User user = userService.getByMobilePhone(mobile);

		if(user == null){
			responseObject.setInfo(CodeEnum.MobileError);
			return responseObject;
		}

		int smsCount = smsService.getCurrentDayCount(mobile);
		if(smsCount>=Constants.SMS_DAY_COUNT){
			responseObject.setInfo(CodeEnum.SmsCountError);
			return responseObject;
		}

		String smsCode = SmsUtil.generateCode();
		try {
			SmsUtil.sendReg(mobile,smsCode);
		} catch (InterruptedException e) {
			e.printStackTrace();
			responseObject.setInfo(CodeEnum.SysError);
			return responseObject;
		}
		SysSms sms = new SysSms();
		sms.setCode(smsCode);
		sms.setSmsStatus(SmsStatus.Enable.getValue());
		sms.setSmsType(SmsType.ResetPswd.getValue());
		sms.setMobilePhone(mobile);
		Date now = new Date();
		sms.setCreateTime(now);
		sms.setUpdateTime(now);
		smsService.add(sms);
		responseObject.setSuccess();
		return  responseObject;
	}



	@RequestMapping(value = "/reg")
	public ResponseObject register(@RequestBody RegVO regVO){
		ResponseObject responseObject = new ResponseObject();
//		Object obj =  SessionUtil.getSession().getAttribute(Constants.V_CODE_KEY);
//		if(obj == null){
//			responseObject.setInfo(CodeEnum.ValidateCodeError);
//			return responseObject;
//		}
//		String validateCode = obj.toString();
//		if(!StringUtils.equals(validateCode,regVO.getValidateCode().toUpperCase())){
//			responseObject.setInfo(CodeEnum.ValidateCodeError);
//			return responseObject;
//		}

		if(StringUtils.isEmpty(regVO.getRegInviteCode())){
			responseObject.setInfo(CodeEnum.InviteCodeError);
			return responseObject;
		}

		String mobile = regVO.getMobile();
		User user = userService.getByMobilePhone(mobile);

		if(user != null){
			responseObject.setInfo(CodeEnum.MobileExist);
			return responseObject;
		}
		SysSms query = new SysSms();
		query.setMobilePhone(regVO.getMobile());
		query.setCode(regVO.getSmsCode());
		query.setSmsStatus(SmsStatus.Enable.getValue());
		query.setSmsType(SmsType.Register.getValue());
		SysSms sms = smsService.getUserFul(query);
		if (sms == null){
			responseObject.setInfo(CodeEnum.ValidateCodeExpire);
			return responseObject;
		}

		if(StringUtils.isNotEmpty(regVO.getUserName())){
			User orgUser = userService.getByUsername(regVO.getUserName());
			if(orgUser != null) {
				responseObject.setInfo(CodeEnum.NameError);
				return responseObject;
			}
		}

		User parentUser = userService.getByInviteCode(regVO.getRegInviteCode().toLowerCase());
		if(parentUser == null) {
			responseObject.setInfo(CodeEnum.InviteCodeError);
			return responseObject;
		}


		User sysUser = new User();
		sysUser.setMobilePhone(mobile);
		String salt = new Md5Hash (mobile).toString();
		sysUser.setPassword(new Md5Hash(regVO.getPassword(), salt).toString());
		sysUser.setUserStatus(UserStatus.Enable.getValue());
		Date now = new Date();
		sysUser.setUpdateTime(now);
		sysUser.setRegisterTime(now);
		sysUser.setLastLoginTime(now);
		sysUser.setLastLoginIp(SessionUtil.getClientIp());
		sysUser.setToken(UUID.randomUUID().toString());
		sysUser.setUserType(UserType.Level_0.getValue());
		sysUser.setErrorCount(Short.valueOf("0"));
		sysUser.setDeviceId(SessionUtil.getDeviceFromHead());
		sysUser.setValidateStatus(UserValidateStatus.Init.getValue());

		sysUser.setRegInviteCode(regVO.getRegInviteCode());
		if(StringUtils.isNotEmpty(regVO.getUserName())){
			sysUser.setUsername(regVO.getUserName());
		}

		try {
			userService.addUser(sysUser);
		} catch (BusinessException e) {
			LOGGER.error(e.getMessage(), e);
			responseObject.setInfo(e.getCodeEnum());
			return responseObject;
		}

		responseObject.setSuccess();
		SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, sysUser);
		LoginToken loginToken = new LoginToken();
		loginToken.setUserName(sysUser.getUsername());
		loginToken.setMobilePhone(sysUser.getMobilePhone());
		loginToken.setToken(sysUser.getToken());
		responseObject.setData(loginToken);
		return  responseObject;
	}

	@RequestMapping(value = "/resetPswd")
	public ResponseObject resetPswd(@RequestBody RegVO regVO){
		ResponseObject responseObject = new ResponseObject();
		Object obj =  SessionUtil.getSession().getAttribute(Constants.V_CODE_KEY);
		if(obj == null){
			responseObject.setInfo(CodeEnum.ValidateCodeError);
			return responseObject;
		}
		String validateCode = obj.toString();
		if(!StringUtils.equals(validateCode,regVO.getValidateCode().toUpperCase())){
			responseObject.setInfo(CodeEnum.ValidateCodeError);
			return responseObject;
		}
		String mobile = regVO.getMobile();
		User user = userService.getByMobilePhone(mobile);

		if(user == null){
			responseObject.setInfo(CodeEnum.MobileError);
			return responseObject;
		}
		SysSms query = new SysSms();
		query.setMobilePhone(regVO.getMobile());
		query.setCode(regVO.getSmsCode());
		query.setSmsStatus(SmsStatus.Enable.getValue());
		query.setSmsType(SmsType.ResetPswd.getValue());
		SysSms sms = smsService.getUserFul(query);
		if (sms == null){
			responseObject.setInfo(CodeEnum.ValidateCodeExpire);
			return responseObject;
		}

		String salt = new Md5Hash(mobile).toString();
		user.setPassword(new Md5Hash(regVO.getPassword(), salt).toString());
		Date now = new Date();
		user.setUpdateTime(now);
		user.setLastLoginTime(now);
		user.setLastLoginIp(SessionUtil.getClientIp());
		user.setToken(UUID.randomUUID().toString());
		userService.updateLoginInfo(user);
		responseObject.setSuccess();
		SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
		LoginToken loginToken = new LoginToken();
		loginToken.setUserName(user.getUsername());
		loginToken.setMobilePhone(user.getMobilePhone());
		loginToken.setToken(user.getToken());
		responseObject.setData(loginToken);
		return  responseObject;
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
    public ResponseObject logout(){
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess();
		User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
		SessionUtil.getSession().removeAttribute(Constants.API_USER_KEY);
		if(user != null)
			userService.updateLogout(user);
        return responseObject;
    }

	@RequestMapping(value="/getSessionId", method=RequestMethod.GET)
	public ResponseObject getSessionId(){
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess();
		responseObject.setData(SessionUtil.getSession().getId());
		return responseObject;
	}

}
