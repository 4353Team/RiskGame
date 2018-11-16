package riskgame;


import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class JavaBotTest {
    public static void main(String[] args) throws IOException {
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register our bot
        try {
            botsApi.registerBot(new JavaRiskGameBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}