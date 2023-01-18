package com.skincare.bee.service.impl;

import com.skincare.bee.domain.Stock;
import com.skincare.bee.repository.StockRepository;
import com.skincare.bee.service.StockService;
import com.skincare.bee.service.dto.StockDTO;
import com.skincare.bee.service.mapper.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Stock}.
 */
@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;

    private final StockMapper stockMapper;

    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    @Override
    public Mono<StockDTO> save(StockDTO stockDTO) {
        log.debug("Request to save Stock : {}", stockDTO);
        return stockRepository.save(stockMapper.toEntity(stockDTO)).map(stockMapper::toDto);
    }

    @Override
    public Mono<StockDTO> update(StockDTO stockDTO) {
        log.debug("Request to update Stock : {}", stockDTO);
        return stockRepository.save(stockMapper.toEntity(stockDTO)).map(stockMapper::toDto);
    }

    @Override
    public Mono<StockDTO> partialUpdate(StockDTO stockDTO) {
        log.debug("Request to partially update Stock : {}", stockDTO);

        return stockRepository
            .findById(stockDTO.getId())
            .map(existingStock -> {
                stockMapper.partialUpdate(existingStock, stockDTO);

                return existingStock;
            })
            .flatMap(stockRepository::save)
            .map(stockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<StockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAllBy(pageable).map(stockMapper::toDto);
    }

    public Mono<Long> countAll() {
        return stockRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<StockDTO> findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        return stockRepository.findById(id).map(stockMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        return stockRepository.deleteById(id);
    }
}
