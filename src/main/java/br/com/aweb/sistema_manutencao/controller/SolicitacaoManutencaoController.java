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

// anotacao que indica que esta classe e um controller do spring mvc
@Controller
// define que todas as rotas desse controller vao comecar com /solicitacoes
@RequestMapping("/solicitacoes")
public class SolicitacaoManutencaoController {

    // injecao de dependencia do servico que contem a logica de negocio
    @Autowired
    private SolicitacaoManutencaoService solicitacaoService;

    // metodo para listar todas as solicitacoes
    @GetMapping
    public String list(Model model) {
        // adiciona no objeto model a lista de todas as solicitacoes
        model.addAttribute("solicitacoes", solicitacaoService.listarTodas());
        // adiciona um titulo para ser exibido na view
        model.addAttribute("titulo", "todas as solicitacoes");
        // retorna o nome da view que sera renderizada
        return "solicitacao/list";
    }

    // metodo para listar apenas solicitacoes pendentes
    @GetMapping("/pendentes")
    public String listPendentes(Model model) {
        model.addAttribute("solicitacoes", solicitacaoService.listarPendentes());
        model.addAttribute("titulo", "solicitacoes pendentes");
        return "solicitacao/list";
    }

    // metodo que exibe o formulario para criar uma nova solicitacao
    @GetMapping("/nova")
    public String createForm(Model model) {
        // adiciona um objeto vazio de solicitacao no model para preencher no form
        model.addAttribute("solicitacao", new SolicitacaoManutencao());
        return "solicitacao/form";
    }

    // metodo que processa o envio do formulario de nova solicitacao
    @PostMapping("/nova")
    public String create(@Valid @ModelAttribute SolicitacaoManutencao solicitacao, 
                        BindingResult result,
                        Model model) {
        // se houver erros de validacao, retorna o formulario novamente
        if (result.hasErrors()) {
            return "solicitacao/form";
        }
        
        try {
            // chama o servico para salvar a solicitacao no banco
            solicitacaoService.salvar(solicitacao);
            // redireciona para a lista de solicitacoes
            return "redirect:/solicitacoes";
        } catch (Exception e) {
            // caso ocorra erro, retorna o formulario com a mensagem de erro
            model.addAttribute("error", "erro ao salvar solicitacao: " + e.getMessage());
            return "solicitacao/form";
        }
    }

    // metodo para exibir o formulario de edicao de uma solicitacao
    @GetMapping("/editar/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        // busca a solicitacao pelo id, se nao encontrar lanca erro 404
        var solicitacao = solicitacaoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "solicitacao nao encontrada"));
        
        model.addAttribute("solicitacao", solicitacao);
        return "solicitacao/form";
    }

    // metodo que processa a edicao de uma solicitacao
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
            model.addAttribute("error", "erro ao atualizar solicitacao: " + e.getMessage());
            return "solicitacao/form";
        }
    }

    // metodo que exibe o formulario para alterar o status de uma solicitacao
    @GetMapping("/status/{id}")
    public String statusForm(@PathVariable Long id, Model model) {
        var solicitacao = solicitacaoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "solicitacao nao encontrada"));
        
        model.addAttribute("solicitacao", solicitacao);
        return "solicitacao/status";
    }

    // metodo que atualiza o status de uma solicitacao
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                             @RequestParam SolicitacaoManutencao.Status status,
                             @RequestParam(required = false) String observacoesConclusao) {
        // chama o servico para atualizar apenas o status e observacoes
        solicitacaoService.atualizarStatus(id, status, observacoesConclusao);
        return "redirect:/solicitacoes?success=true";
    }

    // metodo que exibe a tela de confirmacao de exclusao
    @GetMapping("/excluir/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        var solicitacao = solicitacaoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "solicitacao nao encontrada"));
        
        model.addAttribute("solicitacao", solicitacao);
        return "solicitacao/delete";
    }

    // metodo que processa a exclusao de uma solicitacao
    @PostMapping("/excluir/{id}")
    public String delete(@PathVariable Long id) {
        solicitacaoService.excluir(id);
        return "redirect:/solicitacoes?success=true";
    }
}
