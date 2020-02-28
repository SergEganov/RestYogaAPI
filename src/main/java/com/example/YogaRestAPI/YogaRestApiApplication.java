package com.example.YogaRestAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YogaRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YogaRestApiApplication.class, args);
	}

	/*@Bean
	CommandLineRunner initDatabase(UserRepo repo) {
        User user = new User();
        user.setFirstName("First");
        user.setLastName("Lastname");
        user.setBirth(LocalDate.now().minusDays(2));
        user.setPhoneNumber("000");
        user.setEmail("a@a");
        userService.save(user);

        return args -> {
            repo.save(new User());
        };
	}*/
}
