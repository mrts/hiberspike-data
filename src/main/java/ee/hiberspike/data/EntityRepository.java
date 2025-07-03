/*
 * Copyright 2011 - 2024 The Apache Software Foundation
 * Copyright 2025 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package ee.hiberspike.data;

import org.hibernate.Session;
import org.hibernate.annotations.processing.Find;
import org.hibernate.query.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Base Repository interface. All non-default methods are implemented by Jakarta Data Repository code generation.
 * <p>
 * Based on org.apache.deltaspike.data.api.EntityRepository and org.apache.deltaspike.data.impl.handler.EntityRepositoryHandler.
 * <p>
 * All modifying operations require an active transaction.
 *
 * @param <E>  Entity type.
 * @param <PK> Primary key type.
 */
public interface EntityRepository<E, PK extends Serializable> {

    // Used below in default methods, but also gives a hint to use stateful sessions to the annotation processor, see
    // https://docs.jboss.org/hibernate/orm/6.6/introduction/html_single/Hibernate_Introduction.html#static-or-instance
    Session session();

    /**
     * Lookup all existing entities of entity class {@code <E>}.
     *
     * @return List of entities, empty if none found.
     */
    @Find
    List<E> findAll();

    /**
     * Lookup a range of existing entities of entity class {@code <E>} with support for pagination.
     *
     * @param page The pagination specification.
     * @return List of entities, empty if none found.
     */
    @Find
    List<E> findAll(Page page);

    /**
     * Lookup a range of existing entities of entity class {@code <E>} with support for pagination.
     *
     * @param start The starting position.
     * @param max   The maximum number of results to return
     * @return List of entities, empty if none found.
     */
    default List<E> findAll(int start, int max) {
        return findAll(Page.page(max, start));
    }

    /**
     * Persist (new entity) or merge the given entity. The distinction on calling either
     * method is done based on the primary key field being null or not.
     * If this results in wrong behavior for a specific case, consider using the
     * {@code session()} directly, which offers both
     * {@code persist} and {@code merge}.
     *
     * @param entity Entity to save.
     * @return Returns the modified entity.
     */
    default E save(E entity) {
        Objects.requireNonNull(entity);
        if (session().contains(entity)) {
            return entity;
        }
        PK primaryKey = getPrimaryKey(entity);
        if (primaryKey == null ||
                (primaryKey instanceof Number && ((Number) primaryKey).longValue() == 0L)) {
            session().persist(entity);
            return entity;
        }
        return session().merge(entity);
    }

    /**
     * {@link #save(Object)}s the given entity and flushes the persistence context afterward.
     *
     * @param entity Entity to save.
     * @return Returns the modified entity.
     */
    default E saveAndFlush(E entity) {
        E savedEntity = save(entity);
        flush();
        return savedEntity;
    }

    /**
     * {@link #save(Object)}s the given entity and flushes the persistence context afterward,
     * followed by a refresh (e.g. to load DB trigger modifications).
     *
     * @param entity Entity to save.
     * @return Returns the modified entity.
     */
    default E saveAndFlushAndRefresh(E entity) {
        E savedEntity = saveAndFlush(entity);
        refresh(savedEntity);
        return savedEntity;
    }

    /**
     * Convenience access to {@link org.hibernate.Session#remove(Object)}.
     *
     * @param entity Entity to remove.
     */
    default void remove(E entity) {
        session().remove(entity);
    }

    /**
     * Convenience access to {@link org.hibernate.Session#remove(Object)}
     * with a following flush.
     *
     * @param entity Entity to remove.
     */
    default void removeAndFlush(E entity) {
        remove(entity);
        flush();
    }

    /**
     * Convenience access to {@link org.hibernate.Session#remove(Object)}
     * with a detached entity.
     *
     * @param entity Entity to remove.
     */
    default void attachAndRemove(E entity) {
        if (!session().contains(entity)) {
            entity = session().merge(entity);
        }
        remove(entity);
    }

    /**
     * Convenience access to {@link org.hibernate.Session#refresh(Object)}.
     *
     * @param entity Entity to refresh.
     */
    default void refresh(E entity) {
        session().refresh(entity);
    }

    /**
     * Convenience access to {@link org.hibernate.Session#flush()}.
     */
    default void flush() {
        session().flush();
    }

    /**
     * Convenience access to {@link org.hibernate.Session#detach()}.
     */
    default void detach(E entity) {
        session().detach(entity);
    }

    /**
     * Return the id/primary key of the entity. If the entity does not yet have an id,
     * returns null or, for primitive numeric primary key types, a {@link Number} whose {@code longValue() == 0L}.
     *
     * @param entity The entity.
     * @return id/primary key of the entity
     */
    @SuppressWarnings("unchecked")
    default PK getPrimaryKey(E entity) {
        return (PK) session().getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .getIdentifier(entity);
    }

}
