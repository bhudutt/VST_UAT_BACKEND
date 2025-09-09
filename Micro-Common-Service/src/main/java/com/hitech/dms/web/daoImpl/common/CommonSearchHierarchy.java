package com.hitech.dms.web.daoImpl.common;

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

import com.hitech.dms.web.model.common.response.CommonHoDetailResponse;
import com.hitech.dms.web.model.common.response.HoModel;
import com.hitech.dms.web.model.common.response.OrgHierBranchList;
import com.hitech.dms.web.model.common.response.OrgHierDealerList;
import com.hitech.dms.web.model.common.response.OrgHierLevelModel;
import com.hitech.dms.web.model.common.response.OrgHierStateModel;
import com.hitech.dms.web.model.common.response.OrgHierTerritoryModel;
import com.hitech.dms.web.model.common.response.OrgHierZoneModel;
import com.hitech.dms.web.model.common.response.ProfitCenterModel;

@Repository
public class CommonSearchHierarchy  implements com.hitech.dms.web.dao.common.CommonSearchHierarchy{

	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	 List<HoModel> hoModelList=null;
	 List<OrgHierZoneModel> zoneList=null;
	 List<OrgHierStateModel> StateList=null;
	 List<OrgHierTerritoryModel> territoryList=null;
	private static final Logger logger=LoggerFactory.getLogger(CommonSearchHierarchy.class);
	@Override
	public CommonHoDetailResponse fetchCommonHoOrgDetail(Integer flag, String userCode) {
		CommonHoDetailResponse response = new CommonHoDetailResponse();
		logger.info("fetchCommonHoOrgDetail "+flag+"userCode "+userCode);
		 List<ProfitCenterModel> pcList;
		// List<HoModel> hoModelList=null;
		 List<OrgHierLevelModel> orgHierLevelList=null;
		 List<OrgHierDealerList> orgHierDealerList=null;
		 List<OrgHierBranchList> orgHierBranchList=null;
		// List<OrgHierZoneModel> zoneList=null;
		 //List<OrgHierStateModel> StateList=null;
		 //List<OrgHierTerritoryModel> territoryList=null;
		Session session = null;
		Query query = null;
		String sqlQuery=null;
        sqlQuery="exec [CM_GET_COMMON_SEARCH_LIST] :FLAG,:UserCode";

		
		try
		{
			session = sessionFactory.openSession();
			pcList=getPcList(1, userCode, session);
			//hoModelList=getHoList(2, userCode, session);
			orgHierLevelList=getOrgList(3, userCode, session);
			orgHierDealerList=getOrgDealerList(6, userCode, session);
			orgHierBranchList=getOrgBranchList(7,userCode, session);
			response.setPcList(pcList);
			response.setHoModelList(hoModelList);
			response.setOrgHierLevelList(orgHierLevelList);
			response.setOrgHierDealerList(orgHierDealerList);
			response.setOrgHierBranchList(orgHierBranchList);
			response.setStateList(StateList);
			response.setZoneList(zoneList);
			response.setTerritoryList(territoryList);
			
			 
			  
			

				
				
				
			
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
	
	
	private   List<ProfitCenterModel>  getPcList(int flag, String userCode,Session session1)
	{
		logger.info("getPcList "+flag+"userCode "+userCode);
		List<ProfitCenterModel> pcList= null;
		Query query=null;
		Session session=null;
		 String SqlQuery=null;
		
		try
		{
			
			session = sessionFactory.openSession();
			SqlQuery="exec [CM_GET_COMMON_SEARCH_LIST] :FLAG,:UserCode";

			query = session.createSQLQuery(SqlQuery);
           query.setParameter("FLAG",flag);
           query.setParameter("UserCode", userCode);
           query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
           pcList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				 for (Object object : data) {
					Map row = (Map) object;
					
					ProfitCenterModel model= new ProfitCenterModel();
					model.setPcId((Integer)row.get("pc_id"));
					model.setPcCode((String)row.get("pc_code"));
					model.setPcDesc((String)row.get("pc_desc"));
					pcList.add(model);
					//response.setPcList(pcList);
						
						}
				 		}
				
				
			
		}catch (SQLGrammarException exp) {
			
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			
		} catch (Exception exp) {
			
		} finally {
			
			if(session!=null)
			{
				session.close();
			}
		}

		
		
		
		return pcList;
	}

	
	
	
	private   List<HoModel>  getHoList(int flag, String userCode,Session session1)
	{
		logger.info("getHoList "+flag+"userCode "+userCode);

		List<HoModel> hoModelList= null;
		Query query=null;
		String SqlQuery=null;
		Session session=null;
		
		try
		{
			session = sessionFactory.openSession();
			SqlQuery="exec [CM_GET_COMMON_SEARCH_LIST] :FLAG,:UserCode";
			query = session.createSQLQuery(SqlQuery);
           query.setParameter("FLAG",flag);
           query.setParameter("UserCode", userCode);
           query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
           hoModelList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				 for (Object object : data) {
					Map row = (Map) object;
					
						HoModel model = new HoModel();
						model.setUserIdVsOrgHierId((BigInteger)row.get("UsrID_vs_OrgHierID"));
						model.setHoUserId((BigInteger)row.get("ho_user_id"));
						model.setOrgHierarchyId((BigInteger)row.get("org_hierarchy_id"));
						hoModelList.add(model);
						//response.setHoModelList(hoModelList);
						
						}
				 		}
				
				
			
		}catch (SQLGrammarException exp) {
			
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			
		} catch (Exception exp) {
			
		} finally {
			
			if(session!=null)
			{
				session.close();
			}
		}

		
		
		
		return hoModelList;
	}
	private   List<OrgHierLevelModel>  getOrgList(int flag, String userCode,Session session1)
	{
		logger.info("getOrgList "+flag+"userCode "+userCode);
		hoModelList=new ArrayList<>();
		StateList= new ArrayList<>();
		zoneList= new ArrayList<>();
		territoryList= new ArrayList<>();

		 List<OrgHierLevelModel> orgList= null;
		
		Query query=null;
		 String SqlQuery=null;
		  Session session=null;
		try
		{
			session = sessionFactory.openSession();
			SqlQuery="exec [CM_GET_COMMON_SEARCH_LIST] :FLAG,:UserCode";

			query = session.createSQLQuery(SqlQuery);
           query.setParameter("FLAG",flag);
           query.setParameter("UserCode", userCode);
           query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
           orgList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				 for (Object object : data) {
					 	Map row = (Map) object;
					 	OrgHierLevelModel model= new OrgHierLevelModel();
						model.setOrgHierId((BigInteger)row.get("org_hierarchy_id"));
						Integer levelId=(Integer) row.get("level_id");
						System.out.println("levelId "+levelId);
							if(levelId==1)
							{
					   
								
							HoModel ho= new HoModel();
						   String heirarchyDesc=(String) row.get("hierarchy_desc");
						   System.out.println("hierarchyDesc"+heirarchyDesc);
						   ho.setHoName(heirarchyDesc);
						   model.setHierarchyDesc(heirarchyDesc);
						   hoModelList.add(ho);
						   
					   }
					   if(levelId==2)
					   {
						   
						   OrgHierZoneModel zone= new OrgHierZoneModel();
						   String heirarchyDesc=(String) row.get("hierarchy_desc");
						   zone.setZoneDesc(heirarchyDesc);
						   zoneList.add(zone);
						   model.setHierarchyDesc(heirarchyDesc);
						   
					   }
					   if(levelId==3)
					   {
						   OrgHierStateModel state= new OrgHierStateModel();
						   String heirarchyDesc=(String) row.get("hierarchy_desc");
						   state.setStateDesc(heirarchyDesc);
						   StateList.add(state);
						   model.setHierarchyDesc(heirarchyDesc);
						   
					   }
					   if(levelId==4)
					   {
						   OrgHierTerritoryModel territory= new OrgHierTerritoryModel();
						   String heirarchyDesc=(String) row.get("hierarchy_desc");
						   territory.setTerritoryDesc(heirarchyDesc);
						   territoryList.add(territory);
						   model.setHierarchyDesc(heirarchyDesc);
						   
					   }
						model.setLevelId((Integer)row.get("level_id"));
						model.setHierarchyCode((String)row.get("hierarchy_code"));
						model.setParentorgHierarchyId((BigInteger)row.get("parent_org_hierarchy_id"));
						orgList.add(model);
						
				 		}
				 
				 		}
				
			



				
			
		}catch (SQLGrammarException exp) {
			
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			
		} catch (Exception exp) {
			
		} finally {
			
			if(session!=null)
			{
				session.close();
			}
		}

		
		
		
		return orgList;

	}
	private   List<OrgHierDealerList>  getOrgDealerList(int flag, String userCode,Session session1)
	{
		logger.info("getOrgDealerList "+flag+"userCode "+userCode);

		List<OrgHierDealerList> dealerList= null;
		Query query=null;
		 String SqlQuery=null;
		 Session session =null;
		
		try
		{
			
			session = sessionFactory.openSession();
			SqlQuery="exec [CM_GET_COMMON_SEARCH_LIST] :FLAG,:UserCode";

			query = session.createSQLQuery(SqlQuery);
           query.setParameter("FLAG",flag);
           query.setParameter("UserCode",userCode);
           query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
           dealerList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				 for (Object object : data) {
					Map row = (Map) object;
					
						OrgHierDealerList model = new OrgHierDealerList();
						model.setDealerId((BigInteger)row.get("DEALER_ID"));
						model.setDealerCode((String) row.get("DEALER_CODE"));
						model.setDealerLocation((String) row.get("DEALER_LOCATION"));
						model.setDealerName((String)row.get("DEALER_NAME"));
						model.setDisplayName((String)row.get("DISPLAY_NAME"));
						dealerList.add(model);
						//response.setOrgHierDealerList(orgHierDealerList);
						
						}
				 		
				
				
			}
		}catch (SQLGrammarException exp) {
			
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			
		} catch (Exception exp) {
			
		} finally {
			
			if(session!=null)
			{
				session.close();
			}
		}

		
		
		
		return dealerList;

	}
	private   List<OrgHierBranchList>  getOrgBranchList(int flag, String userCode,Session session1)
	{
		logger.info("getOrgBranchList "+flag+"userCode "+userCode);
		List<OrgHierBranchList> branchList= null;
		Query query=null;
		 String SqlQuery=null;
		 Session session=null;
		
		try
		{
			session = sessionFactory.openSession();
			SqlQuery="exec [CM_GET_COMMON_SEARCH_LIST] :FLAG,:UserCode";

			query = session.createSQLQuery(SqlQuery);
           query.setParameter("FLAG",flag);
           query.setParameter("UserCode", userCode);
           query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
           branchList= new ArrayList<>();
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				 for (Object object : data) {
					Map row = (Map) object;
					
						OrgHierBranchList model = new OrgHierBranchList();
						model.setBranchId((BigInteger)row.get("BRANCH_ID"));
						model.setBranchCode((String)row.get("BranchCode"));
						model.setBranchName((String)row.get("BranchName"));
						model.setBranchLocation((String)row.get("BranchLocation"));
						model.setDisplayName((String)row.get("DISPLAY_NAME"));
						branchList.add(model);
						//response.setOrgHierBranchList(orgHierBranchList);
						
						}
				 		
				
				
			}
		}catch (SQLGrammarException exp) {
			
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
			
		} catch (Exception exp) {
			
		} finally {
			
			if(session!=null)
			{
				session.close();
			}
		}

		
		
		
		return branchList;

	}
	
	

}
