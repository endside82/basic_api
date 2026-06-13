package com.endside.api.config.db;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl;
import org.springframework.boot.flyway.autoconfigure.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;
import java.util.TimeZone;

/**
 * 데이터소스 설정
 *
 * 읽기/쓰기 분리를 위한 다중 데이터소스 및 트랜잭션 매니저를 구성한다.
 */
@Slf4j
@Configuration
public class DataSourceConfig {
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.read")
    public DataSource readDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @FlywayDataSource
    public DataSource routeDataSource() {
        return new RoutingDataSource() {{
            setDefaultTargetDataSource(writeDataSource());
            setTargetDataSources(new HashMap<>() {{
                put("write", writeDataSource());
                put("read", readDataSource());
            }});
        }};
    }

    @Bean
    public LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy() {
        return new LazyConnectionDataSourceProxy(routeDataSource());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(lazyConnectionDataSourceProxy());
        entityManagerFactoryBean.setPackagesToScan("com.endside.api");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        String profile = System.getProperty("spring.profiles.active");
        if (!"prod".equals(profile)) {
            vendorAdapter.setShowSql(true);
        }

        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.put("hibernate.jdbc.time_zone", "Asia/Seoul");
        properties.put("hibernate.physical_naming_strategy", new PhysicalNamingStrategySnakeCaseImpl());
        properties.put("hibernate.implicit_naming_strategy", new SpringImplicitNamingStrategy());
        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}
