/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.wildfly.guide.testing.data;

import java.time.Instant;
import java.util.List;

import ee.hiberspike.data.EntityRepository;
import org.hibernate.annotations.processing.Find;

import jakarta.validation.constraints.NotNull;

import org.wildfly.guide.testing.model.Task;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public interface TaskRegistry extends EntityRepository<Task, Long> {

    @Find
    Task findBy(Long id);

    /**
     * Returns all available tasks.
     *
     * @return all available tasks
     */
    @Find
    List<Task> getTasks();

    @Find
    List<Task> getTasks(boolean completed);

    @Find
    Task getTaskById(long id);

    /**
     * Adds a task to the repository.
     *
     * @param task the contact to add
     * @return the task
     */
    default Task add(@NotNull Task task) {
        return save(task);
    }

    /**
     * Adds a task to the repository.
     *
     * @param task the contact to add
     * @return the task
     */
    default Task updateOrAdd(@NotNull Task task) {
        return save(task);
    }

    /**
     * Updates the completed status of a task.
     *
     * @param id   the task ID
     * @param task the partial task
     * @return the updated task
     */
    default Task updateCompleted(final long id, final Task task) {
        final Task taskToUpdate = findBy(id);
        if (taskToUpdate != null) {
            taskToUpdate.setCompleted(task.isCompleted());
            taskToUpdate.setUpdated(Instant.now());
            return save(taskToUpdate);
        }
        throw new IllegalArgumentException("Task with id " + task.getId() + " does not exist");
    }

    default Task remove(final long id) {
        final Task task = findBy(id);
        if (task != null) {
            remove(task);
            return task;
        }
        return null;
    }
}
