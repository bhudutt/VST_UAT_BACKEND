package com.hitech.dms.constants;

public interface AppConstants {
	// Hibernate properties constants name
	public static final String HIBERNATE_DIALECT = "hibernate.dialect";
	public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	public static final String HIBERNATE_SQL_COMMENTS = "hibernate.use_sql_comments";
	public static final String HIBERNATE_FORMATE_SQL = "hibernate.format_sql";
	public static final String HIBERNATE_SESSION_CONTEXT = "hibernate.current_session_context_class";
	public static final String HIBERNATE_GENERATE_STATISTICS = "hibernate.generate_statistics";
	public static final String HIBERNATE_AUTOCOMMIT = "hibernate.autocommit";
	public static final String HIBERNATE_AUTO_INCREMENT = "hibernate.c3p0.acquire_increment";
	public static final String HIBERNATE_IDLE_TEST_PERIOD = "hibernate.c3p0.idle_test_period";
	public static final String HIBERNATE_MAX_SIZE = "hibernate.c3p0.max_size";
	public static final String HIBERNATE_MIN_SIZE = "hibernate.c3p0.max_statements";
	public static final String HIBERNATE_MAX_STATEMENT = "hibernate.c3p0.min_size";
	public static final String HIBERNATE_TIMEOUT = "hibernate.c3p0.timeout";

	public static final String CSS_LOCATION = "css/";
	public static final String FONTS_HANDLER = "/fonts/**";
	public static final String IMAGES_LOCATION = "images/";
	public static final String IMAGES_CAT_LOCATION = "images/cat_images/";
	public static final String IMAGES_CATSVG_LOCATION = "images/cat_images/svg/";
	public static final String IMAGES_CATSVGV_LOCATION = "images/cat_images/svg/";
	public static final String JS_LOCATION = "js/";
	public static final String SWF_LOCATION = "swf/";
	public static final String SVG_LOCATION = "svg/";

	public static final String PREFIX = "/WEB-INF/views/";
	public static final String SUFFIX = ".jsp";
	public static final String MESSAGE_RESOURCE_URL = "classpath:resources/messages";
	public static final String VALIDATION_RESOURCE_URL = "classpath:resources/validation";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String LANGUAGE_PARAM = "lang";
	public static final String LANGUAGE_EN = "en";

	public static final String CSS_HANDLER = "/css/**";
	public static final String FONT_AWE_LOCATION = "font_awesome/";
	public static final String FONT_AWE_LOCATION_ = "font-awesome/";
	public static final String FONT_AWE_HANDLER = "/font_awesome/**";
	public static final String FONT_AWE_HANDLER_ = "/font-awesome/**";
	public static final String FONTS_LOCATION = "fonts/";
	public static final String IMAGES_HANDLER = "/images/**";
	public static final String IMAGES_CAT_HANDLER = "/images/cat_images/**";
	public static final String IMAGES_CATSVG_HANDLER = "/images/cat_images/svg/**";
	public static final String IMAGES_CATSVGV_HANDLER = "/images/cat_images/svg/**";
	public static final String JS_HANDLER = "/js/**";
	public static final String SWF_HANDLER = "/swf/**";
	public static final String SVG_HANDLER = "/svg/**";
	
	public static final String SECRET_KEY = "HiTeCh";
	public static final String DNSH_ACTUATOR = "dnshactuator";
	public static final String CNHI_ACTUATOR = "cnhiactuator";
	public static final String ACTUATOR_ADMIN = "ACTUATOR_ADMIN";
	public static final String OEM_ROLE = "OEM_EMP";

	// Mapper xml path declaration
	public String APP_BUSINESS_OBJECT_DOZER_MAPPER = "master-business-object-mapper.xml";
}