package com.example.javaeloadasbeadando;

import com.oanda.v20.account.AccountID;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oanda.api")
public class Config {

    private String url;
    private String token;
    private String accountid;

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountID getAccountid() {
        return new AccountID(accountid);
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }
}
