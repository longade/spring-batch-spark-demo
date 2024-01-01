package com.longade.batchdemo.model;

import java.math.BigInteger;

public class PersonValuesSplit {

    private BigInteger personId;
    private BigInteger valId;
    private String valuesSplitSep;

    public BigInteger getPersonId() {
        return personId;
    }

    public void setPersonId(BigInteger personId) {
        this.personId = personId;
    }

    public BigInteger getValId() {
        return valId;
    }

    public void setValId(BigInteger valId) {
        this.valId = valId;
    }

    public String getValuesSplitSep() {
        return valuesSplitSep;
    }

    public void setValuesSplitSep(String valuesSplitSep) {
        this.valuesSplitSep = valuesSplitSep;
    }
}
