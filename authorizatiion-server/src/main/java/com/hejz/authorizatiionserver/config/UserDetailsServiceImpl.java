package com.hejz.authorizatiionserver.config;

import com.hejz.authorizatiionserver.entity.Account;
import com.hejz.authorizatiionserver.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findByUsername(username);
        return user.map(account ->
                new User(account.getUsername(), account.getPassword(), account.getEnable(), account.getExpired(), account.getCredentials(), account.getLock()
                        , Arrays.stream(account.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
        )
                //用户不存在抛异常
                .orElseThrow(() -> new UsernameNotFoundException("该用户不存在"));
    }
}
