/**
 * 
 */
package com.hitech.dms.app.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.hitech.dms.constants.AppConstants;

/**
 * @author dnsh87
 *
 */
@Configuration
@Import(DbConfigProperties.class)
@EnableJpaRepositories(entityManagerFactoryRef = "userEntityManager", transactionManagerRef = "userTransactionManager", basePackages = {
		"com.hitech.dms.web.repo" })
public class JpaPersistenceConfiguration {
	@Autowired
    Environment environment;
	
	@Value("${jdbc.driverClassName}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;
	
	@Value("${hibernate.dialect}")
	private String hibernateDialect;
	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;
	@Value("${hibernate.use_sql_comments}")
	private String use_sql_comments;
	@Value("${hibernate.format_sql}")
	private String format_sql;
	@Value("${hibernate.generate_statistics}")
	private String generate_statistics;
	@Value("${hibernate.autocommit}")
	private String autocommit;
	@Value("${hibernate.c3p0.acquire_increment}")
	private String acquire_increment;
	@Value("${hibernate.c3p0.idle_test_period}")
	private String idle_test_period;
	@Value("${hibernate.c3p0.max_size}")
	private String max_size;
	@Value("${hibernate.c3p0.max_statements}")
	private String max_statements;
	@Value("${hibernate.c3p0.min_size}")
	private String min_size;
	@Value("${hibernate.c3p0.timeout}")
	private String timeout;
	
	@Autowired
	public DataSource dataSource;
	
	@Bean(name = "userEntityManager")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(new String[] { "com.hitech.dms.web.entity" });
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(getJpaProperties());
		em.setPersistenceUnitName("customers");

		return em;
	}

	@Bean(name = "userTransactionManager")
	public JpaTransactionManager transactionManager(EntityManagerFactory customerEntityManager) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(customerEntityManager);

		return transactionManager;
	}
	
	private Properties getJpaProperties() {
		Properties properties = new Properties();
		properties.put(AppConstants.HIBERNATE_DIALECT, getHibernateDialect());
		properties = getCommonHibernateProperties(properties);
		return properties;
		
	}
	
	private Properties getCommonHibernateProperties(Properties properties) {
		properties.put(AppConstants.HIBERNATE_SHOW_SQL, getHibernateShowSql());
		properties.put(AppConstants.HIBERNATE_SQL_COMMENTS, getUse_sql_comments());
		properties.put(AppConstants.HIBERNATE_FORMATE_SQL, getFormat_sql());
		properties.put(AppConstants.HIBERNATE_AUTOCOMMIT, getAutocommit());
		properties.put(AppConstants.HIBERNATE_AUTO_INCREMENT, getAcquire_increment());
		properties.put(AppConstants.HIBERNATE_IDLE_TEST_PERIOD, getIdle_test_period());
		properties.put(AppConstants.HIBERNATE_MAX_SIZE, getMax_size());
		properties.put(AppConstants.HIBERNATE_MIN_SIZE, getMin_size());
		properties.put(AppConstants.HIBERNATE_MAX_STATEMENT, getMax_statements());
		properties.put(AppConstants.HIBERNATE_TIMEOUT, getTimeout());
		return properties;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHibernateDialect() {
		return hibernateDialect;
	}

	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}

	public String getHibernateShowSql() {
		return hibernateShowSql;
	}

	public void setHibernateShowSql(String hibernateShowSql) {
		this.hibernateShowSql = hibernateShowSql;
	}

	public String getUse_sql_comments() {
		return use_sql_comments;
	}

	public void setUse_sql_comments(String use_sql_comments) {
		this.use_sql_comments = use_sql_comments;
	}

	public String getFormat_sql() {
		return format_sql;
	}

	public void setFormat_sql(String format_sql) {
		this.format_sql = format_sql;
	}

	public String getGenerate_statistics() {
		return generate_statistics;
	}

	public void setGenerate_statistics(String generate_statistics) {
		this.generate_statistics = generate_statistics;
	}

	public String getAutocommit() {
		return autocommit;
	}

	public void setAutocommit(String autocommit) {
		this.autocommit = autocommit;
	}

	public String getAcquire_increment() {
		return acquire_increment;
	}

	public void setAcquire_increment(String acquire_increment) {
		this.acquire_increment = acquire_increment;
	}

	public String getIdle_test_period() {
		return idle_test_period;
	}

	public void setIdle_test_period(String idle_test_period) {
		this.idle_test_period = idle_test_period;
	}

	public String getMax_size() {
		return max_size;
	}

	public void setMax_size(String max_size) {
		this.max_size = max_size;
	}

	public String getMax_statements() {
		return max_statements;
	}

	public void setMax_statements(String max_statements) {
		this.max_statements = max_statements;
	}

	public String getMin_size() {
		return min_size;
	}

	public void setMin_size(String min_size) {
		this.min_size = min_size;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
}
