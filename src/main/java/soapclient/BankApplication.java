package soapclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import soapclient.*;


@SpringBootApplication
@Controller
public class BankApplication {
    private final MNBArfolyamServiceSoap service;

    @Autowired
    public BankApplication(MNBArfolyamServiceSoapImpl impl) {
        this.service = impl.getCustomBindingMNBArfolyamServiceSoap();
    }

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

    @GetMapping("/feladat1")
    @ResponseBody
    public String kiir1() throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage,
            MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage,
            MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage {
        return service.getInfo() + "<br>" + service.getCurrentExchangeRates() + "<br>" +
                service.getExchangeRates("2022-08-14","2022-09-14","EUR");
    }

}
