package com.example.javaeloadasbeadando;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Ez a metódus minden hiba esetén a 404.html oldalt adja vissza.
        // A különböző hibakódok (404, 500, stb.) szerinti eltérő oldalak megjelenítéséhez
        // további logika lenne szükséges itt.
        return "404";
    }
}
