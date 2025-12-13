package com.borracharia.borracharia.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String nome,
    
    String cpf,
    String telefone,
    String cep,
    String rua,
    String email,
    String numero,
    String bairro,
    String cidade,
    String estado,
    
    VeiculoRequestDTO veiculo
) {}