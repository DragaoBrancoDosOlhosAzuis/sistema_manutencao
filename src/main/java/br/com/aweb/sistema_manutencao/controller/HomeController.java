package br.com.aweb.sistema_manutencao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// anotacao que indica que esta classe e um controller do spring mvc
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // redireciona automaticamente para a rota /solicitacoes para levar direto para a aba /solicitacoes 
        return "redirect:/solicitacoes";
    }
}
