package com.hitech.dms.web.dao.spare.claim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.hitech.dms.web.dao.spare.grn.mapping.SpareGRNDaoImpl;
import com.hitech.dms.web.entity.spare.grn.SpareGrnClaimDtlEntity;
import com.hitech.dms.web.entity.spare.grn.SpareGrnClaimHdrEntity;
import com.hitech.dms.web.entity.spare.grn.SpareGrnClaimPhotosEntity;
import com.hitech.dms.web.entity.spare.grn.SpareGrnDtlEntity;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.claim.request.AgreeOrDisagreeClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimSearchRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimUpdateRequest;
import com.hitech.dms.web.model.spare.claim.response.SpareGrnClaimResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.request.PartDetailRequest;

@Repository
public class SpareClaimDaoImpl implements SpareClaimDao {

	private static final Logger logger = LoggerFactory.getLogger(SpareClaimDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	CommonDao commonDao;

	@Override
	public HashMap<BigInteger, String> fetchClaimType() {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "select * from PA_CLAIM_TYPE";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					searchList.put((BigInteger) row.get("Claim_Type_Id"), (String) row.get("Claim_Type"));
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
	public SpareGrnResponse createSpareClaim(String fileName, String fileType, String userCode,
			SpareClaimRequest spareClaimRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(spareClaimRequest.toString());
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
			String claimNumber = null;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				String docType = getDocumentType(spareClaimRequest.getClaimType());
				claimNumber = generateClaimNumber(spareClaimRequest.getBranchId(), spareClaimRequest.getClaimType())
						.getGeneratedNumber();

//				updateSpareGrnHdr(claimNumber, spareClaimRequest.getClaimDate(), spareClaimRequest.getClaimStatus(),
//						spareClaimRequest.getGrnHdrId(), userCode, session);

				SpareGrnClaimHdrEntity spareGrnClaimHdrEntity = new SpareGrnClaimHdrEntity();

				spareGrnClaimHdrEntity.setClaimGenerationNumber(claimNumber);
				spareGrnClaimHdrEntity.setGrnHdrId(spareClaimRequest.getGrnHdrId());
				spareGrnClaimHdrEntity.setClaimDate(todayDate);
				spareGrnClaimHdrEntity.setClaimRemarks(null);
				spareGrnClaimHdrEntity.setClaimType(spareClaimRequest.getClaimType());
				spareGrnClaimHdrEntity.setClaimStatus(spareClaimRequest.getClaimStatus());
				spareGrnClaimHdrEntity.setIsAgree('A');
				spareGrnClaimHdrEntity.setCreatedBy(userCode);
				spareGrnClaimHdrEntity.setCreatedDate(todayDate);
				BigInteger claimHdrId = (BigInteger) session.save(spareGrnClaimHdrEntity);

				for (PartDetailRequest partDetailRequest : spareClaimRequest.getSaveSpareClaim()) {
					SpareGrnClaimDtlEntity spareGrnClaimDtlEntity = new SpareGrnClaimDtlEntity();

					spareGrnClaimDtlEntity.setGrnClaimHdrId(claimHdrId);
					spareGrnClaimDtlEntity.setGrnDtlId(partDetailRequest.getGrnDtlId());
					spareGrnClaimDtlEntity.setIsClaimed((char) 1);
					spareGrnClaimDtlEntity.setClaimedQty(partDetailRequest.getClaimQty());
					spareGrnClaimDtlEntity.setClaimType(spareClaimRequest.getClaimType());
					spareGrnClaimDtlEntity.setClaimRemarks(partDetailRequest.getRemarks());
					spareGrnClaimDtlEntity.setClaimValue(partDetailRequest.getClaimValue());
					spareGrnClaimDtlEntity.setClaimStatus("Open");
					spareGrnClaimDtlEntity.setCreatedBy(userCode);
					spareGrnClaimDtlEntity.setCreatedDate(todayDate);
					BigInteger claimDtlId = (BigInteger) session.save(spareGrnClaimDtlEntity);

				}

				if (fileName != null) {
					BigInteger claimPhotosId = saveFileDetails(fileName, fileType, userCode, claimHdrId, null,
							spareClaimRequest);
				}

				String docTypeDesc = "Claim Generation-" + (spareClaimRequest.getClaimType()).split(" ")[0];

				updateDocumentNumber(claimNumber, docTypeDesc, spareClaimRequest.getBranchId(), docType, session);
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Spare Claim Created Successfully with Claim Number - " + claimNumber;
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

	public BigInteger saveFileDetails(String fileName, String contentType, String userCode, BigInteger claimHdrId,
			BigInteger claimDtlId, SpareClaimRequest spareClaimRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(spareClaimRequest.toString());

		}
		Session session = null;
		Transaction transaction = null;

		Map<String, Object> mapData = null;
		SpareGrnResponse spareGrnResponse = new SpareGrnResponse();
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String active = "N";
			mapData = fetchUserDTLByUserCode(session, userCode);

			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				SpareGrnClaimPhotosEntity spareGrnClaimPhotosEntity = new SpareGrnClaimPhotosEntity();

				spareGrnClaimPhotosEntity.setGrnClaimHdrId(claimHdrId);
//				spareGrnClaimPhotosEntity.setGrnClaimDtlId(claimDtlId);
				spareGrnClaimPhotosEntity.setFileType(contentType);
				spareGrnClaimPhotosEntity.setFileName(fileName);
				spareGrnClaimPhotosEntity.setCreatedBy(userCode);
				spareGrnClaimPhotosEntity.setCreatedDate(todayDate);
				BigInteger id = (BigInteger) session.save(spareGrnClaimPhotosEntity);
				return id;
			}
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			spareGrnResponse.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			transaction.commit();
			session.close();
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	private void updateSpareGrnHdr(String claimNumber, Date claimDate, String claimStatus, BigInteger grnHdrId,
			String userCode, Session session) {
		if (claimNumber != null) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
//			String updateQuery = "EXEC [PA_Update_Spare_Grn]" + null + ", :grnHdrId," + null
//					+ ", :claimNumber, :modifiedBy";

			String updateQuery = "update PA_GRN_HDR set ClaimGenerationNumber = :claimNumber, "
					+ "ClaimStatus = :claimStatus, ClaimDate = :claimDate, ModifiedBy = :modifiedBy, ModifiedDate = :modifiedDate"
					+ " where mrn_hdr_id = :grnHdrId";

			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("claimNumber", claimNumber);
			query.setParameter("claimStatus", claimStatus);
			query.setParameter("claimDate", claimDate);
			query.setParameter("grnHdrId", grnHdrId);
			query.setParameter("modifiedBy", userCode);
			query.setParameter("modifiedDate", currentDate);
			query.executeUpdate();
		}
	}

	private void updateDocumentNumber(String grnNumber, String documentTypeDesc, Integer branchId, String documentType,
			Session session) {
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

	public String getDocumentType(String claimType) {
		Session session = null;

		String docType = null;
		Query query = null;
		claimType = claimType.split(" ")[0];
		String docTypeDesc = "Claim Generation-" + claimType;
		String sqlQuery = "select DocumentTypeDesc, DocumentPrefix from CM_DOC_BRANCH" + " where DocumentTypeDesc = '"
				+ docTypeDesc + "'";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;

					docType = (String) row.get("DocumentPrefix");
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return docType;
	}

	public GeneratedNumberModel generateClaimNumber(Integer branchId, String claimType) {
		Session session = null;
		GeneratedNumberModel generatedNumberModel = null;

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());

		Query query = null;
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			String docType = getDocumentType(claimType);
			query.setParameter("DocumentType", docType);
			query.setParameter("BranchID", branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					generatedNumberModel = new GeneratedNumberModel();
					generatedNumberModel.setGeneratedNumber((String) row.get("Doc_number"));
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return generatedNumberModel;
	}
	
	@Override
	public ApiResponse<List<SpareGrnClaimResponse>> fetchGrnList(SpareClaimSearchRequest spareClaimSearchRequest, String userCode) throws ParseException {
	    List<SpareGrnClaimResponse> spareGrnClaimResponseList = new ArrayList<>();
	    int dataCount = 0;
	    ApiResponse<List<SpareGrnClaimResponse>> apiResponse = null;
	    String sqlQuery = "exec [PA_Get_Spare_Claim] :claimNumber, :claimType, :fromDate, :toDate, " +
	                      ":userCode, :page, :size, :pcId , :hoId, :zoneId, :stateId , :territoryId";

	    try (Session session = sessionFactory.openSession()) {
	        Query query = session.createSQLQuery(sqlQuery)
	            .setParameter("claimNumber", Optional.ofNullable(spareClaimSearchRequest.getClaimNumber()).orElse(null))
	            .setParameter("claimType", Optional.ofNullable(spareClaimSearchRequest.getClaimType()).orElse(null))
	            .setParameter("fromDate", spareClaimSearchRequest.getFromDate())
	            .setParameter("toDate", spareClaimSearchRequest.getToDate())
	            .setParameter("userCode", userCode)
	            .setParameter("page", spareClaimSearchRequest.getPage())
	            .setParameter("size", spareClaimSearchRequest.getSize())
	            .setParameter("pcId", null)
	            .setParameter("hoId", null)
	            .setParameter("zoneId", null)
	            .setParameter("stateId", null)
	            .setParameter("territoryId", null)
	            .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

	        List<Map<String, Object>> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Map<String, Object> row : data) {
	                SpareGrnClaimResponse response = new SpareGrnClaimResponse();
	                response.setAction((String) row.get("action"));
	                response.setId((BigInteger) row.get("mrn_claim_hdr_id"));
	                response.setClaimNumber((String) row.get("ClaimGenerationNumber"));
	                response.setClaimDate(parseDateInStringFormat((Date) row.get("ClaimDate")));
	                response.setDealerName((String) row.get("branch"));;
	                response.setClaimStatus((String) row.get("ClaimStatus"));
	                response.setGrnNumber((String) row.get("MRNNumber"));
	                response.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
	                response.setInvoiceNumber((String) row.get("InvoiceNo"));
	                response.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
	                response.setPartyCategoryName((String) row.get("PartyCategoryName"));
	                response.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
	                response.setTransporterName((String) row.get("transporterName"));
	                response.setTransporterVehicle((String) row.get("transporterVehicle"));
	                response.setProductCategory((String) row.get("productCategory"));
	                response.setDriverName((String) row.get("DriverName"));
	                response.setDriverMobNo((String) row.get("DriverMobNo"));
	                response.setSupplier((String) row.get("PONumber"));
	                response.setClaimType((String) row.get("claimType"));
	                response.setBranchId((BigInteger) row.get("branch_id"));
	                spareGrnClaimResponseList.add(response);

	                dataCount = ((Number) row.get("totalCount")).intValue(); // Handle conversion properly
	            }
	        }
	    } catch (SQLGrammarException exp) {
	        logger.error("SQL Grammar Error: {}", exp.getMessage(), exp);
	    } catch (HibernateException exp) {
	        logger.error("Hibernate Error: {}", exp.getMessage(), exp);
	    } catch (Exception exp) {
	        logger.error("General Error: {}", exp.getMessage(), exp);
	    }

	    // Return response directly, handling empty cases
	    apiResponse = new ApiResponse<>();
		apiResponse.setCount(dataCount);
		apiResponse.setResult(spareGrnClaimResponseList);
		apiResponse.setMessage(spareGrnClaimResponseList.isEmpty() ? "No Spare Claim Details found." : "Spare Claim Details Search Successfully.");
		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
	    
		return apiResponse;
	}


//	@Override
//	public ApiResponse<List<SpareGrnClaimResponse>> fetchGrnList(SpareClaimSearchRequest spareClaimSearchRequest,
//			String userCode) throws ParseException {
//		Session session = null;
//		ApiResponse<List<SpareGrnClaimResponse>> apiResponse = null;
//		List<SpareGrnClaimResponse> spareGrnClaimResponseList = new ArrayList<SpareGrnClaimResponse>();;
//		SpareGrnClaimResponse spareGrnClaimResponse = null;
//
//		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
////		String dateToString = null;
////		String fromDateToString = dateFormat1.format(spareClaimSearchRequest.getFromDate());
////		String toDateToString = dateFormat1.format(spareClaimSearchRequest.getToDate());
//		Integer dataCount = 0;
//
//		Query query = null;
//		String sqlQuery = "exec [PA_Get_Spare_Claim] :claimNumber, :claimType, '"
//				+ spareClaimSearchRequest.getFromDate() + "','" + spareClaimSearchRequest.getToDate() + "', "
//				+ ":userCode, :page, :size, :pcId , :hoId, :zoneId, :stateId , :territoryId";
//
//		try {
//			session = sessionFactory.openSession();
//			query = session.createSQLQuery(sqlQuery);
//
//			query.setParameter("claimNumber",
//					spareClaimSearchRequest.getClaimNumber() == null ? null : spareClaimSearchRequest.getClaimNumber());
//			query.setParameter("claimType",
//					spareClaimSearchRequest.getClaimType() == null ? null : spareClaimSearchRequest.getClaimType());
//			query.setParameter("userCode", userCode);
//			query.setParameter("page", spareClaimSearchRequest.getPage());
//			query.setParameter("size", spareClaimSearchRequest.getSize());
//			query.setParameter("pcId", null);
//			query.setParameter("hoId", null);
//			query.setParameter("zoneId", null);
//			query.setParameter("stateId", null);
//			query.setParameter("territoryId", null);
//			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
//
//			List data = query.list();
//			if (data != null && !data.isEmpty()) {
//				spareGrnClaimResponseList = new ArrayList<SpareGrnClaimResponse>();
//				for (Object object : data) {
//					Map row = (Map) object;
//					spareGrnClaimResponse = new SpareGrnClaimResponse();
//
////					String claimStatus = (String) row.get("ClaimStatus");
////					String action = claimStatus != null
////							? (claimStatus.equalsIgnoreCase("Waiting For Approval") ? "Go For Approval"
////									: (claimStatus.equalsIgnoreCase("Approved")
////											|| claimStatus.equalsIgnoreCase("Rejected")) ? "Agree Or DisAgree" : "")
////							: "";
//					spareGrnClaimResponse.setAction((String) row.get("action"));
//					spareGrnClaimResponse.setId((BigInteger) row.get("mrn_claim_hdr_id"));
//					spareGrnClaimResponse.setClaimNumber((String) row.get("ClaimGenerationNumber"));
//					spareGrnClaimResponse.setClaimDate(parseDateInStringFormat((Date) row.get("ClaimDate")));
//					spareGrnClaimResponse.setClaimStatus((String) row.get("ClaimStatus"));
//					spareGrnClaimResponse.setGrnNumber((String) row.get("MRNNumber"));
//					spareGrnClaimResponse.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
//					spareGrnClaimResponse.setInvoiceNumber((String) row.get("InvoiceNo"));
//					spareGrnClaimResponse.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
//					spareGrnClaimResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
//					spareGrnClaimResponse.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
////					spareGrnClaimResponse.setStore((String) row.get("StoreDesc"));
//					spareGrnClaimResponse.setTransporterName((String) row.get("transporterName"));
//					spareGrnClaimResponse.setTransporterVehicle((String) row.get("transporterVehicle"));
//					spareGrnClaimResponse.setProductCategory((String) row.get("productCategory"));
//					spareGrnClaimResponse.setDriverName((String) row.get("DriverName"));
//					spareGrnClaimResponse.setDriverMobNo((String) row.get("DriverMobNo"));
//					spareGrnClaimResponse.setSupplier((String) row.get("PONumber"));
//					dataCount = (Integer) row.get("totalCount");
//					spareGrnClaimResponse.setClaimType((String) row.get("claimType"));
//					spareGrnClaimResponse.setBranchId((BigInteger) row.get("branch_id"));
//					spareGrnClaimResponseList.add(spareGrnClaimResponse);
//				}
//			}
//		} catch (SQLGrammarException exp) {
//			String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
//			logger.error(errorMessage + this.getClass().getName(), exp);
//		} catch (HibernateException exp) {
//			String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
//			logger.error(errorMessage + this.getClass().getName(), exp);
//		} catch (Exception exp) {
//			String errorMessage = "An error occurred: " + exp.getMessage();
//			logger.error(errorMessage + this.getClass().getName(), exp);
//		} finally {
//			if (spareGrnClaimResponseList != null) {
//				apiResponse = new ApiResponse<>();
//				apiResponse.setCount(dataCount);
//				apiResponse.setResult(spareGrnClaimResponseList);
//				apiResponse.setMessage("Spare Claim Details Search Successfully.");
//				apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
//			}
//			if (session != null) {
//				session.close();
//			}
//		}
//
//		return apiResponse;
//	}

	@Override
	public HashMap<BigInteger, String> searchGrnNumberForClaimType(String searchType, String searchText,
			String claimType, String userCode) {
		Session session = null;
		String grnNumber = null;
		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Spare_Grn_List] :searchText, :isFor, :claimType, :userCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", searchType);
			query.setParameter("searchText", searchText);
			query.setParameter("claimType", claimType.equalsIgnoreCase(null) ? null : claimType);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					/* only GRN created from VST is allowed */
					if (((String) row.get("Grn_type")).equalsIgnoreCase("VST")) {
						searchList.put((BigInteger) row.get("mrn_hdr_id"), (String) row.get("MRNNumber"));
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
	public SpareGrnResponse updateSpareClaim(String userCode, SpareClaimUpdateRequest spareClaimUpdateRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(spareClaimUpdateRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		SpareGrnResponse spareGrnResponse = new SpareGrnResponse();
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			BigInteger id = null;
			boolean isExist = false;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				SpareGrnDtlEntity spareGrnDtlEntity = new SpareGrnDtlEntity();
				for (PartDetailRequest partDetailRequest : spareClaimUpdateRequest.getPartDetailRequest()) {
					SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
					String currentDate = dateFormat1.format(new java.util.Date());
					String updateQuery = "EXEC [PA_Update_Spare_Claim_Dtl] :grnDtlId,"
							+ " :unrestrictedQty, :modifiedBy";

					query = session.createSQLQuery(updateQuery);
					query.setParameter("grnDtlId", partDetailRequest.getGrnDtlId());
					query.setParameter("unrestrictedQty", partDetailRequest.getUnRestrictedQty());
					query.setParameter("modifiedBy", userCode);
					query.executeUpdate();

//					commonDao.updateStockForInPartBranchAndStockBin(session, "ADD",
//							patchPartDetailRequest(partDetailRequest), spareClaimUpdateRequest.getBranchId(),
//							"PA_CLAIM_DTL", userCode);

					com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest partRequest = commonDao
							.updateStockForInPartBranchAndStockBin(session, "ADD",
									patchPartDetailRequest(partDetailRequest), spareClaimUpdateRequest.getBranchId(),
									"PA_CLAIM_DTL", userCode);

					BigInteger partBranchId = partDetailRequest.getPartBranchId();

					BigInteger stockBinId = null;

					List<BranchSpareIssueBinStockResponse> binList = partRequest.getBinRequest();

					for (BranchSpareIssueBinStockResponse binRequest : partRequest.getBinRequest()) {
						if (binRequest.getPartBranchId().equals(0)) {
							binRequest.setPartBranchId(partDetailRequest.getPartBranchId().intValue());
							stockBinId = commonDao.updateStockInStockBin(session, binRequest,
									spareClaimUpdateRequest.getBranchId(), partDetailRequest.getBranchStoreId(),
									partRequest.getInvoiceQty(), "PA_CLAIM_DTL", userCode, "ADD");
							binList = new ArrayList<BranchSpareIssueBinStockResponse>();
							binRequest.setBinId(stockBinId);
							binList.add(binRequest);
							partRequest.setBinRequest(binList);
						} else if (binRequest.getBinId() == null || binRequest.getBinId().equals(0)) {
							binRequest.setPartBranchId(partBranchId.intValue());
							stockBinId = commonDao.updateStockInStockBin(session, binRequest,
									spareClaimUpdateRequest.getBranchId(), partDetailRequest.getBranchStoreId(),
									partRequest.getInvoiceQty(), "PA_CLAIM_DTL", userCode, "ADD");
							binList = new ArrayList<BranchSpareIssueBinStockResponse>();
							binRequest.setBinId(stockBinId);
							binList.add(binRequest);
							partRequest.setBinRequest(binList);
						} else {
							stockBinId = commonDao.updateStockInStockBin(session, binRequest,
									spareClaimUpdateRequest.getBranchId(), partDetailRequest.getBranchStoreId(),
									partRequest.getInvoiceQty(), "PA_CLAIM_DTL", userCode, "ADD");
						}
					}

				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Spare Claim Decision Saved Successfully";
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

	private com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest patchPartDetailRequest(
			PartDetailRequest partDetailRequest) {
		com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest partRequest = new com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest();
		partRequest.setPartBranchId(partDetailRequest.getPartBranchId());
		partRequest.setBranchStoreId(partDetailRequest.getBranchStoreId());
		partRequest.setBinId(partDetailRequest.getStockBinId());
		partRequest.setInvoiceQty(partDetailRequest.getRestrictedQty());
		partRequest.setBasicUnitPrice(partDetailRequest.getBasicUnitPrice());
		partRequest.setPartId(partDetailRequest.getPartId());
		partRequest.setBinRequest(patchBinData(partRequest));

		return partRequest;
	}

	private List<BranchSpareIssueBinStockResponse> patchBinData(
			com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest partDetailRequest) {
		List<BranchSpareIssueBinStockResponse> binList = new ArrayList<BranchSpareIssueBinStockResponse>();

		BranchSpareIssueBinStockResponse binRequest1 = new BranchSpareIssueBinStockResponse();
		binRequest1.setPartBranchId(partDetailRequest.getPartBranchId().intValue());
		binRequest1.setBranchStoreId(partDetailRequest.getBranchStoreId());
		binRequest1.setUnitPrice(partDetailRequest.getBasicUnitPrice());
		binRequest1.setBinLocation(partDetailRequest.getBinLocation());
		binRequest1.setIssueQty(partDetailRequest.getInvoiceQty());
		binRequest1.setBinId(partDetailRequest.getBinId());
		binList.add(binRequest1);

		partDetailRequest.setBinRequest(binList);

		return binList;
	}

	@Override
	public SpareGrnResponse approveOrReject(String userCode, String claimStatus, BigInteger claimHdrId,
			String claimType, BigInteger branchId) {

		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		SpareGrnResponse spareGrnResponse = new SpareGrnResponse();
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;

		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			BigInteger id = null;
			boolean isExist = false;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				SpareGrnDtlEntity spareGrnDtlEntity = new SpareGrnDtlEntity();
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				String currentDate = dateFormat1.format(new java.util.Date());

				String updateQuery = "update PA_GRN_CLAIM_HDR set ClaimStatus =:claimStatus,"
						+ " ModifiedBy = :modifiedBy where mrn_claim_hdr_id = :claimHdrId";

				query = session.createSQLQuery(updateQuery);
				query.setParameter("claimHdrId", claimHdrId);
				query.setParameter("claimStatus", claimStatus);
				query.setParameter("modifiedBy", userCode);
				query.executeUpdate();
			}

			if (claimType.equalsIgnoreCase("Excess Claim") && claimStatus.equalsIgnoreCase("Approved")) {

				List<PartNumberDetailResponse> partNumberDetailResponseList = fetchGrnClaimPartDetails(
						claimHdrId.intValue(), claimType);

				for (PartNumberDetailResponse partNumberDetailResponse : partNumberDetailResponseList) {

//					updateStockForIssue(session, partNumberDetailResponse, branchId, userCode);

					com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest partRequest = commonDao
							.updateStockForInPartBranchAndStockBin(session, "SUBTRACT",
									patchPartNumberData(partNumberDetailResponse), branchId, "PA_CLAIM_DTL", userCode);

					for (BranchSpareIssueBinStockResponse binRequest : partRequest.getBinRequest()) {
						BigInteger stockBinId = commonDao.updateStockInStockBin(session, binRequest,
								branchId, partRequest.getBranchStoreId(),
								partRequest.getInvoiceQty(), "PA_CLAIM_DTL", userCode, "SUBTRACT");
					}
				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Spare Claim Dealer Decision Saved Successfully";
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


	private com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest patchPartNumberData(
			PartNumberDetailResponse partNumberDetailResponse) {
		com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest partRequest = new com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest();
		partRequest.setPartBranchId(BigInteger.valueOf(partNumberDetailResponse.getPartBranchId()));
		partRequest.setBranchStoreId(partNumberDetailResponse.getBranchStoreId());
		partRequest.setBinId(partNumberDetailResponse.getStockBinId());
		partRequest.setInvoiceQty(partNumberDetailResponse.getClaimedQty());
		partRequest.setBasicUnitPrice(partNumberDetailResponse.getBasicUnitPrice());
		partRequest.setPartId(BigInteger.valueOf(partNumberDetailResponse.getPartId()));
		partRequest.setBinRequest(patchBinData(partRequest));

		return partRequest;
	}

	@Override
	public SpareGrnResponse agreeOrDisagree(String userCode, AgreeOrDisagreeClaimRequest agreeOrDisagreeRequest) {

		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		SpareGrnResponse spareGrnResponse = new SpareGrnResponse();
		Date todayDate = new Date();
		Query query = null;
		boolean isSuccess = true;

		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);

			BigInteger id = null;
			boolean isExist = false;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				SpareGrnDtlEntity spareGrnDtlEntity = new SpareGrnDtlEntity();
				SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				String currentDate = dateFormat1.format(new java.util.Date());

				if (agreeOrDisagreeRequest != null) {

//					Character isAgree = 'N';
//					if (agreeOrDisagreeRequest.getIsAgree().equals('Y')) {
//						isAgree = 'Y';
//					}

					String updateQuery = "update PA_GRN_CLAIM_HDR set IsAgree = :isAgree,"
							+ "ModifiedBy = :modifiedBy where mrn_claim_hdr_id = :claimHdrId";

					query = session.createSQLQuery(updateQuery);
					query.setParameter("isAgree", agreeOrDisagreeRequest.getIsAgree());
					query.setParameter("claimHdrId", agreeOrDisagreeRequest.getClaimHdrId());
					query.setParameter("modifiedBy", userCode);
					query.executeUpdate();
				}

			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Spare Claim Dealer Decision Saved Successfully";
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

	@Override
	public List<PartNumberDetailResponse> fetchGrnPartDetails(int grnHdrId, String pageOrClaimType) {
		Session session = null;
		List<PartNumberDetailResponse> partDetailResponsList = null;
		PartNumberDetailResponse partDetailResponse = null;

		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_Details] :grnHdrId";

		try {

			if (pageOrClaimType.equalsIgnoreCase("claimView") || pageOrClaimType.equalsIgnoreCase("Excess Claim")) {
				sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_Details] :grnHdrId, :page";
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("grnHdrId", grnHdrId);
				query.setParameter("page", pageOrClaimType);

			} else {
				sqlQuery = "exec [PA_Get_Spare_Grn_Dtl_By_Claim_Type] :grnHdrId, :claimType";
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("grnHdrId", grnHdrId);
				query.setParameter("claimType", pageOrClaimType);
			}

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partDetailResponsList = new ArrayList<PartNumberDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;

					partDetailResponse = new PartNumberDetailResponse();
					partDetailResponse.setGrnDtlId((BigInteger) row.get("mrn_dtl_id"));
					partDetailResponse.setPartNumber((String) row.get("PartNumber"));
					partDetailResponse.setPartDesc((String) row.get("PartDesc"));
					partDetailResponse.setPartBranchId((Integer) row.get("partBranch_id"));
					partDetailResponse.setPartSubCategory((String) row.get("partSubCategory"));
					partDetailResponse.setBinLocation((String) row.get("binName"));
					partDetailResponse.setAvailableQty((Integer) row.get("availableQty"));
					partDetailResponse.setHsnCode((String) row.get("hsnCode"));
					partDetailResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
					partDetailResponse.setSerialNumber((String) row.get("serialNumber"));
					partDetailResponse.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
					partDetailResponse.setInvoiceQty((BigDecimal) row.get("invoiceQty"));
					partDetailResponse.setUnRestrictedQty((BigDecimal) row.get("UnrestrictedQty"));
					partDetailResponse.setRestrictedQty((BigDecimal) row.get("RestrictedQty"));
					partDetailResponse.setClaimedQty((BigDecimal) row.get("ClaimQty"));
					partDetailResponse.setClaimValue((BigDecimal) row.get("ClaimValue"));
					partDetailResponse.setClaimStatus((String) row.get("ClaimStatus"));
					partDetailResponse.setRemarks((String) row.get("ClaimRemarks"));
					String isAgree = "N";
					if (((Character) row.get("IsAgree")).equals('Y')) {
						isAgree = "Y";
					}
					partDetailResponse.setIsAgree(isAgree);
					;
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
	public SpareGrnClaimResponse fetchGrnDetails(int grnHdrId, String pageName) {
		Session session = null;
		List<SpareGrnClaimResponse> spareGrnClaimResponseList = null;
		SpareGrnClaimResponse spareGrnClaimResponse = null;
		List<SpareGrnClaimPhotosEntity> spareGrnClaimPhotos = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Grn_Hdr_Details] :grnHdrId," + null + ", :pageName";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("grnHdrId", grnHdrId);
			query.setParameter("pageName", pageName);
//			query.setParameter("grnTypeId", (grnTypeId.compareTo(BigInteger.ZERO) == 0) ? null : grnTypeId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
//					spareGrnDetailsResponseList = new ArrayList<SpareGrnInventoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnClaimResponse = new SpareGrnClaimResponse();
					spareGrnClaimResponse.setId((BigInteger) row.get("mrn_claim_hdr_id"));
					spareGrnClaimResponse.setClaimNumber((String) row.get("ClaimGenerationNumber"));
					spareGrnClaimResponse.setClaimStatus((String) row.get("ClaimStatus"));
					spareGrnClaimResponse.setGrnNumber((String) row.get("MRNNumber"));
					spareGrnClaimResponse.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
					spareGrnClaimResponse.setInvoiceNumber((String) row.get("InvoiceNo"));
					spareGrnClaimResponse.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
					spareGrnClaimResponse.setClaimDate(parseDateInStringFormat((Date) row.get("ClaimDate")));
					spareGrnClaimResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
					spareGrnClaimResponse.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
					spareGrnClaimResponse.setDriverName((String) row.get("DriverName"));
					spareGrnClaimResponse.setDriverMobNo((String) row.get("DriverMobNo"));
					spareGrnClaimResponse.setTransporterName((String) row.get("transporterName"));
					spareGrnClaimResponse.setSupplier((String) row.get("PONumber"));

//					Character isAgree = (Character) row.get("IsAgree");

					spareGrnClaimResponse.setIsAgree((Character) row.get("IsAgree"));
					spareGrnClaimResponse.setProductCategory((String) row.get("productCategory"));
					spareGrnClaimResponse.setInvoiceAmount((BigDecimal) row.get("MRNInvoiceAmount"));

					spareGrnClaimResponse
							.setSpareGrnClaimPhotos(fetchSpareGrnClaimPhotos(spareGrnClaimResponse.getId()));
					;
				}

			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnClaimResponse;
	}

	@Override
	public SpareGrnClaimResponse fetchClaimDetails(int claimHdrId) {
		Session session = null;
		List<SpareGrnClaimResponse> spareGrnClaimResponseList = null;
		SpareGrnClaimResponse spareGrnClaimResponse = null;
		List<SpareGrnClaimPhotosEntity> spareGrnClaimPhotos = null;
		Query query = null;
		String sqlQuery = "exec [PA_Get_Spare_Claim_Hdr_Details] :claimHdrId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("claimHdrId", claimHdrId);
//			query.setParameter("grnTypeId", (grnTypeId.compareTo(BigInteger.ZERO) == 0) ? null : grnTypeId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
//					spareGrnDetailsResponseList = new ArrayList<SpareGrnInventoryResponse>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnClaimResponse = new SpareGrnClaimResponse();
					spareGrnClaimResponse.setId((BigInteger) row.get("mrn_claim_hdr_id"));
					spareGrnClaimResponse.setClaimNumber((String) row.get("ClaimGenerationNumber"));
					spareGrnClaimResponse.setClaimStatus((String) row.get("ClaimStatus"));
					spareGrnClaimResponse.setGrnNumber((String) row.get("MRNNumber"));
					spareGrnClaimResponse.setGrnDate(parseDateInStringFormat((Date) row.get("MRNDate")));
					spareGrnClaimResponse.setInvoiceNumber((String) row.get("InvoiceNo"));
					spareGrnClaimResponse.setInvoiceDate(parseDateInStringFormat((Date) row.get("InvoiceDate")));
					spareGrnClaimResponse.setClaimDate(parseDateInStringFormat((Date) row.get("ClaimDate")));
					spareGrnClaimResponse.setPartyCategoryName((String) row.get("PartyCategoryName"));
					spareGrnClaimResponse.setPartyCategoryCode((String) row.get("PartyCategoryCode"));
					spareGrnClaimResponse.setDriverName((String) row.get("DriverName"));
					spareGrnClaimResponse.setDriverMobNo((String) row.get("DriverMobNo"));
					spareGrnClaimResponse.setTransporterName((String) row.get("transporterName"));
					spareGrnClaimResponse.setSupplier((String) row.get("PONumber"));

//					Character isAgree = (Character) row.get("IsAgree");

					spareGrnClaimResponse.setIsAgree((Character) row.get("IsAgree"));
					spareGrnClaimResponse.setProductCategory((String) row.get("productCategory"));
					spareGrnClaimResponse.setInvoiceAmount((BigDecimal) row.get("MRNInvoiceAmount"));

					spareGrnClaimResponse
							.setSpareGrnClaimPhotos(fetchSpareGrnClaimPhotos(spareGrnClaimResponse.getId()));
					;
				}

			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnClaimResponse;
	}

	private List<SpareGrnClaimPhotosEntity> fetchSpareGrnClaimPhotos(BigInteger id) {
		Session session = null;
		List<SpareGrnClaimPhotosEntity> spareGrnClaimPhotos = null;
		SpareGrnClaimPhotosEntity spareGrnClaimPhoto = null;
		Query query = null;
		String sqlQuery = "  select * from PA_GRN_CLAIM_PHOTOS where mrn_claim_hdr_id = :grnClaimHdrId";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("grnClaimHdrId", id);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				spareGrnClaimPhotos = new ArrayList<SpareGrnClaimPhotosEntity>();
				for (Object object : data) {
					Map row = (Map) object;
					spareGrnClaimPhoto = new SpareGrnClaimPhotosEntity();
					spareGrnClaimPhoto.setId((BigInteger) row.get("id"));
					spareGrnClaimPhoto.setFileType((String) row.get("file_type"));
					spareGrnClaimPhoto.setFileName((String) row.get("file_name"));
					spareGrnClaimPhoto.setCreatedBy((String) row.get("CreatedBy"));
					spareGrnClaimPhoto.setCreatedDate((Date) row.get("CreatedDate"));
					spareGrnClaimPhotos.add(spareGrnClaimPhoto);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return spareGrnClaimPhotos;
	}

	public String parseDateInStringFormat(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String newDate = dateFormat.format(date);
		return newDate;
	}

	@Override
	public HashMap<BigInteger, String> searchClaimNumber(String searchType, String searchText, String userCode) {
		Session session = null;
		String grnNumber = null;
		String claimType = null;
		System.out.println("searchText " + searchText);
		System.out.println("searchType " + searchType);
		System.out.println("ClaimType " + claimType);
		System.out.println("userCode " + userCode);

		HashMap<BigInteger, String> searchList = null;
		Query query = null;
		String sqlQuery = "exec [PA_Search_Spare_Grn_List] :searchText, :isFor, :claimType, :userCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", searchType);
			query.setParameter("searchText", searchText);
			query.setParameter("claimType", claimType!=null ? claimType : null);
			query.setParameter("userCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				searchList = new HashMap<>();
				for (Object object : data) {
					Map row = (Map) object;
					/* only GRN created from VST is allowed */
					if(searchType.equalsIgnoreCase("CLAIMNO")) {
						searchList.put((BigInteger) row.get("mrn_hdr_id"), (String) row.get("ClaimGenerationNumber"));
					}
					else if (((String) row.get("Grn_type")).equalsIgnoreCase("VST")) {
						searchList.put((BigInteger) row.get("mrn_hdr_id"), (String) row.get("MRNNumber"));
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
	public SpareGrnResponse ReSubmitSpareClaim(String fileName, String fileType, String userCode,
			SpareClaimRequest spareClaimRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
			logger.debug(spareClaimRequest.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Map<String, Object> mapData = null;
		SpareGrnResponse spareGrnResponse = new SpareGrnResponse();
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
			String claimNumber = spareClaimRequest.getClaimNumber();
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				if (fileName != null) {
					BigInteger claimPhotosId = saveFileDetails(fileName, fileType, userCode,
							spareClaimRequest.getGrnClaimHdrId(), null, spareClaimRequest);
					updateClaimStatus(session, spareClaimRequest.getGrnClaimHdrId(), userCode);
				}
			}

			if (isSuccess) {
				transaction.commit();
				session.close();
				spareGrnResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
				msg = "Spare Claim Updated Successfully with Claim Number - " + claimNumber;
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

	private void updateClaimStatus(Session session, BigInteger grnClaimHdrId, String userCode) {
		Transaction transaction = null;
		String msg = null;
		int statusCode = 0;

		Query query = null;
		boolean isSuccess = true;

		try {
			BigInteger userId = null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			String updateQuery = "update PA_GRN_CLAIM_HDR set ClaimStatus = :ClaimStatus, IsAgree = :IsAgree,"
					+ "ModifiedBy = :modifiedBy where mrn_claim_hdr_id = :mrn_claim_hdr_id";

			query = session.createSQLQuery(updateQuery);
			query.setParameter("ClaimStatus", "Waiting For Approval");
			query.setParameter("IsAgree", 'A');
			query.setParameter("mrn_claim_hdr_id", grnClaimHdrId);
			query.setParameter("modifiedBy", userCode);
			query.executeUpdate();

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

	@Override
	public List<PartNumberDetailResponse> fetchGrnClaimPartDetails(int claimHdrId, String pageOrClaimType) {
		Session session = null;
		List<PartNumberDetailResponse> partDetailResponsList = null;
		PartNumberDetailResponse partDetailResponse = null;

		Query query = null;
		String sqlQuery = null;

		try {
			sqlQuery = "exec [PA_Get_Spare_Claim_Dtl_By_Claim_Type] :claimHdrId, :claimType";
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("claimHdrId", claimHdrId);
			query.setParameter("claimType", pageOrClaimType);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				partDetailResponsList = new ArrayList<PartNumberDetailResponse>();
				for (Object object : data) {
					Map row = (Map) object;

					partDetailResponse = new PartNumberDetailResponse();
					partDetailResponse.setGrnDtlId((BigInteger) row.get("mrn_dtl_id"));
					partDetailResponse.setPartNumber((String) row.get("PartNumber"));
					partDetailResponse.setPartDesc((String) row.get("PartDesc"));
					partDetailResponse.setPartBranchId((Integer) row.get("partBranch_id"));
					partDetailResponse.setPartSubCategory((String) row.get("partSubCategory"));
					partDetailResponse.setBinLocation((String) row.get("binName"));
					partDetailResponse.setAvailableQty((Integer) row.get("availableQty"));
					partDetailResponse.setHsnCode((String) row.get("hsnCode"));
					partDetailResponse.setStockBinId((BigInteger) row.get("stock_bin_id"));
					partDetailResponse.setBranchStoreId((Integer) row.get("branch_store_id"));
					partDetailResponse.setSerialNumber((String) row.get("serialNumber"));
					partDetailResponse.setBasicUnitPrice((BigDecimal) row.get("basicUnitPrice"));
					partDetailResponse.setInvoiceQty((BigDecimal) row.get("invoiceQty"));
					partDetailResponse.setUnRestrictedQty((BigDecimal) row.get("UnrestrictedQty"));
					partDetailResponse.setRestrictedQty((BigDecimal) row.get("RestrictedQty"));
					partDetailResponse.setClaimedQty((BigDecimal) row.get("ClaimQty"));
					partDetailResponse.setClaimValue((BigDecimal) row.get("ClaimValue"));
					partDetailResponse.setClaimStatus((String) row.get("ClaimStatus"));
					partDetailResponse.setRemarks((String) row.get("ClaimRemarks"));
					String isAgree = "N";
					if (((Character) row.get("IsAgree")).equals('Y')) {
						isAgree = "Y";
					}
					partDetailResponse.setIsAgree(isAgree);
					;
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

}
