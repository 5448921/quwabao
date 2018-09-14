package com.gedoumi.asset.service;

import com.gedoumi.asset.dao.UserRentDao;
import com.gedoumi.asset.entity.UserRent;
import com.gedoumi.common.AppConfig;
import com.gedoumi.common.enums.RentStatus;
import com.gedoumi.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class UserRentService {

	Logger logger = LoggerFactory.getLogger(UserRentService.class);
	


	@Resource
	private UserRentDao userRentDao;

	@Resource
	private AppConfig appConfig;


	@PersistenceContext
	private EntityManager entityManager;

	public UserRent getById(Long id){
		return userRentDao.findOne(id);
	}

	public UserRent getAcviteByUser(User user){
		List<UserRent> userRents = userRentDao.findByUserAndRentStatus(user, RentStatus.Active.getValue());
		if(CollectionUtils.isEmpty(userRents)){
			return null;
		}
		return userRents.get(0);
	}

	public List<UserRent> getByRentType(int rentType){
		return userRentDao.findByRentType(rentType);
	}

}
