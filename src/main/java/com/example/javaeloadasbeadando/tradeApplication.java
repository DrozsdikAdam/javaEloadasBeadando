package com.example.javaeloadasbeadando;

import com.oanda.v20.Context;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountInstrumentsResponse;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.primitives.Instrument;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class tradeApplication {

    @Value("${oanda.url}")
    private String url;

    @Value("${oanda.token}")
    private String token;

    @Value("${oanda.accountid}")
    private String accountId;

    private Context getContext() {
        return new Context(url, token);
    }

    private AccountID getAccountID() {
        return new AccountID(accountId);
    }

    public AccountSummary getAccountSummary() {
        try {
            return getContext().account.summary(getAccountID()).getAccount();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getTradableInstruments() {
        try {
            AccountInstrumentsResponse response = getContext().account.instruments(getAccountID());
            return response.getInstruments().stream()
                    .map(instrument -> instrument.getName().toString())
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public ClientPrice getCurrentPrice(String instrumentName) {
        try {
            PricingGetRequest request = new PricingGetRequest(getAccountID(), Collections.singletonList(instrumentName));
            return getContext().pricing.get(request).getPrices().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Candlestick> getHistoricalCandles(String instrumentName, CandlestickGranularity granularity) {
        try {
            InstrumentName name = new InstrumentName(instrumentName);
            InstrumentCandlesRequest request = new InstrumentCandlesRequest(name)
                    .setGranularity(granularity)
                    .setCount(10L); // Corrected to Long type
            return getContext().instrument.candles(request).getCandles();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<CandlestickGranularity> getAvailableGranularities() {
        return Arrays.asList(CandlestickGranularity.values());
    }
}
