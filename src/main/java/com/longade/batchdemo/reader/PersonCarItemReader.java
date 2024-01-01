package com.longade.batchdemo.reader;

import com.longade.batchdemo.factory.CarBQSparkDataFactory;
import com.longade.batchdemo.factory.CarSparkDataFactory;
import com.longade.batchdemo.factory.PersonSparkDataFactory;
import com.longade.batchdemo.model.Car;
import com.longade.batchdemo.model.Person;
import com.longade.batchdemo.model.PersonCar;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// @Component
public class PersonCarItemReader implements ItemReader<PersonCar> {

    private List<PersonCar> personCarList;

    @Autowired
    private PersonSparkDataFactory personSparkDataFactory;

    @Autowired
    private CarSparkDataFactory carSparkDataFactory;

    // @Autowired
    // private CarBQSparkDataFactory carBQSparkDataFactory;

    private Dataset<Car> joinCarsDataset(Dataset<Car> carsBQDataset, Dataset<Car> carsDataset) {
        Dataset<Car> joinedCarsDataset = carsBQDataset
                .join(
                        carsDataset,
                        carsBQDataset.col("carId").equalTo(carsDataset.col("carId")),
                        "inner"
                )
                .select(
                        carsBQDataset.col("carId"),
                        carsBQDataset.col("brand"),
                        carsBQDataset.col("model"),
                        carsBQDataset.col("personId")
                )
                .as(Encoders.bean(Car.class));

        joinedCarsDataset.show();

        return joinedCarsDataset;
    }

    private List<PersonCar> initializeList() {
        Dataset<Person> peopleDataset = personSparkDataFactory.readData();
        // Dataset<Car> carsBQDataset = carBQSparkDataFactory.readData();
        Dataset<Car> carsDataset = carSparkDataFactory.readData();

        // Dataset<Car> joinedCarsDataset = joinCarsDataset(carsBQDataset, carsDataset);

        Dataset<PersonCar> peopleCarDataset = peopleDataset
                .join(
                        carsDataset,
                        peopleDataset.col("personId").equalTo(carsDataset.col("personId")),
                        "inner"
                )
                .drop(carsDataset.col("personId"))
                .orderBy(peopleDataset.col("personId"), carsDataset.col("carId"))
                .as(Encoders.bean(PersonCar.class));

        peopleCarDataset.show();

        return new ArrayList<>(peopleCarDataset.collectAsList());
    }

    @Override
    public PersonCar read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (personCarList == null) {
            personCarList = initializeList();
        }

        if (!personCarList.isEmpty()) {
            return personCarList.remove(0);
        }
        return null;
    }
}
