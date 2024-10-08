package br.edu.atitus.paradigma.cambio_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.paradigma.cambio_service.entity.CambioEntity;
import br.edu.atitus.paradigma.cambio_service.repository.CambioRepository;

import java.util.Optional;

@RestController 
@RequestMapping("cambio-service") // Mapeia os endpoints para o caminho cambio-service
public class CambioController {

    @Autowired
    private CambioRepository cambioRepository;

    @GetMapping("/{valor}/{origem}/{destino}")
    public CambioEntity getCambio(@PathVariable("valor") double valor,
                                  @PathVariable("origem") String origem,
                                  @PathVariable("destino") String destino) {

        // Busca a taxa de câmbio no banco de dados usando origem e destino
        Optional<CambioEntity> cambioOpt = cambioRepository.findByOrigemAndDestino(origem, destino);

        // Verifica se a taxa de câmbio foi encontrada
        if (cambioOpt.isEmpty()) {
            throw new RuntimeException("Câmbio não encontrado para " + origem + " para " + destino);
        }

        CambioEntity cambio = cambioOpt.get();

        // Calcula o valor convertido com base na taxa de câmbio
        double valorConvertido = valor * cambio.getFator();
        cambio.setValorConvertido(valorConvertido); // Define o valor convertido

        // Define um ambiente
        cambio.setAmbiente("Desenvolvimento"); // Exemplo de valor de ambiente q vai puxar da entity

        // Retorna a entidade com o valor convertido e ambiente
        return cambio;
    }
}
