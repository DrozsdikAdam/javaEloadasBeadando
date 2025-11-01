package com.example.javaeloadasbeadando;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import soapclient.MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage;
import soapclient.MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage;
import soapclient.MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage;

@Controller
public class pageController implements ErrorController {

    private final BankFunctions bankFunctions;

    @Autowired
    public pageController(BankFunctions bankFunctions) {
        this.bankFunctions = bankFunctions;
    }

    @RequestMapping("/error")
    public String handleError() {
        return "404";
    }

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/soap")
    public String soap(Model model) throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage, MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage, MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage {
        model.addAttribute("feladat1Result", bankFunctions.getMnbData());
        return "soap";
    }

    @GetMapping("/forex/account")
    public String forexAccount() { return "forex-account"; }

    @GetMapping("/forex/aktar")
    public String forexAktar() { return "forex-aktar"; }

    @GetMapping("/forex/histar")
    public String forexHistar() { return "forex-histar"; }

    @GetMapping("/forex/nyit")
    public String forexNyit() { return "forex-nyit"; }

    @GetMapping("/forex/poz")
    public String forexPoz() { return "forex-poz"; }

    @GetMapping("/forex/zar")
    public String forexZar() { return "forex-zar"; }
}
