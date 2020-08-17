package com.falabella.HabilitarPuntoDevolucion.services.Impl;

import com.falabella.HabilitarPuntoDevolucion.model.Rule;
import com.falabella.HabilitarPuntoDevolucion.services.ApiReglasService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiReglasServiceImpl implements ApiReglasService {

    @Value("${apireglas.findAllURL}")
    private String apireglasFindAll;

    @Override
    public List<Rule> getAllRules() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<List<Rule>> response = restTemplate.exchange(apireglasFindAll, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Rule>>() {});
            return response.getBody();
        } catch(Exception e) {
            return new ArrayList<>();
        }
    }
}
