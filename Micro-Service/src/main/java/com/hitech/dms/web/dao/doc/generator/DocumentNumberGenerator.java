package com.hitech.dms.web.dao.doc.generator;

import java.math.BigInteger;

import org.hibernate.Session;

public interface DocumentNumberGenerator {

	public String getDocumentNumber(String prefix, Integer suffix, Session session);
	public String getDocumentNumber1(String prefix, BigInteger suffix, Session session);
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId,
			Session session,String docDesc);
}
