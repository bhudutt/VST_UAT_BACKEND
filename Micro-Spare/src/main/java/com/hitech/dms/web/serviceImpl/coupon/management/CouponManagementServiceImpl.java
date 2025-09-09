package com.hitech.dms.web.serviceImpl.coupon.management;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.coupon.management.CouponManagementDao;
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
import com.hitech.dms.web.service.coupon.management.CouponManagementService;

import net.sf.jasperreports.engine.JasperPrint;

@Service
public class CouponManagementServiceImpl  implements CouponManagementService{

	@Autowired
	CouponManagementDao dao;
	
	@Override
	public CouponDetailSaveResponse saveCouponDetail(CouponDetailSaveResponse couponDetail, String userCode) {
		return dao.saveCouponDetails(couponDetail,userCode);
	}

	@Override
	public CouponSearchResponse fetchCouponDetail(CouponDetailSearchRequest request, String userCode) {
		return dao.ftechCouponDetils(request, userCode);
	}

	@Override
	public List<String> searchDocumentNo(String searchType, String searchText, String userCode) {
		System.out.println("at here dao");
		return dao.searchDocumentNo(searchType,searchText, userCode);
	}

	@Override
	public CouponDetailResponse getCouponDetailByDocNo(String dealerCode, String documentNo, String userCode) {
		return dao.getCouponDetail(dealerCode, documentNo, userCode);
	}

	@Override
	public UpdateCouponResponse updateCouponApproval(List<UpdateCoupanRequest> updateList,String documentNo,BigInteger approvedAmount, String userCode) {
		return dao.updateApprovalRequest(updateList,documentNo,approvedAmount,userCode);
	}

	@Override
	public CouponSearchResponse searchCouponByDate(CouponSearchRequest request,String userCode) {
		
		
		return dao.getCouponDetailByDate(request,userCode);
	}

	@Override
	public ApprovalDetailResponse getApprovalDetail(String userCode) {
		
		return dao.getApprovalDetail(userCode);
	}

	@Override
	public JasperPrint PdfGeneratorReportForJobCardOpen(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		return dao.PdfGeneratorReportForJobCardOpenDao(request,jasperName,jasperParameter,filePath);
	}

	@Override
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) {
		
		   try {
			dao.printReport(jasperPrint, format, printStatus, outputStream, reportName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String string,
			HashMap<String, Object> jasperParameter, String filePath) {
		
		return dao.ExcelGeneratorReportdao(request,string,
				 jasperParameter,  filePath);
	}

	@Override
	public void printReport1(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) {
		
		
		try
		{
			dao.printReport1(jasperPrint, format, printStatus, outputStream, reportName);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

	

	
	
	
}
