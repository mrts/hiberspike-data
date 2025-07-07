package org.example.moduleb;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class FooBServiceImpl implements FooBService {
    @Inject
    FooBRepository repo;

    @Override
    public String saveAndFindAllFooB() {
        repo.saveAndFlush(new FooB());
        return repo.findAll().toString();
    }
}
