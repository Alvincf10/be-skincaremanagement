package com.skincare.bee.service;

import com.skincare.bee.service.dto.IngridientDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.skincare.bee.domain.Ingridient}.
 */
public interface IngridientService {
    /**
     * Save a ingridient.
     *
     * @param ingridientDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<IngridientDTO> save(IngridientDTO ingridientDTO);

    /**
     * Updates a ingridient.
     *
     * @param ingridientDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<IngridientDTO> update(IngridientDTO ingridientDTO);

    /**
     * Partially updates a ingridient.
     *
     * @param ingridientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<IngridientDTO> partialUpdate(IngridientDTO ingridientDTO);

    /**
     * Get all the ingridients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<IngridientDTO> findAll(Pageable pageable);

    /**
     * Returns the number of ingridients available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" ingridient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<IngridientDTO> findOne(Long id);

    /**
     * Delete the "id" ingridient.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
