package com.hitech.dms.web.dao.enquiry.digitalUpload;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.hibernate.Session;
import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.digitalReport.request.DigitalEnquiReportModel;

/**
 * @author vinay.gautam
 *
 */
public interface DigitalUploadDao {
    String[] PreDefinedColumns = new String[]{"CUSTOMER NAME","MOBILE NO.","EMAIL ID","MODEL","DISTRICT","TEHSIL/TALUKA/MANDAL","STATE"};
    
    Map<String, StringBuffer> uploadExcel(String userCode, BigInteger digitalPlatform,BigInteger profitCenter,Device device, MultipartFile file) throws IOException;
    

    
}
