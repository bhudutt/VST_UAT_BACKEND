package com.hitech.dms.web.repo.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.dms.web.entity.user.UserCustomerEntity;

public interface UserCustomerRepository extends JpaRepository<UserCustomerEntity, Long>
{
	@Override
	Page<UserCustomerEntity> findAll(Pageable pageable);
	Optional<UserCustomerEntity> findByEmail(String email);
    Page<UserCustomerEntity> findByEmailContains(String email, Pageable pageable);
    Page<UserCustomerEntity> findAllByEmail(String email, Pageable pageable);
    Page<UserCustomerEntity> findAllByEmailContainsAndEmail(String email, String auth, Pageable pageable);

    Boolean existsByEmail(String email);
}
