package com.gedoumi.asset.dao;

import com.gedoumi.asset.entity.UserReward;
import com.gedoumi.asset.entity.UserTeamReward;
import com.gedoumi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface UserRewardDao extends JpaRepository<UserReward, Long> {

     List<UserReward> findByRemainFrozenAfter(BigDecimal remainAsset);


}
