package com.falabella.HabilitarPuntoDevolucion.services;

import com.falabella.HabilitarPuntoDevolucion.model.Rule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiReglasService {
    List<Rule> getAllRules();
}
