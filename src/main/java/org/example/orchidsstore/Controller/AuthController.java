package org.example.orchidsstore.Controller;

import org.example.orchidsstore.Entity.Account;
import org.example.orchidsstore.Entity.Role;
import org.example.orchidsstore.Service.AccountService;
import org.example.orchidsstore.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(Account account, RedirectAttributes redirectAttributes) {
        if (accountService.existsByEmail(account.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã tồn tại!");
            return "redirect:/register";
        }
        
        // Mặc định role là USER
        Role userRole = roleService.getRoleByName("USER");
        if (userRole == null) {
            // Tạo role USER nếu chưa có
            userRole = new Role();
            userRole.setRoleName("USER");
            userRole = roleService.saveRole(userRole);
        }
        account.setRole(userRole);
        
        accountService.saveAccount(account);
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }
} 