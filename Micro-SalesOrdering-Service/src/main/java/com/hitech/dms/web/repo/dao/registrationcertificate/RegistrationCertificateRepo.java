package com.hitech.dms.web.repo.dao.registrationcertificate;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.dms.web.controller.registrationsearch.dto.RegistrationSearchResponse;
import com.hitech.dms.web.entity.registratoncertificate.RegistrationCertificateEntity;

public interface RegistrationCertificateRepo extends JpaRepository<RegistrationCertificateEntity, Long> {

	@Query(value = "exec sp_get_rc_search_by_chassis :userCode,:chassisNo", nativeQuery = true)
    List<Map<String, Object>> autoCompleteChassisNo(@Param("userCode") String userCode,
                                                    @Param("chassisNo") String chassisNo);
	
	@Query(value = "exec DETAILS_ON_CHASSISNO :chassisNo", nativeQuery = true)
    Map<String, Object> detailsByChassisNo(@Param("chassisNo") String chassisNo);
	
	 @Query(value = "exec sp_registration_search :chassisNo,:registrationNo,:page,:size,:userCode,:selectedValue", nativeQuery = true)
	 List<RegistrationSearchResponse> rcSearch(
	                                      @Param("chassisNo") String chassisNo,
	                                      @Param("registrationNo") String registrationNo,
	                                      @Param("page") Integer page,
	                                      @Param("size") Integer size,
	                                      @Param("userCode") String userCode,
	                                      @Param("selectedValue") String selectedValue
	                                    );
}
