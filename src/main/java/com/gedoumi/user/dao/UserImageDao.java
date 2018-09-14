package com.gedoumi.user.dao;

import com.gedoumi.user.entity.User;
import com.gedoumi.user.entity.UserImage;
import com.gedoumi.user.entity.UserTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserImageDao extends JpaRepository<UserImage, Long> {

	public UserImage findByUserId(Long userId);

}
