package com.vini.hackathon.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalUtils {

    private DecimalUtils() {
        // Do nothing. Construtor protegido para evitar instanciação de classe utilitária.
    }

    public static BigDecimal roundValue(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.DOWN);
    }

}
