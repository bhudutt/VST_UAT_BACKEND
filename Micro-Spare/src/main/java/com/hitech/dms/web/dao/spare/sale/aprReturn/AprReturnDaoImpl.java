package com.hitech.dms.web.dao.spare.sale.aprReturn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.spare.sale.aprReturn.AprReturnDtlEntity;
import com.hitech.dms.web.entity.spare.sale.aprReturn.AprReturnHrdEntity;
import com.hitech.dms.web.entity.spare.sale.invoice.SaleInvoiceDtlEntity;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnCancelDtl;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnCancelRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnDtl;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.AprReturnSearchRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.request.CreateAprReturnRequest;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprAppointmentSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprAppointmentSearchRep;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnNumber;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnSearchList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.AprReturnSearchRep;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.PartyInvoiceList;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.SpareAprRetunCreateResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.ViewAprReturnDtlResponse;
import com.hitech.dms.web.model.spare.sale.aprReturn.response.ViewAprReturnResponse;

@Repository
public class AprReturnDaoImpl implements AprReturnDao{
	
	private static final Logger logger = LoggerFactory.getLogger(AprReturnDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;



	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<PartyInvoiceList> fetchPartyInvoiceList(Integer partyBranchId, String userCode) {
	    List<PartyInvoiceList> finalList = null;
	    
	    // Assuming you want to call a stored procedure with Hibernate's StoredProcedureQuery
	    Session session = sessionFactory.getCurrentSession();
	    try {
	        // Create the StoredProcedureQuery
	        StoredProcedureQuery query = session.createStoredProcedureQuery("SP_PA_INVOICELIST_APR")
	            .registerStoredProcedureParameter("userCode", String.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("PartybranchId", Integer.class, ParameterMode.IN);
	        
	        // Set parameters
	        query.setParameter("userCode", userCode);
	        query.setParameter("PartybranchId", partyBranchId);
	        
	        // Execute the stored procedure
	        query.execute();
	        
	        // Retrieve the result list from the stored procedure
	        List<Object[]> results = query.getResultList();
	        finalList = new ArrayList<>();
	        
	        // Process the results
	        for (Object[] row : results) {
	            PartyInvoiceList res = new PartyInvoiceList();
	            res.setSaleInvoiceId((BigInteger) row[0]);  // Adjust index based on your stored procedure output
	            res.setInvoiceNumber((String) row[1]);    // Adjust index based on your stored procedure output
	            res.setInvoiceDate((Date) row[2]); // Adjust index based on your stored procedure output
	            finalList.add(res);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();  // Handle exceptions properly in production code
	    }
	    
	    return finalList;
	}


	@Override
	@Transactional(readOnly = true)
	public List<?> getInvoicePartDetail(Integer saleInvoiceId, String userCode) {
		
	    String sqlQuery = "exec [SP_PA_APR_RETURN_PARTDETAIL]  :invoiceSaleId";
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Query<?> query = null;
	    List<?> data = null;
	    try {
	    	Session session = sessionFactory.getCurrentSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("invoiceSaleId", saleInvoiceId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}


	@Override
	public SpareAprRetunCreateResponse createAprReturn(String userCode,
			@Valid CreateAprReturnRequest requestModel) {
		
		
		Session session = null;
		Transaction transaction = null;
		AprReturnHrdEntity hdrEntity = new AprReturnHrdEntity();
		AprReturnDtlEntity dtlEntity = null;
		SaleInvoiceDtlEntity saleInvBean = null;
		
		SpareAprRetunCreateResponse responseModel = new SpareAprRetunCreateResponse();
		Integer aprReturnId = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		try {
			
			Integer userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			mapData = fetchUserDTLByUserCode(session, userCode);
			hdrEntity.setBranchId(requestModel.getBranchId());
			hdrEntity.setAprReturnDate(new Date());
			hdrEntity.setCreatedDate(new Date());
			hdrEntity.setPartyId(requestModel.getPartyId());
			hdrEntity.setPartyTypeId(requestModel.getPartyTypeId());	
			hdrEntity.setAprReturedDocNo(requestModel.getAprReturedDocNo());
			hdrEntity.setInvoiceId(requestModel.getInvoiceId());
			hdrEntity.setAprReturnStatus(requestModel.getAprReturnStatus());
			hdrEntity.setTotalTaxableAmount(requestModel.getTotalTaxableAmount());
			hdrEntity.setTotalGstAmount(requestModel.getTotalGstAmount());
			hdrEntity.setReturnAmount(requestModel.getReturnAmount());
			
			if (mapData != null && mapData.get("SUCCESS") != null) {

				userId = ((BigInteger)mapData.get("userId")).intValue();
				hdrEntity.setCreatedBy(userId);

			}
		
			session.save(hdrEntity);
			aprReturnId = hdrEntity.getAprReturedId();
			List<AprReturnDtl> list = new ArrayList<>(requestModel.getSparePartDetails());
			if (!list.isEmpty()) {
				for (AprReturnDtl bean : list) {
					Integer qty = 0;
					dtlEntity =	new AprReturnDtlEntity();
					saleInvBean = new SaleInvoiceDtlEntity();
					
					BeanUtils.copyProperties(bean, dtlEntity);
					dtlEntity.setAprReturnHrdEntity(hdrEntity);
					saleInvBean = session.get(SaleInvoiceDtlEntity.class, bean.getInvoiceSaleDetailId());
					qty = saleInvBean.getAprReturnedQty()!=null?saleInvBean.getAprReturnedQty():0;
					saleInvBean.setAprReturnedQty(qty+bean.getAprReturnQty());
					session.update(saleInvBean);
					
					session.save(dtlEntity);
					}
				}

			}
		  catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);

		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {

				responseModel.setMsg("Apr Return Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setAprReturnHdrId(aprReturnId);
				responseModel.setAprReturnNumber(requestModel.getAprReturedDocNo());

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
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
		public List<AprReturnNumber> fetchAprReturnNoList(String userCode, InvoicePartNoRequest requestModel) {
			
			if (logger.isDebugEnabled()) {
				//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
			}
			Session session = null;
			Query query = null;
			AprReturnNumber bean=null;
			List<AprReturnNumber> docNoList = null;
			try {
				session = sessionFactory.openSession();
				 String sqlQuery = "EXEC GET_APR_DOC_LIST_BY_USER :USERCODE, :APRDOC";
				query = session.createSQLQuery(sqlQuery);
				 query.setParameter("USERCODE", userCode);
			     query.setParameter("APRDOC", requestModel.getSearchText().toUpperCase());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					
					docNoList = new ArrayList<AprReturnNumber>();
					for (Object object : data) {
						  Map row = (Map) object;
			                bean = new AprReturnNumber();
			                bean.setAprReturnId((Integer) row.get("APR_RETURN_ID"));
   		                    bean.setAprReturnNumber((String)row.get("APR_RETURN_DOC_NO"));
   		                    
   		                 docNoList.add(bean);
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
			return docNoList;
		}
		
		
		@Override
		public List<PartyInvoiceList> fetchInvoiceNumList(String userCode, InvoicePartNoRequest requestModel) {
			
			if (logger.isDebugEnabled()) {
				//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
			}
			Session session = null;
			Query query = null;
			PartyInvoiceList bean=null;
			List<PartyInvoiceList> invNoList = null;
			try {
				session = sessionFactory.openSession();
				 String sqlQuery = "EXEC GET_APR_INVOICE_NO_LIST_BY_USER :USERCODE, :APRDOC";
				query = session.createSQLQuery(sqlQuery);
				 query.setParameter("USERCODE", userCode);
			     query.setParameter("APRDOC", requestModel.getSearchText().toUpperCase());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					
					invNoList = new ArrayList<PartyInvoiceList>();
					for (Object object : data) {
						  Map row = (Map) object;
			                bean = new PartyInvoiceList();
			                bean.setSaleInvoiceId((BigInteger) row.get("invoice_sale_id"));  
			                bean.setInvoiceNumber((String) row.get("DocNumber"));   
			               
   		                 invNoList.add(bean);
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
			return invNoList;
		}


		@Override
		public AprReturnSearchList search(String userCode, AprReturnSearchRequest request, Device device) {
			
			if (logger.isDebugEnabled()) {
				//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
			}
			Session session = null;
			Query query = null;
			AprReturnSearchRep bean=null;
			List<AprReturnSearchRep> resList=null;
			AprReturnSearchList list = new AprReturnSearchList();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				session = sessionFactory.openSession();
				 String sqlQuery = "EXEC SEARCH_APR_RETURN_DETAIL :userCode, :fromDate, :toDate, :partyTypeId, :branchId, :partyCodeId, :invoiceId,"
				 		+ ":aprReturnNumber, :page, :size";
				 query = session.createSQLQuery(sqlQuery);
				 
				 query.setParameter("userCode", userCode);
				 query.setParameter("fromDate", formatter.format(request.getFromDate()));
				 query.setParameter("toDate", formatter.format(DateToStringParserUtils.addDayByOne(request.getToDate())));
				 query.setParameter("partyTypeId", request.getPartyTypeId());
				 query.setParameter("branchId", request.getBranchId());
				 query.setParameter("partyCodeId", request.getPartyCodeId());
				 query.setParameter("invoiceId", request.getInvoiceId());
				 query.setParameter("aprReturnNumber", request.getAprReturnNumber());
				 query.setParameter("page", request.getPage());
				 query.setParameter("size", request.getSize());
			   
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					
					resList = new ArrayList<AprReturnSearchRep>();
					for (Object object : data) {
						  Map row = (Map) object;
			                bean = new AprReturnSearchRep();
			            //    bean.setBranchId((Integer)row.get("branch_id"));
			                bean.setId((Integer) row.get("apr_return_id"));
			                bean.setBranchName((String)row.get("BRANCHNAME"));
			                bean.setAprReturnDocNo((String) row.get("APR_RETURN_DOC_NO"));
			                bean.setAprReturnDate(DateToStringParserUtils.parseDateToStringDDMMYYYY((Date) row.get("APR_RETURN_DATE")));
			                bean.setAprReturnStatus((String) row.get("APR_RETURN_STATUS"));
			            //    bean.setPartyId((Integer)row.get("PARTY_ID"));
			                bean.setPartyName((String)row.get("PARTYNAME"));
			                bean.setPartCode((String)row.get("PARTCODE"));
			                bean.setPartyType((String)row.get("PARTYTYPE"));
			              //  bean.setInvoiceId((Integer)row.get("INVOICE_ID"));
			                bean.setInvoiceNo((String)row.get("INVOICE_NO"));
			                bean.setAction((String)row.get("action"));
//			                bean.setAprReturnQty((Integer)row.get("APR_RETURN_QTY"));
//			                bean.setAprReturnedQty((Integer)row.get("APR_RETURNED_QTY"));
			                resList.add(bean);
			                
					}
					list.setSearchList(resList);
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
			return list;
		}


		@Override
		public ViewAprReturnResponse viewAprReturnHeader(BigInteger aprReturnId, String userCode) {
			
			    ViewAprReturnResponse bean = null;
			    Session session = null;
			    String sqlQuery = "exec [SP_VIEW_APR_RETURN_DETAIL_BY_ID]  :ID, :status";
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			    Query<?> query = null;
			    List<?> data = null;
			    try {
			    	session = sessionFactory.openSession();
			        query = session.createSQLQuery(sqlQuery);
			        query.setParameter("ID", aprReturnId);
			        query.setParameter("status", "header");
			        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			        data = query.list();
			        if (data != null && !data.isEmpty()) {
						
						
						for (Object object : data) {
							  Map row = (Map) object;
				                bean = new ViewAprReturnResponse();
				                
				                bean.setBranchName((String) row.get("BranchName"));
				                bean.setPartyName((String) row.get("PartyName"));
				                bean.setPartycode((String) row.get("PartyCode"));
				                bean.setInvoiceNumber((String) row.get("InvoiceNumber"));
				                bean.setInvoiceDate((String) row.get("InvoiceDate"));
				                bean.setMobileNumber((String) row.get("MobileNumber"));
				                bean.setPartyAddress((String) row.get("PartyAddress"));
//				                bean.setMobileNumber((Integer) row.get(""));
				                bean.setPartyType((String) row.get("PartyType"));
				                bean.setPinCode((String) row.get("PinCode"));
				                bean.setStateName((String) row.get("Statename"));
				                bean.setDistrictName((String) row.get("DistrictName"));
				                bean.setTehsilName((String) row.get("TehsilName"));
				            	bean.setCityName((String) row.get("CityName"));
				            	bean.setPostOfficeName((String) row.get("PostOfficeName"));
				            	bean.setAprNumber((String) row.get("AprNumber"));
				            	bean.setAprDate((String) row.get("AprDate"));
				            	bean.setTotalTaxableAmount((BigDecimal)row.get("totalTaxableAmount"));
				            	bean.setTotalGstAmt((BigDecimal)row.get("totalGstAmt"));
				            	bean.setReturnAmount((BigDecimal)row.get("returnAmount"));
				            	bean.setAprReturnStatus((String)row.get("apr_return_status"));
				            	bean.setRemark((String)row.get("remark"));
				            	
				            
						}
						
					}
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
				return bean;
		}


		@Override
		public List<ViewAprReturnDtlResponse> viewAprReturnDetail(BigInteger aprReturnId, String userCode) {
			
			
			    ViewAprReturnDtlResponse bean = null;
			    List<ViewAprReturnDtlResponse> list = null;
			    Session session = null;
			    String sqlQuery = "exec [SP_VIEW_APR_RETURN_DETAIL_BY_ID]  :ID, :status";
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			    Query<?> query = null;
			    List<?> data = null;
			    try {
			    	session = sessionFactory.openSession();
			        query = session.createSQLQuery(sqlQuery);
			        query.setParameter("ID", aprReturnId);
			        query.setParameter("status", "detail");
			        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			        data = query.list();
			        if (data != null && !data.isEmpty()) {
						
							list=new ArrayList<ViewAprReturnDtlResponse>();
						for (Object object : data) {
							  Map row = (Map) object;
				                bean = new ViewAprReturnDtlResponse();
				                
				                
				                bean.setMrp((BigDecimal)row.get("mrp"));
				                bean.setPartNumber((String)row.get("PARTNUMBER"));
				                bean.setPartDesc((String)row.get("PartDesc"));
				                bean.setProductCtg((String)row.get("Product_Category"));
				                bean.setOrderQty((Integer)row.get("OnOrderQty"));
				                bean.setTotalStock((Integer)row.get("OnHandQty"));
				                bean.setAprReturnedQty((Integer)row.get("APR_RETURNED_QTY"));
				                bean.setAprReturnQty((Integer)row.get("APR_RETURN_QTY"));
				                bean.setStore((String)row.get("StoreDesc"));
				                bean.setInvoiceQty((Integer)row.get("INVOICE_QTY"));
				                bean.setBinName((String)row.get("BinName"));
				            	bean.setDiscountRate((Integer)row.get("DISCOUNT_RATE"));
				            	bean.setDiscountValue((BigDecimal)row.get("DISCOUNT_VALUE"));
				            	bean.setIgstAmount((BigDecimal)row.get("IGST_AMOUNT"));
				            	bean.setCgstAmount((BigDecimal)row.get("CGST_AMOUNT"));
				            	bean.setSgstAmount((BigDecimal)row.get("SGST_AMOUNT"));
				            	bean.setCgstPer((BigDecimal)row.get("CGST_PER"));
				            	bean.setIgstPer((BigDecimal)row.get("IGST_PER"));
				            	bean.setSgstPer((BigDecimal)row.get("SGST_PER"));
				            	bean.setAdditionalDiscountAmount((BigDecimal)row.get("ADDITIONAL_DISCOUNT_AMOUNT"));
				            	bean.setAdditionalDiscountRate((BigDecimal)row.get("ADDITIONAL_DISCOUNT_RATE"));
				            	bean.setAdditionalDiscountType((String)row.get("ADDITIONAL_DISCOUNT_TYPE"));
				            	bean.setTaxableAmount((BigDecimal)row.get("TAXABLE_AMOUNT"));
				            	bean.setBasicValue((BigDecimal)row.get("BasicValue"));
				            	bean.setPartBranchId((Integer)row.get("partbranch_id"));
				            	bean.setPartId((Integer)row.get("part_id"));
				            	bean.setBranchId((Integer)row.get("branch_id"));
				            	bean.setStockStoreId((Integer)row.get("stock_store_id"));
				            	bean.setStockBinId((BigInteger)row.get("stock_bin_id"));
				            	bean.setInvoiceDetailId((BigInteger)row.get("invoice_sale_dtl_id"));
				            	list.add(bean);
				            
				            
						}
						
					}
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
				return list;
		}


		@Override
		@Transactional
		public Integer getQtyAndReturnQtyForApr(List<AprReturnDtl> aprList, String userCode) {
			
			String sqlQuery = "exec [SP_PA_APR_RETURN_ADD_QTY] :USERCODE, :BRANCHID, :PARTBRANCHID, :PARTID, :STOCKSTOREID, :STOCKBINID, :Qty";
				
			
			org.hibernate.query.Query<?> query = null;
			try {
				Session session = sessionFactory.getCurrentSession();
				for (AprReturnDtl req : aprList) {
					
			        query = session.createSQLQuery(sqlQuery);
					query.setParameter("USERCODE", userCode);
					query.setParameter("BRANCHID", req.getBranchId());
					query.setParameter("PARTBRANCHID", req.getPartBranchId());
					query.setParameter("PARTID", req.getPartId());
					query.setParameter("STOCKSTOREID", req.getStockStoreId());
					query.setParameter("STOCKBINID", req.getStockBinId());
					query.setParameter("Qty", req.getAprReturnQty());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					query.executeUpdate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


		@Override
		public List<PartyCategoryResponse> aprPartyCodeList(String searchText,String userCode) {
			
			Session session = null;
			Query query = null;
			PartyCategoryResponse responseModel=null;
			List<PartyCategoryResponse>  responseListModel= null;  
			
			try {
				session = sessionFactory.openSession();
				String sqlQuery = "exec [Apr_Search_Party_Code] :searchText,:userCode";
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
		public AprAppointmentSearchList aprAppointmentSearch(String userCode, AprReturnSearchRequest request,
				Device device) {
			
			if (logger.isDebugEnabled()) {
				//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
			}
			Session session = null;
			Query query = null;
			AprAppointmentSearchRep bean=null;
			List<AprAppointmentSearchRep> resList=null;
			AprAppointmentSearchList list = new AprAppointmentSearchList();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				session = sessionFactory.openSession();
				 String sqlQuery = "EXEC apr_appointment_search :userCode, :fromDate, :toDate, :partyTypeId, :branchId, :partyCodeId, "
				 		+ ":stateId, :districtId, :status, :page, :size";
				 query = session.createSQLQuery(sqlQuery);
				 
				 query.setParameter("userCode", userCode);
				 query.setParameter("fromDate", formatter.format(request.getFromDate()));
				 query.setParameter("toDate", formatter.format(DateToStringParserUtils.addDayByOne(request.getToDate())));
				 query.setParameter("partyTypeId", request.getPartyTypeId());
				 query.setParameter("branchId", request.getBranchId());
				 query.setParameter("partyCodeId", request.getPartyCodeId());
				 query.setParameter("stateId", request.getStateId());
				 query.setParameter("districtId", null);
				 query.setParameter("status", request.getStatus());
				 query.setParameter("page", request.getPage());
				 query.setParameter("size", request.getSize());
			   
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					
					resList = new ArrayList<AprAppointmentSearchRep>();
					for (Object object : data) {
						  Map row = (Map) object;
						  
			                bean = new AprAppointmentSearchRep();
			                bean.setPartyCode((String)row.get("PartyCode"));
			                bean.setPartyName((String)row.get("PartyName"));
			                bean.setPartyLocation((String)row.get("PartyLocation"));
			                bean.setDealerCode((String)row.get("DealerCode"));
			                bean.setFirstName((String)row.get("FirstName"));
			                bean.setMiddleName((String)row.get("MiddleName"));
			                bean.setLastName((String)row.get("LastName"));
			                bean.setEmail((String)row.get("Email"));
			                bean.setMobileNumber((String)row.get("MobileNumber"));
			                bean.setGstNumber((String)row.get("GSTno"));
			                bean.setAddress((String)row.get("Address"));
			                bean.setCity((String)row.get("City"));
			                bean.setDistrict((String)row.get("District"));
			                bean.setPinCode((String)row.get("Pincode"));
			                bean.setState((String)row.get("State"));
			                bean.setStatus((String)row.get("Status"));
			                bean.setPartyCategory((String)row.get("PartyCategoryName"));
			                bean.setCreationDate((String)row.get("CreatedDate"));
			              
			                resList.add(bean);
			                
					}
					list.setSearchList(resList);
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
			return list;
		}
		
		
		@Override
		@Transactional
		public Integer cancelAprReturnStatus(String userCode, AprReturnCancelRequest request, Device device) {
			Session session = sessionFactory.getCurrentSession();
			org.hibernate.query.Query<?> query = null;
			String sqlQuery = "UPDATE APR_RETURN_HDR SET APR_RETURN_STATUS=:status, REMARK=:remark WHERE APR_RETURN_ID=:aprReturnId";

			query = session.createSQLQuery(sqlQuery);
			query.setParameter("aprReturnId", request.getAprReturnId());
			query.setParameter("remark", request.getRemarks());
			query.setParameter("status", request.getStatus());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			Integer id = query.executeUpdate();
			return id;
		}


		@Override
		@Transactional
		public Integer cancelAprReturn(String userCode, AprReturnCancelRequest request,
				Device device) {
			Integer id = null;
			String sqlQuery = "exec [SP_PA_APR_RETURN_SUB_QTY] :USERCODE, :BRANCHID, :PARTBRANCHID, :PARTID, :STOCKSTOREID, :STOCKBINID, :Qty, :invoiceDetailId";
			List<AprReturnCancelDtl> list = new ArrayList<>();
			list.addAll(request.getPartDetails());
			org.hibernate.query.Query<?> query = null;
			try {
				Session session = sessionFactory.getCurrentSession();
				for (AprReturnCancelDtl req : list) {
					
			        query = session.createSQLQuery(sqlQuery);
					query.setParameter("USERCODE", userCode);
					query.setParameter("BRANCHID", req.getBranchId());
					query.setParameter("PARTBRANCHID", req.getPartBranchId());
					query.setParameter("PARTID", req.getPartId());
					query.setParameter("STOCKSTOREID", req.getStockStoreId());
					query.setParameter("STOCKBINID", req.getStockBinId());
					query.setParameter("Qty", req.getAprReturnQty());
					query.setParameter("invoiceDetailId", req.getInvoiceDetailId());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					id = query.executeUpdate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		 }
		
		



@Override
public AprAppointmentSearchList aprMappingReportSearch(String userCode, AprReturnSearchRequest request,
		Device device) {
	
	if (logger.isDebugEnabled()) {
		//logger.debug("searchPartyNameList invoked.." + categoryId.toString());
	}
	Session session = null;
	Query query = null;
	AprAppointmentSearchRep bean=null;
	List<AprAppointmentSearchRep> resList=null;
	AprAppointmentSearchList list = new AprAppointmentSearchList();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	try {
		session = sessionFactory.openSession();
		 String sqlQuery = "EXEC apr_appointment_mapping_report :userCode, :fromDate, :toDate, :partyTypeId, :branchId, :partyCodeId, "
		 		+ ":stateId, :districtId, :status, :page, :size";
		 query = session.createSQLQuery(sqlQuery);
		 
		 query.setParameter("userCode", userCode);
		 query.setParameter("fromDate", formatter.format(request.getFromDate()));
		 query.setParameter("toDate", formatter.format(DateToStringParserUtils.addDayByOne(request.getToDate())));
		 query.setParameter("partyTypeId", request.getPartyTypeId());
		 query.setParameter("branchId", request.getBranchId());
		 query.setParameter("partyCodeId", request.getPartyCodeId());
		 query.setParameter("stateId", request.getStateId());
		 query.setParameter("districtId", null);
		 query.setParameter("status", request.getStatus());
		 query.setParameter("page", request.getPage());
		 query.setParameter("size", request.getSize());
	   
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();
		if (data != null && !data.isEmpty()) {
			
			resList = new ArrayList<AprAppointmentSearchRep>();
			for (Object object : data) {
				  Map row = (Map) object;
				  
	                bean = new AprAppointmentSearchRep();
	                bean.setPartyCode((String)row.get("PartyCode"));
	                bean.setPartyName((String)row.get("PartyName"));
	                bean.setPartyLocation((String)row.get("PartyLocation"));
	                bean.setDealerCode((String)row.get("DealerCode"));
	                bean.setFirstName((String)row.get("FirstName"));
	                bean.setMiddleName((String)row.get("MiddleName"));
	                bean.setLastName((String)row.get("LastName"));
	                bean.setEmail((String)row.get("Email"));
	                bean.setMobileNumber((String)row.get("MobileNumber"));
	                bean.setGstNumber((String)row.get("GSTno"));
	                bean.setAddress((String)row.get("Address"));
	                bean.setCity((String)row.get("City"));
	                bean.setDistrict((String)row.get("District"));
	                bean.setPinCode((String)row.get("Pincode"));
	                bean.setState((String)row.get("State"));
	                bean.setStatus((String)row.get("Status"));
	                bean.setPartyCategory((String)row.get("PartyCategoryName"));
	                bean.setCreationDate((String)row.get("CreatedDate"));
	              
	                resList.add(bean);
	                
			}
			list.setSearchList(resList);
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
	return list;
}
	
}



