package org.tsri.batch;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;



@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BatchApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(BatchApplication.class, args);
        context.getBean(Main.class).execute();
    }


}
