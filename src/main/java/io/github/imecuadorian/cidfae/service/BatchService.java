package io.github.imecuadorian.cidfae.service;

import io.github.imecuadorian.cidfae.model.Batch;
import io.github.imecuadorian.cidfae.repository.impl.BatchRepository;
import io.github.imecuadorian.cidfae.repository.Repository;

import java.util.List;
import java.util.Optional;

public class BatchService {

    private final Repository<String, Batch> repository;

    public BatchService() {
        this.repository = new BatchRepository();
    }

    public boolean createBatch(Batch batch) {
        return repository.save(batch);
    }

    public Optional<Batch> getBatchByTitle(String title) {
        return repository.findByKey(title);
    }

    public List<Batch> getAllBatches() {
        return repository.findAll();
    }

    public List<Batch> getLast10Batches() {
        if (repository instanceof BatchRepository batchRepo) {
            return batchRepo.findLast10();
        }
        return List.of();
    }
}
