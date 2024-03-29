package com.gedoumi.user.controller;

import com.alibaba.fastjson.JSON;
import com.gedoumi.api.face.FaceApi;
import com.gedoumi.api.face.FaceApiResponse;
import com.gedoumi.api.face.IDApi;
import com.gedoumi.api.face.IDApiResponse;
import com.gedoumi.api.face.vo.FaceVO;
import com.gedoumi.api.gateway.ApiBindResponse;
import com.gedoumi.api.gateway.ApiResponse;
import com.gedoumi.api.gateway.TransApi;
import com.gedoumi.api.gateway.vo.BindVO;
import com.gedoumi.api.gateway.vo.QueryVO;
import com.gedoumi.api.gateway.vo.RechargeVO;
import com.gedoumi.common.Constants;
import com.gedoumi.common.base.ResponseObject;
import com.gedoumi.common.enums.*;
import com.gedoumi.common.exception.BusinessException;
import com.gedoumi.sys.entity.SysLog;
import com.gedoumi.sys.service.SysLogService;
import com.gedoumi.user.entity.User;
import com.gedoumi.user.vo.BindRegVO;
import com.gedoumi.user.vo.PswdVO;
import com.gedoumi.user.vo.ValidateUserVO;
import com.gedoumi.util.SessionUtil;
import com.gedoumi.util.SmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.user.service.UserService;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/v1/user")
public class UserController {

	static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@Resource
	private SysLogService logService;

	@RequestMapping(value = "/getUser")
	public ResponseObject getUser(@RequestParam(value = "mobile") String mobile){
		ResponseObject responseObject = new ResponseObject();
		if(!SmsUtil.isPhone(mobile)){
			responseObject.setInfo(CodeEnum.MobileError);
			return responseObject;
		}
		User user = userService.getByMobilePhone(mobile);
		if(user != null){
			responseObject.setData(user);
		}
		responseObject.setSuccess();
		return  responseObject;
	}

	@RequestMapping("/page")
	public ResponseObject getPage(@RequestBody PageParam param){
		User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
		DataGrid dataGrid = userService.getList(param, user);

		ResponseObject responseObject = new ResponseObject();
		responseObject.setData(dataGrid);
		responseObject.setSuccess();
		return responseObject;
	}

	@RequestMapping("/bindCode")
	public ResponseObject bindRegInviteCode(@RequestBody BindRegVO bindRegVO){
		ResponseObject responseObject = new ResponseObject();
		User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
		if(user.getUserType() == UserType.Level_Team.getValue()){
			responseObject.setInfo(CodeEnum.TeamBindError);
			return responseObject;
		}
		if(StringUtils.equals(bindRegVO.getRegInviteCode(), user.getInviteCode())){
			responseObject.setInfo(CodeEnum.BindInviteSelfError);
			return responseObject;
		}

		if(StringUtils.isNotEmpty(user.getRegInviteCode())){
			responseObject.setInfo(CodeEnum.InviteCodeBindError);
			return responseObject;
		}
		if(StringUtils.isNotEmpty(bindRegVO.getUserName())){
			User orgUser = userService.getByUsername(bindRegVO.getUserName());
			if(orgUser != null && orgUser.getId() != user.getId()) {
				responseObject.setInfo(CodeEnum.NameError);
				return responseObject;
			}
		}
		if(StringUtils.isNotEmpty(bindRegVO.getRegInviteCode())){
			User parentUser = userService.getByInviteCode(bindRegVO.getRegInviteCode().toLowerCase());
			if(parentUser == null) {
				responseObject.setInfo(CodeEnum.InviteCodeError);
				return responseObject;
			}
		}
		user.setRegInviteCode(bindRegVO.getRegInviteCode());
		if(StringUtils.isNotEmpty(bindRegVO.getUserName())){
			user.setUsername(bindRegVO.getUserName());
		}

		try {
			userService.bindRegInviteCode(user);
		} catch (BusinessException e) {
			LOGGER.error(e.getMessage(), e);
			responseObject.setInfo(e.getCodeEnum());
			return responseObject;
		}
		SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
		responseObject.setSuccess();
		return responseObject;
	}

	@RequestMapping("/updateUsername")
	public ResponseObject updateUsername(@RequestBody BindRegVO bindRegVO){
		ResponseObject responseObject = new ResponseObject();
		User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);

		if(StringUtils.isEmpty(bindRegVO.getUserName())){
			responseObject.setInfo(CodeEnum.NoNameError);
			return responseObject;
		}

		User orgUser = userService.getByUsername(bindRegVO.getUserName());
		if(orgUser != null) {
			responseObject.setInfo(CodeEnum.NameError);
			return responseObject;
		}

		user.setUsername(bindRegVO.getUserName());
		user.setUpdateTime(new Date());
		userService.update(user);

		responseObject.setSuccess();
		SessionUtil.getSession().setAttribute(Constants.API_USER_KEY,user);
		return responseObject;
	}

	@RequestMapping("/updatePswd")
	public ResponseObject updatePswd(@RequestBody PswdVO pswdVO){

		ResponseObject responseObject = new ResponseObject();
		if(StringUtils.isEmpty(pswdVO.getOrgPswd()) || StringUtils.isEmpty(pswdVO.getPswd())){
			responseObject.setInfo(CodeEnum.ResetPswdError);
			return responseObject;
		}
		User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
		String salt = new Md5Hash(user.getMobilePhone()).toString();
		String orgPswd = new Md5Hash(pswdVO.getOrgPswd(),salt).toString();

		if(!StringUtils.equals(user.getPassword(), orgPswd)){
			responseObject.setInfo(CodeEnum.OrgPswdError);
			return responseObject;
		}
		String pswd = new Md5Hash(pswdVO.getPswd(),salt).toString();
		user.setPassword(pswd);
		user.setUpdateTime(new Date());
		userService.update(user);

		responseObject.setSuccess();
		return responseObject;
	}


	@RequestMapping(value = "/checkName")
	public ResponseObject checkUser(@RequestParam(value = "name") String name){
		ResponseObject responseObject = new ResponseObject();
		if(StringUtils.isNotEmpty(name)){
			User user = userService.getByUsername(name);
			if(user == null){
				responseObject.setData(false);
			}else {
				responseObject.setData(true);
			}
		}
		responseObject.setSuccess();
		return  responseObject;
	}

	@RequestMapping(value = "/valUser")
	public ResponseObject validateUser(@RequestBody ValidateUserVO validateUserVO){
		LOGGER.info("in valUser ");
		ResponseObject responseObject = new ResponseObject();
		if(
			StringUtils.isEmpty(validateUserVO.getIdCard()) ||
			StringUtils.isEmpty(validateUserVO.getRealName())){
			responseObject.setInfo(CodeEnum.ValidateIdParamError);
			return  responseObject;
		}

		Date now = new Date();
		User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
		if(user.getValidateStatus() != null && user.getValidateStatus() == UserValidateStatus.Pass.getValue()){
			responseObject.setInfo(CodeEnum.ValidateAlready);
			return  responseObject;
		}

		User orgUser = userService.getByIdCard(validateUserVO.getIdCard());
		if(orgUser != null && !orgUser.getId().equals(user.getId())){
			responseObject.setInfo(CodeEnum.ValidateIdCardAlready);
			return  responseObject;
		}

		validateUserVO.setUserId(user.getId());
		FaceVO faceVO = new FaceVO();
		faceVO.setMall_id(FaceApi.mall_id);
		faceVO.setIdcard(validateUserVO.getIdCard());
		faceVO.setImage(validateUserVO.getBase64Img());
		faceVO.setRealname(validateUserVO.getRealName());
		faceVO.setTm(String.valueOf(System.currentTimeMillis()));
		faceVO.setSign(faceVO.generateSign());
//		FaceApiResponse faceApiResponse = new FaceApiResponse();
		IDApiResponse idApiResponse = new IDApiResponse();
		SysLog sysLog = new SysLog();
		sysLog.setRequestBody(StringUtils.EMPTY);
		sysLog.setLogType(LogType.FaceValidate.getValue());
		sysLog.setMobile(user.getMobilePhone());
		sysLog.setRequestUrl(FaceApi.getUrl()+FaceApi.path);
		sysLog.setCreateTime(now);
		sysLog.setUpdateTime(now);
		sysLog.setLogStatus(SysLogStatus.Fail.getValue());
		sysLog.setClientIp(SessionUtil.getClientIp());
		logService.add(sysLog);
		try {
//			 faceApiResponse = FaceApi.testFace(faceVO);
			 idApiResponse = IDApi.testID(faceVO);
		} catch (Exception e) {
			LOGGER.error("call face api error",e);
			responseObject.setData(false);
		}
		userService.validateUser(validateUserVO, idApiResponse);
		if (idApiResponse.isSucess()){
			sysLog.setLogStatus(SysLogStatus.Success.getValue());
			logService.update(sysLog);
			responseObject.setData(true);
		}else {
			responseObject.setData(false);
		}

		User valUser = userService.getByMobilePhone(user.getMobilePhone());
		SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, valUser);
		responseObject.setSuccess();
		return  responseObject;
	}



	
}
