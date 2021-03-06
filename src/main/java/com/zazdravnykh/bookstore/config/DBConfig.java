package com.zazdravnykh.bookstore.config;

import java.util.Properties;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import com.zazdravnykh.bookstore.repository.BookDAO;

@Configuration
@EnableJpaRepositories(basePackageClasses = BookDAO.class)
public class DBConfig {

	// @Autowired
	// ApplicationContext context;

	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/bookstore_spring?useSSL=false");
		// dataSource.setUrl("jdbc:mysql://localhost:3306/freelanc_bookstore_spring");
		dataSource.setUsername("root");
		// dataSource.setUsername("freelanc_eugnat");
		dataSource.setPassword("root");
		// dataSource.setPassword("lornat45Ex");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
		emFactory.setDataSource(dataSource());
		emFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		// properties.setProperty("hibernate.hbm2ddl.auto", "create");
		// properties.setProperty("hibernate.hbm2ddl.auto", "validate");
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.format_sql", "false");
		emFactory.setJpaProperties(properties);
		emFactory.setPackagesToScan("com.zazdravnykh.bookstore", "com.zazdravnykh.bookstore.domain");
		return emFactory;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tiles = new TilesConfigurer();
		tiles.setDefinitions(new String[] { "/WEB-INF/tiles/definitions/tile-definition.xml" });
		tiles.setCheckRefresh(true);
		return tiles;
	}
	//
	// @Bean(name = "viewResolver")
	// public ViewResolver viewResolver() {
	// UrlBasedViewResolver resolver = new UrlBasedViewResolver();
	// resolver.setOrder(-2);
	// resolver.setViewClass(TilesView.class);
	// return resolver;
	// }

	@Bean(name = "tilesViewResolver")
	TilesViewResolver tilesViewResolver() {

		TilesViewResolver resolver = new TilesViewResolver();

		return resolver;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(1024000);
		return resolver;
	}

	@Bean("messageSource")
	public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {

		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setBasename("classpath:messages");

		Properties properties = new Properties();
		properties.setProperty("fileEncodings", "UTF-8");
		messageSource.setFileEncodings(properties);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean("validator")
	public LocalValidatorFactoryBean validator() {

		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

		validator.setValidationMessageSource(reloadableResourceBundleMessageSource());

		return validator;
	}

}
