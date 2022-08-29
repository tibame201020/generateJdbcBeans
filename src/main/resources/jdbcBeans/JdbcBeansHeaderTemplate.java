package org.dao.dataSourceConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ImportResource("classpath:jdbc-beans.xml")
public class JdbcBeans {
    jdbcBeansReplaceHere
}
