package com.example.javaeloadasbeadando;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import soapclient.MNBArfolyamServiceSoap;
import soapclient.MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage;
import soapclient.MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage;
import soapclient.MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage;
import soapclient.MNBArfolyamServiceSoapImpl;

@Controller
public class BankController {
    private final MNBArfolyamServiceSoap service;

    @Autowired
    public BankController(MNBArfolyamServiceSoapImpl impl) {
        this.service = impl.getCustomBindingMNBArfolyamServiceSoap();
    }

    @GetMapping("/feladat1")
    @ResponseBody
    public String feladat1() throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage,
            MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage,
            MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage {
        return service.getInfo() + "<br>" + service.getCurrentExchangeRates() + "<br>" +
                service.getExchangeRates("2022-08-14","2022-09-14","EUR");
    }
}
