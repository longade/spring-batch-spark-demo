package com.longade.batchdemo.reader;

import com.longade.batchdemo.factory.CarSparkDataFactory;
import com.longade.batchdemo.factory.PersonSparkDataFactory;
import com.longade.batchdemo.model.PersonCar;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonItemReader implements ItemReader<PersonCar> {

    private static int index = 0;

    @Autowired
    private PersonSparkDataFactory personSparkDataFactory;

    @Autowired
    private CarSparkDataFactory carSparkDataFactory;

    @Override
    public PersonCar read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Dataset<Row> peopleDataset = personSparkDataFactory.readData();
        Dataset<Row> carsDataset = carSparkDataFactory.readData();

        Dataset<Row> peopleCarDataset = peopleDataset
                .join(
                        carsDataset,
                        peopleDataset.col("person_id").equalTo(carsDataset.col("person_id")),
                        "inner"
                );

        List<PersonCar> personCarList = peopleCarDataset
                // .select("*")
                .collectAsList()
                .stream()
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
                .collect(Collectors.toList());

        PersonCar c = personCarList.get(index);
        index++;
        if(index == personCarList.size())
            return null;
        return c;
    }
}
