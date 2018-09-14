package com.gedoumi.sys.controller;

import com.gedoumi.asset.entity.UserTeam;
import com.gedoumi.asset.service.UserAssetService;
import com.gedoumi.asset.service.UserRewardService;
import com.gedoumi.asset.service.UserTeamService;
import com.gedoumi.asset.vo.UserTeamVO;
import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/reward")
public class SysRewardController {
	static Logger LOGGER = LoggerFactory.getLogger(SysRewardController.class);

	@Resource
	private UserService userService;

	@Resource
	private UserAssetService userAssetService;

	@Resource
	private UserTeamService userTeamService;

	@Resource
	private UserRewardService userRewardService;


    @RequiresPermissions("team:read")
    @GetMapping("/{id}/page")
    public DataGrid getPage(PageParam param, @PathVariable("id")Long id){

        UserTeam userTeam = userTeamService.getById(id);
        UserTeamVO userTeamVO = new UserTeamVO();
        userTeamVO.setUserId(userTeam.getUser().getId());
	    return userRewardService.getList(param, userTeamVO);
    }





}
