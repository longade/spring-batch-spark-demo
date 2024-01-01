package com.longade.batchdemo.model.tmp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TmpBean {
    private Long personId;
    private Long valId;
    private List<List<BigDecimal>> values = new ArrayList<>();

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getValId() {
        return valId;
    }

    public void setValId(Long valId) {
        this.valId = valId;
    }

    public List<List<BigDecimal>> getValues() {
        return values;
    }

    public void setValues(List<List<BigDecimal>> values) {
        this.values = values;
    }
}