package com.longade.batchdemo.factory;

import com.longade.batchdemo.model.Person;
import com.longade.batchdemo.util.ClassFieldsUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class PersonSparkDataFactory extends SparkDataFactory<Person> {

    @Autowired
    private DataSource dataSource;

    public Dataset<Person> readData() {

        HikariDataSource ds = (HikariDataSource) dataSource;
        String url = ds.getJdbcUrl().substring(0, ds.getJdbcUrl().indexOf(";"));

        Dataset<Person> dataset = sparkSession.read()
                .format("jdbc")
                .option("url", url)
                .option("user", ds.getUsername())
                .option("password", ds.getPassword())
                // .option("dbtable", "people")
                .option("query", "select * from people")
                .load()
                .withColumnsRenamed(ClassFieldsUtils.getFieldsMapping(Person.class))
                .as(Encoders.bean(Person.class));

        dataset.show();

        return dataset;

    }

}
