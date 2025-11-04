package com.oanda.v20;

// A hibás import javítása a projektben lévő valós osztályra.
import com.oanda.v20.account.AccountID;

public class Config {
    private Config() {}
    public static final String URL = "https://api-fxpractice.oanda.com";
    public static final AccountID ACCOUNTID = new AccountID("<AccountID>");
    public static final String TOKEN = "<Token>";
}
