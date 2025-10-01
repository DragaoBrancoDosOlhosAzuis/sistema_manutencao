package br.com.aweb.sistema_manutencao.repository;

import br.com.aweb.sistema_manutencao.model.SolicitacaoManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
// indica que esta interface e um repository do spring
@Repository
public interface SolicitacaoManutencaoRepository extends JpaRepository<SolicitacaoManutencao, Long> {
    
    // busca por status, ordenadas pela data
    List<SolicitacaoManutencao> findByStatusOrderByDataSolicitacaoDesc(SolicitacaoManutencao.Status status);
    
    // busca todas as solicitacoes ordenadas pela data
    List<SolicitacaoManutencao> findByOrderByDataSolicitacaoDesc();
    
    // consulta para buscar solicitacoes pendentes
    // ordena pela prioridade
    // ordena pela data de solicitacao em ordem crescente
    @Query("SELECT s FROM SolicitacaoManutencao s WHERE s.status IN ('ABERTA', 'EM_ANDAMENTO') " +
           "ORDER BY " +
           "CASE s.prioridade " +
           "  WHEN 'URGENTE' THEN 1 " +
           "  WHEN 'ALTA' THEN 2 " +
           "  WHEN 'MEDIA' THEN 3 " +
           "  WHEN 'BAIXA' THEN 4 " +
           "END, s.dataSolicitacao ASC")
    List<SolicitacaoManutencao> findPendentesOrderByPrioridade();
}
