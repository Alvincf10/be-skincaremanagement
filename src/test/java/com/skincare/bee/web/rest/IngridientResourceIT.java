package com.skincare.bee.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.skincare.bee.IntegrationTest;
import com.skincare.bee.domain.Ingridient;
import com.skincare.bee.repository.EntityManager;
import com.skincare.bee.repository.IngridientRepository;
import com.skincare.bee.service.dto.IngridientDTO;
import com.skincare.bee.service.mapper.IngridientMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link IngridientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IngridientResourceIT {

    private static final String DEFAULT_INGRIDIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INGRIDIENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ingridients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngridientRepository ingridientRepository;

    @Autowired
    private IngridientMapper ingridientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ingridient ingridient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingridient createEntity(EntityManager em) {
        Ingridient ingridient = new Ingridient().ingridientName(DEFAULT_INGRIDIENT_NAME);
        return ingridient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingridient createUpdatedEntity(EntityManager em) {
        Ingridient ingridient = new Ingridient().ingridientName(UPDATED_INGRIDIENT_NAME);
        return ingridient;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ingridient.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        ingridient = createEntity(em);
    }

    @Test
    void createIngridient() throws Exception {
        int databaseSizeBeforeCreate = ingridientRepository.findAll().collectList().block().size();
        // Create the Ingridient
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeCreate + 1);
        Ingridient testIngridient = ingridientList.get(ingridientList.size() - 1);
        assertThat(testIngridient.getIngridientName()).isEqualTo(DEFAULT_INGRIDIENT_NAME);
    }

    @Test
    void createIngridientWithExistingId() throws Exception {
        // Create the Ingridient with an existing ID
        ingridient.setId(1L);
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);

        int databaseSizeBeforeCreate = ingridientRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllIngridients() {
        // Initialize the database
        ingridientRepository.save(ingridient).block();

        // Get all the ingridientList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(ingridient.getId().intValue()))
            .jsonPath("$.[*].ingridientName")
            .value(hasItem(DEFAULT_INGRIDIENT_NAME));
    }

    @Test
    void getIngridient() {
        // Initialize the database
        ingridientRepository.save(ingridient).block();

        // Get the ingridient
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ingridient.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ingridient.getId().intValue()))
            .jsonPath("$.ingridientName")
            .value(is(DEFAULT_INGRIDIENT_NAME));
    }

    @Test
    void getNonExistingIngridient() {
        // Get the ingridient
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIngridient() throws Exception {
        // Initialize the database
        ingridientRepository.save(ingridient).block();

        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();

        // Update the ingridient
        Ingridient updatedIngridient = ingridientRepository.findById(ingridient.getId()).block();
        updatedIngridient.ingridientName(UPDATED_INGRIDIENT_NAME);
        IngridientDTO ingridientDTO = ingridientMapper.toDto(updatedIngridient);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ingridientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
        Ingridient testIngridient = ingridientList.get(ingridientList.size() - 1);
        assertThat(testIngridient.getIngridientName()).isEqualTo(UPDATED_INGRIDIENT_NAME);
    }

    @Test
    void putNonExistingIngridient() throws Exception {
        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();
        ingridient.setId(count.incrementAndGet());

        // Create the Ingridient
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ingridientDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIngridient() throws Exception {
        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();
        ingridient.setId(count.incrementAndGet());

        // Create the Ingridient
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIngridient() throws Exception {
        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();
        ingridient.setId(count.incrementAndGet());

        // Create the Ingridient
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIngridientWithPatch() throws Exception {
        // Initialize the database
        ingridientRepository.save(ingridient).block();

        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();

        // Update the ingridient using partial update
        Ingridient partialUpdatedIngridient = new Ingridient();
        partialUpdatedIngridient.setId(ingridient.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIngridient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIngridient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
        Ingridient testIngridient = ingridientList.get(ingridientList.size() - 1);
        assertThat(testIngridient.getIngridientName()).isEqualTo(DEFAULT_INGRIDIENT_NAME);
    }

    @Test
    void fullUpdateIngridientWithPatch() throws Exception {
        // Initialize the database
        ingridientRepository.save(ingridient).block();

        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();

        // Update the ingridient using partial update
        Ingridient partialUpdatedIngridient = new Ingridient();
        partialUpdatedIngridient.setId(ingridient.getId());

        partialUpdatedIngridient.ingridientName(UPDATED_INGRIDIENT_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIngridient.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIngridient))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
        Ingridient testIngridient = ingridientList.get(ingridientList.size() - 1);
        assertThat(testIngridient.getIngridientName()).isEqualTo(UPDATED_INGRIDIENT_NAME);
    }

    @Test
    void patchNonExistingIngridient() throws Exception {
        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();
        ingridient.setId(count.incrementAndGet());

        // Create the Ingridient
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ingridientDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIngridient() throws Exception {
        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();
        ingridient.setId(count.incrementAndGet());

        // Create the Ingridient
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIngridient() throws Exception {
        int databaseSizeBeforeUpdate = ingridientRepository.findAll().collectList().block().size();
        ingridient.setId(count.incrementAndGet());

        // Create the Ingridient
        IngridientDTO ingridientDTO = ingridientMapper.toDto(ingridient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ingridientDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ingridient in the database
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIngridient() {
        // Initialize the database
        ingridientRepository.save(ingridient).block();

        int databaseSizeBeforeDelete = ingridientRepository.findAll().collectList().block().size();

        // Delete the ingridient
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ingridient.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ingridient> ingridientList = ingridientRepository.findAll().collectList().block();
        assertThat(ingridientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
