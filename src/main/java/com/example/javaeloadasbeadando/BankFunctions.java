package com.example.javaeloadasbeadando;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soapclient.*;

@Service
public class BankFunctions {

    private final MNBArfolyamServiceSoap service;

    @Autowired
    public BankFunctions(MNBArfolyamServiceSoapImpl impl) {
        this.service = impl.getCustomBindingMNBArfolyamServiceSoap();
    }

    public String getMnbData() throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage,
            MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage,
            MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage {
        return service.getInfo() + "<br>" + service.getCurrentExchangeRates() + "<br>" +
                service.getExchangeRates("2022-08-14","2022-09-14","EUR");
    }
}
