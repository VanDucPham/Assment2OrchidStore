package org.example.orchidsstore.repository;

import org.example.orchidsstore.Entity.Orchid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrchidRepository extends JpaRepository<Orchid, Long> {
    List<Orchid> findByCategoryCategoryId(Long categoryId);
    List<Orchid> findByIsNatural(boolean isNatural);
    
    @Query("SELECT o FROM Orchid o WHERE o.orchidName LIKE %:keyword% OR o.orchidDescription LIKE %:keyword%")
    List<Orchid> searchByKeyword(@Param("keyword") String keyword);
    
    List<Orchid> findByPriceBetween(double minPrice, double maxPrice);
} 