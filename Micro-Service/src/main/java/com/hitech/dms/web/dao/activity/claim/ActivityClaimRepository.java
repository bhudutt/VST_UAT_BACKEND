/**
 * 
 */
package com.hitech.dms.web.dao.activity.claim;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.entity.activityclaim.ActivityClaimEntity;

/**
 * @author santosh.kumar
 *
 */
//@Repository
public interface ActivityClaimRepository extends JpaRepository<ActivityClaimEntity, Integer>{

	/**
	 * @param userCode
	 * @param acIntegertivityPlanId
	 * @return
	 */
	//@Query(value = "exec SV_Activity_details_For_Claim :UserCode,:ActivityPlanId", nativeQuery = true)
	//List<Map<String, Object>> getActivityDetailsList(@Param("UserCode") String userCode,@Param("ActivityPlanId") Integer acIntegertivityPlanId);
}
