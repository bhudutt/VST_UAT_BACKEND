package com.hitech.dms.web.service.coupon.management;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.web.model.coupon.management.ApprovalDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSaveResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponDetailsEntity;
import com.hitech.dms.web.model.coupon.management.CouponSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponSearchResponse;
import com.hitech.dms.web.model.coupon.management.DocumentNoModel;
import com.hitech.dms.web.model.coupon.management.UpdateCoupanRequest;
import com.hitech.dms.web.model.coupon.management.UpdateCouponResponse;

import net.sf.jasperreports.engine.JasperPrint;

public interface CouponManagementService {

	
	
    public CouponDetailSaveResponse saveCouponDetail(CouponDetailSaveResponse couponDetail,String userCode);
    
    public CouponSearchResponse fetchCouponDetail(CouponDetailSearchRequest request,String userCode);
    public List<String> searchDocumentNo(String searchType,String searchText,String userCode);
    
    public 	CouponSearchResponse searchCouponByDate(CouponSearchRequest request,String userCode);
    JasperPrint PdfGeneratorReportForJobCardOpen(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath);

    
    public CouponDetailResponse getCouponDetailByDocNo(String dealerCode,String documentNo,String userCode);
    
    public UpdateCouponResponse updateCouponApproval(List<UpdateCoupanRequest> updateList,String documentNo,BigInteger approvedAmount,String userCode);
    
    public ApprovalDetailResponse  getApprovalDetail(String userCode);

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName);

	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String string,
			HashMap<String, Object> jasperParameter, String filePath);

	public void printReport1(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName);
}
