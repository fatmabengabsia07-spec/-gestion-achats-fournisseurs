package com.purchasing.repository;

import com.purchasing.entity.HistoriqueAchats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueAchatsRepository extends JpaRepository<HistoriqueAchats, Long> {

    List<HistoriqueAchats> findByFournisseurId(Long fournisseurId);

    @Query("SELECT AVG(h.delaiLivraison) FROM HistoriqueAchats h WHERE h.fournisseur.id = :fId")
    Double getAvgDelaiByFournisseur(@Param("fId") Long fournisseurId);

    @Query("SELECT h.produit, COUNT(h), AVG(h.delaiLivraison) FROM HistoriqueAchats h " +
           "WHERE h.fournisseur.id = :fId GROUP BY h.produit")
    List<Object[]> getStatsByFournisseur(@Param("fId") Long fournisseurId);
}