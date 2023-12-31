package com.longade.batchdemo.factory;

import com.longade.batchdemo.model.Car;
import com.longade.batchdemo.util.ClassFieldsUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.springframework.stereotype.Component;

@Component
public class CarBQSparkDataFactory extends SparkDataFactory<Car> {

    private static final String DATASET_NAME = "TMP_DB_SPARK";
    private static final String TABLE_NAME = "T_CARS";

    @Override
    public Dataset<Car> readData() {

        String query = String.format(
                "select * from `%s.%s`",
                DATASET_NAME,
                TABLE_NAME
        );

        Dataset<Car> dataset = sparkSession.read()
                .format("bigquery")
                .option("query", query)
                .load()
                .withColumnsRenamed(ClassFieldsUtils.getFieldsMapping(Car.class))
                .as(Encoders.bean(Car.class));

        dataset.show();

        return dataset;
    }
}
