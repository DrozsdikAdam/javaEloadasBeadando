package com.example.javaeloadasbeadando;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import soapclient.MessagePrice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class pageController {

    private final BankFunctions bankFunctions;

    @Autowired
    public pageController(BankFunctions bankFunctions) {
        this.bankFunctions = bankFunctions;
    }

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/soap")
    public String soap(Model model) {
        try {
            model.addAttribute("currencies", bankFunctions.getAvailableCurrencies());
        } catch (Exception e) {
            model.addAttribute("soapError", "Hiba történt a pénznemek lekérdezése során: " + e.getMessage());
        }
        model.addAttribute("messagePrice", new MessagePrice());
        return "soap";
    }

    @PostMapping("/soap")
    public String soap2(@ModelAttribute MessagePrice messagePrice, Model model) {
        try {
            List<ExchangeRateData> rates = bankFunctions.getExchangeRates(messagePrice.getStartDate(), messagePrice.getEndDate(), messagePrice.getCurrency());
            model.addAttribute("rates", rates);

            List<String> chartLabels = new ArrayList<>();
            List<Double> chartData = new ArrayList<>();

            // A kapott adatok fordított sorrendben vannak, ezért megfordítjuk őket.
            Collections.reverse(rates);

            for (ExchangeRateData rate : rates) {
                chartLabels.add(rate.getDate());
                // A rate.getRate() már a helyes Double értéket adja vissza.
                chartData.add(rate.getRate());
            }

            model.addAttribute("chartLabels", chartLabels);
            model.addAttribute("chartData", chartData);

            model.addAttribute("currencies", bankFunctions.getAvailableCurrencies()); // Re-add currencies for the form
        } catch (Exception e) {
            model.addAttribute("soapError", "Hiba történt a SOAP kérés során: " + e.getMessage());
            try {
                model.addAttribute("currencies", bankFunctions.getAvailableCurrencies()); // Try to get currencies even on error
            } catch (Exception ex) {
                // If this also fails, do nothing, the error is already set
            }
        }
        model.addAttribute("messagePrice", new MessagePrice()); // Re-add empty object for the form
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
