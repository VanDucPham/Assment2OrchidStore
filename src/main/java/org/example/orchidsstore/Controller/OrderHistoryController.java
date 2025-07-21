package org.example.orchidsstore.Controller;

import org.example.orchidsstore.Service.OrderService;
import org.example.orchidsstore.Service.AccountService;
import org.example.orchidsstore.Entity.Order;
import org.example.orchidsstore.Entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class OrderHistoryController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/orders")
    public String myOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Account account = accountService.getAccountByEmail(email).orElse(null);
        List<Order> orders = (account != null) ? orderService.getOrdersByAccount(account.getAccId()) : List.of();
        model.addAttribute("orders", orders);
        return "orders";
    }
} 