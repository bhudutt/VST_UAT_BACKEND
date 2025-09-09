package com.hitech.dms.web.dao.servicequotation.create;

import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.app.utils.DmsCollectionUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.quotation.create.request.ServiceQuotationEntity;
import com.hitech.dms.web.entity.quotation.create.request.ServiceQuotationLabrEntity;
import com.hitech.dms.web.entity.quotation.create.request.ServiceQuotationOutsideLbabrEntity;
import com.hitech.dms.web.entity.quotation.create.request.ServiceQuotationPartEntity;
import com.hitech.dms.web.entity.quotation.create.request.ServiceVerbatimEntity;
import com.hitech.dms.web.model.masterdata.response.QuotationSearchResponse;
import com.hitech.dms.web.model.partmaster.create.response.ServiceQutationCreateResponseModel;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationCloseResponse;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationLabourModel;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationModel;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationOutsideLabourModel;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationPartModel;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceVerbatimModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Repository
public class ServiceQuatationCreateDaoImpl implements ServiceQuatationCreateDao {

private static final Logger logger = LoggerFactory.getLogger(ServiceQuatationCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private DocumentNumberGenerator docNumber;
	
	
	@Autowired
	private CommonServiceClient commonServiceClient;
	
	@Override
	public ServiceQutationCreateResponseModel createServiceQuotation(String authorizationHeader, String userCode,
			ServiceQuotationModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createServiceQuotation invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		ServiceQutationCreateResponseModel responseModel = new ServiceQutationCreateResponseModel();
		ServiceQuotationEntity entity=null;
		boolean isSuccess = true;
		Integer save=0;
		String type="";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			Date todayDate = new Date();
			String userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger Id = (BigInteger) mapData.get("userId");
				userId=Id.toString();
			}
			
			entity = mapper.map(requestModel, ServiceQuotationEntity.class,
					"quotationMapId");
			if (entity.getServiceVerbatim() !=null && DmsCollectionUtils.isNotEmpty(entity.getServiceVerbatim())) {
				entity.getServiceVerbatim().removeIf(a->a.getCheck() ==false || (a.getCustomerVoice()==null || a.getCustomerVoice().equalsIgnoreCase("")));
			
				List<ServiceVerbatimEntity> l1 = entity.getServiceVerbatim();
				if (l1 != null && l1.size() > 0) {
					int i = 0;
					Iterator<ServiceVerbatimEntity> it = l1.iterator();
					while (it.hasNext()) {
						ServiceVerbatimEntity e = it.next();
						if (e.getCustomerVoice()==null || e.getCustomerVoice().equalsIgnoreCase("")) {
							it.remove();
							continue;
						}
						e.setSrNo(++i);
						e.setQuotationVerbtmId(null);
						e.setServiceQuotationEntity(entity);
						e.setCreatedBy(userCode);
						e.setCreatedDate(new Date());
					}
				}
			}
			
			
			if (entity.getServiceQuotationLabrEntity() !=null && DmsCollectionUtils.isNotEmpty(entity.getServiceQuotationLabrEntity())) {
				//entity.getServiceQuotationLabrEntity().removeIf(a -> a.getCheck()==false ||  a.getLabourDescription().equalsIgnoreCase(""));
					
				List<ServiceQuotationLabrEntity> l2 = entity.getServiceQuotationLabrEntity();
				if (l2 != null && l2.size() > 0) {
					Iterator<ServiceQuotationLabrEntity> it = l2.iterator();
					while (it.hasNext()) {
						ServiceQuotationLabrEntity e = it.next();
						if (e.getLabourDescription()==null || e.getLabourDescription().equalsIgnoreCase("") || e.getCheck()==false) {
							it.remove();
							continue;
						}
						e.setQuotationLabourId(null);
						e.setServiceQuotationEntity(entity);
						e.setBasicAmt(e.getRate());
						e.setCreatedBy(userCode);
						e.setCreatedDate(new Date());
					}
				}
			}
			
			
			
			if (entity.getServiceQuotationOutsideLabrEntity() !=null && DmsCollectionUtils.isNotEmpty(entity.getServiceQuotationOutsideLabrEntity())) {
				//entity.getServiceQuotationOutsideLabrEntity().removeIf(a -> a.getCheck()==false ||  a.getLabourDescription().equalsIgnoreCase(""));
			
				List<ServiceQuotationOutsideLbabrEntity> l4 = entity.getServiceQuotationOutsideLabrEntity();
				if (l4 != null && l4.size() > 0) {
					Iterator<ServiceQuotationOutsideLbabrEntity> it = l4.iterator();
					while (it.hasNext()) {
						ServiceQuotationOutsideLbabrEntity e = it.next();
						if (e.getLabourDescription()==null || e.getLabourDescription().equalsIgnoreCase("") || e.getCheck()==false) {
							it.remove();
							continue;
						}
						e.setQuotationOutsideLabourId(null);
						e.setServiceQuotationEntity(entity);
						e.setBasicAmt(e.getRate());
						e.setCreatedBy(userCode);
						e.setCreatedDate(new Date());
					}
				}
			
			}
			
			
			
			
			if (entity.getServiceQuotationPartEntity() !=null && DmsCollectionUtils.isNotEmpty(entity.getServiceQuotationPartEntity())) {
				//entity.getServiceQuotationPartEntity().removeIf(a -> a.getPartId() == null);
			
				List<ServiceQuotationPartEntity> l3 = entity.getServiceQuotationPartEntity();
				if (l3 != null && l3.size() > 0) {
					Iterator<ServiceQuotationPartEntity> it = l3.iterator();
					while (it.hasNext()) {
						ServiceQuotationPartEntity e = it.next();
						if (e.getPartId() == null ||  e.getPartId() == 0 ||  e.getCheck()==false) {
							it.remove();
							continue;
						}
						e.setQuotationPartId(null);
						e.setServiceQuotationEntity(entity);
						e.setCreatedBy(userCode);
						e.setCreatedDate(new Date());
					}
				}
			}
			
			entity.setBranchId(entity.getBranchId());
			entity.setCreatedBy(userCode);
			entity.setCreatedDate(new Date());
			
			String docnum = null;
			
			if (entity.getQuotationId() == null) {
				//docnum=   "QUO123456";//docNumber.getDocumentNumber("QUO", entity.getBranchId() + "", session);
				type="create";
				docnum = docNumber.getDocumentNumber("QUO",entity.getBranchId(), session);
				entity.setQuotationNumber(docnum);
				if (docnum == null || docnum.equals("")) {
					responseModel.setMsg("Document No. not generatng");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					return responseModel;
				}
				save =(Integer) session.save(entity);
				
				if(save>0) {
					docNumber.updateDocumentNumber(docnum.substring(docnum.length() - 7), "QUO",entity.getBranchId() + "", session,"Quotation");
					transaction.commit();
					isSuccess = true;
				}else {
					transaction.rollback();
					isSuccess = false;
				}
			}else {
				type="edit";
				String sqlDeleteQuery = "Delete from SV_QUOTATION_PRT where Quotation_Id =:Id";
				query = session.createNativeQuery(sqlDeleteQuery);
				query.setParameter("Id", entity.getQuotationId());
				query.executeUpdate();
				
				sqlDeleteQuery = "Delete from SV_QUOTATION_LBR where Quotation_Id =:Id";
				query = session.createNativeQuery(sqlDeleteQuery);
				query.setParameter("Id", entity.getQuotationId());
				query.executeUpdate();
				
				sqlDeleteQuery = "Delete from SV_QUOTATION_OUTSIDE_LBR where Quotation_Id =:Id";
				query = session.createNativeQuery(sqlDeleteQuery);
				query.setParameter("Id", entity.getQuotationId());
				query.executeUpdate();
				
				sqlDeleteQuery = "Delete from SV_QUOTATION_VERBTM where Quotation_Id =:Id";
				query = session.createNativeQuery(sqlDeleteQuery);
				query.setParameter("Id", entity.getQuotationId());
				query.executeUpdate();
				
				save=entity.getQuotationId();
				entity.setQuotationRevSeq(entity.getQuotationRevSeq()+1);
				session.saveOrUpdate(entity);
				isSuccess=true;
				transaction.commit();
			}
			
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		}finally {
			if (isSuccess) {
				
				mapData = fetchQuotationById(session, save);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setQuotationId(save);
					responseModel.setQuotationNumber((String)mapData.get("QuotationNumber"));
					responseModel.setMsg((String) mapData.get("QuotationNumber") + " " + "Quotation Number Created Successfully.");
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					
					if(type.equalsIgnoreCase("edit")){
						responseModel.setMsg((String) mapData.get("QuotationNumber") + " " +"Quotation Edited Successfully.");
						responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					}else{
						responseModel.setMsg((String) mapData.get("QuotationNumber") + "Quotation Created Successfully.");
						responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					}
				}

				
			}else {
				responseModel.setMsg("Some Issue While Creating Quotation.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		

		return responseModel;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchQuotationById(Session session, Integer id) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select Quotation_id,QuotationNumber from SV_QUOTATION_HDR  where Quotation_Id=:id";
		mapData.put("ERROR", "Quotation Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("id", id);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String quoNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					quoNumber = (String) row.get("QuotationNumber");
				}
				mapData.put("QuotationNumber", quoNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Quotation DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Quotation DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	public ServiceQuotationModel fetchQuotationView(String userCode, BigInteger id) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchQuotationView invoked.." + id);
		}
		Session session = null;
		ServiceQuotationModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			List data = fetchQuotationDtl(session, userCode, id,1);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ServiceQuotationModel();
					responseModel.setQuotationId((Integer) row.get("Quotation_Id"));
					responseModel.setRemarkscustomer((String) row.get("RemarksCustomer"));	
					responseModel.setBranchId((Integer) row.get("Branch_Id"));			
					responseModel.setQuotationNumber((String) row.get("QuotationNumber"));
					responseModel.setQuotationDate((Date) row.get("Quotation_Date"));
					responseModel.setServiceCategory((Integer) row.get("Service_Category"));
					responseModel.setServiceTypeId((Integer) row.get("Service_Type_Id"));
					responseModel.setCustomerId((Integer) row.get("Customer_Id"));	
					responseModel.setRepairOrderType((Integer) row.get("Repair_Order_Id"));
					responseModel.setQuotationRevSeq((Integer) row.get("QuotationRevSeq"));
					responseModel.setVinId((Integer) row.get("Vin_Id"));
					responseModel.setStatus((String) row.get("Status"));	
					responseModel.setCustomerName((String)row.get("customer_name"));
					responseModel.setMobNo((String) row.get("mobile_no"));
					responseModel.setPinCode((String) row.get("pincode"));
					responseModel.setVillage((String) row.get("village"));
					responseModel.setTehsil((String)row.get("tehsil"));
					responseModel.setDistrict((String) row.get("district"));
					responseModel.setState((String) row.get("state"));
					responseModel.setCountry((String) row.get("Country"));
					responseModel.setChargesAmountOnPart((BigDecimal) row.get("ChargeAmtPart"));	
					responseModel.setTotalAmountOnPart((BigDecimal) row.get("TotalAmtPart"));
					responseModel.setTotalAmountOnLabour((BigDecimal) row.get("TotalAmtLabour"));
					responseModel.setInsurancePatyType((Integer) row.get("insurancePartyId"));
					responseModel.setCurrentHours((String) row.get("CurrentHours"));
					responseModel.setTotalAmountOnOutsideLabour((BigDecimal) row.get("TotalAmtOutsideLabour"));	
					responseModel.setTotalHours((String) row.get("totalHours"));
					responseModel.setSource((Integer) row.get("Source_ID"));	
					responseModel.setChassisNo((String) row.get("chassis_no"));
					responseModel.setVinCode((String) row.get("VinCode"));
					responseModel.setRegistrationNumber((String) row.get("RegistrationNo"));
					responseModel.setEngineNumber((String) row.get("EngineNo"));
					responseModel.setMfgInvoiceDate((Date) row.get("MfgInvoiceDate"));
					responseModel.setSaleDate((Date) row.get("saleDate"));
					responseModel.setRetailDate((Date) row.get("RetailDate"));	
					responseModel.setModel((String) row.get("model"));
					responseModel.setModelId((BigInteger) row.get("model_id"));	
					responseModel.setServiceTypeDesc((String) row.get("SrvTypeDesc"));
				}
					data = fetchQuotationDtl(session, userCode, id,2);
					if (data != null && !data.isEmpty()) {
						List<ServiceQuotationLabourModel> list=new ArrayList<>();
						for (Object object : data) {
							Map row = (Map) object;
							ServiceQuotationLabourModel quoLbrMdl=new ServiceQuotationLabourModel();
							quoLbrMdl.setQuotationLabourId((Integer) row.get("Quotation_Labour_Id"));
							quoLbrMdl.setLabourId((Integer) row.get("Labour_Id"));
							quoLbrMdl.setRate((BigDecimal)row.get("Rate"));
							quoLbrMdl.setDiscountType((Integer) row.get("DiscountType"));
							quoLbrMdl.setChargeAmt((BigDecimal) row.get("chargeAmt"));
							quoLbrMdl.setTotalAmt((BigDecimal)row.get("TotalAmt"));
							quoLbrMdl.setLabourCode((String) row.get("LabourCode"));
							quoLbrMdl.setNetAmt((BigDecimal)row.get("NetAmt"));
							quoLbrMdl.setLabourDescription((String) row.get("LabourDesc"));
							quoLbrMdl.setDiscountRate((BigDecimal) row.get("DiscountRate"));
							list.add(quoLbrMdl);						
						}
						responseModel.setLabourList(list);
					}
					data = fetchQuotationDtl(session, userCode, id,3);
					if (data != null && !data.isEmpty()) {
						List<ServiceQuotationPartModel> partList=new ArrayList<ServiceQuotationPartModel>();
						for (Object object : data) {
							Map row = (Map) object;
							ServiceQuotationPartModel quoPart=new ServiceQuotationPartModel();
							quoPart.setQuotationPartId((Integer) row.get("Quotation_Part_Id"));
							quoPart.setPartBranchId((Integer) row.get("PartBranch_Id"));
							quoPart.setQtyReq((BigDecimal) row.get("QtyReq"));
							quoPart.setRate((BigDecimal) row.get("Rate"));	
							quoPart.setBasicAmt((BigDecimal) row.get("BasicAmt"));
							quoPart.setDiscountType((Integer) row.get("DiscountType"));
							quoPart.setDiscountRate((BigDecimal) row.get("DiscountRate"));
							quoPart.setNetAmt((BigDecimal) row.get("NetAmt"));
							quoPart.setTaxAmt((BigDecimal) row.get("ChargeAmt"));
							quoPart.setTotalAmt((BigDecimal) row.get("TotalAmt"));
							quoPart.setPartId((Integer) row.get("part_id"));
							quoPart.setPartNo((String) row.get("partNumber"));			
							quoPart.setDescription((String) row.get("PartDesc"));
							
							partList.add(quoPart);
						}
						responseModel.setPartList(partList);		
						
					}
					data = fetchQuotationDtl(session, userCode, id,4);
					if (data != null && !data.isEmpty()) {
						List<ServiceVerbatimModel> serviceVerList = new ArrayList<ServiceVerbatimModel>();
						ServiceVerbatimModel serviceVervatim=null;
						for (Object object : data) {
							Map row = (Map) object;
							serviceVervatim = new ServiceVerbatimModel();
							serviceVervatim.setQuotationVerbtmId((Integer) row.get("Quotation_Verbtm_Id"));		
							serviceVervatim.setCustomerVoice((String) row.get("CustomerVoice"));	
							serviceVerList.add(serviceVervatim);						
						}
						responseModel.setVerbatimList(serviceVerList);					
					}
					data = fetchQuotationDtl(session, userCode, id,5);
					if (data != null && !data.isEmpty()) {
						List<ServiceQuotationOutsideLabourModel> outsideLabourList=new ArrayList<ServiceQuotationOutsideLabourModel>();
						for (Object object : data) {
							Map row = (Map) object;
							ServiceQuotationOutsideLabourModel quoOutLbr=new ServiceQuotationOutsideLabourModel();
							quoOutLbr.setQuotationOutsideLabourId((Integer) row.get("Quotation_Outside_Labour_Id"));
							quoOutLbr.setLabourId((Integer) row.get("Labour_Id"));
							quoOutLbr.setRate((BigDecimal) row.get("Rate"));
							quoOutLbr.setDiscountType((Integer) row.get("DiscountType"));
							quoOutLbr.setDiscountRate((BigDecimal) row.get("DiscountRate"));	
							quoOutLbr.setChargeAmt((BigDecimal) row.get("ChargeAmt"));
							quoOutLbr.setTotalAmt((BigDecimal) row.get("TotalAmt"));
							quoOutLbr.setNetAmt((BigDecimal)row.get("NetAmt"));
							quoOutLbr.setLabourCode((String) row.get("LabourCode"));
							quoOutLbr.setLabourDescription((String) row.get("LabourDesc"));
							outsideLabourList.add(quoOutLbr);
						}
						responseModel.setOutsideLabourList(outsideLabourList);
					}
				
			}
		}catch (SQLGrammarException exp) {
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
	public List fetchQuotationDtl(Session session, String userCode, BigInteger id,Integer flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchQuotationDtl invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_QUO_SEARCH_VIEW_EDIT] :userCode, :id, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("id", id);
			query.setParameter("flag",flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return data;
	}
	
	@Override
	public List<ServiceQuotationModel> fetchQuotationNoList(String userCode,
			ServiceQuotationModel requestModel) {
		
		Session session = null;
	    List<ServiceQuotationModel> responseList = null;
	    ServiceQuotationModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GETQuotationNO] :userCode, :quoNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("quoNo", requestModel.getQuotationNumber());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceQuotationModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceQuotationModel();
					response.setQuotationId((Integer) row.get("Quotation_Id"));
					response.setQuotationNumber((String) row.get("QuotationNumber"));
					responseList.add(response);
					}
				}
				} catch (SQLGrammarException sqlge) {
					sqlge.printStackTrace();
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
	public ServiceQuotationCloseResponse quotationCloseStatusUpdate(String userCode, BigInteger quotationId,
			String remarks, String status) {
		ServiceQuotationCloseResponse response=new ServiceQuotationCloseResponse();
		Session session = null;
		NativeQuery<?> query = null;
		Transaction transaction = null; // 
		String sqlQuery = " update SV_QUOTATION_HDR set status=:status,remarks=:remarks where quotation_id=:quotation_id";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("remarks",remarks);
			query.setParameter("quotation_id",quotationId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			int rowCount = query.executeUpdate(); // Execute the insert query
			System.out.println("row count before commit "+rowCount);
			transaction.commit(); // Commit the transaction		
			if(rowCount==1)
			{
				System.out.println("Status Updated Successfullt ");
				response.setMessage("Updated Successfully ");
				response.setStatusCode(200);
				
			}
				} catch (SQLGrammarException sqlge) {
					sqlge.printStackTrace();
					response.setMessage(sqlge.getMessage());
					response.setStatusCode(301);
				} catch (HibernateException exp) {
					logger.error(this.getClass().getName(), exp);
					response.setMessage(exp.getMessage());
					response.setStatusCode(301);
				} catch (Exception exp) {
					response.setMessage(exp.getMessage());
					response.setStatusCode(301);
					logger.error(this.getClass().getName(), exp);
				} finally {
					if (session != null) {
						session.close();
					}
				}
		   System.out.println("before send response "+response);
			return response;
	
	}

	@Override
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jsaperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePaths = filePath + jsaperName;

			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePaths, jasperParameter, connection);

			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}

	@Override
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException {
		if (format != null && format.equalsIgnoreCase("xls")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
		}

	}
}
