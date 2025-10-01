package br.com.aweb.sistema_manutencao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

// indica que esta classe e uma entidade do banco de dados
@Entity
// define o nome da tabela no banco que esta entidade vai representar
@Table(name = "solicitacoes_manutencao")
public class SolicitacaoManutencao {

    @Id
    // estrategia de geracao automatica do id (auto increment no banco)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // campo obrigatorio
    @NotBlank(message = "nome do solicitante e obrigatorio")
    // configuracao da coluna no banco
    @Column(nullable = false, length = 100)
    private String solicitante;

    @NotBlank(message = "descricao do problema e obrigatoria")
    @Column(nullable = false, length = 500)
    private String descricaoProblema;

    @NotBlank(message = "local/setor e obrigatorio")
    @Column(nullable = false, length = 100)
    private String localSetor;

    @NotBlank(message = "item e obrigatorio")
    @Column(nullable = false, length = 100)
    private String item;

    // campo obrigatorio que usa enumeracao
    @NotNull(message = "prioridade e obrigatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    //representa o status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // data que a solicitacao foi criada
    @Column(nullable = false)
    private LocalDateTime dataSolicitacao;

    // data que a solicitacao foi concluida
    private LocalDateTime dataConclusao;

    // observacoes opcionais
    @Column(length = 500)
    private String observacoesConclusao;

    // enumeracao que define os niveis de prioridade possiveis
    public enum Prioridade {
        BAIXA, MEDIA, ALTA, URGENTE
    }

    // enumeracao que define os estados possiveis da solicitacao
    public enum Status {
        ABERTA, EM_ANDAMENTO, CONCLUIDA, CANCELADA
    }

    // construtor padrao
    public SolicitacaoManutencao() {
        // define automaticamente a data da solicitacao como o momento atual
        this.dataSolicitacao = LocalDateTime.now();
        // define status inicial como aberta
        this.status = Status.ABERTA;
    }

    // construtor alternativo para inicializar ja com valores
    public SolicitacaoManutencao(String solicitante, String descricaoProblema, String localSetor, 
                                String item, Prioridade prioridade) {
        this(); // chama o construtor padrao
        this.solicitante = solicitante;
        this.descricaoProblema = descricaoProblema;
        this.localSetor = localSetor;
        this.item = item;
        this.prioridade = prioridade;
    }

    // getters e setters (metodos de acesso para cada campo)
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

    // metodo de ciclo de vida jpa chamado antes de salvar no banco
    @PrePersist
    public void prePersist() {
        //data da solicitacao nao fique nula
        if (dataSolicitacao == null) {
            dataSolicitacao = LocalDateTime.now();
        }
        // garante que o status inicial seja aberta caso nao esteja definido
        if (status == null) {
            status = Status.ABERTA;
        }
    }

    // sobrescrita para representar o objeto como texto
    @Override
    public String toString() {
        return "solicitacaomanutencao{" +
                "id=" + id +
                ", solicitante='" + solicitante + '\'' +
                ", item='" + item + '\'' +
                ", status=" + status +
                '}';
    }
}
