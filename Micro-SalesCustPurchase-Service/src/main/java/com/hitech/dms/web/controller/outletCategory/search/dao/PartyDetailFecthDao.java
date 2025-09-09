package com.hitech.dms.web.controller.outletCategory.search.dao;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hitech.dms.web.entity.partycode.PartyCodeEditResponse;
import com.hitech.dms.web.model.partybybranch.create.request.PanGstSearchRequest;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeUpdateRequest;
import com.hitech.dms.web.model.partycode.search.response.PartyDetailFetchResponse;

import net.sf.jasperreports.engine.JasperPrint;

public interface PartyDetailFecthDao {

	public PartyCodeEditResponse updatePartyCode(PartyCodeUpdateRequest requestModel,String UserCode);
	public PartyDetailFetchResponse fetchPartyDetalByPartyBranchId(Integer partyBranchId, Integer branchId, String dealerCode);
	public List<PanGstSearchRequest> checkExistPanGst(PanGstSearchRequest request,String userCode);
	public JasperPrint ExcelGeneratorReportdao(HttpServletRequest request, String string,
			HashMap<String, Object> jasperParameter, String filePath);
	
	public void printReport1(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName);

}


