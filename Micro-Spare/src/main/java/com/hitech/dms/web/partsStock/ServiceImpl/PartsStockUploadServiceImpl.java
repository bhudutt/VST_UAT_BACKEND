package com.hitech.dms.web.partsStock.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.user.InvoiceReturnApprovalEntity;
import com.hitech.dms.web.entity.user.SparePartsStockEntity;
import com.hitech.dms.web.entity.user.StockUploadStagingEntity;
import com.hitech.dms.web.partsStock.Dao.PartsStockUploadCreateDao;
import com.hitech.dms.web.partsStock.Model.ExcelUploadMultiTableStatus;
import com.hitech.dms.web.partsStock.Model.PartBranchDetailStatus;
import com.hitech.dms.web.partsStock.Model.PartsSearchResponseModel;
import com.hitech.dms.web.partsStock.Model.PartsStockUploadError;
import com.hitech.dms.web.partsStock.Model.PartsStockUploadModel;
import com.hitech.dms.web.partsStock.Model.StockBinDetailResponseModel;
import com.hitech.dms.web.partsStock.Model.StockSaveHeaderResponseModel;
import com.hitech.dms.web.partsStock.Model.StoreSearchResponseModel;
import com.hitech.dms.web.partsStock.Service.PartsStockUploadService;
import com.hitech.dms.web.partsStockController.create.response.PartsUploadCreateResponse;

@Service
public class PartsStockUploadServiceImpl implements PartsStockUploadService {

	@Autowired
	PartsStockUploadCreateDao partsUploadDao;

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(PartsStockUploadServiceImpl.class);

	@Override
	public PartsUploadCreateResponse partsUploadService(String authorizationHeader, String userCode, Integer delaer,
			Integer branch, MultipartFile file) {
		PartsUploadCreateResponse response = partsUploadDao.partsStockUploadDao(authorizationHeader, userCode, delaer,
				branch, file);
		 Integer rowCount1=0;
		PartsUploadCreateResponse responseservice = new PartsUploadCreateResponse();
		List<PartsStockUploadModel> partsStockList = response.getExcelListOfParts();
		Map<String, String> errorData1 = new LinkedHashMap<>();
		List<StockUploadStagingEntity> stgEntity= response.getStagingPartsEntity();
		List<SparePartsStockEntity> partsList= response.getSparePartsList();
		Map<String, String> errorData = response.getErrorPartsData();
		int statusCode = response.getStatusCode();
		Random random = new Random();

		Session session = null;
		Query query = null;
		String sqlQuery = null;
		boolean isSucces = false;
		if(response.getStatusCode()==407 || response.getMessage().equals("INVALID EXCEL FORMAT.")) {
		 	responseservice.setMessage(response.getMessage());
			responseservice.setStatusCode(302);		
			return responseservice;
		}
	
		if((response.getExcelListOfParts()== null || response.getExcelListOfParts().isEmpty()) && (response.getErrorPartsData()==null || response.getErrorPartsData().isEmpty()))
		{
			System.out.println("If  excel is Empty ");
		 	responseservice.setMessage("Excel is empty please fill the data");
			responseservice.setStatusCode(302);		
			return responseservice;
		}
		if (response.getErrorPartsData().isEmpty() && statusCode == 201) {

			String message = null;
			try {
				
					Transaction tx=null;
					session = sessionFactory.openSession();
					tx=session.beginTransaction();
					for(SparePartsStockEntity entity:partsList)
					{
						rowCount1++;
						session.save(entity);
					}
					
					tx.commit();
					
					message = "Data Save successfully";
					responseservice.setMessage(message);
					responseservice.setTotalRowCount(rowCount1);
					responseservice.setStatusCode(200);

					System.out.println("AFTER SAVE " + responseservice);
					isSucces = true;
				} catch (SQLGrammarException exp) {
				if (message == null) {
					message = exp.getMessage();

				}
				isSucces = false;
				logger.error(this.getClass().getName(), exp);

			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);
				if (message == null) {
					message = exp.getMessage();

				}
				isSucces = false;
			} catch (Exception exp) {
				if (message == null) {
					message = exp.getMessage();

				}
				isSucces = false;

				logger.error(this.getClass().getName(), exp);
			} finally {
				if (session != null) {
					session.close();
				}
			}
			if (isSucces = false) {
				responseservice.setMessage(message);
				responseservice.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				return responseservice;

			}
			
			Integer partsStockStatus=0;
			List<PartsStockUploadError> errorList= new ArrayList<>();
			if(rowCount1 >0)
			{
				  errorList=validateStock();
				if(errorList==null)
				{
					
					
					Integer saveStatus=saveStockToStaging(stgEntity);
					if(saveStatus>=1)
					{
						partsStockStatus=savePartsStock();
						if(partsStockStatus>=1)
						{
							message="Stock Uploaded Successfully";
							responseservice.setMessage(message);
							responseservice.setTotalRowCount(rowCount1);
							responseservice.setStatusCode(200);
						}
						else
						{
							message="Stock Not uploaded to main Table";
							responseservice.setMessage(message);
							responseservice.setTotalRowCount(rowCount1);
							responseservice.setStatusCode(200);
							
						}
					}
					else
					{
						message="Data Not Upload to Staging table";
						responseservice.setMessage(message);
						responseservice.setTotalRowCount(rowCount1);
						responseservice.setStatusCode(200);
						
					}
					
					
					
				}
				else
				{
					

					for(PartsStockUploadError error : errorList)
					{
						String storeMessage="";
						String partMessage="";
						System.out.println("error "+error);
						if(error.getPartRemarks()!=null)
						{
							 
								 partMessage=error.getPartRemarks();	

							 

							
						}
						if(error.getStoreRemarks()!=null)
						{
							 storeMessage=error.getStoreRemarks();

						}
						String finalMessage="   "+storeMessage;
						String msg =error.getPartNo()+" "+ partMessage   +"  "+"," + finalMessage;

						errorData.put(Integer.toString(random.nextInt(1000)),msg );

						//errorData.put(partMessage,storeMessage);

					}
					message="Stock Not uploaded Successfully";
					responseservice.setMessage(message);
					responseservice.setErrorPartsData(errorData);
					responseservice.setTotalRowCount(rowCount1);
					responseservice.setStatusCode(500);
				}
			}

			
			

			
			return responseservice;

		}
		
		
			//System.out.println("return from this place last ");
			response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			response.setMessage("Error while uploading excel");
			

		 

		return response;
	}
	
	

	private Integer saveStockToStaging(List<StockUploadStagingEntity> stgEntity) {
		Integer saveStockStatus=0;
		Session session=null;
		try
		{
			
			session=sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for(StockUploadStagingEntity entity:stgEntity)
			{
				session.save(entity);
			}
			
			tx.commit();
			saveStockStatus=1;
			
		}
		catch(Exception e)
		{
			saveStockStatus=0;
			e.printStackTrace();
		}
		
		
		return saveStockStatus;
	}



	private Integer savePartsStock() {
		Integer saveStockStatus=0;
		Session session=null;
		Query query=null;
		session=sessionFactory.openSession();
		String sqlQuery = "exec UploadStockData";
		Transaction transaction = session.beginTransaction();
        query = session.createSQLQuery(sqlQuery);
		 query.executeUpdate();
		 transaction.commit();
		 System.out.println("Commit data successfully ");
		 saveStockStatus=1;
		return saveStockStatus;
	}

	
	
	@SuppressWarnings("deprecation")
	private List<PartsStockUploadError> validateStock() {
		Integer saveStockStatus=0;
		List<PartsStockUploadError> errorList= new ArrayList<>();
		Session session=null;
		Query query=null;
		try
		{
		session=sessionFactory.openSession();
		String sqlQuery = "exec SP_CheckPartAndStoreExistence";
        query = session.createSQLQuery(sqlQuery);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();
		if (data != null && !data.isEmpty()) {

        	System.out.println("in this condition");
            for (Object obj : data) {
				Map row = (Map) obj;

                PartsStockUploadError error = new PartsStockUploadError();
                error.setPartNo((String) row.get("PartNumber"));
                error.setPartRemarks((String) row.get("PartRemarks"));
                error.setStoreCode((String) row.get("StoreCode"));
                error.setStoreRemarks((String) row.get("StoreRemarks"));
                errorList.add(error);
       		 	saveStockStatus=1;

            }
        }

      
		}
        catch(Exception e )
        {
        	e.printStackTrace();
        	errorList=null;
        	saveStockStatus=0;

        }
		finally
		{
			if(saveStockStatus==1)
			{
				
			}
			else
			{
				errorList=null;
			}
			
		}
		return errorList;
	}
	private boolean checkDuplicatePartNo(List<PartsStockUploadModel> excelListOfParts) {
		
		
		Map<String, String> partNoToBinLocationMap = new HashMap<>();
        boolean isDuplicate = false;

        for (PartsStockUploadModel model : excelListOfParts) {
            String partNo = model.getPartNo();
            String storeBinLocation = model.getStoreBinLocation();

            // Check if partNo already exists in the map
            if (partNoToBinLocationMap.containsKey(partNo)) {
                // If yes, check if the bin location is the same
                String existingBinLocation = partNoToBinLocationMap.get(partNo);
                if (existingBinLocation.equals(storeBinLocation)) {
                    // If bin locations are the same, it's a duplicate
                    isDuplicate = true;
                    break;
                }
            } else {
                // If partNo does not exist in the map, add it along with its bin location
                partNoToBinLocationMap.put(partNo, storeBinLocation);
            }
        }
        return isDuplicate;

		

	    
//		Set<String> seenPartNames = new HashSet<>();
//		Set<String> seenBinName = new HashSet<>();
//
//		boolean isDuplicate = false;
//		for (PartsStockUploadModel model : excelListOfParts) {
//		    String partNo = model.getPartNo();
//		    String binLocation=model.getStoreBinLocation();
//		    if (seenPartNames.contains(partNo)) {
//		    	isDuplicate = true; 
//			        break; 
//		    	} else {
//		        seenPartNames.add(partNo);
//		        seenBinName.contains(binLocation);
//		    }
//		}
//		return isDuplicate;
	}

	private ExcelUploadMultiTableStatus exceuploadStatus(List<PartsStockUploadModel> list, Integer branch, String userCode,
			Integer delaer, List<StoreSearchResponseModel> storeList,Integer totalRecords) {
		ExcelUploadMultiTableStatus statusOp= new ExcelUploadMultiTableStatus();
		try
		{
		PartBranchDetailStatus res = partsUploadDao.checkStockBinandUploadStockDetail(storeList, branch, userCode, delaer, list,storeList);
		//System.out.print("at serviceImpl partBranchDetail res new " + res);
		if (res.getStatusCode() == 200) {
			StockSaveHeaderResponseModel stockHeaderResponse = new StockSaveHeaderResponseModel();

			stockHeaderResponse = partsUploadDao.saveStockHeaderWithPartBranchId(res.getPartBranchDetailResponse(),
					branch, userCode, delaer, list);
			//System.out.print("at serviceImpl stockHeaderResponse " + stockHeaderResponse);
			StockBinDetailResponseModel stockDtlres = new StockBinDetailResponseModel();
			if (stockHeaderResponse.getStatusCode() == 200) {

				stockDtlres = partsUploadDao.updateStockBinDetailTableStockBinId(stockHeaderResponse.getStockHeaderList(), list, userCode);
				//System.out.println("at serviceImpl stockDtlres " + stockDtlres);
				if(stockDtlres.getStatusCode()==200)
				{
					statusOp.setStatusCode(200);
					statusOp.setTotalRecords(totalRecords);
					statusOp.setMessage(stockDtlres.getMessage());
				}
				else
				{
					statusOp.setStatusCode(200);
					statusOp.setMessage(stockDtlres.getMessage());
				}
			}
			else
			{
				statusOp.setStatusCode(304);
				statusOp.setMessage(stockHeaderResponse.getMessage());
				
			}
			
			statusOp.setStatusCode(200);
			statusOp.setMessage(res.getMessage());

		}
		else
		{
			statusOp.setStatusCode(304);
			statusOp.setMessage("part is already available  for this branch ");
		}

		
		}catch(Exception e)
		{
			statusOp.setStatusCode(302);
			statusOp.setMessage("Something went wrong while saving data");

		}
		

		return statusOp;
	}

}
