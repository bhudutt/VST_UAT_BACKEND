package com.hitech.dms.web.dao.geo;

import java.math.BigInteger;
import java.util.List;

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

public interface GeoDTLDao {
	public List<GeoDistricDTLResponseModel> fetchDistrictListByDealerID(String userCode, Long dealerID, BigInteger stateId);
	
	public List<GeoDistricDTLResponseModel> fetchListDistrictListByDealerID(String userCode, BigInteger stateId);
	
	public List<GeoDistricDTLResponseModel> fetchDistrictListByDealerID(String userCode,
			GeoDistricDTLRequestModel geoDistricDTLRequestModel);

	public List<GeoCityDTLResponseModel> fetchCityListByTehsilID(String userCode, Long tehsilID);

	public List<GeoCityDTLResponseModel> fetchCityListByTehsilID(String userCode,
			GeoCityDTLRequestModel geoCityDTLRequestModel);

	public List<GeoPinDTLResponseModel> fetchPinListByCityID(String userCode, Long cityID);

	public List<GeoPinDTLResponseModel> fetchPinListByCityID(String userCode,
			GeoPinDTLRequestModel geoPinDTLRequestModel);

	public List<GeoCountryDTLResponseModel> fetchCountryList(String userCode);

	public List<GeoDTLResponseModel> fetchGeoDTL(String userCode, Long pinID);

	public List<GeoDTLResponseModel> fetchGeoDTL(String userCode, GeoDTLRequestModel geoDTLRequestModel);

	public List<GeoStateDTLResponseModel> fetchStateListByCountryID(String userCode, Long countryID, String isFor);

	public List<GeoStateDTLResponseModel> fetchStateListByCountryID(String userCode,
			GeoStateDTLRequestModel geoStateDTLRequestModel);
	
	public GeoStateDTLResponseModel fetchStateDtlByStateID(String userCode, Long stateId);
	
	public GeoStateDTLResponseModel fetchStateDtlByStateID(String userCode,
			GeoStateDTLRequestModel geoStateDTLRequestModel);

	public List<GeoTehsilDTLResponseModel> fetchTehsilListByDistrictID(String userCode,
			Long districtID, Long dealerID, String isFor);
	
	//New API
	public List<GeoTehsilDTLResponseModel> fetchListTehsilListByDistrictID(String userCode,
			GeoTehsilDTLRequestModel geoTehsilDTLRequestModel);
	//end

	public List<GeoTehsilDTLResponseModel> fetchTehsilListByDistrictID(String userCode,
			GeoTehsilDTLRequestModel geoTehsilDTLRequestModel);
}
