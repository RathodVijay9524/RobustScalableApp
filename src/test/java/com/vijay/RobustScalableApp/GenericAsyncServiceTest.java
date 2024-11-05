package com.vijay.RobustScalableApp;

import com.vijay.RobustScalableApp.exception.ResourceNotFoundException;
import com.vijay.RobustScalableApp.service.Impl.GenericAsyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenericAsyncServiceTest {

    @Mock
    private JpaRepository<TestEntity, Long> repository;

    private GenericAsyncService<TestEntity, Long> genericAsyncService;

    private final Executor synchronousExecutor = Runnable::run;

    @BeforeEach
    void setUp() {
        genericAsyncService = new GenericAsyncService<TestEntity, Long>(repository, TestEntity.class, synchronousExecutor) {};
    }

    @Test
    void testCreate() {
        TestEntity entity = new TestEntity(1L, "Test Name");
        when(repository.save(entity)).thenReturn(entity);

        CompletableFuture<TestEntity> future = genericAsyncService.create(entity);

        assertDoesNotThrow(() -> {
            TestEntity result = future.get();
            assertEquals(entity, result);
        });
        verify(repository, times(1)).save(entity);
    }

    @Test
    void testGetById_Found() {
        Long id = 1L;
        TestEntity entity = new TestEntity(id, "Test Name");
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CompletableFuture<TestEntity> future = genericAsyncService.getById(id);

        assertDoesNotThrow(() -> {
            TestEntity result = future.get();
            assertEquals(entity, result);
        });
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetById_NotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        CompletableFuture<TestEntity> future = genericAsyncService.getById(id);

        CompletionException thrown = assertThrows(CompletionException.class, future::join);
        assertEquals(ResourceNotFoundException.class, thrown.getCause().getClass());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetAll() {
        TestEntity entity1 = new TestEntity(1L, "Test Name 1");
        TestEntity entity2 = new TestEntity(2L, "Test Name 2");
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        CompletableFuture<List<TestEntity>> future = genericAsyncService.getAll();

        assertDoesNotThrow(() -> {
            List<TestEntity> result = future.get();
            assertEquals(2, result.size());
            assertTrue(result.contains(entity1));
            assertTrue(result.contains(entity2));
        });
        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        TestEntity existingEntity = new TestEntity(id, "Old Name");
        TestEntity newEntity = new TestEntity(id, "New Name");

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(repository.save(existingEntity)).thenReturn(newEntity);

        CompletableFuture<TestEntity> future = genericAsyncService.update(newEntity, id);

        assertDoesNotThrow(() -> {
            TestEntity result = future.get();
            assertEquals(newEntity.getName(), result.getName());
        });
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(existingEntity);
    }

    @Test
    void testDelete() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        CompletableFuture<Void> future = genericAsyncService.delete(id);

        assertDoesNotThrow(() -> future.get());
        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_NotFound() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        CompletableFuture<Void> future = genericAsyncService.delete(id);

        CompletionException thrown = assertThrows(CompletionException.class, future::join);
        assertEquals(ResourceNotFoundException.class, thrown.getCause().getClass());
        verify(repository, times(1)).existsById(id);
        verify(repository, never()).deleteById(id);
    }

    // Sample entity class for testing purposes

    static class TestEntity {
        private Long id;
        private String name;

        // Constructor, Getters, Setters, equals, and hashCode methods

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }

        // equals and hashCode should be overridden for proper comparison in assertions
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestEntity that = (TestEntity) o;
            return id.equals(that.id) && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }
}

