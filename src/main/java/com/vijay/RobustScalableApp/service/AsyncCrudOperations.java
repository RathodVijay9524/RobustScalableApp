package com.vijay.RobustScalableApp.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncCrudOperations <T, ID> {
    CompletableFuture<T> create(T entity);
    CompletableFuture<T> getById(ID id);

    CompletableFuture<List<T>> getAll();
    CompletableFuture<T> update(T entity, ID id);
    CompletableFuture<Void> delete(ID id);
}
