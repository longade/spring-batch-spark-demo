package com.longade.batchdemo.factory;

import com.longade.batchdemo.config.properties.SqlReaderConfigurationProperties;
import com.longade.batchdemo.model.Car;
import com.longade.batchdemo.util.ClassFieldsUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CarSparkDataFactory extends SparkDataFactory<Car> {

    @Autowired
    private SqlReaderConfigurationProperties sqlReaderConfProps;

    @Autowired
    @Qualifier("mysqlDataSource")
    private DataSource dataSource;

    public Dataset<Car> readDataCursor() {
        HikariDataSource ds = (HikariDataSource) dataSource;

        Dataset<Car> dataset = sparkSession.read()
                .format("jdbc")
                .option("url", ds.getJdbcUrl())
                .option("user", ds.getUsername())
                .option("password", ds.getPassword())
                // .option("dbtable", "cars")
                .option("query", sqlReaderConfProps.getQuery())
                .load()
                .withColumnsRenamed(ClassFieldsUtils.getFieldsMapping(Car.class))
                .as(Encoders.bean(Car.class));

        dataset.show();

        return dataset;

    }

    private Map<String, Order> sortKeysMap(String sortKey, Order order) {
        return Map.of(sortKey, order);
    }

    private MySqlPagingQueryProvider queryProvider() {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(sqlReaderConfProps.getSelectClause());
        queryProvider.setFromClause(sqlReaderConfProps.getFromClause());
        // queryProvider.setWhereClause(sqlReaderConfProps.getWhereClause());
        queryProvider.setSortKeys(sortKeysMap(sqlReaderConfProps.getSortKey(), sqlReaderConfProps.getOrder()));
        return queryProvider;
    }

    private List<Car> readCarsDataPaging() {
        JdbcPagingItemReader<Car> reader = new JdbcPagingItemReaderBuilder<Car>()
                .name(sqlReaderConfProps.getName())
                .dataSource(dataSource)
                .queryProvider(queryProvider())
                .pageSize(sqlReaderConfProps.getPageSize())
                .fetchSize(sqlReaderConfProps.getFetchSize())
                .rowMapper(new BeanPropertyRowMapper<>(Car.class))
                .build();

        List<Car> cars = new ArrayList<>();

        try {
            reader.afterPropertiesSet();
            Car car = reader.read();
            while (car != null) {
                cars.add(car);
                car = reader.read();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return cars;
    }

    public Dataset<Car> readData() {
        List<Car> cars = readCarsDataPaging();

        Dataset<Car> carDataset = sparkSession.createDataset(cars, Encoders.bean(Car.class));

        carDataset.show();

        return carDataset;
    }

}
