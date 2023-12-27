package com.longade.batchdemo.factory;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarSparkDataFactory {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tmpdb_spark";
    private static final String USERNAME = "tmpdb_spark_user";
    private static final String PASSWORD = "tmpdb_spark_user";

    @Autowired
    private SparkSession sparkSession;

    public Dataset<Row> readData() {
        return sparkSession.read()
                .format("jdbc")
                .option("url", JDBC_URL)
                .option("user", USERNAME)
                .option("password", PASSWORD)
                // .option("dbtable", "cars")
                .option("query", "select * from cars")
                .load();
    }

}
