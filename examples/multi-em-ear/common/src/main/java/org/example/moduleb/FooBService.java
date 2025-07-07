package org.example.moduleb;

import jakarta.ejb.Local;

@Local
public interface FooBService {
    String saveAndFindAllFooB();
}
