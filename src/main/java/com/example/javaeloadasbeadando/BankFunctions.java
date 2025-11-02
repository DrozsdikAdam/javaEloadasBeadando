package com.example.javaeloadasbeadando;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import soapclient.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankFunctions {

    private final MNBArfolyamServiceSoap service = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();

    public List<String> getAvailableCurrencies() throws Exception {
        String xmlString = service.getCurrencies();
        List<String> currencies = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlString));
        Document doc = builder.parse(is);

        NodeList currNodes = doc.getElementsByTagName("Curr");

        for (int i = 0; i < currNodes.getLength(); i++) {
            currencies.add(currNodes.item(i).getTextContent());
        }
        // HUF-ra nincs értelme árfolyamot lekérni, ezért eltávolítjuk a listából.
        currencies.remove("HUF");
        return currencies;
    }

    public List<ExchangeRateData> getExchangeRates(String startDate, String endDate, String currency) throws Exception {
        String xmlString = service.getExchangeRates(startDate, endDate, currency);
        List<ExchangeRateData> rates = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlString));
        Document doc = builder.parse(is);

        NodeList dayNodes = doc.getElementsByTagName("Day");

        for (int i = 0; i < dayNodes.getLength(); i++) {
            Element dayElement = (Element) dayNodes.item(i);
            String date = dayElement.getAttribute("date");

            NodeList rateNodes = dayElement.getElementsByTagName("Rate");
            if (rateNodes.getLength() > 0) {
                Element rateElement = (Element) rateNodes.item(0);
                String rateValue = rateElement.getTextContent();
                String curr = rateElement.getAttribute("curr");
                rates.add(new ExchangeRateData(date, rateValue, curr));
            }
        }
        return rates;
    }
}
