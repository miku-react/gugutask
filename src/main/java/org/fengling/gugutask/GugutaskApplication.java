package org.fengling.gugutask;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.fengling.gugutask.dao")
public class GugutaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(GugutaskApplication.class, args);
    }

}
