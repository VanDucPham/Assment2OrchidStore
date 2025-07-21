package org.example.orchidsstore.Controller;

import org.example.orchidsstore.Service.OrchidService;
import org.example.orchidsstore.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @Autowired
    private OrchidService orchidService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("orchids", orchidService.getAllOrchids());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home";
    }
    
    @GetMapping("/orchids")
    public String orchids(@RequestParam(required = false) String category,
                         @RequestParam(required = false) String type,
                         @RequestParam(required = false) String search,
                         Model model) {
        
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("orchids", orchidService.searchOrchids(search));
        } else if (category != null && !category.isEmpty()) {
            model.addAttribute("orchids", orchidService.getOrchidsByCategory(Long.parseLong(category)));
        } else if ("natural".equals(type)) {
            model.addAttribute("orchids", orchidService.getNaturalOrchids());
        } else if ("artificial".equals(type)) {
            model.addAttribute("orchids", orchidService.getArtificialOrchids());
        } else {
            model.addAttribute("orchids", orchidService.getAllOrchids());
        }
        
        model.addAttribute("categories", categoryService.getAllCategories());
        return "orchids";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
} 