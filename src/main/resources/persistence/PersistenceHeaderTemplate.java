package org.dao.dataSourceConfig;

import org.springframework.jdbc.core.JdbcTemplate;

public enum Persistence {

jdbcBeansReplaceHere

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getValue() {
        return jdbcTemplate;
    }

    public void setValue(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
