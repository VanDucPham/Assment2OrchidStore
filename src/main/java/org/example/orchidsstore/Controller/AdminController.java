package org.example.orchidsstore.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.example.orchidsstore.Service.OrchidService;
import org.example.orchidsstore.Service.CategoryService;
import org.example.orchidsstore.Service.OrderService;
import org.example.orchidsstore.Service.AccountService;
import org.example.orchidsstore.Entity.Orchid;
import org.example.orchidsstore.Entity.Category;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private OrchidService orchidService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;

    @GetMapping("")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String manageProducts(Model model) {
        model.addAttribute("orchids", orchidService.getAllOrchids());
        return "admin/products";
    }

    @GetMapping("/categories")
    public String manageCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @GetMapping("/orders")
    public String manageOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @GetMapping("/accounts")
    public String manageAccounts(Model model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "admin/accounts";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("orchid", new Orchid());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products_add";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Orchid orchid,
                             @RequestParam("category.categoryId") Long categoryId,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Model model) throws IOException {
        Category category = categoryService.getCategoryById(categoryId).orElse(null);
        orchid.setCategory(category);

        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();

            // ✅ Dùng đường dẫn tuyệt đối giống như trong editProduct
            String uploadDir = System.getProperty("user.dir") + "/uploads/orchids/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            File dest = new File(uploadPath, fileName);
            System.out.println("Uploading to: " + dest.getAbsolutePath());
            imageFile.transferTo(dest);

            // Gán URL đúng để hiển thị
            orchid.setOrchidUrl("/uploads/orchids/" + fileName);
        }

        orchidService.saveOrchid(orchid);
        return "redirect:/admin/products";
    }


    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Orchid orchid = orchidService.getOrchidById(id).orElse(null);
        if (orchid == null) return "redirect:/admin/products";
        model.addAttribute("orchid", orchid);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products_edit";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              @ModelAttribute Orchid orchid,
                              @RequestParam("category.categoryId") Long categoryId,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              Model model) throws IOException {
        Orchid existing = orchidService.getOrchidById(id).orElse(null);
        if (existing == null) return "redirect:/admin/products";
        Category category = categoryService.getCategoryById(categoryId).orElse(null);
        orchid.setCategory(category);
        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();

            // Sử dụng đường dẫn tuyệt đối để tránh lỗi trong thư mục tạm của Tomcat
            String uploadDir = System.getProperty("user.dir") + "/uploads/orchids/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            File dest = new File(uploadPath, fileName);
            System.out.println(dest.getAbsolutePath());
            imageFile.transferTo(dest);

            orchid.setOrchidUrl(fileName);
        } else {
            orchid.setOrchidUrl(existing.getOrchidUrl());
        }

        orchid.setOrchidId(id);
        orchidService.saveOrchid(orchid);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        orchidService.deleteOrchid(id);
        return "redirect:/admin/products";
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/orders/view/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        var orderOpt = orderService.getOrderById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "admin/order_detail";
        }
        return "redirect:/admin/orders";
    }

    // Xóa đơn hàng
    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }

    // Sửa tài khoản (hiển thị form)
    @GetMapping("/accounts/edit/{id}")
    public String editAccountForm(@PathVariable Integer id, Model model) {
        var accOpt = accountService.getAccountById(id);
        if (accOpt.isPresent()) {
            model.addAttribute("account", accOpt.get());
            return "admin/account_edit";
        }
        return "redirect:/admin/accounts";
    }

    // Sửa tài khoản (xử lý submit)
    @PostMapping("/accounts/edit/{id}")
    public String editAccount(@PathVariable Integer id, @ModelAttribute("account") org.example.orchidsstore.Entity.Account account) {
        account.setAccId(id);
        accountService.saveAccount(account);
        return "redirect:/admin/accounts";
    }

    // Xóa tài khoản
    @GetMapping("/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
        return "redirect:/admin/accounts";
    }

    // Hiển thị form thêm danh mục
    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new org.example.orchidsstore.Entity.Category());
        return "admin/categories_add";
    }

    // Xử lý thêm danh mục
    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute org.example.orchidsstore.Entity.Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    // Hiển thị form sửa danh mục
    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        var catOpt = categoryService.getCategoryById(id);
        if (catOpt.isPresent()) {
            model.addAttribute("category", catOpt.get());
            return "admin/categories_edit";
        }
        return "redirect:/admin/categories";
    }

    // Xử lý sửa danh mục
    @PostMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute org.example.orchidsstore.Entity.Category category) {
        category.setCategoryId(id);
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }
} 