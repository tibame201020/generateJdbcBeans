
    private static JdbcTemplate dataSourceJdbcName;

    public static JdbcTemplate getDataSourceJdbc() {
        return dataSourceJdbcName;
    }

    @Autowired
    public void setDataSourceJdbc(@Qualifier("dataSourceJdbcName") JdbcTemplate dataSourceJdbcName) {
        JdbcBeans.dataSourceJdbcName = dataSourceJdbcName;
    }
