package org.skypro.recommendationservice.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final TelegramRecommendationService telegramRecommendationService;

    public RecommendationBotUpdatesListener(TelegramBot telegramBot,
                                            TelegramRecommendationService telegramRecommendationService) {
        this.telegramBot = telegramBot;
        this.telegramRecommendationService = telegramRecommendationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null && update.message().text() != null) {
                String text = update.message().text();
                Long chatId = update.message().chat().id();

                if ("/start".equals(text)) {
                    telegramBot.execute(new SendMessage(chatId, telegramRecommendationService.getHelpMessage()));
                } else if (text.startsWith("/recommend")) {
                    String response = telegramRecommendationService.handleRecommendCommand(text);
                    telegramBot.execute(new SendMessage(chatId, response));
                }
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}