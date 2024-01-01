package com.longade.batchdemo.reader;

import com.google.common.collect.Lists;
import com.longade.batchdemo.factory.PersonSparkDataFactory;
import com.longade.batchdemo.factory.PersonValuesSparkDataFactory;
import com.longade.batchdemo.model.Person;
import com.longade.batchdemo.model.PersonValues;
import com.longade.batchdemo.model.PersonValuesSplit;
import com.longade.batchdemo.model.tmp.TmpBean;
import com.longade.batchdemo.util.ClassFieldsUtils;
import com.longade.batchdemo.util.ListsUtils;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PersonValuesItemReader implements ItemReader<PersonValuesSplit> {

    private List<PersonValuesSplit> personValuesList;

    @Autowired
    private PersonSparkDataFactory personSparkDataFactory;

    @Autowired
    private PersonValuesSparkDataFactory personValuesSparkDataFactory;

    private List<PersonValuesSplit> initializeList() {
        Dataset<Person> peopleDataset = personSparkDataFactory.readData();
        Dataset<PersonValues> personValuesDataset = personValuesSparkDataFactory.readData();

        Dataset<Row> columnsToRowsDS = peopleDataset
                .join(
                        personValuesDataset,
                        peopleDataset.col("personId").equalTo(personValuesDataset.col("personId")),
                        "inner"
                )
                .drop(peopleDataset.col("personId"))
                .unpivot(
                        Arrays.asList(personValuesDataset.col("personId"), personValuesDataset.col("valId")).toArray(Column[]::new),
                        Arrays.asList(
                                personValuesDataset.col("val1"),
                                personValuesDataset.col("val2"),
                                personValuesDataset.col("val3"),
                                personValuesDataset.col("val4"),
                                personValuesDataset.col("val5"),
                                personValuesDataset.col("val6"),
                                personValuesDataset.col("val7"),
                                personValuesDataset.col("val8"),
                                personValuesDataset.col("val9"),
                                personValuesDataset.col("val10"),
                                personValuesDataset.col("val11"),
                                personValuesDataset.col("val12"),
                                personValuesDataset.col("val13"),
                                personValuesDataset.col("val14"),
                                personValuesDataset.col("val15"),
                                personValuesDataset.col("val16"),
                                personValuesDataset.col("val17"),
                                personValuesDataset.col("val18"),
                                personValuesDataset.col("val19"),
                                personValuesDataset.col("val20")
                        ).toArray(Column[]::new),
                        "key",
                        "value"
                )
                .where("value is not null");

        columnsToRowsDS.printSchema();
        columnsToRowsDS.show();

        StructType tmpStructType = new StructType();
        tmpStructType.add("personId", DataTypes.LongType);
        tmpStructType.add("valId", DataTypes.LongType);
        tmpStructType.add("values", DataTypes.createArrayType(DataTypes.createDecimalType(5, 2)));

        Dataset<TmpBean> rowToColumnsDS = columnsToRowsDS
                .groupBy(columnsToRowsDS.col("personId"), columnsToRowsDS.col("valId"))
                .agg(
                        // functions.concat_ws("|", functions.collect_list("value")).as("values")
                        functions.array_agg(functions.col("value")).as("values")
                )
                .map((MapFunction<Row, TmpBean>) row -> {
                    TmpBean tmpBean = new TmpBean();
                    tmpBean.setPersonId((Long) row.get(0));
                    tmpBean.setValId((Long) row.get(1));
                    // List<List<BigDecimal>> values = Lists.partition(row.getList(2), 5); -> with google guava
                    List<List<BigDecimal>> values = ListsUtils.partitionBasedOnSize(row.getList(2), 5);
                    tmpBean.setValues(values);
                    return tmpBean;
                }, Encoders.bean(TmpBean.class));

        rowToColumnsDS.printSchema();
        rowToColumnsDS.show(false);

        Dataset<PersonValuesSplit> rowToColumnsSplitDS = rowToColumnsDS
                .groupBy(functions.col("personId"), functions.col("valId"), functions.col("values"))
                .agg(
                        functions.explode(functions.col("values")).as("values_split")
                )
                .drop(functions.col("values"))
                .groupBy(functions.col("personId"), functions.col("valId"), functions.col("values_split"))
                .agg(
                        functions.concat_ws("|", functions.col("values_split")).as("values_split_sep")
                )
                .drop(functions.col("values_split"))
                .orderBy(functions.col("personId"))
                .withColumnsRenamed(ClassFieldsUtils.getFieldsMapping(PersonValuesSplit.class))
                .as(Encoders.bean(PersonValuesSplit.class));

        rowToColumnsSplitDS.printSchema();
        rowToColumnsSplitDS.show(false);

        return new ArrayList<>(rowToColumnsSplitDS.collectAsList());
    }

    @Override
    public PersonValuesSplit read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (personValuesList == null) {
            personValuesList = initializeList();
        }

        if (!personValuesList.isEmpty()) {
            return personValuesList.remove(0);
        }
        return null;
    }
}
