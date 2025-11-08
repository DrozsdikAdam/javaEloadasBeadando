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
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    public OrderCreateResponse createMarketOrder(String instrumentName, double units) throws Exception {
        if (units < 0) {
            List<Position> openPositions = getOpenPositions();
            Optional<Position> positionOpt = openPositions.stream()
                    .filter(p -> p.getInstrument().toString().equals(instrumentName))
                    .findFirst();

            if (positionOpt.isEmpty()) {
                throw new InsufficientPositionException("Nem létező pozícióból próbál eladni: " + instrumentName);
            }

            Position position = positionOpt.get();
            double longUnits = position.getLong().getUnits().doubleValue();
            double shortUnits = position.getShort().getUnits().doubleValue();
            if (longUnits < Math.abs(units)) {
                throw new InsufficientPositionException("Nincs elegendő egység az eladáshoz. Jelenlegi: " + longUnits + ", Eladni kívánt: " + Math.abs(units));
            }
        }

        MarketOrderRequest marketOrderRequest = new MarketOrderRequest()
                .setInstrument(new InstrumentName(instrumentName))
                .setUnits(units)
                .setTimeInForce(TimeInForce.FOK);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(getAccountID())
                .setOrder(marketOrderRequest);

        return getContext().order.create(orderCreateRequest);
    }
}