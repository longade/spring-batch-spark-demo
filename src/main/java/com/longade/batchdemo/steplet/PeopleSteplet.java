package com.longade.batchdemo.steplet;

import com.longade.batchdemo.model.Person;
import com.longade.batchdemo.model.PersonCar;
import com.longade.batchdemo.reader.PersonItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class PeopleSteplet {

    @Autowired
    private DataSource dataSource;

    /*@Bean
    public JdbcCursorItemReader<Person> reader() {
        return new JdbcCursorItemReaderBuilder<Person>()
                .name("itemReader")
                .sql("select * from people")
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Person.class))
                .build();
    }*/

    @Autowired
    private PersonItemReader personItemReader;

    @Bean
    public ItemReader<PersonCar> reader() {
        return personItemReader;
    }

    @Bean
    public ItemProcessor<PersonCar, PersonCar> processor() {
        return item -> item;
    }

    @Bean
    public FlatFileItemWriter<PersonCar> writer() {
        return new FlatFileItemWriterBuilder<PersonCar>()
                .name("itemWriter")
                .resource(new FileSystemResource("target/test_file.csv"))
                .append(false)
                .delimited()
                .delimiter(",")
                .names("personId", "firstName", "lastName", "carId", "brand", "model")
                .build();
    }

}
