package com.longade.batchdemo.factory;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class PersonSparkDataFactory {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private DataSource dataSource;

    public Dataset<Row> readData() {

        HikariDataSource ds = (HikariDataSource) dataSource;
        String url = ds.getJdbcUrl().substring(0, ds.getJdbcUrl().indexOf(";"));

        return sparkSession.read()
                .format("jdbc")
                .option("url", url)
                .option("user", ds.getUsername())
                .option("password", ds.getPassword())
                // .option("dbtable", "people")
                .option("query", "select * from people")
                .load();
    }

}
