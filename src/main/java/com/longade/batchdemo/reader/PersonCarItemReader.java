package com.longade.batchdemo.reader;

import com.longade.batchdemo.factory.CarSparkDataFactory;
import com.longade.batchdemo.factory.PersonSparkDataFactory;
import com.longade.batchdemo.model.Car;
import com.longade.batchdemo.model.Person;
import com.longade.batchdemo.model.PersonCar;
import com.longade.batchdemo.util.ClassFieldsUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PersonCarItemReader implements ItemReader<PersonCar> {

    private List<PersonCar> personCarList;

    @Autowired
    private PersonSparkDataFactory personSparkDataFactory;

    @Autowired
    private CarSparkDataFactory carSparkDataFactory;

    private List<PersonCar> initializeList() {
        Dataset<Person> peopleDataset = personSparkDataFactory.readData();
        Dataset<Car> carsDataset = carSparkDataFactory.readData();

        Dataset<PersonCar> peopleCarDataset = peopleDataset
                .join(
                        carsDataset,
                        peopleDataset.col("personId").equalTo(carsDataset.col("personId")),
                        "inner"
                )
                // .select() --> to select desired columns
                .drop(carsDataset.col("personId"))
                // .withColumnsRenamed(ClassFieldsUtils.getFieldsMapping(PersonCar.class))
                .as(Encoders.bean(PersonCar.class));

        peopleCarDataset.show();

        return new ArrayList<>(peopleCarDataset
                // .select("*")
                .collectAsList());
                /*.stream()
                .map(row -> {
                    PersonCar personCar = new PersonCar();
                    personCar.setPersonId(new BigInteger(String.valueOf(row.get(0))));
                    personCar.setFirstName((String) row.get(1));
                    personCar.setLastName((String) row.get(2));
                    personCar.setCarId(new BigInteger(String.valueOf(row.get(3))));
                    personCar.setBrand((String) row.get(4));
                    personCar.setModel((String) row.get(5));
                    return personCar;
                })
                .collect(Collectors.toList());*/
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
