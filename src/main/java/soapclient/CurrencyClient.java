package soapclient;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Service
public class CurrencyClient {

    private final WebServiceTemplate webServiceTemplate;
    private final String soapEndpoint = "http://www.mnb.hu/arfolyamok.asmx";

    public CurrencyClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public GetCurrenciesResponseBody getCurrencies() {
        GetCurrenciesRequestBody request = new GetCurrenciesRequestBody();

        return (GetCurrenciesResponseBody) webServiceTemplate.marshalSendAndReceive(
                soapEndpoint,
                request,
                new SoapActionCallback("http://www.mnb.hu/webservices/GetCurrencies")
        );
    }
}