package com.example.springboot.service;

import com.example.springboot.controller.dto.ShoppingCartDTO;
import com.example.springboot.entity.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {

    /**
     * Add ShoppingCart
     */
    void addShoppingCart(Integer tableId, ShoppingCartDTO shoppingCartDTO);

    /**
     * Show ShoppingCart
     */
    List<ShoppingCart> showShoppingCart(Integer tableId);

    /**
     * Clear ShoppingCart
     */
    void cleanShoppingCart(Integer tableId);

    /**
     * Decrease product quantity in the shopping cart
     */
    void subShoppingCart(Integer tableId, ShoppingCartDTO shoppingCartDTO);

    /**
     * Delete by Id
     */
    void deleteById(Long id);


    /**
     * Update Number by Id
     */
    void updateNumberById(ShoppingCart shoppingCart);

}