/**
 * 
 */
package com.hitech.dms.web.dao.geo;

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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.geo.request.GeoCityDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoDistricDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoPinDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoStateDTLRequestModel;
import com.hitech.dms.web.model.geo.request.GeoTehsilDTLRequestModel;
import com.hitech.dms.web.model.geo.response.GeoCityDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoCountryDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoDistricDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoPinDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoStateDTLResponseModel;
import com.hitech.dms.web.model.geo.response.GeoTehsilDTLResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class GeoDTLDaoImpl implements GeoDTLDao {
	private static final Logger logger = LoggerFactory.getLogger(GeoDTLDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public List<GeoDistricDTLResponseModel> fetchDistrictListByDealerID(String userCode, Long dealerID,
			BigInteger stateId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDistrictListByDealerID invoked.." + userCode);
		}
		Session session = null;
		List<GeoDistricDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchDistrictListByDealerID(session, userCode, dealerID, stateId);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public List<GeoDistricDTLResponseModel>fetchListDistrictListByDealerID(String userCode,
			BigInteger stateId){
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchListDistrictListByDealerID invoked.." + userCode);
		}
		Session session = null;
		List<GeoDistricDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchListDistrictListByDealerID(session, userCode, stateId);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	//Begin
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoDistricDTLResponseModel> fetchListDistrictListByDealerID(Session session, String userCode,
			BigInteger stateId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDistrictListByDealerID invoked.." + userCode + " ");
		}
		Query query = null;
		List<GeoDistricDTLResponseModel> responseModelList = null;
		String sqlQuery = "select district_id,districtcode, districtdesc from CM_GEO_DIST";
		try {
			query = session.createSQLQuery(sqlQuery);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoDistricDTLResponseModel>();
				GeoDistricDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoDistricDTLResponseModel();
					responseModel.setDistrictID((BigInteger) row.get("district_id"));
					responseModel.setDistrictCode((String) row.get("districtcode"));
					responseModel.setDistricDesc((String) row.get("districtdesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	//END
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoDistricDTLResponseModel> fetchDistrictListByDealerID(Session session, String userCode, Long dealerID,
			BigInteger stateId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDistrictListByDealerID invoked.." + userCode + " " + dealerID);
		}
		Query query = null;
		List<GeoDistricDTLResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GEO_District_details] :userCode, :dealerID, :stateId";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerID", dealerID);
			query.setParameter("stateId", stateId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoDistricDTLResponseModel>();
				GeoDistricDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoDistricDTLResponseModel();
					responseModel.setDistrictID((BigInteger) row.get("district_id"));
					responseModel.setDistrictCode((String) row.get("districtcode"));
					responseModel.setDistricDesc((String) row.get("districtdesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	public List<GeoDistricDTLResponseModel> fetchDistrictListByDealerID(String userCode,
			GeoDistricDTLRequestModel geoDistricDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDistrictListByDealerID invoked.." + geoDistricDTLRequestModel.toString());
		}
		Session session = null;
		List<GeoDistricDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchDistrictListByDealerID(session, userCode, geoDistricDTLRequestModel.getDealerID(),
					geoDistricDTLRequestModel.getStateId());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public List<GeoCityDTLResponseModel> fetchCityListByTehsilID(String userCode, Long tehsilID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCityListByTehsilID invoked.." + tehsilID);
		}
		Session session = null;
		List<GeoCityDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchCityListByTehsilID(session, userCode, tehsilID);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoCityDTLResponseModel> fetchCityListByTehsilID(Session session, String userCode, Long tehsilID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCityListByTehsilID invoked.." + userCode + " " + tehsilID);
		}
		Query query = null;
		List<GeoCityDTLResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GEO_City_Details] :tehsilID";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("tehsilID", tehsilID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoCityDTLResponseModel>();
				GeoCityDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoCityDTLResponseModel();
					responseModel.setCityID((BigInteger) row.get("city_id"));
					responseModel.setCityCode((String) row.get("CityCode"));
					responseModel.setCityDesc((String) row.get("CityDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	public List<GeoCityDTLResponseModel> fetchCityListByTehsilID(String userCode,
			GeoCityDTLRequestModel geoCityDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCityListByTehsilID invoked.." + geoCityDTLRequestModel.toString());
		}
		Session session = null;
		List<GeoCityDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchCityListByTehsilID(session, userCode, geoCityDTLRequestModel.getTehsilID());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public List<GeoPinDTLResponseModel> fetchPinListByCityID(String userCode, Long cityID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPinListByCityID invoked.." + cityID);
		}
		Session session = null;
		List<GeoPinDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchPinListByCityID(session, userCode, cityID);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoPinDTLResponseModel> fetchPinListByCityID(Session session, String userCode, Long cityID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPinListByCityID invoked.." + userCode + " " + cityID);
		}
		Query query = null;
		List<GeoPinDTLResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GEO_Pin_Details] :cityID";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("cityID", cityID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoPinDTLResponseModel>();
				GeoPinDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoPinDTLResponseModel();
					responseModel.setPinID((BigInteger) row.get("pin_id"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setLocalityCode((String) row.get("localityCode"));
					responseModel.setLocalityName((String) row.get("localityName"));
					responseModel.setDisplayName((String) row.get("displayname"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	public List<GeoPinDTLResponseModel> fetchPinListByCityID(String userCode,
			GeoPinDTLRequestModel geoPinDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPinListByCityID invoked.." + geoPinDTLRequestModel.toString());
		}
		Session session = null;
		List<GeoPinDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchPinListByCityID(session, userCode, geoPinDTLRequestModel.getCityID());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public List<GeoCountryDTLResponseModel> fetchCountryList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCountryList invoked..");
		}
		Session session = null;
		List<GeoCountryDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchCountryList(session, userCode);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoCountryDTLResponseModel> fetchCountryList(Session session, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCountryList invoked.." + userCode);
		}
		Query query = null;
		List<GeoCountryDTLResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GEO_Country_details] ";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoCountryDTLResponseModel>();
				GeoCountryDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoCountryDTLResponseModel();
					responseModel.setCountryID((BigInteger) row.get("country_id"));
					responseModel.setCountryCode((String) row.get("CountryCode"));
					responseModel.setCountryDesc((String) row.get("countryDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	public List<GeoDTLResponseModel> fetchGeoDTL(String userCode, Long pinID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGeoDTL invoked.." + pinID);
		}
		Session session = null;
		List<GeoDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchGeoDTL(session, userCode, pinID);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoDTLResponseModel> fetchGeoDTL(Session session, String userCode, Long pinID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGeoDTL invoked.." + userCode + " " + pinID);
		}
		Query query = null;
		List<GeoDTLResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GEO_Details] :pinID";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pinID", pinID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoDTLResponseModel>();
				GeoDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoDTLResponseModel();
					responseModel.setPinID((BigInteger) row.get("pin_id"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setLocalityCode((String) row.get("localityCode"));
					responseModel.setLocalityName((String) row.get("localityName"));
					responseModel.setDisplayName((String) row.get("displayname"));
					responseModel.setCityID((BigInteger) row.get("city_id"));
					responseModel.setCityDesc((String) row.get("cityDesc"));
					responseModel.setTehsilID((BigInteger) row.get("tehsil_id"));
					responseModel.setTehsilDesc((String) row.get("tehsilDesc"));
					responseModel.setDistrictID((BigInteger) row.get("district_id"));
					responseModel.setDistricDesc((String) row.get("districtDesc"));
					responseModel.setStateDesc((String) row.get("stateDesc"));
					responseModel.setStateID((BigInteger) row.get("state_id"));
					responseModel.setCountryID((BigInteger) row.get("country_id"));
					responseModel.setCountryDesc((String) row.get("countryDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	public List<GeoDTLResponseModel> fetchGeoDTL(String userCode, GeoDTLRequestModel geoDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGeoDTL invoked.." + geoDTLRequestModel.toString());
		}
		Session session = null;
		List<GeoDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchGeoDTL(session, userCode, geoDTLRequestModel.getPinID());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public List<GeoStateDTLResponseModel> fetchStateListByCountryID(String userCode, Long countryID, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchStateListByCountryID invoked.." + countryID);
		}
		Session session = null;
		List<GeoStateDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchStateListByCountryID(session, userCode, countryID, isFor);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoStateDTLResponseModel> fetchStateListByCountryID(Session session, String userCode, Long countryID, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchStateListByCountryID invoked.." + userCode + " " + countryID);
		}
		Query query = null;
		List<GeoStateDTLResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GEO_State_details] :userCode, :countryID, :isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("countryID", countryID);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoStateDTLResponseModel>();
				GeoStateDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoStateDTLResponseModel();
					responseModel.setStateID((BigInteger) row.get("state_id"));
					responseModel.setStateCode((String) row.get("StateCode"));
					responseModel.setStateDesc((String) row.get("stateDesc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	public List<GeoStateDTLResponseModel> fetchStateListByCountryID(String userCode,
			GeoStateDTLRequestModel geoStateDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchStateListByCountryID invoked.." + geoStateDTLRequestModel.toString());
		}
		Session session = null;
		List<GeoStateDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchStateListByCountryID(session, userCode, geoStateDTLRequestModel.getCountryID(), geoStateDTLRequestModel.getIsFor());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	public GeoStateDTLResponseModel fetchStateDtlByStateID(String userCode, Long stateId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchStateDtlByStateID invoked.." + stateId);
		}
		Session session = null;
		GeoStateDTLResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchStateDtlByStateID(session, userCode, stateId);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	public GeoStateDTLResponseModel fetchStateDtlByStateID(String userCode,
			GeoStateDTLRequestModel geoStateDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchStateDtlByStateID invoked.." + geoStateDTLRequestModel.toString());
		}
		Session session = null;
		GeoStateDTLResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = fetchStateDtlByStateID(session, userCode, geoStateDTLRequestModel.getStateId());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public GeoStateDTLResponseModel fetchStateDtlByStateID(Session session, String userCode, Long stateId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchStateDtlByStateID invoked.." + userCode + " " + stateId);
		}
		Query query = null;
		GeoStateDTLResponseModel responseModel = null;
		String sqlQuery = "Select * from CM_GEO_STATE (nolock) where state_id=:stateId ";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("stateId", stateId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoStateDTLResponseModel();
					responseModel.setStateID((BigInteger) row.get("state_id"));
					responseModel.setStateCode((String) row.get("StateCode"));
					responseModel.setStateDesc((String) row.get("stateDesc"));
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModel;
	}

	public List<GeoTehsilDTLResponseModel> fetchTehsilListByDistrictID(String userCode, Long districtID, Long dealerID,
			String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchTehsilListByDistrictID invoked.." + districtID);
		}
		Session session = null;
		List<GeoTehsilDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchTehsilListByDistrictID(session, userCode, districtID, dealerID, isFor);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoTehsilDTLResponseModel> fetchTehsilListByDistrictID(Session session, String userCode,
			Long districtID, Long dealerID, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchTehsilListByDistrictID invoked.." + userCode + " " + districtID + " " + dealerID + " : "
					+ isFor);
		}
		Query query = null;
		List<GeoTehsilDTLResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GEO_Tehsil_Details] :districtID, :userCode, :dealerID, :isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("districtID", districtID);
			query.setParameter("dealerID", dealerID);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoTehsilDTLResponseModel>();
				GeoTehsilDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoTehsilDTLResponseModel();
					responseModel.setTehsilID((BigInteger) row.get("tehsil_id"));
					responseModel.setTehsilCode((String) row.get("TehsilCode"));
					responseModel.setTehsilDesc((String) row.get("TehsilDesc"));
					responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}
// New API
	public List<GeoTehsilDTLResponseModel> fetchListTehsilListByDistrictID(String userCode,
			GeoTehsilDTLRequestModel geoTehsilDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchListTehsilListByDistrictID invoked.." + geoTehsilDTLRequestModel.toString());
		}
		Session session = null;
		List<GeoTehsilDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchListTehsilListByDistrictID(session, userCode, geoTehsilDTLRequestModel.getDistrictID());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<GeoTehsilDTLResponseModel> fetchListTehsilListByDistrictID(Session session, String userCode,
			Long districtID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchListTehsilListByDistrictID invoked.." + userCode + " " + districtID + " ");
		}
		Query query = null;
		List<GeoTehsilDTLResponseModel> responseModelList = null;
		//Select * from CM_GEO_STATE (nolock) where state_id=:stateId
		String sqlQuery = "exec [SP_CM_GEO_District_details_By_DistrictId] :districtID";
		try {
			query = session.createSQLQuery(sqlQuery);
			
			query.setParameter("districtID", districtID);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GeoTehsilDTLResponseModel>();
				GeoTehsilDTLResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GeoTehsilDTLResponseModel();
					responseModel.setTehsilID((BigInteger) row.get("tehsil_id"));
					responseModel.setTehsilCode((String) row.get("TehsilCode"));
					responseModel.setTehsilDesc((String) row.get("TehsilDesc"));
					//responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}
	
//END	
	public List<GeoTehsilDTLResponseModel> fetchTehsilListByDistrictID(String userCode,
			GeoTehsilDTLRequestModel geoTehsilDTLRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchTehsilListByDistrictID invoked.." + geoTehsilDTLRequestModel.toString());
		}
		Session session = null;
		List<GeoTehsilDTLResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchTehsilListByDistrictID(session, userCode, geoTehsilDTLRequestModel.getDistrictID(),
					geoTehsilDTLRequestModel.getDealerID(), geoTehsilDTLRequestModel.getIsFor());
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
}
