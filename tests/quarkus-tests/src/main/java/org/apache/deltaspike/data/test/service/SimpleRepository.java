/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.data.test.service;

import ee.hiberspike.data.EntityRepository;
import jakarta.persistence.OrderBy;
import org.apache.deltaspike.data.test.domain.Simple;
import org.hibernate.annotations.processing.Find;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.annotations.processing.Pattern;
import org.hibernate.annotations.processing.SQL;
import org.hibernate.query.Page;

import java.util.List;
import java.util.Optional;

public interface SimpleRepository extends EntityRepository<Simple, Long> {

    @HQL("select s from Simple s where s.name = ?1 and s.enabled = ?2 order by s.id asc")
    List<Simple> findByNamedQueryIndexed(String name, Boolean enabled);

    @HQL("select e from Simple e where e.name like ?1")
    Simple findByNameOptional(String name);

    @HQL("select e from Simple e where e.name like ?1")
    Simple findByNameAny(String name);

    @HQL("select s from Simple s where s.name = ?1 and s.enabled = ?2 order by s.id asc")
    List<Simple> findByNamedQueryRestricted(String name, Boolean enabled, Page page);

    // TODO: lock = PESSIMISTIC_WRITE
    @HQL("select s from Simple s where s.id = :id and s.enabled = :enabled")
    Simple findByNamedQueryNamed(Long id, Boolean enabled);

    @HQL("select s from Simple s where s.name = ?1")
    Simple findByQuery(String name);

    @HQL("select count(s) from Simple s where s.name = ?1")
    Long findCountByQuery(String name);

    @Find
    Simple findByNameAndEnabled(String name, Boolean enabled);

    @Find
    Optional<Simple> findOptionalByName(String name);

    @Find
    @OrderBy("name")
    List<Simple> findAllOrderByName();

    // TODO: naming convention based queries rewritten with HQL

    @HQL("where name ilike :name")
    Simple findByNameLikeIgnoreCase(@Pattern String name);

    @HQL("where lower(name) = lower(:name)")
    Simple findByNameIgnoreCase(String name);

    @HQL("where name = :name order by name limit 1")
    Simple findAnyByName(String name);

    @HQL("where name = :name order by name limit 2")
    List<Simple> findFirst2ByName(String name);

    default List<Simple> findTop2ByName(String name) {
        return findFirst2ByName(name);
    }

    @HQL("from Simple s order by name limit 3")
    List<Simple> findFirst3OrderByName();

    @HQL("select count(s) from Simple s where s.name = :name")
    long countByName(String name);

    @HQL("from Simple s order by s.counter asc, s.id desc")
    List<Simple> findByOrderByCounterAscIdDesc();

    @SQL(value = "SELECT * from SIMPLE_TABLE s WHERE s.name = ?1")
    List<Simple> findWithNative(String name);

    @HQL("update Simple as s set s.name = ?1 where s.id = ?2")
    int updateNameForId(String name, Long id);

    @HQL("delete from Simple s where s.name = :name")
    void deleteByName(String name);

    default void removeByName(String name) {
        deleteByName(name);
    }

    @HQL("delete from Simple s where s.name = :name and s.enabled = :enable")
    void deleteByNameAndEnabled(String name, boolean enable);

    default void removeByNameAndEnabled(String name, Boolean aTrue) {
        deleteByNameAndEnabled(name, aTrue);
    }

}
