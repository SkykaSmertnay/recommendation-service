package org.skypro.recommendationservice.rule;

import org.skypro.recommendationservice.dto.RecommendationDto;
import org.skypro.recommendationservice.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String PRODUCT_TEXT = """
            Откройте мир выгодных кредитов с нами!

            Ищете способ быстро и без лишних хлопот получить нужную сумму?
            Тогда наш выгодный кредит — именно то, что вам нужно!
            Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.

            Почему выбирают нас:

            Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.

            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.

            Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели:
            покупку недвижимости, автомобиля, образование, лечение и многое другое.

            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!
            """;

    private final RecommendationsRepository repository;

    public SimpleCreditRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasCredit = repository.existsProductType(userId, "CREDIT");

        int debitDepositSum = repository.getTransactionSum(userId, "DEBIT", "DEPOSIT");
        int debitWithdrawSum = repository.getTransactionSum(userId, "DEBIT", "WITHDRAW");

        boolean balanceCondition = debitDepositSum > debitWithdrawSum;
        boolean withdrawCondition = debitWithdrawSum > 100000;

        if (!hasCredit && balanceCondition && withdrawCondition) {
            return Optional.of(new RecommendationDto(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
        }

        return Optional.empty();
    }
}