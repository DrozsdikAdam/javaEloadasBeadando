package com.example.javaeloadasbeadando.dto;

public class ExchangeRateData {
    private String date;
    private Double rate;
    private String currency;

    public ExchangeRateData(String date, Double rate, String currency) {
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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
