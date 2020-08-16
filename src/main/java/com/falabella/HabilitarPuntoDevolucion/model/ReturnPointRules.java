package com.falabella.HabilitarPuntoDevolucion.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnPointRules {

    private String ruleType;
    private String pricing;
    private OperadorValor customer;
    private OperadorValor motive;
    private OperadorValor price;
    private OperadorValor size;
    private OperadorValor purchaseDate;
    private OperadorValor withdrawalDate;
    private BusinessLogic businessLogic;
    private BusinessPrice businessPrice;

}
