package com.yu.ding;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

class DingApplicationTests {

    @Test
    void contextLoads() {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        Object[] param1 = new Object[]{"a", "b", "c"};
        List<Object> param2 = new ArrayList<>();
        param2.add("d");
        param2.add("e");
        param2.add("f");
        Object[] res = new Object[param1.length+param2.size()];
        System.arraycopy(param1, 0, res, 0, param1.length);
        System.arraycopy(param2.toArray(), 0, res, param1.length, param2.size());
        for(Object o : res){
            System.out.println(o.toString());
        }
    }

}
