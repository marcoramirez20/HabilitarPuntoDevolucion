package com.falabella.HabilitarPuntoDevolucion.services;

import com.falabella.HabilitarPuntoDevolucion.model.PuntosDevolucion;
import com.falabella.HabilitarPuntoDevolucion.model.Rule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface PuntosDevolucionService {
    List<PuntosDevolucion> getPuntosDevolucion(HttpServletRequest httpServletRequest, Rule rule);
}
