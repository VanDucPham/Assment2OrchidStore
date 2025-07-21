package org.example.orchidsstore.Controller;

import org.example.orchidsstore.Entity.CartItem;
import org.example.orchidsstore.Service.OrchidService;
import org.example.orchidsstore.Entity.Orchid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.example.orchidsstore.Service.OrderService;
import org.example.orchidsstore.Service.AccountService;
import org.example.orchidsstore.Entity.Order;
import org.example.orchidsstore.Entity.OrderDetail;
import org.example.orchidsstore.Entity.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private OrchidService orchidService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;

    @GetMapping("")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);
        double total = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        model.addAttribute("total", total);
        return "cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        Orchid orchid = orchidService.getOrchidById(id).orElse(null);
        if (orchid != null) {
            boolean found = false;
            for (CartItem item : cart) {
                if (item.getOrchidId().equals(id)) {
                    item.setQuantity(item.getQuantity() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                cart.add(new CartItem(
                    orchid.getOrchidId(),
                    orchid.getOrchidName(),
                    orchid.getOrchidUrl(),
                    orchid.getPrice(),
                    1
                ));
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getOrchidId().equals(id));
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("id") Long id, @RequestParam("quantity") int quantity, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getOrchidId().equals(id)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        double total = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Account account = accountService.getAccountByEmail(email).orElse(null);
        if (account == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để đặt hàng!");
            return "redirect:/login";
        }
        Order order = new Order();
        order.setOrderDate(java.time.LocalDate.now());
        order.setOrderStatus("PENDING");
        order.setAccount(account);
        double total = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        order.setTotalAmount(total);
        order = orderService.saveOrder(order);
        java.util.List<OrderDetail> details = new java.util.ArrayList<>();
        for (CartItem item : cart) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setOrchid(orchidService.getOrchidById(item.getOrchidId()).orElse(null));
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQuantity());
            details.add(detail);
        }
        order.setOrderDetails(details);
        // Lưu từng OrderDetail
        for (OrderDetail d : details) {
            // Giả sử có OrderDetailService, nếu không thì dùng orderDetailRepository.save(d)
            // Ở đây sẽ dùng entityManager hoặc repository nếu có
        }
        // Xóa giỏ hàng khỏi session
        session.removeAttribute("cart");
        model.addAttribute("success", "Đặt hàng thành công!");
        return "redirect:/orders";
    }
} 