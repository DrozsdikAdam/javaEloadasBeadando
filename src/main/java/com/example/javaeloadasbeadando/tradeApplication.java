package com.example.javaeloadasbeadando;

import com.oanda.v20.Context;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
// Az osztály nevét a fájlnévhez igazítottam (tradeApplication.java -> public class tradeApplication)
public class tradeApplication {

    @Value("${oanda.url}")
    private String url;

    @Value("${oanda.token}")
    private String token;

    @Value("${oanda.accountid}")
    private String accountId;

    public AccountSummary getAccountSummary() {
        try {
            Context ctx = new Context(url, token);
            AccountID accountID = new AccountID(accountId);
            return ctx.account.summary(accountID).getAccount();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
