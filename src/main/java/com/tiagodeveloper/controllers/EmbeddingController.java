package com.tiagodeveloper.controllers;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.postgresml.PostgresMlEmbeddingModel;
import org.springframework.ai.postgresml.PostgresMlEmbeddingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmbeddingController(EmbeddingModel embeddingModel, JdbcTemplate jdbcTemplate) {
        this.embeddingModel = embeddingModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/ai/embedding")
    public Map<String, EmbeddingResponse> embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }
    @GetMapping("/ai/embedding/jdbc")
    public Map<String, EmbeddingResponse> embedJdbc(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {

        PostgresMlEmbeddingModel embeddingModel = new PostgresMlEmbeddingModel(this.jdbcTemplate,
                PostgresMlEmbeddingOptions.builder()
                        .withTransformer("distilbert-base-uncased") // huggingface transformer model name.
                        .withVectorType(PostgresMlEmbeddingModel.VectorType.PG_VECTOR) //vector type in PostgreSQL.
                        .withKwargs(Map.of("device", "cpu")) // optional arguments.
                        .withMetadataMode(MetadataMode.EMBED) // Document metadata mode.
                        .build());

        embeddingModel.afterPropertiesSet(); // initialize the jdbc template and database.

        EmbeddingResponse embeddingResponse = embeddingModel
                .embedForResponse(List.of("Hello World", "World is big and salvation is near"));
        return Map.of("embedding", embeddingResponse);
    }
}
