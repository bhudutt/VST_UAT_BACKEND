package com.hitech.dms.web.dao.enquiry.productTrial.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.productTrial.search.request.ProductTrialSearchRequestModel;
import com.hitech.dms.web.model.productTrial.search.response.ProductTrialSearchMainResponseModel;
import com.hitech.dms.web.model.productTrial.search.response.ProductTrialSearchResponseModel;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class ProductTrialSearchDaoImpl implements ProductTrialSearchDao{
	private static final Logger logger = LoggerFactory.getLogger(ProductTrialSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	@Override
	public ProductTrialSearchMainResponseModel fetchProductTrialSearchList(String userCode, ProductTrialSearchRequestModel requestModel) {
			NativeQuery<?> query = null;
			Session session = null;
			ProductTrialSearchResponseModel responseModel = null;
			List<ProductTrialSearchResponseModel> responseModelList = null;
			ProductTrialSearchMainResponseModel mainSearch = null;
			Integer recordCount = 0;
			String sqlQuery = "exec [SP_ENQ_PRODUCTTRAIL_SEARCH] :userCode, :pcId, :orgHierId, :dealerId, :branchId, :productTrailNo,:enquiryNo, :fromDate, :toDate , :includeInactive, :page, :size";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("pcId", requestModel.getPcId());
				query.setParameter("orgHierId", requestModel.getOrgHierId());
				query.setParameter("dealerId", requestModel.getDealerId());
				query.setParameter("branchId", requestModel.getBranchId());
				query.setParameter("productTrailNo", requestModel.getProductTrialNo());
				query.setParameter("enquiryNo", requestModel.getEnquiryNo());
				query.setParameter("fromDate", requestModel.getFromDate());
				query.setParameter("toDate", requestModel.getToDate());
				query.setParameter("includeInactive", 'N');
				query.setParameter("page", requestModel.getPage());
				query.setParameter("size", requestModel.getSize());
				
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<?> data = query.list();
				if (data != null && !data.isEmpty()) {
					responseModelList = new ArrayList<ProductTrialSearchResponseModel>();
					mainSearch = new ProductTrialSearchMainResponseModel();
					for (Object object : data) {
						Map<?, ?> row = (Map<?, ?>) object;
						responseModel = new ProductTrialSearchResponseModel();
						responseModel.setId((BigInteger) row.get("id"));
						responseModel.setProductTrailNo((String) row.get("Product_Trial_No"));
						responseModel.setProductTrailDate((String) row.get("Product_Trial_Date"));
						responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
						responseModel.setEnquiryNumber((String) row.get("enquiry_number"));
						responseModel.setTrailGivenBy((String) row.get("TrailGivenBy"));
						responseModel.setOverallRating( (BigDecimal) row.get("Overall_Rating"));
						responseModel.setChassisNo((String) row.get("Chassis_No"));
						responseModel.setTempRegNo((String) row.get("Temp_Reg_No"));
						responseModel.setStartTime((String) row.get("Start_time"));
						responseModel.setEndTime((String) row.get("End_Time"));
						responseModel.setStartKm((String) row.get("Start_Km"));
						responseModel.setEndKm((String) row.get("End_Km"));
						responseModel.setRemarks((String) row.get("Remarks"));
						responseModel.setBranchId((BigInteger) row.get("BRANCH_ID"));
						if (recordCount.compareTo(0) == 0) {
							recordCount = (Integer) row.get("totalRecords");
						}
						responseModelList.add(responseModel);
						}
					mainSearch.setProductTrailSearch(responseModelList);
					mainSearch.setRecordCount(recordCount);
				}
			} catch (SQLGrammarException ex) {
				logger.error(this.getClass().getName(), ex);
			} catch (HibernateException ex) {
				logger.error(this.getClass().getName(), ex);
			} catch (Exception ex) {
				logger.error(this.getClass().getName(), ex);
			} finally {
				if (session.isOpen())
					session.close();
			}
			return mainSearch;
		}
	

}
