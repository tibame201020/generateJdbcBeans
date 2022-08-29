    DATASOURCE_JDBC_ENUM_NAME {
        @Override
        public JdbcTemplate getValue() {
            return JdbcBeans.getDataSourceJdbc();
        }
    },