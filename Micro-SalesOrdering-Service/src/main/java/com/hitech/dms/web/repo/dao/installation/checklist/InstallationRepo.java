package com.hitech.dms.web.repo.dao.installation.checklist;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.dms.web.controller.installation.dto.InstallationSearchResponse;
import com.hitech.dms.web.controller.registrationsearch.dto.RegistrationSearchResponse;
import com.hitech.dms.web.entity.installation.InstallationEntity;

public interface InstallationRepo extends JpaRepository<InstallationEntity, Long> {

	@Query(value = "exec sp_get_installation_search_by_chassis :userCode,:chassisNo", nativeQuery = true)
    List<Map<String, Object>> autoCompleteChassisNo(@Param("userCode") String userCode,
                                                    @Param("chassisNo") String chassisNo);
	
	@Query(value = "exec DETAILS_ON_CHASSISNO :chassisNo", nativeQuery = true)
    Map<String, Object> vinDetailsByChassisNo(@Param("chassisNo") String chassisNo);
	
	@Query(value = "exec  sp_representative_type :userCode,:lookUpCode", nativeQuery = true)
    List<Map<String, Object>> getRepresentingList(@Param("userCode") String userCode,@Param("lookUpCode") String lookUpCode);
	
	@Query(value = "exec  sp_representative_type :userCode,:lookUpCode", nativeQuery = true)
    List<Map<String, Object>> getInstallationTypeList(@Param("userCode") String userCode,@Param("lookUpCode") String lookUpCode);
	
	@Query(value = "exec  sp_installation_done_by :userCode", nativeQuery = true)
    List<Map<String, Object>> installationDoneByList(@Param("userCode") String userCode);
	
	@Query(value = "exec SP_SERVCIE_SV_INSTALLATION_CHECK_POINTS :dealerId,:branchId,:userCode",nativeQuery = true)
    List<Map<String,Object>> getAllCheckpoint(@Param("dealerId") String dealerId,@Param("branchId") String branchId,@Param("userCode") String userCode);
	
	 @Query(value = "exec sp_installation_search :chassisNo,:installationNo,:fromDate,:toDate,:status,:page,:size,:userCode", nativeQuery = true)
	 List<InstallationSearchResponse> installationSearch(
	                                      @Param("chassisNo") String chassisNo,
	                                      @Param("installationNo") String installationNo,
	                                      @Param("fromDate") String fromDate,
	                                      @Param("toDate") String toDate,
	                                      @Param("status") String status,
	                                      @Param("page") Integer page,
	                                      @Param("size") Integer size,
	                                      @Param("userCode") String userCode
	                                    );
	
	
}
