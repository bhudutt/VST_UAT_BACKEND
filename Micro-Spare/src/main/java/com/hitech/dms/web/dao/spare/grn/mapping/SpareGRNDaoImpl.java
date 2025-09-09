package com.hitech.dms.web.dao.spare.grn.mapping;

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

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.partmaster.create.request.PartStockBinDtlEntity;
import com.hitech.dms.web.entity.partmaster.create.request.PartStockBinEntity;
import com.hitech.dms.web.entity.spare.grn.SpareGrnDtlEntity;
import com.hitech.dms.web.entity.spare.grn.SpareGrnHdrEntity;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnRequest;
import com.hitech.dms.web.model.spare.grn.mapping.response.BinLocationListResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.InvoiceNumberResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnHdr;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnPODetailsReponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnPONumberReponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.StoreResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.service.common.CommonService;

@Repository
public class SpareGRNDaoImpl implements SpareGRNDao {

	private static final Logger logger = LoggerFactory.getLogger(SpareGRNDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CommonService commonService;

	@Autowired
	private CommonDao commonDao;

	@Override
	public ApiResponse<List<SpareGrnFromResponse>> fetchGrnFromList(int dealerId, String userCode) {
		Session session = null;
		ApiResponse<List<SpareGrnFromResponse>> apiResponse = new ApiResponse<>();
		List<SpareGrnFromResponse> spareGrnFromResponseList = null;
		SpareGrnFromResponse spareGrnFromResponse = null;
		Query<?> query = null;
		String sqlQuery = "exec [PA_Get_GRN_From] :dealerId,:grnTypeId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);
			query.setParameter("grnTypeId", null);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			System.out.println("size "+data.size()+" dealerId="+dealerId);
			if (data != null && !data.isEmpty()) {
				spareGrnFromResponseList = new ArrayList<SpareGrnFromResponse>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					spareGrnFromResponse = new SpareGrnFromResponse();
					spareGrnFromResponse.setValueId((Integer) row.get("id"));
					spareGrnFromResponse.setValueCode((String) row.get("Type_code"));
					spareGrnFromResponse.setTypeName((String) row.get("Grn_type"));

					spareGrnFromResponseList.add(spareGrnFromResponse);
					apiResponse.setResult(spareGrnFromResponseList);
					apiResponse.setMessage("Header List Successfully fetched");
					apiResponse.setStatus(WebConstants.STATUS_OK_200);
				}
			} else {
				apiResponse.setMessage("No Data Found");
				apiResponse.setStatus(WebConstants.STATUS_NO_CONTENT_204);
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		System.out.println(apiResponse.toString());

		return apiResponse;
	}

	@Override
	public SpareGrnFromResponse fetchGrnFromDetails(int dealerId, int grnTypeId, String userCode) {

		Session session = null;
		SpareGrnFromResponse spareGrnFromResponse = null;
		Query<SpareGrnFromResponse> query = null;
		String sqlQuery = "exec [PA_Get_GRN_From] :dealerId, :grnTypeId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);
			query.setParameter("grnTypeId", grnTypeId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					spareGrnFromResponse = new SpareGrnFromResponse();
					spareGrnFromResponse.setValueId((Integer) row.get("id"));
					spareGrnFromResponse.setValueCode((String) row.get("Grn_type"));
					spareGrnFromResponse.setTypeName((String) row.get("Type_code"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnFromResponse;
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
					storeResponse.setBinName((String) row.get("BinName"));
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
	public List<StoreResponse> fetchStoreListByPartId(String userCode, Integer partId) {

		Session session = null;
		List<StoreResponse> storeResponseList = null;
		StoreResponse storeResponse = null;
		Query<StoreResponse> query = null;
		String sqlQuery = "exec [PA_Get_Store_List_By_Part_Id] :UserCode, :partId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("UserCode", userCode);
			query.setParameter("partId", partId);

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
					storeResponse.setBinName((String) row.get("BinName"));
					storeResponse.setIsMainStore((Character) row.get("isMainStore"));
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
	public HashMap<BigInteger, String> fetchBinByStore(Integer branchStoreId, Integer partId) {

		Session session = null;
		HashMap<BigInteger, String> binName = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Bin_By_Store] :branchStoreId, :partId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("branchStoreId", branchStoreId);
			query.setParameter("partId", partId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				binName = new HashMap<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					binName.put((BigInteger) row.get("stock_bin_id"), (String) row.get("BinName"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return binName;
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
					partyCodeSearchResponse.setPartyBranchId((BigInteger) row.get("party_branch_id"));
					partyCodeSearchResponse.setPartyName((String) row.get("PartyName"));
					partyCodeSearchResponse.setPartyLocation((String) row.get("Party_Location"));
					partyCodeSearchResponse.setPinCode((String) row.get("PinCode"));
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
	public PartyCodeDetailResponse fetchPartyDetails(Integer partyCategoryId) {
		Session session = null;
		PartyCodeDetailResponse partyDetailResponse = null;
		List<PartyCodeDetailResponse> partyDetailResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Party_Category_Details] :partyCategoryId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partyCategoryId", BigInteger.valueOf(partyCategoryId));

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
					partyDetailResponse.setPartyBranchId((BigInteger) row.get("party_branch_id"));
					;
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
	public List<InvoiceNumberResponse> searchInvoiceNumber(String searchText, String grnType, String userCode,
			String dealerCode, String partyCode) {
		Session session = null;
		InvoiceNumberResponse invoiceNumberResponse = null;
		List<InvoiceNumberResponse> invoiceNumberResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Invoice_No] :searchText, :grnType, :dealerCode, :partyCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("grnType", grnType);
			query.setParameter("dealerCode", dealerCode.equalsIgnoreCase("null") ? null : dealerCode);
			query.setParameter("partyCode", partyCode.equalsIgnoreCase("null") ? null : partyCode);

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
	public InvoiceNumberResponse fetchInvoiceDetails(String InvoiceNo, String grnType) {
		Session session = null;
		InvoiceNumberResponse invoiceNumberResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Invoice_Details] :InvoiceNo, :grnType";

		String msg = null;
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("InvoiceNo", InvoiceNo);
			query.setParameter("grnType", grnType);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					invoiceNumberResponse = new InvoiceNumberResponse();
					msg = (String) row.get("Message");
					if (msg != null && msg.equalsIgnoreCase("Spare Grn already completed")) {
						invoiceNumberResponse.setMsg(msg);
						invoiceNumberResponse.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					} else {
//					invoiceNumberResponse.setInvoiceHdrId((BigInteger) row.get("erp_invoice_hdr_id"));
						invoiceNumberResponse.setInvoiceNo((String) row.get("InvoiceNo"));
						invoiceNumberResponse.setInvoiceDate((Date) row.get("InvoiceDate"));
						invoiceNumberResponse.setPoNumber((String) row.get("PoNumber"));
						invoiceNumberResponse.setProductCategory((String) row.get("CategoryCode"));
						invoiceNumberResponse.setTransporter((String) row.get("TransporterCode"));
						invoiceNumberResponse.setLrNo((String) row.get("LRNo"));
						invoiceNumberResponse.setLrDate((Date) row.get("LRDate"));
						invoiceNumberResponse.setInvoiceAmount((BigDecimal) row.get("InvoiceAmount"));
						invoiceNumberResponse.setGrnNumber((String) row.get("MRNNumber"));
						invoiceNumberResponse.setGrnStatus((String) row.get("status"));
						invoiceNumberResponse.setPartyId((BigInteger) row.get("parent_dealer_id"));
						invoiceNumberResponse.setPartyCode((String) row.get("ParentDealerCode"));
						invoiceNumberResponse.setPartyName((String) row.get("ParentDealerName"));
						invoiceNumberResponse.setSpecialDiscount((BigDecimal) row.get("Spl_Discount_Rate"));
						invoiceNumberResponse.setTcsPercent((BigDecimal) row.get("TCS_Percent"));
						invoiceNumberResponse.setOtherCharges((BigDecimal) row.get("OtherCharges"));
						invoiceNumberResponse.setOtherChargesGst((BigDecimal) row.get("OtherChargesGst"));
						invoiceNumberResponse.setMsg("Invocie Details fetched successfully");
						invoiceNumberResponse.setStatusCode(WebConstants.STATUS_OK_200);
					}
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
	public List<SpareGrnPONumberReponse> searchPONumber(String searchText, String grnType, String categoryCode,
			String partyCode, String userCode, Integer branchId, String isFor) {
		Session session = null;
		SpareGrnPONumberReponse spareGrnPONumberReponse = null;
		List<SpareGrnPONumberReponse> spareGrnPONumberReponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Po_number] :searchText, :grnType, :categoryCode, :partyCode, :branchId, :isFor";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchText", searchText);
			query.setParameter("grnType", grnType.equalsIgnoreCase("null") ? null : grnType);
			query.setParameter("categoryCode", categoryCode.equalsIgnoreCase("null") ? null : categoryCode);
			query.setParameter("partyCode", partyCode.equalsIgnoreCase("null") ? null : partyCode);
			query.setParameter("branchId", branchId != null ? branchId : null);
			query.setParameter("isFor", isFor);
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
	public List<SpareGrnPODetailsReponse> fetchPODetails(BigInteger poHdrId) {
		Session session = null;
		List<SpareGrnPODetailsReponse> spareGrnPODetailsReponseList = null;

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
				spareGrnPODetailsReponseList = new ArrayList<SpareGrnPODetailsReponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnPODetailsReponse = new SpareGrnPODetailsReponse();
					spareGrnPODetailsReponse.setPoHdrId((BigInteger) row.get("po_hdr_id"));
					spareGrnPODetailsReponse.setPoNumber((String) row.get("PONumber"));
					spareGrnPODetailsReponse.setInvoiceNo((String) row.get("InvoiceNo"));
					spareGrnPODetailsReponse.setProductCategory((String) row.get("productCategory"));
					spareGrnPODetailsReponse.setPOCreationDate((Date) row.get("POCreationDate"));
					spareGrnPODetailsReponse.setPOStatus((String) row.get("POStatus"));
					spareGrnPODetailsReponseList.add(spareGrnPODetailsReponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnPODetailsReponseList;
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
	public List<PartNumberDetailResponse> fetchBinDetails(String invoiceNo, int grnTypeId, String userCode) {
		Session session = null;
		PartNumberDetailResponse partDetailResponse = null;
		List<PartNumberDetailResponse> partDetailResponseList = null;
		Query query = null;
		String sqlQuery = "exec [PA_get_bin_stock_details] :invoiceNo, :mrnType_id, :UserCode ";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("invoiceNo", invoiceNo);
			query.setParameter("mrnType_id", grnTypeId);
			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partDetailResponseList = new ArrayList<PartNumberDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					BigDecimal basicUnitPrice = (BigDecimal) row.get("basicUnitPrice");

					partDetailResponse = new PartNumberDetailResponse();
					partDetailResponse.setPartId((Integer) row.get("part_id"));
					partDetailResponse.setPartBranchId((Integer) row.get("partBranch_id"));
					partDetailResponse.setPartNumber((String) row.get("PartNumber"));
					partDetailResponse.setPartDesc((String) row.get("PartDesc"));
					partDetailResponse.setPartSubCategory((String) row.get("partSubCategory"));
					partDetailResponse.setBinLocation((String) row.get("binName"));
					partDetailResponse.setHsnCode((String) row.get("hsnCode"));
					partDetailResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partDetailResponse.setSerialNumber((String) row.get("serialNumber"));
					partDetailResponse.setBasicUnitPrice(basicUnitPrice);
					partDetailResponse.setInvoiceQty((BigDecimal) row.get("invoiceQty"));
					partDetailResponse.setAccessibleValue((BigDecimal) row.get("accessibleValue"));
					partDetailResponse.setDiscountPercentage((BigDecimal) row.get("discountPercentage"));
					partDetailResponse.setDiscountAmount((BigDecimal) row.get("discountValue"));
					partDetailResponse.setCgstPercentage((BigDecimal) row.get("cgstPercentage"));
					partDetailResponse.setCgstAmount((BigDecimal) row.get("cgst"));
					partDetailResponse.setSgstPercentage((BigDecimal) row.get("sgstPercentage"));
					partDetailResponse.setSgstAmount((BigDecimal) row.get("sgst"));
					partDetailResponse.setIgstPercentage((BigDecimal) row.get("igstPercentage"));
					partDetailResponse.setIgstAmount((BigDecimal) row.get("igst"));
					partDetailResponse.setReceiptQty((BigDecimal) row.get("ReceiptQty"));
					partDetailResponse.setReceiptValue((BigDecimal) row.get("receiptValue"));
					partDetailResponse.setPoNumber((String) row.get("PONumber"));
					partDetailResponse.setStoreDesc((String) row.get("storeDesc"));
					partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));

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
			String grnNumber = null;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				SpareGrnHdrEntity spareGrnHdrEntity = new SpareGrnHdrEntity();

				SpareGrnHdr spareGrnHdr = checkIfSpareGrnHdrAlreadyExist(spareGrnRequest.getBranchStoreId(),
						spareGrnRequest.getGrnTypeId(), spareGrnRequest.getBranchId(), spareGrnRequest.getPartyId(),
						spareGrnRequest.getInvoiceNumber(), userCode);

				id = spareGrnHdr.getGrnHdrId();
				if (spareGrnHdr.getGrnHdrId() == null) {

					String generatedGrnNumber = generateGrnNumber(spareGrnRequest.getBranchId());
					grnNumber = generatedGrnNumber;
					spareGrnHdrEntity.setBranchId(spareGrnRequest.getBranchId());
//					spareGrnHdrEntity.setBranchStoreId(spareGrnRequest.getBranchStoreId());
					spareGrnHdrEntity.setGrnTypeId(spareGrnRequest.getGrnTypeId());
					spareGrnHdrEntity.setGrnNumber(generatedGrnNumber);
					spareGrnHdrEntity.setGrnDate(spareGrnRequest.getGrnDate());
					spareGrnHdrEntity.setPartyId(spareGrnRequest.getPartyId());
					spareGrnHdrEntity.setDriverName(spareGrnRequest.getDriverName());
					spareGrnHdrEntity.setDriverMobNo(spareGrnRequest.getDriverMobNo());
					spareGrnHdrEntity.setStatus(spareGrnRequest.getStatus());
					spareGrnHdrEntity.setPoPlantId(spareGrnRequest.getPoPlantId());
					spareGrnHdrEntity.setSuplrDcNo(null);
					spareGrnHdrEntity.setSuplrDcDate(null);
					spareGrnHdrEntity.setInvoiceNo(spareGrnRequest.getInvoiceNumber());
					spareGrnHdrEntity.setInvoiceDate(spareGrnRequest.getInvoiceDate());
					spareGrnHdrEntity.setRemarks(null);
					spareGrnHdrEntity.setPartyId(spareGrnRequest.getPartyId());
					spareGrnHdrEntity.setTransporterName(spareGrnRequest.getTransporterName());
					spareGrnHdrEntity.setTransporterVehicleNo(spareGrnRequest.getTransporterVehicleNo());
					spareGrnHdrEntity.setRoadPermitDetails(null);
					spareGrnHdrEntity.setTotalItems(null);
					spareGrnHdrEntity.setTNetValue(spareGrnRequest.getTotalBasicValue());
					spareGrnHdrEntity.setTDiscountValue(spareGrnRequest.getTotalDiscountValue());
					spareGrnHdrEntity.setTExciseValue(null);
					spareGrnHdrEntity.setTTaxValue(spareGrnRequest.getTotalTaxValue());
					spareGrnHdrEntity.setRoundOffValue(null);
					spareGrnHdrEntity.setTGrossValue(null);
					spareGrnHdrEntity.setHandlingCharges(null);
					spareGrnHdrEntity.setFreightCharges(null);
					spareGrnHdrEntity.setOtherCharges(null);
					spareGrnHdrEntity.setGrnInvoiceAmount(spareGrnRequest.getInvoiceAmount());
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
					updateDocumentNumber(generatedGrnNumber, "Material Receipt Note", spareGrnRequest.getBranchId(),
							"MRN", session);

				} else {
					BigDecimal grnValue = BigDecimal.ZERO;
					if (spareGrnHdr.getGrnValue() != null) {
						grnValue = spareGrnHdr.getGrnValue();
					}
					grnNumber = spareGrnRequest.getGrnNumber();
					grnValue = grnValue.add(spareGrnRequest.getGrnValue());
					spareGrnHdrEntity.setGrnHdrId(id);
					spareGrnHdrEntity.setGrnValue(grnValue);
					spareGrnHdrEntity.setStatus(spareGrnRequest.getStatus());
					spareGrnHdrEntity.setModifiedBy(userCode);
//					session.update(spareGrnHdrEntity);
					updateSpareGrnHdr(spareGrnHdrEntity, userCode, session);
				}
				for (PartNumberDetailResponse partNumberDetailResponse : spareGrnRequest
						.getPartNumberDetailResponseList()) {
					SpareGrnDtlEntity spareGrnDtlEntity = new SpareGrnDtlEntity();

					isExist = checkIfSpareGrnDtlMappingAlreadyExist(id, partNumberDetailResponse.getStockBinId());

					if (!isExist) {

						PartDetailRequest partDetailRequest = commonDao.updateStockForInPartBranchAndStockBin(session,
								"ADD", patchData(partNumberDetailResponse), spareGrnRequest.getBranchId(), "PA_GRN_DTL",
								userCode);
						
						BigInteger partBranchId = partDetailRequest.getPartBranchId();
						
						BigInteger stockBinId = null;
						
						List<BranchSpareIssueBinStockResponse> binList = partDetailRequest.getBinRequest();
						
						for (BranchSpareIssueBinStockResponse binRequest : partDetailRequest.getBinRequest()) {
							if (binRequest.getPartBranchId().equals(0)) {
								binRequest.setPartBranchId(partDetailRequest.getPartBranchId().intValue());
								stockBinId = commonDao.updateStockInStockBin(session, binRequest, spareGrnRequest.getBranchId(),
										partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), "PA_GRN_DTL",
										userCode, "ADD");
								binList = new ArrayList<BranchSpareIssueBinStockResponse>();
								binRequest.setBinId(stockBinId);
								binList.add(binRequest);
								partDetailRequest.setBinRequest(binList);
							} else if (binRequest.getBinId() == null || binRequest.getBinId().equals(0)) {
								binRequest.setPartBranchId(partBranchId.intValue());
								stockBinId = commonDao.updateStockInStockBin(session, binRequest, spareGrnRequest.getBranchId(),
										partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), "PA_GRN_DTL",
										userCode, "ADD");
								binList = new ArrayList<BranchSpareIssueBinStockResponse>();
								binRequest.setBinId(stockBinId);
								binList.add(binRequest);
								partDetailRequest.setBinRequest(binList);
							} else {
								stockBinId = commonDao.updateStockInStockBin(session, binRequest, spareGrnRequest.getBranchId(),
										partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), "PA_GRN_DTL",
										userCode, "ADD");
							}
						}

						spareGrnDtlEntity.setGrnHdrId(id);

						spareGrnDtlEntity.setFinalPoNumber(spareGrnRequest.getPoNumber());
						spareGrnDtlEntity.setPoNumber(spareGrnRequest.getPoNumber());
						spareGrnDtlEntity.setBinName(partNumberDetailResponse.getBinLocation());
						spareGrnDtlEntity.setRefPartBranchId(partNumberDetailResponse.getPartBranchId() == 0
								? Integer.valueOf(partDetailRequest.getPartBranchId().toString())
								: partNumberDetailResponse.getPartBranchId());
						spareGrnDtlEntity.setBranchStoreId(partNumberDetailResponse.getBranchStoreId());
						spareGrnDtlEntity.setStockBinId(
								partNumberDetailResponse.getStockBinId().compareTo(BigInteger.ZERO) == 0 ? partDetailRequest.getBinRequest().get(0).getBinId()
										: partNumberDetailResponse.getStockBinId());
						spareGrnDtlEntity.setIInvoiceQty(partNumberDetailResponse.getInvoiceQty());
						spareGrnDtlEntity.setCostPerUnit(partNumberDetailResponse.getBasicUnitPrice());
						spareGrnDtlEntity.setIMrp(partNumberDetailResponse.getMrp());
						spareGrnDtlEntity.setCgst(partNumberDetailResponse.getCgstPercentage());
						spareGrnDtlEntity.setSgst(partNumberDetailResponse.getSgstPercentage());
						spareGrnDtlEntity.setIgst(partNumberDetailResponse.getIgstPercentage());
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
				msg = "Spare GRN Created Successfully with GRN number - " + grnNumber;
				spareGrnResponse.setGrnNumber(grnNumber);
				spareGrnResponse.setMsg(msg);
			} else {
				transaction.rollback();
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

	private PartDetailRequest patchData(PartNumberDetailResponse partNumberDetailResponse) {
		PartDetailRequest partDetailRequest = new PartDetailRequest();

		partDetailRequest.setPartId(BigInteger.valueOf(partNumberDetailResponse.getPartId()));
		partDetailRequest.setPartBranchId(BigInteger.valueOf(partNumberDetailResponse.getPartBranchId()));
		partDetailRequest.setBasicUnitPrice(partNumberDetailResponse.getBasicUnitPrice());
		partDetailRequest.setBinLocation(partNumberDetailResponse.getBinLocation());
		partDetailRequest.setBinId(partNumberDetailResponse.getStockBinId());
		partDetailRequest.setBranchStoreId(partNumberDetailResponse.getBranchStoreId());
		partDetailRequest.setInvoiceQty(partNumberDetailResponse.getInvoiceQty());
		partDetailRequest.setNdp(partNumberDetailResponse.getBasicUnitPrice());
		partDetailRequest.setMrp(partNumberDetailResponse.getMrp());
		partDetailRequest.setBinRequest(patchBinData(partDetailRequest));
		
		return partDetailRequest;
	}

	private List<BranchSpareIssueBinStockResponse> patchBinData(PartDetailRequest partDetailRequest) {
		List<BranchSpareIssueBinStockResponse> binList = new ArrayList<BranchSpareIssueBinStockResponse>();

//		if (tableName.equalsIgnoreCase("PA_GRN_DTL") || tableName.equalsIgnoreCase("PA_CLAIM_DTL")
//				|| tableName.equalsIgnoreCase("PA_SALE_INVOICE_CANCEL")) {
			BranchSpareIssueBinStockResponse binRequest1 = new BranchSpareIssueBinStockResponse();
			binRequest1.setPartBranchId(partDetailRequest.getPartBranchId().intValue());
			binRequest1.setBranchStoreId(partDetailRequest.getBranchStoreId());
			binRequest1.setUnitPrice(partDetailRequest.getBasicUnitPrice());
			binRequest1.setBinLocation(partDetailRequest.getBinLocation());
			binRequest1.setIssueQty(partDetailRequest.getInvoiceQty());
			binRequest1.setBinId(partDetailRequest.getBinId());
			binList.add(binRequest1);

			partDetailRequest.setBinRequest(binList);
//		}
//
//		for (BranchSpareIssueBinStockResponse binRequest : partDetailRequest.getBinRequest()) {
//			if (binRequest.getPartBranchId().equals(0)) {
//				binRequest.setPartBranchId(partBranchId);
//				stockBinId = updateStockInStockBin(session, binRequest, branchId,
//						partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), tableName,
//						userCode, flag);
//				binList = new ArrayList<BranchSpareIssueBinStockResponse>();
//				binRequest.setBinId(stockBinId);
//				binList.add(binRequest);
//				partDetailRequest.setBinRequest(binList);
//			} else if (binRequest.getBinId() == null || binRequest.getBinId().equals(0)) {
//				binRequest.setPartBranchId(partBranchId);
//				stockBinId = updateStockInStockBin(session, binRequest, branchId,
//						partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), tableName,
//						userCode, flag);
//				binList = new ArrayList<BranchSpareIssueBinStockResponse>();
//				binRequest.setBinId(stockBinId);
//				binList.add(binRequest);
//				partDetailRequest.setBinRequest(binList);
//			} else {
//				stockBinId = updateStockInStockBin(session, binRequest, branchId,
//						partDetailRequest.getBranchStoreId(), partDetailRequest.getInvoiceQty(), tableName,
//						userCode, flag);
//			}
//		}
			
			return binList;
	}

	private void addInGRNDtlAndUpdateStock(BigInteger id, PartNumberDetailResponse partNumberDetailResponse,
			BigInteger branchId, BigInteger branchStoreId, String userCode, Session session) {
		if (partNumberDetailResponse.getInvoiceQty() != null
				&& BigInteger.valueOf(partNumberDetailResponse.getPartBranchId()).compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			logger.info("partNumberDetailResponse", partNumberDetailResponse.toString());

			String updateQuery = "exec PA_Update_Stock_For_Grn :invoiceQty, " + ":branchId," + ":partBranchId,"
					+ ":stockBinId, " + ":branchStoreId, " + ":binName, " + ":basicUnitPrice, :mrp, " + ":cgst,"
					+ ":sgst," + ":igst," + ":grnHdrId," + ":poNumber," + ":modifiedBy";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("invoiceQty", partNumberDetailResponse.getInvoiceQty());
			query.setParameter("branchId", branchId);
			query.setParameter("partBranchId", partNumberDetailResponse.getPartBranchId());
			query.setParameter("stockBinId", partNumberDetailResponse.getStockBinId());
			query.setParameter("branchStoreId", partNumberDetailResponse.getBranchStoreId());
			query.setParameter("binName", partNumberDetailResponse.getBinLocation());
			query.setParameter("basicUnitPrice", partNumberDetailResponse.getBasicUnitPrice());
			query.setParameter("mrp", partNumberDetailResponse.getMrp());
			query.setParameter("cgst", partNumberDetailResponse.getCgstPercentage());
			query.setParameter("sgst", partNumberDetailResponse.getSgstPercentage());
			query.setParameter("igst", partNumberDetailResponse.getIgstPercentage());
			query.setParameter("grnHdrId", id);
			query.setParameter("poNumber", partNumberDetailResponse.getPoNumber());
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();
		}
	}

	private PartNumberDetailResponse updateStockInPartBranchAndStockBin(
			PartNumberDetailResponse partNumberDetailResponse, BigInteger branchStoreId, BigInteger branchId,
			String userCode, Session session) {
		if (partNumberDetailResponse.getInvoiceQty() != null
				&& BigInteger.valueOf(partNumberDetailResponse.getPartBranchId()).compareTo(BigInteger.ZERO) > 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			String updateQuery = "update PA_PART_BRANCH set OnHandQty = "
					+ "(select OnHandQty from PA_PART_BRANCH where partBranch_id = :partBranchId) + :invoiceQty,"
					+ " ModifiedBy =:modifiedBy" + " where partBranch_id =:partBranchId";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("partBranchId", partNumberDetailResponse.getPartBranchId());
			query.setParameter("invoiceQty", partNumberDetailResponse.getInvoiceQty());
			query.setParameter("modifiedBy", "userCode");
			query.executeUpdate();

			partNumberDetailResponse = checkIfBinLocationExistOrUpdate(partNumberDetailResponse, branchStoreId,
					branchId, session);
		}
		return partNumberDetailResponse;
	}

	private PartNumberDetailResponse checkIfBinLocationExistOrUpdate(PartNumberDetailResponse partNumberDetailResponse,
			BigInteger branchStoreId, BigInteger branchId, Session session) {
		Query query = null;
		String sqlQuery = "select stock_bin_id, BinName from PA_STOCK_BIN where stock_bin_id = "
				+ partNumberDetailResponse.getStockBinId() + " and BinName = '"
				+ partNumberDetailResponse.getBinLocation() + "'";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			BigInteger stockBinId = null;
			if (data != null && !data.isEmpty()) {

				String binLocation = null;
				for (Object object : data) {
					Map row = (Map) object;
					stockBinId = (BigInteger) row.get("stock_bin_id");
					binLocation = (String) row.get("BinName");
				}
				updateInStockBin(stockBinId, session, partNumberDetailResponse);
				partNumberDetailResponse.setStockBinId(stockBinId);
				partNumberDetailResponse.setBinLocation(binLocation);
			} else {
				PartStockBinEntity partStockBinEntity = new PartStockBinEntity();
				partStockBinEntity.setBinName(partNumberDetailResponse.getBinLocation());
				partStockBinEntity.setActiveStatus(true);
				partStockBinEntity.setBranchId(Integer.valueOf(branchId.toString()));
				partStockBinEntity.setBinType("NORMAL");
				partStockBinEntity.setStockStoreId(null);
				partStockBinEntity.setCurrentStock(partNumberDetailResponse.getInvoiceQty());
				partStockBinEntity.setCurrentStockAmount(partNumberDetailResponse.getBasicUnitPrice()
						.multiply(partNumberDetailResponse.getInvoiceQty()));
				partStockBinEntity.setDeafultStatus(false);
				partStockBinEntity.setBinBlocked(false);
				partStockBinEntity.setPartBranchId(partNumberDetailResponse.getPartBranchId());
				partStockBinEntity.setCreatedBy(null);
				partStockBinEntity.setCreatedDate(null);

				Integer id = (Integer) session.save(partStockBinEntity);
				stockBinId = BigInteger.valueOf(id);
				partNumberDetailResponse.setStockBinId(stockBinId);

			}
			updateInStockBinDtl(stockBinId, session, partNumberDetailResponse, branchId, branchStoreId);
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return partNumberDetailResponse;
	}

	private void updateInStockBin(BigInteger stockBinId, Session session,
			PartNumberDetailResponse partNumberDetailResponse) {

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = dateFormat1.format(new java.util.Date());

		String updateQuery = "update PA_STOCK_BIN set OnHandQty = "
				+ "(select OnHandQty from PA_PART_BRANCH where partBranch_id = :partBranchId) + :invoiceQty,"
				+ " ModifiedBy =:modifiedBy" + " where partBranch_id =:partBranchId";

		Query query = session.createSQLQuery(updateQuery);
		query.setParameter("partBranchId", partNumberDetailResponse.getPartBranchId());
		query.setParameter("invoiceQty", partNumberDetailResponse.getInvoiceQty());
		query.setParameter("modifiedBy", "userCode");
		query.executeUpdate();

	}

	private void updateInStockBinDtl(BigInteger stockBinId, Session session,
			PartNumberDetailResponse partNumberDetailResponse, BigInteger branchStoreId, BigInteger branchId) {
		if (partNumberDetailResponse != null) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());

			PartStockBinDtlEntity partStockBinDtlEntity = new PartStockBinDtlEntity();
			partStockBinDtlEntity.setBranchId(branchId);
			partStockBinDtlEntity.setPartBranchId(BigInteger.valueOf(partNumberDetailResponse.getPartBranchId()));
			partStockBinDtlEntity.setStockBinId(stockBinId);
			partStockBinDtlEntity.setDocumentId(null);
			partStockBinDtlEntity.setTableName(null);
			partStockBinDtlEntity.setRefDocHdrId(null);
			partStockBinDtlEntity.setTransactionDate(null);
			partStockBinDtlEntity.setIssueQty(null);
			partStockBinDtlEntity.setReceiptQty(null);
			partStockBinDtlEntity.setAvlbQty(null);
			partStockBinDtlEntity.setNdp(partNumberDetailResponse.getBasicUnitPrice());
			partStockBinDtlEntity.setMrp(null);
			partStockBinDtlEntity.setGndp(null);
			partStockBinDtlEntity.setDiscountValue(null);
			partStockBinDtlEntity.setExciseValue(null);
			partStockBinDtlEntity.setTaxValue(null);
			partStockBinDtlEntity.setGrossValue(null);
			partStockBinDtlEntity.setLandedCost(null);
			partStockBinDtlEntity.setCreatedDate(null);
			partStockBinDtlEntity.setCreatedBy(null);
			partStockBinDtlEntity.setCgst(partNumberDetailResponse.getCgstPercentage());
			partStockBinDtlEntity.setSgst(partNumberDetailResponse.getSgstPercentage());
			partStockBinDtlEntity.setIgst(partNumberDetailResponse.getIgstPercentage());
		}
	}

	private void updateSpareGrnHdr(SpareGrnHdrEntity spareGrnHdrEntity, String userCode, Session session) {
		if (spareGrnHdrEntity != null) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
//			String updateQuery = "EXEC [PA_Update_Spare_Grn] :grnValue, :grnHdrId, ':grnStatus', :InventoryNumber, "
//					+ " :ClaimGenerationNumber,"
//					+ " :modifiedBy, :modifiedDate";

			String updateQuery = "update PA_GRN_HDR set " + " MRNValue = " + spareGrnHdrEntity.getGrnValue()
					+ ", status = '" + spareGrnHdrEntity.getStatus() + "', ModifiedBy = '" + userCode
					+ "', ModifiedDate = " + currentDate + "	where mrn_hdr_id = " + spareGrnHdrEntity.getGrnHdrId();

			Query query = session.createSQLQuery(updateQuery);
//			query.setParameter("grnValue", spareGrnHdrEntity.getGrnValue());
//			query.setParameter("grnHdrId", spareGrnHdrEntity.getGrnHdrId());
//			query.setParameter("grnStatus", spareGrnHdrEntity.getStatus());
//			query.setParameter("InventoryNumber", null);
//			query.setParameter("ClaimGenerationNumber", null);
//			query.setParameter("modifiedDate", currentDate);
//			query.setParameter("modifiedBy", userCode);
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

	// @Override
	public SpareGrnHdr checkIfSpareGrnHdrAlreadyExist(BigInteger branchStoreId, BigInteger grnTypeId,
			BigInteger branchId, BigInteger partyId, String invoiceNumber, String userCode) {
		Session session = sessionFactory.openSession();
		Query query = null;

		SpareGrnHdr spareGrnHdr = new SpareGrnHdr();
		String sqlQuery = "exec [PA_Get_Spare_Grn_Hdr_List] :grnTypeId, :invoiceNumber, :userCode";
		query = session.createNativeQuery(sqlQuery);
		query.setParameter("grnTypeId", grnTypeId);
		query.setParameter("invoiceNumber", invoiceNumber);
		query.setParameter("userCode", userCode);
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

	public String generateGrnNumber(BigInteger branchId) {
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

		return spareGrnResponse.getGrnNumber();
	}

	@Override
	public List<String> searchGrnOrInvoiceNumber(String searchType, String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		List<String> searchList = null;
		String isFor = null;
		System.out.println("searchText " + searchText);

		Query query = null;
		String sqlQuery = "exec [PA_Search_Spare_Grn_List] :searchText, :isFor," + null + ", :userCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("isFor", searchType);
			query.setParameter("searchText", searchText);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					if (searchType.equalsIgnoreCase("INVOICENO")) {
						searchList.add((String) row.get("InvoiceNo"));
					} else if (searchType.equalsIgnoreCase("INVENTORYTRANSFERNO")) {
						searchList.add((String) row.get("InventoryTransferNumber"));
					} else if (searchType.equalsIgnoreCase("CLAIMNO")) {
						System.out.println("ClaimNo we get " + searchType);
						searchList.add((String) row.get("ClaimGenerationNumber"));
					} else if (searchType.equalsIgnoreCase("PONO")) {
						searchList.add((String) row.get("PONumber"));
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
	public ApiResponse<List<SpareGrnDetailsResponse>> fetchGrnDetailsList(String grnNumber, String invoiceNo,
			String poNumber, Date fromDate, Date toDate, String userCode, Integer page, Integer size)
			throws ParseException {
		Session session = null;
		ApiResponse<List<SpareGrnDetailsResponse>> apiResponse = null;

		List<SpareGrnDetailsResponse> spareGrnDetailsResponseList = null;
		SpareGrnDetailsResponse spareGrnDetailsResponse = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//		String dateToString = null;
		String fromDateToString = dateFormat1.format(fromDate);
		String toDateToString = dateFormat1.format(toDate);
		Integer dataCount = 0;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn] :grnNumber, :invoiceNo, :poNumber, " + "'" + fromDateToString + "','"
				+ toDateToString + "'," + " :userCode, :page, :size, "
				+ ":pcId, :hoId, :zoneId, :stateId, :territoryId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("grnNumber", grnNumber.equalsIgnoreCase("null") ? null : grnNumber);
			query.setParameter("invoiceNo", invoiceNo.equalsIgnoreCase("null") ? null : invoiceNo);
			query.setParameter("poNumber", poNumber.equalsIgnoreCase("null") ? null : poNumber);
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
				spareGrnDetailsResponseList = new ArrayList<SpareGrnDetailsResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnDetailsResponse = new SpareGrnDetailsResponse();

					spareGrnDetailsResponse.setId((BigInteger) row.get("mrn_hdr_id"));
					spareGrnDetailsResponse.setGrnNumber((String) row.get("MRNNumber"));
					spareGrnDetailsResponse.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
					spareGrnDetailsResponse.setGrnValue((BigDecimal) row.get("MRNValue"));
					spareGrnDetailsResponse.setGrnFrom((String) row.get("GrnFrom"));
					spareGrnDetailsResponse.setStatus((String) row.get("status"));
					spareGrnDetailsResponse.setInvoiceNo((String) row.get("InvoiceNo"));
					spareGrnDetailsResponse.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
					spareGrnDetailsResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
					spareGrnDetailsResponse.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
					spareGrnDetailsResponse.setProductCategory((String) row.get("productCategory"));
					spareGrnDetailsResponse.setSupplier((String) row.get("PONumber"));
					dataCount = (Integer) row.get("totalCount");

					spareGrnDetailsResponseList.add(spareGrnDetailsResponse);
				}
			}
		} catch (SQLGrammarException exp) {
			String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} catch (Exception exp) {
			String errorMessage = "An error occurred: " + exp.getMessage();
			logger.error(errorMessage + this.getClass().getName(), exp);
		} finally {
			if (spareGrnDetailsResponseList != null) {
				apiResponse = new ApiResponse<>();
				apiResponse.setCount(dataCount);
				apiResponse.setResult(spareGrnDetailsResponseList);
				apiResponse.setMessage("GRN Details Search Successfully.");
				apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
			if (session != null) {
				session.close();
			}
		}

		return apiResponse;
	}

	public String parseDateInStringFormat(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String newDate = dateFormat.format(date);
		return newDate;
	}

	@Override
	public SpareGrnDetailsResponse fetchGrnHdrAndDtl(int grnHdrId, String pageName) {
		SpareGrnDetailsResponse spareGrnDetailsResponse = null;

		spareGrnDetailsResponse = fetchGrnHdrDetails(grnHdrId, pageName);
		spareGrnDetailsResponse.setPartNumberDetailResponse(fetchGrnPartDetails(grnHdrId));
		return spareGrnDetailsResponse;
	}

	private SpareGrnDetailsResponse fetchGrnHdrDetails(int grnHdrId, String pageName) {
		Session session = null;
		SpareGrnDetailsResponse spareGrnDetailsResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn_Hdr_Details] :grnHdrId," + null + ", :pageName";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("grnHdrId", grnHdrId);
			query.setParameter("pageName", pageName);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnDetailsResponse = new SpareGrnDetailsResponse();
					spareGrnDetailsResponse.setInvoiceNo((String) row.get("InvoiceNo"));
					spareGrnDetailsResponse.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
					spareGrnDetailsResponse.setGrnNumber((String) row.get("MRNNumber"));
					spareGrnDetailsResponse.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
					spareGrnDetailsResponse.setGrnValue((BigDecimal) row.get("MRNValue"));
					spareGrnDetailsResponse.setGrnFrom((String) row.get("GrnFrom"));
					spareGrnDetailsResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
					spareGrnDetailsResponse.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
					spareGrnDetailsResponse.setStore((String) row.get("StoreDesc"));
					spareGrnDetailsResponse.setDriverName((String) row.get("DriverName"));
					spareGrnDetailsResponse.setDriverMobNo((String) row.get("DriverMobNo"));
					spareGrnDetailsResponse.setTransporterName((String) row.get("transporterName"));
					spareGrnDetailsResponse.setSupplier((String) row.get("PONumber"));
					spareGrnDetailsResponse.setProductCategory((String) row.get("productCategory"));
					spareGrnDetailsResponse.setSpecialDiscount((BigInteger)row.get("SpecialDiscount"));	
					spareGrnDetailsResponse.setTransportCharges((BigInteger)row.get("TransportCharges"));
					spareGrnDetailsResponse.setTransportGST((BigInteger)row.get("TransportGst"));
					spareGrnDetailsResponse.setTcsValue((BigInteger)row.get("TcsValue"));
					spareGrnDetailsResponse.setInvoiceAmount((BigDecimal)row.get("InvoiceAmount"));
					
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return spareGrnDetailsResponse;
	}

	public List<PartNumberDetailResponse> fetchGrnPartDetails(int grnHdrId) {
		Session session = null;
		List<PartNumberDetailResponse> partDetailResponsList = null;
		PartNumberDetailResponse partDetailResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_Details] :grnHdrId";

		try {

//				sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_Details] :grnHdrId";
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("grnHdrId", grnHdrId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partDetailResponsList = new ArrayList<PartNumberDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					BigDecimal basicUnitPrice = (BigDecimal) row.get("basicUnitPrice");
					BigDecimal discountPercentage = (BigDecimal) row.get("discountPercentage");
					BigDecimal cgstPercentage = (BigDecimal) row.get("cgst_per");
					BigDecimal igstPercentage = (BigDecimal) row.get("igst_per");
					BigDecimal sgstPercentage = (BigDecimal) row.get("sgst_per");

					BigDecimal discountAmount = ((discountPercentage).multiply(basicUnitPrice)
							.divide(BigDecimal.valueOf(100)));
					BigDecimal cgstAmount = (BigDecimal) row.get("cgst");
					BigDecimal sgstAmount = (BigDecimal) row.get("sgst");
					BigDecimal igstAmount = (BigDecimal) row.get("igst");

					partDetailResponse = new PartNumberDetailResponse();
					partDetailResponse.setGrnDtlId((BigInteger) row.get("mrn_dtl_id"));
					partDetailResponse.setPartNumber((String) row.get("PartNumber"));
					partDetailResponse.setPartDesc((String) row.get("PartDesc"));
					partDetailResponse.setPartBranchId((Integer) row.get("partBranch_id"));
					partDetailResponse.setPartSubCategory((String) row.get("partSubCategory"));
					partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
					partDetailResponse.setStoreDesc((String) row.get("StoreDesc"));
					partDetailResponse.setBinLocation((String) row.get("binName"));
					partDetailResponse.setAvailableQty((Integer) row.get("availableQty"));
					partDetailResponse.setHsnCode((String) row.get("hsnCode"));
					partDetailResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partDetailResponse.setSerialNumber((String) row.get("serialNumber"));
					partDetailResponse.setBasicUnitPrice(basicUnitPrice);
					partDetailResponse.setInvoiceQty((BigDecimal) row.get("invoiceQty"));
					partDetailResponse.setUnRestrictedQty((BigDecimal) row.get("UnrestrictedQty"));
					partDetailResponse.setRestrictedQty((BigDecimal) row.get("RestrictedQty"));
					partDetailResponse.setClaimedQty((BigDecimal) row.get("ClaimedQty"));
					partDetailResponse.setClaimStatus((String) row.get("setClaimStatus"));
					partDetailResponse.setAccessibleValue((BigDecimal) row.get("accessibleValue"));
					partDetailResponse.setDiscountPercentage((BigDecimal) row.get("discountPercentage"));
					partDetailResponse.setDiscountAmount((BigDecimal) row.get("discountValue"));
					partDetailResponse.setCgstPercentage((BigDecimal) row.get("cgst_per"));
					partDetailResponse.setCgstAmount((BigDecimal) row.get("cgst"));
					partDetailResponse.setSgstPercentage((BigDecimal) row.get("sgst_per"));
					partDetailResponse.setSgstAmount((BigDecimal) row.get("sgst"));
					partDetailResponse.setIgstPercentage((BigDecimal) row.get("igst_per"));
					partDetailResponse.setIgstAmount((BigDecimal) row.get("igst"));
					partDetailResponse.setReceiptQty((BigDecimal) row.get("ReceiptQty"));
					partDetailResponse.setReceiptValue((BigDecimal) row.get("receiptValue"));
//					partDetailResponse.setIsAgree((Character) row.get("IsAgree"));

					partDetailResponsList.add(partDetailResponse);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return partDetailResponsList;
	}

	@Override
	public List<String> searchClaimOrInvoiceNumber(String searchType, String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		List<String> searchList = null;
		System.out.println("looking out for the invoiceNoGrnList" + searchType + " searchText " + searchText
				+ "userCode " + userCode);
		Query query = null;
		String sqlQuery = "exec [PA_Search_Spare_Grn_List] :searchText, :isFor," + null + ", :userCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", searchType);
			query.setParameter("searchText", searchText);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					if (searchType.equalsIgnoreCase("INVOICENO")) {
						searchList.add((String) row.get("InvoiceNo"));
					} else if (searchType.equalsIgnoreCase("INVENTORYTRANSFERNO")) {
						searchList.add((String) row.get("InventoryTransferNumber"));
					} else if (searchType.equalsIgnoreCase("CLAIMNO")) {
						System.out.println("I am in claimNo ");
						searchList.add((String) row.get("ClaimGenerationNumber"));

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
}
