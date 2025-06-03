/*
 * Copyright 2025 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package ee.hiberspike.data;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Adds the {@link #findBy(PK id)}, {@link #findOptionalBy(PK id)} and {@link #count()} operations to {@link EntityRepository}.
 * <p>
 * Extending repositories must provide the entity's class with
 * {@code @Override default Class<EntityType> getEntityClass() { return EntityType.class; } }
 *
 * @param <E>  Entity type.
 * @param <PK> Primary key type.
 */
public interface ExtendedEntityRepository<E, PK extends Serializable>
        extends EntityRepository<E, PK> {

    /**
     * Entity lookup by ID/primary key.
     *
     * @param id Entity ID field, DB primary key.
     * @return Entity identified by primary key or null if it does not exist.
     */
    default E findBy(PK id) {
        return entityManager().find(getEntityClass(), id);
    }

    /**
     * Entity lookup by ID/primary key.
     *
     * @param id Entity ID field, DB primary key.
     * @return Entity identified by primary key or null if it does not exist, wrapped by Optional.
     */
    default Optional<E> findOptionalBy(PK id) {
        return ofNullable(findBy(id));
    }

    /**
     * Count all existing entities of entity class {@code <E>}.
     *
     * @return Counter.
     */
    default Long count() {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(getEntityClass())));
        return entityManager().createQuery(cq).getSingleResult();
    }

    /**
     * Must return the {@link Class} object that represents the entity
     * type {@code <E>} managed by this repository.
     * <p>
     * Should be implemented with a simple
     * {@code @Override default Class<EntityType> getEntityClass() { return EntityType.class; } }
     * statement in extending repositories.
     *
     * @return non-null entity class
     */
    Class<E> getEntityClass();
}
