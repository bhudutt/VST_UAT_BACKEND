package com.hitech.dms.web.controller.outletCategory.search.daoImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.controller.outletCategory.search.dao.OutCategorySearchDao;
import com.hitech.dms.web.model.outletCategory.search.response.OutletCategoryPartsModel;
import com.hitech.dms.web.model.outletCategory.search.response.OutletCategorySearchResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyCategoryResponse;

@Repository
public class OutletCategorySearchDaoImpl implements OutCategorySearchDao {

	private static final Logger logger = LoggerFactory.getLogger(OutletCategorySearchDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public OutletCategorySearchResponse getOutLetCategorySearch(String userCode, String categoryId) {

		Integer category = Integer.parseInt(categoryId);
		logger.info("OutletCategorySearchDaoImpl  is invoked ...... " + userCode, " " + categoryId);

		Session session = null;
		Query query = null;
		OutletCategorySearchResponse responseModel = new OutletCategorySearchResponse();
		OutletCategoryPartsModel model = null;
		List<OutletCategoryPartsModel> outletCategoryList = null;

		try {
			session = sessionFactory.openSession();
			String sqlQuery = "select * from PA_PO_CATEGORY where IsActive=:active ";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("active", 'Y');
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				outletCategoryList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model = new OutletCategoryPartsModel();

					Integer poCategoryId = (Integer) row.get("PO_Category_Id");
					String poCategoryCode = (String) row.get("PO_Category_Code");
					String poCategoryDesc = (String) row.get("PO_Category_Desc");
					if (category == 4) {

						if (poCategoryCode.equalsIgnoreCase("Spare Parts") || poCategoryCode.equalsIgnoreCase("Lubricants")) {
							model.setPoCategoryId(poCategoryId);
							model.setPoCategoryCode(poCategoryCode);
							model.setPoCategoryDesc(poCategoryDesc);
							outletCategoryList.add(model);

						}

					} else if (category == 5) {

						if (poCategoryCode.equalsIgnoreCase("Spare Parts") || poCategoryCode.equalsIgnoreCase("Lubricants")
								|| poCategoryCode.equalsIgnoreCase("Engines")
								|| poCategoryCode.equalsIgnoreCase("Attachments")
								|| poCategoryCode.equalsIgnoreCase("Electrical Pumps")){
								model.setPoCategoryId(poCategoryId);
								model.setPoCategoryCode(poCategoryCode);
								model.setPoCategoryDesc(poCategoryDesc);
								outletCategoryList.add(model);

						}

					} else if (category == 6) {

						if (poCategoryCode.equalsIgnoreCase("Electrical Pumps")) {
							model.setPoCategoryId(poCategoryId);
							model.setPoCategoryCode(poCategoryCode);
							model.setPoCategoryDesc(poCategoryDesc);
							outletCategoryList.add(model);

						}

					}

				}

			}
			System.out.println("before send response " + outletCategoryList);
			responseModel.setStatusCode(200);
			responseModel.setStatusMessage("Fetched Success");
			responseModel.setOutletCategoryModel(outletCategoryList);

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
		return responseModel;
	}
	
	@Override
	public List<OutletCategoryPartsModel> getOutLetCategoryList(String userCode, String categoryId,String partyCode) {

		Integer category = Integer.parseInt(categoryId);
		logger.info("OutletCategorySearchDaoImpl  is invoked ...... " + userCode, " " + categoryId);

		Session session = null;
		Query query = null;
		OutletCategorySearchResponse responseModel = new OutletCategorySearchResponse();
		OutletCategoryPartsModel model = null;
		List<OutletCategoryPartsModel> outletCategoryList = new ArrayList<>();

		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [PA_PARTY_DETAIL_OUTLETCATEGORY]  :partyCode";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("partyCode",partyCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				outletCategoryList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					model = new OutletCategoryPartsModel();

							Integer poCategoryId = (Integer) row.get("po_category_id");
							String poCategoryCode = (String) row.get("PO_Category_Code");
							String poCategoryDesc = (String) row.get("PO_Category_Desc");
					
							model.setPoCategoryId(poCategoryId);
							model.setPoCategoryCode(poCategoryCode);
							model.setPoCategoryDesc(poCategoryDesc);
							outletCategoryList.add(model);
						

					}

				

			}
			System.out.println("before send response " + outletCategoryList);
			responseModel.setStatusCode(200);
			responseModel.setStatusMessage("Fetched Success");
			responseModel.setOutletCategoryModel(outletCategoryList);

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
		return outletCategoryList;
	}


}
