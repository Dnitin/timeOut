package com.fastenal.gen;

import com.fastenal.gen.service.TimeOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@PropertySource("/prop/application.properties")
public class Launcher implements CommandLineRunner {

	@Autowired
	private TimeOutService service;

	public static void main(String[] args)
	{
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Launcher.class);
		builder.headless(false).run(args);
	}

	@Override
	public void run(String... arg0) throws Exception
	{
		service.tellTime();
	}
}