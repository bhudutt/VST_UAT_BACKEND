package com.hitech.dms.web.dao.spare.sale.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.spare.sale.invoice.SaleInvoiceDtlEntity;
import com.hitech.dms.web.entity.spare.sale.invoice.SaleInvoiceEntity;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.SpareModel.partSerachDetailsByPohdrIdResponseModel;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanNumberResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.model.spare.sale.invoice.SalesInvoiceDCResponse;
import com.hitech.dms.web.model.spare.sale.invoice.request.CustomerOrderOrDCRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SpareSalesInvoiceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.TaxDetailsRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.PartTaxCalCulationResponse;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;
import com.hitech.dms.web.model.spare.search.response.SparePoHdrDetailsResponse;

@Repository
public class SpareSaleInvoiceDaoImpl implements SpareSaleInvoiceDao {
	private static final Logger logger = LoggerFactory.getLogger(SpareSaleInvoiceDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	CommonDao commonDao;

	@Override
	public HashMap<BigInteger, String> fetchReferenceDocList(Integer dealerId, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec PA_GET_SALE_INVOICE_REF_DOC_TYPE :dealerId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("ref_doc_id"), (String) row.get("ref_doc_name"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return searchList;
	}

	@Override
	public List<SalesInvoiceDCResponse> deliveryChallanDtl(Integer partyBranchId, String reqType) {
		Session session = null;
		List<SalesInvoiceDCResponse> responseList = null;
		SalesInvoiceDCResponse response = null;
		NativeQuery<?> query = null;
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("select DChallan_Id, DCNumber, DC_Date, DCStatus " + "from PA_DCHALLAN_HDR where PartyCode = "
				+ partyBranchId + "and (DCStatus = 'Submit' or  DCStatus = 'PARTIALLY INVOICED')");
//		if (reqType == null) {
//			sqlQuery.append(" And Dc_Isselect='Y'");
//		}
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery.toString());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SalesInvoiceDCResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SalesInvoiceDCResponse();
					response.setDChallanId((BigInteger) row.get("DChallan_Id"));
					response.setDcNumber((String) row.get("DCNumber"));
					response.setDcDate((Date) row.get("DC_Date"));
//					if (row.get("Dc_Isselect") != null) {
					response.setDcStatus((String) row.get("DCStatus"));
//					}
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
	public List<COpartDetailResponse> getDcPartDetail(String deliveryChallanNumber) {

		Session session = null;
		if (logger.isDebugEnabled()) {
			logger.debug("delivery challan order part detail invoked.." + deliveryChallanNumber);
		}
		Query query = null;
		COpartDetailResponse responseModel = null;
		List<COpartDetailResponse> list = new ArrayList<>();
		Integer recordCount = 0;
		String sqlQuery = "exec [PA_GET_SALE_INVOICE_DC_PRT_DTL] :deliveryChallanNumber";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("deliveryChallanNumber", deliveryChallanNumber);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new COpartDetailResponse();

					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPartNumber((String) row.get("PartNumber"));
					responseModel.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
					responseModel.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
					responseModel.setPartDesc((String) row.get("PartDesc"));
					responseModel.setProductSubCategory((String) row.get("PA_SUBCATEGORY_DESC"));
					responseModel.setTotalStock((Integer) row.get("OnHandQty"));
					responseModel.setCustomerOrderQty((Integer) row.get("Order_Qty"));

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
	public ApiResponse<List<SparePoHdrDetailsResponse>> fetchPoHdrDetailsById(String userCode, Integer poHdrId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPoHdrDetailsById invoked.." + userCode + " " + poHdrId);
		}
		Session session = null;
		Query query = null;
		String msg = null;

		ApiResponse<List<SparePoHdrDetailsResponse>> apiResponse = new ApiResponse<>();

		List<SparePoHdrDetailsResponse> responseModelList = null;
		String sqlQuery = "exec [PA_GET_PO_HDR_DETAILS_FOR_INV] :poHdrId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("poHdrId", poHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePoHdrDetailsResponse>();
				SparePoHdrDetailsResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SparePoHdrDetailsResponse();
					msg = (String) row.get("Message");
					if (msg != null && msg.equalsIgnoreCase("Duplicate PO Order")) {
						apiResponse.setMessage(msg);
						apiResponse.setStatus(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					} else {
						responseModel.setPoHdrId((BigInteger) row.get("po_hdr_id"));
						responseModel.setBranchName((String) row.get("BranchName"));
						responseModel.setPoNumber((String) row.get("PONumber"));
						responseModel.setPoStatus((String) row.get("POStatus"));
						responseModel.setPoON((String) row.get("poON"));
						responseModel.setPOCreationDate((Date) row.get("POCreationDate"));
						responseModel.setPartyName((String) row.get("partyName"));
						responseModel.setPartyCode((String) row.get("PartyCode"));
						responseModel.setProductCategory((String) row.get("CategoryCode"));
						responseModel.setPOType((String) row.get("poType"));
						responseModel.setRemarks((String) row.get("Remarks"));
						responseModel.setJobCardNo((String) row.get("jobcard_no"));
						responseModel.setRso((String) row.get("plantName") + "-" + (String) row.get("plantCode"));
						responseModel.setTotalQty((BigDecimal) row.get("TotalQty"));
						responseModel.setTotelItem((BigDecimal) row.get("TotalItems"));
						responseModel.setPoReleaseDate((Date) row.get("POReleaseDate"));
						responseModel.setTotalBaseAmount((BigDecimal) row.get("totalBaseAmount"));
						responseModel.setTotalGstAmount((BigDecimal) row.get("totalGstAmount"));
						responseModel.setTcsPercent((String) row.get("tcsPercent"));
						responseModel.setTotalPoAmount((BigDecimal) row.get("totalPoAmount"));
						responseModel.setTotalTcsAmount((BigDecimal) row.get("totalTcsAmount"));
						responseModel.setDealerId((BigInteger) row.get("parent_dealer_id"));
						responseModel.setBranchId((BigInteger) row.get("branch_id"));
						responseModel.setCategoryId((Integer) row.get("PO_Category_Id"));
						responseModelList.add(responseModel);
						apiResponse.setMessage("PO Details Fetched Successfully");
						apiResponse.setStatus(WebConstants.STATUS_OK_200);
						apiResponse.setResult(responseModelList);
						;
					}
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
		return apiResponse;

	}

	@Override
	public List<partSerachDetailsByPohdrIdResponseModel> fetchPoPartDetailsByPohdrId(String userCode, Integer poHdrId) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchPoPartsDetailsBypoHdrId invoked.." + userCode + " " + poHdrId);
		}
		Session session = null;
		Query query = null;
		List<partSerachDetailsByPohdrIdResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_GET_PO_SPARE_PARTS_DETAILS] :poHdrId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("poHdrId", poHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<partSerachDetailsByPohdrIdResponseModel>();
				partSerachDetailsByPohdrIdResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new partSerachDetailsByPohdrIdResponseModel();
					responseModel.setPartId((Integer) row.get("part_id"));
					responseModel.setPohdrId((BigInteger) row.get("poHdrId"));
					responseModel.setPartNumber((String) row.get("PartNumber"));
					responseModel.setPartDesc((String) row.get("PartDesc"));
					partSearchResponseModel partModel = new partSearchResponseModel();
					partModel.setPartId(responseModel.getPartId());
					partModel.setPartNo(responseModel.getPartNumber());
					responseModel.setPartDtl(partModel);
					responseModel.setProductSubCategory((String) row.get("PARTSUBCATEGORY"));
					responseModel.setPackQty((Integer) row.get("PackQty") != null ? (Integer) row.get("PackQty") : 0);
					responseModel.setMinOrderQty(
							(Integer) row.get("MinOrderQty") != null ? (Integer) row.get("MinOrderQty") : 0);
					responseModel.setCurrentStock(
							(Integer) row.get("CurrentStock") != null ? (Integer) row.get("CurrentStock") : 0);
					responseModel.setBackOrderQty(
							(Integer) row.get("BackOrderQty") != null ? (Integer) row.get("BackOrderQty") : 0);
					responseModel.setTransitQty(
							(Integer) row.get("InTransitQty") != null ? (Integer) row.get("InTransitQty") : 0);
					responseModel.setBasicUnitPrice(
							(BigDecimal) row.get("BasicUnitPrice") != null ? (BigDecimal) row.get("BasicUnitPrice")
									: null);
					responseModel.setOrderQty(
							(BigDecimal) row.get("quantity") != null ? (BigDecimal) row.get("quantity") : null);
					responseModel.setTotalBasePrice(
							(BigDecimal) row.get("net_amount") != null ? (BigDecimal) row.get("net_amount") : null);
					responseModel.setGSTAmount(
							(BigDecimal) row.get("gst_amount") != null ? (BigDecimal) row.get("gst_amount") : null);
					responseModel.setGSTP((BigDecimal) row.get("gst_percent"));
					responseModel.setAmount(
							(BigDecimal) row.get("total_amount") != null ? (BigDecimal) row.get("total_amount") : null);
					responseModel.setSONO((String) row.get("SONO"));
					responseModel.setSODate((String) row.get("SODATE"));
					responseModel.setSAPRemarks((String) row.get("SAPREMARKS"));
					responseModel.setViewImage((String) row.get("VIEWIMAGE"));

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

	@Transactional
	@Override
	public SpareSalesInvoiceResponse createSpareSaleInvoice(String userCode,
			SpareSalesInvoiceRequest salesInvoiceRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
//			logger.debug(salesInvoiceRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = new SpareSalesInvoiceResponse();
		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;
		String generatedNumber = "";
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				SaleInvoiceEntity saleInvoiceEntity = new SaleInvoiceEntity();
				generatedNumber = generateDocNumber(salesInvoiceRequest.getBranchId(), "INV");
				if (generatedNumber != null) {
					saleInvoiceEntity.setDocNumber(generatedNumber);
					saleInvoiceEntity.setDocDate(todayDate);
					saleInvoiceEntity.setDocType(salesInvoiceRequest.getReferenceDocumentId());
					saleInvoiceEntity.setBranchId(salesInvoiceRequest.getBranchId());
					saleInvoiceEntity.setPoHdrId(salesInvoiceRequest.getPoHdrId());
					saleInvoiceEntity.setCustomerId(salesInvoiceRequest.getCustomerId());
					saleInvoiceEntity.setPartyAddLine1(salesInvoiceRequest.getAddress());
					saleInvoiceEntity.setCategoryCode(salesInvoiceRequest.getProductCategoryCode());
					saleInvoiceEntity.setPinId(salesInvoiceRequest.getPinId());
					saleInvoiceEntity.setCustomerName(salesInvoiceRequest.getCustomerName());
					saleInvoiceEntity.setMobileNo(salesInvoiceRequest.getMobileNo());
					saleInvoiceEntity.setPartyCode(salesInvoiceRequest.getPartyCode());
					saleInvoiceEntity.setTransporterName(salesInvoiceRequest.getTransporterName());
					saleInvoiceEntity.setTransporterVehicleNo(salesInvoiceRequest.getTransporterVehicleNo());
					saleInvoiceEntity.setLrNo(salesInvoiceRequest.getLrNo());
					saleInvoiceEntity.setSplDiscountType(salesInvoiceRequest.getSplDiscountType());
					saleInvoiceEntity.setSplDiscountRate(salesInvoiceRequest.getSplDiscountPercent());
					saleInvoiceEntity.setTotalSplDiscountValue(salesInvoiceRequest.getTotalSplDiscountValue());
					saleInvoiceEntity.setCreatedBy(userCode);
					saleInvoiceEntity.setCreatedDate(todayDate);
					saleInvoiceEntity.setTBasicValue(salesInvoiceRequest.getTotalBasicValue());
					saleInvoiceEntity.setTNetValue(salesInvoiceRequest.getTotalNetValue());
					saleInvoiceEntity.setTDiscountValue(salesInvoiceRequest.getTotalDiscountValue());
					saleInvoiceEntity.setTTaxValue(salesInvoiceRequest.getTotalTaxValue());
					saleInvoiceEntity.setTTaxableValue(salesInvoiceRequest.getTotalTaxableAmount());
					saleInvoiceEntity.setOtherCharges(salesInvoiceRequest.getOtherCharges());
					saleInvoiceEntity.setTcsPercent(salesInvoiceRequest.getTcsPercent());
					saleInvoiceEntity.setTcsAmount(salesInvoiceRequest.getTcsAmount());
					saleInvoiceEntity.setTBillValue(salesInvoiceRequest.getTotalBillValue());
					saleInvoiceEntity.setForwardingPackagingAmount(salesInvoiceRequest.getForwardingPackagingAmount());	
					saleInvoiceEntity.setForwardingPackagingGst(salesInvoiceRequest.getForwardingPackagingGst());
					saleInvoiceEntity.setForwardGstAmount(salesInvoiceRequest.getForwardGstAmount());
					
					id = (BigInteger) session.save(saleInvoiceEntity);
					spareSalesInvoiceResponse.setId(id);
					updateDocumentNumber(generatedNumber, "Sales Invoice", salesInvoiceRequest.getBranchId(), "INV",
							session);
					isSuccess = true;

					isSuccess = saveInvoiceDtlAndUpdateStock(session, salesInvoiceRequest, id, userCode, userId);

				} else {
					isSuccess = false;
				}

			}
			if (isSuccess) {
				transaction.commit();
				session.close();
				spareSalesInvoiceResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Sapre Sale Invoice Created Successfully with Invoice Number - " + generatedNumber;
				spareSalesInvoiceResponse.setMsg(msg);
			} else {
				transaction.rollback();
				session.close();
				msg = "Spare Sale Invoice Not Created";
				spareSalesInvoiceResponse.setStatusCode(statusCode);
				spareSalesInvoiceResponse.setMsg(msg);
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return spareSalesInvoiceResponse;
	}

	private boolean saveInvoiceDtlAndUpdateStock(Session session, SpareSalesInvoiceRequest salesInvoiceRequest,
			BigInteger saleInvoiceId, String userCode, BigInteger userId) {
		boolean isSuccess = true;

		BigInteger referenceDocId = salesInvoiceRequest.getReferenceDocumentId();

		boolean isCustomerOrder = referenceDocId.equals(BigInteger.valueOf(2));
		boolean isDcOrder = referenceDocId.equals(BigInteger.valueOf(3));

		boolean isPoOrder = referenceDocId.equals(BigInteger.valueOf(4)) || referenceDocId.equals(BigInteger.valueOf(5))
				|| referenceDocId.equals(BigInteger.valueOf(6));
		boolean isCounterSaleOrder = referenceDocId.equals(BigInteger.valueOf(1));

		Integer row = 0;

		List<PartDetailRequest> partDetails = salesInvoiceRequest.getPartDetails();

//		partDetails.stream()
//	    .filter(partDetail -> partDetail.getPartId().compareTo(BigInteger.ZERO) == 1)
//	    .map(PartDetailRequest::getBinRequest)
//	    .forEach(binRequest -> {
//	    	
//	    });

		for (PartDetailRequest partDetailRequest : salesInvoiceRequest.getPartDetails()) {

//			String flag = isCustomerOrder ? "CO" : isDcOrder ? "DC" : null;
			String OrderFlag = isPoOrder ? "PO" : isCustomerOrder ? "CO" : isDcOrder ? "DC" : null;

			if (!isCustomerOrder && !isDcOrder) {
				partDetailRequest = commonDao.updateStockInPartBranchAndStockStore(session, "SUBTRACT",
						partDetailRequest, salesInvoiceRequest.getBranchId(), partDetailRequest.getBranchStoreId(),
						"PA_SALE_INVOICE", userCode);
			} else if (isCustomerOrder) {
				commonDao.updateStockInPartBranchAndStockStore(session, "SUBTRACT", partDetailRequest,
						salesInvoiceRequest.getBranchId(), partDetailRequest.getBranchStoreId(), "PA_SALE_INVOICE",
						userCode);
			}
			BigInteger invoiceDtlId = null;
			for (BranchSpareIssueBinStockResponse binRequest : partDetailRequest.getBinRequest()) {
				invoiceDtlId = saveInvoiceDtl(session, saleInvoiceId, partDetailRequest, binRequest, isCustomerOrder,
						isDcOrder, userCode);

//				if (invoiceDtlId != null && OrderFlag != null) {
//					row = updateInvoiceQtyInOrder(session, partDetailRequest, binRequest, OrderFlag, userCode, userId);
//				}

//				if ((isCustomerOrder || isDcOrder) && row > 0) {
				if ((isCustomerOrder)) {
//					commonDao.updateStockInPartBranchAndStockStore(session, "SUBTRACT", partDetailRequest, 
//							salesInvoiceRequest.getBranchId(), partDetailRequest.getBranchStoreId(), "PA_SALE_INVOICE", userCode);
					commonDao.updateStockInStockBin(session, binRequest, salesInvoiceRequest.getBranchId(),
							partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), "PA_SALE_INVOICE",
							userCode, "SUBTRACT");

				} else if (!isCustomerOrder && !isDcOrder) {
					commonDao.updateStockInStockBin(session, binRequest, salesInvoiceRequest.getBranchId(),
							partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), "PA_SALE_INVOICE",
							userCode, "SUBTRACT");
				}

			}


			if (invoiceDtlId != null && OrderFlag != null) {
				row = updateInvoiceQtyInOrder(session, partDetailRequest, OrderFlag, userCode, userId);
			}
			
			String orderType = isPoOrder ? "PO" : isCustomerOrder ? "CO" : isDcOrder ? "DC" : isCounterSaleOrder ? "CS" : null;

			logger.info("orderType is --" , orderType);;
			updateInvoiceQtyInPickList(session, partDetailRequest.getPickListDtlId(), partDetailRequest.getInvoiceQty(), userCode);


//			boolean updateStock = false;
//
//			if (referenceDocId.equals(BigInteger.valueOf(1)) || referenceDocId.equals(BigInteger.valueOf(4))
//					|| referenceDocId.equals(BigInteger.valueOf(5)) || referenceDocId.equals(BigInteger.valueOf(6))
//					|| referenceDocId.equals(BigInteger.valueOf(7))) {
//
//				updateStock = true;
//
//			} else if (referenceDocId.equals(BigInteger.valueOf(2)) || referenceDocId.equals(BigInteger.valueOf(3))) {
//				updateStock = row > 0;
//			}

//			if (updateStock) {
//				commonDao.updateStockForInPartBranchAndStockBin(session, "SUBTRACT", partDetailRequest,
//						salesInvoiceRequest.getBranchId(), "PA_SALE_INVOICE", userCode);
//			} else {
//				isSuccess = false;
//			}
		}
		return isSuccess;

	}

	private BigInteger saveInvoiceDtl(Session session, BigInteger saleInvoiceId, PartDetailRequest partDetailRequest,
			BranchSpareIssueBinStockResponse binRequest, Boolean isCustomerOrder, Boolean isDcOrder, String userCode) {
		SaleInvoiceDtlEntity saleInvoiceDtlEntity = new SaleInvoiceDtlEntity();

		saleInvoiceDtlEntity.setInvoiceSaleId(saleInvoiceId);
		saleInvoiceDtlEntity.setCustomerHdrId(isCustomerOrder ? partDetailRequest.getCustomerOrderHdrId() : null);
		saleInvoiceDtlEntity.setDcId(isDcOrder ? partDetailRequest.getCustomerOrderHdrId() : null);
		saleInvoiceDtlEntity.setPartId(partDetailRequest.getPartId());
		saleInvoiceDtlEntity.setPartBranchId(partDetailRequest.getPartBranchId());
		saleInvoiceDtlEntity.setStockBinId(binRequest.getBinId());
		saleInvoiceDtlEntity.setHsnCode(partDetailRequest.getHsnCode());
		saleInvoiceDtlEntity.setNdp(partDetailRequest.getNdp());
		saleInvoiceDtlEntity.setMrp(partDetailRequest.getMrp());
		Character individualBin = 'N';
		if (partDetailRequest.getIsIndividualBin()) {
			individualBin = 'Y';
		}

		saleInvoiceDtlEntity.setIsIndividualBin(individualBin);
		saleInvoiceDtlEntity.setQty(
				partDetailRequest.getIsIndividualBin() ? partDetailRequest.getInvoiceQty() : binRequest.getIssueQty());
		saleInvoiceDtlEntity.setOrderQty(partDetailRequest.getOrderQty());
		saleInvoiceDtlEntity.setBasicValue(partDetailRequest.getBasicUnitPrice());
		saleInvoiceDtlEntity.setDiscountType(partDetailRequest.getDiscountType());
		saleInvoiceDtlEntity.setDiscountRate(partDetailRequest.getDiscountRate());
		saleInvoiceDtlEntity.setDiscountValue(partDetailRequest.getDiscountAmount());
		saleInvoiceDtlEntity.setTaxValue(partDetailRequest.getTaxValue());
		saleInvoiceDtlEntity.setBillValue(partDetailRequest.getBillValue());
		saleInvoiceDtlEntity.setSplDiscountType(partDetailRequest.getSplDiscountType());
		saleInvoiceDtlEntity.setSplDiscountRate(partDetailRequest.getSplDiscountRate());
		saleInvoiceDtlEntity.setSplDiscountAmount(partDetailRequest.getSplDiscountAmount());
		saleInvoiceDtlEntity.setCgstPercent(partDetailRequest.getCgstPercent());
		saleInvoiceDtlEntity.setSgstPercent(partDetailRequest.getSgstPercent());
		saleInvoiceDtlEntity.setIgstPercent(partDetailRequest.getIgstPercent());
		saleInvoiceDtlEntity.setCreatedBy(userCode);
		saleInvoiceDtlEntity.setCreatedDate(new Date());

		saleInvoiceDtlEntity.setPoDtlId(partDetailRequest.getPoDtlId());
		saleInvoiceDtlEntity.setCustomerDtlId(isCustomerOrder ? partDetailRequest.getCustomerOrderDtlId() : BigInteger.valueOf(0));
		saleInvoiceDtlEntity.setDcDtlId(isDcOrder ? partDetailRequest.getCustomerOrderDtlId() : BigInteger.valueOf(0));
		saleInvoiceDtlEntity.setPickListDtlId(partDetailRequest.getPickListDtlId());

		return (BigInteger) session.save(saleInvoiceDtlEntity);

	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
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

	public String generateDocNumber(BigInteger branchId, String docType) {
		Session session = null;
		SpareGrnResponse spareGrnResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());

		Query query = null;
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("DocumentType", docType);
			query.setParameter("BranchID", branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnResponse = new SpareGrnResponse();
					spareGrnResponse.setGrnNumber((String) row.get("Doc_number"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnResponse.getGrnNumber();
	}

	private void updateDocumentNumber(String grnNumber, String documentTypeDesc, BigInteger branchId,
			String documentType, Session session) {
		String lastDocumentNumber = grnNumber.substring(grnNumber.length() - 7);
		logger.info(lastDocumentNumber);
		if (null != lastDocumentNumber) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC [Update_INV_Doc_No] :lastDocumentNo,"
					+ ":documentTypeDesc, :currentDate, :branchId, :documentType";
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("lastDocumentNo", lastDocumentNumber);
			query.setParameter("documentTypeDesc", documentTypeDesc);
			query.setParameter("branchId", branchId);
			query.setParameter("documentType", documentType);
			query.setParameter("currentDate", currentDate);
			query.executeUpdate();
		}
	}

	@Override
	public HashMap<BigInteger, String> searchDCNumber(String searchText, String categoryCode, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec PA_Search_DC_No_By_Category_Code :userCode, :categoryCode, :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("categoryCode", categoryCode);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("DChallan_Id"), (String) row.get("DCNumber"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return searchList;
	}

	@Override
	public HashMap<BigInteger, String> searchInvoiceList(String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "select * from PA_SALE_INVOICE where DocNumber like '%" + searchText + "%' and createdBy = "
				+ userCode;

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("invoice_sale_id"), (String) row.get("DocNumber"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return searchList;
	}

	@Override
	public List<SpareSalesInvoiceResponse> fetchInvoiceList(String invoiceNumber, Date fromDate, Date toDate,
			String userCode, Integer page, Integer size) {
		Session session = null;
		List<SpareSalesInvoiceResponse> spareSalesInvoiceResponseList = null;
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//			String dateToString = null;
		String fromDateToString = dateFormat1.format(fromDate);
		String toDateToString = dateFormat1.format(toDate);

		Query query = null;
		String sqlQuery = "exec [PA_Get_Sale_Invoice] :invoiceNumber," + "'" + fromDateToString + "','" + toDateToString
				+ "', " + ":userCode, :page, :size, " + ":pcId, :hoId, :zoneId, :stateId, :territoryId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("invoiceNumber", invoiceNumber.equalsIgnoreCase("null") ? null : invoiceNumber);
			query.setParameter("userCode", userCode);
			query.setParameter("page", page);
			query.setParameter("size", size);
			query.setParameter("pcId", null);
			query.setParameter("hoId", null);
			query.setParameter("zoneId", null);
			query.setParameter("stateId", null);
			query.setParameter("territoryId", null);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				spareSalesInvoiceResponseList = new ArrayList<SpareSalesInvoiceResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareSalesInvoiceResponse = new SpareSalesInvoiceResponse();

					spareSalesInvoiceResponse.setId((BigInteger) row.get("invoice_sale_id"));
					spareSalesInvoiceResponse.setAction((String) row.get("Cancel"));
					spareSalesInvoiceResponse.setInvoiceNumber((String) row.get("DocNumber"));
					spareSalesInvoiceResponse.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
					spareSalesInvoiceResponse.setDocType((String) row.get("DocType"));
					spareSalesInvoiceResponse.setInvoiceStatus((String) row.get("InvoiceStatus"));
					spareSalesInvoiceResponse.setPartyName((String) row.get("PartyName"));
					spareSalesInvoiceResponse.setBranchName((String) row.get("BranchName"));
					spareSalesInvoiceResponse.setPartyCategory((String) row.get("PartyCategory"));
					spareSalesInvoiceResponse.setPartyCode((String) row.get("PartyCode"));
					spareSalesInvoiceResponse.setCustomerName((String) row.get("CustomerName"));
					spareSalesInvoiceResponse.setPoNumber((String) row.get("PoNo"));
					spareSalesInvoiceResponse.setPoDate(parseDateInStringFormat((Date) row.get("PoDate")));

					spareSalesInvoiceResponse.setPinCode((String) row.get("PinCode"));
					spareSalesInvoiceResponse.setState((String) row.get("StateDesc"));
					spareSalesInvoiceResponse.setDistrict((String) row.get("DistrictDesc"));
					spareSalesInvoiceResponse.setTehsil((String) row.get("TehsilDesc"));
					spareSalesInvoiceResponse.setCity((String) row.get("CityDesc"));
					spareSalesInvoiceResponse.setPostOffice((String) row.get("postOffice"));
					spareSalesInvoiceResponse.setAddress((String) row.get("PartyAddLine1"));
					spareSalesInvoiceResponse.setTotalInvocieAmount((BigDecimal) row.get("totalInvocieAmount"));
					spareSalesInvoiceResponse.setTaxableAmount((BigDecimal)row.get("taxableAmount"));
					spareSalesInvoiceResponse.setTotalCount((Integer)row.get("totalCount"));
					spareSalesInvoiceResponseList.add(spareSalesInvoiceResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareSalesInvoiceResponseList;
	}

	@Override
	public SpareSalesInvoiceResponse fetchHdr(BigInteger invoiceId) {
		Session session = null;
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = null;

		BigInteger flag = BigInteger.valueOf(1);
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Query query = null;

		if (flag.equals(BigInteger.valueOf(1))) {
			String sqlQuery = "exec [PA_Get_Sale_Invoice_Details] :invoiceId, :flag";

			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);

				query.setParameter("invoiceId", invoiceId);
				query.setParameter("flag", flag);

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				List data = query.list();
				if (data != null && !data.isEmpty()) {

					for (Object object : data) {
						Map row = (Map) object;
						spareSalesInvoiceResponse = new SpareSalesInvoiceResponse();
						spareSalesInvoiceResponse.setId((BigInteger) row.get("invoice_sale_id"));
						spareSalesInvoiceResponse.setInvoiceNumber((String) row.get("InvoiceNumber"));
						spareSalesInvoiceResponse.setBranchId((BigInteger) row.get("branch_id"));
						spareSalesInvoiceResponse.setBranchName((String) row.get("BranchName"));
						spareSalesInvoiceResponse
								.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
						spareSalesInvoiceResponse.setInvoiceStatus((String) row.get("InvoiceStatus"));
						spareSalesInvoiceResponse.setDocTypeId((BigInteger) row.get("DocTypeId"));
						spareSalesInvoiceResponse.setDocType((String) row.get("DocType"));
//						spareSalesInvoiceResponse.setAction("Cancel");
						spareSalesInvoiceResponse.setPartyCategory((String) row.get("ProductCategory"));
						spareSalesInvoiceResponse.setPartyName((String) row.get("PartyName"));
						spareSalesInvoiceResponse.setPartyCode((String) row.get("PartyCode"));
						spareSalesInvoiceResponse.setCustomerName((String) row.get("CustomerName"));
						spareSalesInvoiceResponse.setCustomerMobileNumber((String) row.get("CustomerMobileNumber"));
						spareSalesInvoiceResponse.setPoNumber((String) row.get("PONumber"));
						spareSalesInvoiceResponse.setPoHdrId((BigInteger) row.get("po_hdr_id"));
						spareSalesInvoiceResponse.setPoDate(parseDateInStringFormat((Date) row.get("PODate")));
						spareSalesInvoiceResponse.setAddress((String) row.get("Address"));
						spareSalesInvoiceResponse.setPinCode((String) row.get("PinCode"));
						spareSalesInvoiceResponse.setDistrict((String) row.get("District"));
						spareSalesInvoiceResponse.setState((String) row.get("State"));
						spareSalesInvoiceResponse.setTehsil((String) row.get("Tehsil"));
						spareSalesInvoiceResponse.setCity((String) row.get("City"));
						spareSalesInvoiceResponse.setPostOffice((String) row.get("PostOffice"));
						spareSalesInvoiceResponse.setTransporterName((String) row.get("TransporterName"));
						spareSalesInvoiceResponse.setTransporterVehicleNo((String) row.get("TransporterVehicleNo"));
						spareSalesInvoiceResponse.setLrNo((String) row.get("LR_No"));
						spareSalesInvoiceResponse.setOtherCharges((BigDecimal) row.get("OtherCharges"));
						spareSalesInvoiceResponse.setTotalBaseAmount((BigDecimal) row.get("totalBaseAmount"));
						spareSalesInvoiceResponse.setTDiscountValue((BigDecimal) row.get("TDiscountValue"));
						spareSalesInvoiceResponse.setSpecialDiscountPercent((BigDecimal) row.get("SpecialDiscountPer"));
						spareSalesInvoiceResponse.setSpecialDiscountAmount((BigDecimal) row.get("SpecialDiscount"));
						spareSalesInvoiceResponse.setNetBaseAmount((BigDecimal) row.get("NetBaseAmount"));
						spareSalesInvoiceResponse.setTotalTaxableAmount((BigDecimal) row.get("totalTaxableAmount"));
						spareSalesInvoiceResponse.setTotalGstAmount((BigDecimal) row.get("totalGstAmount"));
						spareSalesInvoiceResponse.setTcsAmount((BigDecimal) row.get("TCS_Amount"));
						spareSalesInvoiceResponse.setTcsPercent((BigDecimal) row.get("TCS_Percent"));
						spareSalesInvoiceResponse.setTotalInvocieAmount((BigDecimal) row.get("totalInvocieAmount"));
						spareSalesInvoiceResponse.setTotalPart((Integer) row.get("totalPart"));
						spareSalesInvoiceResponse.setTotalQty((BigDecimal) row.get("totalQty"));
						spareSalesInvoiceResponse.setQRCodeText((String) row.get("SignedQRCode"));
						spareSalesInvoiceResponse.setForwardGstAmount((BigDecimal)row.get("Part_Gst"));
						spareSalesInvoiceResponse.setForwardingPackagingAmount((BigDecimal)row.get("FP_Amount"));
						spareSalesInvoiceResponse.setForwardingPackagingGst((BigDecimal)row.get("FP_GST"));
						;
						Boolean isQRCodeGenerated = false;
						if (((Character) row.get("isQRCodeGenerated")).equals('Y')) {
							isQRCodeGenerated = true;
						}
						spareSalesInvoiceResponse.setIsQRCodeGenerated(isQRCodeGenerated);

						Boolean isCancelled = false;
						if (((Character) row.get("isCancelled")).equals('Y')) {
							isCancelled = true;
						}

						spareSalesInvoiceResponse.setIsCancelled(isCancelled);
						spareSalesInvoiceResponse.setCancellationRemarks((String) row.get("Cancellation_Remarks"));
					}

				}
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			} finally {
				if (session.isOpen())
					session.close();
			}
		}

		flag = spareSalesInvoiceResponse.getDocTypeId();

		if (flag.equals(BigInteger.valueOf(2))) {
			String sqlQuery = "exec [PA_Get_Sale_Invoice_Details] :invoiceId, :flag";

			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);

				query.setParameter("invoiceId", invoiceId);
				query.setParameter("flag", flag);

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				List data = query.list();
				if (data != null && !data.isEmpty()) {

					List<CustomerOrderNumberResponse> customerOrderResponseList = new ArrayList<CustomerOrderNumberResponse>();
					for (Object object : data) {
						Map row = (Map) object;
						CustomerOrderNumberResponse customerOrderResponse = new CustomerOrderNumberResponse();
						customerOrderResponse.setCustomerOrderNumber((String) row.get("Customer_Order_Number"));
						customerOrderResponse.setCustomerOrderDate((Date) row.get("Customer_Order_Date"));
						customerOrderResponseList.add(customerOrderResponse);
					}
					spareSalesInvoiceResponse.setCustomerOrderResponse(customerOrderResponseList);

				}
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			} finally {
				if (session.isOpen())
					session.close();
			}
		}
		flag = BigInteger.valueOf(3);
		if (flag.equals(BigInteger.valueOf(3))) {
			String sqlQuery = "exec [PA_Get_Sale_Invoice_Details] :invoiceId, :flag";

			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);

				query.setParameter("invoiceId", invoiceId);
				query.setParameter("flag", flag);

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				List data = query.list();
				if (data != null && !data.isEmpty()) {

					List<DeliveryChallanNumberResponse> dcResponseList = new ArrayList<DeliveryChallanNumberResponse>();
					for (Object object : data) {
						Map row = (Map) object;
						DeliveryChallanNumberResponse dcResponse = new DeliveryChallanNumberResponse();
						dcResponse.setDeliveryChallanNumber((String) row.get("DCNumber"));
						dcResponse.setDcDate((Date) row.get("DC_Date"));
						dcResponseList.add(dcResponse);
					}
					spareSalesInvoiceResponse.setDcResponse(dcResponseList);

				}
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			} finally {
				if (session.isOpen())
					session.close();
			}
		}

		List<PartDetailRequest> partDetailResponseList = new ArrayList<PartDetailRequest>();

		flag = BigInteger.valueOf(7);
		if (flag.equals(BigInteger.valueOf(7))) {
			String sqlQuery = "exec [PA_Get_Sale_Invoice_Details] :invoiceId, :flag";

			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);

				query.setParameter("invoiceId", invoiceId);
				query.setParameter("flag", flag);

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				List data = query.list();
				if (data != null && !data.isEmpty()) {

					List<BranchSpareIssueBinStockResponse> binResponseList = new ArrayList<BranchSpareIssueBinStockResponse>();

					for (Object object : data) {
						Map row = (Map) object;

						PartDetailRequest partDetailResponse = new PartDetailRequest();

						partDetailResponse.setInvoiceSaleId((BigInteger) row.get("invoice_sale_id"));
						partDetailResponse.setPartNumber((String) row.get("PartNumber"));
						partDetailResponse.setPartId((BigInteger) row.get("part_id"));
						partDetailResponse.setPartBranchId((BigInteger) row.get("partBranch_id"));
						partDetailResponse.setPartDesc((String) row.get("PartDesc"));
						partDetailResponse.setPartSubCategory((String) row.get("PartSubCategory"));
						partDetailResponse.setBranchStore((String) row.get("FromStore"));
						partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));

						partDetailResponse.setBinLocation((String) row.get("StoreBinLocation"));
						partDetailResponse.setBinId((BigInteger) row.get("stock_bin_id"));

						partDetailResponse.setTotalStock((Integer) row.get("totalStock"));
						partDetailResponse.setBasicUnitPrice((BigDecimal) row.get("BasicValue"));
						partDetailResponse.setBillValue((BigDecimal) row.get("BillValue"));
						partDetailResponse.setCustomerOrderHdrId((BigInteger) row.get("customer_hdr_id"));
						partDetailResponse.setDiscountAmount((BigDecimal) row.get("DiscountValue"));
						partDetailResponse.setDiscountRate((BigDecimal) row.get("DiscountRate"));
						partDetailResponse.setHsnCode((String) row.get("HsnCode"));
						partDetailResponse.setOrderQty((BigDecimal) row.get("OrderQty"));
						partDetailResponse.setInvoiceQty((BigDecimal) row.get("Qty"));
						partDetailResponse.setCgstPercent((Integer) row.get("cgstPercent"));
						partDetailResponse.setCgst((BigDecimal) row.get("cgst"));
						partDetailResponse.setSgstPercent((Integer) row.get("sgstPercent"));
						partDetailResponse.setSgst((BigDecimal) row.get("sgst"));
						partDetailResponse.setIgstPercent((Integer) row.get("igstPercent"));
						partDetailResponse.setIgst((BigDecimal) row.get("igst"));
						partDetailResponse.setTaxableValue((BigDecimal) row.get("taxableValue"));
						partDetailResponse.setPoDtlId((BigInteger) row.get("po_dtl_id"));
						partDetailResponse.setCustomerOrderDtlId((BigInteger) row.get("customer_dtl_id"));

						partDetailResponse.setDcHdrId((BigInteger) row.get("dc_id"));
						partDetailResponse.setDcDtlId((BigInteger) row.get("dc_dtl_id"));
						partDetailResponse.setPickListDtlId((BigInteger) row.get("pickListDtlId"));

						Character isIndividualBin = (Character) row.get("isIndividualBin");
						if (isIndividualBin != null && isIndividualBin.toString().equals("Y")) {
							partDetailResponse.setIsIndividualBin((true));
						} else {
							partDetailResponse.setIsIndividualBin(false);
						}
						partDetailResponse.setIssueQty((BigDecimal) row.get("IssueQty"));
						partDetailResponse.setMrp((BigDecimal) row.get("MRP"));
						partDetailResponse.setNdp((BigDecimal) row.get("BasicValue"));
						partDetailResponse.setSplDiscountAmount((BigDecimal) row.get("Add_Discount_Amount"));
						partDetailResponse.setSplDiscountRate((BigDecimal) row.get("Add_Discount_Rate"));
						partDetailResponse.setTaxValue((BigDecimal) row.get("TaxValue"));

						partDetailResponseList.add(partDetailResponse);

					}

					spareSalesInvoiceResponse.setSaleInvoiceDtl(partDetailResponseList);

				}
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			} finally {
				if (session.isOpen())
					session.close();
			}
		}

		flag = BigInteger.valueOf(8);
		if (flag.equals(BigInteger.valueOf(8))) {
			String sqlQuery = "exec [PA_Get_Sale_Invoice_Details] :invoiceId, :flag";

			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);

				query.setParameter("invoiceId", invoiceId);
				query.setParameter("flag", flag);

				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				List data = query.list();
				if (data != null && !data.isEmpty()) {

					List<BranchSpareIssueBinStockResponse> binResponseList = new ArrayList<BranchSpareIssueBinStockResponse>();

					for (Object object : data) {
						Map row = (Map) object;

						BranchSpareIssueBinStockResponse binResponse = new BranchSpareIssueBinStockResponse();

						binResponse.setPartBranchId((Integer) row.get("partBranch_id"));
						binResponse.setBinId((BigInteger) row.get("stock_bin_id"));
						binResponse.setBinLocation((String) row.get("StoreBinLocation"));
						binResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
						binResponse.setAvailableQty((BigDecimal) row.get("totalStock"));
//						binResponse.setUnitPrice((BigDecimal) row.get("unitPrice"));
						binResponse.setIssueQty((BigDecimal) row.get("Qty"));
						binResponse.setStoreName((String) row.get("FromStore"));
						binResponse.setUnitPrice((BigDecimal) row.get("BasicValue"));

//							Character isIdividualBin = (Character) row.get("isIdividualBin");
//							if (isIdividualBin != null && isIdividualBin.toString().equals("Y")) {
//								binResponse.setIsIndividualBin((true));
//							} else {
//								binResponse.setIsIndividualBin(true);
//							}

						binResponseList.add(binResponse);

					}
//					spareSalesInvoiceResponse
//							.setSaleInvoiceDtl(patchPartAndBinData(partDetailResponseList, binResponseList));

				}
			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			} finally {
				if (session.isOpen())
					session.close();
			}
		}

		return spareSalesInvoiceResponse;

	}

	private List<PartDetailRequest> patchPartAndBinData(List<PartDetailRequest> partDetailResponseList,
			List<BranchSpareIssueBinStockResponse> binResponseList) {
		List<PartDetailRequest> partDetailResponseList1 = new ArrayList<PartDetailRequest>();
		for (PartDetailRequest partDetailResponse : partDetailResponseList) {
			List<BranchSpareIssueBinStockResponse> binResponseList1 = new ArrayList<BranchSpareIssueBinStockResponse>();
			BigDecimal invoiceQty = BigDecimal.ZERO;
			for (BranchSpareIssueBinStockResponse binResponse : binResponseList) {

				if (partDetailResponse.getPartBranchId().equals(BigInteger.valueOf(binResponse.getPartBranchId()))) {
					binResponse.setUnitPrice(partDetailResponse.getBasicUnitPrice());
					binResponseList1.add(binResponse);
					invoiceQty = invoiceQty.add(binResponse.getIssueQty());
				}
			}
			partDetailResponse.setBinRequest(binResponseList1);
			partDetailResponse.setInvoiceQty(invoiceQty);

			partDetailResponseList1.add(partDetailResponse);
		}
		return partDetailResponseList1;
	}

	@Override
	public PartDetailRequest fetchDtl(BigInteger invoiceId, Integer flag) {
		Session session = null;
		PartDetailRequest partDetailRequest = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Query query = null;
		String sqlQuery = "exec [PA_Get_Sale_Invoice_Details] :invoiceId, :flag";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("invoiceId", invoiceId);
			query.setParameter("flag", flag);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<BranchSpareIssueBinStockResponse> binRequest = null;
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					partDetailRequest = new PartDetailRequest();
					partDetailRequest.setPartId((BigInteger) row.get(""));
					partDetailRequest.setPartBranchId((BigInteger) row.get(""));
					partDetailRequest.setBranchStoreId((Integer) row.get(""));
					partDetailRequest.setBasicUnitPrice((BigDecimal) row.get(""));
					partDetailRequest.setBillValue((BigDecimal) row.get(""));
					partDetailRequest.setBinRequest(binRequest);
					partDetailRequest.setCustomerOrderDtlId((BigInteger) row.get(""));
					partDetailRequest.setDiscountAmount((BigDecimal) row.get(""));
					partDetailRequest.setDiscountRate((BigDecimal) row.get(""));
					partDetailRequest.setHsnCode((String) row.get(""));
					partDetailRequest.setInvoiceQty((BigDecimal) row.get(""));
					partDetailRequest.setIsIndividualBin((Boolean) row.get(""));
					partDetailRequest.setIssueQty((BigDecimal) row.get(""));
					partDetailRequest.setMrp((BigDecimal) row.get(""));
					partDetailRequest.setNdp((BigDecimal) row.get(""));
					partDetailRequest.setOrderQty((BigDecimal) row.get(""));
					partDetailRequest.setSplDiscountAmount((BigDecimal) row.get(""));
					partDetailRequest.setSplDiscountRate((BigDecimal) row.get(""));
					partDetailRequest.setTaxValue((BigDecimal) row.get(""));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partDetailRequest;
	}

	@Override
	public SpareSalesInvoiceResponse spareCancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest,
			String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(saleInvoiceCancelRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = new SpareSalesInvoiceResponse();
		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				String updateQuery = "update PA_SALE_INVOICE set isCancelled = 'Y' , " + " Cancellation_Remarks  = "
						+ saleInvoiceCancelRequest.getCancelRemarks() + ", ModifiedBy = " + userCode
						+ " where invoice_sale_id = " + saleInvoiceCancelRequest.getInvoiceSaleId();

				query = session.createSQLQuery(updateQuery);
//				
//				query.setParameter("invoiceSaleId", saleInvoiceCancelRequest.getInvoiceSaleId());
//				query.setParameter("remarks", saleInvoiceCancelRequest.getCancelRemarks());
//				query.setParameter("userCode", userCode);

			}
			if (isSuccess) {
				transaction.commit();
				session.close();
				spareSalesInvoiceResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Sapre Sale Invoice Cancelled Successfully";
				spareSalesInvoiceResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				spareSalesInvoiceResponse.setStatusCode(statusCode);
				spareSalesInvoiceResponse.setMsg(msg);
			}

		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareSalesInvoiceResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return spareSalesInvoiceResponse;
	}

	@Override
	public Integer updateInOrderStatus(String flag, BigInteger coOrDcHdrId, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
		}
		Integer row = 0;
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {
			BigInteger userId = null;

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);

			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				String updateQuery = "exec PA_Update_Status_In_Order_For_INV " + " :flag, " + " :InvoiceQty, "
						+ " :poHdrId, " + " :customerHdrId, " + " :dcHdrId," + " :UserCode ";

				query = session.createSQLQuery(updateQuery);
				query.setParameter("flag", flag);
				query.setParameter("InvoiceQty", 0);
				query.setParameter("poHdrId", 0);
				query.setParameter("customerHdrId", flag.equalsIgnoreCase("CO") ? coOrDcHdrId : 0);
				query.setParameter("dcHdrId", flag.equalsIgnoreCase("DC") ? coOrDcHdrId : 0);
				query.setParameter("UserCode", userCode);
				row = query.executeUpdate();

			}

			logger.info("row " + row);
			if (isSuccess) {
				transaction.commit();
				session.close();
			} else {
				transaction.commit();
				session.close();
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
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return row;
	}

//	@Override
//	public Integer updateInCustomerOrder(BigInteger customerHdrId, BigInteger userId) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(" invoked.." + userId);
//		}
//		Integer row = 0;
//		Session session = null;
//		Transaction transaction = null;
//		String msg = null;
//		int statusCode = 0;
//
//		Map<String, Object> mapData = null;
//		Date todayDate = new Date();
//		Query query = null;
//		boolean isSuccess = true;
//		BigInteger id = null;
//
//		try {
//			session = sessionFactory.openSession();
//			transaction = session.beginTransaction();
//
//			String updateQuery = "exec PA_Update_Status_In_Order_For_INV " 
//					+ " :InvoiceQty , " 
//					+ " :poHdrId , "
//					+ " :customerHdrId, "
//					+ " :dcHdrId,"
//					+ " :UserCode ";
//
//			query = session.createSQLQuery(updateQuery);
//			query.setParameter("InvoiceQty", 0);
//			query.setParameter("poHdrId", null);
//			query.setParameter("customerHdrId", customerHdrId);
//			query.setParameter("dcHdrId", null);
//			query.setParameter("UserCode", null);
//			
//			row = query.executeUpdate();
//
//			logger.info("row " + row);
//			if (isSuccess) {
//				transaction.commit();
//				session.close();
//			} else {
//				transaction.commit();
//				session.close();
//			}
//
//		} catch (SQLGrammarException ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			isSuccess = false;
//			logger.error(this.getClass().getName(), ex);
//		} catch (HibernateException ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			isSuccess = false;
//			logger.error(this.getClass().getName(), ex);
//		} catch (Exception ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
//			isSuccess = false;
//			logger.error(this.getClass().getName(), ex);
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//		return row;
//
//	}

	@Override
	public Integer updateInCustomerOrderDtl(PartDetailRequest partDetailRequest, BigInteger userId) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userId);
			logger.debug(partDetailRequest.toString());
		}
		Integer row = 0;
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			String updateQuery = "Exec PA_Update_Invoice_Qty_Cust_Order :invoiceQty, :userCode, :customerDtlId";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("invoiceQty", partDetailRequest.getInvoiceQty());
			query.setParameter("userCode", userId);
			query.setParameter("customerDtlId", partDetailRequest.getCustomerOrderDtlId());
			row = query.executeUpdate();

			logger.info("row " + row);
			if (isSuccess) {
				transaction.commit();
				session.close();
			} else {
				transaction.rollback();
				session.close();
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
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return row;

	}

	public String parseDateInStringFormat(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String newDate = dateFormat.format(date);
			return newDate;
		}
		return null;
	}

	@Override
	public Integer updateInPoHdr(String flag, BigInteger poHdrId, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + poHdrId);
		}
		Integer row = 0;
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			String updateQuery = "exec PA_Update_Status_In_Order_For_INV " + " :flag, " + " :InvoiceQty, "
					+ " :poHdrId, " + " :customerHdrId, " + " :dcHdrId," + " :UserCode ";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("flag", flag);
			query.setParameter("InvoiceQty", 0);
			query.setParameter("poHdrId", poHdrId);
			query.setParameter("customerHdrId", 0);
			query.setParameter("dcHdrId", 0);
			query.setParameter("UserCode", userCode);
			row = query.executeUpdate();

			logger.info("row " + row);
			if (isSuccess) {
				transaction.commit();
				session.close();
			} else {
				transaction.rollback();
				session.close();
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
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return row;
	}

	@Override
	public PartTaxCalCulationResponse fetchTaxDetails(TaxDetailsRequest taxDetailsRequest, String userCode) {

		PartTaxCalCulationResponse partTaxCalCulationResponse = null;
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec PA_INV_CALCULATEItemAmnt :taxableAmount, :partId, :qty,"
				+ ":DealerId, :userCode, :partyCode, :pinId, :flag";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("taxableAmount", taxDetailsRequest.getTaxableAmount());
			query.setParameter("partId", taxDetailsRequest.getPartId());
			query.setParameter("qty", taxDetailsRequest.getQty());
			query.setParameter("DealerId", taxDetailsRequest.getDealerId());
			query.setParameter("userCode", userCode);
			query.setParameter("partyCode", taxDetailsRequest.getPartyCode());
			query.setParameter("pinId", taxDetailsRequest.getPinId());
			query.setParameter("flag", taxDetailsRequest.getFlag());

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partTaxCalCulationResponse = new PartTaxCalCulationResponse();
				for (Object object : data) {
					Map row = (Map) object;
					partTaxCalCulationResponse.setPartId((Integer) row.get("part_id"));
					partTaxCalCulationResponse.setCgst((BigDecimal) row.get("CGST_PER"));
					partTaxCalCulationResponse.setSgst((BigDecimal) row.get("SGST_PER"));
					partTaxCalCulationResponse.setIgst((BigDecimal) row.get("IGST_PER"));
					partTaxCalCulationResponse.setCgstAmount((BigDecimal) row.get("CGST_AMT"));
					partTaxCalCulationResponse.setSgstAmount((BigDecimal) row.get("SGST_AMT"));
					partTaxCalCulationResponse.setIgstAmount((BigDecimal) row.get("IGST_AMT"));

				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partTaxCalCulationResponse;
	}

//	@Override
	public Integer updateInvoiceQtyInOrder(Session session, PartDetailRequest partDetailRequest, String orderType,
			String userCode, BigInteger userId) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
		}
		Integer row = 0;
//		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {
//			session = sessionFactory.openSession();
//			transaction = session.beginTransaction();

			String updateQuery = "exec PA_Update_InvoiceQty_In_Order " + ":OrderType, :flag, :InvoiceQty, "
					+ ":poDtlId, :CustomerDtlId, :DcDtlId, :PickListDtlId, :UserCode";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("OrderType", orderType);
			query.setParameter("flag", "ADD");
			query.setParameter("InvoiceQty", partDetailRequest.getInvoiceQty());
			query.setParameter("poDtlId", partDetailRequest.getPoDtlId());
			query.setParameter("CustomerDtlId",
					orderType.equalsIgnoreCase("CO") ? partDetailRequest.getCustomerOrderDtlId() : 0);
			query.setParameter("DcDtlId",
					orderType.equalsIgnoreCase("DC") ? partDetailRequest.getCustomerOrderDtlId() : 0);
			query.setParameter("PickListDtlId", 0);
			query.setParameter("UserCode", orderType.equalsIgnoreCase("PO") ? userCode : userId);

			row = query.executeUpdate();

			logger.info("row " + row);
//			if (isSuccess) {
//				transaction.commit();
//				session.close();
//			} else {
//				transaction.commit();
//				session.close();
//			}

		} catch (SQLGrammarException ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
//			if (transaction != null) {
//				transaction.rollback();
//			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
//			if (session != null) {
//				session.close();
//			}
		}
		return row;
	}

	public Integer updateInvoiceQtyInPickList(Session session, BigInteger pickListDtlId, BigDecimal invoiceQty,
			String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
		}
		Integer row = 0;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		BigInteger id = null;

		try {

			String updateQuery = "exec PA_Update_InvoiceQty_In_Order " + ":OrderType, :flag, :InvoiceQty, "
					+ ":poDtlId, :CustomerDtlId, :DcDtlId, :PickListDtlId, :UserCode";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("OrderType", "pickList");
			query.setParameter("flag", "ADD");
			query.setParameter("InvoiceQty", invoiceQty);
			query.setParameter("poDtlId", 0);
			query.setParameter("CustomerDtlId", 0);
			query.setParameter("DcDtlId", 0);
			query.setParameter("PickListDtlId", pickListDtlId);
			query.setParameter("UserCode", userCode);

			row = query.executeUpdate();

			logger.info("row " + row);

		} catch (SQLGrammarException ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return row;
	}

	@Override
	public void updateQrCodeStatus(BigInteger invoiceId, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.. invoiceId" + invoiceId);
		}
		Integer row = 0;
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			String updateQuery = "update PA_SALE_INVOICE set " + " isQRCodeGenerated = 'Y', "
					+ " QRImageName = :QRImageName, " + " ModifiedBy = :userCode, " + " ModifiedDate = :modifiedDate "
					+ " where invoice_sale_id = :invoiceId";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("invoiceId", invoiceId);
			query.setParameter("QRImageName", invoiceId + ".png");
			query.setParameter("userCode", userCode);
			query.setParameter("modifiedDate", todayDate);
			row = query.executeUpdate();

			logger.info("row " + row);
			if (isSuccess) {
				transaction.commit();
				session.close();
			} else {
				transaction.rollback();
				session.close();
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
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

}
