package com.hitech.dms.web.daoImpl.spare.creditDebit;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.dao.spare.creditDebit.CreditDebitNoteDao;
import com.hitech.dms.web.entity.common.SystemLookUpEntity;
import com.hitech.dms.web.entity.spare.creditDebit.note.CreditDebitNoteEntity;
import com.hitech.dms.web.model.spara.creditDebit.note.request.FilterCreditDebitNoteReq;
import com.hitech.dms.web.model.spara.creditDebit.note.response.CreditDebitNoteReponse;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

@Repository
@Transactional
public class CreditDebitNoteDaoImpl implements CreditDebitNoteDao {
	
private static final Logger logger = LoggerFactory.getLogger(CreditDebitNoteDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	 

		@Override
		public List<PaymentVoucherList> getCreditAndDebitType(String lookupTypeCode) {
			
		List<PaymentVoucherList> finalList =null;
		PaymentVoucherList res = null;
		 CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		 CriteriaQuery<SystemLookUpEntity> criteria = builder.createQuery(SystemLookUpEntity.class);
		 Root<SystemLookUpEntity> postRoot =  criteria.from(SystemLookUpEntity.class);
		 criteria.select(postRoot).where(builder.equal(postRoot.get("lookTypeCode"), lookupTypeCode));
		 List<SystemLookUpEntity> list = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
		 if(!list.isEmpty()) {
			 finalList = new ArrayList<>();
			 for(SystemLookUpEntity bean:list) {
				 res = new PaymentVoucherList();
				 res.setValueId(bean.getLookUpId());
				 res.setValueCode(bean.getLookupVal());
				 res.setDisplayValue(bean.getDisplayOrder());
				 finalList.add(res);
			 }
		 }
		  return finalList;
		}

		

		@Override
		public BigInteger save(CreditDebitNoteEntity entity, String userCode) {
			 Map<String, Object> mapData = null;
			 BigInteger userId = null;
			Session session = this.sessionFactory.getCurrentSession();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			entity.setCreatedBy(userId);
			entity.setCreatedDate(new Date());
			return (BigInteger) session.save(entity);
		}

		
		@Transactional(readOnly = true)
		public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
			Map<String, Object> mapData = new HashMap<>();
			NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("Select user_id from ADM_USER where UserCode =:userCode");
			 query.setReadOnly(true);
			 query.setParameter("userCode", userCode);
			 BigInteger id = (BigInteger) query.uniqueResult();
			 mapData.put("userId", id);
			 mapData.put("SUCCESS", "FETCHED");
			return mapData;
		}



		@Override
		@Transactional(readOnly = true)
		public List<PartyCategoryResponse> partyTypeList() {
			PartyCategoryResponse bean=null;
			List<PartyCategoryResponse> beans=null;
			 NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("select party_category_id, PartyCategoryCode, PartyCategoryName from ADM_BP_MST_PARTY_CTGRY where IsActive = :IsActive");
			 query.setReadOnly(true);
			 query.setParameter("IsActive", 'Y');
			 List<Object[]> list = query.list();
			 if(!list.isEmpty()) {
			  beans=new ArrayList<>();
			 for (Object[] arr : list) {
					bean = new PartyCategoryResponse();
					bean.setPartyCategoryId((BigInteger)arr[0]);
					bean.setPartyCategoryCode((String)arr[1]);
					bean.setPartyCategoryName((String)arr[2]);
					beans.add(bean);
				}
		 }
				return beans;
		       
		}
		

		@Override
		public List<SaveResponse> searchCreditDebitNumber(String searchText, String userCode) {
			
			SaveResponse bean=null;
			List<SaveResponse> beans=null;
			
			 Map<String, Object> mapData = null;
			 BigInteger userId = null;
			Session session = this.sessionFactory.getCurrentSession();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			 NativeQuery query = session.createNativeQuery("select Cr_Dr_id, Cr_Dr_No from PA_CREDIT_DEBIT_NOTE where Cr_Dr_No like :crDrNo and CreatedBy=:userId");
			 query.setReadOnly(true);
			 query.setParameter("crDrNo",  "%" + searchText.toLowerCase() + "%");
			 query.setParameter("userId", userId);
			 List<Object[]> list = query.list();
			 if(!list.isEmpty()) {
			  beans=new ArrayList<>();
			 for (Object[] arr : list) {
					bean = new SaveResponse();
					bean.setId((BigInteger)arr[0]);
					bean.setNumber((String)arr[1]);
					beans.add(bean);
				}
		 }
				return beans;
		       
		}


		
		@Override
		@Transactional(readOnly = true)
		public List<?> filter(@Valid FilterCreditDebitNoteReq requestModel, String userCode) {
		    String sqlQuery = "exec [SP_CREDIT_DEBIT_SEARCH]  :dealerId, :branchId, :stateId, :pcId, :userCode, :creditDebitNo, :startDate, :toDate, :page, :size";
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    Query<?> query = null;
		    List<?> data = null;
		    try {
		    	Session session = sessionFactory.getCurrentSession();
		        query = session.createSQLQuery(sqlQuery);
		        query.setParameter("dealerId", requestModel.getDealerId());
				query.setParameter("branchId", requestModel.getBranchId());
				query.setParameter("stateId", requestModel.getStateId());
				query.setParameter("pcId", requestModel.getPcId());
				query.setParameter("userCode", userCode);
		        query.setParameter("creditDebitNo", requestModel.getCreditDebitNo());
		        query.setParameter("startDate", formatter.format(requestModel.getFromDate()));
		        query.setParameter("toDate", formatter.format(requestModel.getToDate()));
		        query.setParameter("page", requestModel.getPage());
		        query.setParameter("size", requestModel.getSize());
		        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		        data = query.list();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return data;
		}



		@Override
		public List<?> viewCreditDebitDetail(BigInteger creditDebitNoteId, String userCode) {
			
			 String sqlQuery = "exec [SP_CREDIT_DEBIT_VIEW_TEST] :creditDebitNoteId, :userCode";
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			    Query<?> query = null;
			    List<?> data = null;
			    try {
			    	Session session = sessionFactory.getCurrentSession();
			        query = session.createSQLQuery(sqlQuery);
			        query.setParameter("creditDebitNoteId", creditDebitNoteId);
			        query.setParameter("userCode", userCode);
			      //  query.setParameter("flag", 0);
			        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			        data = query.list();
			    } catch (Exception e) {
			        e.printStackTrace();
			    }

			    return data;
		}
		

		
		

}
