package com.hitech.dms.web.dao.enquiry.digitalUpload;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.digitalUpload.DigitalUploadDtlEntity;
import com.hitech.dms.web.entity.digitalUpload.DigitalUploadHdrEntity;
import com.hitech.dms.web.model.enquiry.digitalReport.response.DigitalSourceResponseModel;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class DigitalUploadDaoImpl implements DigitalUploadDao {

	private static final Logger logger = LoggerFactory.getLogger(DigitalUploadDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;


	@SuppressWarnings("deprecation")
	@Override
	public Map<String, StringBuffer> uploadExcel(String userCode, BigInteger digitalPlatform, BigInteger profitCenter,
			Device device, MultipartFile file) throws IOException {
		InputStream in = file.getInputStream();
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		Date currDate = new Date();
		List<DigitalUploadExcelDto> uploadeData = Poiji.fromExcel(in, PoijiExcelType.XLSX, DigitalUploadExcelDto.class,
				PoijiOptions.PoijiOptionsBuilder.settings().headerStart(0).build());

		DigitalUploadHdrEntity hdr = new DigitalUploadHdrEntity();
		Map<String, StringBuffer> map = new HashMap<>();
		StringBuffer errors = new StringBuffer("");
		List<DigitalUploadDtlEntity> dtl = new ArrayList<>();
		uploadeData.forEach(p -> {
			if (p.getCustomerName() !=null && p.getMobileNo() !=null && p.getEmailId() !=null && p.getModel()!=null && p.getDistrict()!=null && p.getTehsil()!=null && p.getState()!=null) {
				if (isValidMobileNo(p.getMobileNo())) {
					if (isValidEmail(p.getEmailId())) {
						DigitalUploadDtlEntity obj = new DigitalUploadDtlEntity();
						obj.setCustomerName(p.getCustomerName());
						obj.setCustomerMobileNo(p.getMobileNo());
						obj.setCustomerEmailI(p.getEmailId());
						obj.setModel(p.getModel());
						obj.setSegment(p.getSegment());
						obj.setCustomerDistrict(p.getDistrict());
						obj.setCustomerTehsil(p.getTehsil());
						obj.setCustomerState(p.getState());
						obj.setDigitalEnqHdr(hdr);
						obj.setStatus("OPEN");
						dtl.add(obj);
					}else {
						errors.append(p.getEmailId()+" , ");
					}
				}
				else {
					errors.append(p.getMobileNo()+" , ");
				}
			}else {
				errors.append("Value/Values");
			}
			
			map.put("error", errors);
			
			
			
		});

		hdr.setDigitalUploadDtl(dtl);

		try {
			session = sessionFactory.openSession();
			if (!dtl.isEmpty() && map.get("error").toString().equals("")) {
				BigInteger userId = null;
				mapData = fetchUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					userId = (BigInteger) mapData.get("userId");
				}
				hdr.setDigitalEnqNo("DRA");
				hdr.setDigitalSourceId(digitalPlatform);
				hdr.setPcId(profitCenter);
				hdr.setCreatedBy(userId);
				hdr.setCreatedDate(currDate);
				transaction = session.beginTransaction();
				BigInteger digitalEnqHdrId = (BigInteger) session.save(hdr);
				transaction.commit();
				Query<?> query = null;
				String sqlQuery = "exec [SP_ENQ_upload_Digital_Enq] :digitalEnqHdrId";
				if (digitalEnqHdrId != null) {
					try {
						query = session.createNativeQuery(sqlQuery);
						query.setParameter("digitalEnqHdrId", digitalEnqHdrId);
						query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
						List<?> data = query.list();
						if (data != null && !data.isEmpty()) {
							char successFlag;
							for (Object object : data) {
								@SuppressWarnings("rawtypes")
								Map row = (Map) object;
								successFlag = (char) row.get("successFlag");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
			if (transaction.isActive())
				transaction.rollback();
		} finally {
			if (session.isOpen())
				session.close();
			in.close();
		}

		return map;
	}

	public List<DigitalSourceResponseModel> getDigitalSource(String userCode) {
		List<DigitalSourceResponseModel> digitalSourceData = fetchDigitalSource(userCode);
		return digitalSourceData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public List<DigitalSourceResponseModel> fetchDigitalSource(String userCode) {
		Session session = null;
		List<DigitalSourceResponseModel> responseModelList = null;
		DigitalSourceResponseModel responseModel = null;
		Query<DigitalSourceResponseModel> query = null;
		String sqlQuery = "exec [SP_ENQ_Get_digital_Source] :userCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode",userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DigitalSourceResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DigitalSourceResponseModel();
					responseModel.setDigital_Source_id(((Integer) row.get("digital_Source_id")));
					responseModel.setDigitalSourceName(((String) row.get("DigitalSourceName")));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
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
			ex.printStackTrace();
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			ex.printStackTrace();
		} finally {
		}
		return mapData;
	}
	
	   public static boolean isValidMobileNo(String mobileNo){
		 	boolean flag = false;
	        Pattern mobelPattern = Pattern.compile("^\\d{10}$");
	        Matcher m = mobelPattern.matcher(mobileNo);
	        if (m.matches()) {
				flag = true;
			}
	        return (flag);
	    }
	   
	   public static boolean isValidEmail(String email ){
		 	boolean flag = false;
	        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$");
	        Matcher e = emailPattern.matcher(email);
	        if (e.matches()) {
				flag = true;
			}
	        return (flag);
	    }

}
