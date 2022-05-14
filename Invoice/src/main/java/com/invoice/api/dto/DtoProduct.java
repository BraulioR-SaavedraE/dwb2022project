package com.invoice.api.dto;

/*
 * Sprint 1 - Requerimiento 2
 * Agregar atributos de clase para la validación del producto
 */
public class DtoProduct {
    String gtin;
    Integer stock;

    public DtoProduct() {
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
