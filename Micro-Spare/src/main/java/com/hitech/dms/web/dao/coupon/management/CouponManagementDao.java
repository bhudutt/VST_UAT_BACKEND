package com.hitech.dms.web.dao.coupon.management;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.web.model.coupon.management.ApprovalDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSaveResponse;
import com.hitech.dms.web.model.coupon.management.CouponDetailSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponSearchRequest;
import com.hitech.dms.web.model.coupon.management.CouponSearchResponse;
import com.hitech.dms.web.model.coupon.management.DocumentNoModel;
import com.hitech.dms.web.model.coupon.management.UpdateCoupanRequest;
import com.hitech.dms.web.model.coupon.management.UpdateCouponResponse;

import net.sf.jasperreports.engine.JasperPrint;

public interface CouponManagementDao {
	
	
	public CouponDetailSaveResponse saveCouponDetails(CouponDetailSaveResponse couponDetail,String userCode);

	public CouponSearchResponse ftechCouponDetils(CouponDetailSearchRequest request,String userCode);
	
	public List<String> searchDocumentNo(String searchType,String searchText ,String userCode);
	public CouponDetailResponse getCouponDetail(String dealerCode,String documentNo,String userCode);
	public UpdateCouponResponse updateApprovalRequest(List<UpdateCoupanRequest> updateList,String documentNo,BigInteger approvedAmount,String userCode);
	
	public  CouponSearchResponse getCouponDetailByDate(CouponSearchRequest request,String userCode);
	
	public ApprovalDetailResponse getApprovalDetail(String userCode);
	
	public JasperPrint PdfGeneratorReportForJobCardOpenDao(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath);

	void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception;

	public void printReport1(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName);

	public JasperPrint ExcelGeneratorReportdao(HttpServletRequest request, String string,
			HashMap<String, Object> jasperParameter, String filePath);
	
	
	



}
