package com.longade.batchdemo.factory;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class SparkDataFactory<T> {

    @Autowired
    protected SparkSession sparkSession;

    public abstract Dataset<T> readData();

}
