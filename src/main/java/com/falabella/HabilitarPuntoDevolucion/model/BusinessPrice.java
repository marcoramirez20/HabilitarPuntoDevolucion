package com.falabella.HabilitarPuntoDevolucion.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessPrice {

    private OperadorValor cost;
    private OperadorValor method;
    private OperadorValor productType;
    private OperadorValor timeBand;
    private OperadorValor outcome;

}
