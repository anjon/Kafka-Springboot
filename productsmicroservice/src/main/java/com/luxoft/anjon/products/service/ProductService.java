package com.luxoft.anjon.products.service;

import com.luxoft.anjon.products.rest.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel productRestModel) throws Exception;
}
