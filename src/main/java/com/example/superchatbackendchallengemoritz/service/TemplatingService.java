package com.example.superchatbackendchallengemoritz.service;

import com.example.superchatbackendchallengemoritz.service.model.CoinbaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TemplatingService {

    public String substitute(String input, Object context) {
        Handlebars handlebars = new Handlebars();
        try {
            Template template = handlebars.compileInline(input);
            String applied = template.apply(context);

            Pattern pattern = Pattern.compile("\\d BTC|\\d\\.\\d{1,8} BTC");
            Matcher matcher = pattern.matcher(applied);
            if (matcher.find()) {
                Double exchangeRate = getBitcoinExchangeRate();
                String value = matcher.group();
                String valueWithoutSuffix = value.substring(0, value.length() - 4);
                Double btcValue = Double.parseDouble(valueWithoutSuffix);
                applied =
                        applied.replaceAll(
                                value, String.format("%1$,.2f USD", btcValue * exchangeRate));
            }

            return applied;
        } catch (Exception e) {
            log.error("Could not substitute string '{}'.", input, e);
            return input;
        }
    }

    private Double getBitcoinExchangeRate() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request =
                new Request.Builder()
                        .url("https://api.coinbase.com/v2/exchange-rates?currency=BTC")
                        .build();

        ObjectMapper objectMapper = new ObjectMapper();
        try (Response response = client.newCall(request).execute()) {
            CoinbaseResponse body =
                    objectMapper.readValue(response.body().bytes(), CoinbaseResponse.class);
            return Double.parseDouble(body.getData().getRates().get("USD"));
        }
    }
}
