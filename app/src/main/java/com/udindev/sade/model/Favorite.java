package com.udindev.sade.model;

import java.util.List;

public class Favorite {
    private List<String> listProductId;

    public Favorite() {}

    public Favorite(List<String> listProductId) {
        this.listProductId = listProductId;
    }

    public List<String> getListProductId() {
        return listProductId;
    }

    public void setListProductId(List<String> listProductId) {
        this.listProductId = listProductId;
    }
}
