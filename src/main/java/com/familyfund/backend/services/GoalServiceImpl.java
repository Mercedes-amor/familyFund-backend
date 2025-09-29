package com.familyfund.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familyfund.backend.repositories.FamilyRepository;
import com.familyfund.backend.repositories.GoalRepository;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    GoalRepository goalRepository;
    @Autowired
    FamilyRepository familyRepository;


}
