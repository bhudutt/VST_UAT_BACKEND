package com.hitech.dms.web.daoImpl.spare.paymentVoucher;

import java.math.BigDecimal;
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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.paymentVoucher.PaymentVoucherDao;
import com.hitech.dms.web.entity.common.SystemLookUpEntity;
import com.hitech.dms.web.entity.spare.payment.voucher.PaymentVoucherDtlEntity;
import com.hitech.dms.web.entity.spare.payment.voucher.PaymentVoucherGrnInvoiceDtlEntity;
import com.hitech.dms.web.entity.spare.payment.voucher.PaymentVoucherHdrEntity;
import com.hitech.dms.web.model.spara.creditDebit.note.response.SaveResponse;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeListResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.CreatePayVoucherRequest;
import com.hitech.dms.web.model.spara.payment.voucher.request.FilterPaymentVoucherReq;
import com.hitech.dms.web.model.spara.payment.voucher.request.PVpartyCodeRequestModel;
import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocForGrnInvReq;
import com.hitech.dms.web.model.spara.payment.voucher.request.RefDocListRequest;
import com.hitech.dms.web.model.spara.payment.voucher.response.PVGrnAndInvoiceResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PayVoucherResponse;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentBankList;
import com.hitech.dms.web.model.spara.payment.voucher.response.PaymentVoucherList;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;

@Repository
@Transactional
public class PaymentVoucherDaoImpl implements PaymentVoucherDao{
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentVoucherDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PayVoucherResponse savePayment(String authorizationHeader, String userCode,
			@Valid CreatePayVoucherRequest requestModel, Device device) {
		 PayVoucherResponse response = new PayVoucherResponse();
		 PaymentVoucherHdrEntity entity = new PaymentVoucherHdrEntity();
		 PaymentVoucherDtlEntity detailEntity = new PaymentVoucherDtlEntity();
		 PaymentVoucherGrnInvoiceDtlEntity grnInvoiceDetailEntity = null;
		 Integer id=null;
		 Map<String, Object> mapData = null;
		 BigInteger userId = null;
		 try {
		Session session = sessionFactory.getCurrentSession();  
		 BeanUtils.copyProperties(requestModel, entity);
		 String[] partyCode = entity.getPartyCode().split("\\|");
		 entity.setPartyCode(partyCode[0].trim());
		 mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
		entity.setCreatedBy(userId);
		entity.setCreatedDate(new Date());
		
//		String check = entity.getDocNo().substring(0, 3);
//			if(check.equalsIgnoreCase("REC")) {
//				entity.setBranchId(null);
//			}else {
//				entity.setPartyCode(null);
//			}
		  id = (Integer) session.save(entity);
		 response.setPaymentVoucherId(id);
		 if(id!=null) {
			 BeanUtils.copyProperties(requestModel, detailEntity);
			 detailEntity.setPayRecHrdId(id);
			 detailEntity.setRecieptModeId(requestModel.getReceiptModeId());
			 session.save(detailEntity);
		 }
		 if(requestModel.getRefDocList()!=null && !requestModel.getRefDocList().isEmpty()) {
			 updateGrnInvoiceAmt(requestModel.getRefDocList(),userCode);
			 for(RefDocListRequest bean:requestModel.getRefDocList()) {
				 grnInvoiceDetailEntity = new PaymentVoucherGrnInvoiceDtlEntity();
				 BeanUtils.copyProperties(bean, grnInvoiceDetailEntity);
				 grnInvoiceDetailEntity.setSettleDate(new Date());				 
				 grnInvoiceDetailEntity.setPayRecHrdId(id);
				 session.save(grnInvoiceDetailEntity);
			 }
			
		 }
		 }catch(Exception e) {
			 logger.error("Server side error in payment voucher",e.getMessage());
			 response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			 response.setPaymentVoucherNumber(requestModel.getDocNo());
			 response.setMsg("Server side error in payment voucher :- "+e.getMessage());
		 }finally {
			 if(id!=null) {
				 response.setStatusCode(WebConstants.STATUS_OK_200);
				 response.setPaymentVoucherNumber(requestModel.getDocNo());
				 response.setMsg(" Payment voucher save successfully...");
			 }
		 }
		 
		return response;
	}
	
	
	
	
	private void updateGrnInvoiceAmt(List<RefDocListRequest> refDocList, String userCode) {
		
	    String sqlQuery = "exec [SP_SA_GET_UPDATE_MRN_PRICE] :MRNNUMBER, :PAY_REC_HRD_ID, :AMOUNT";
	    org.hibernate.query.Query<?> query = null;
	    	try {
	    	for(RefDocListRequest req:refDocList) {
	    	Session session = sessionFactory.getCurrentSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("MRNNUMBER", req.getGrnInvId());
	        query.setParameter("PAY_REC_HRD_ID", req.getPayHrdId());
	        query.setParameter("AMOUNT", req.getPendingAmt());
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        query.executeUpdate();
	    	}
		   }catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	

	
	@Override
	public List<PaymentVoucherList> fetchPaymentReceiptList(String lookupTypeCode) {
		
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
	public List<PaymentBankList> getBankCategory(String bankCode, String userCode) {
	
		Session session = null;
		if (logger.isDebugEnabled()) {
			logger.debug("bankCode invoked.." + bankCode);
		}
		Query query = null;
		PaymentBankList responseModel = null;
		List<PaymentBankList> list = new ArrayList<>();
		Integer recordCount = 0;
		String sqlQuery = "PA_GET_PV_BANK_DTL :bankCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("bankCode", "BNK");
		
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PaymentBankList();
					
					responseModel.setBankId((Integer)row.get("Party_Master_Id"));
					responseModel.setBankDisplay((String)row.get("PartyName"));
			
					list.add(responseModel);
				}

				

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return list;
	}

//	@Override
//	public List<PartyCategoryResponse> searchPartyTypeCategory() {
//		PartyCategoryResponse responseModel=null;
//		List<PartyCategoryResponse>  finalList= null;
//		 Session session = sessionFactory.getCurrentSession();
//		 Query query = session.createQuery("select party_category_id, PartyCategoryCode, PartyCategoryName from ADM_BP_MST_PARTY_CTGRY where IsActive='Y'".toString());
//	     List<Object[]> list = query.list();
//	      
//		
//
//	 if(!list.isEmpty()) {
//		 finalList = new ArrayList<>();
//		 for(Object[] object:list) {
//			 
//			 Object[] obj =  object;
//			 
//			 responseModel = new PartyCategoryResponse();
//				
//			responseModel.setPartyCategoryId((BigInteger) obj[0]);
//			responseModel.setPartyCategoryCode((String) obj[1]);
//			responseModel.setPartyCategoryName((String) obj[2]);
//			finalList.add(responseModel);
//			
//		 }
//	 }
//	  return finalList;
//	}

	
	


	@Override
	public List<PartyCategoryResponse> searchPartyTypeCategory() {

		
		Session session = null;
		Query query = null;
		PartyCategoryResponse responseModel=null;
		List<PartyCategoryResponse>  responseListModel= null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "select party_category_id, PartyCategoryCode, PartyCategoryName from ADM_BP_MST_PARTY_CTGRY where IsActive='Y'";
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					
					responseModel = new PartyCategoryResponse();
					
					responseModel.setPartyCategoryId((BigInteger) row.get("party_category_id"));
					responseModel.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
					responseModel.setPartyCategoryName((String) row.get("PartyCategoryName"));
					responseListModel.add(responseModel);
				}

			}
			
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
		return responseListModel;
	}
	
	@Override
	public PartyCodeListResponseModel searchPVpartyCodeList(String userCode, PVpartyCodeRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchPartyCodeList invoked.." + requestModel.toString());
		}
		Session session = null;
		PartyCodeListResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchParyCodeViewList(session, userCode, requestModel);
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
	
	


	@SuppressWarnings({ "deprecation", "rawtypes" })
	public PartyCodeListResponseModel searchParyCodeViewList(Session session, String userCode,
			PVpartyCodeRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAllotList invoked.." + requestModel.toString());
		}
		Query query = null;
		//List<PartyCodeListResponseModel> resp = new ArrayList<PartyCodeListResponseModel>();
		PartyCodeListResponseModel partyCodeResp = new PartyCodeListResponseModel();
		PartyCodeModel partyCodeModel = null;
		List<PartyCodeModel> partyCodeStr =new ArrayList<PartyCodeModel>();
		Integer recordCount = 0;
		String sqlQuery = "exec [Sp_get_Customer_party_code] :barnchid,:PartyCode, :PartyTypeId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("barnchid", requestModel.getBranchId());
			query.setParameter("PartyCode", requestModel.getSearchText());
			query.setParameter("PartyTypeId", requestModel.getPartyTypeId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					partyCodeModel = new PartyCodeModel();
					partyCodeModel.setPartyCode((String)row.get("PartyCode"));
					partyCodeModel.setBranchPartyCodeId((BigInteger)row.get("party_branch_id"));
					partyCodeStr.add(partyCodeModel);
					recordCount++;
				}
				
				partyCodeResp.setPartyCode(partyCodeStr);
				partyCodeResp.setRecordCount(recordCount);
				
				//resp.add(partyCodeResp);
					
					
					
			}
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
		return partyCodeResp;
	}


	@Override
	public List<PVGrnAndInvoiceResponse>  getRefDocDetail(String userCode, RefDocForGrnInvReq requestModel) {
		
		Session session = null;
		if (logger.isDebugEnabled()) {
			logger.debug("getGrnDetail invoked.." + requestModel.toString());
		}
		Query query = null;
		PVGrnAndInvoiceResponse PVGrnAndInvoiceResponse = null;
		List<PVGrnAndInvoiceResponse> responseList =new ArrayList<PVGrnAndInvoiceResponse>();
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_GET_MRN_PARTYBY] :PARTYID, :FLAG, :userCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("PARTYID",  requestModel.getPartyTypeId());//requestModel.getPartyTypeId());
			query.setParameter("FLAG", requestModel.getRefDocType()!=null?(requestModel.getRefDocType().equals("SPARES GRN")?"MRN":requestModel.getRefDocType().equals("SPARES SALE INVOICE")?"INVOICE":requestModel.getRefDocType().equals("APR RETURN")?"APRRETURN" :"COUNTERSALE"):"");		
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					PVGrnAndInvoiceResponse = new PVGrnAndInvoiceResponse();
					PVGrnAndInvoiceResponse.setPayHeaderId((BigInteger) row.get("PAYHDRID"));
					PVGrnAndInvoiceResponse.setGrnInvNumber((String) row.get("MRNNumber"));
					PVGrnAndInvoiceResponse.setGrnInvDate((Date)row.get("MRNDate"));
					PVGrnAndInvoiceResponse.setGrnInvAmt((BigDecimal)row.get("MRNValue"));
					PVGrnAndInvoiceResponse.setPendingAmt((BigDecimal)row.get("PENDING_AMT"));   
					PVGrnAndInvoiceResponse.setSettleAmt((BigDecimal) row.get("SETTLE_AMT"));
					PVGrnAndInvoiceResponse.setSettleDate((String) row.get("SETTLE_DATE")!=null?(String) row.get("SETTLE_DATE"):null);
					responseList.add(PVGrnAndInvoiceResponse);
					recordCount++;
				}
				
				//responseList.setPartyCode(partyCodeStr);
				//responseList.setRecordCount(recordCount);
				
				//resp.add(partyCodeResp);
					
					
					
			}
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
		return responseList;
	}

	

	@Override
	public BigDecimal getPartyWiseAmt(String partyCode,String pvTypeCode) {
	
		Session session = null;
		Query query = null;  
		BigDecimal value=null;
		
		
		try {
			session = sessionFactory.openSession();
			
			BigInteger pvType = (BigInteger) session.createSQLQuery("select lookup_id from SYS_LOOKUP where lookupVal="+"'"+pvTypeCode+"'").uniqueResult();
			String sqlQuery = "SELECT ADV_AMT from PA_PAYMENT_REC_HDR WHERE PARTY_CODE="+"'"+partyCode+"'"+" and VOUCHER_TYPE_ID="+"'"+pvType.intValue()+"'";
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;	
					 value=(BigDecimal) row.get("ADV_AMT")!=null?(BigDecimal) row.get("ADV_AMT"):BigDecimal.ZERO;
				}
			}
			
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
		return value;
	}


	@Override
	public List<?> filter(FilterPaymentVoucherReq requestModel, String userCode) {
		 String sqlQuery = "exec [SP_PAYMENT_VOUCHER_SEARCH] :dealerId, :branchId, :stateId, :pcId, :userCode, :paymentVoucherNo, :voucherType,  :partyTypeId, :partyCodeId, :startDate, :toDate, :page, :size";
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
		        query.setParameter("paymentVoucherNo", requestModel.getPaymentVoucherNo());
		        query.setParameter("voucherType", requestModel.getVoucherTypeId());
		        query.setParameter("partyTypeId", requestModel.getPartyTypeId());
		        query.setParameter("partyCodeId", requestModel.getPartyCodeId());
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
	public List<SaveResponse> searchPaymentVoucherNumber(String searchText, String userCode) {
		
		SaveResponse bean=null;
		List<SaveResponse> beans=null;
		 NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("select ID, DOC_NO from PA_PAYMENT_REC_HDR where DOC_NO like :paymentVoucherNo");
		 query.setReadOnly(true);
		 query.setParameter("paymentVoucherNo",  "%" + searchText.toLowerCase() + "%");
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
	public List<?> viewPaymentVoucherDetail(BigInteger paymentVoucherId, String userCode, Integer flag) {
		
		 String sqlQuery = "exec [SP_VIEW_PAYMENT_VOUCHER] :paymentVoucherId, :FLAG, :userCode";
		    Query<?> query = null;
		    List<?> data = null;
		    try {
		    	Session session = sessionFactory.getCurrentSession();
		        query = session.createSQLQuery(sqlQuery);
		        query.setParameter("paymentVoucherId",paymentVoucherId);
		        query.setParameter("FLAG",flag);
		        query.setParameter("userCode", userCode);		        
		        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		        data = query.list();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return data;
		}
	
	@SuppressWarnings("deprecation")
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}


	@Override
	public List<PartyCategoryResponse> partyCodeList(String searchText,String userCode) {
		
		Session session = null;
		Query query = null;
		PartyCategoryResponse responseModel=null;
		List<PartyCategoryResponse>  responseListModel= null;  
		
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [PV_Search_Party_Code] :searchText,:userCode";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<PartyCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyCategoryResponse();
					responseModel.setPartyCategoryId((BigInteger) row.get("party_branch_id"));
					responseModel.setPartyCategoryName((String) row.get("PartyName"));
					responseModel.setPartyCategoryCode((String) row.get("PartyCode"));
					
					responseListModel.add(responseModel);
				}

			}
			
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
		return responseListModel;
	}
	

	@Override
	public List<SparePoDealerAndDistributerSearchResponse> getPartyNameList(String userCode,
			SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchDealerAndDistributorList invoked.." + userCode + " "
					+ sparePoDealerAndDistributorRequest.toString());
		}
		Session session = null;
		Query query = null;
		List<SparePoDealerAndDistributerSearchResponse> responseModelList = null;
		String sqlQuery = "exec [getCounterSalePartyName] :userCode, :searchText";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", sparePoDealerAndDistributorRequest.getDealerCode());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePoDealerAndDistributerSearchResponse>();
				SparePoDealerAndDistributerSearchResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePoDealerAndDistributerSearchResponse();
					responseModel.setParentDealerId((BigInteger) row.get("invoice_sale_id"));
					responseModel.setParentDealerCode((String) row.get("CustomerName"));
					responseModel.setParentDealerName((String) row.get("CustomerName"));
					responseModelList.add(responseModel);
				}
			}
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
		return responseModelList;
	}




	@Override
	public DistributorDetailResponse getPartyNameDetails(Integer distributorId) {
		Session session = null;
		DistributorDetailResponse distributorDetailResponse = null;
		Query query = null;
		
		String sqlQuery = "exec [SP_Get_Sale_Invoice_Details] :saleInvoiceId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("saleInvoiceId", distributorId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					distributorDetailResponse = new DistributorDetailResponse();
					distributorDetailResponse.setDistributorName((String) row.get("ParentDealerName"));
					distributorDetailResponse.setDistributorCode((String) row.get("PartyCode"));
					
					distributorDetailResponse.setDistrict((String) row.get("DistrictDesc"));
					distributorDetailResponse.setTehsil((String) row.get("TehsilDesc"));
					distributorDetailResponse.setPinCode((String) row.get("PinCode"));
					distributorDetailResponse.setPinId((BigInteger) row.get("pin_id"));
					distributorDetailResponse.setState((String) row.get("StateDesc"));
//					distributorDetailResponse.setBranchId((String) row.get("branch_id"));
					//added by vivek
					distributorDetailResponse.setDealerAddress1((String) row.get("PartyAddLine1"));
//					distributorDetailResponse.setDealerAddress2((String) row.get("DealerAddress2"));
//					distributorDetailResponse.setDealerAddress3((String) row.get("DealerAddress3"));
					distributorDetailResponse.setDealerCity((String) row.get("CityDesc"));
					distributorDetailResponse.setDealerCountry((String) row.get("CountryDesc"));
//					distributorDetailResponse.setDealerPincode((String) row.get("Dealer_Pincode"));
					distributorDetailResponse.setMobileNumber((String) row.get("Mobile_No"));
					distributorDetailResponse.setGstNo((String) row.get("GST_NO"));
//					distributorDetailResponse.setPanNo((String) row.get("pan_no"));
//					distributorDetailResponse.setTanNo((String) row.get("TANNo"));
//					distributorDetailResponse.setCinNo((String) row.get("CINNo"));		
					
					
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return distributorDetailResponse;
	}




	@Override
	public DistributorDetailResponse getPartyWisePVDetails(Integer distributorId, String flag) {
		Session session = null;
		DistributorDetailResponse distributorDetailResponse = null;
		Query query = null;
		
		String sqlQuery = "exec [SP_GET_PARTY_WISE_PV_DETAIL] :ParentDealerId, :Flag";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("ParentDealerId", distributorId);
			query.setParameter("Flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					distributorDetailResponse = new DistributorDetailResponse();
					distributorDetailResponse.setDistributorName((String) row.get("ParentDealerName"));
					distributorDetailResponse.setDistributorCode((String) row.get("ParentDealerCode"));
					distributorDetailResponse.setDistrict((String) row.get("Dealer_district"));
					distributorDetailResponse.setTehsil((String) row.get("Dealer_tehsil"));
					distributorDetailResponse.setPinCode((String) row.get("dealer_pincode"));
					distributorDetailResponse.setPinId((BigInteger) row.get("pin_id"));
					distributorDetailResponse.setState((String) row.get("Dealer_state"));
					distributorDetailResponse.setBranchId((String) row.get("branch_id"));
					//added by vivek
					distributorDetailResponse.setDealerAddress1((String) row.get("DealerAddress1"));
					distributorDetailResponse.setDealerAddress2((String) row.get("DealerAddress2"));
					distributorDetailResponse.setDealerAddress3((String) row.get("DealerAddress3"));
					distributorDetailResponse.setDealerCity((String) row.get("Dealer_city"));
					distributorDetailResponse.setDealerCountry((String) row.get("Dealer_country"));
					distributorDetailResponse.setDealerPincode((String) row.get("Dealer_Pincode"));
					distributorDetailResponse.setMobileNumber((String) row.get("MobileNumber"));
					distributorDetailResponse.setGstNo((String) row.get("gst_no"));
					distributorDetailResponse.setPanNo((String) row.get("pan_no"));
					distributorDetailResponse.setTanNo((String) row.get("TANNo"));
					distributorDetailResponse.setCinNo((String) row.get("CINNo"));		
					
					
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return distributorDetailResponse;
	}




	@Override
	public List<PartyCategoryResponse> getPvCategory(String userCode) {
		
		if (logger.isDebugEnabled()) {
			//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
		}
		Session session = null;
		Query query = null;
		PartyCategoryResponse responseModel=null;
		List<PartyCategoryResponse>  responseListModel= null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "select * from ADM_BP_MST_PARTY_CTGRY where IsActive=:active ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("active", 'Y');
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel = new ArrayList<PartyCategoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PartyCategoryResponse();
					responseModel.setPartyCategoryId((BigInteger) row.get("party_category_id"));
					responseModel.setPartyCategoryName((String) row.get("PartyCategoryName"));
					responseModel.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
					
					responseListModel.add(responseModel);
				}

			}
			
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
		return responseListModel;
	}



	
		
}
