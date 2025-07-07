package org.example.moduleb;

import jakarta.ejb.Local;

import java.util.List;

@Local
public interface FooBService {
    List<FooB> saveAndFindAll(FooB foo);
}
