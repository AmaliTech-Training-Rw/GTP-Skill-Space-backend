package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        // Get the role from the account entity
        UserRole userRole = account.getRole();

        // Create a list of GrantedAuthority based on the UserRole enum
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));


        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }
}

