package org.skypro.recommendationservice.rule;

import org.skypro.recommendationservice.dto.RecommendationDto;
import org.skypro.recommendationservice.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String PRODUCT_TEXT = """
            Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент,
            который поможет вам легко и удобно накапливать деньги на важные цели.
            Больше никаких забытых чеков и потерянных квитанций — всё под контролем!

            Преимущества «Копилки»:

            Накопление средств на конкретные цели. Установите лимит и срок накопления,
            и банк будет автоматически переводить определенную сумму на ваш счет.

            Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления
            и корректируйте стратегию при необходимости.

            Безопасность и надежность. Ваши средства находятся под защитой банка,
            а доступ к ним возможен только через мобильное приложение или интернет-банкинг.

            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!
            """;

    private final RecommendationsRepository repository;

    public TopSavingRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasDebit = repository.existsProductType(userId, "DEBIT");

        int debitDepositSum = repository.getTransactionSum(userId, "DEBIT", "DEPOSIT");
        int savingDepositSum = repository.getTransactionSum(userId, "SAVING", "DEPOSIT");
        int debitWithdrawSum = repository.getTransactionSum(userId, "DEBIT", "WITHDRAW");

        boolean depositCondition = debitDepositSum >= 50000 || savingDepositSum >= 50000;
        boolean balanceCondition = debitDepositSum > debitWithdrawSum;

        if (hasDebit && depositCondition && balanceCondition) {
            return Optional.of(new RecommendationDto(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
        }

        return Optional.empty();
    }
}