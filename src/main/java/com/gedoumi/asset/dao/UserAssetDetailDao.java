package com.gedoumi.asset.dao;

import com.gedoumi.asset.entity.UserAssetDetail;
import com.gedoumi.asset.entity.UserRent;
import com.gedoumi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserAssetDetailDao extends JpaRepository<UserAssetDetail, Long> {

        List<UserAssetDetail> findByUserRent(UserRent userRent);

        UserAssetDetail findByUserRentAndDigDateAndTransType(UserRent userRent, Date digDate, int transType);

        UserAssetDetail findByApiTransSeq(String apiTransSeq);

        List<UserAssetDetail> findByUserAndDigDate(User user, Date digDate);
        List<UserAssetDetail> findByUserAndRewardUserAndDigDate(User user, User rewardUser, Date digDate);

        List<UserAssetDetail> findByUserAndTransTypeAndCreateTimeAfter(User user, int transType, Date createTime);
}
