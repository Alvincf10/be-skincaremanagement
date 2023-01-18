package com.skincare.bee.repository;

import com.skincare.bee.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long>, ProductRepositoryInternal {
    Flux<Product> findAllBy(Pageable pageable);

    @Query("SELECT * FROM product entity WHERE entity.stock_id = :id")
    Flux<Product> findByStock(Long id);

    @Query("SELECT * FROM product entity WHERE entity.stock_id IS NULL")
    Flux<Product> findAllWhereStockIsNull();

    @Query("SELECT * FROM product entity WHERE entity.category_id = :id")
    Flux<Product> findByCategory(Long id);

    @Query("SELECT * FROM product entity WHERE entity.category_id IS NULL")
    Flux<Product> findAllWhereCategoryIsNull();

    @Query("SELECT * FROM product entity WHERE entity.ingridient_id = :id")
    Flux<Product> findByIngridient(Long id);

    @Query("SELECT * FROM product entity WHERE entity.ingridient_id IS NULL")
    Flux<Product> findAllWhereIngridientIsNull();

    @Override
    <S extends Product> Mono<S> save(S entity);

    @Override
    Flux<Product> findAll();

    @Override
    Mono<Product> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ProductRepositoryInternal {
    <S extends Product> Mono<S> save(S entity);

    Flux<Product> findAllBy(Pageable pageable);

    Flux<Product> findAll();

    Mono<Product> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Product> findAllBy(Pageable pageable, Criteria criteria);

}
