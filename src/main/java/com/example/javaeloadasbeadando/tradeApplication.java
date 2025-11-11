package com.example.javaeloadasbeadando;

import com.oanda.v20.Context;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountInstrumentsResponse;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.order.TimeInForce;
import com.oanda.v20.position.Position;
import com.oanda.v20.primitives.DecimalNumber;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.oanda.v20.trade.*;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;

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
                    .setCount(10L);
            return getContext().instrument.candles(request).getCandles();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<CandlestickGranularity> getAvailableGranularities() {
        return Arrays.asList(CandlestickGranularity.values());
    }

    public List<Position> getOpenPositions() {
        try {
            return getContext().position.listOpen(getAccountID()).getPositions();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public OrderCreateResponse createMarketOrder(String instrumentName, Double units) throws Exception {
        MarketOrderRequest marketOrderRequest = new MarketOrderRequest()
                .setInstrument(new InstrumentName(instrumentName))
                .setUnits(new DecimalNumber(units)) // Correctly use DecimalNumber
                .setTimeInForce(TimeInForce.FOK); // Fill Or Kill

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(getAccountID())
                .setOrder(marketOrderRequest);

        return getContext().order.create(orderCreateRequest);
    }
    /**
     * Lezár egy trade-et a TradeCloseRequest objektum használatával.
     * @param tradeIdToClose A bezárandó trade azonosítója (String).
     */
    public void closeTradeWithRequest(String tradeIdToClose) {

        if (tradeIdToClose == null || tradeIdToClose.trim().isEmpty()) {
            return;
        }

        try {
            // 1. Hozzunk létre egy TradeSpecifier-t a string ID alapján
            TradeSpecifier tradeSpecifier = new TradeSpecifier(tradeIdToClose);

            // 2. Hozzunk létre egy TradeCloseRequest-et
            //    (getAccountID() a te meglévő metódusod)
            TradeCloseRequest request = new TradeCloseRequest(getAccountID(), tradeSpecifier);

            System.out.println("Kísérlet a(z) " + tradeIdToClose + " zárására (TradeCloseRequest metódussal)...");

            // 3. Hívjuk meg a close metódust a request objektummal
            //    A válasz (response) itt is TradeCloseResponse típusú
            TradeCloseResponse response = getContext().trade.close(request);

            System.out.println("Trade (ID: " + tradeIdToClose + ") sikeresen lezárva. Tranzakció ID: "
                    + response.getOrderFillTransaction().getId());

        } catch (RequestException | ExecuteException e) {
            System.err.println("Hiba történt a(z) " + tradeIdToClose + " trade zárása közben: " + e.getMessage());
            throw new RuntimeException("API hiba a trade zárása közben: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Általános hiba a(z) " + tradeIdToClose + " trade zárásakor: " + e.getMessage());
            throw new RuntimeException("Általános hiba a trade zárása közben: " + e.getMessage(), e);
        }
    }
    public List<Trade> getOpenTrades() {
        try {
            // Ez a metódus lekéri az összes egyedi nyitott trade-et
            TradeListOpenResponse response = getContext().trade.listOpen(getAccountID());
            return response.getTrades();
        } catch (Exception e) {
            e.printStackTrace();
            // Hiba esetén üres listát adunk vissza
            return Collections.emptyList();
        }
    }
}