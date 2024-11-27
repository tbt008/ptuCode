package com.example.oj;

import com.example.oj.common.Language;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.service.impl.QuestionServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OjApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjApplication.class, args);
    }

}
