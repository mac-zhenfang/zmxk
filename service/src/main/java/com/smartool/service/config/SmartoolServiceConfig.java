package com.smartool.service.config;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.google.common.io.Closeables;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.smartool.service.Encrypter;
import com.smartool.service.controller.AuthenticationInterceptor;
import com.smartool.service.controller.AuthorizationInterceptor;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.AttendeeDaoImpl;
import com.smartool.service.dao.CreditRecordDao;
import com.smartool.service.dao.CreditRecordDaoImpl;
import com.smartool.service.dao.CreditRuleDao;
import com.smartool.service.dao.CreditRuleDaoImpl;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.EventDaoImpl;
import com.smartool.service.dao.EventTypeDao;
import com.smartool.service.dao.EventTypeDaoImpl;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.KidDaoImpl;
import com.smartool.service.dao.SecurityCodeDao;
import com.smartool.service.dao.SecurityCodeDaoImpl;
import com.smartool.service.dao.SerieDao;
import com.smartool.service.dao.SerieDaoImpl;
import com.smartool.service.dao.SiteDao;
import com.smartool.service.dao.SiteDaoImpl;
import com.smartool.service.dao.TagDao;
import com.smartool.service.dao.TagDaoImpl;
import com.smartool.service.dao.TeamDao;
import com.smartool.service.dao.TeamDaoImpl;
import com.smartool.service.dao.UserDao;
import com.smartool.service.dao.UserDaoImpl;
import com.smartool.service.service.CreditGenerator;
import com.smartool.service.service.CreditService;
import com.smartool.service.service.CreditServiceImpl;
import com.smartool.service.service.EventStartNotification;
import com.smartool.service.service.UserService;
import com.smartool.service.service.UserServiceImpl;
import com.smartool.service.sms.SmsClient;
import com.smartool.service.sms.SmsClientFactory;

import redis.clients.jedis.Jedis;

@Configuration
@ComponentScan(basePackages = { "com.smartool.service.*" })
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:zmxk.properties")
@PropertySource("classpath:jdbc.properties")
@PropertySource("classpath:quartz.properties")
public class SmartoolServiceConfig extends WebMvcConfigurationSupport {
	private String defaultKey = "JJ7kKLiXxkTWZFjl43+X9A==";
	@Autowired
	Environment env;
	@Autowired
	Properties properties;

	@Bean
	public CreditGenerator getCreditGenerator() {
		return new CreditGenerator(env.getProperty("credit.generate.interval.hour", Long.class));
	}
	
	@Bean
	public EventStartNotification eventStartNotification() {
		return new EventStartNotification();
	}

	@Bean
	public Encrypter getEncrypter() {
		return new Encrypter(env.getProperty("secure_algorithm"), env.getProperty("secure_key", defaultKey));
	}

	@Bean
	public CreditRecordDao getCreditRecordDao() {
		return new CreditRecordDaoImpl();
	}

	@Bean
	public SmsClient smsClient() {
		SmsClient client = new SmsClient(smsClientFactory(),
				env.getProperty("sms_send_url", "http://sms-api.luosimao.com/v1"));
		return client;
	}

	@Bean
	public SmsClientFactory smsClientFactory() {
		SmsClientFactory clientFactory = SmsClientFactory.builder().disableSSLChecks(true).build();
		return clientFactory;
	}

	@Bean
	public Jedis redis() {
		Jedis jedis = new Jedis("10.0.50.157");
		return jedis;
	}

	@Bean
	public UserService getUserService() {
		return new UserServiceImpl();
	}

	@Bean(initMethod = "iocInit")
	public CreditService getCreditService() {
		return new CreditServiceImpl();
	}

	@Bean
	public UserDao getUserDao() {
		return new UserDaoImpl();
	}

	@Bean
	public EventTypeDao getEventTypeDao() {
		return new EventTypeDaoImpl();
	}

	@Bean
	public SiteDao getSiteDao() {
		return new SiteDaoImpl();
	}

	@Bean
	public TeamDao teamDao() {
		return new TeamDaoImpl();
	}

	@Bean
	public SerieDao getSerieDao() {
		return new SerieDaoImpl();
	}

	@Bean
	public TagDao getTagDao() {
		return new TagDaoImpl();
	}

	@Bean
	public KidDao getKidDao() {
		return new KidDaoImpl();
	}
	
	@Bean
	public EventDao getEventDao() {
		return new EventDaoImpl();
	}

	@Bean
	public AttendeeDao getAttendeeDao() {
		return new AttendeeDaoImpl();
	}

	@Bean
	public CreditRuleDao getCreditRuleDao() {
		return new CreditRuleDaoImpl();
	}

	@Bean
	public SecurityCodeDao getSecurityCodeDao() {
		return new SecurityCodeDaoImpl();
	}

	@Bean
	public AuthenticationInterceptor getAuthenticationInterceptor() {
		return new AuthenticationInterceptor();
	}

	@Bean
	public AuthorizationInterceptor getAuthorizationInterceptor() {
		return new AuthorizationInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAuthenticationInterceptor());
		registry.addInterceptor(getAuthorizationInterceptor());
	}

	@Bean
	public VelocityEngine velocityEngine() {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		return ve;
	}

	@Bean
	public DataSource getDataSource() throws PropertyVetoException {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
		comboPooledDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		comboPooledDataSource.setUser(env.getProperty("jdbc.username"));
		comboPooledDataSource.setPassword(env.getProperty("jdbc.password"));
		comboPooledDataSource.setAutoCommitOnClose(false);
		comboPooledDataSource.setCheckoutTimeout(env.getProperty("cpool.checkoutTimeout", Integer.class));
		comboPooledDataSource.setInitialPoolSize(env.getProperty("cpool.minPoolSize", Integer.class));
		comboPooledDataSource.setMinPoolSize(env.getProperty("cpool.minPoolSize", Integer.class));
		comboPooledDataSource.setMaxPoolSize(env.getProperty("cpool.maxPoolSize", Integer.class));
		comboPooledDataSource.setMaxIdleTime(env.getProperty("cpool.maxIdleTime", Integer.class));
		comboPooledDataSource.setAcquireIncrement(env.getProperty("cpool.acquireIncrement", Integer.class));
		comboPooledDataSource
				.setMaxIdleTimeExcessConnections(env.getProperty("cpool.maxIdleTimeExcessConnections", Integer.class));
		return comboPooledDataSource;
	}

	@Bean
	public SqlSessionFactoryBean getSqlSessionFactoryBean() throws PropertyVetoException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(getDataSource());
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("spring/mybatis-config.xml"));
		return sqlSessionFactoryBean;
	}

	@Bean
	public DataSourceTransactionManager getDataSourceTransactionManager() throws PropertyVetoException {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(getDataSource());

		return dataSourceTransactionManager;
	}

	@Bean
	public SqlSession getSqlSessionTemplate() throws Exception {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getSqlSessionFactoryBean().getObject());
		return sqlSessionTemplate;
	}

	public static PropertyPlaceholderConfigurer getQuartzProperties() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] { new ClassPathResource("quartz.properties") };
		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		return ppc;
	}

	public Properties getProperties() throws Exception {
		Properties systemProperties = System.getProperties();
		Resource[] resources = new ClassPathResource[] { new ClassPathResource("quartz.properties") };
		for (final Resource resource : resources) {
			final InputStream inputStream = resource.getInputStream();
			try {
				systemProperties.load(inputStream);
			} finally {
				Closeables.closeQuietly(inputStream);
			}
		}
		return systemProperties;
	}
	

	@Bean
	public SchedulerFactoryBean getSchedulerFactoryBean() throws Exception {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setDataSource(getDataSource());
		schedulerFactoryBean.setTransactionManager(getDataSourceTransactionManager());
		schedulerFactoryBean.setQuartzProperties(getProperties());
		schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
		Map<String, Object> schedulerContextAsMap = new HashMap<String, Object>();
		schedulerContextAsMap.put("CreditGenerator", getCreditGenerator());
		schedulerContextAsMap.put("EventStartNotification", eventStartNotification());
		schedulerFactoryBean.setSchedulerContextAsMap(schedulerContextAsMap);
		schedulerFactoryBean.start();
		return schedulerFactoryBean;
	}

	public String getQrCodePath() {
		return env.getProperty("smartool_site", "http://120.55.117.171");
	}

	public int getEventNofityTime() {
		return env.getProperty("event_notify_time", Integer.class, 20 * 60 * 1000);
	}
	
	public int getNeedNotifyTimes() {
		return env.getProperty("event_need_notify_time",  Integer.class,  1);
	}
	
	public int getQrCodeLength() {
		return env.getProperty("qrcode_length",  Integer.class,  350); 
	}
	public int getQrCodeWidth() {
		return env.getProperty("qrcode_width",  Integer.class,  350); 
	}
}
