package org.example.orchidsstore.Service;

import org.example.orchidsstore.Entity.Orchid;
import org.example.orchidsstore.repository.OrchidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrchidService {
    
    @Autowired
    private OrchidRepository orchidRepository;
    
    public List<Orchid> getAllOrchids() {
        return orchidRepository.findAll();
    }
    
    public Optional<Orchid> getOrchidById(Long id) {
        return orchidRepository.findById(id);
    }
    
    public Orchid saveOrchid(Orchid orchid) {
        return orchidRepository.save(orchid);
    }
    
    public void deleteOrchid(Long id) {
        orchidRepository.deleteById(id);
    }
    
    public List<Orchid> getOrchidsByCategory(Long categoryId) {
        return orchidRepository.findByCategoryCategoryId(categoryId);
    }
    
    public List<Orchid> getNaturalOrchids() {
        return orchidRepository.findByIsNatural(true);
    }
    
    public List<Orchid> getArtificialOrchids() {
        return orchidRepository.findByIsNatural(false);
    }
    
    public List<Orchid> searchOrchids(String keyword) {
        return orchidRepository.searchByKeyword(keyword);
    }
    
    public List<Orchid> getOrchidsByPriceRange(double minPrice, double maxPrice) {
        return orchidRepository.findByPriceBetween(minPrice, maxPrice);
    }
} 