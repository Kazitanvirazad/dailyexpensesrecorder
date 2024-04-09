package net.expenses.recorder.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Kazi Tanvir Azad
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"net.expenses.recorder.repository"},
        transactionManagerRef = "transactionmanager", entityManagerFactoryRef = "entitymanager")
@PropertySource(value = {"classpath:db-config-${spring.profiles.active}.properties"})
public class DBConfig {

    @Value("${jpa.database-platform}")
    private String DATABASE_PLATFORM;
    @Value("${jpa.show-sql}")
    private String SHOW_SQL;
    @Value("${datasource.url}")
    private String DATASOURCE_URL;
    @Value("${datasource.username}")
    private String DATASOURCE_USERNAME;
    @Value("${datasource.password}")
    private String DATASOURCE_PASSWORD;
    @Value("${datasource.driver-class-name}")
    private String DATASOURCE_DRIVER_CLASS_NAME;
    @Value("${hibernate.hbm2ddl.auto}")
    private String HIBERNATE_HBM2DDL_AUTO;

    @Bean(name = "datasource")
    public DataSource getDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(Objects.requireNonNull(DATASOURCE_DRIVER_CLASS_NAME));
        driverManagerDataSource.setUrl(DATASOURCE_URL);
        driverManagerDataSource.setUsername(DATASOURCE_USERNAME);
        driverManagerDataSource.setPassword(DATASOURCE_PASSWORD);

        return driverManagerDataSource;
    }

    @Bean(name = "entitymanager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        Properties properties = new Properties();
        properties.setProperty(Environment.DIALECT, DATABASE_PLATFORM);
        properties.setProperty(Environment.SHOW_SQL, SHOW_SQL);
        properties.put(Environment.HBM2DDL_AUTO, HIBERNATE_HBM2DDL_AUTO);

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(getDataSource());
        localContainerEntityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        localContainerEntityManagerFactoryBean.setPackagesToScan("net.uncrack.server.api.app.dao");
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaProperties(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "transactionmanager")
    @DependsOn(value = "entitymanager")
    public JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
