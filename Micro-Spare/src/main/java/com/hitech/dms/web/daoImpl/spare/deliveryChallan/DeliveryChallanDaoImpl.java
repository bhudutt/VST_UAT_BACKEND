package com.hitech.dms.web.daoImpl.spare.deliveryChallan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.deliveryChallan.DeliveryChallanDao;
import com.hitech.dms.web.entity.spare.delivery.challan.DeliveryChallanHdrEntity;
import com.hitech.dms.web.entity.spare.delivery.challan.DeliveryChallanIssueDetailEntity;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcSearchRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcUpdateSparePartRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcUpdateStatusRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanDetailRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanPartDetailRequest;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DCcustomerOrderResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DCpartDetailListResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcPartQtyCalCulationResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcSearchBean;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcSearchListResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanHeaderAndDetailResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.SpareDcCreateResponse;

@Repository
@Transactional
public class DeliveryChallanDaoImpl implements DeliveryChallanDao {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryChallanDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public SpareDcCreateResponse createDeliveryChallan(String authorizationHeader, String userCode,
			@Valid DeliveryChallanDetailRequest requestModel, Device device) {

		Session session = null;
		Transaction transaction = null;
		DeliveryChallanHdrEntity deliveryChallanHdrEntity = null;
		DeliveryChallanIssueDetailEntity deliveryChallanIssueDetailEntity = null;
		SpareDcCreateResponse responseModel = new SpareDcCreateResponse();
		BigInteger dchallanId = null;
		boolean isSuccess = false;
		List<String> stockBinList = new ArrayList<String>();
		Map<String, Object> mapData = null;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			mapData = fetchUserDTLByUserCode(session, userCode);
			deliveryChallanHdrEntity = mapper.map(requestModel, DeliveryChallanHdrEntity.class);
			if (mapData != null && mapData.get("SUCCESS") != null) {

				userId = (BigInteger) mapData.get("userId");
				deliveryChallanHdrEntity.setCreatedBy(userId);
				deliveryChallanHdrEntity.setCreatedDate(new Date());

			}
			deliveryChallanHdrEntity.setDcDate(new Date());
			deliveryChallanHdrEntity.setChallanType(10);
			session.save(deliveryChallanHdrEntity);
			dchallanId = deliveryChallanHdrEntity.getDchallanId();
		//	List<DeliveryChallanPartDetailRequest> dcPartDetailList = new ArrayList<>(dcPartDetailMap.values());
			List<DeliveryChallanPartDetailRequest> list = new ArrayList<>(requestModel.getDcPartDetailList());
			if (!list.isEmpty()) {
				for (DeliveryChallanPartDetailRequest bean : list) {
					
					String[] intList = bean.getStockBinId().trim().split(",");
					stockBinList = Arrays.asList(intList);
					for(String stockBinId:stockBinList) {
					deliveryChallanIssueDetailEntity = new DeliveryChallanIssueDetailEntity();
					deliveryChallanIssueDetailEntity.setDChallanDtlId(bean.getDChallanDtlId());
					deliveryChallanIssueDetailEntity.setDChallanId(dchallanId);
					deliveryChallanIssueDetailEntity.setPartBranchId(bean.getPartBranchId());
					deliveryChallanIssueDetailEntity.setStockBinId(new BigInteger(stockBinId.trim()));
					deliveryChallanIssueDetailEntity.setMrp(bean.getMrp());
					deliveryChallanIssueDetailEntity.setIssuedQty(BigInteger.valueOf(bean.getBinDetailList().get(stockBinId.trim()).getIssueQty()));
					deliveryChallanIssueDetailEntity.setValue(bean.getValue());
					deliveryChallanIssueDetailEntity.setReturnedQty(bean.getReturnedQty());
					deliveryChallanIssueDetailEntity.setInvoiceQty(BigInteger.ZERO);
					deliveryChallanIssueDetailEntity.setPartId(bean.getPartId());
					deliveryChallanIssueDetailEntity.setFromStore(bean.getBinDetailList().get(stockBinId.trim()).getStoreName());
					deliveryChallanIssueDetailEntity.setBinList(bean.getBinDetailList().get(stockBinId.trim()).getBinLocation());
					deliveryChallanIssueDetailEntity.setVersionNo(bean.getVersionNo());
					deliveryChallanIssueDetailEntity.setCreatedBy(userId);
					deliveryChallanIssueDetailEntity.setCreatedDate(new Date());
					deliveryChallanIssueDetailEntity.setCustomerDtlId(bean.getCustomerId());
					deliveryChallanIssueDetailEntity.setPickListNumber(bean.getPickListNumber());
					deliveryChallanIssueDetailEntity.setPickListDtlId(bean.getPickListDtlId().intValue());
					session.save(deliveryChallanIssueDetailEntity);
					isSuccess =true;
					}
				}

			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);

		} finally {
			if (session != null && isSuccess) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {

				responseModel.setMsg("Delivery Challan Created Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setDeliveryId(dchallanId);
				responseModel.setDeliveryChallanNumber(requestModel.getDcNumber());

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
	public List<DCcustomerOrderResponse> customerOrderDtl(Integer productCategoryId, Integer partyId, String dcNumber, String reqType, BigInteger branchId) {

		Session session = null;
		List<DCcustomerOrderResponse> responseList = null;
		DCcustomerOrderResponse response = null;
		String statusType = null;
		NativeQuery<?> query = null;
		StringBuilder sqlQuery = new StringBuilder();
		
		if (reqType!= null && reqType.equals("Create")) {
			sqlQuery.append(
					"SELECT Distinct CH.Customer_Id, Customer_Order_Number, Customer_Order_Date,Customer_Order_Status, Dc_Isselect "
							+ "FROM PA_Customer_Order_HDR CH"
							+ "  inner join PA_Customer_Order_DTL CD on CH.Customer_Id = CD.Customer_Id "
							+ "where CH.PartyCode=" + partyId + " And ProductCategory=" + productCategoryId + " And CH.BRANCH_ID=" + branchId +" and  (Invoice_Qty = 0 or Invoice_Qty is null) AND (CASE WHEN is_pickList_created = 'Y' AND Issue_Qty != dc_issue_qty THEN 1 WHEN (is_pickList_created = 'N' OR is_pickList_created IS NULL) AND Order_Qty != dc_issue_qty THEN 1 ELSE 0 END) = 1");
		}
		if(reqType == null) {
//			sqlQuery.append(
//					"SELECT Distinct CH.Customer_Id, Customer_Order_Number, Customer_Order_Date,Customer_Order_Status, Dc_Isselect "
//							+ "FROM PA_Customer_Order_HDR CH"
//							+ "  inner join PA_Customer_Order_DTL CD on CH.Customer_Id = CD.Customer_Id "
//							+ " inner join PA_DCHALLAN_HDR dhdr on dhdr.PartyCode=CH.PartyCode "
//							+ "where CH.PartyCode=" + partyId + " And ProductCategory=" + productCategoryId + " and  (Invoice_Qty = 0 or Invoice_Qty is null)");
			
			sqlQuery.append("SELECT Distinct CH.Customer_Id, Customer_Order_Number, Customer_Order_Date, Customer_Order_Status, Dc_Isselect from PA_Customer_Order_HDR CH "
					+ "inner join PA_Customer_Order_DTL(nolock) CD on CH.Customer_Id = CD.Customer_Id "
					+ "inner join PA_DCHALLAN_ISSUE_DTL(nolock) dcih on dcih.Customer_dtl_Id=cd.Id "
					+ "inner join PA_DCHALLAN_HDR dhdr(nolock) on dhdr.PartyCode=CH.PartyCode and dhdr.dchallan_id=dcih.dchallan_id "
					+ " where ch.PartyCode="+ partyId + " And ProductCategory=" + productCategoryId + " And CH.BRANCH_ID=" + branchId +" and (Invoice_Qty = 0 or Invoice_Qty is null) ");
			sqlQuery.append(" And Dc_Isselect='Y' ");
		
		} else if(reqType.equals("Invoice")) {
			sqlQuery.append(
					"SELECT Distinct CH.Customer_Id, Customer_Order_Number, Customer_Order_Date,Customer_Order_Status, Dc_Isselect "
							+ " FROM PA_Customer_Order_HDR CH"
							+ " inner join PA_Customer_Order_DTL CD on CH.Customer_Id = CD.Customer_Id "
							+ " inner join PA_PICKLIST_DTL(nolock) PID on PID.Co_Dtl_Id = CD.Id "
							+ " where CH.PartyCode=" + partyId + " And ProductCategory=" + productCategoryId + ""
							+ " and (CD.Invoice_Qty != CD.Order_Qty)"
							+ " and (Customer_Order_Status = 'Submit' or Customer_Order_Status = 'PARTIALLY INVOICED')");
		}
		if(dcNumber!=null){
			sqlQuery.append(" and dhdr.DCNumber ='"+dcNumber+"'");
		}
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery.toString());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DCcustomerOrderResponse>();
				for (Object object : data) {
					Map row = (Map) object;

					response = new DCcustomerOrderResponse();
					statusType = (String) row.get("Customer_Order_Status");
					if (reqType!=null && reqType.equalsIgnoreCase("Invoice")) {
						response.setCustomerId((Integer) row.get("Customer_Id"));
						response.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
						response.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
					} else if (statusType.equals("Submit") || statusType.equals("Partially-DC")) {

						response.setCustomerId((Integer) row.get("Customer_Id"));
						response.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
						response.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
					} else if (reqType == null) {
						response.setCustomerId((Integer) row.get("Customer_Id"));
						response.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
						response.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
						response.setDcSelect(
								(Character) row.get("Dc_Isselect") != null ? (Character) row.get("Dc_Isselect") : 0);
					} else if (reqType != null && reqType.equals("Create")
							&& (row.get("Dc_Isselect") == null || (Character) row.get("Dc_Isselect") != 'Y')) {
						response.setDcSelect(
								(Character) row.get("Dc_Isselect") != null ? (Character) row.get("Dc_Isselect") : 0);
					} else {
						response.setDcSelect((Character) row.get("Dc_Isselect") != null ? (Character) row.get("Dc_Isselect") : 0);
					}
					
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
	public List<COpartDetailResponse> getDcPartDetail(String customerOrderNumber, String flag, String userCode) {

		Session session = null;
		if (logger.isDebugEnabled()) {
			logger.debug("customer order part detail invoked.." + customerOrderNumber);
		}
		Query query = null;
		COpartDetailResponse responseModel = null;
		List<COpartDetailResponse> list = new ArrayList<>();
		Integer recordCount = 0;
		String sqlQuery = "exec [PA_GET_DC_PRT_DTL] :userCode, :customerOrderNumber, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);	
			query.setParameter("customerOrderNumber", customerOrderNumber);
			query.setParameter("flag", flag);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					
					Integer orderQty = (Integer)row.get("Order_Qty");
					BigInteger issueQty = (BigInteger)row.get("Issue_Qty")!=null?(BigInteger)row.get("Issue_Qty"):BigInteger.ZERO;
					BigDecimal coBalanceQty = new BigDecimal(row.get("coBalanceQty") != null ? (BigInteger) row.get("coBalanceQty") : BigInteger.ZERO);
				
			
					//if(orderQty>issueQty.intValue()) {	
					responseModel = new COpartDetailResponse();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNumber((String) row.get("PartNumber"));
					responseModel.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
					responseModel.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
					responseModel.setPicklistnumber((String) row.get("Picklistnumber"));
					responseModel.setPickListDtlId(((BigInteger) row.get("PickListDtlId")).intValue());
					responseModel.setPartDesc((String) row.get("PartDesc"));
					responseModel.setProductSubCategory((String) row.get("PA_SUBCATEGORY_DESC"));
					responseModel.setTotalStock((Integer)row.get("OnHandQty"));
					responseModel.setCustomerOrderQty(orderQty);
					responseModel.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
					responseModel.setDealerMRP((BigDecimal) row.get("Dealer_MRP"));
					responseModel.setHsnCode((String) row.get("Hsn_code"));
					responseModel.setSgstPer((BigDecimal) row.get("SGST_PER"));
					responseModel.setIgstPer((BigDecimal) row.get("IGST_PER"));
					responseModel.setCgstPer((BigDecimal) row.get("CGST_PER"));
					responseModel.setPartStoreCount((BigDecimal) row.get("PARTSTORECOUNT"));
					responseModel.setStoreNames((String) row.get("StoreName"));
					responseModel.setBinLocations((String) row.get("binlocationName"));
					responseModel.setPartBranchId((Integer) row.get("partBranch_id"));
					responseModel.setCustomerDtlId((Integer) row.get("Id"));
					responseModel.setCoBalanceQty(row.get("coBalanceQty") != null ? (BigInteger) row.get("coBalanceQty") : BigInteger.ZERO);
					responseModel.setBinId(null);
					if(((String) row.get("Picklistnumber")).isEmpty()&& !((String) row.get("Picklistnumber")).isBlank()) {
						responseModel.setBinId((BigInteger)row.get("binId"));
					}
					
					responseModel.setCgstAmount((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(coBalanceQty).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setSgstAmount((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(coBalanceQty).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setIgstAmount((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(coBalanceQty).setScale(2, BigDecimal.ROUND_HALF_UP));
//					responseModel.setTotalGst((BigDecimal) row.get("NetAmount"));
					
					
					responseModel.setCgstAmount((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(coBalanceQty).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setSgstAmount((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(coBalanceQty).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setIgstAmount((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(coBalanceQty).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setTotalBasePrice(((BigDecimal) row.get("basicUnitPrice")).multiply(coBalanceQty).setScale(2, BigDecimal.ROUND_HALF_UP));
					
					responseModel.setMrp(((BigDecimal) row.get("basicUnitPrice")).multiply(coBalanceQty));
					responseModel.setTotalBasePrice(((BigDecimal) row.get("basicUnitPrice")).multiply(coBalanceQty));
					responseModel.setTotalSumOfIssue((Integer) row.get("totalSumOfIssue"));
					responseModel = getDcPartStockDetailCo(responseModel, responseModel.getCustomerDtlId(),
							responseModel.getPartBranchId(), userCode,responseModel.getBinId());
					list.add(responseModel);
					//}
					
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
	
	public DCpartDetailListResponse getDcPartStockDetail(DCpartDetailListResponse responseModel, Integer customerDtlId,
			Integer partBranchId, String userCode, BigInteger stockBinid) {

		Session session = null;
		Query query = null;
//		COpartDetailResponse responseModel = null;

		String sqlQuery = "exec [PA_GET_DC_STOCK_DTL] :customerDtlId, :partBranchId, :userCode,:stockBinid";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerDtlId", customerDtlId);
			query.setParameter("partBranchId", partBranchId);
			query.setParameter("userCode", userCode);
			query.setParameter("stockBinid", stockBinid);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setStoreId((Integer) row.get("storeId"));
					responseModel.setStockStoreId((Integer) row.get("stock_store_id"));
					responseModel.setBranchId((Integer) row.get("branch_id"));
					responseModel.setBinId((BigInteger) row.get("binId"));
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
		return responseModel;
	}

	public COpartDetailResponse getDcPartStockDetailCo(COpartDetailResponse responseModel, Integer customerDtlId,
			Integer partBranchId, String userCode,BigInteger stockBinid) {

		Session session = null;
		Query query = null;
//		COpartDetailResponse responseModel = null;

		String sqlQuery = "exec [PA_GET_DC_STOCK_DTL] :customerDtlId, :partBranchId, :userCode, :stockBinid";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerDtlId", customerDtlId);
			query.setParameter("partBranchId", partBranchId);
			query.setParameter("userCode", userCode);
			query.setParameter("stockBinid", stockBinid);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setStockBinid((BigInteger) row.get("stock_bin_id"));
					responseModel.setStoreId((Integer) row.get("storeId"));
					responseModel.setStockStoreId((Integer) row.get("stock_store_id"));
					responseModel.setBranchId((Integer) row.get("branch_id"));
					responseModel.setBinId((BigInteger) row.get("binId"));
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
		return responseModel;
	}

	@Override
	public List<DcPartQtyCalCulationResponse> getPartDetailsByDcQty(CustomerOrderPartNoRequest resquest) {

		Session session = null;
		if (logger.isDebugEnabled()) {
			logger.debug("DC order part detail invoked.." + resquest);
		}
		Query query = null;
		DcPartQtyCalCulationResponse responseModel = null;
		List<DcPartQtyCalCulationResponse> list = new ArrayList<>();
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_ORD_CALCULATEPOItemAmnt] :dealerId, :branchId, :PartId, :qty, :partyBranchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			
			query.setParameter("dealerId", resquest.getDealarId());
			query.setParameter("branchId", resquest.getBranchId());
			query.setParameter("PartId", resquest.getPartId());
			query.setParameter("qty", resquest.getOrderQty());
			query.setParameter("partyBranchId", resquest.getPartyBranchId());

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;

					responseModel = new DcPartQtyCalCulationResponse();

					responseModel.setCgst((BigDecimal) row.get("CGST_PER"));
					responseModel.setCgstAmount((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) resquest.getOrderQty())).setScale(2, BigDecimal.ROUND_HALF_UP));
					
					responseModel.setSgst((BigDecimal) row.get("SGST_PER"));
					responseModel.setSgstAmount((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) resquest.getOrderQty())).setScale(2, BigDecimal.ROUND_HALF_UP));
					
					responseModel.setIgst((BigDecimal) row.get("IGST_PER"));
					
					responseModel.setIgstAmount((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) resquest.getOrderQty())).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setTotalGst((BigDecimal) row.get("NetAmount"));
					responseModel.setMrp(((BigDecimal) row.get("basicUnitPrice")).multiply(BigDecimal.valueOf((Integer) resquest.getOrderQty())));
					responseModel.setTotalBasePrice(((BigDecimal) row.get("basicUnitPrice"))
							.multiply(BigDecimal.valueOf((Integer) resquest.getOrderQty())));

					
					responseModel.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));

//					responseModel.setStore((String) row.get("OnHandQty"));
//					responseModel.setBinlocation((String) row.get("Order_Qty"));

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

	@Override
	public List<DeliveryChallanNumberResponse> searchDeliveryChallanNumber(String searchText, String userCode) {
		Session session = null;
		Query query = null;
		DeliveryChallanNumberResponse responseModel = null;
		List<DeliveryChallanNumberResponse> responseListModel = null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "select DChallan_Id, DCNumber from PA_DCHALLAN_HDR where DCNumber LIKE " + "'%"
					+ searchText + "%'";
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				responseListModel = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DeliveryChallanNumberResponse();
					responseModel.setId((BigInteger) row.get("DChallan_Id"));
					responseModel.setDeliveryChallanNumber((String) row.get("DCNumber"));
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
	public DcSearchListResponse searchByDcFields(String userCode, DcSearchRequest resquest, Device device) {

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Integer totalRow = 0;
		boolean isSuccess = true;
		List<DcSearchBean> responseList = null;
		DcSearchBean responseModel = null;
		DcSearchListResponse bean = new DcSearchListResponse();
		Integer rowCount = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_SEARCH_DELIVERY_CHALLAN] :dealerId, :branchId, :stateId, :pcId, :userCode, :startDate, :endDate, :DcStatus, :DcNumber, :PartyTypeId, "
					+ ":ProductCategoryId, :PartyCodeId, :page, :size ";
			query = session.createSQLQuery(sqlQuery);
			
			query.setParameter("dealerId", resquest.getDealerId());
			query.setParameter("branchId", resquest.getBranchId());
			query.setParameter("stateId", resquest.getStateId());
			query.setParameter("pcId", resquest.getPcId());
			query.setParameter("userCode", userCode);
			query.setParameter("startDate", formatter.format(resquest.getStartDate()));
			query.setParameter("endDate", formatter.format(DateToStringParserUtils.addDayByOne(resquest.getEndDate())));
			query.setParameter("DcStatus", resquest.getDcStatus()); //
			query.setParameter("DcNumber", resquest.getDcNumber());
			query.setParameter("PartyTypeId", resquest.getPartyTypeId());
			query.setParameter("ProductCategoryId", resquest.getProductCategoryId());
//			query.setParameter("ProductSubCategoryId", resquest.getProductSubCategoryId());	
			query.setParameter("PartyCodeId", resquest.getPartyCodeId());
			query.setParameter("page", resquest.getPage());
			query.setParameter("size", resquest.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DcSearchBean>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DcSearchBean();
					responseModel.setId((BigInteger) row.get("DChallan_Id"));
					responseModel.setAction((String) row.get("Action"));
					responseModel.setCancelAction((String) row.get("CancelAction"));
					responseModel.setDeliveryChallanNumber((String) row.get("DCNumber"));
					responseModel.setPartyTypeName((String) row.get("PartyName"));
					responseModel.setBranchId((BigInteger) row.get("Branch_id"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setProductCategory((String) row.get("ProductCategory"));
					responseModel.setDcStatus((String) row.get("DCStatus"));
					responseModel.setDcDate((Date) row.get("DC_Date"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					// responseModel.setProductCategoryId((Integer)row.get("ProductCategory"));
					responseModel.setAddress((String) row.get("Address"));
					responseModel.setTehsil((String) row.get("TehsilDesc"));
					responseModel.setPinCode((String) row.get("PinCode"));
//					responseModel.setCustomDate((Date)row.get("Customer_Order_Date"));
					responseModel.setState((String) row.get("StateDesc"));
					responseModel.setCity((String) row.get("CityDesc"));
//					responseModel.setTotalPart((BigInteger) row.get("TotalPart"));
//					responseModel.setDistrict((String) row.get("DistrictDesc"));
					responseModel.setPostOffice((String) row.get("PostOffice"));
//					responseModel.setTotalQty((BigInteger) row.get("TotalQuantity"));
//					responseModel.setPartName((String) row.get("PartDesc"));
//					responseModel.setPartyType((String) row.get("PartyType"));
					totalRow = (Integer) row.get("totalCount");

					responseList.add(responseModel);
					rowCount++;
				}
				bean.setTotalRowCount(totalRow);
				bean.setSearchList(responseList);
				bean.setRowCount(rowCount);
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {
				bean.setStatusCode(WebConstants.STATUS_OK_200);
				bean.setMsg("Delivery Challan Fetches Successfully...");

			} else {
				bean.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return bean;
	}

	@Override
	public DeliveryChallanHeaderAndDetailResponse getDeliveryChallanHeaderById(String userCode,
			Integer deliveryChallanId) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Integer totalRow = 0;
		boolean isSuccess = true;
		DeliveryChallanHeaderAndDetailResponse responseModel = null;

		Integer rowCount = 0;

		try {

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_VIEW_DELIVERY_CHALLAN] :DChallanId, :userCode, :Flag ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DChallanId", deliveryChallanId);
			query.setParameter("userCode", userCode);
			query.setParameter("Flag", "Header");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DeliveryChallanHeaderAndDetailResponse();

					responseModel.setDChallanId((BigInteger) row.get("DChallan_Id"));
					responseModel.setDcNumber((String) row.get("DCNumber"));
					responseModel.setDcStatus((String) row.get("DCStatus"));
					responseModel.setPartyType((String) row.get("Party_Type"));
					responseModel.setBranchId((BigInteger) row.get("Branch_id"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setProductCategory((String) row.get("ProductCategory"));
					if (row.get("DCStatus").equals("Submit")) {
						responseModel.setDcStatus("DC Generated");
					}
					if (row.get("DCStatus").equals("Cancel")) {
						responseModel.setDcStatus("DC Cancelled");
					}
					responseModel.setDcDate((Date) row.get("DC_Date"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setProductCategoryId((Integer) row.get("productCategoryId"));
					responseModel.setAddress((String) row.get("Address"));
					responseModel.setTahsilDesc((String) row.get("tehsilDesc"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setStateDesc((String) row.get("StateDesc"));
					responseModel.setCityDesc((String) row.get("CityDesc"));
					responseModel.setPostOffice((String) row.get("PostOffice"));
					responseModel.setTotalIssuedQty((BigDecimal) row.get("TotalQty"));
					responseModel.setPartName((String) row.get("partName"));
					responseModel.setTotalValue((BigDecimal) row.get("TotalPart"));
					responseModel.setDistrictDesc((String) row.get("DistrictDesc"));
					responseModel.setPartyBranchId((BigInteger) row.get("partyBranchId"));
					responseModel.setPartyName((String) row.get("PartyName"));

				}
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
		}
		return responseModel;
	}

	@Override
	public List<DCpartDetailListResponse> getDeliveryChallanDetailById(String userCode, Integer deliveryChallanId) {

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Integer totalRow = 0;
		boolean isSuccess = true;
		List<DCpartDetailListResponse> list = null;
		DCpartDetailListResponse responseModel = null;

		Integer rowCount = 0;

		try {
			Integer issueSum=0;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_VIEW_DELIVERY_CHALLAN] :DChallanId,:userCode, :Flag ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DChallanId", deliveryChallanId);
			query.setParameter("userCode", userCode);
			query.setParameter("Flag", "Detail");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				list = new ArrayList<>();
				for (Object object : data) {

					Map row = (Map) object;
					responseModel = new DCpartDetailListResponse();

					responseModel.setDChallanDtlId((BigInteger) row.get("DChallan_Dtl_Id"));
					responseModel.setPart_id((Integer) row.get("part_id"));
					responseModel.setBasicUnitPrice((BigDecimal)row.get("basicUnitPrice"));
					responseModel.setDealerMRP((BigDecimal) row.get("basicUnitPrice"));
					responseModel.setHsnCode((String) row.get("Hsn_code"));
					responseModel.setPartNumber((String) row.get("partnumber"));
					responseModel.setCurrentStock((Integer) row.get("CurrentStock"));
					responseModel.setPartDesc((String) row.get("PArtdesc"));
					responseModel.setPaSubCategoryDesc((String) row.get("PA_SUBCATEGORY_DESC"));
					responseModel.setIssuedQty((BigDecimal) row.get("IssuedQty"));
					responseModel.setFromStore((String) row.get("From_Store"));
					responseModel.setBinList((String) row.get("Bin_List"));
					responseModel.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
					responseModel.setPicklistnumber((String)row.get("Picklistnumber"));
					responseModel.setPickListDtlId((Integer) row.get("pickListDtlId"));
					responseModel.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
					responseModel.setCoQty((Integer) row.get("Order_Qty"));
					responseModel.setCgstPer((BigDecimal) row.get("CGST_PER"));
					responseModel.setCgstAmt((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply((BigDecimal) row.get("IssuedQty")!=null? ((BigDecimal) row.get("IssuedQty")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setSgstPer((BigDecimal) row.get("SGST_PER"));
					responseModel.setSgstAmt((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply((BigDecimal) row.get("IssuedQty")!=null? ((BigDecimal) row.get("IssuedQty")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setIgstPer((BigDecimal) row.get("IGST_PER"));
					responseModel.setIgstAmt((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply((BigDecimal) row.get("IssuedQty")!=null? ((BigDecimal) row.get("IssuedQty")):BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
					responseModel.setNetAmount((BigDecimal) row.get("NetAmount"));
					responseModel.setPartBranchId((Integer) row.get("partBranch_id"));
					responseModel.setCustomerDtlId((Integer) row.get("Id"));
					responseModel.setBalanceQty(((BigDecimal)row.get("balanceQty")).intValue());
					responseModel.setStockBinid((BigInteger) row.get("Stock_Bin_Id"));
					responseModel = getDcPartStockDetail(responseModel, responseModel.getCustomerDtlId(),
							responseModel.getPartBranchId(), userCode, responseModel.getStockBinid());
					
					list.add(responseModel);
				}
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
		}
		return list;

	}

	@Override
	public SpareDcCreateResponse updateStatusAndRemark(String userCode, DcUpdateStatusRequest requestModel) {

		SpareDcCreateResponse responseModel = null;
		Session session = null;
		Transaction transaction = null;
		boolean isSuccess = true;
		DeliveryChallanHdrEntity entity = null;
		Map<String, Object> mapData = null;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			
			mapData = fetchUserDTLByUserCode(session, userCode);

			responseModel = new SpareDcCreateResponse();
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}

			entity = (DeliveryChallanHdrEntity) session.get(DeliveryChallanHdrEntity.class,requestModel.getDchallanId());
			if(entity!=null) {
			transaction = session.beginTransaction();
			entity.setDchallanId(requestModel.getDchallanId());
			entity.setDcStatus(requestModel.getStatus());
			entity.setRemarks(requestModel.getRemarks());
			entity.setModifiedBy(userId);
			entity.setModifiedDate(new Date());
			session.update(entity);
			}
		} catch (SQLGrammarException ex) {
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

				responseModel.setMsg("Delivery Challan Updated Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setDeliveryId(requestModel.getDchallanId());
				responseModel.setDeliveryChallanNumber(entity.getDcNumber());

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}

		}
		return responseModel;
	}

	@Override
	public void deleteStockInventory(List<DeliveryChallanPartDetailRequest> dcPartDetailMap, String userCode) {

		List<String> stockBinList = new ArrayList<String>();
		
		List<DeliveryChallanPartDetailRequest> dcPartDetailList = new ArrayList<>(dcPartDetailMap);
		String sqlQuery = "exec [SP_PA_DC_UPDATE_STOCK] :Qty, :stockbinid, :partbranchid, :stockstoreid, :partid, :branchId,"
				+ ":basicUnitPrice, :modifiedBy, :branch_store_id, :DChallanDtlId ";
		org.hibernate.query.Query<?> query = null;
		try {
			for (DeliveryChallanPartDetailRequest req : dcPartDetailList) {
				
				String[] binList = req.getStockBinId().split(",");
				stockBinList = Arrays.asList(binList);
				
				Session session = sessionFactory.getCurrentSession();
				query = session.createSQLQuery(sqlQuery);
				for(String binBean:stockBinList) {
				query.setParameter("Qty", req.getBinDetailList().get(binBean.trim()).getIssueQty().intValue());
				query.setParameter("stockbinid",Integer.parseInt(binBean.trim()));
				query.setParameter("partbranchid", req.getPartBranchId());
				query.setParameter("stockstoreid", req.getBinDetailList().get(binBean.trim()).getStockStoreId());
				query.setParameter("partid", req.getPartId());
				query.setParameter("branchId", req.getBranchId());
				query.setParameter("basicUnitPrice", req.getBasicUnitPrice());
				query.setParameter("modifiedBy", userCode);
				query.setParameter("branch_store_id", req.getBinDetailList().get(binBean.trim()).getBranchStoreId());
				query.setParameter("DChallanDtlId", req.getCustomerId());

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				query.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addStockInventory(List<DcUpdateSparePartRequest> sparePartDetails, String userCode) {
		
		String sqlQuery = "exec [SP_PA_DC_ADD_STOCK] :Qty, :stockbinid, :partbranchid, :stockstoreid, :partid, :branchId,"
				+ ":basicUnitPrice, :modifiedBy, :branch_store_id, :DChallanDtlId ";
		org.hibernate.query.Query<?> query = null;
		try {
			for (DcUpdateSparePartRequest req : sparePartDetails) {
				Session session = sessionFactory.getCurrentSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("Qty", req.getIssuedQty());
				query.setParameter("stockbinid", req.getStockBinid());
				query.setParameter("partbranchid", req.getPartBranchId());
				query.setParameter("stockstoreid", req.getStockStoreId());
				query.setParameter("partid", req.getPartId());
				query.setParameter("branchId", req.getBranchId());
				query.setParameter("basicUnitPrice", req.getBasicUnitPrice());
				query.setParameter("modifiedBy", userCode);
				query.setParameter("branch_store_id", req.getStoreId());
				query.setParameter("DChallanDtlId", req.getCustomerDtlId());

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				query.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	

}
