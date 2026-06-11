package hr.algebra.festlog.controller.mvc;

import hr.algebra.festlog.dto.Dto;
import hr.algebra.festlog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthMvcController {

    private static final String TEMPLATE_AUTH_REGISTER = "auth/register";

    private final AuthService authService;

    public AuthMvcController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new Dto.RegisterRequest("", "", ""));
        return TEMPLATE_AUTH_REGISTER;
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("registerRequest") Dto.RegisterRequest request,
        BindingResult result,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (result.hasErrors()) {
            return TEMPLATE_AUTH_REGISTER;
        }
        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("successMessage", "Account created! Please log in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return TEMPLATE_AUTH_REGISTER;
        }
    }
}
