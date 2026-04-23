package com.purchasing.repository;

import com.purchasing.entity.CommandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {

    List<CommandeAchat> findByFournisseurId(Long fournisseurId);

    List<CommandeAchat> findByStatut(String statut);

    @Query("SELECT c FROM CommandeAchat c WHERE c.fournisseur.id = :fId AND c.statut = :statut")
    List<CommandeAchat> findByFournisseurAndStatut(
        @Param("fId") Long fournisseurId,
        @Param("statut") String statut
    );

    @Query("SELECT SUM(c.montant) FROM CommandeAchat c WHERE c.fournisseur.id = :fId")
    Double getTotalMontantByFournisseur(@Param("fId") Long fournisseurId);
}