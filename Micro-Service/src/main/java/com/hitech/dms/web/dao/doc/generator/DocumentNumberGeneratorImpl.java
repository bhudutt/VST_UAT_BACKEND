package com.hitech.dms.web.dao.doc.generator;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.common.CommonDaoImpl;

@Repository
public class DocumentNumberGeneratorImpl implements DocumentNumberGenerator {

	private static final Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public String getDocumentNumber(String documentPrefix, Integer branchId, Session session) {
		Query query = null;
		String documentNumber = null;
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);
		
		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		return documentNumber;
	}
	
	@Override
	public String getDocumentNumber1(String documentPrefix, BigInteger branchId, Session session) {
		Query query = null;
		String documentNumber = null;
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);
		
		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		return documentNumber;
	}
	
	@Override
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId,
			Session session,String docDesc) {

		if (null != lastDocumentNumber) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC Update_INV_Doc_No :lastDocumentNo,:DocumentTypeDesc,:currentDate,:branchId,:documentPrefix";
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("lastDocumentNo", lastDocumentNumber);
			query.setParameter("DocumentTypeDesc", docDesc);
			query.setParameter("documentPrefix", documentPrefix);
			query.setParameter("branchId", Integer.parseInt(branchId));
			query.setParameter("currentDate", currentDate);
			query.executeUpdate();
		}
	}

	
}	
