package com.example.javaeloadasbeadando;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class pageController {
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
