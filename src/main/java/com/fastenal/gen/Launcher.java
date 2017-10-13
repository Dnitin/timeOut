package com.fastenal.gen;

import com.fastenal.gen.service.TimeOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("/prop/application.properties")
public class Launcher implements CommandLineRunner {

	@Autowired
	private TimeOutService service;

	public static void main(String[] args)
	{
		SpringApplication.run(Launcher.class).close();
	}

	@Override
	public void run(String... arg0) throws Exception
	{
		service.tellTime();
	}
}