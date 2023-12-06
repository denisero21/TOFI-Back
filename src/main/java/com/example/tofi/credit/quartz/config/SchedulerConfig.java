package com.example.tofi.credit.quartz.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

	private final ApplicationContext applicationContext;
	private final DataSource dataSource;
	private final QuartzProperties quartzProperties;


	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {

		SchedulerJobFactory jobFactory = new SchedulerJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setDataSource(dataSource);
		factory.setQuartzProperties(quartzProperties());
		factory.setJobFactory(jobFactory);
		factory.setApplicationContextSchedulerContextKey("applicationContext");
		return factory;
	}

	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		Properties props = new Properties();
		props.putAll(quartzProperties.getProperties());
		propertiesFactoryBean.setProperties(props);
		propertiesFactoryBean.afterPropertiesSet(); //it's important
		return propertiesFactoryBean.getObject();
	}

}
