package com.purchasing.repository;

import com.purchasing.entity.LigneCommandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LigneCommandeAchatRepository extends JpaRepository<LigneCommandeAchat, Long> {

    List<LigneCommandeAchat> findByCommandeId(Long commandeId);

    List<LigneCommandeAchat> findByProduitContainingIgnoreCase(String produit);
}