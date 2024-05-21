package com.IEEEUWUSB.IEEEStudentBranchBackEnd;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IEEE_SB_App {

    public static void main(String[] args) {
        SpringApplication.run(IEEE_SB_App.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
