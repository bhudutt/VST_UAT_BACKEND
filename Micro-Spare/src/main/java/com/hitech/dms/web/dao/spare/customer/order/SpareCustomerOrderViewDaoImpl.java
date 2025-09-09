package com.hitech.dms.web.dao.spare.customer.order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderSearchRequest;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderPartDetailResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderSearchListResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderViewListResponseModel;

@Repository
public class SpareCustomerOrderViewDaoImpl implements SpareCustomerOrderViewDao {

	private static final Logger logger = LoggerFactory.getLogger(SpareCustomerOrderViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public SpareCustOrderSearchResponseModel searchCODetailByStartAndEndDate(String authorizationHeader,
			String userCode, CustomerOrderSearchRequest requestMap, Device device) throws ParseException {

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Integer totalRow = 0;
		boolean isSuccess = true;
		List<SpareCustOrderSearchListResponseModel> responseList = null;
		SpareCustOrderSearchListResponseModel responseModel = null;
		SpareCustOrderSearchResponseModel bean = new SpareCustOrderSearchResponseModel();
		Integer rowCount = 0;
		try {

			// "exec [SP_SEARCH_CUSTOMER_ORDER] :startDate, :endDate, :profitCenter,
			// :dealership, :branchName,:customerOrderStatus, :partyType, :productCategory,
			// :productSubCategory, :partyCode, :page, :size ";

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_SEARCH_CUSTOMER_ORDER] :DEALERID, :BRANCHID, :PCID, :STATE, :startDate, :endDate, :Status, :CustomerOrderNumber, :PartyTypeId, "
					+ ":ProductCategoryId, :ProductSubCategoryId, :PartyCodeId, :userCode,  :page, :size ";

			// :pcId, :zoneId, :dealerId, :territoryId, :stateId, :branchId, 
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DEALERID", requestMap.getDealerId());
			query.setParameter("BRANCHID", requestMap.getBranchId());
			query.setParameter("PCID", requestMap.getPcId());
			query.setParameter("STATE", requestMap.getStateId());
			query.setParameter("startDate", requestMap.getStartDate());
			query.setParameter("endDate", DateToStringParserUtils.addEndTimeOFTheDay(requestMap.getEndDate()));
			query.setParameter("Status", requestMap.getCustomerOrderStatus()); 
			query.setParameter("CustomerOrderNumber", requestMap.getCustomerOrderNumber());
			query.setParameter("PartyTypeId", requestMap.getPartyTypeId());
			query.setParameter("ProductCategoryId", requestMap.getProductCategoryId());
			query.setParameter("ProductSubCategoryId", requestMap.getProductSubCategoryId());
			query.setParameter("PartyCodeId", requestMap.getPartyCodeId());
			query.setParameter("userCode", userCode);
			query.setParameter("page", requestMap.getPage());
			query.setParameter("size", requestMap.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SpareCustOrderSearchListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SpareCustOrderSearchListResponseModel();
					responseModel.setId((Integer) row.get("Customer_Id"));
					responseModel.setAction((String) row.get("Action"));
//					responseModel.setCustomerOrderPick((String) row.get("customerOrderPick"));
					responseModel.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setBranchId((BigInteger) row.get("Branch_id"));
					responseModel.setProductCategory((String) row.get("ProductCategory"));
					responseModel.setCustomerOrderStatus((String) row.get("Customer_Order_Status"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					// responseModel.setProductCategoryId((Integer)row.get("ProductCategory"));
					responseModel.setAddress((String) row.get("Address"));
					responseModel.setTehsil((String) row.get("TehsilDesc"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
					responseModel.setState((String) row.get("StateDesc"));
					responseModel.setCity((String) row.get("CityDesc"));
					responseModel.setTotalPart((BigInteger) row.get("TotalPart"));
					responseModel.setDistrict((String) row.get("DistrictDesc"));
					responseModel.setPostOffice((String) row.get("PostOffice"));
					responseModel.setTotalQty((BigInteger) row.get("TotalQuantity"));
//					responseModel.setPartName((String) row.get("PartDesc"));
					responseModel.setPartyType((String) row.get("PartyType"));
					responseModel.setProductSubCategory((String) row.get("Product_SubCategory"));
					totalRow = (Integer) row.get("totalCount");

					responseList.add(responseModel);
					rowCount++;
				}
				bean.setTotalRowCount(totalRow);
				bean.setDetailList(responseList);
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

			} else {
				bean.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return bean;
	}

	@Override
	public SpareCustOrderViewListResponseModel getCustomerOrderById(String userCode, Integer customerOrderId) {

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		boolean isSuccess = true;
		Integer rowCount = 0;
		SpareCustOrderViewListResponseModel responseModel = null;
		SpareCustOrderPartDetailResponse partDetail = null;
		List<SpareCustOrderPartDetailResponse> response = new ArrayList<SpareCustOrderPartDetailResponse>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_VIEW_CUSTOMER_ORDER] :customerOrderId, :Flag";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerOrderId", customerOrderId);
			query.setParameter("Flag", "HEADER");

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SpareCustOrderViewListResponseModel();
					responseModel.setId((Integer) row.get("Customer_Id"));
					responseModel.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
					responseModel.setCustomerOrderStatus((String) row.get("Customer_Order_Status"));
					responseModel.setBranchId((BigInteger) row.get("Branch_id"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setPartyCategoryName((String) row.get("Party_Type"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPo_Category_Desc((String) row.get("ProductCategory"));
					responseModel.setAddress((String) row.get("Address"));
					// responseModel.setPartyType((String) row.get("Address"));
					responseModel.setBranchPartyCodeId((BigInteger) row.get("party_branch_id"));;
					responseModel.setTehsil((String) row.get("tehsilDesc"));
					responseModel.setPinId((BigInteger) row.get("pin_id"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setCustomDate((Date) row.get("Customer_Order_Date"));
					responseModel.setState((String) row.get("StateDesc"));
					responseModel.setCity((String) row.get("CityDesc"));
					responseModel.setTotalPart((BigInteger) row.get("TotalPart"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setDistrict((String) row.get("DistrictDesc"));
					responseModel.setPostOffice((String) row.get("PostOffice"));
					responseModel.setTotalQty((BigInteger) row.get("TotalQuantity"));
					responseModel.setPartName((String) row.get("PartDesc"));
					responseModel.setProductCategoryId((Integer) row.get("productCategoryId"));
					responseModel.setMobileNo((String) row.get("mobileNo"));
					responseModel.setPartyTypeId((Integer)row.get("PartyTypeId"));
					Boolean isPickListCreated = false;
					if (row.get("is_pickList_created")!=null &&((Character) row.get("is_pickList_created")).toString().equalsIgnoreCase("Y")) {
						isPickListCreated = true;
					}
					responseModel.setIsPickListCreated(isPickListCreated);

//					//Added Part Details
//					
//					  partDetail = new SpareCustOrderPartDetailResponse();
//					  
//					  partDetail.setPartId((Integer) row.get("part_id"));
//					  partDetail.setPartNo((String)row.get("partNumber"));
//					  partDetail.setPartDesc((String)row.get("PartDesc"));
//					  partDetail.setProductSubCategory((String) row.get("ProductSubCategory"));
////					  partDetail.setBasicUnitPrice((Integer) row.get("basicUnitPrice"));
//					  
//					  partDetail.setHSNCode((String)row.get("HSN_CODE"));
//					  partDetail.setCgst((BigDecimal) row.get("CGST_PER"));
//					  partDetail.setCgstAmount((BigDecimal) row.get("CGST_AMT"));
//					  partDetail.setSgst((BigDecimal) row.get("SGST_PER"));
//					  partDetail.setSgstAmount((BigDecimal) row.get("SGST_AMT"));
//					  partDetail.setIgst((BigDecimal) row.get("IGST_PER"));
//					  partDetail.setIgstAmount((BigDecimal) row.get("SGST_AMT"));
//					  partDetail.setTotalGst((BigDecimal)row.get("NetAmount"));
//					  response.add(partDetail);

				}

				// responseModel.setPartDetailList(response);
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				responseModel.setMsg("Customer order details have been fetch successfully.");

			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@Override
	public List<SpareCustOrderPartDetailResponse> getCustomerOrderDetailsById(String userCode,
			Integer customerOrderId) {

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		boolean isSuccess = true;
		Integer rowCount = 0;
		// SpareCustOrderViewListResponseModel responseModel =null;
		SpareCustOrderPartDetailResponse partDetail = null;
		List<SpareCustOrderPartDetailResponse> response = new ArrayList<SpareCustOrderPartDetailResponse>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String sqlQuery = "exec [SP_VIEW_CUSTOMER_ORDER] :customerOrderId, :Flag";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerOrderId", customerOrderId);
			query.setParameter("Flag", "DETAIL");

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;

					// Added Part Details

					partDetail = new SpareCustOrderPartDetailResponse();
					partDetail.setId((Integer) row.get("id"));
					partDetail.setCurrentStock((Integer) row.get("CurrentStock"));
					partDetail.setPartId((Integer) row.get("part_id"));
					partDetail.setPartNo((String) row.get("partnumber"));
					partDetail.setPartDesc((String) row.get("PArtdesc"));
					partDetail.setProductSubCategory((String) row.get("PA_SUBCATEGORY_DESC"));
					partDetail.setMrp((BigDecimal) row.get("Dealer_MRP"));
					partDetail.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
					partDetail.setOrderQty((Integer) row.get("Order_Qty"));
					partDetail.setIssueQty((BigInteger) row.get("Issue_Qty"));
					partDetail.setBalanceQty((BigInteger) row.get("Balance_Qty"));
					partDetail.setTotalBasePrice(((BigDecimal) row.get("basicUnitPrice"))
							.multiply(BigDecimal.valueOf((Integer) row.get("Order_Qty")!=null?(Integer) row.get("Order_Qty"):0.0)));
					partDetail.setHSNCode((String) row.get("Hsn_code"));
			
					partDetail.setCgst((BigDecimal) row.get("CGST_PER"));
					partDetail.setCgstAmount((((BigDecimal) row.get("CGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) row.get("Order_Qty")!=null?(Integer) row.get("Order_Qty"):0.0)).setScale(2, BigDecimal.ROUND_HALF_UP));
					partDetail.setSgst((BigDecimal) row.get("SGST_PER"));
					partDetail.setSgstAmount((((BigDecimal) row.get("SGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) row.get("Order_Qty")!=null?(Integer) row.get("Order_Qty"):0.0)).setScale(2, BigDecimal.ROUND_HALF_UP));
					partDetail.setIgst((BigDecimal) row.get("IGST_PER"));
					partDetail.setIgstAmount((((BigDecimal) row.get("IGST_PER")).divide(BigDecimal.valueOf(100)).multiply((BigDecimal) row.get("basicUnitPrice"))).multiply(BigDecimal.valueOf((Integer) row.get("Order_Qty")!=null?(Integer) row.get("Order_Qty"):0.0)).setScale(2, BigDecimal.ROUND_HALF_UP));
					partDetail.setTotalGst((BigDecimal) row.get("NetAmount"));
					partDetail.setStore((String) row.get("FromStore"));
					partDetail.setBinlocation((String) row.get("StoreBinLocation"));
					if((BigInteger) row.get("partBranchId")!=null) {
					partDetail.setPartBranchId(((BigInteger) row.get("partBranchId")).intValue());
					}
					response.add(partDetail);
				}

				// responseModel.setPartDetailList(response);
			}

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				transaction.commit();
				session.close();
			}
//			if (isSuccess) {
//				response.setStatusCode(WebConstants.STATUS_OK_200);
//				response.setMsg("Customer order details have been fetch successfully.");
//			
//			} else {
//				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//			}
		}
		return response;
	}

}
