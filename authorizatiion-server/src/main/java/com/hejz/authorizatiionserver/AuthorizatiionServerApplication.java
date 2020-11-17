package com.hejz.authorizatiionserver;

import com.hejz.authorizatiionserver.entity.Account;
import com.hejz.authorizatiionserver.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class AuthorizatiionServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizatiionServerApplication.class, args);
    }

    @Autowired
    AccountRepository accountRepository;

//    @PostConstruct
//    public void init() {
//        //启动后可以加载用户到数据库中
//        accountRepository.deleteAll();//先清空数据
//        accountRepository.save(new Account("foo", "pass"));
//        System.out.println("数据加载完毕");
//    }
    @Override
	public void run(String... args) throws Exception {
		//启动后可以加载用户到数据库中
		accountRepository.deleteAll();//先清空数据
		accountRepository.save(new Account("foo","pass"));
		System.out.println("数据加载完毕");
	}
}
