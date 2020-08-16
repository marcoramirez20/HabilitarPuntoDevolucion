package com.falabella.HabilitarPuntoDevolucion.services.Impl;

import com.falabella.HabilitarPuntoDevolucion.model.OperadorValor;
import com.falabella.HabilitarPuntoDevolucion.model.PuntosDevolucion;
import com.falabella.HabilitarPuntoDevolucion.model.ReturnPointRules;
import com.falabella.HabilitarPuntoDevolucion.model.Rule;
import com.falabella.HabilitarPuntoDevolucion.services.PuntosDevolucionService;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PuntosDevolucionServiceImpl implements PuntosDevolucionService {

    private static final String ruleType = "1";

    @Override
    public ResponseEntity getPuntosDevolucion(HttpServletRequest httpServletRequest, Rule rule) {
        //TODO aqui va el llamado al servicio de las reglas
        List<Rule> lista = llenarLista();
        Map<String, List<Rule>> agrupadas = new HashMap<>();
        List<PuntosDevolucion> puntosDevolucions;
        lista.forEach((x) -> {
            if(ruleType.equalsIgnoreCase(x.getReturnPointRules().getRuleType())){
                if(agrupadas.containsKey(x.getReturnPoint())){
                    agrupadas.get(x.getReturnPoint()).add(x);
                } else {
                    List<Rule> nuevaLista = new ArrayList<>();
                    nuevaLista.add(x);
                    agrupadas.put(x.getReturnPoint(), nuevaLista);
                }
            }
        });
        puntosDevolucions = new ArrayList<>(agrupadas.size());
        agrupadas.forEach((x, y) -> {
            boolean habilitado = false;
            for(Rule z : y){
                boolean cumpleRegla = cumpleRegla(z.getReturnPointRules().getCustomer(), rule.getReturnPointRules().getCustomer())
                        && cumpleRegla(z.getReturnPointRules().getMotive(), rule.getReturnPointRules().getMotive())
                        && cumpleRegla(z.getReturnPointRules().getPrice(), rule.getReturnPointRules().getPrice())
                        && cumpleRegla(z.getReturnPointRules().getPurchaseDate(), rule.getReturnPointRules().getPurchaseDate())
                        && cumpleRegla(z.getReturnPointRules().getSize(), rule.getReturnPointRules().getSize())
                        && cumpleRegla(z.getReturnPointRules().getWithdrawalDate(), rule.getReturnPointRules().getWithdrawalDate())
                        && cumpleRegla(z.getReturnPointRules().getBusinessLogic().getDeliveryMethod(),
                        rule.getReturnPointRules().getBusinessLogic().getDeliveryMethod())
                        && cumpleRegla(z.getReturnPointRules().getBusinessLogic().getPolitics(),
                        rule.getReturnPointRules().getBusinessLogic().getPolitics())
                        && cumpleRegla(z.getReturnPointRules().getBusinessLogic().getSalesChannel(),
                        rule.getReturnPointRules().getBusinessLogic().getSalesChannel())
                        && cumpleRegla(z.getReturnPointRules().getBusinessLogic().getStorage(),
                        rule.getReturnPointRules().getBusinessLogic().getStorage());
                habilitado = habilitado || !cumpleRegla;
            }
        });
        return null;
    }

    private boolean cumpleRegla(OperadorValor operadorRegla, OperadorValor operadorAEvaluar){
        if(operadorRegla == null || operadorRegla.getOper() == null || operadorRegla.getOper().trim().isEmpty()
                || operadorRegla.getValor() == null || operadorRegla.getValor().trim().isEmpty()
                || operadorAEvaluar == null || operadorAEvaluar.getOper() == null
                || operadorAEvaluar.getOper().trim().isEmpty() || operadorAEvaluar.getValor() == null
                || operadorAEvaluar.getValor().trim().isEmpty()){
            return true;
        }
        switch(operadorRegla.getOper()){
            case "=":
                return operadorRegla.getValor().equalsIgnoreCase(operadorAEvaluar.getValor());
            case "!=":
                return !operadorRegla.getValor().equalsIgnoreCase(operadorAEvaluar.getValor());
            default:
                try{
                    Double valorRegla = Double.parseDouble(operadorRegla.getValor());
                    Double valorAEvaluar = Double.parseDouble(operadorAEvaluar.getValor());
                    switch(operadorRegla.getOper()){
                        case ">":
                            return valorAEvaluar > valorRegla;
                        case ">=":
                            return valorAEvaluar >= valorRegla;
                        case "<":
                            return valorAEvaluar < valorRegla;
                        case "<=":
                            return valorAEvaluar <= valorRegla;
                        default:
                            return true;
                    }
                } catch (Exception e) {
                    return true;
                }
        }
    }

    //TODO mientras se integra el servicio de las reglas
    private List<Rule> llenarLista(){
        String regla1 = "{\n" +
                "            \"returnPoint\": \"Retiro\",\n" +
                "            \"returnPointRules\":{               \n" +
                "                \"ruleType\" : \"1\",  \n" +
                "                \"pricing\" :\"TRUE\",\n" +
                "                \"customer\" : { \"OPER\" : \"=\", \"VALOR\" : \"Hola\" },\n" +
                "                \"motive\" :{ \"OPER\" : \"!=\", \"VALOR\" : \"Prueba\" },\n" +
                "                \"price\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"size\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"purchaseDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"withdrawalDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"businessLogic\":\n" +
                "                {                         \n" +
                "                    \"storage\" :  { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"politics\" :{ \"OPER\" : \"\", \"VALOR\" : \"\"},\n" +
                "                    \"deliveryMethod\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"salesChannel\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                },\n" +
                "                \"businessPrice\":\n" +
                "                {\n" +
                "                    \"cost\": { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"method\":{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"productType\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"timeBand\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"outcome\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                }\n" +
                "            }\n" +
                "        }";

        String regla2 = "{\n" +
                "            \"returnPoint\": \"Retiro\",\n" +
                "            \"returnPointRules\":{               \n" +
                "                \"ruleType\" : \"1\",  \n" +
                "                \"pricing\" :\"TRUE\",\n" +
                "                \"customer\" : { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"motive\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"price\" :{ \"OPER\" : \">=\", \"VALOR\" : \"20000\" },\n" +
                "                \"size\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"purchaseDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"withdrawalDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"businessLogic\":\n" +
                "                {                         \n" +
                "                    \"storage\" :  { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"politics\" :{ \"OPER\" : \"\", \"VALOR\" : \"\"},\n" +
                "                    \"deliveryMethod\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"salesChannel\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                },\n" +
                "                \"businessPrice\":\n" +
                "                {\n" +
                "                    \"cost\": { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"method\":{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"productType\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"timeBand\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"outcome\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                }\n" +
                "            }\n" +
                "        }";

        String regla3 = "{\n" +
                "            \"returnPoint\": \"Tienda Falabella\",\n" +
                "            \"returnPointRules\":{               \n" +
                "                \"ruleType\" : \"1\",  \n" +
                "                \"pricing\" :\"TRUE\",\n" +
                "                \"customer\" : { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"motive\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"price\" :{ \"OPER\" : \"<\", \"VALOR\" : \"10000\" },\n" +
                "                \"size\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"purchaseDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"withdrawalDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"businessLogic\":\n" +
                "                {                         \n" +
                "                    \"storage\" :  { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"politics\" :{ \"OPER\" : \"\", \"VALOR\" : \"\"},\n" +
                "                    \"deliveryMethod\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"salesChannel\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                },\n" +
                "                \"businessPrice\":\n" +
                "                {\n" +
                "                    \"cost\": { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"method\":{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"productType\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"timeBand\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"outcome\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                }\n" +
                "            }\n" +
                "        }";

        String regla4 = "{\n" +
                "            \"returnPoint\": \"Tienda Falabella\",\n" +
                "            \"returnPointRules\":{               \n" +
                "                \"ruleType\" : \"1\",  \n" +
                "                \"pricing\" :\"TRUE\",\n" +
                "                \"customer\" : { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"motive\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"price\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"size\" :{ \"OPER\" : \"=\", \"VALOR\" : \"mediano\" },\n" +
                "                \"purchaseDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"withdrawalDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"businessLogic\":\n" +
                "                {                         \n" +
                "                    \"storage\" :  { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"politics\" :{ \"OPER\" : \"\", \"VALOR\" : \"\"},\n" +
                "                    \"deliveryMethod\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"salesChannel\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                },\n" +
                "                \"businessPrice\":\n" +
                "                {\n" +
                "                    \"cost\": { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"method\":{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"productType\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"timeBand\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"outcome\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                }\n" +
                "            }\n" +
                "        }";

        String regla5 = "{\n" +
                "            \"returnPoint\": \"Tienda Falabella\",\n" +
                "            \"returnPointRules\":{               \n" +
                "                \"ruleType\" : \"2\",  \n" +
                "                \"pricing\" :\"TRUE\",\n" +
                "                \"customer\" : { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"motive\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"price\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"size\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"purchaseDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"withdrawalDate\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                \"businessLogic\":\n" +
                "                {                         \n" +
                "                    \"storage\" :  { \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"politics\" :{ \"OPER\" : \"\", \"VALOR\" : \"\"},\n" +
                "                    \"deliveryMethod\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"salesChannel\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                },\n" +
                "                \"businessPrice\":\n" +
                "                {\n" +
                "                    \"cost\": { \"OPER\" : \"<\", \"VALOR\" : \"5000\" },\n" +
                "                    \"method\":{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"productType\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"timeBand\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" },\n" +
                "                    \"outcome\" :{ \"OPER\" : \"\", \"VALOR\" : \"\" }\n" +
                "                }\n" +
                "            }\n" +
                "        }";
        ArrayList<Rule> lista = new ArrayList<>(5);
        lista.add(new Gson().fromJson(regla1, Rule.class));
        lista.add(new Gson().fromJson(regla2, Rule.class));
        lista.add(new Gson().fromJson(regla3, Rule.class));
        lista.add(new Gson().fromJson(regla4, Rule.class));
        lista.add(new Gson().fromJson(regla5, Rule.class));
        return lista;
    }

}
