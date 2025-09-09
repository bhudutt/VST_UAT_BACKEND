package com.hitech.dms.web.repo.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hitech.dms.web.entity.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByUsername(String username);
	@Query(value = "select * from ADM_USER where UserCode= :username", nativeQuery = true)
	Optional<UserEntity> findByUsercode(String username);
}
