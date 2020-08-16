package com.falabella.HabilitarPuntoDevolucion.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessLogic {

    private OperadorValor storage;
    private OperadorValor politics;
    private OperadorValor deliveryMethod;
    private OperadorValor salesChannel;

}
