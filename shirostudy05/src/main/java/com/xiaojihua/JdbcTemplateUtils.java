package com.xiaojihua;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * springjdbc 工具类
 */
public class JdbcTemplateUtils {

    private static JdbcTemplate jdbcTemplate;

    public static JdbcTemplate jdbcTemplate() {
        if(jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate();
        }
        return jdbcTemplate;
    }

    private static JdbcTemplate createJdbcTemplate() {

        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/shiro?useUnicode=true&characterEncoding=UTF8");
        ds.setUsername("root");
        ds.setPassword("root");

        return new JdbcTemplate(ds);
    }

}
