package org.example.orchidsstore.Service;

import org.example.orchidsstore.Entity.Account;
import org.example.orchidsstore.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public Optional<Account> getAccountById(Integer id) {
        return accountRepository.findById(id);
    }
    
    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
    
    public Account saveAccount(Account account) {
        // Mã hóa mật khẩu trước khi lưu
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }
    
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
    
    public void deleteAccount(Integer id) {
        accountRepository.deleteById(id);
    }
} 