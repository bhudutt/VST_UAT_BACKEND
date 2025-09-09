package com.hitech.dms.web.dao.partmaster.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.spare.party.model.mapping.request.PartBranchRequestModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartBranchStoreResponseModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartStockBinRequestModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartTableRequestModel;
import com.hitech.dms.web.spare.party.model.mapping.request.PartyMappingResponseModel;

@Repository
public class PartMasterDaoCreateImpl implements PartMasterDaoCreate {

	private static Logger logger = LoggerFactory.getLogger(PartMasterDaoCreateImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public PartyMappingResponseModel fetchPartNumberDetailsnew(String userCode, String partNumber,String isFor) {
		boolean isSuccess = false;
		List<PartStockBinRequestModel> stockBinDetailList= new ArrayList<>();
		BigInteger branchId=getBranchIdByUSerCode(userCode);
	
		
		PartyMappingResponseModel response= new PartyMappingResponseModel();
		PartBranchRequestModel branchRequest= new PartBranchRequestModel();
		List<PartBranchStoreResponseModel> partBranchStoreList=null;
		System.out.println("Calling from this method " + partNumber + " " + userCode);
		
		try
		{
			
		PartTableRequestModel req = getPartNumberDetail(userCode, partNumber);
		System.out.println("req after method call "+req);
		System.out.println("req after method call "+req.getPartNumber());

		if(req!=null && req.getPartNumber()!=null)
		{
			System.out.println("in if condititon ");
			response.setPartDetails(req);
			isSuccess=true;
			branchRequest=getBranchPartDetailByPartId(req.getPartId(),branchId,isFor);
			System.out.println("after response  at top ");
			if(branchRequest!=null || branchRequest.getPartBranchId()!=0 ) 
			{
			
				partBranchStoreList=getPartBranchStoreDetailList(branchRequest.getBranchId(),req.getPartId());
				System.out.println("after response ");
				//stockBinDetailList=getSTockBinDetailList(branchRequest.getPartBranchId());
				response.setPartBranchDetail(branchRequest);
				response.setBranchStoreList(partBranchStoreList);
				//response.setBranchStockList(stockBinDetailList);
				

			}
			else
			{
				isSuccess=false;
			}
			
		}
		}catch(Exception e)
		{
			isSuccess=false;
			System.out.println(e.getMessage());
		}
		
		if(isSuccess)
		{
			response.setStatusMessage("Success ");
			response.setStatusCode(200);
			return response;
		}
		
		response.setStatusMessage("Not able to fatch data for partNo "+partNumber);
		response.setStatusCode(302);
		return response;
	}
	
	
	private BigInteger getBranchIdByUSerCode(String userCode) {
		
		BigInteger branchId=BigInteger.ZERO;
		try
		{
			Query query=null;
			System.out.println("UserCode "+userCode);
			Session session = null;
			session = sessionFactory.openSession();
			 query = session.createSQLQuery("SELECT * FROM FN_CM_GETBRANCH_BYUSERCODE(:userCode, 'N')");
			    query.setParameter("userCode", userCode);			
			    List<Object[]> data = query.list();
			    if (data != null && !data.isEmpty()) {
			        for (Object[] row : data) {
			            // Assuming BRANCH_ID is the first column
			            branchId = (BigInteger) row[0];
			            System.out.println("get Branch Id " + branchId);
			        }
			    }
			
		}catch(Exception e)
		{
			
			e.printStackTrace();
		}
		
		return branchId;
	}

	private List<PartStockBinRequestModel> getSTockBinDetailList(int partBranchId) {
		
		System.out.println("Part branch id for StockBin "+partBranchId);
		List<PartStockBinRequestModel> stockBinList= new ArrayList<>();
		PartStockBinRequestModel model=null;
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		Session session = null;
		String SqlQuery=null;

		mapData1.put("ERROR", "ISSUE Details Not Found");
		try {
			session = sessionFactory.openSession();
            SqlQuery="Select * from pa_stock_bin where partBranch_id=:partBranchId";
    		System.out.println("Sql Query is  " + SqlQuery);
			query = session.createNativeQuery(SqlQuery);
			query.setParameter("partBranchId",partBranchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger issueId = null;
				for (Object object : data) {
					model= new PartStockBinRequestModel();
					Map row = (Map) object;
					
					BigInteger stcokBinId=(BigInteger) row.get("stock_bin_id");
					model.setStockBinId(stcokBinId!=null?stcokBinId:null);
					BigInteger partBranch_Id=(BigInteger) row.get("partBranch_id");
					model.setPartBranchId(partBranch_Id!=null?partBranch_Id:null);
					char isDefault=(char) row.get("IsDefault");
					model.setIsDefault(String.valueOf(isDefault)!=null ?String.valueOf(isDefault):"");
					char isActive=(char) row.get("IsActive");
					model.setIsActive(String.valueOf(isActive)!=null ?String.valueOf(isActive):"");
					
					String binName=(String) row.get("BinName");
					model.setBinName(binName!=null ? binName :"");
					String binType=(String) row.get("BinType");
					model.setBinType(binType!=null? binType :"");
					model.setCurrentStock((BigDecimal)row.get("currentStock"));
					

					
					stockBinList.add(model);
				
					
					
				}
			}
			
			
			
			
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
	//	System.out.println("before sending list "+stockBinList);	
	
		
		
		
		return stockBinList;
	}

	private List<PartBranchStoreResponseModel> getPartBranchStoreDetailList(int branchId, int partId) {
		
		System.out.println("get branchId "+branchId);
		List<PartBranchStoreResponseModel> branchStoreList= new ArrayList<>();
		PartBranchStoreResponseModel model=null;
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		Session session = null;
		String SqlQuery=null;

		mapData1.put("ERROR", "ISSUE Details Not Found");
		try {
			session = sessionFactory.openSession();
            SqlQuery="exec SP_PART_BRANCH_MASTER :branchid,:PartId";
    		System.out.println("Sql Query is  " + SqlQuery);
			query = session.createNativeQuery(SqlQuery);
			query.setParameter("branchid", branchId);
			query.setParameter("PartId", partId);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					model= new PartBranchStoreResponseModel();
					Map row = (Map) object;
					
					model.setPartNumber((String)row.get("partnumber"));
					model.setPartDesc((String) row.get("partdesc"));
					model.setStoreDesc((String)row.get("Stores"));
					model.setBinName((String)row.get("Binname"));
					model.setIsActive((String)row.get("IsActive"));
					model.setIsMainStore((String) row.get("IsDefault"));
					model.setStockInHand((BigDecimal)row.get("StockInHand"));
					
					
//					Integer branchStoreId=(Integer) row.get("branch_store_id");
//					model.setBranchStoreId(branchStoreId != null ? branchStoreId.intValue() : 0);
//					String storeCode=(String) row.get("StoreCode");
//					
//					model.setStoreCode(storeCode != null ? storeCode : "");
//					String storeDesc=(String) row.get("StoreDesc");
//					model.setStoreDesc(storeDesc != null ? storeDesc : "");
//					char isActive=(char) row.get("IsActive");
//					model.setIsActive(String.valueOf(isActive) != null ? String.valueOf(isActive) : "");
//					char isMainStore=(char) row.get("isMainStore");
//					model.setIsMainStore( String.valueOf(isMainStore)!= null ? String.valueOf(isMainStore) : "");
					
					branchStoreList.add(model);
				
					
					
				}
			}
			
			
			
			
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		System.out.println("before sending branchStock "+branchStoreList);	
	
		return branchStoreList;
	}

	private PartBranchRequestModel getBranchPartDetailByPartId(int partId, BigInteger branchId2, String isFor) {
		
		PartBranchRequestModel  branchRequestModel =null;
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		Session session = null;
		String SqlQuery=null;

		 Integer branch_id = branchId2.intValue();
		mapData1.put("ERROR", "ISSUE Details Not Found");
		try {
			session = sessionFactory.openSession();
			
            SqlQuery="exec PA_PART_PRICE_BY_USER :part_id,:branch_id,:isFor";
    		System.out.println("Sql Query is  " + SqlQuery);
			query = session.createNativeQuery(SqlQuery);
			query.setParameter("part_id", partId);
			query.setParameter("branch_id", branch_id);
			query.setParameter("isFor", isFor);


			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger issueId = null;
				for (Object object : data) {
					branchRequestModel= new PartBranchRequestModel();
					
					Map row = (Map) object;
					Integer partBranchId=(Integer) row.get("partBranch_id");
					branchRequestModel.setPartBranchId(partBranchId != null ? partBranchId.intValue() : 0);
					Integer part_Id=(Integer) row.get("part_id");
					branchRequestModel.setPartId(part_Id != null ? part_Id.intValue() : 0);
					Integer branchId=(Integer) row.get("branch_id");
					branchRequestModel.setBranchId(branchId != null ? branchId.intValue() : 0);
					Integer accountCategoryId=(Integer) row.get("accountCategory_id");
					branchRequestModel.setAccountCategoryId(accountCategoryId != null ? accountCategoryId.intValue() : 0);
					BigDecimal mrp=(BigDecimal) row.get("MRP");
					
					branchRequestModel.setMrp(mrp != null ? mrp.floatValue() : 0);
					
					BigDecimal ndp=(BigDecimal) row.get("NDP");
					branchRequestModel.setNdp(ndp != null ? ndp.floatValue() : 0);
					BigDecimal sellingPrice=(BigDecimal) row.get("SellingPrice");

					branchRequestModel.setSellingPrice(sellingPrice != null ? sellingPrice.floatValue() : 0);

					Integer minLevelQty=(Integer) row.get("MinLevelQty");
					branchRequestModel.setMinLevelQty(minLevelQty != null ? minLevelQty.intValue() : 0);

					Integer maxLevelQty=(Integer) row.get("MaxLevelQty");
					branchRequestModel.setMaxLevelQty(maxLevelQty != null ? maxLevelQty.intValue() : 0);

					Integer recordLevelQty=(Integer) row.get("ReorderLevelQty");
					branchRequestModel.setRecordLevelQty(recordLevelQty != null ? recordLevelQty.intValue() : 0);
					
					Float reOrderQty=(Float) row.get("ReorderQty");
					branchRequestModel.setReOrderQty(reOrderQty!=null?reOrderQty:0);
					
					Integer movementClass_id=(Integer) row.get("movementclass_id");
					branchRequestModel.setMovementClassId(movementClass_id != null ? movementClass_id.intValue() : 0);
					
					Integer reserveLevelQty=(Integer) row.get("WorkshopReserveQty");
					branchRequestModel.setReserveLevelQty(reserveLevelQty != null ? reserveLevelQty.intValue() : 0);
					

					String ABC = (String) row.get("ABC");
					branchRequestModel.setAbc(ABC != null ? ABC : "");
					
					String FMS = (String) row.get("FMS");
					branchRequestModel.setFms(FMS != null ? FMS : "");

					String VED = (String) row.get("VED");
					branchRequestModel.setVed(VED != null ? VED : "");
					
					String isSeasonal = (String) row.get("IsSeasonal");
					branchRequestModel.setIsSeasonal(isSeasonal != null ? isSeasonal : "");
					
					BigDecimal vorQty=(BigDecimal) row.get("VOR_Qty");
					branchRequestModel.setVorQty(vorQty != null ? vorQty.floatValue() : 0);
					
					String isAuctionable = (String) row.get("IsAuctionable");
					branchRequestModel.setIsAuctionable(isAuctionable != null ? isAuctionable : "");

					String auctionablePart = (String) row.get("Auctionable_Part");
					branchRequestModel.setAuctionablePart(auctionablePart != null ? auctionablePart : "");
					
					BigDecimal auctionableQty=(BigDecimal) row.get("Auctionable_Qty");
					branchRequestModel.setAuctionableQty(auctionableQty != null ? auctionableQty.floatValue() : 0);
					
					BigDecimal stockAmount=(BigDecimal) row.get("StockAmount");
					branchRequestModel.setStockAmount(stockAmount != null ? stockAmount.floatValue() : 0);

					Integer onHandQty=(Integer) row.get("OnHandQty");
					branchRequestModel.setOnHandQty(onHandQty != null ? onHandQty.intValue() : 0);

					Integer backOrderQty=(Integer) row.get("BackOrder_Qty");
					branchRequestModel.setBackOrderQty(backOrderQty != null ? backOrderQty.intValue() : 0);

					Integer wipQty=(Integer) row.get("WIP_QTY");
					branchRequestModel.setWipQty(wipQty != null ? wipQty.intValue() : 0);

					Integer inTransitQty=(Integer) row.get("InTransitQty");
					branchRequestModel.setInTransitQty(inTransitQty != null ? inTransitQty.intValue() : 0);


					
				}
			}
			
			
			
			
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		System.out.println("we get result at end is  "+branchRequestModel);

		return branchRequestModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private PartTableRequestModel getPartNumberDetail(String userCode, String partNumber) {
		PartTableRequestModel partrequestResponse = null;
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		Session session = null;
		String sqlQuery = "exec PART_TAX_BY_PART_ID :PartNumber,:UserCode";
		System.out.println("Sql Query is  " + sqlQuery);

	//	mapData1.put("ERROR", "ISSUE Details Not Found");
		try {
			session = sessionFactory.openSession();

			query = session.createNativeQuery(sqlQuery);
			query.setParameter("PartNumber",partNumber);
			query.setParameter("UserCode",userCode);

			// query.setResultTransformer(Transformers.aliasToBean(PartTableRequestModel.class));
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger issueId = null;
				for (Object object : data) {
					Map row = (Map) object;
					System.out.println("In enter the data "+(Integer) row.get("part_id"));
					partrequestResponse = new PartTableRequestModel();
					Integer partId=(Integer) row.get("part_id");
					String PartNumber = (String) row.get("PartNumber");
					String partDesc = (String) row.get("PartDesc");
					partrequestResponse.setPartId((partId != null ? partId.intValue() : 0));
					partrequestResponse.setPartNumber(PartNumber);

					partrequestResponse.setPartDesc(partDesc);
					Integer uomId = (Integer) row.get("uom_id");
					partrequestResponse.setUomId(uomId != null ? uomId.intValue() : 0);
					String AltPartNumber = (String) row.get("AltPartNumber");
					partrequestResponse.setAltPartNumber(AltPartNumber != null ? AltPartNumber : "");
					Integer issueindicator_id = (Integer) row.get("issueindicator_id");
					partrequestResponse
							.setIssueIndiacatorId((issueindicator_id != null ? issueindicator_id.intValue() : 0));

					Integer partcategory_id = (Integer) row.get("partcategory_id");

					partrequestResponse.setPartCategoryId(partcategory_id != null ? partcategory_id.intValue() : 0);

					String AllowDecimalInQty = (String) row.get("AllowDecimalInQty");
					partrequestResponse.setAllowDecimalQty(AllowDecimalInQty != null ? AllowDecimalInQty : "");

					Integer MinOrderQty = (Integer) row.get("MinOrderQty");

					partrequestResponse.setMinOrderQty(MinOrderQty != null ? MinOrderQty.intValue() : 0);

					Integer AGGREGATE_ID = (Integer) row.get("AGGREGATE_ID");

					partrequestResponse.setAggregateId(AGGREGATE_ID != null ? AGGREGATE_ID.intValue() : 0);

					Integer Model_Group_Id = (Integer) row.get("Model_Group_Id");

					partrequestResponse.setModelGroupId(Model_Group_Id != null ? Model_Group_Id.intValue() : 0);

					Integer MODEL_PLATFORM_ID = (Integer) row.get("MODEL_PLATFORM_ID");

					partrequestResponse
							.setModelPlatformId(MODEL_PLATFORM_ID != null ? MODEL_PLATFORM_ID.intValue() : 0);
					Integer PerVehicleQty = (Integer) row.get("PerVehicleQty");

					partrequestResponse.setPerVehicleQuantity(PerVehicleQty != null ? PerVehicleQty.intValue() : 0);

					Integer PackQty = (Integer) row.get("PackQty");

					partrequestResponse.setPackQty(PackQty != null ? PackQty.intValue() : 0);
					String OrderToOEM = (String) row.get("OrderToOEM");

					partrequestResponse.setOrderToOem(OrderToOEM != null ? OrderToOEM : "");

					Integer Model_Variant_Id = (Integer) row.get("Model_Variant_Id");

					partrequestResponse.setModelVariantId(Model_Variant_Id != null ? Model_Variant_Id.intValue() : 0);

					String IsPartUnderWarranty = (String) row.get("IsPartUnderWarranty");

					partrequestResponse.setIsPartWarranty(IsPartUnderWarranty != null ? IsPartUnderWarranty : "");

					String Is_MFG_PartNo = (String) row.get("Is_MFG_PartNo");

					partrequestResponse.setIsPartMfgNo(Is_MFG_PartNo != null ? Is_MFG_PartNo : "");
					String HSN_CODE = (String) row.get("HSN_CODE");

					partrequestResponse.setHssnCode(HSN_CODE != null ? HSN_CODE : "");

					BigDecimal IGST = (BigDecimal) row.get("IGST");
					

					partrequestResponse.setIGst(IGST.toBigInteger()!= null ? IGST.toBigInteger() : null);

					BigDecimal CGST = (BigDecimal) row.get("CGST");

					partrequestResponse.setCGst(CGST.toBigInteger() != null ? CGST.toBigInteger() : null);

					BigDecimal SGST = (BigDecimal) row.get("SGST");

					partrequestResponse.setSGst(SGST.toBigInteger() != null ? SGST.toBigInteger() : null);

				}
				// mapData1.put("issueId", issueId);
				// mapData1.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		System.out.println("we get result at end " + partrequestResponse);
		return partrequestResponse;
	}

}
