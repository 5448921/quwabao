package com.gedoumi.sys.service;

import com.gedoumi.asset.entity.UserTeam;
import com.gedoumi.asset.vo.SysRentVO;
import com.gedoumi.common.Constants;
import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.common.enums.RentStatus;
import com.gedoumi.sys.dao.SysRentDao;
import com.gedoumi.sys.entity.SysRent;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class SysQueryService {

	@PersistenceContext
	private EntityManager entityManager;


	public List getList(String queryStr){
		if(!StringUtils.containsIgnoreCase(queryStr,"limit")){
			queryStr = queryStr + " limit 1000";
		}
		Query queryData = entityManager.createNativeQuery(queryStr);

		queryData.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = (List)queryData.getResultList();


		return list;
	}



}
