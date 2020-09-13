package com.fruit.task.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class FruitTaskMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FruitTaskMasterApplication.class, args);
    }

}
