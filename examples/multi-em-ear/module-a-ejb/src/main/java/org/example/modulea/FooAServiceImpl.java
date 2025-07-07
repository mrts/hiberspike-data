package org.example.modulea;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class FooAServiceImpl implements FooAService {
    @Inject
    FooARepository repo;

    @Override
    public long saveAndCountFooA() {
        repo.saveAndFlush(new FooA());
        return repo.count();
    }
}
