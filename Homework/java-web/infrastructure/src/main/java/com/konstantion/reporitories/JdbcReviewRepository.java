package com.konstantion.reporitories;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public record JdbcReviewRepository(NamedParameterJdbcTemplate jdbcTemplate) {
}
