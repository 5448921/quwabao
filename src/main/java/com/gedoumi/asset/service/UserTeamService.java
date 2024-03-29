package com.gedoumi.asset.service;

import com.gedoumi.asset.dao.*;
import com.gedoumi.asset.entity.*;
import com.gedoumi.asset.vo.UserTeamVO;
import com.gedoumi.common.AppConfig;
import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.common.enums.*;
import com.gedoumi.user.dao.UserDao;
import com.gedoumi.user.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class UserTeamService {

	Logger logger = LoggerFactory.getLogger(UserTeamService.class);
	


	@Resource
	private UserTeamDao userTeamDao;

	@Resource
	private UserDao userDao;

	@Resource
	private UserTeamRewardDao userTeamRewardDao;

	@Resource
	private AppConfig appConfig;


	@PersistenceContext
	private EntityManager entityManager;

	public UserTeam getById(Long id){
		return userTeamDao.findOne(id);
	}


	@Transactional
	public void updateUserTeam(UserTeam userTeam){
		userTeamDao.save(userTeam);
	}

	@Transactional
	public void updateUserTeam(UserTeam userTeam, int userType){
		userTeamDao.save(userTeam);
		User orgUser = userDao.findOne(userTeam.getUser().getId());
		orgUser.setUpdateTime(new Date());
		orgUser.setUserType(userType);
		userDao.save(orgUser);
	}



	@SuppressWarnings("unchecked")
	public DataGrid getList(PageParam param, UserTeamVO userTeamVO){
		DataGrid data=new DataGrid();
		String prefSql = " from UserTeam t where 1=1 ";
		StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
		StringBuffer sqlData = new StringBuffer("select t ").append(prefSql);


		String mobile = userTeamVO.getMobilePhone();
		String username = userTeamVO.getUsername();
		Integer teamStatus = userTeamVO.getTeamStatus();
		if(StringUtils.isNotEmpty(mobile)){
			sqlCount.append("and t.user.mobilePhone like :mobilePhone ");
			sqlData.append("and t.user.mobilePhone like :mobilePhone ");
		}
		if(StringUtils.isNotEmpty(username)){
			sqlCount.append("and t.user.username like :username ");
			sqlData.append("and t.user.username like :username ");
		}
		if(teamStatus != TeamStatus.All.getValue()){
			sqlCount.append("and t.teamStatus = :teamStatus ");
			sqlData.append("and t.teamStatus = :teamStatus ");
		}
		sqlData.append("order by t.id desc ");
		Query queryCount = entityManager.createQuery(sqlCount.toString());
		Query queryData = entityManager.createQuery(sqlData.toString());
		if(StringUtils.isNotEmpty(mobile)){
			queryCount.setParameter("mobilePhone", "%"+mobile+"%");
			queryData.setParameter("mobilePhone", "%"+mobile+"%");
		}
		if(StringUtils.isNotEmpty(username)){
			queryCount.setParameter("username", "%"+username+"%");
			queryData.setParameter("username", "%"+username+"%");
		}
		if(teamStatus != TeamStatus.All.getValue()){
			queryCount.setParameter("teamStatus", teamStatus);
			queryData.setParameter("teamStatus", teamStatus);
		}
		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());

		List<UserTeam> list = (List<UserTeam>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();

		data.setTotal(count);
		data.setRows(list);
		return data;
	}

}
