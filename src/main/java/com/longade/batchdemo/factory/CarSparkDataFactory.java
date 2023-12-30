package com.longade.batchdemo.factory;

import com.longade.batchdemo.model.Car;
import com.longade.batchdemo.util.ClassFieldsUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CarSparkDataFactory extends SparkDataFactory<Car> {

    @Autowired
    @Qualifier("mysqlDataSource")
    private DataSource dataSource;

    public Dataset<Car> readData() {
        HikariDataSource ds = (HikariDataSource) dataSource;

        Dataset<Car> dataset = sparkSession.read()
                .format("jdbc")
                .option("url", ds.getJdbcUrl())
                .option("user", ds.getUsername())
                .option("password", ds.getPassword())
                // .option("dbtable", "cars")
                .option("query", "select * from cars")
                .load()
                .withColumnsRenamed(ClassFieldsUtils.getFieldsMapping(Car.class))
                .as(Encoders.bean(Car.class));

        dataset.show();

        return dataset;

    }

}
