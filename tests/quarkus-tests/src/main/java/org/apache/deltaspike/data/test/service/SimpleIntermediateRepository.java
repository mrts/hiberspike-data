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
import org.apache.deltaspike.data.test.domain.Simple;
import org.hibernate.annotations.processing.Find;
import org.hibernate.annotations.processing.SQL;

import java.util.List;

public interface SimpleIntermediateRepository extends EntityRepository<Simple, Long>
{
    // TODO:
    //    @Query(hints = {
    //            @QueryHint(name = "openjpa.hint.OptimizeResultCount", value = "some.invalid.value"),
    //            @QueryHint(name = "org.hibernate.comment", value = "I'm a little comment short and stout")
    //    })
    @Find
    Simple findBy(Long id);

    // @Query(value = "select name from simple_table", isNative = true)
    @SQL("select name from simple_table")
    List<String> findAllNames();
}
