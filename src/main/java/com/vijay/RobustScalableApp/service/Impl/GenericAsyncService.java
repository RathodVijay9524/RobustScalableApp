package com.vijay.RobustScalableApp.service.Impl;

import com.vijay.RobustScalableApp.exception.ResourceNotFoundException;
import com.vijay.RobustScalableApp.service.AsyncCrudOperations;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Log4j2
public abstract class GenericAsyncService <T,ID> implements AsyncCrudOperations<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final Class<T> entityClass;
    protected final Executor executor;

    protected GenericAsyncService(JpaRepository<T, ID> repository, Class<T> entityClass, Executor executor) {
        this.repository = repository;
        this.entityClass = entityClass;
        this.executor = executor;
    }
    @Override
    public CompletableFuture<T> create(T entity) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating entity: {}", entity);
            return repository.save(entity);
        });
    }

    @Override
    public CompletableFuture<T> getById(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching entity with ID: {}", id);
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName(), "id", id));
        });
    }

    @Override
    public CompletableFuture<List<T>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Fetching all entities");
            return repository.findAll();
        });
    }

    @Override
    public CompletableFuture<T> update(T entity, ID id) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Updating entity with ID: {}", id);
            T existingEntity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(entityClass.getSimpleName(), "id", id));
            // Copy new properties to existing entity, ignoring ID
            BeanUtils.copyProperties(entity, existingEntity, "id");
            return repository.save(existingEntity);
        });
    }

    @Override
    public CompletableFuture<Void> delete(ID id) {
        return CompletableFuture.runAsync(() -> {
            log.info("Deleting entity with ID: {}", id);
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException(entityClass.getSimpleName(), "id",  id);
            }
            repository.deleteById(id);
        });
    }
}
