/*
 * Copyright 2025 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package ee.hiberspike.data;

import org.hibernate.annotations.processing.Find;

import java.io.Serializable;
import java.util.Optional;

/**
 * Adds ID/primary key lookup helpers to {@link EntityRepository}.
 * <p>
 * The generated query is assembled from the name of the method parameter,
 * therefore the identifier property in the entity <em>must</em> be named
 * {@code id}. If your entity uses a different field name, create an explicit
 * query method instead of using this interface.
 *
 * @param <E>  Entity type.
 * @param <PK> Primary key type.
 */
public interface EntityWithIdRepository<E, PK extends Serializable>
        extends EntityRepository<E, PK> {

    /**
     * Entity lookup by ID/primary key.
     *
     * @param id Entity ID field, DB primary key.
     * @return Entity identified by primary key or null if it does not exist.
     */
    @Find
    E findBy(PK id);

    /**
     * Entity lookup by ID/primary key.
     *
     * @param id Entity ID field, DB primary key.
     * @return Entity identified by primary key or null if it does not exist, wrapped by Optional.
     */
    @Find
    Optional<E> findOptionalBy(PK id);
}
