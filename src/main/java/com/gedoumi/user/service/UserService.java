package com.gedoumi.user.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.alibaba.fastjson.JSON;
import com.gedoumi.api.face.FaceApi;
import com.gedoumi.api.face.FaceApiResponse;
import com.gedoumi.api.face.IDApiResponse;
import com.gedoumi.api.face.vo.FaceVO;
import com.gedoumi.asset.dao.UserAssetDao;
import com.gedoumi.asset.entity.UserAsset;
import com.gedoumi.asset.vo.UserInfoVO;
import com.gedoumi.asset.vo.UserTeamVO;
import com.gedoumi.common.enums.CodeEnum;
import com.gedoumi.common.enums.UserStatus;
import com.gedoumi.common.enums.UserValidateStatus;
import com.gedoumi.common.exception.BusinessException;
import com.gedoumi.sys.dao.SysSmsDao;
import com.gedoumi.user.dao.UserImageDao;
import com.gedoumi.user.dao.UserTreeDao;
import com.gedoumi.user.entity.UserImage;
import com.gedoumi.user.entity.UserTree;
import com.gedoumi.user.entity.User;
import com.gedoumi.user.vo.ValidateUserVO;
import com.gedoumi.util.CipherUtils;
import com.gedoumi.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gedoumi.common.base.DataGrid;
import com.gedoumi.common.base.PageParam;
import com.gedoumi.user.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * 类名：SysUserService
 * 功能：用户管理 业务层
 *
 */
@Service
public class UserService {

	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Resource
	private UserDao userDao;

	@Resource
	private SysSmsDao smsDao;

	@Resource
	private UserTreeDao userTreeDao;

	@Resource
	private UserImageDao userImageDao;

	@Resource
	private UserAssetDao assetDao;
	

	@PersistenceContext  
    private EntityManager entityManager;

	@Transactional
	public void addUser(User user) throws BusinessException {
		user.setInviteCode(CipherUtils.generateCode());
		while (true){
			User orgUser = userDao.findByInviteCode(user.getInviteCode());
			if(orgUser == null){
				break;
			}
			user.setInviteCode(CipherUtils.generateCode());
		}
		userDao.save(user);
		if(StringUtils.isEmpty(user.getUsername())){
			int length = String.valueOf(user.getId()).length();
			length = length > 4 ? length : 4;
			String format = "%0" + length + "d";
			user.setUsername(User.PREFIX+NumberUtil.randomInt(0,999)+String.format(format,user.getId()));
			userDao.save(user);
		}


		smsDao.updateSmsStatus(user.getMobilePhone());

		bindRegInviteCode(user);

		Date now = new Date();
		UserAsset userAsset = new UserAsset();
		userAsset.setUpdateTime(now);
		userAsset.setCreateTime(now);
		userAsset.setInitFrozenAsset(BigDecimal.ZERO);
		userAsset.setProfit(BigDecimal.ZERO);
		userAsset.setRemainAsset(BigDecimal.ZERO);
		userAsset.setInitBaseAsset(BigDecimal.ZERO);
		userAsset.setFrozenAsset(BigDecimal.ZERO);
		userAsset.setTotalAsset(BigDecimal.ZERO);
		userAsset.setUser(user);

		assetDao.save(userAsset);
	}
	
	public List<User> getAll(){
		return userDao.findAll();
	}

	/**
	 * 好友列表，2级
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataGrid getList(PageParam param, User user){
		DataGrid data=new DataGrid();
		String prefSql = " from UserTree t where t.parent.id " +
				"in (select u.child.id from UserTree u where u.parent.id=?) " +
				"or t.parent.id=? ";
		StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
		StringBuffer sqlData = new StringBuffer("select t.child ").append(prefSql);


		Query queryCount = entityManager.createQuery(sqlCount.toString());
		queryCount.setParameter(1, user.getId());
		queryCount.setParameter(2, user.getId());
		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());
		Query queryData = entityManager.createQuery(sqlData.toString());
		queryData.setParameter(1, user.getId());
		queryData.setParameter(2, user.getId());
		List<User> list = (List<User>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();

		data.setTotal(count);
		data.setRows(list);
		return data;
	}


	/**
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public DataGrid getPageList(PageParam param, UserInfoVO user){
		DataGrid data=new DataGrid();
		String prefSql = " from User t ";
		StringBuffer sqlCount = new StringBuffer("select count(t.id)  ").append(prefSql);
		StringBuffer sqlData = new StringBuffer("select t ").append(prefSql);

		sqlCount.append("where 1=1 ");
		sqlData.append("where 1=1 ");

		if(StringUtils.isNotEmpty(user.getMobilePhone())){
			sqlCount.append("and t.mobilePhone like :phone ");
			sqlData.append("and t.mobilePhone like :phone ");
		}

		Query queryCount = entityManager.createQuery(sqlCount.toString());
		Query queryData = entityManager.createQuery(sqlData.toString());
		if(StringUtils.isNotEmpty(user.getMobilePhone())){
			queryCount.setParameter("phone", "%"+user.getMobilePhone()+"%");
			queryData.setParameter("phone", "%"+user.getMobilePhone()+"%");
		}

		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());


		List<User> list = (List<User>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();

		data.setTotal(count);
		data.setRows(list);
		return data;
	}
	
	public User getById(Long id){
		return userDao.findOne(id);
	}
	
	public User create(User user){
		if(StringUtils.isEmpty(user.getPassword()))
			user.setPassword(new Md5Hash(User.PWD_INIT, user.getMobilePhone()).toString());
		User loginUser = (User)SecurityUtils.getSubject().getSession().getAttribute("user");
		return userDao.save(user);
	}
	
	public User update(Long id, User user){
		User oldUser = userDao.findOne(id);
		oldUser.setRealName(user.getRealName());
		oldUser.setMobilePhone(user.getMobilePhone());
		oldUser.setUserStatus(user.getUserStatus());
		User loginUser = (User)SecurityUtils.getSubject().getSession().getAttribute("user");
		oldUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		return userDao.save(oldUser);
	}
	
	public User update(User user){
		return userDao.save(user);
	}
	
	public Boolean delete(String[] ids){

		return true;
	}
	
	public Boolean delete(Long id){
		userDao.delete(id);
		return true;
	}
	

	
	public User getByUsername(String username){
		User user = userDao.findByUsername(username);
		return user;
	}

	public User getByIdCard(String idCard){
		User user = userDao.findByIdCard(idCard);
		return user;
	}


	public User getByMobilePhone(String mobile){
		User user = userDao.findByMobilePhone(mobile);
		return user;
	}

	public User getByToken(String token){
		User user = userDao.findByToken(token);
		return user;
	}

	public User getByInviteCode(String inviteCode){
		User user = userDao.findByInviteCode(inviteCode);
		return user;
	}
	
	public User checkLoginUser(String username, UserStatus userStatus){
		User user = userDao.findByMobilePhoneAndUserStatus(username, userStatus.getValue());
		return user;
	}

	@Transactional
	public void updateLoginInfo(User user){
		user.setErrorCount(Short.valueOf("0"));
		user.setUpdateTime(new Date());
		userDao.save(user);
	}

	@Transactional
	public void updateLogout(User user){
		user.setUpdateTime(new Date());
		user.setToken(UUID.randomUUID().toString());
		userDao.save(user);
	}

	@Transactional
	public void updateLoginError(User user){
		Short errorCount = user.getErrorCount();
		errorCount++;
		user.setErrorCount(errorCount);
		user.setErrorTime(new Date());
		userDao.save(user);
	}

	public void checkRegInviteCode(User user, Long checkUserid) throws BusinessException {
		List<UserTree> userTreeList = userTreeDao.findByParent(user);
		for (UserTree userTree : userTreeList) {
			if(userTree.getChild().getId().equals(checkUserid)){
				throw new BusinessException(CodeEnum.BindInviteCodeError);
			}
			checkRegInviteCode(userTree.getChild(),checkUserid);
		}

	}

	@Transactional
	public void bindRegInviteCode(User user) throws BusinessException {

		User parentUser = userDao.findByInviteCode(user.getRegInviteCode());
		if(parentUser != null){
			checkRegInviteCode(user, parentUser.getId());
		}


		userDao.save(user);

		if(parentUser != null){
			UserTree userTree = new UserTree();
			userTree.setChild(user);
			userTree.setParent(parentUser);
			userTreeDao.save(userTree);
		}


	}

	@Transactional
	public void validateUser(ValidateUserVO validateUserVO, IDApiResponse faceApiResponse){
		if(faceApiResponse != null)
			logger.info("validateUser faceApiResponse = {}", JSON.toJSON(faceApiResponse));
		Date now = new Date();
		Long userId = validateUserVO.getUserId();
		UserImage orgUserImage = userImageDao.findByUserId(userId);
		if(orgUserImage != null){
			orgUserImage.setUserImage(StringUtils.EMPTY);
//			orgUserImage.setUserImage(validateUserVO.getBase64Img());
			orgUserImage.setUpdateTime(now);
			orgUserImage.setMessage(faceApiResponse.getData().getMessage());
			orgUserImage.setValidateCode(faceApiResponse.getData().getCode());
			userImageDao.save(orgUserImage);
		}else {
			UserImage userImage = new UserImage();
			userImage.setUserId(validateUserVO.getUserId());
//			userImage.setUserImage(validateUserVO.getBase64Img());
			userImage.setUserImage(StringUtils.EMPTY);
			userImage.setCreateTime(now);
			userImage.setUpdateTime(now);
			userImage.setValidateCode(faceApiResponse.getData().getCode());
			userImage.setScore(StringUtils.EMPTY);
			userImage.setMessage(faceApiResponse.getData().getMessage());
			userImageDao.save(userImage);
		}

		if(faceApiResponse != null){
			User user = userDao.findOne(userId);
			user.setUpdateTime(now);
			user.setRealName(validateUserVO.getRealName());
			user.setIdCard(validateUserVO.getIdCard());
			if(faceApiResponse.isSucess()){
				user.setValidateStatus(UserValidateStatus.Pass.getValue());
			}else {
				user.setValidateStatus(UserValidateStatus.UnPass.getValue());
			}

			userDao.save(user);
		}

	}

	public UserImage getUserImg(Long userId){
		return userImageDao.findByUserId(userId);
	}

}
