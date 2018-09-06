package com.xbd.quartz.configure;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzPropertiesTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public QuartzProperties quartzProperties () {
        return new QuartzProperties();
    }

    @Test
    public void testQuartzPropertiesLoaded() {
        logger.info(JSONObject.toJSONString(quartzProperties()));
    }

}