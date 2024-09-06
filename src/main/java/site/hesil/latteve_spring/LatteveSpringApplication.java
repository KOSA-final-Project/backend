package site.hesil.latteve_spring;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableRabbit
@SpringBootApplication
public class LatteveSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LatteveSpringApplication.class, args);
	}

}
