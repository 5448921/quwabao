package com.gedoumi.sys.controller;

import com.gedoumi.asset.entity.UserRent;
import com.gedoumi.asset.service.UserAssetService;
import com.gedoumi.asset.service.UserRentService;
import com.gedoumi.asset.service.UserTeamService;
import com.gedoumi.asset.vo.UserInfoVO;
import com.gedoumi.common.AppConfig;
import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.common.base.ResponseObject;
import com.gedoumi.common.enums.UserValidateStatus;
import com.gedoumi.sys.entity.SysRent;
import com.gedoumi.sys.service.SysRentService;
import com.gedoumi.user.entity.User;
import com.gedoumi.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class ManageUserController {
	static Logger LOGGER = LoggerFactory.getLogger(ManageUserController.class);

	@Resource
	private UserService userService;

	@Resource
	private AppConfig appConfig;

	@RequestMapping(value = "/")
	public ModelAndView getUserTeam(){
		return new ModelAndView("user/user");
	}

    @RequiresPermissions("user:read")
    @GetMapping("/page")
    public DataGrid getPage(PageParam param, UserInfoVO userInfoVO){

	    return userService.getPageList(param, userInfoVO);
    }


    /**
     * 同步矿机信息
     * @param ids
     * @return
     */
    @RequestMapping("cal/{ids}")
    public ResponseObject cal(@PathVariable("ids")String[] ids){
        ResponseObject responseObject = new ResponseObject();
        for (String id : ids) {
            User user = userService.getById(Long.parseLong(id));
            user.setValidateStatus(UserValidateStatus.Pass.getValue());
            userService.update(user);
        }
        responseObject.setSuccess();
        return responseObject;
    }




}
