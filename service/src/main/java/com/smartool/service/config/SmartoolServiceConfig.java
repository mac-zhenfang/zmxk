package com.smartool.service.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.smartool.service.Encrypter;
import com.smartool.service.controller.AuthenticationInterceptor;
import com.smartool.service.controller.AuthorizationInterceptor;
import com.smartool.service.dao.AttendeeDao;
import com.smartool.service.dao.AttendeeDaoImpl;
import com.smartool.service.dao.CreditRuleDao;
import com.smartool.service.dao.CreditRuleDaoImpl;
import com.smartool.service.dao.EventDao;
import com.smartool.service.dao.EventDaoImpl;
import com.smartool.service.dao.KidDao;
import com.smartool.service.dao.KidDaoImpl;
import com.smartool.service.dao.SecurityCodeDao;
import com.smartool.service.dao.SecurityCodeDaoImpl;
import com.smartool.service.dao.UserDao;
import com.smartool.service.dao.UserDaoImpl;
import com.smartool.service.service.UserService;
import com.smartool.service.service.UserServiceImpl;

@Configuration
@ComponentScan(basePackages = { "com.smartool.service.*" })
@PropertySource("classpath:zmxk.properties")
@PropertySource("classpath:jdbc.properties")
public class SmartoolServiceConfig extends WebMvcConfigurationSupport {
	private String defaultKey = "JJ7kKLiXxkTWZFjl43+X9A==";
	@Autowired
	Environment env;

	@Bean
	public Encrypter getEncrypter() {
		return new Encrypter(env.getProperty("secure_algorithm"), env.getProperty("secure_key", defaultKey));
	}

	@Bean
	public UserService getUserService() {
		return new UserServiceImpl();
	}

	@Bean
	public UserDao getUserDao() {
		return new UserDaoImpl();
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
	public DataSource getComboPooledDataSource() throws PropertyVetoException {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
		comboPooledDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		comboPooledDataSource.setUser(env.getProperty("jdbc.username"));
		comboPooledDataSource.setPassword(env.getProperty("jdbc.password"));
		comboPooledDataSource.setAutoCommitOnClose(true);
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
		sqlSessionFactoryBean.setDataSource(getComboPooledDataSource());
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("spring/mybatis-config.xml"));
		return sqlSessionFactoryBean;
	}

	@Bean
	public DataSourceTransactionManager getDataSourceTransactionManager() throws PropertyVetoException {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(getComboPooledDataSource());
		return dataSourceTransactionManager;
	}

	@Bean
	public SqlSession getSqlSessionTemplate() throws Exception {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getSqlSessionFactoryBean().getObject());
		return sqlSessionTemplate;
	}
}
