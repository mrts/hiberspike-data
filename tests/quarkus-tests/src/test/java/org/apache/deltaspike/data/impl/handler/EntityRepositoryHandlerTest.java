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
package org.apache.deltaspike.data.impl.handler;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.apache.deltaspike.data.test.domain.Simple;
import org.apache.deltaspike.data.test.domain.SimpleStringId;
import org.apache.deltaspike.data.test.service.ExtendedRepositoryInterface;
import org.apache.deltaspike.data.test.service.SimpleIntermediateRepository;
import org.apache.deltaspike.data.test.service.SimpleStringIdRepository;
import org.apache.deltaspike.data.test.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@TestTransaction
public class EntityRepositoryHandlerTest {

    @Inject
    ExtendedRepositoryInterface repo;

    @Inject
    SimpleStringIdRepository stringIdRepo;

    @Inject
    SimpleIntermediateRepository intermediate;

    @Inject
    EntityManager entityManager;

    TestData testData;

    @BeforeEach
    public void init() {
        testData = new TestData(entityManager);
    }

    @Test
    public void should_save() throws Exception {
        // given
        Simple simple = new Simple("test");

        // when
        simple = repo.save(simple);

        // then
        assertNotNull(simple.getId());
    }

    @Test
    public void should_merge() throws Exception {
        // given
        Simple simple = testData.createSimple("testMerge");
        Long id = simple.getId();

        // when
        final String newName = "testMergeUpdated";
        simple.setName(newName);
        simple = repo.save(simple);

        // then
        assertEquals(id, simple.getId());
        assertEquals(newName, simple.getName());
    }

    @Test
    public void should_save_and_flush() throws Exception {
        // given
        Simple simple = new Simple("test");

        // when
        simple = repo.saveAndFlush(simple);
        Simple fetch = (Simple) entityManager
                .createNativeQuery("select * from SIMPLE_TABLE where id = ?", Simple.class)
                .setParameter(1, simple.getId())
                .getSingleResult();

        // then
        assertEquals(simple.getId(), fetch.getId());
    }

    @Test
    public void should_save_with_string_id() {
        // given
        SimpleStringId foo = new SimpleStringId("foo", "bar");

        // when
        foo = stringIdRepo.save(foo);

        // then
        assertNotNull(foo);
    }


    @Test
    public void should_refresh() throws Exception {
        // given
        final String name = "testRefresh";
        Simple simple = testData.createSimple(name);

        // when
        simple.setName("override");
        repo.refresh(simple);

        // then
        assertEquals(name, simple.getName());
    }

    @Test
    public void should_find_by_pk() throws Exception {
        // given
        Simple simple = testData.createSimple("testFindByPk");

        // when
        Simple find = repo.findBy(simple.getId());

        // then
        assertEquals(simple.getName(), find.getName());
    }

    @Test
    public void should_find__by_pk() throws Exception {
        // given
        Simple simple = testData.createSimple("testFindByPk");

        // when
        Optional<Simple> find = repo.findOptionalBy(simple.getId());

        // then
        assertEquals(simple.getName(), find.get().getName());
    }

    @Test
    public void should_find_all() {
        // given
        testData.createSimple("testFindAll1");
        testData.createSimple("testFindAll2");

        // when
        List<Simple> find = repo.findAll();

        // then
        assertEquals(2, find.size());
    }

    @Test
    public void should_find_by_all_with_start_and_max() {
        // given
        testData.createSimple("testFindAll1");
        testData.createSimple("testFindAll2");

        // when
        List<Simple> find = repo.findAll(0, 1);

        // then
        assertEquals(1, find.size());
    }

    @Test
    public void should_count_all() {
        // given
        testData.createSimple("testCountAll");

        // when
        Long result = repo.count();

        // then
        assertEquals(Long.valueOf(1), result);
    }

    @Test
    public void should_remove() {
        // given
        Simple simple = testData.createSimple("testRemove");

        // when
        repo.remove(simple);
        repo.flush();
        Simple lookup = entityManager.find(Simple.class, simple.getId());

        // then
        assertNull(lookup);
    }

    @Test
    public void should_remove_and_flush() {
        // given
        Simple simple = testData.createSimple("testRemoveAndFlush");

        // when
        repo.removeAndFlush(simple);
        Simple lookup = entityManager.find(Simple.class, simple.getId());

        // then
        assertNull(lookup);
    }

    @Test
    public void should_remove_detach_entity() {
        //given
        Simple simple = testData.createSimple("testeAttachAndRemove");

        //when
        repo.detach(simple);
        repo.attachAndRemove(simple);
        repo.flush();
        Simple lookup = entityManager.find(Simple.class, simple.getId());

        // then
        assertNull(lookup);
    }

    @Test
    public void should_return_entity_primary_key() {
        //given
        Simple simple = testData.createSimple("should_return_entity_primary_key");
        Long id = simple.getId();

        //when
        Long primaryKey = repo.getPrimaryKey(simple);

        // then
        assertNotNull(primaryKey);
        assertEquals(id, primaryKey);
    }

    @Test
    public void should_return_null_primary_key() {
        //given
        Simple simple = new Simple("should_return_null_primary_key");

        //when
        Long primaryKey = repo.getPrimaryKey(simple);

        // then
        assertNull(primaryKey);
    }

    @Test
    public void should_return_entity_primary_key_detached_entity() {
        //given
        Simple simple = testData.createSimple("should_return_entity_primary_key");
        Long id = simple.getId();

        //when
        entityManager.detach(simple);
        Long primaryKey = repo.getPrimaryKey(simple);

        // then
        assertNotNull(primaryKey);
        assertEquals(id, primaryKey);
    }

    @Test
    public void should_query_with_hints() {
        Simple simple = testData.createSimple("should_return_entity_primary_key");
        Long id = simple.getId();

        entityManager.flush();
        entityManager.clear();

        Simple found = intermediate.findBy(id);

        assertEquals(id, found.getId());
    }

    @Test
    public void should_query_names() {
        String name = "should_return_entity_primary_key";
        testData.createSimple(name);

        List<String> names = intermediate.findAllNames();

        assertEquals(name, names.get(0));
    }

    @Test
    public void should_query_by_name() {
        String name = "should_return_entity_primary_key";
        Simple simple = testData.createSimple(name);

        Simple byName = stringIdRepo.findByName(name);

        assertEquals(simple, byName);
    }

    @Test
    public void should_query_list_by_name() {
        String name = "should_return_entity_primary_key";
        Simple simple = testData.createSimple(name);

        List<Simple> byName = stringIdRepo.findByName2(name);

        assertEquals(1, byName.size());
        assertEquals(simple, byName.get(0));
    }
}

