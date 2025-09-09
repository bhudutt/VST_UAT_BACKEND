package com.hitech.dms.web.service.goodsInTransitReportImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.goodsInTransitReport.GoodsInTransitReportDao;
import com.hitech.dms.web.model.goodsInTransitReport.request.GoodsInTransitReportRequest;
import com.hitech.dms.web.model.goodsInTransitReport.response.GoodsInTransitReportRepList;
import com.hitech.dms.web.model.goodsInTransitReport.response.GoodsInTransitReportResponse;
import com.hitech.dms.web.model.goodsInTransitReport.response.ModelItemList;
import com.hitech.dms.web.service.goodsInTransitReport.GoodsInTransitReportService;

import brave.Response;

@Service
public class GoodsTransitReportServiceImpl implements GoodsInTransitReportService  
{
	private static final Logger logger = LoggerFactory.getLogger(GoodsTransitReportServiceImpl.class);
	@Autowired
	private GoodsInTransitReportDao goodsInTransitReportDao; 
	
	@Override
	public GoodsInTransitReportRepList goodsInTransitSearch(String userCode, GoodsInTransitReportRequest resquest,Device device) {
		
		GoodsInTransitReportResponse bean = null;
		List<GoodsInTransitReportResponse> res = new LinkedList<GoodsInTransitReportResponse>();
		GoodsInTransitReportRepList goodsBean = new GoodsInTransitReportRepList();
		Integer count = 0;
		Integer totalCount=0;
		List<?> list =  goodsInTransitReportDao.goodsInTransitSearch(userCode, resquest, device);
		try {
		if (list != null && !list.isEmpty()) {
			res = new ArrayList<>();
            for (Object object : list) {
                @SuppressWarnings("rawtypes")
                Map row = (Map) object;
                bean = new GoodsInTransitReportResponse();

                bean.setProfitCenter((String)row.get("Profitcenter"));
                bean.setState((String) row.get("state"));
                bean.setDealership((String)row.get("dealership"));
                bean.setParentDealerCode((String)row.get("ParentDealerCode"));
    			bean.setParentDealerLocation((String)row.get("ParentDealerLocation"));
    			bean.setProductdivision((String)row.get("productdivision"));
    			bean.setModel((String)row.get("model"));
    			bean.setVariant((String)row.get("variant"));
    			bean.setItemNo((String)row.get("item_no"));
    			bean.setItemDescription((String)row.get("item_description"));
    			bean.setChassisNo((String)row.get("chassis_no"));
    			bean.setVinNo((String)row.get("vin_no"));
    			bean.setEngineNo((String)row.get("engine_no"));
    			bean.setMfgInvoiceNumber((String)row.get("MfgInvoiceNumber"));
    			bean.setMfgInvoiceDate((String)row.get("MfgInvoiceDate"));
    			bean.setUnitPrice((BigDecimal)row.get("unit_price"));
    			bean.setPONO((String)row.get("pono"));
    			bean.setPODATE((Date)row.get("po_date"));
    			bean.setTransporter((String)row.get("transporter"));
    			bean.setLRNO((String)row.get("LRNo"));
    			bean.setLRDATE((Date)row.get("LRDate"));
    			bean.setStockQuantity((Integer)row.get("StockQuantity"));

    			res.add(bean);
    			count++;
    			totalCount = (Integer) row.get("Count")!=null?(Integer) row.get("Count"):0;
            }
            goodsBean.setTotalResult(totalCount);
            goodsBean.setResult(res);
            goodsBean.setCount(count);
		} 
    } catch (HibernateException exp) {
         goodsBean.setMessage(exp.getMessage());
         goodsBean.setStatus(Status.INTERNAL_SERVER_ERROR);
        logger.error(this.getClass().getName(), exp);
    } catch (Exception exp) {
         goodsBean.setMessage(exp.getMessage());
         goodsBean.setStatus(Status.INTERNAL_SERVER_ERROR);
        logger.error(this.getClass().getName(), exp);
    }finally {
    	if(list.isEmpty() && res.isEmpty()) {
             goodsBean.setMessage("Data Not Found...");
             goodsBean.setStatus(Status.NOT_FOUND);
    	}else if(!list.isEmpty() && !res.isEmpty()){
    		 goodsBean.setMessage("Goods In Transit Data Fetches Successfully...");
    		 goodsBean.setStatus(Status.OK);
    	}else {
    		 goodsBean.setMessage("something went wrong...");
    		 goodsBean.setStatus(Status.INTERNAL_SERVER_ERROR);
    	}
    }
    return goodsBean;
	
	}

	@Override
	public List<ModelItemList> itemDetailList(BigInteger modelId, String userCode) {
		
		List<ModelItemList> responseList=null;
		ModelItemList response =null;
		List<?> data =  goodsInTransitReportDao.itemDetailList(modelId, userCode);
		try {
		if (data != null && !data.isEmpty()) {
			responseList = new ArrayList<ModelItemList>();
			for (Object object : data) {
				Map row = (Map) object;
				response = new ModelItemList();
				response.setMachineItemId((BigInteger)row.get("machine_item_id"));
				response.setModelId((BigInteger)row.get("model_id"));
				response.setItemNo((String)row.get("item_no"));
				response.setItemDesc((String)row.get("item_description"));
				response.setVariant((String)row.get("variant"));
				responseList.add(response);
				}
			}	
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} 
		return responseList;
	}
	
	
	
	
	 
}
