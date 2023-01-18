package com.skincare.bee.web.rest;

import com.skincare.bee.repository.IngridientRepository;
import com.skincare.bee.service.IngridientService;
import com.skincare.bee.service.dto.IngridientDTO;
import com.skincare.bee.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.skincare.bee.domain.Ingridient}.
 */
@RestController
@RequestMapping("/api")
public class IngridientResource {

    private final Logger log = LoggerFactory.getLogger(IngridientResource.class);

    private static final String ENTITY_NAME = "ingridient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngridientService ingridientService;

    private final IngridientRepository ingridientRepository;

    public IngridientResource(IngridientService ingridientService, IngridientRepository ingridientRepository) {
        this.ingridientService = ingridientService;
        this.ingridientRepository = ingridientRepository;
    }

    /**
     * {@code POST  /ingridients} : Create a new ingridient.
     *
     * @param ingridientDTO the ingridientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingridientDTO, or with status {@code 400 (Bad Request)} if the ingridient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingridients")
    public Mono<ResponseEntity<IngridientDTO>> createIngridient(@RequestBody IngridientDTO ingridientDTO) throws URISyntaxException {
        log.debug("REST request to save Ingridient : {}", ingridientDTO);
        if (ingridientDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingridient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ingridientService
            .save(ingridientDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ingridients/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ingridients/:id} : Updates an existing ingridient.
     *
     * @param id the id of the ingridientDTO to save.
     * @param ingridientDTO the ingridientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingridientDTO,
     * or with status {@code 400 (Bad Request)} if the ingridientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingridientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingridients/{id}")
    public Mono<ResponseEntity<IngridientDTO>> updateIngridient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IngridientDTO ingridientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ingridient : {}, {}", id, ingridientDTO);
        if (ingridientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingridientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ingridientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ingridientService
                    .update(ingridientDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /ingridients/:id} : Partial updates given fields of an existing ingridient, field will ignore if it is null
     *
     * @param id the id of the ingridientDTO to save.
     * @param ingridientDTO the ingridientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingridientDTO,
     * or with status {@code 400 (Bad Request)} if the ingridientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ingridientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingridientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingridients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IngridientDTO>> partialUpdateIngridient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IngridientDTO ingridientDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingridient partially : {}, {}", id, ingridientDTO);
        if (ingridientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingridientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ingridientRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IngridientDTO> result = ingridientService.partialUpdate(ingridientDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /ingridients} : get all the ingridients.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingridients in body.
     */
    @GetMapping("/ingridients")
    public Mono<ResponseEntity<List<IngridientDTO>>> getAllIngridients(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Ingridients");
        return ingridientService
            .countAll()
            .zipWith(ingridientService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /ingridients/:id} : get the "id" ingridient.
     *
     * @param id the id of the ingridientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingridientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingridients/{id}")
    public Mono<ResponseEntity<IngridientDTO>> getIngridient(@PathVariable Long id) {
        log.debug("REST request to get Ingridient : {}", id);
        Mono<IngridientDTO> ingridientDTO = ingridientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingridientDTO);
    }

    /**
     * {@code DELETE  /ingridients/:id} : delete the "id" ingridient.
     *
     * @param id the id of the ingridientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingridients/{id}")
    public Mono<ResponseEntity<Void>> deleteIngridient(@PathVariable Long id) {
        log.debug("REST request to delete Ingridient : {}", id);
        return ingridientService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
