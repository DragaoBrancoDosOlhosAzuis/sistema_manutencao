package br.com.aweb.sistema_manutencao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.aweb.sistema_manutencao.model.SolicitacaoManutencao;
import br.com.aweb.sistema_manutencao.service.SolicitacaoManutencaoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/solicitacoes")
public class SolicitacaoManutencaoController {

    @Autowired
    private SolicitacaoManutencaoService solicitacaoService;

    // Listar todas as solicitações
    @GetMapping
    public String list(Model model) {
        model.addAttribute("solicitacoes", solicitacaoService.listarTodas());
        model.addAttribute("titulo", "Todas as Solicitações");
        return "solicitacao/list";
    }

    // Listar solicitações pendentes
    @GetMapping("/pendentes")
    public String listPendentes(Model model) {
        model.addAttribute("solicitacoes", solicitacaoService.listarPendentes());
        model.addAttribute("titulo", "Solicitações Pendentes");
        return "solicitacao/list";
    }

    // Formulário de nova solicitação
    @GetMapping("/nova")
    public String createForm(Model model) {
        model.addAttribute("solicitacao", new SolicitacaoManutencao());
        return "solicitacao/form";
    }

    // Salvar nova solicitação
    @PostMapping("/nova")
public String create(@Valid @ModelAttribute SolicitacaoManutencao solicitacao, 
                    BindingResult result,
                    Model model) {
    if (result.hasErrors()) {
        return "solicitacao/form";
    }
    
    try {
        solicitacaoService.salvar(solicitacao);
        return "redirect:/solicitacoes";
    } catch (Exception e) {
        model.addAttribute("error", "Erro ao salvar solicitação: " + e.getMessage());
        return "solicitacao/form";
    }
}

    // Formulário de edição
    @GetMapping("/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        var solicitacao = solicitacaoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada"));
        
        model.addAttribute("solicitacao", solicitacao);
        return "solicitacao/form";
    }

    // Atualizar solicitação
    @PostMapping("/editar/{id}")
    public String edit(@PathVariable Long id,
                      @Valid @ModelAttribute SolicitacaoManutencao solicitacao,
                      BindingResult result,
                      Model model) {
        if (result.hasErrors()) {
            return "solicitacao/form";
        }
        
        try {
            solicitacaoService.atualizar(id, solicitacao);
            return "redirect:/solicitacoes?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar solicitação: " + e.getMessage());
            return "solicitacao/form";
        }
    }

    // Formulário para alterar status
    @GetMapping("/status/{id}")
    public String statusForm(@PathVariable Long id, Model model) {
        var solicitacao = solicitacaoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada"));
        
        model.addAttribute("solicitacao", solicitacao);
        return "solicitacao/status";
    }

    // Atualizar status
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                             @RequestParam SolicitacaoManutencao.Status status,
                             @RequestParam(required = false) String observacoesConclusao) {
        solicitacaoService.atualizarStatus(id, status, observacoesConclusao);
        return "redirect:/solicitacoes?success=true";
    }

    // Confirmação de exclusão
    @GetMapping("/excluir/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        var solicitacao = solicitacaoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada"));
        
        model.addAttribute("solicitacao", solicitacao);
        return "solicitacao/delete";
    }

    // Excluir solicitação
    @PostMapping("/excluir/{id}")
    public String delete(@PathVariable Long id) {
        solicitacaoService.excluir(id);
        return "redirect:/solicitacoes?success=true";
    }
}