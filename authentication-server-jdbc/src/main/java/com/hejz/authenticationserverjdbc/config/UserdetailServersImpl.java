package com.hejz.authenticationserverjdbc.config;

import com.hejz.authenticationserverjdbc.entiey.MyUserDetails;
import com.hejz.authenticationserverjdbc.entiey.User;
import com.hejz.authenticationserverjdbc.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userDetailServers")
public class UserdetailServersImpl implements UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userDetailsRepository.findByUsername(username);
        optionalUser.orElseThrow(()->new UsernameNotFoundException("用户名不存在"));
        return new MyUserDetails(optionalUser.get());
    }
}
