package com.example.javaeloadasbeadando;

import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.pricing.ClientPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import soapclient.MessagePrice;

import java.util.List;

@Controller
public class pageController {

    private final BankFunctions bankFunctions;
    private final tradeApplication tradeApplication;

    @Autowired
    public pageController(BankFunctions bankFunctions, tradeApplication tradeApplication) {
        this.bankFunctions = bankFunctions;
        this.tradeApplication = tradeApplication;
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
    public String forexAccount(Model model) {
        AccountSummary summary = tradeApplication.getAccountSummary();
        model.addAttribute("accountSummary", summary);
        return "forex-account";
    }

    @GetMapping("/forex/aktar")
    public String forexAktar(Model model) {
        model.addAttribute("instruments", tradeApplication.getTradableInstruments());
        return "forex-aktar";
    }

    @PostMapping("/forex/aktar")
    public String forexAktarPost(@RequestParam String instrument, Model model) {
        ClientPrice price = tradeApplication.getCurrentPrice(instrument);
        model.addAttribute("price", price);
        model.addAttribute("selectedInstrument", instrument);
        // Re-populate the instruments list for the dropdown
        model.addAttribute("instruments", tradeApplication.getTradableInstruments());
        return "forex-aktar";
    }

    @GetMapping("/forex/histar")
    public String forexHistar(Model model) {
        model.addAttribute("instruments", tradeApplication.getTradableInstruments());
        model.addAttribute("granularities", tradeApplication.getAvailableGranularities());
        return "forex-histar";
    }

    @PostMapping("/forex/histar")
    public String forexHistarPost(@RequestParam String instrument, @RequestParam CandlestickGranularity granularity, Model model) {
        List<Candlestick> candles = tradeApplication.getHistoricalCandles(instrument, granularity);
        model.addAttribute("candles", candles);
        model.addAttribute("selectedInstrument", instrument);
        model.addAttribute("selectedGranularity", granularity);
        // Re-populate the instruments and granularities lists for the dropdowns
        model.addAttribute("instruments", tradeApplication.getTradableInstruments());
        model.addAttribute("granularities", tradeApplication.getAvailableGranularities());
        return "forex-histar";
    }

    @GetMapping("/forex/nyit")
    public String forexNyit() { return "forex-nyit"; }

    @GetMapping("/forex/poz")
    public String forexPoz() { return "forex-poz"; }

    @GetMapping("/forex/zar")
    public String forexZar() { return "forex-zar"; }
}
