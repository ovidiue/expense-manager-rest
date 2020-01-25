package com.example.expensemanagerrest.service;

import com.example.expensemanagerrest.model.DAOUser;
import com.example.expensemanagerrest.model.UserDTO;
import com.example.expensemanagerrest.repository.UserRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder bcryptEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    DAOUser user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);

    }
    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(),
        new ArrayList<>());
  }

  public DAOUser save(UserDTO user) {
    DAOUser newDAOUser = new DAOUser();
    newDAOUser.setUsername(user.getUsername());
    newDAOUser.setPassword(bcryptEncoder.encode(user.getPassword()));
    return userRepository.save(newDAOUser);
  }
}
