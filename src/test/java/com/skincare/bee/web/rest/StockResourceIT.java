package com.skincare.bee.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.skincare.bee.IntegrationTest;
import com.skincare.bee.domain.Stock;
import com.skincare.bee.repository.EntityManager;
import com.skincare.bee.repository.StockRepository;
import com.skincare.bee.service.dto.StockDTO;
import com.skincare.bee.service.mapper.StockMapper;
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
 * Integration tests for the {@link StockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class StockResourceIT {

    private static final Integer DEFAULT_QUANTITY_STOCK = 1;
    private static final Integer UPDATED_QUANTITY_STOCK = 2;

    private static final String ENTITY_API_URL = "/api/stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Stock stock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock().quantityStock(DEFAULT_QUANTITY_STOCK);
        return stock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createUpdatedEntity(EntityManager em) {
        Stock stock = new Stock().quantityStock(UPDATED_QUANTITY_STOCK);
        return stock;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Stock.class).block();
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
        stock = createEntity(em);
    }

    @Test
    void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().collectList().block().size();
        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantityStock()).isEqualTo(DEFAULT_QUANTITY_STOCK);
    }

    @Test
    void createStockWithExistingId() throws Exception {
        // Create the Stock with an existing ID
        stock.setId(1L);
        StockDTO stockDTO = stockMapper.toDto(stock);

        int databaseSizeBeforeCreate = stockRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllStocks() {
        // Initialize the database
        stockRepository.save(stock).block();

        // Get all the stockList
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
            .value(hasItem(stock.getId().intValue()))
            .jsonPath("$.[*].quantityStock")
            .value(hasItem(DEFAULT_QUANTITY_STOCK));
    }

    @Test
    void getStock() {
        // Initialize the database
        stockRepository.save(stock).block();

        // Get the stock
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, stock.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(stock.getId().intValue()))
            .jsonPath("$.quantityStock")
            .value(is(DEFAULT_QUANTITY_STOCK));
    }

    @Test
    void getNonExistingStock() {
        // Get the stock
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingStock() throws Exception {
        // Initialize the database
        stockRepository.save(stock).block();

        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).block();
        updatedStock.quantityStock(UPDATED_QUANTITY_STOCK);
        StockDTO stockDTO = stockMapper.toDto(updatedStock);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, stockDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantityStock()).isEqualTo(UPDATED_QUANTITY_STOCK);
    }

    @Test
    void putNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, stockDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStockWithPatch() throws Exception {
        // Initialize the database
        stockRepository.save(stock).block();

        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();

        // Update the stock using partial update
        Stock partialUpdatedStock = new Stock();
        partialUpdatedStock.setId(stock.getId());

        partialUpdatedStock.quantityStock(UPDATED_QUANTITY_STOCK);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStock.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStock))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantityStock()).isEqualTo(UPDATED_QUANTITY_STOCK);
    }

    @Test
    void fullUpdateStockWithPatch() throws Exception {
        // Initialize the database
        stockRepository.save(stock).block();

        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();

        // Update the stock using partial update
        Stock partialUpdatedStock = new Stock();
        partialUpdatedStock.setId(stock.getId());

        partialUpdatedStock.quantityStock(UPDATED_QUANTITY_STOCK);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStock.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStock))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantityStock()).isEqualTo(UPDATED_QUANTITY_STOCK);
    }

    @Test
    void patchNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, stockDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().collectList().block().size();
        stock.setId(count.incrementAndGet());

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(stockDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStock() {
        // Initialize the database
        stockRepository.save(stock).block();

        int databaseSizeBeforeDelete = stockRepository.findAll().collectList().block().size();

        // Delete the stock
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, stock.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Stock> stockList = stockRepository.findAll().collectList().block();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
