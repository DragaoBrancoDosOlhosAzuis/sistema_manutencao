package br.com.aweb.sistema_manutencao.service;

import br.com.aweb.sistema_manutencao.model.SolicitacaoManutencao;
import br.com.aweb.sistema_manutencao.repository.SolicitacaoManutencaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SolicitacaoManutencaoService {

    @Autowired
    private SolicitacaoManutencaoRepository solicitacaoRepository;

    // CREATE
    public SolicitacaoManutencao salvar(SolicitacaoManutencao solicitacao) {
        return solicitacaoRepository.save(solicitacao);
    }

    // READ
    @Transactional(readOnly = true)
    public List<SolicitacaoManutencao> listarTodas() {
        return solicitacaoRepository.findByOrderByDataSolicitacaoDesc();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoManutencao> listarPendentes() {
        return solicitacaoRepository.findPendentesOrderByPrioridade();
    }

    @Transactional(readOnly = true)
    public Optional<SolicitacaoManutencao> buscarPorId(Long id) {
        return solicitacaoRepository.findById(id);
    }

    // UPDATE
    public SolicitacaoManutencao atualizar(Long id, SolicitacaoManutencao solicitacaoAtualizada) {
        Optional<SolicitacaoManutencao> optionalSolicitacao = buscarPorId(id);
        if (optionalSolicitacao.isEmpty()) {
            throw new RuntimeException("Solicitação não encontrada com ID: " + id);
        }

        SolicitacaoManutencao solicitacaoExistente = optionalSolicitacao.get();
        
        // Atualiza apenas os campos permitidos
        solicitacaoExistente.setSolicitante(solicitacaoAtualizada.getSolicitante());
        solicitacaoExistente.setDescricaoProblema(solicitacaoAtualizada.getDescricaoProblema());
        solicitacaoExistente.setLocalSetor(solicitacaoAtualizada.getLocalSetor());
        solicitacaoExistente.setItem(solicitacaoAtualizada.getItem());
        solicitacaoExistente.setPrioridade(solicitacaoAtualizada.getPrioridade());
        
        // Observações podem ser atualizadas a qualquer momento
        if (solicitacaoAtualizada.getObservacoesConclusao() != null) {
            solicitacaoExistente.setObservacoesConclusao(solicitacaoAtualizada.getObservacoesConclusao());
        }

        return solicitacaoRepository.save(solicitacaoExistente);
    }

    // UPDATE STATUS
    public SolicitacaoManutencao atualizarStatus(Long id, SolicitacaoManutencao.Status novoStatus, String observacoes) {
        Optional<SolicitacaoManutencao> optionalSolicitacao = buscarPorId(id);
        if (optionalSolicitacao.isEmpty()) {
            throw new RuntimeException("Solicitação não encontrada com ID: " + id);
        }

        SolicitacaoManutencao solicitacao = optionalSolicitacao.get();
        solicitacao.setStatus(novoStatus);
        
        // Se foi concluída, registra a data de conclusão
        if (novoStatus == SolicitacaoManutencao.Status.CONCLUIDA && solicitacao.getDataConclusao() == null) {
            solicitacao.setDataConclusao(LocalDateTime.now());
        }
        
        // Atualiza observações se fornecidas
        if (observacoes != null && !observacoes.trim().isEmpty()) {
            solicitacao.setObservacoesConclusao(observacoes);
        }

        return solicitacaoRepository.save(solicitacao);
    }

    // DELETE
    public void excluir(Long id) {
        if (!solicitacaoRepository.existsById(id)) {
            throw new RuntimeException("Solicitação não encontrada com ID: " + id);
        }
        solicitacaoRepository.deleteById(id);
    }

    // Verificar existência
    public boolean existePorId(Long id) {
        return solicitacaoRepository.existsById(id);
    }
}