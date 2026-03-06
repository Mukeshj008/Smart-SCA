package com.example.app.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Product entity model.
 * Uses: commons-lang3
 */
public class Product {
    private String id;
    private String name;
    private String description;
    private double price;

    public Product() {}

    public Product(String id, String name, double price) {
        this.id = StringUtils.trimToEmpty(id);
        this.name = StringUtils.trimToEmpty(name);
        this.price = NumberUtils.toDouble(String.valueOf(price), 0.0);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
