package com.gedoumi.asset.controller;

import com.gedoumi.asset.entity.UserAssetDetail;
import com.gedoumi.asset.service.AssetDetailService;
import com.gedoumi.asset.vo.DetailTypeVO;
import com.gedoumi.asset.vo.UserAssetDetailVO;
import com.gedoumi.asset.vo.UserTeamVO;
import com.gedoumi.common.Constants;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.common.base.ResponseObject;
import com.gedoumi.common.enums.RentType;
import com.gedoumi.user.entity.User;
import com.gedoumi.user.service.UserService;
import com.gedoumi.util.NumberUtil;
import com.gedoumi.util.SessionUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/v1/detail")
public class AssetDetailController {
	 Logger logger = LoggerFactory.getLogger(AssetDetailController.class);

	@Resource
	private AssetDetailService assetDetailService;

	@Resource
	private UserService userService;

	@RequestMapping(value = "/page")
	ResponseObject getAssetDetail(@RequestBody DetailTypeVO detailTypeVO){
		User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
		detailTypeVO.setUserId(user.getId());
		PageParam pageParam = new PageParam();
		pageParam.setPage(detailTypeVO.getPage());
		pageParam.setRows(detailTypeVO.getRows());
		List<UserAssetDetail>  data = assetDetailService.getList(pageParam, detailTypeVO);
		ResponseObject responseObject = new ResponseObject();
		List<UserAssetDetailVO> dataVO = Lists.newArrayList();
		for (UserAssetDetail assetDetail : data) {
			UserAssetDetailVO vo = new UserAssetDetailVO();
			BeanUtils.copyProperties(assetDetail,vo);
//			if(assetDetail.getUserRent() != null){
//				RentType rentType = RentType.fromValue(assetDetail.getUserRent().getRentType());
//				vo.setDigNumber(NumberUtil.randomInt(rentType.getDigMin(), rentType.getDigMax()));
//			}

			dataVO.add(vo);
		}
		responseObject.setData(data);
		responseObject.setSuccess();
		return responseObject;
	}



}
