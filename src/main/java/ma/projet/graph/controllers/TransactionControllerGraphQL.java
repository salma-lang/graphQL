package ma.projet.graph.controllers;

import lombok.AllArgsConstructor;
import ma.projet.graph.dto.TransactionRequest;
import ma.projet.graph.entities.Compte;
import ma.projet.graph.entities.Transaction;
import ma.projet.graph.entities.TypeTransaction;
import ma.projet.graph.repositories.CompteRepository;
import ma.projet.graph.repositories.TransactionRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TransactionControllerGraphQL {
    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;

    @QueryMapping
    public List<Transaction> allTransactions(){
        return transactionRepository.findAll();
    }

    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));
        Transaction transaction = new Transaction(null, transactionRequest.getMontant(), transactionRequest.getDate(), transactionRequest.getType(), compte);
        transactionRepository.save(transaction);
        return transaction;
    }

    @QueryMapping
    public List<Transaction> compteTransaction(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte not found"));
        return transactionRepository.findByCompte(compte);
    }

    @QueryMapping
    public Map<String, Object> transactionStats() {
        long count = transactionRepository.count();
        double sumDepot = transactionRepository.sumByType(TypeTransaction.DEPOT);
        double sumRetrait = transactionRepository.sumByType(TypeTransaction.RETRAIT);
        return Map.of(
                "count", count,
                "sumDepot", sumDepot,
                "sumRetrait", sumRetrait
        );
    }
}