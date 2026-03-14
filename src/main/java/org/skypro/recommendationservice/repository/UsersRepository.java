package org.skypro.recommendationservice.repository;

import org.skypro.recommendationservice.dto.TelegramUserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsersRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TelegramUserInfo> findByUsername(String username) {
        return jdbcTemplate.query(
                "select id, username, first_name, last_name from users where username = ?",
                (rs, rowNum) -> new TelegramUserInfo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ),
                username
        );
    }
}