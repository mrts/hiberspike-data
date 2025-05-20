/*
 * Copyright 2025 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package ee.hiberspike.data;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import java.io.Serializable;

/**
 * Adds the {@link #count()} operation to {@link EntityRepository}.
 * <p>
 * Extending repositories must provide the entity's class with
 * {@code @Override default Class<EntityType> getEntityClass() { return EntityType.class; } }
 *
 * @param <E> Entity type.
 */
public interface EntityCountRepository<E, PK extends Serializable>
        extends EntityRepository<E, PK> {

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
     * Returns the {@link Class} object that represents the entity
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
