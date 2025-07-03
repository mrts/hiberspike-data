package org.example.moduleb;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class FooBService {
    @Inject
    FooBRepository repo;

    public List<FooB> saveAndFindAll(FooB foo) {
        repo.saveAndFlush(foo);
        return repo.findAll();
    }
}
