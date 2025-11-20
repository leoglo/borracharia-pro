package com.borracharia.borracharia.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Size(min = 11, max = 14, message = "CPF inválido")
    private String cpf;

    @Size(min = 10, max = 16, message = "Telefone inválido")
    private String telefone;

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }
}
