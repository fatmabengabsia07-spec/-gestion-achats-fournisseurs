package com.purchasing.repository;

import com.purchasing.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    List<Fournisseur> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT f FROM Fournisseur f WHERE f.note >= :noteMin ORDER BY f.note DESC")
    List<Fournisseur> findTopFournisseurs(@Param("noteMin") Double noteMin);

    @Query("SELECT f FROM Fournisseur f ORDER BY f.note DESC")
    List<Fournisseur> findAllOrderByNoteDesc();
}