package org.example.modulea;

import jakarta.ejb.Local;

@Local
public interface FooAService {
    long saveAndCount(FooA foo);
}
