package com.oanda.v20;

// A projekt valós struktúrájának megfelelő, végleges importok
import com.oanda.v20.*;
import com.oanda.v20.Context;
import com.oanda.v20.account.AccountSummary;


import com.oanda.v20.account.AccountSummaryResponse;
import org.springframework.stereotype.Service;

@Service
public class TradeApplication {

    public AccountSummary getAccountSummary() {
        try {
            Context ctx = new Context(Config.URL, Config.TOKEN);
            // A summary() hívás egy AccountSummaryResponse objektumot ad vissza.
            AccountSummaryResponse response = ctx.account.summary(Config.ACCOUNTID);
            // Ezen az objektumon hívjuk a getAccount() metódust.
            return response.getAccount();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
