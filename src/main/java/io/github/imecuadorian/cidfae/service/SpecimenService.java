package io.github.imecuadorian.cidfae.service;

import io.github.imecuadorian.cidfae.model.Specimen;
import io.github.imecuadorian.cidfae.repository.Repository;
import io.github.imecuadorian.cidfae.repository.impl.SpecimenRepository;

import java.util.List;
import java.util.Optional;

public class SpecimenService {

    private final Repository<Integer, Specimen> repository;

    public SpecimenService() {
        this.repository = new SpecimenRepository();
    }

    public boolean createSpecimen(Specimen specimen) {
        return repository.save(specimen);
    }

    public Optional<Specimen> getSpecimenById(int id) {
        return repository.findByKey(id);
    }

    public List<Specimen> getAllSpecimens() {
        return repository.findAll();
    }

    public List<Specimen> getSpecimensByBatchId(int batchId) {
        if (repository instanceof SpecimenRepository repo) {
            return repo.findByBatchId(batchId);
        }
        return List.of();
    }

    public boolean updateSpecimen(Specimen specimen) {
        if (repository instanceof SpecimenRepository repo) {
            return repo.update(specimen);
        }
        return false;
    }

    public boolean deleteSpecimen(int id) {
        if (repository instanceof SpecimenRepository repo) {
            return repo.delete(id);
        }
        return false;
    }
}
