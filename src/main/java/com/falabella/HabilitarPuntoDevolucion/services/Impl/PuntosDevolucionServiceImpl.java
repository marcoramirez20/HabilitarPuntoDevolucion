package com.falabella.HabilitarPuntoDevolucion.services.Impl;

import com.falabella.HabilitarPuntoDevolucion.model.OperadorValor;
import com.falabella.HabilitarPuntoDevolucion.model.PuntosDevolucion;
import com.falabella.HabilitarPuntoDevolucion.model.Rule;
import com.falabella.HabilitarPuntoDevolucion.services.ApiReglasService;
import com.falabella.HabilitarPuntoDevolucion.services.PuntosDevolucionService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PuntosDevolucionServiceImpl implements PuntosDevolucionService {

    @Autowired
    private ApiReglasService apiReglasService;

    @Value("${ruleType}")
    private String ruleType;

    @Override
    public List<PuntosDevolucion> getPuntosDevolucion(HttpServletRequest httpServletRequest, Rule rule) {
        List<Rule> lista = apiReglasService.getAllRules();
        Map<String, List<Rule>> agrupadas = new HashMap<>();
        List<PuntosDevolucion> puntosDevolucions;
        lista.forEach((x) -> {
            if(ruleType.equalsIgnoreCase(x.getReturnPointRulesEntities().getRuleType())){
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
                boolean cumpleRegla = cumpleRegla(z.getReturnPointRulesEntities().getCustomer(), rule.getReturnPointRulesEntities().getCustomer())
                        && cumpleRegla(z.getReturnPointRulesEntities().getMotive(), rule.getReturnPointRulesEntities().getMotive())
                        && cumpleRegla(z.getReturnPointRulesEntities().getPrice(), rule.getReturnPointRulesEntities().getPrice())
                        && cumpleRegla(z.getReturnPointRulesEntities().getPurchaseDate(), rule.getReturnPointRulesEntities().getPurchaseDate())
                        && cumpleRegla(z.getReturnPointRulesEntities().getSize(), rule.getReturnPointRulesEntities().getSize())
                        && cumpleRegla(z.getReturnPointRulesEntities().getWithdrawalDate(), rule.getReturnPointRulesEntities().getWithdrawalDate())

                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessLogic().getDeliveryMethod(),
                        rule.getReturnPointRulesEntities().getBusinessLogic().getDeliveryMethod())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessLogic().getPolitics(),
                        rule.getReturnPointRulesEntities().getBusinessLogic().getPolitics())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessLogic().getSalesChannel(),
                        rule.getReturnPointRulesEntities().getBusinessLogic().getSalesChannel())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessLogic().getStorage(),
                        rule.getReturnPointRulesEntities().getBusinessLogic().getStorage())

                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessNC().getHierarchy(),
                        rule.getReturnPointRulesEntities().getBusinessNC().getHierarchy())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessNC().getStatus(),
                        rule.getReturnPointRulesEntities().getBusinessNC().getStatus())

                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessPrice().getCost(),
                        rule.getReturnPointRulesEntities().getBusinessPrice().getCost())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessPrice().getMethod(),
                        rule.getReturnPointRulesEntities().getBusinessPrice().getMethod())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessPrice().getOutcome(),
                        rule.getReturnPointRulesEntities().getBusinessPrice().getOutcome())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessPrice().getProductType(),
                        rule.getReturnPointRulesEntities().getBusinessPrice().getProductType())
                        && cumpleRegla(z.getReturnPointRulesEntities().getBusinessPrice().getTimeBand(),
                        rule.getReturnPointRulesEntities().getBusinessPrice().getTimeBand())
                        ;
                habilitado = habilitado || !cumpleRegla;
            }
            puntosDevolucions.add(new PuntosDevolucion(x, habilitado));
        });
        return puntosDevolucions;
    }

    private boolean cumpleRegla(OperadorValor operadorRegla, OperadorValor operadorAEvaluar){
        if(operadorRegla == null || operadorRegla.getOper() == null || operadorRegla.getOper().trim().isEmpty()
                || operadorRegla.getValue() == null || operadorRegla.getValue().trim().isEmpty()
                || operadorAEvaluar == null || operadorAEvaluar.getValue() == null
                || operadorAEvaluar.getValue().trim().isEmpty()){
            return true;
        }
        switch(operadorRegla.getOper()){
            case "=":
                return operadorRegla.getValue().equalsIgnoreCase(operadorAEvaluar.getValue());
            case "!=":
                return !operadorRegla.getValue().equalsIgnoreCase(operadorAEvaluar.getValue());
            default:
                try{
                    Double valorRegla = Double.parseDouble(operadorRegla.getValue());
                    Double valorAEvaluar = Double.parseDouble(operadorAEvaluar.getValue());
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
