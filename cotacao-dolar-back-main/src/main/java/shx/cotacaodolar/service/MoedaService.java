package shx.cotacaodolar.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import shx.cotacaodolar.model.Moeda;
import shx.cotacaodolar.model.Periodo;



@Service
public class MoedaService {

    // o formato da data que o método recebe é "MM-dd-yyyy"
    public List<Moeda> getCotacoesPeriodo(String startDate, String endDate) throws IOException, MalformedURLException, ParseException{
        Periodo periodo = new Periodo(startDate, endDate);

        String urlString = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarPeriodo(dataInicial=@dataInicial,dataFinalCotacao=@dataFinalCotacao)?%40dataInicial='" + periodo.getDataInicial() + "'&%40dataFinalCotacao='" + periodo.getDataFinal() + "'&%24format=json&%24skip=0&%24top=" + periodo.getDiasEntreAsDatasMaisUm();

        URL url = new URL(urlString);
        HttpURLConnection request = (HttpURLConnection)url.openConnection();
        request.connect();

        JsonElement response = JsonParser.parseReader(new InputStreamReader((InputStream)request.getContent()));
        JsonObject rootObj = response.getAsJsonObject();
        JsonArray cotacoesArray = rootObj.getAsJsonArray("value");

        List<Moeda> moedasLista = new ArrayList<Moeda>();

        for(JsonElement obj : cotacoesArray){
            Moeda moedaRef = new Moeda();
            Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getAsJsonObject().get("dataHoraCotacao").getAsString());

            moedaRef.preco = obj.getAsJsonObject().get("cotacaoCompra").getAsDouble();
            moedaRef.data = new SimpleDateFormat("dd/MM/yyyy").format(data);
            moedaRef.hora = new SimpleDateFormat("HH:mm:ss").format(data);
            moedasLista.add(moedaRef);
        }
        return moedasLista;
    }

    public List<Moeda> getCotacoesMenoresAtual(String startDate, String endDate) throws IOException, MalformedURLException, ParseException{
        List<Moeda> listaMoedas = getCotacoesPeriodo(startDate,endDate);
        double cotacaoAtual = getCotacaoAtual();

        for (int i = 0; i < listaMoedas.size(); i++) {
            Moeda moeda = listaMoedas.get(i);
            if (moeda.preco > cotacaoAtual) {
                listaMoedas.remove(i);
                i--;  // Atualizar o índice após a remoção
            }
        }
        return listaMoedas;
    }

    public Double getCotacaoAtual() throws IOException, ParseException{

        String dataFormatada =  new SimpleDateFormat("MM-dd-yyyy").format(new Date());

        JsonArray cotacoesArray = getJsonCotaPelaData(dataFormatada);

        if (cotacoesArray.isEmpty()){
            //caso não encontre valor para o dia atual, o sistema irá buscar a cotação do dia anterior
            Date dataAtual = new Date();
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(dataAtual);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date diaAnterior = calendar.getTime();

            String dataDiaAnteriorFormatada =  new SimpleDateFormat("MM-dd-yyyy").format(diaAnterior);

            cotacoesArray = getJsonCotaPelaData(dataDiaAnteriorFormatada);
        }

        Moeda moeda = new Moeda();

        for(JsonElement obj : cotacoesArray){
            String dateString = obj.getAsJsonObject().get("dataHoraCotacao").getAsString();
            Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);

            moeda.data = new SimpleDateFormat("dd/MM/yyyy").format(data);
            moeda.hora = new SimpleDateFormat("HH:mm:ss").format(data);
            moeda.preco = obj.getAsJsonObject().get("cotacaoCompra").getAsDouble();
        }

        return moeda.preco;
    }

    private JsonArray getJsonCotaPelaData(String data) throws IOException{
        String urlString = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='"+ data +"'&$top=100&$format=json";

        URL url = new URL(urlString);
        HttpURLConnection request = (HttpURLConnection)url.openConnection();
        request.connect();

        JsonElement response = JsonParser.parseReader(new InputStreamReader((InputStream)request.getContent()));
        JsonObject rootObj = response.getAsJsonObject();
        return rootObj.getAsJsonArray("value");
    }
}
