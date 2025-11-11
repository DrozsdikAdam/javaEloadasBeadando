package com.example.javaeloadasbeadando.dto;

public class MessageClosePosition {

    private String tradeId;

    // A Spring form binding miatt kellenek az üres get/set metódusok
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
