package com.example.app.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Elasticsearch client configuration.
 * Uses: elasticsearch, log4j
 */
public class ElasticsearchConfig {
    private static final Logger logger = LogManager.getLogger(ElasticsearchConfig.class);

    public TransportClient createClient() throws UnknownHostException {
        logger.info("Creating Elasticsearch transport client");
        Settings settings = Settings.builder()
                .put("cluster.name", "sample-cluster")
                .build();
        return new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }
}
