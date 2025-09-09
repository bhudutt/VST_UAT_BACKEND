package com.hitech.dms.web.dao.admin.dtl.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.dao.common.model.CommonHoDetailResponse;
import com.hitech.dms.web.dao.common.model.HoModel;
import com.hitech.dms.web.dao.common.model.OrgHierBranchList;
import com.hitech.dms.web.dao.common.model.OrgHierDealerList;
import com.hitech.dms.web.dao.common.model.OrgHierLevelModel;
import com.hitech.dms.web.dao.common.model.OrgHierStateModel;
import com.hitech.dms.web.dao.common.model.OrgHierTerritoryModel;
import com.hitech.dms.web.dao.common.model.OrgHierZoneModel;
import com.hitech.dms.web.dao.common.model.ProfitCenterModel;
import com.hitech.dms.web.model.admin.dtl.request.HoUserDTLRequestModel;
import com.hitech.dms.web.model.admin.dtl.response.HoUserDTLResponseModel;
import com.hitech.dms.web.model.admin.org.dtl.response.HoUserVSFieldRoleResponseModel;
import com.hitech.dms.web.model.user.role.response.UserRoleResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class HoUserDTLDaoImpl implements HoUserDTLDao {
	private static final Logger logger = LoggerFactory.getLogger(HoUserDTLDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public HoUserDTLResponseModel fetchHoUserDtl(String userCode, HoUserDTLRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchHoUserDtl invoked.." + userCode + " " + requestModel.toString());
			logger.debug("fetchHoUserDtl empcodeval invoked.."  + " " + requestModel.getEmpCodeVal());
			
		}
		Session session = null;
		Query query = null;
		HoUserDTLResponseModel responseModel = null;
		BigInteger userId = null;
		
		//change after UI changes
		//requestModel.setEmpCodeVal("DIGIT0001");
		
		String sqlQuery="";
		if(requestModel.getEmpCodeVal() !=null && requestModel.getEmpCodeVal().startsWith("DIGIT")) {
			sqlQuery=" select Digital_Source_ID as ho_usr_id,DigitalEmpCode as employee_code, "
					+ " ContactPersonName as employee_name,ContactPerson_No as Emp_ContactNo,ContactPerson_Email_id as Emp_Mail,IsActive  "
					+ " from SA_MST_ENQ_SOURCE_DIGITAL where Digital_Source_ID=:digitalSourceId";
		}else {
			sqlQuery = "select usrHo.*, AMD.DepartmentDesc, " + " AHDL.DesignationLevelDesc, AHMD.DesignationDesc, "
					+ " AU.user_id, AU.UserCode, AUVP.Password as password " + " from ADM_HO_USER (nolock) usrHo "
					+ " INNER JOIN ADM_MST_DEPARTMENT(nolock) AMD ON usrHo.department_id = AMD.department_id "
					+ " INNER JOIN ADM_HO_MST_DESIG (nolock) AHMD ON usrHo.ho_designation_id = AHMD.ho_designation_id "
					+ " INNER JOIN ADM_HO_MST_DESIG_LEVEL (nolock) AHDL ON AHDL.ho_designation_level_id = usrHo.ho_designation_level_id"
					+ " left join ADM_USER (nolock) AU ON usrHo.ho_usr_id = AU.ho_usr_id"
					+ " INNER JOIN ADM_USER_VS_PASSWORD(nolock) AUVP ON AUVP.UserCode = AU.UserCode where ";
		}
		
		
		if(requestModel.getEmpCodeVal() !=null && requestModel.getEmpCodeVal().startsWith("DIGIT")) {
			
		}else {
			if (requestModel.getHoUserId() != null) {
				sqlQuery = sqlQuery + " usrHo.ho_usr_id =:hoUserId";
			} else if (requestModel.getCriteria() != null && requestModel.getCriteria().equalsIgnoreCase("MOBILE_NO")) {
				sqlQuery = sqlQuery + " Emp_ContactNo =:criteriaText";
			} else {
				sqlQuery = sqlQuery + " employee_code =:criteriaText";
			}
		}
		
		
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
//			query.setParameter("userCode", userCode);
//			if (requestModel.getCriteria() != null && !requestModel.getCriteria().equals("")) {
//				query.setParameter("criteria", requestModel.getCriteria());
//			}
			
			if(requestModel.getEmpCodeVal() !=null && requestModel.getEmpCodeVal().startsWith("DIGIT")) {
				query.setParameter("digitalSourceId", requestModel.getHoUserId());
			}else {
				if (requestModel.getHoUserId() != null) {
					query.setParameter("hoUserId", requestModel.getHoUserId());
				} else {
					query.setParameter("criteriaText", requestModel.getCriteriaText());
				}
			}
			
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new HoUserDTLResponseModel();
					responseModel.setHoUserId((BigInteger) row.get("ho_usr_id"));
					responseModel.setEmployeeCode((String) row.get("employee_code"));
					responseModel.setEmployeeName((String) row.get("employee_name"));
					responseModel.setEmpContactNo((String) row.get("Emp_ContactNo"));
					responseModel.setDepartmentId((Integer) row.get("department_id"));
					responseModel.setDepartmentDesc((String) row.get("DepartmentDesc"));
					responseModel.setEmpMail((String) row.get("Emp_Mail"));
					responseModel.setHoDesignationId((Integer) row.get("ho_designation_id"));
					responseModel.setDesignationDesc((String) row.get("DesignationDesc"));
					responseModel.setHoDesignationLevelId((Integer) row.get("ho_designation_level_id"));
					responseModel.setDesignationLevelDesc((String) row.get("DesignationLevelDesc"));
					
					  Character isActive = (Character) row.get("IsActive"); if (isActive != null &&
					  isActive.toString().equals("Y")) { responseModel.setIsActive(true); } else {
					  responseModel.setIsActive(false); }
					 
					responseModel.setUserCode((String) row.get("UserCode"));
					responseModel.setUserId((BigInteger) row.get("user_id"));
					responseModel.setPassword((String)row.get("password"));
					
					Integer userForVal=(Integer) row.get("userForId");
					
					if(userForVal !=null && userForVal>0) {
						responseModel.setUserForId(userForVal);
					}else {
						responseModel.setUserForId(1);
					}
					
					responseModel.setPlatformId((Integer) row.get("platformId"));
					
					
					

					List<HoUserVSFieldRoleResponseModel> hoUserFieldRoleOrgList = fetchHoUserFieldRoleList(session,
							responseModel.getHoUserId());
					responseModel.setHoUserFieldRoleOrgList(hoUserFieldRoleOrgList);

					userId = responseModel.getUserId();
				}
			} else {
				// user not found
				responseModel = new HoUserDTLResponseModel();
				responseModel.setMsg("AVAILABLE");
			}
			List<UserRoleResponseModel> userRoleList = commonDao.fetchUserRoleList(session, userCode,
					WebConstants.ADMIN, userId);
			responseModel.setUserRoleList(userRoleList);
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<HoUserVSFieldRoleResponseModel> fetchHoUserFieldRoleList(Session session, BigInteger hoUserId) {
		List<HoUserVSFieldRoleResponseModel> hoUserFieldRoleOrgList = null;
		HoUserVSFieldRoleResponseModel fieldRoleModel = null;
		String sqlQuery = "select fRole.org_hierarchy_id, fRole.UsrID_vs_OrgHierID, fRole.ho_usr_id, fRole.IsActive, firelRoleHier.hierarchy_desc, ABMPC.pc_desc, AMD.DepartmentDesc "
				+ " from ADM_HO_USER_ORGHIER (nolock) fRole "
				+ " inner join ADM_HO_MST_ORG_LEVEL_HIER (nolock) firelRoleHier on firelRoleHier.org_hierarchy_id = fRole.org_hierarchy_id "
				+ " inner join ADM_HO_MST_ORG_LEVEL (nolock) AHMOL on firelRoleHier.level_id = AHMOL.level_id "
				+ " inner join ADM_BP_MST_PROFIT_CENTER (nolock) ABMPC on AHMOL.pc_id = ABMPC.pc_id "
				+ " inner join ADM_MST_DEPARTMENT (nolock) AMD on AHMOL.department_id = AMD.department_id "
				+ " where fRole.ho_usr_id =:hoUserID";
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("hoUserID", hoUserId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			hoUserFieldRoleOrgList = new ArrayList<HoUserVSFieldRoleResponseModel>();
			if (data != null && !data.isEmpty()) {
				for (Object object1 : data) {
					Map row1 = (Map) object1;
					fieldRoleModel = new HoUserVSFieldRoleResponseModel();
					fieldRoleModel.setOrgDesc((String) row1.get("hierarchy_desc"));
					fieldRoleModel.setOrgId((BigInteger) row1.get("org_hierarchy_id"));
					fieldRoleModel.setUsrIdVsOrdId((BigInteger) row1.get("UsrID_vs_OrgHierID"));
					fieldRoleModel.setHoUserId((BigInteger) row1.get("ho_usr_id"));
					fieldRoleModel.setPcDesc((String) row1.get("pc_desc"));
					fieldRoleModel.setDepartmentDesc((String) row1.get("DepartmentDesc"));
					Character isActive = (Character) row1.get("IsActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						fieldRoleModel.setIsActive(true);
					} else {
						fieldRoleModel.setIsActive(false);
					}
					hoUserFieldRoleOrgList.add(fieldRoleModel);
				}
			}
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			sqlQuery = null;
		}
		return hoUserFieldRoleOrgList;
	}

	@Override
	public CommonHoDetailResponse fetchCommonHoOrgDetail(Integer flag, String userCode) {
		
		CommonHoDetailResponse response = new CommonHoDetailResponse();
		 List<ProfitCenterModel> pcList;
		 List<HoModel> hoModelList=null;
		 List<OrgHierLevelModel> orgHierLevelList=null;
		 List<OrgHierDealerList> orgHierDealerList=null;
		 List<OrgHierBranchList> orgHierBranchList=null;
		 List<OrgHierZoneModel> zoneList=null;
		 List<OrgHierStateModel> StateList=null;
		 List<OrgHierTerritoryModel> territoryList=null;
		Session session = null;
		Query query = null;
		String sqlQuery=null;
		
		try
		{
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
            sqlQuery="exec [CM_GET_COMMON_SEARCH_LIST] :FLAG,:UserCode";
            query.setParameter("FLAG",flag);
            query.setParameter("UserCode", userCode);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            pcList= new ArrayList<>();
			hoModelList= new ArrayList<>();
			orgHierLevelList= new ArrayList<>();
			orgHierDealerList= new ArrayList<>();
			orgHierBranchList= new ArrayList<>();
			zoneList= new ArrayList<>();
			StateList= new ArrayList<>();
			territoryList= new ArrayList<>();
			for(int i=1;i<=5;i++)
			{
				System.out.println("in loop "+i);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				//responseModelList = new ArrayList<StoreSearchResponseModel>();
				
				for (Object object : data) {
					Map row = (Map) object;
					if(i==1)
					{
						
						ProfitCenterModel model= new ProfitCenterModel();
						model.setPcId((Integer)row.get("pc_id"));
						model.setPcCode((String)row.get("pc_code"));
						model.setPcDesc((String)row.get("pc_desc"));
						pcList.add(model);
						response.setPcList(pcList);
						System.out.println("pcList "+pcList);
						
						
					}
					else if(i==2)
					{
						HoModel model = new HoModel();
						model.setUserIdVsOrgHierId((BigInteger)row.get("UsrID_vs_OrgHierID"));
						model.setHoUserId((BigInteger)row.get("ho_user_id"));
						model.setOrgHierarchyId((BigInteger)row.get("org_hierarchy_id"));
						hoModelList.add(model);
						response.setHoModelList(hoModelList);
						System.out.println("hoModelList "+hoModelList);

						//model.setIsActive();
					}
					else if(i==3)
					{
						OrgHierLevelModel model= new OrgHierLevelModel();
						model.setOrgHierId((BigInteger)row.get("org_hierarchy_id"));
						model.setLevelId((Integer)row.get("level_id"));
						model.setHierarchyCode((String)row.get("hierarchy_code"));
						model.setHierarchyDesc((String)row.get("hierarchy_desc"));
						model.setParentorgHierarchyId((BigInteger)row.get("parent_org_hierarchy_id"));
						orgHierLevelList.add(model);
						response.setOrgHierLevelList(orgHierLevelList);
						System.out.println("orgHierLevelList "+orgHierLevelList);

						
					}
					else if(i==4)
					{
						OrgHierDealerList model = new OrgHierDealerList();
						model.setDealerId((BigInteger)row.get("DEALER_ID"));
						model.setDealerCode((String) row.get("DEALER_CODE"));
						model.setDealerLocation((String) row.get("DEALER_LOCATION"));
						model.setDealerName((String)row.get("DEALER_NAME"));
						model.setDisplayName((String)row.get("DISPLAY_NAME"));
						orgHierDealerList.add(model);
						response.setOrgHierDealerList(orgHierDealerList);
						System.out.println("orgHierDealerList "+orgHierDealerList);

						
						
					}
					else if(i==5)
					{
						
						OrgHierBranchList model = new OrgHierBranchList();
						model.setBranchId((BigInteger)row.get("BRANCH_ID"));
						model.setBranchCode((String)row.get("BranchCode"));
						model.setBranchName((String)row.get("BranchName"));
						model.setBranchLocation((String)row.get("BranchLocation"));
						model.setDisplayName((String)row.get("DISPLAY_NAME"));
						orgHierBranchList.add(model);
						response.setOrgHierBranchList(orgHierBranchList);
						System.out.println("orgHierBranchList "+orgHierBranchList);





					}
					
					
					
				}
				
				
			}
			}
			

			
			
		}catch (SQLGrammarException exp) {
			response.setStatusCode(301);
			response.setStatusMessage(exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			response.setStatusCode(301);
			response.setStatusMessage(exp.getMessage());
		} catch (Exception exp) {
			response.setStatusCode(301);
			response.setStatusMessage(exp.getMessage());
		} finally {
			
			if(session!=null)
			{
				session.close();
			}
		}

		
		System.out.println("before send response "+response);
		
		return response;

	}
}
