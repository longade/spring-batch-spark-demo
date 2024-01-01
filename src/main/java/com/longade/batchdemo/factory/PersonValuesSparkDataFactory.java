package com.longade.batchdemo.factory;

import com.longade.batchdemo.model.PersonValues;
import com.longade.batchdemo.util.ClassFieldsUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class PersonValuesSparkDataFactory extends SparkDataFactory<PersonValues> {

    @Autowired
    @Qualifier("mysqlDataSource")
    private DataSource dataSource;

    @Override
    public Dataset<PersonValues> readData() {
        HikariDataSource ds = (HikariDataSource) dataSource;

        Dataset<PersonValues> dataset = sparkSession.read()
                .format("jdbc")
                .option("url", ds.getJdbcUrl())
                .option("user", ds.getUsername())
                .option("password", ds.getPassword())
                // .option("dbtable", "cars")
                .option("query", "select * from person_values")
                .load()
                .withColumnsRenamed(ClassFieldsUtils.getFieldsMapping(PersonValues.class))
                .as(Encoders.bean(PersonValues.class));

        dataset.show();

        return dataset;
    }
}
