package com.gedoumi.asset.service;

import com.gedoumi.asset.dao.UserTeamDao;
import com.gedoumi.asset.dao.UserTeamRewardDao;
import com.gedoumi.asset.entity.UserTeamReward;
import com.gedoumi.asset.vo.UserTeamVO;
import com.gedoumi.common.AppConfig;
import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.user.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class UserRewardService {

	Logger logger = LoggerFactory.getLogger(UserRewardService.class);
	


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

	public UserTeamReward getById(Long id){
		return userTeamRewardDao.findOne(id);
	}


	@Transactional
	public void update(UserTeamReward userTeamReward){
		userTeamRewardDao.save(userTeamReward);
	}



	@SuppressWarnings("unchecked")
	public DataGrid getList(PageParam param, UserTeamVO userTeamVO){
		DataGrid data=new DataGrid();
		String prefSql = " from UserTeamReward t where 1=1 ";
		StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
		StringBuffer sqlData = new StringBuffer("select t ").append(prefSql);


		Long userId = userTeamVO.getUserId();
		if(userId != null){
			sqlCount.append("and t.user.id = :id ");
			sqlData.append("and t.user.id = :id ");
		}

		Query queryCount = entityManager.createQuery(sqlCount.toString());
		Query queryData = entityManager.createQuery(sqlData.toString());
		if(userId != null){
			queryCount.setParameter("id", userId);
			queryData.setParameter("id", userId);
		}

		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());

		List<UserTeamReward> list = (List<UserTeamReward>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();

		data.setTotal(count);
		data.setRows(list);
		return data;
	}

}
