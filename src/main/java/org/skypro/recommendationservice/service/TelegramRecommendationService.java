package org.skypro.recommendationservice.service;

import org.skypro.recommendationservice.dto.RecommendationDto;
import org.skypro.recommendationservice.dto.RecommendationResponse;
import org.skypro.recommendationservice.dto.TelegramUserInfo;
import org.skypro.recommendationservice.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramRecommendationService {

    private final UsersRepository usersRepository;
    private final RecommendationService recommendationService;

    public TelegramRecommendationService(UsersRepository usersRepository,
                                         RecommendationService recommendationService) {
        this.usersRepository = usersRepository;
        this.recommendationService = recommendationService;
    }

    public String getHelpMessage() {
        return "Привет! Я бот рекомендаций.\n" +
                "Используй команду:\n" +
                "/recommend username";
    }

    public String handleRecommendCommand(String text) {
        String[] parts = text.trim().split("\\s+", 2);

        if (parts.length < 2 || parts[1].isBlank()) {
            return "Укажите username после команды. Пример:\n/recommend ivanov";
        }

        String username = parts[1].trim();
        List<TelegramUserInfo> users = usersRepository.findByUsername(username);

        if (users.size() != 1) {
            return "Пользователь не найден.";
        }

        TelegramUserInfo user = users.get(0);
        RecommendationResponse response = recommendationService.getRecommendations(user.getId());

        if (response.getRecommendations().isEmpty()) {
            return "Здравствуйте " + user.getFirstName() + " " + user.getLastName() + "!\n" +
                    "Новые продукты для вас:\n" +
                    "• Пока рекомендаций нет";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Здравствуйте ")
                .append(user.getFirstName())
                .append(" ")
                .append(user.getLastName())
                .append("!\n")
                .append("Новые продукты для вас:\n");

        for (RecommendationDto recommendation : response.getRecommendations()) {
            builder.append("• ")
                    .append(recommendation.getName())
                    .append("\n");
        }

        return builder.toString();
    }
}