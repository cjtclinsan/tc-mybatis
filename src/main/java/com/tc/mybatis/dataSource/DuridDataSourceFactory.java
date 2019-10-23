package com.tc.mybatis.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author taosh
 * @create 2019-10-23 11:30
 */
public class DuridDataSourceFactory implements DataSourceFactory {
    private Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource dds = new DruidDataSource();

        dds.setUrl(properties.getProperty("url"));
        dds.setPassword(properties.getProperty("password"));
        dds.setUsername(properties.getProperty("username"));
        dds.setDriverClassName(properties.getProperty("driver"));

        try {
            dds.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dds;
    }
}
