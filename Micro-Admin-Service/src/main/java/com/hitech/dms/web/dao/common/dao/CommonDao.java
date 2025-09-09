package com.hitech.dms.web.dao.common.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.model.department.list.response.DepartmentListResponseModel;
import com.hitech.dms.web.model.desig.list.response.DealerDesiginationResponseModel;
import com.hitech.dms.web.model.desig.list.response.DesiginationLevelResponseModel;
import com.hitech.dms.web.model.desig.list.response.DesiginationResponseModel;
import com.hitech.dms.web.model.user.role.response.UserRoleResponseModel;

public interface CommonDao {
	public List<UserRoleResponseModel> fetchUserRoleList(String userCode, String isFor, BigInteger userId);

	public List<UserRoleResponseModel> fetchUserRoleList(Session session, String userCode, String isFor,
			BigInteger userId);
	
	public List<DepartmentListResponseModel> fetchDepartmentList(String userCode, String applicableForDealer,
			String isActive);
	
	public List<DesiginationResponseModel> fetchDesigList(String userCode, Integer departmentId);
	
	public List<DesiginationLevelResponseModel> fetchDesigLevelList(String userCode, Integer departmentId);
	
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);
	
	public List<DealerDesiginationResponseModel> fetchDealerDesigList(String userCode, Integer dealerDepartmentId);

	/**
	 * @param userCode
	 * @param dealerCode
	 * @return
	 */
	public String getPartyIdFromdealercode(String userCode, Integer dealerCode);
}
