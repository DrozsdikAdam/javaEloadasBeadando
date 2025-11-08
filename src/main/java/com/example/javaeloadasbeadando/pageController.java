package com.example.javaeloadasbeadando;

import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.position.Position;
import com.oanda.v20.pricing.ClientPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import soapclient.MessagePrice;

import java.util.List;
import java.util.stream.Collectors;

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
            model.addAttribute("currencies", bankFunctions.getAvailableCurrencies());

            // Prepare data for the chart
            List<String> chartLabels = rates.stream().map(ExchangeRateData::getDate).collect(Collectors.toList());
            List<Double> chartData = rates.stream().map(ExchangeRateData::getRate).collect(Collectors.toList());
            model.addAttribute("chartLabels", chartLabels);
            model.addAttribute("chartData", chartData);

        } catch (Exception e) {
            model.addAttribute("soapError", "Hiba történt a SOAP kérés során: " + e.getMessage());
            try {
                model.addAttribute("currencies", bankFunctions.getAvailableCurrencies());
            } catch (Exception ex) {
                // If this also fails, do nothing, the error is already set
            }
        }
        model.addAttribute("messagePrice", messagePrice); // Re-add submitted object for the form
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
        model.addAttribute("instruments", tradeApplication.getTradableInstruments());
        model.addAttribute("granularities", tradeApplication.getAvailableGranularities());
        return "forex-histar";
    }

    @GetMapping("/forex/nyit")
    public String forexNyit(Model model) {
        model.addAttribute("instruments", tradeApplication.getTradableInstruments());
        return "forex-nyit";
    }

    @PostMapping("/forex/nyit")
    public String forexNyitPost(@RequestParam String instrument, @RequestParam Double units, RedirectAttributes redirectAttributes) {
        try {
            OrderCreateResponse response = tradeApplication.createMarketOrder(instrument, units);
            if (response != null && response.getOrderFillTransaction() != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Megbízás sikeresen elküldve! Tranzakció ID: " + response.getOrderFillTransaction().getId());
            } else if (response != null && response.getOrderCancelTransaction() != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sikertelen megbízás: " + response.getOrderCancelTransaction().getReason());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Ismeretlen hiba történt a megbízás feldolgozása során.");
            }
        } catch (RequestException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "API Hiba: " + e.getErrorMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Váratlan hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/forex/poz";
    }

    @GetMapping("/forex/poz")
    public String forexPoz(Model model) {
        List<Position> positions = tradeApplication.getOpenPositions();
        model.addAttribute("positions", positions);
        return "forex-poz";
    }

    @GetMapping("/forex/zar")
    public String forexZar() { return "forex-zar"; }
}