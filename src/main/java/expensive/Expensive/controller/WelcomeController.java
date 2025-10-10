package expensive.Expensive.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import expensive.Expensive.service.MessageService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class WelcomeController {

    private final MessageService messageService;

    public WelcomeController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/home")
    public String getHome() {
        return messageService.getHelloMessage();
    }

    @GetMapping("/")
    public void defaultHome(HttpServletResponse response) throws IOException {
        response.sendRedirect("/home");
    }
}
