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
public class OperadorValor {

    @JsonProperty("OPER")
    private String oper;
    @JsonProperty("VALOR")
    private String valor;

}
