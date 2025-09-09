/**
 * 
 */
package com.hitech.dms.app.config.publisher;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.ConnectionConfiguration;


/**
 * @author santosh.kumar
 *
 */
@Repository
public class ConnectionConfigurationImpl implements ConnectionConfiguration {

	
//	@Qualifier("sessionFactoryForReports")
	@Autowired
	private SessionFactory sessionFactoryForReports;
	
	private Connection connection = null;
	private Session session = null;

	@Override
	public Connection getConnection() {
		session = sessionFactoryForReports.openSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		connection = sessionImpl.connection();
		return connection;
	}

}