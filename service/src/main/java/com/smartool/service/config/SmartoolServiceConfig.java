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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.smartool.service.dao.UserDao;
import com.smartool.service.dao.UserDaoImpl;

@Configuration
@ComponentScan(basePackages = { "com.smartool.service.*" })
@PropertySource("classpath:zmxk.properties")
@PropertySource("classpath:jdbc.properties")
public class SmartoolServiceConfig extends WebMvcConfigurationSupport {
	@Autowired
	Environment env;

	@Bean
	public UserDao getUserDao() {
//		return new MockedUserDaoImpl();
		return new UserDaoImpl();
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

	// @Bean
	// public DataSourceTransactionManager getDataSourceTransactionManager() {
	// DataSourceTransactionManager dataSourceTransactionManager = new
	// DataSourceTransactionManager();
	// return dataSourceTransactionManager;
	// }

	public SqlSessionFactoryBean getSqlSessionFactoryBean() throws PropertyVetoException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(getComboPooledDataSource());
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("spring/mybatis-config.xml"));
		return sqlSessionFactoryBean;
	}

	@Bean
	public SqlSession getSqlSessionTemplate() throws Exception {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(getSqlSessionFactoryBean().getObject());
		return sqlSessionTemplate;
	}

	// <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
	// destroy-method="close">
	// <property name="driverClass" value="${jdbc.driverClassName}" />
	// <property name="jdbcUrl" value="${jdbc.url}" />
	// <property name="user" value="${jdbc.username}" />
	// <property name="password" value="${jdbc.password}" />
	//
	// <property name="autoCommitOnClose" value="true" />
	// <property name="checkoutTimeout" value="${cpool.checkoutTimeout}" />
	// <property name="initialPoolSize" value="${cpool.minPoolSize}" />
	// <property name="minPoolSize" value="${cpool.minPoolSize}" />
	// <property name="maxPoolSize" value="${cpool.maxPoolSize}" />
	// <property name="maxIdleTime" value="${cpool.maxIdleTime}" />
	// <property name="acquireIncrement" value="${cpool.acquireIncrement}" />
	// <property name="maxIdleTimeExcessConnections"
	// value="${cpool.maxIdleTimeExcessConnections}" />
	// </bean>
	//
	// <!-- See
	// http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/transaction.html
	// -->
	// <bean id="transactionManager"
	// class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
	// p:dataSource-ref="dataSource" />
	//
	// <bean id="sqlSessionFactory"
	// class="org.mybatis.spring.SqlSessionFactoryBean">
	// <property name="dataSource" ref="dataSource" />
	// <property name="configLocation"
	// value="classpath:spring/mybatis-config.xml" />
	// </bean>
	// <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
	// <constructor-arg index="0" ref="sqlSessionFactory" />
	// </bean>
}
