package com.example.javaeloadasbeadando;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class pageController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "404";
    }

    @GetMapping("/")
    public String index() { return "index"; }

    @GetMapping("/soap")
    public String soap() { return "soap"; }

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
