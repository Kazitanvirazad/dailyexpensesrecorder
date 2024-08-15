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
    private String databasePlatform;
    @Value("${jpa.show-sql}")
    private String showSql;
    @Value("${datasource.url}")
    private String datasourceUrl;
    @Value("${datasource.username}")
    private String datasourceUsername;
    @Value("${datasource.password}")
    private String datasourcePassword;
    @Value("${datasource.driver-class-name}")
    private String datasourceDriverClassName;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernate_hbm2ddl_auto;
    @Value("${datasource.dbname}")
    private String dbName;
    @Value("${datasource.schema}")
    private String datasourceSchema;

    @Bean(name = "datasource")
    public DataSource getDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(Objects.requireNonNull(datasourceDriverClassName));
        driverManagerDataSource.setUrl(datasourceUrl);
        driverManagerDataSource.setUsername(datasourceUsername);
        driverManagerDataSource.setPassword(datasourcePassword);

        return driverManagerDataSource;
    }

    @Bean(name = "entitymanager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        Properties properties = new Properties();
        properties.setProperty(Environment.DIALECT, databasePlatform);
        properties.setProperty(Environment.SHOW_SQL, showSql);
        properties.put(Environment.HBM2DDL_AUTO, hibernate_hbm2ddl_auto);

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(getDataSource());
        localContainerEntityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        localContainerEntityManagerFactoryBean.setPackagesToScan("net.expenses.recorder.dao");
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
