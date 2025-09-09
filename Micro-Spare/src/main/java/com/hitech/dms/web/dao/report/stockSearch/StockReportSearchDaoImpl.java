package com.hitech.dms.web.dao.report.stockSearch;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.report.model.InvoicePartNoRequest;
import com.hitech.dms.web.model.report.model.InvoiceReportSearchRequest;
import com.hitech.dms.web.model.report.model.KPDResponse;
import com.hitech.dms.web.model.report.model.PartStockSearchList;
import com.hitech.dms.web.model.report.model.PartStockSearchRes;

@Repository
public class StockReportSearchDaoImpl implements StockReportSearchDao{
	
	private static final Logger logger = LoggerFactory.getLogger(StockReportSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PartStockSearchList search(String userCode, InvoiceReportSearchRequest resquest, Device device) {

	
			Session session = null;
			Transaction transaction = null;
			Query query = null;
			Integer totalRow = 0;
			boolean isSuccess = true;
			List<PartStockSearchRes> responseList = null;
			PartStockSearchRes responseModel = null;
			PartStockSearchList bean = new PartStockSearchList();
			Integer rowCount = 0;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {

				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				String sqlQuery = "exec [SP_PA_STOCK_REPORT] :UserCode,:DealerCode,:PartNo,:Zone,:State,:Partcategory,:HSNCode,:Bin,:StoreName,:FromDate,:ToDate,:BranchId, :Page,:size";
				String[] str=null;
				query = session.createSQLQuery(sqlQuery);
				if(resquest.getPartNumber()!=null) {
				 str = resquest.getPartNumber().split("\\|");
				}
				query.setParameter("UserCode", userCode);
				query.setParameter("DealerCode", resquest.getDealerCode());
				query.setParameter("PartNo", str!=null?str[0].trim():null);
				query.setParameter("Zone", resquest.getZone());
				query.setParameter("State", resquest.getStateId());
				query.setParameter("Partcategory", resquest.getPartCategoryId());
				query.setParameter("HSNCode", resquest.getHSNCode());
				query.setParameter("Bin", resquest.getBin());
				query.setParameter("StoreName", resquest.getStoreName());
				query.setParameter("FromDate", formatter.format(resquest.getFromDate()));
				query.setParameter("ToDate", formatter.format(DateToStringParserUtils.addDayByOne(resquest.getToDate())));
				query.setParameter("BranchId", resquest.getBranchId());
				query.setParameter("Page", resquest.getPage());
				query.setParameter("size", resquest.getSize());
				
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

				List<?> data = query.list();
				if (data != null && !data.isEmpty()) {
					responseList = new LinkedList<PartStockSearchRes>();
					for (Object object : data) {
						Map row = (Map) object;
									
						
						responseModel = new PartStockSearchRes();
						
						responseModel.setDealerCode((String) row.get("DealerCode"));
						responseModel.setDealerName((String) row.get("DealerName"));
						responseModel.setPartnumber((String) row.get("partnumber"));
						responseModel.setPartdesc((String) row.get("partdesc"));
						responseModel.setHSNcode((String) row.get("HSN_CODE"));
						responseModel.setStoreName((String) row.get("StoreName"));
						responseModel.setBinLocation((String) row.get("BinLocation"));
						responseModel.setOpeningQty((BigDecimal) row.get("OpeningQty"));
						responseModel.setOpeningValue((BigDecimal) row.get("OpeningValue"));
						responseModel.setReceiptQty((BigDecimal) row.get("ReceiptQty"));
						responseModel.setReceiptRate((BigDecimal) row.get("ReceiptRate"));
						responseModel.setReceiptValue((BigDecimal) row.get("ReceiptValue"));
						responseModel.setIssuedQty((BigDecimal) row.get("IssuedQty"));
						responseModel.setIssueRate((BigDecimal) row.get("IssueRate"));
						responseModel.setIssueValue((BigDecimal) row.get("IssueValue"));
						responseModel.setBalanceQty((BigDecimal) row.get("balanceQty"));
						responseModel.setBalanceRate((BigDecimal) row.get("balanceRate"));
						responseModel.setBalancevalue((BigDecimal) row.get("balancevalue"));
						responseModel.setCurrentStock((BigDecimal) row.get("currentStock"));//added on 11-09-24
						
						totalRow = (Integer) row.get("totalCount");

						responseList.add(responseModel);
						rowCount++;
					}
					bean.setTotalRowCount(totalRow);
					bean.setSearchList(responseList);
					bean.setRowCount(rowCount);
				}

			} catch (SQLGrammarException exp) {
				if (transaction != null) {
					transaction.rollback();
				}
				isSuccess = false;
				logger.error(this.getClass().getName(), exp);
			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);
			} finally {
				if (session != null) {
					transaction.commit();
					session.close();
				}
				if (isSuccess) {
					bean.setStatusCode(WebConstants.STATUS_OK_200);
					bean.setMsg("Part Stock Detail Fetches Successfully...");

				} else {
					bean.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			}
			return bean;
		}

	@Override
	public List<KPDResponse> getPartStockKPDList(String userCode, InvoicePartNoRequest requestModel) {

			Session session = null;
			Query query = null;
			KPDResponse responseModel=null;
			List<KPDResponse>  responseListModel= null;
			String sqlQuery = "exec [pa_part_stock_report_KPD_search] :userCode, :searchText";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("searchText", requestModel.getSearchText());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					
					responseListModel = new ArrayList<>();
					for (Object object : data) {
						Map row = (Map) object;
						
						responseModel = new KPDResponse();
						
						responseModel.setId((BigInteger) row.get("Id"));
						responseModel.setCustomerName((String)row.get("CustomerCode")+" | "+(String) row.get("CustomerName"));
						responseModel.setCustomerCode((String)row.get("CustomerCode"));
						responseListModel.add(responseModel);
					}
				}
			} catch (SQLGrammarException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (HibernateException exp) {
				logger.error(this.getClass().getName(), exp);
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			} finally {
				if (session != null) {
					session.close();
				}
			}
			return responseListModel;
		}

}
