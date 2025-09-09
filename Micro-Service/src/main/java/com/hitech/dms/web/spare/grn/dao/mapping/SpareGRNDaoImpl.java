package com.hitech.dms.web.spare.grn.dao.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.spare.grn.model.mapping.response.InvoiceNumberResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.spare.grn.SpareGrnDtlEntity;
import com.hitech.dms.web.entity.spare.grn.SpareGrnHdrEntity;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingDtlEntity;
import com.hitech.dms.web.entity.spare.partymaster.mapping.DealerDistributorMappingEntity;
import com.hitech.dms.web.spare.grn.model.mapping.request.SpareGrnRequest;
import com.hitech.dms.web.spare.grn.model.mapping.response.BinLocationListResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnHdr;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnPODetailsReponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnPONumberReponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.StoreResponse;
import com.hitech.dms.web.spare.party.dao.mapping.DealerDistributorMappingDaoImpl;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorModel;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyDetailResponse;

@Repository
public class SpareGRNDaoImpl implements SpareGRNDao {

	private static final Logger logger = LoggerFactory.getLogger(SpareGRNDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private DocumentNumberGenerator docNumber;

	@Override
	public List<SpareGrnFromResponse> fetchGrnFromList(int dealerId, String userCode) {

		Session session = null;
		List<SpareGrnFromResponse> spareGrnFromResponseList = null;
		SpareGrnFromResponse spareGrnFromResponse = null;
		Query<SpareGrnFromResponse> query = null;
		String sqlQuery = "exec [PA_Get_GRN_From] :dealerId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				spareGrnFromResponseList = new ArrayList<SpareGrnFromResponse>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					spareGrnFromResponse = new SpareGrnFromResponse();
					spareGrnFromResponse.setValueId((Integer) row.get("id"));
					spareGrnFromResponse.setValueCode((String) row.get("Grn_type"));

					spareGrnFromResponseList.add(spareGrnFromResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnFromResponseList;
	}

	@Override
	public List<StoreResponse> fetchStoreList(String userCode) {

		Session session = null;
		List<StoreResponse> storeResponseList = null;
		StoreResponse storeResponse = null;
		Query<StoreResponse> query = null;
		String sqlQuery = "exec [PA_Get_Store_List] :UserCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				storeResponseList = new ArrayList<StoreResponse>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					storeResponse = new StoreResponse();
					storeResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
					storeResponse.setStoreCode((String) row.get("StoreCode"));
					storeResponse.setStoreDesc((String) row.get("StoreDesc"));

					storeResponseList.add(storeResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return storeResponseList;
	}

	@Override
	public List<PartyCodeSearchResponse> searchPartyCode(String searchText, String userCode) {
		Session session = null;
		PartyCodeSearchResponse partyCodeSearchResponse = null;
		List<PartyCodeSearchResponse> partyCodeSearchResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Party_Code_Search] :partycategory, :UserCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partycategory", searchText);
			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partyCodeSearchResponseList = new ArrayList<PartyCodeSearchResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					partyCodeSearchResponse = new PartyCodeSearchResponse();
					partyCodeSearchResponse.setPartyCode((String) row.get("PartyCode"));
					partyCodeSearchResponse.setPartyBranchId((BigInteger) row.get("party_branch_id"));
					partyCodeSearchResponse.setPartyCategoryId((BigInteger) row.get("party_category_id"));
					partyCodeSearchResponseList.add(partyCodeSearchResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partyCodeSearchResponseList;
	}

	@Override
	public PartyCodeDetailResponse fetchPartyDetails(BigInteger partyCategoryId) {
		Session session = null;
		PartyCodeDetailResponse partyDetailResponse = null;
		List<PartyCodeDetailResponse> partyDetailResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Party_Category_Details] :partyCategoryId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partyCategoryId", partyCategoryId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partyDetailResponseList = new ArrayList<PartyCodeDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					partyDetailResponse = new PartyCodeDetailResponse();
					partyDetailResponse.setPartyCategoryId((BigInteger) row.get("party_category_id"));
					partyDetailResponse.setPartyName((String) row.get("PartyCategoryName"));
					partyDetailResponse.setPartyCode((String) row.get("PartyCode"));
					partyDetailResponse.setBranchId((BigInteger) row.get("Branch_ID"));
					partyDetailResponse.setPartyBranchId((BigInteger) row.get("party_branch_id"));;
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partyDetailResponse;
	}
	
	@Override
	public List<InvoiceNumberResponse> searchInvoiceNumber(String searchText, String userCode) {
		Session session = null;
		InvoiceNumberResponse invoiceNumberResponse = null;
		List<InvoiceNumberResponse> invoiceNumberResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Invoice_No] :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				invoiceNumberResponseList = new ArrayList<InvoiceNumberResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					invoiceNumberResponse = new InvoiceNumberResponse();
//					invoiceNumberResponse.setInvoiceHdrId((BigInteger) row.get("erp_invoice_hdr_id"));
					invoiceNumberResponse.setInvoiceNo((String) row.get("InvoiceNo"));
//					invoiceNumberResponse.setInvoiceDate((Date) row.get("InvoiceDate"));
					invoiceNumberResponseList.add(invoiceNumberResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return invoiceNumberResponseList;
	}

	@Override
	public InvoiceNumberResponse fetchInvoiceDetails(String InvoiceNo) {
		Session session = null;
		InvoiceNumberResponse invoiceNumberResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Invoice_Details] :InvoiceNo";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("InvoiceNo", InvoiceNo);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					invoiceNumberResponse = new InvoiceNumberResponse();
//					invoiceNumberResponse.setInvoiceHdrId((BigInteger) row.get("erp_invoice_hdr_id"));
					invoiceNumberResponse.setInvoiceNo((String) row.get("InvoiceNo"));
					invoiceNumberResponse.setInvoiceDate((Date) row.get("InvoiceDate"));
					invoiceNumberResponse.setTransporter((String) row.get("TransporterCode"));
					invoiceNumberResponse.setLrNo((String) row.get("LRNo"));
					invoiceNumberResponse.setLrDate((Date) row.get("LRDate"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return invoiceNumberResponse;
	}

	@Override
	public List<SpareGrnPONumberReponse> searchPONumber(String searchText, String userCode) {
		Session session = null;
		SpareGrnPONumberReponse spareGrnPONumberReponse = null;
		List<SpareGrnPONumberReponse> spareGrnPONumberReponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Po_number] :searchText";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				spareGrnPONumberReponseList = new ArrayList<SpareGrnPONumberReponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnPONumberReponse = new SpareGrnPONumberReponse();
					spareGrnPONumberReponse.setPoHdrId((BigInteger) row.get("po_hdr_id"));
					spareGrnPONumberReponse.setPoNumber((String) row.get("PONumber"));
					spareGrnPONumberReponseList.add(spareGrnPONumberReponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnPONumberReponseList;
	}

	@Override
	public SpareGrnPODetailsReponse fetchPODetails(BigInteger poHdrId) {
		Session session = null;
		SpareGrnPODetailsReponse spareGrnPODetailsReponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_PO_Number_Details] :poHdrId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("poHdrId", poHdrId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnPODetailsReponse = new SpareGrnPODetailsReponse();
					spareGrnPODetailsReponse.setPoHdrId((BigInteger) row.get("po_hdr_id"));
					spareGrnPODetailsReponse.setPoNumber((String) row.get("PONumber"));
					spareGrnPODetailsReponse.setProductCategory((String) row.get("productCategory"));
					spareGrnPODetailsReponse.setPOCreationDate((Date) row.get("POCreationDate"));
					spareGrnPODetailsReponse.setPOStatus((String) row.get("POStatus"));

				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnPODetailsReponse;
	}

	@Override
	public List<BinLocationListResponse> searchBinLocation(String searchText, String userCode, int branchStoreId,
			String invoiceNo) {
		Session session = null;
		BinLocationListResponse binLocationListResponse = null;
		List<BinLocationListResponse> binLocationListResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_get_bin_stock_list] :UserCode, :searchText, :branchStoreId, :invoiceNo";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("searchText", searchText);
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("invoiceNo", invoiceNo);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				binLocationListResponseList = new ArrayList<BinLocationListResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					binLocationListResponse = new BinLocationListResponse();
					binLocationListResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));
					binLocationListResponse.setBinName((String) row.get("BinName"));
					binLocationListResponseList.add(binLocationListResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return binLocationListResponseList;
	}

	@Override
	public List<PartNumberDetailResponse> fetchBinDetails(String invoiceNo, int grnTypeId) {
		Session session = null;
		PartNumberDetailResponse partDetailResponse = null;
		List<PartNumberDetailResponse> partDetailResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_get_bin_stock_details] :invoiceNo, :mrnType_id";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("invoiceNo", invoiceNo);
			query.setParameter("mrnType_id", grnTypeId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partDetailResponseList = new ArrayList<PartNumberDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					BigDecimal basicUnitPrice = (BigDecimal) row.get("basicUnitPrice");
					BigDecimal discountPercentage = (BigDecimal) row.get("discountPercentage");
					BigDecimal cgstPercentage = (BigDecimal) row.get("cgst_per");
					BigDecimal igstPercentage = (BigDecimal) row.get("igst_per");
					BigDecimal sgstPercentage = (BigDecimal) row.get("sgst_per");

					BigDecimal discountAmount = (((BigDecimal) row.get("discountPercentage")).multiply(basicUnitPrice)
							.divide(BigDecimal.valueOf(100)));
					BigDecimal cgstAmount = (BigDecimal) row.get("cgst");
					BigDecimal sgstAmount = (BigDecimal) row.get("sgst");
					BigDecimal igstAmount = (BigDecimal) row.get("igst");

//					BigDecimal discountAmount = (((BigDecimal) row.get("discountPercentage"))
//							.multiply(basicUnitPrice).divide(BigDecimal.valueOf(100)));
//					BigDecimal cgstAmount = (((BigDecimal) row.get("cgstPercentage"))
//									.multiply((BigDecimal) row.get("basicUnitPrice"))).divide(BigDecimal.valueOf(100));
//					BigDecimal sgstAmount = (((BigDecimal) row.get("sgstPercentage"))
//							.multiply((BigDecimal) row.get("basicUnitPrice"))).divide(BigDecimal.valueOf(100));
//					BigDecimal igstAmount = (((BigDecimal) row.get("igstPercentage"))
//							.multiply((BigDecimal) row.get("basicUnitPrice"))).divide(BigDecimal.valueOf(100));;
//					

					
					partDetailResponse = new PartNumberDetailResponse();
					partDetailResponse.setPartId((Integer) row.get("part_id"));
					partDetailResponse.setPartNumber((String) row.get("PartNumber"));
					partDetailResponse.setPartDesc((String) row.get("PartDesc"));
					partDetailResponse.setPartSubCategory((String) row.get("partSubCategory"));
					partDetailResponse.setBinLocation((String) row.get("BinName"));
					partDetailResponse.setHsnCode((String) row.get("hsnCode"));
					partDetailResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partDetailResponse.setSerialNumber((String) row.get("serialNumber"));
					partDetailResponse.setBasicUnitPrice(basicUnitPrice);
					partDetailResponse.setInvoiceQty((BigDecimal) row.get("invoiceQty"));
					partDetailResponse.setAccessibleValue((BigDecimal) row.get("accessibleValue"));
					partDetailResponse.setDiscountPercentage((BigDecimal) row.get("discountPercentage"));
					partDetailResponse.setDiscountAmount(discountAmount);
					partDetailResponse.setCgstPercentage(cgstPercentage);
					partDetailResponse.setCgstAmount(cgstAmount);
					partDetailResponse.setSgstPercentage(sgstPercentage);
					partDetailResponse.setSgstAmount(sgstAmount);
					partDetailResponse.setIgstPercentage(igstPercentage);
					partDetailResponse.setIgstAmount(igstAmount);
					partDetailResponse.setReceiptQty((BigDecimal) row.get("ReceiptQty"));
					partDetailResponse.setReceiptValue((BigDecimal) row.get("receiptValue"));
					
					partDetailResponse.setStoreDesc((String) row.get("StoreDesc"));
					partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
					partDetailResponse.setBinLocation((String) row.get("BinName"));
					partDetailResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));

					
					
//					partDetailResponse.setBinLocation((String) row.get("binLocation"));
//					partDetailResponse.setStockBinId((BigInteger) row.get("binId"));
//					partDetailResponse.setStoreName((String) row.get("storeName"));
//					partDetailResponse.setStoreId((BigInteger) row.get("storeId"));
//					

					partDetailResponseList.add(partDetailResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partDetailResponseList;
	}

	@Override
	public SpareGrnResponse createSpareGrn(String userCode, SpareGrnRequest spareGrnRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(spareGrnRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		SpareGrnResponse spareGrnResponse = new SpareGrnResponse();
		List<SpareGrnResponse> spareGrnResponseList = null;
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String active = "N";
			mapData = fetchUserDTLByUserCode(session, userCode);

			BigInteger id = null;
			boolean isExist = false;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				SpareGrnHdrEntity spareGrnHdrEntity = new SpareGrnHdrEntity();

				SpareGrnHdr spareGrnHdr = checkIfSpareGrnHdrAlreadyExist(spareGrnRequest.getBranchStoreId(), spareGrnRequest.getGrnTypeId(),
						spareGrnRequest.getBranchId(), spareGrnRequest.getPartyBranchId(),
						spareGrnRequest.getInvoiceNumber());

					id = spareGrnHdr.getGrnHdrId();
				if (spareGrnHdr.getGrnHdrId() == null) {
					spareGrnHdrEntity.setBranchId(spareGrnRequest.getBranchId());
					spareGrnHdrEntity.setGrnTypeId(spareGrnRequest.getGrnTypeId());
					spareGrnHdrEntity.setGrnNumber(spareGrnRequest.getGrnNumber());
					spareGrnHdrEntity.setGrnDate(spareGrnRequest.getGrnDate());
					spareGrnHdrEntity.setPartyBranchId(spareGrnRequest.getPartyBranchId());
					spareGrnHdrEntity.setBranchStoreId(spareGrnRequest.getBranchStoreId());
					spareGrnHdrEntity.setDriverName(spareGrnRequest.getDriverName());
					spareGrnHdrEntity.setDriverMobNo(spareGrnRequest.getDriverMobNo());
					spareGrnHdrEntity.setStatus(spareGrnRequest.getStatus());
					spareGrnHdrEntity.setSuplrDcNo(null);
					spareGrnHdrEntity.setSuplrDcDate(null);
					spareGrnHdrEntity.setInvoiceNo(spareGrnRequest.getInvoiceNumber());
					spareGrnHdrEntity.setInvoiceDate(spareGrnRequest.getInvoiceDate());
					spareGrnHdrEntity.setRemarks(null);
					spareGrnHdrEntity.setTransporterDetails(spareGrnRequest.getTransporter());
					spareGrnHdrEntity.setRoadPermitDetails(null);
					spareGrnHdrEntity.setTotalItems(null);
					spareGrnHdrEntity.setTNetValue(null);
					spareGrnHdrEntity.setTDiscountValue(null);
					spareGrnHdrEntity.setTExciseValue(null);
					spareGrnHdrEntity.setTTaxValue(null);
					spareGrnHdrEntity.setRoundOffValue(null);
					spareGrnHdrEntity.setTGrossValue(null);
					spareGrnHdrEntity.setHandlingCharges(null);
					spareGrnHdrEntity.setFreightCharges(null);
					spareGrnHdrEntity.setOtherCharges(null);
					spareGrnHdrEntity.setGrnInvoiceAmount(null);
					spareGrnHdrEntity.setShortAmount(null);
					spareGrnHdrEntity.setDamagedAmount(null);
					spareGrnHdrEntity.setGrnValue(spareGrnRequest.getGrnValue());
					spareGrnHdrEntity.setLrNo(spareGrnRequest.getLrNo());
					spareGrnHdrEntity.setLrDate(spareGrnRequest.getLrDate());
					spareGrnHdrEntity.setOtherExpenses(null);
					spareGrnHdrEntity.setPoNumber(spareGrnRequest.getPoNumber());
					spareGrnHdrEntity.setPoDate(spareGrnRequest.getPoDate());
					spareGrnHdrEntity.setCreatedDate(todayDate);
					spareGrnHdrEntity.setCreatedBy(userCode);

					id = (BigInteger) session.save(spareGrnHdrEntity);
					updateDocumentNumber(spareGrnRequest.getGrnNumber(), 
							"Material Receipt Note", spareGrnRequest.getBranchId(), "MRN", session);

				} else {
					BigDecimal grnValue = BigDecimal.ZERO;
					if (spareGrnHdr.getGrnValue() != null) {
						grnValue = spareGrnHdr.getGrnValue();
					}
					grnValue = grnValue.add(spareGrnRequest.getGrnValue());
					spareGrnHdrEntity.setGrnValue(grnValue);
//					session.update(spareGrnHdrEntity);
					updateSpareGrnHdr(spareGrnHdrEntity, session);
				}
				for (PartNumberDetailResponse partNumberDetailResponse : spareGrnRequest
						.getPartNumberDetailResponseList()) {
					SpareGrnDtlEntity spareGrnDtlEntity = new SpareGrnDtlEntity();

					isExist = checkIfSpareGrnDtlMappingAlreadyExist(id, partNumberDetailResponse.getStockBinId());

					if (!isExist) {
						spareGrnDtlEntity.setGrnHdrId(id);
						spareGrnDtlEntity.setStockBinId(partNumberDetailResponse.getStockBinId());
						spareGrnDtlEntity.setFinalPoNumber(spareGrnRequest.getPoNumber());
						spareGrnDtlEntity.setPoNumber(spareGrnRequest.getPoNumber());
						spareGrnDtlEntity.setCostPerUnit(partNumberDetailResponse.getBasicUnitPrice());
						spareGrnDtlEntity.setCgst(partNumberDetailResponse.getCgstAmount());
						spareGrnDtlEntity.setSgst(partNumberDetailResponse.getSgstAmount());
						spareGrnDtlEntity.setIgst(partNumberDetailResponse.getIgstAmount());
						spareGrnDtlEntity.setCreatedDate(todayDate);
						spareGrnDtlEntity.setCreatedBy(userCode);
						session.save(spareGrnDtlEntity);
					} else {
						isSuccess = false;
						isExist = false;
						msg = "Spare Grn mapping already exist";
						statusCode = WebConstants.STATUS_INTERNAL_SERVER_ERROR_500;
						spareGrnResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						spareGrnResponse.setMsg(msg);
					}
				}
			}

			
			if (isSuccess) {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Spare GRN Created Successfully";
				spareGrnResponse.setMsg(msg);
			} else {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(statusCode);
				spareGrnResponse.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareGrnResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareGrnResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareGrnResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return spareGrnResponse;

	}

private void updateSpareGrnHdr(SpareGrnHdrEntity spareGrnHdrEntity, Session session) {
	if (spareGrnHdrEntity != null) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC [PA_Update_Spare_Grn] :grnValue, :grnHdrId, :modifiedBy";
			
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("grnValue", spareGrnHdrEntity.getGrnValue());
			query.setParameter("grnHdrId", spareGrnHdrEntity.getGrnHdrId());
			query.setParameter("modifiedBy", "sk01");

			query.executeUpdate();
			
		}
		
	}

private void updateDocumentNumber(String grnNumber, String documentTypeDesc, BigInteger branchId, 
		String documentType, Session session) {
	String lastDocumentNumber = grnNumber.substring(grnNumber.length() - 7);	
	System.out.println(lastDocumentNumber);
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

	//	@Override
	public SpareGrnHdr checkIfSpareGrnHdrAlreadyExist(BigInteger branchStoreId, BigInteger grnTypeId,
			BigInteger branchId, BigInteger partyBranchId, String invoiceNumber) {
		Session session = sessionFactory.openSession();
		Query query = null;

		SpareGrnHdr spareGrnHdr = new SpareGrnHdr();
		String sqlQuery = "exec [PA_Get_Spare_Grn_Hdr_List] :grnTypeId, :invoiceNumber";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("grnTypeId", grnTypeId);
		query.setParameter("invoiceNumber", invoiceNumber);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				spareGrnHdr.setGrnHdrId((BigInteger) row.get("mrn_hdr_id"));
				spareGrnHdr.setGrnValue((BigDecimal) row.get("MRNValue"));
			}
		}

		return spareGrnHdr;
	}

//	@Override
	public boolean checkIfSpareGrnDtlMappingAlreadyExist(BigInteger id, BigInteger stockBinId) {
		Session session = sessionFactory.openSession();
		Query query = null;

		String sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_Mapping] :id, :stockBinId";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("id", id);
		query.setParameter("stockBinId", stockBinId);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		if (data != null && !data.isEmpty()) {
			for (Object object : data) {
				Map row = (Map) object;
				id = (BigInteger) row.get("mrn_dtl_id");
				return true;
			}
		}
		return false;
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

	@Override
	public SpareGrnResponse generateGrnNumber(Integer branchId) {
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
			String docType = "MRN";
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

		return spareGrnResponse;
	}

	@Override
	public List<String> searchGrnOrInvoiceNumber(String searchType, String searchText) {
		Session session = null;
		String grnNumber = null;
		List<String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Spare_Grn_List] :searchText, :isFor";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", searchType);
			query.setParameter("searchText", searchText);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					if (searchType.equalsIgnoreCase("INVOICE NO.")) {
						searchList.add((String) row.get("InvoiceNo"));
					} else {
						searchList.add((String) row.get("MRNNumber"));

					}
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
	public List<String> searchInvoiceNumberByGrn(String grnNumber, String searchText) {
		Session session = null;
		String invoiceNumber = null;
		List<String> invoiceNumberList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Spare_Invoice_Number] :searchText, :mrnNumber";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("mrnNumber", grnNumber);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				invoiceNumberList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					invoiceNumberList.add((String) row.get("InvoiceNo"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return invoiceNumberList;
	}
	

	@Override
	public List<SpareGrnDetailsResponse> fetchGrnDetails(String grnNumber, String invoiceNo,
			Date fromDate, Date toDate) throws ParseException {
		Session session = null;
		List<SpareGrnDetailsResponse> spareGrnDetailsResponseList = null;
		SpareGrnDetailsResponse spareGrnDetailsResponse = null;
		
		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn] :grnNumber, :invoiceNo, :fromDate, :toDate";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("grnNumber", grnNumber);
			query.setParameter("invoiceNo", invoiceNo);
			query.setParameter("fromDate", fromDate);
			query.setParameter("toDate", toDate);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				spareGrnDetailsResponseList = new ArrayList<SpareGrnDetailsResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnDetailsResponse = new SpareGrnDetailsResponse();
					spareGrnDetailsResponse.setGrnNumber((String) row.get("MRNNumber"));
					spareGrnDetailsResponse.setGrnDate((Date) row.get("MRNDate"));
					spareGrnDetailsResponse.setGrnValue((BigDecimal) row.get("MRNValue"));
					spareGrnDetailsResponse.setBinName((String) row.get("binName"));
					spareGrnDetailsResponse.setStatus((String) row.get("status"));
					spareGrnDetailsResponse.setInvoiceNo((String) row.get("InvoiceNo"));
					spareGrnDetailsResponse.setInvoiceDate((Date) row.get("InvoiceDate"));
					spareGrnDetailsResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
					spareGrnDetailsResponse.setTransporterName((String) row.get("transporterName"));
					spareGrnDetailsResponse.setTransporterVehicle((String) row.get("transporterVehicle"));
					spareGrnDetailsResponse.setProductCategory((String) row.get("productCategory"));
					spareGrnDetailsResponse.setDriverName((String) row.get("DriverName"));
					spareGrnDetailsResponse.setDriverMobNo((String) row.get("DriverMobNo"));
					spareGrnDetailsResponseList.add(spareGrnDetailsResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnDetailsResponseList;
	}
}
