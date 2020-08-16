package com.falabella.HabilitarPuntoDevolucion.services;

import com.falabella.HabilitarPuntoDevolucion.model.Rule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface PuntosDevolucionService {
    ResponseEntity getPuntosDevolucion(HttpServletRequest httpServletRequest, Rule rule);
}
