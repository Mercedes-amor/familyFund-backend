package com.familyfund.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.repositories.FamilyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;

    // @Autowired
    public FamilyServiceImpl(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    //Crear y guardar Familia
    @Override
    public Family save(Family family) {
        return familyRepository.save(family);
    }

    //Mostrar todas las familias
    @Override
    public List<Family> findAll() {
        return familyRepository.findAll();
    }

    //Encontrar por Id
    @Override
    public Family findById(Long id) {
        Optional<Family> family = familyRepository.findById(id);
        return family.orElse(null); //orElse??
    }

    //Borrar por id
    @Override
    public void deleteById(Long id) {
        familyRepository.deleteById(id);
    }
}
