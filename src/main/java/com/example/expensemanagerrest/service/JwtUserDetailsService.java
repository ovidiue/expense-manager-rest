package com.example.expensemanagerrest.service;

import java.util.ArrayDeque;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if ("javainuse".equals(username)) {
      return new User(
          "javainuse",
          "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
          new ArrayDeque<>()
      );
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
