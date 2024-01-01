package com.longade.batchdemo.steplet;

import com.longade.batchdemo.model.PersonValues;
import com.longade.batchdemo.model.PersonValuesSplit;
import com.longade.batchdemo.reader.PersonValuesItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class PersonValuesSteplet {

    @Autowired
    private PersonValuesItemReader personValuesItemReader;

    @Bean
    public ItemReader<PersonValuesSplit> reader() {
        return personValuesItemReader;
    }

    @Bean
    public ItemProcessor<PersonValuesSplit, PersonValuesSplit> processor() {
        return item -> item;
    }

    @Bean
    public FlatFileItemWriter<PersonValuesSplit> writer() {
        return new FlatFileItemWriterBuilder<PersonValuesSplit>()
                .name("itemWriter")
                .resource(new FileSystemResource("target/test_file_pv.csv"))
                .append(false)
                .delimited()
                .delimiter(";")
                .names("personId", "valId", "valuesSplitSep")
                .build();
    }

}
