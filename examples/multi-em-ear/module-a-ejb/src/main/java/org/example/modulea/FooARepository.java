package org.example.modulea;

import ee.hiberspike.data.ExtendedEntityRepository;
import org.hibernate.Session;
import org.hibernate.annotations.processing.Find;

public interface FooARepository extends ExtendedEntityRepository<FooA, Long> {

    @Find
    FooA findById_UNUSED(Long id);

    @Override
    @FooAQualifier
    Session session();

    @Override
    default Class<FooA> getEntityClass() {
        return FooA.class;
    }

}
