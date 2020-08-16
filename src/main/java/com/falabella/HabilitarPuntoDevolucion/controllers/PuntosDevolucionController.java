package com.falabella.HabilitarPuntoDevolucion.controllers;

import com.falabella.HabilitarPuntoDevolucion.model.Rule;
import com.falabella.HabilitarPuntoDevolucion.services.PuntosDevolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/puntos-devolucion")
@RestController
public class PuntosDevolucionController {

    @Autowired
    private PuntosDevolucionService puntosDevolucionService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/getPuntosDevolucion")
    public ResponseEntity getPuntosDevolucion(HttpServletRequest httpServletRequest,
                                              @RequestBody Rule rule) {
        return puntosDevolucionService.getPuntosDevolucion(httpServletRequest, rule);
    }

}
