package com.lashkevich.bank.entity;

import java.math.BigDecimal;

public enum Currency {

    USD(new BigDecimal("1")),
    BYN(new BigDecimal("0.32")),
    RUB(new BigDecimal("0.0115"));

    private final BigDecimal rateToDollar;

    Currency(BigDecimal rateToDollar) {
        this.rateToDollar = rateToDollar;
    }

    public BigDecimal getRateToDollar() {
        return rateToDollar;
    }

}
