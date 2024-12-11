package ma.projet.graph.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;

    @Temporal(TemporalType.DATE)
    private Date dateTransaction;

    @Enumerated(EnumType.STRING)
    private TypeTransaction type;

    @ManyToOne
    @JoinColumn(name = "compteId")
    private Compte compte;
}
