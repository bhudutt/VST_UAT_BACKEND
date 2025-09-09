/**
 * 
 */
package com.hitech.dms.web.dao.productdivision.list;

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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.productdivision.request.ProductDivisionListRequestModel;
import com.hitech.dms.web.model.productdivision.response.ProductDivisionListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ProductDivisionListDaoImpl implements ProductDivisionListDao {
	private static final Logger logger = LoggerFactory.getLogger(ProductDivisionListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ProductDivisionListResponseModel> fetchPcForBranchDealerList(String userCode,
			ProductDivisionListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPcForBranchDealerList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<ProductDivisionListResponseModel> responseList = null;
		String sqlQuery = "exec [SP_CM_getPrdDivision_wrt_dlr_pc] :userCode, :dlrId, :pcId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dlrId", requestModel.getDealerId());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ProductDivisionListResponseModel>();
				ProductDivisionListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ProductDivisionListResponseModel();
					responseModel.setProductDivisionId((Integer) row.get("ProductDivisionId"));
					responseModel.setProductDivisionCode((String) row.get("ProductDivisionCode"));
					responseModel.setProductDivisionDesc((String) row.get("ProductDivisionDesc"));
					responseList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}
}
