package com.example.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Elasticsearch search service.
 * Uses: elasticsearch, log4j
 */
public class SearchService {
    private static final Logger logger = LogManager.getLogger(SearchService.class);
    private final TransportClient client;

    public SearchService(TransportClient client) {
        this.client = client;
    }

    public List<Map<String, Object>> search(String index, String field, String value) {
        logger.info("Searching index {} for {}={}", index, field, value);
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            SearchRequestBuilder srb = client.prepareSearch(index)
                    .setQuery(QueryBuilders.termQuery(field, value));
            SearchResponse response = srb.get();
            for (SearchHit hit : response.getHits()) {
                results.add(hit.getSourceAsMap());
            }
        } catch (Exception e) {
            logger.error("Search failed", e);
        }
        return results;
    }
}
