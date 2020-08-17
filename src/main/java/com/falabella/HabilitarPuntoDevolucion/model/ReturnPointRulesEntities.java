package com.falabella.HabilitarPuntoDevolucion.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnPointRulesEntities {

    private String ruleType;
    private String pricing;
    private OperadorValor customer;
    private OperadorValor motive;
    private OperadorValor price;
    private OperadorValor size;
    private OperadorValor purchaseDate;
    private OperadorValor withdrawalDate;
    @JsonProperty("BusinessPrice")
    private BusinessPrice businessPrice;
    @JsonProperty("BusinessNC")
    private BusinessNC businessNC;
    @JsonProperty("BusinessLogic")
    private BusinessLogic businessLogic;

}
