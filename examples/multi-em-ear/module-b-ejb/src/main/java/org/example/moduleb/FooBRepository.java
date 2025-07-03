package org.example.moduleb;

import ee.hiberspike.data.EntityRepository;
import org.hibernate.Session;
import org.hibernate.annotations.processing.Find;

public interface FooBRepository extends EntityRepository<FooB, Long> {

    @Find
    FooB findById_UNUSED(Long id);

    @Override
    @FooBQualifier
    Session session();

}
