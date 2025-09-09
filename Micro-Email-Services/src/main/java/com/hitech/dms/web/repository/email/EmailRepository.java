/**
 * 
 */
package com.hitech.dms.web.repository.email;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.web.entity.email.EmailEntity;

import feign.Param;

/**
 * @author santosh.kumar
 *
 */
@Repository
@Transactional
public interface EmailRepository extends JpaRepository<EmailEntity, BigInteger> {

	// List<Object[]> getEmailConfigDetailsWithUserGmail(@Param("userCode") String
	// userCode, @Param("targetDate") String targetDate);

	/**
	 * @param userCode
	 * @param targetDate
	 * @return
	 */
	@Query(value = "exec GET_EMAIL_CONFIG_DETAILS :todayDate ", nativeQuery = true)
	List<EmailEntity> getEmailConfigDetails( @Param String todayDate);

	/**
	 * @param mailItemId
	 * @return 
	 * @return
	 */
	//@Modifying
	@Query(value = "exec CONFIG_UDPATE_MAIL_STATUS :mailItemId ", nativeQuery = true)
	String updateMailStatus(@Param("mailItemId") BigInteger mailItemId);

}
