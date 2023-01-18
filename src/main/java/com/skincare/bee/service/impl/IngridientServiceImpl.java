package com.skincare.bee.service.impl;

import com.skincare.bee.domain.Ingridient;
import com.skincare.bee.repository.IngridientRepository;
import com.skincare.bee.service.IngridientService;
import com.skincare.bee.service.dto.IngridientDTO;
import com.skincare.bee.service.mapper.IngridientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Ingridient}.
 */
@Service
@Transactional
public class IngridientServiceImpl implements IngridientService {

    private final Logger log = LoggerFactory.getLogger(IngridientServiceImpl.class);

    private final IngridientRepository ingridientRepository;

    private final IngridientMapper ingridientMapper;

    public IngridientServiceImpl(IngridientRepository ingridientRepository, IngridientMapper ingridientMapper) {
        this.ingridientRepository = ingridientRepository;
        this.ingridientMapper = ingridientMapper;
    }

    @Override
    public Mono<IngridientDTO> save(IngridientDTO ingridientDTO) {
        log.debug("Request to save Ingridient : {}", ingridientDTO);
        return ingridientRepository.save(ingridientMapper.toEntity(ingridientDTO)).map(ingridientMapper::toDto);
    }

    @Override
    public Mono<IngridientDTO> update(IngridientDTO ingridientDTO) {
        log.debug("Request to update Ingridient : {}", ingridientDTO);
        return ingridientRepository.save(ingridientMapper.toEntity(ingridientDTO)).map(ingridientMapper::toDto);
    }

    @Override
    public Mono<IngridientDTO> partialUpdate(IngridientDTO ingridientDTO) {
        log.debug("Request to partially update Ingridient : {}", ingridientDTO);

        return ingridientRepository
            .findById(ingridientDTO.getId())
            .map(existingIngridient -> {
                ingridientMapper.partialUpdate(existingIngridient, ingridientDTO);

                return existingIngridient;
            })
            .flatMap(ingridientRepository::save)
            .map(ingridientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IngridientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ingridients");
        return ingridientRepository.findAllBy(pageable).map(ingridientMapper::toDto);
    }

    public Mono<Long> countAll() {
        return ingridientRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<IngridientDTO> findOne(Long id) {
        log.debug("Request to get Ingridient : {}", id);
        return ingridientRepository.findById(id).map(ingridientMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Ingridient : {}", id);
        return ingridientRepository.deleteById(id);
    }
}
