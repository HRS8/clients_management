package com.example.clients_management.controllers;

import java.util.List;  // Add this import

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;  // Add this import
import com.example.clients_management.entities.ClientDetails;
import com.example.clients_management.entities.ServiceProviderDetails;
import com.example.clients_management.entities.AdminDetails;
import com.example.clients_management.entities.Bookings;  // Add this import
import com.example.clients_management.entities.Category;  // Add this import
import com.example.clients_management.repositories.AdminRepository;
import com.example.clients_management.repositories.ClientRepository;
import com.example.clients_management.repositories.ServiceProviderRepository;
import com.example.clients_management.repositories.BookingsRepository;
import com.example.clients_management.repositories.CategoryRepository;  // Add this import

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inject the existing PasswordEncoder

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private BookingsRepository bookingsRepository;  // Add this repository

    @Autowired
    private CategoryRepository categoryRepository;  // Add this repository

    @GetMapping("/adminlogin")
    public String showLoginPage() {
        return "adminLogin";
    }

    @PostMapping("/adminlogin")
    public String processLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        AdminDetails admin = adminRepository.findByEmail(email);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            session.setAttribute("adminId", admin.getId());
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("error", "Invalid credentials");
        return "adminLogin";
    }

    @GetMapping("/admin/dashboard")
    public String showDashboard(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        return "adminDashboard";
    }

    @GetMapping("/admin/addcategory")
    public String showAddCategory(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        return "addCategory";
    }

    @PostMapping("/admin/addcategory")
    public String addCategory(
            @RequestParam String categoryName,
            @RequestParam String description,
            HttpSession session,
            Model model) {
        
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setDescription(description);

        try {
            categoryRepository.save(category);
            return "redirect:/admin/viewcategories";
        } catch (Exception e) {
            model.addAttribute("error", "Category with this name already exists");
            return "addCategory";
        }
    }

    @GetMapping("/admin/viewcategories")
    public String showViewCategories(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        model.addAttribute("categories", categoryRepository.findAll());
        return "viewCategories";
    }

    @GetMapping("/admin/clients")
    public String showClients(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        model.addAttribute("clients", clientRepository.findAll());
        return "clientList";
    }

    @GetMapping("/admin/serviceproviders")
    public String showServiceProviders(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        model.addAttribute("providers", serviceProviderRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll()); // Add this line
        return "serviceProviderList";
    }

    @GetMapping("/admin/servicerequests")
    public String showServiceRequests(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        // Fetch all bookings and add to model
        List<Bookings> bookings = bookingsRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "serviceRequests";  // Changed from "serviceHistory" to "serviceRequests"
    }

    @GetMapping("/admin/servicehistory")
    public String showServiceHistory(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        List<Bookings> bookings = bookingsRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "serviceHistory";
    }

    @GetMapping("/admin/deletecategory/{id}")
    public String deleteCategory(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        
        try {
            // First get the category to find its name
            Category category = categoryRepository.findById(id).orElse(null);
            if (category != null) {
                // Delete all service providers with matching preferred service
                List<ServiceProviderDetails> providers = serviceProviderRepository.findByPreferredService(category.getName());
                serviceProviderRepository.deleteAll(providers);
                
                // Then delete the category
                categoryRepository.deleteById(id);
            }
        } catch (Exception e) {
            // Handle any errors if needed
        }
        
        return "redirect:/admin/viewcategories";
    }

    @GetMapping("/admin/deleterequest/{id}")
    public String deleteServiceRequest(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        
        try {
            bookingsRepository.deleteById(id);
        } catch (Exception e) {
            // Handle any errors if needed
        }
        
        return "redirect:/admin/servicerequests";
    }

    @GetMapping("/admin/deletehistory/{id}")
    public String deleteServiceHistory(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        
        try {
            bookingsRepository.deleteById(id);
        } catch (Exception e) {
            // Handle any errors if needed
        }
        
        return "redirect:/admin/servicehistory";
    }

    @GetMapping("/admin/deleteclient/{id}")
    public String deleteClient(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            // Handle any errors if needed
        }
        
        return "redirect:/admin/clients";
    }

    @GetMapping("/admin/deleteprovider/{id}")
    public String deleteServiceProvider(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }
        
        try {
            serviceProviderRepository.deleteById(id);
        } catch (Exception e) {
            // Handle any errors if needed
        }
        
        return "redirect:/admin/serviceproviders";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/adminlogin";
    }

    @PostMapping("/admin/addclient")
    public String addClient(@RequestParam String name,
                          @RequestParam String mobile,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String location,
                          HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }

        ClientDetails client = new ClientDetails();
        client.setName(name);
        client.setMobile(mobile);
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(password));
        client.setLocation(location);

        clientRepository.save(client);
        return "redirect:/admin/clients";
    }

    @PostMapping("/admin/addprovider")
    public String addServiceProvider(@RequestParam String name,
                                   @RequestParam String mobile,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String location,
                                   @RequestParam String preferredService,
                                   @RequestParam double budget,
                                   HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/adminlogin";
        }

        ServiceProviderDetails provider = new ServiceProviderDetails();
        provider.setName(name);
        provider.setMobile(mobile);
        provider.setEmail(email);
        provider.setPassword(passwordEncoder.encode(password));
        provider.setLocation(location);
        provider.setPreferredService(preferredService);
        provider.setBudget(budget);

        serviceProviderRepository.save(provider);
        return "redirect:/admin/serviceproviders";
    }

}