package ru.sua;

//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Slf4j
public class Main {
//    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
//        logger.info("LOGGER TEST");
//        log.info("LOMBOK LOGGER TEST");
        SpringApplication.run(Main.class);
    }
}
