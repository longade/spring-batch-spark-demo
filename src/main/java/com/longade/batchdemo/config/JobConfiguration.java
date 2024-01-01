package com.longade.batchdemo.config;

import com.longade.batchdemo.model.PersonCar;
import com.longade.batchdemo.model.PersonValues;
import com.longade.batchdemo.model.PersonValuesSplit;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /*@Bean
    public Job storePeopleJob(Step step1) {
        return this.jobBuilderFactory.get("storePeopleJob")
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(ItemReader<PersonCar> reader, ItemProcessor<PersonCar, PersonCar> processor, FlatFileItemWriter<PersonCar> writer) {
        return this.stepBuilderFactory
                .get("step1")
                .<PersonCar, PersonCar> chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }*/

    @Bean
    public Job personValuesJob(Step step2) {
        return this.jobBuilderFactory.get("personValuesJob")
                .start(step2)
                .build();
    }

    @Bean
    public Step step2(ItemReader<PersonValuesSplit> reader, ItemProcessor<PersonValuesSplit, PersonValuesSplit> processor, FlatFileItemWriter<PersonValuesSplit> writer) {
        return this.stepBuilderFactory
                .get("step2")
                .<PersonValuesSplit, PersonValuesSplit> chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
