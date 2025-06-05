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
package org.apache.deltaspike.data.test.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = Simple.BY_NAME_LIKE,
                   query = "select e from Simple e where e.name like ?1"),
        @NamedQuery(name = Simple.BY_NAME_ENABLED,
                   query = "select s from Simple s where s.name = ?1 and s.enabled = ?2 order by s.id asc"),
        @NamedQuery(name = Simple.BY_ID,
                   query = "select s from Simple s where s.id = :id and s.enabled = :enabled")
})
@Table(name = "SIMPLE_TABLE")
public class Simple extends SuperSimple
{

    public static final String BY_NAME_LIKE = "simple.byNameLike";
    public static final String BY_NAME_ENABLED = "simple.byNameAndEnabled";
    public static final String BY_ID = "simple.byId";

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String camelCase;
    private Boolean enabled = Boolean.TRUE;
    private Integer counter = Integer.valueOf(0);
    @Temporal(TemporalType.TIMESTAMP)
    private Date temporal;
    private EmbeddedSimple embedded;

    protected Simple()
    {
    }

    public Simple(String name)
    {
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public Integer getCounter()
    {
        return counter;
    }

    public void setCounter(Integer counter)
    {
        this.counter = counter;
    }

    public String getCamelCase()
    {
        return camelCase;
    }

    public void setCamelCase(String camelCase)
    {
        this.camelCase = camelCase;
    }

    public Date getTemporal()
    {
        return temporal;
    }

    public void setTemporal(Date temporal)
    {
        this.temporal = temporal;
    }

    public EmbeddedSimple getEmbedded()
    {
        return embedded;
    }

    public void setEmbedded(EmbeddedSimple embedded)
    {
        this.embedded = embedded;
    }

    @Override
    public String toString()
    {
        return "Simple [id=" + id + ", name=" + name + ", camelCase=" + camelCase + ", enabled=" + enabled
                + ", counter=" + counter + ", temporal=" + temporal + ", embedded=" + embedded + "]";
    }

}
