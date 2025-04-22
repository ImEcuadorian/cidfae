package io.github.imecuadorian.cidfae.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<K, V> {
    boolean save(V entity);
    Optional<V> findByKey(K key);
    List<V> findAll();
}
