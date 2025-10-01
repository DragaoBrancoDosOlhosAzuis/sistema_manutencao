package br.com.aweb.sistema_manutencao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//marca esta classe como aplicacao principal
@SpringBootApplication
public class SistemaManutencaoApplication {

    // metodo que inicia a aplicacao
    public static void main(String[] args) {
        // o metodo run inicializa todo contexto do spring
        SpringApplication.run(SistemaManutencaoApplication.class, args);
    }
}