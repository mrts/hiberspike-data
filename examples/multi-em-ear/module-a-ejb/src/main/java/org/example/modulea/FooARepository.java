package org.example.modulea;

import ee.hiberspike.data.ExtendedEntityRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.processing.Find;

public interface FooARepository extends ExtendedEntityRepository<FooA, Long> {

    @Find
    FooA findById_UNUSED(Long id);

    @Override
    @FooAQualifier
    EntityManager entityManager();

    @Override
    default Class<FooA> getEntityClass() {
        return FooA.class;
    }

}
