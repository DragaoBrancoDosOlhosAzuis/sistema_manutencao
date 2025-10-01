package br.com.aweb.sistema_manutencao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes_manutencao")
public class SolicitacaoManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do solicitante é obrigatório")
    @Column(nullable = false, length = 100)
    private String solicitante;

    @NotBlank(message = "Descrição do problema é obrigatória")
    @Column(nullable = false, length = 500)
    private String descricaoProblema;

    @NotBlank(message = "Local/Setor é obrigatório")
    @Column(nullable = false, length = 100)
    private String localSetor;

    @NotBlank(message = "Item é obrigatório")
    @Column(nullable = false, length = 100)
    private String item;

    @NotNull(message = "Prioridade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataConclusao;

    @Column(length = 500)
    private String observacoesConclusao;

    public enum Prioridade {
        BAIXA, MEDIA, ALTA, URGENTE
    }

    public enum Status {
        ABERTA, EM_ANDAMENTO, CONCLUIDA, CANCELADA
    }

    // Construtores
    public SolicitacaoManutencao() {
        this.dataSolicitacao = LocalDateTime.now();
        this.status = Status.ABERTA;
    }

    public SolicitacaoManutencao(String solicitante, String descricaoProblema, String localSetor, 
                                String item, Prioridade prioridade) {
        this();
        this.solicitante = solicitante;
        this.descricaoProblema = descricaoProblema;
        this.localSetor = localSetor;
        this.item = item;
        this.prioridade = prioridade;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }

    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }

    public String getLocalSetor() { return localSetor; }
    public void setLocalSetor(String localSetor) { this.localSetor = localSetor; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public String getObservacoesConclusao() { return observacoesConclusao; }
    public void setObservacoesConclusao(String observacoesConclusao) { this.observacoesConclusao = observacoesConclusao; }

    @PrePersist
    public void prePersist() {
        if (dataSolicitacao == null) {
            dataSolicitacao = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.ABERTA;
        }
    }

    @Override
    public String toString() {
        return "SolicitacaoManutencao{" +
                "id=" + id +
                ", solicitante='" + solicitante + '\'' +
                ", item='" + item + '\'' +
                ", status=" + status +
                '}';
    }
}