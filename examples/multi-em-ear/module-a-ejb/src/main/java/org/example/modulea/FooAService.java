package org.example.modulea;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class FooAService {
    @Inject
    FooARepository repo;

    public long saveAndCount(FooA foo) {
        repo.saveAndFlush(foo);
        return repo.count();
    }
}
