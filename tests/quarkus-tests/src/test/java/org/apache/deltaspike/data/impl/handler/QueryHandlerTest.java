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
import org.apache.deltaspike.data.test.domain.Simple2;
import org.apache.deltaspike.data.test.domain.SimpleBuilder;
import org.apache.deltaspike.data.test.service.Simple2Repository;
import org.apache.deltaspike.data.test.service.SimpleRepository;
import org.hibernate.NonUniqueResultException;
import org.hibernate.query.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestTransaction
public class QueryHandlerTest {

    @Inject
    SimpleRepository repo;

    @Inject
    Simple2Repository repo2;

    @Inject
    EntityManager entityManager;

    private SimpleBuilder builder;

    @BeforeEach
    public void init() {
        builder = new SimpleBuilder(entityManager);
    }

    @Test
    public void should_create_named_query_index() {
        // given
        final String name = "testCreateNamedQueryIndex";
        builder.createSimple(name);

        // when
        List<Simple> result = repo.findByNamedQueryIndexed(name, Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }

    @Test
    public void should_create_named_query_named() {
        // given
        final String name = "testCreateNamedQueryNamed";
        Simple simple = builder.createSimple(name);

        // when
        Simple result = repo.findByNamedQueryNamed(simple.getId(), Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    public void should_run_annotated_query() {
        // given
        final String name = "testRunAnnotatedQuery";
        builder.createSimple(name);

        // when
        Simple result = repo.findByQuery(name);

        // then
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    public void should_create_query_by_method_name() {
        // given
        final String name = "testCreateQueryByMethodName";
        builder.createSimple(name);

        // when
        Simple result = repo.findByNameAndEnabled(name, Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    public void should_create_query_delete_by_method_name() {
        // given
        final String name = "testCreateQueryByMethodName";
        builder.createSimple(name);

        // when
        repo.deleteByName(name);
        repo.flush();
        Optional<Simple> result = repo.findAnyByName(name);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    public void should_create_query_remove_by_method_name() {
        // given
        final String name = "testCreateQueryByMethodName";
        builder.createSimple(name);

        // when
        repo.removeByName(name);
        repo.flush();
        Optional<Simple> result = repo.findAnyByName(name);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    public void should_create_query_remove_by_method_name_with_multiply_params() {
        // given
        final String name = "testCreateQueryByMethodName";
        builder.createSimple(name);

        // when
        repo.removeByNameAndEnabled(name, Boolean.TRUE);
        repo.flush();
        Optional<Simple> result = repo.findAnyByName(name);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    public void should_create_query_delete_by_method_name_with_multiply_params() {
        // given
        final String name = "testCreateQueryByMethodName";
        builder.createSimple(name);

        // when
        repo.deleteByNameAndEnabled(name, Boolean.TRUE);
        repo.flush();
        Optional<Simple> result = repo.findAnyByName(name);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    public void should_restrict_result_size_by_annotation() {
        // given
        final String name = "testRestrictResultSizeByAnnotation";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        List<Simple> result = repo.findByNamedQueryIndexed(name, Boolean.TRUE);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void should_restrict_result_size_by_parameters() {
        // given
        final String name = "testRestrictResultSizeByParameters";
        builder.createSimple(name);
        Simple second = builder.createSimple(name);

        // when
        List<Simple> result = repo.findByNamedQueryRestricted(name, Boolean.TRUE, Page.page(1, 1));

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(second.getId(), result.get(0).getId());
    }

    @Test
    public void should_work_with_2nd_repo() {
        // given
        final String name = "testWorkWith2ndRepository";
        Simple2 simple = createSimple2(name);

        // when
        Simple2 result = repo2.findByName(name);

        // then
        assertNotNull(result);
        assertEquals(simple.getId(), result.getId());
        assertEquals(name, result.getName());
    }

    @Test
    public void should_return_aggregate() {
        // given
        final String name = "testReturnAggregate";
        builder.createSimple(name);

        // when
        Long result = repo.findCountByQuery(name);

        // then
        assertNotNull(result);
    }

    @Test
    public void should_find_with_native_query() {
        // given
        final String name = "testFindWithNativeQuery";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        List<Simple> result = repo.findWithNative(name);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertInstanceOf(Simple.class, result.get(0));
        assertEquals(name, result.get(0).getName());
    }

    @Test
    public void should_order_result_by_method_order_by() {
        // given
        final String name = "testFindWithNativeQuery";
        builder.createSimple(name, 33);
        builder.createSimple(name, 66);
        builder.createSimple(name, 66);
        builder.createSimple(name, 22);
        builder.createSimple(name, 55);

        // when
        List<Simple> result = repo.findByOrderByCounterAscIdDesc();

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        long lastId = Long.MAX_VALUE;
        int lastCounter = Integer.MIN_VALUE;
        for (Simple simple : result) {
            long currentId = simple.getId();
            int currentCounter = simple.getCounter();
            if (currentCounter == lastCounter) {
                assertTrue(currentId < lastId);
            } else {
                assertTrue(currentCounter > lastCounter);
            }
            lastId = currentId;
            lastCounter = currentCounter;
        }
    }

    @Test
    public void should_execute_update() {
        // given
        final String name = "testFindWithNativeQuery";
        final String newName = "testFindWithNativeQueryUpdated" + System.currentTimeMillis();
        Simple s = builder.createSimple(name);

        // when
        int count = repo.updateNameForId(newName, s.getId());

        // then
        assertEquals(1, count);
    }

    @Test
    public void should_create_optinal_query_by_name() {
        // given
        final String name = "should_create_optinal_query_by_name";
        builder.createSimple(name);

        // when
        Optional<Simple> result1 = repo.findOptionalByName(name);
        Optional<Simple> result2 = repo.findOptionalByName(name + "_doesnt_exist");

        // then
        assertTrue(result1.isPresent());
        assertEquals(name, result1.get().getName());
        assertTrue(result2.isEmpty());
    }

    @Test
    public void should_create_optinal_query_by_annotation() {
        // given
        final String name = "should_create_optinal_query_by_annotation";
        builder.createSimple(name);

        // when
        Optional<Simple> result1 = repo.findByNameOptional(name);
        Optional<Simple> result2 = repo.findByNameOptional(name + "_doesnt_exist");

        // then
        assertTrue(result1.isPresent());
        assertEquals(name, result1.get().getName());
        assertFalse(result2.isPresent());
    }

    @Test
    public void should_fail_optional_query_by_name_with_nonunique() {
        // given
        final String name = "should_fail_optinal_query_by_name_with_nonunique";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        assertThrows(NonUniqueResultException.class, () -> repo.findOptionalByName(name));
    }

    @Test
    public void should_fail_optional_query_by_annotation_with_nonunique() {
        // given
        final String name = "should_fail_optinal_query_by_annotation_with_nonunique";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        assertThrows(NonUniqueResultException.class, () -> repo.findByNameOptional(name));
    }

    @Test
    public void should_create_any_query_by_name() {
        // given
        final String name = "should_create_any_query_by_name";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        Optional<Simple> result1 = repo.findAnyByName(name);
        Optional<Simple> result2 = repo.findAnyByName(name + "_doesnt_exist");

        // then
        assertTrue(result1.isPresent());
        assertEquals(name, result1.get().getName());
        assertFalse(result2.isPresent());
    }

    @Test
    public void should_create_any_query_by_annotation() {
        // given
        final String name = "should_create_any_query_by_annotation";
        builder.createSimple(name);
        builder.createSimple(name);

        // when
        Optional<Simple> result1 = repo.findByNameAny(name);
        Optional<Simple> result2 = repo.findByNameAny(name + "_doesnt_exist");

        // then
        assertTrue(result1.isPresent());
        assertEquals(name, result1.get().getName());
        assertFalse(result2.isPresent());
    }

    @Test
    public void should_create_case_insensitive_query_for_like_comparator() {
        // given
        final String name = "Should_Create_Case_Insensitive_Query_For_Like";
        builder.createSimple(name);

        // when
        Simple result = repo.findByNameLikeIgnoreCase("should_create_CASE_Insensitive_QUERY_for_l%");

        // then
        assertEquals(name, result.getName());
    }

    @Test
    public void should_create_case_insensitive_query_for_equals_comparator() {
        // given
        final String name = "Should_Create_Case_Insensitive_Query_for_Equals";
        builder.createSimple(name);

        // when
        Simple result = repo.findByNameIgnoreCase(name.toLowerCase());

        // then
        assertEquals(name, result.getName());
    }

    @Test
    public void should_find_first_2() {
        final String name = "Should_Create_Case_Insensitive_Query_for_Equals";
        builder.createSimple(name);
        builder.createSimple(name);
        builder.createSimple(name);
        builder.createSimple("this is something else");

        List<Simple> result = repo.findFirst2ByName(name);

        assertEquals(2, result.size());
    }

    @Test
    public void should_find_top_2() {
        final String name = "Should_Create_Case_Insensitive_Query_for_Equals";
        builder.createSimple(name);
        builder.createSimple(name);
        builder.createSimple(name);
        builder.createSimple("this is something else");

        List<Simple> result = repo.findTop2ByName(name);

        assertEquals(2, result.size());
    }

    @Test
    public void should_find_top_3_ordered() {
        builder.createSimple("zebra");
        builder.createSimple("willow");
        builder.createSimple("kangaroo");
        builder.createSimple("bologna");

        List<Simple> result = repo.findFirst3OrderByName();

        assertEquals("bologna", result.get(0).getName());
        assertEquals("kangaroo", result.get(1).getName());
        assertEquals("willow", result.get(2).getName());
    }

    @Test
    public void should_find_all_ordered() {
        builder.createSimple("zebra");
        builder.createSimple("willow");
        builder.createSimple("kangaroo");
        builder.createSimple("bologna");

        List<Simple> result = repo.findAllOrderByName();

        assertEquals("bologna", result.get(0).getName());
        assertEquals("kangaroo", result.get(1).getName());
        assertEquals("willow", result.get(2).getName());
        assertEquals("zebra", result.get(3).getName());
    }

    @Test
    public void should_count_by_name() {
        builder.createSimple("zebra");
        builder.createSimple("zebra");
        builder.createSimple("willow");
        builder.createSimple("kangaroo");
        builder.createSimple("kangaroo");
        builder.createSimple("kangaroo");
        builder.createSimple("bologna");

        assertEquals(1, repo.countByName("bologna"));
        assertEquals(3, repo.countByName("kangaroo"));
        assertEquals(1, repo.countByName("willow"));
        assertEquals(2, repo.countByName("zebra"));
    }

    private Simple2 createSimple2(String name) {
        Simple2 result = new Simple2(name);
        entityManager.persist(result);
        entityManager.flush();
        return result;
    }

}
