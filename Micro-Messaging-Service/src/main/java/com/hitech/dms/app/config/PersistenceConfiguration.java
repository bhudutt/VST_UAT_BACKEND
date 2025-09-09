package com.hitech.dms.app.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
//import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hitech.dms.app.constants.AppConstants;

@Configuration
@Import(DbConfigProperties.class)
@ComponentScan({ "com.hitech.dms.app.repo" })
@EnableTransactionManagement
public class PersistenceConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(PersistenceConfiguration.class);

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
	@Value("${jdbc.driverClassNameForReports}")
	private String driverClassNameForReports;
	@Value("${jdbc.urlForReports}")
	private String urlForReports;
	@Value("${jdbc.usernameForReports}")
	private String usernameForReports;
	@Value("${jdbc.passwordForReports}")
	private String passwordForReports;
	@Value("${hibernate.dialectForReports}")
	private String hibernateDialectForReports;

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

	@Bean(name = "dataSource")
	@Primary
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(getDriverClassName());
		dataSource.setUrl(getUrl());
		dataSource.setUsername(getUsername());
		dataSource.setPassword(getPassword());
		return dataSource;
	}

	@Bean(name = "dataSourceForReports")
	public DataSource getDataSourceForReports() {
		DriverManagerDataSource dataSourceForReports = new DriverManagerDataSource();
		dataSourceForReports.setDriverClassName(getDriverClassNameForReports());
		dataSourceForReports.setUrl(getUrlForReports());
		dataSourceForReports.setUsername(getUsernameForReports());
		dataSourceForReports.setPassword(getPasswordForReports());
		return dataSourceForReports;
	}

	@Bean(name = "transactionManager")
	@Primary
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory);
		return hibernateTransactionManager;
	}

	@Bean(name = "transactionManagerForReports")
	@Autowired
	public HibernateTransactionManager transactionManagerForReports(
			@Qualifier("sessionFactoryForReports") SessionFactory sessionFactoryForReports) {
		HibernateTransactionManager hibernateTransactionManagerForReports = new HibernateTransactionManager();
		hibernateTransactionManagerForReports.setSessionFactory(sessionFactoryForReports);
		return hibernateTransactionManagerForReports;
	}

	@Bean(name = "sessionFactory")
	@Primary
	@Autowired
	public SessionFactory sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder localSessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource);
		localSessionFactoryBuilder.scanPackages(new String[] { "com.hitech.dms.app.entity" });
		localSessionFactoryBuilder.addProperties(getHibernateProperties());
		return localSessionFactoryBuilder.buildSessionFactory();
	}

	@Bean(name = "sessionFactoryForReports")
	@Autowired
	public SessionFactory sessionFactoryForReports(@Qualifier("dataSourceForReports") DataSource dataSourceForReports) {
		LocalSessionFactoryBuilder localSessionFactoryBuilderForReports = new LocalSessionFactoryBuilder(
				dataSourceForReports);
		localSessionFactoryBuilderForReports.scanPackages(new String[] { "com.hitech.dms.app.entity" });
		localSessionFactoryBuilderForReports.addProperties(getHibernatePropertiesForReports());
		return localSessionFactoryBuilderForReports.buildSessionFactory();
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put(AppConstants.HIBERNATE_DIALECT, getHibernateDialect());
		properties = getCommonHibernateProperties(properties);
		return properties;

	}

	private Properties getHibernatePropertiesForReports() {
		Properties properties = new Properties();
		properties.put(AppConstants.HIBERNATE_DIALECT, getHibernateDialectForReports());
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
//	@Bean
//	public Mapper getDozerBeanMapper() {
//		List<String> list = new ArrayList<String>();
//		list.add(AppConstants.APP_BUSINESS_OBJECT_DOZER_MAPPER);
//		Mapper mapper = new DozerBeanMapper(list);
//		return mapper;
//	}

//	@Bean(name = "org.dozer.Mapper")
//	public DozerBeanMapper dozerBean() {
//		List<String> mappingFiles = Arrays.asList(AppConstants.APP_BUSINESS_OBJECT_DOZER_MAPPER);
//
//		DozerBeanMapper dozerBean = new DozerBeanMapper();
//		dozerBean.setMappingFiles(mappingFiles);
//		return dozerBean;
//	}

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

	public String getDriverClassNameForReports() {
		return driverClassNameForReports;
	}

	public void setDriverClassNameForReports(String driverClassNameForReports) {
		this.driverClassNameForReports = driverClassNameForReports;
	}

	public String getUrlForReports() {
		return urlForReports;
	}

	public void setUrlForReports(String urlForReports) {
		this.urlForReports = urlForReports;
	}

	public String getUsernameForReports() {
		return usernameForReports;
	}

	public void setUsernameForReports(String usernameForReports) {
		this.usernameForReports = usernameForReports;
	}

	public String getPasswordForReports() {
		return passwordForReports;
	}

	public void setPasswordForReports(String passwordForReports) {
		this.passwordForReports = passwordForReports;
	}

	public String getHibernateDialectForReports() {
		return hibernateDialectForReports;
	}

	public void setHibernateDialectForReports(String hibernateDialectForReports) {
		this.hibernateDialectForReports = hibernateDialectForReports;
	}
}
