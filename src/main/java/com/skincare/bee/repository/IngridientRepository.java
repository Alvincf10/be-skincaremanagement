package com.skincare.bee.repository;

import com.skincare.bee.domain.Ingridient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Ingridient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngridientRepository extends ReactiveCrudRepository<Ingridient, Long>, IngridientRepositoryInternal {
    Flux<Ingridient> findAllBy(Pageable pageable);

    @Override
    <S extends Ingridient> Mono<S> save(S entity);

    @Override
    Flux<Ingridient> findAll();

    @Override
    Mono<Ingridient> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IngridientRepositoryInternal {
    <S extends Ingridient> Mono<S> save(S entity);

    Flux<Ingridient> findAllBy(Pageable pageable);

    Flux<Ingridient> findAll();

    Mono<Ingridient> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Ingridient> findAllBy(Pageable pageable, Criteria criteria);

}
