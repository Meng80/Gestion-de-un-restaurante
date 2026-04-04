package com.example.springboot.mapper;

import com.example.springboot.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * list ShoppingCart
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    /**
     * Update Number
     * @param shoppingCart
     */
    void updateNumberById(ShoppingCart shoppingCart);


    /**
     * Insert ShoppingCart
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * Delete by TableId
     * @param tableId
     */
    void deleteByTableId(Long tableId);

    /**
     * DeleteById
     * @param id
     */
    void deleteById(Long id);

    /**
     * Insert batch en ShoppingList
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
