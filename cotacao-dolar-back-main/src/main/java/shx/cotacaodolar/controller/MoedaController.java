package shx.cotacaodolar.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import shx.cotacaodolar.model.Moeda;
import shx.cotacaodolar.service.MoedaService;


@RestController
@RequestMapping(value = "/")
public class MoedaController {

    @Autowired
    private MoedaService moedaService;

    @GetMapping("/moeda/{data1}&{data2}&{currencyCode}")
    public List<Moeda> getCotacoesPeriodo(@PathVariable("data1") String startDate, @PathVariable("data2") String endDate, @PathVariable("currencyCode") String currencyCode) throws IOException, MalformedURLException, ParseException{
        return moedaService.getCotacoesPeriodo(startDate, endDate,currencyCode);
    }
    @GetMapping("/moeda/atual/{currencyCode}")
    public Double getCotacaoAtual(@PathVariable("currencyCode") String currencyCode) throws IOException, MalformedURLException, ParseException{
         return moedaService.getCotacaoAtual(currencyCode);
    }
    @GetMapping("/moeda/cotacoesMenoresAtual/{data1}&{data2}&{currencyCode}")
    public List<Moeda> getCotacoesMenoresAtual(@PathVariable("data1") String startDate, @PathVariable("data2") String endDate, @PathVariable("currencyCode") String currencyCode) throws IOException, MalformedURLException, ParseException{
        return moedaService.getCotacoesMenoresAtual(startDate, endDate,currencyCode);
    }

}
