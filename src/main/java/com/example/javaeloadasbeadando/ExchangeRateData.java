package com.example.javaeloadasbeadando;

public class ExchangeRateData {
    private String date;
    private String rate;
    private String currency;

    public ExchangeRateData(String date, String rate, String currency) {
        this.date = date;
        this.rate = rate;
        this.currency = currency;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
