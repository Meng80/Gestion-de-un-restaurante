package com.example.springboot.service.impl;

import com.example.springboot.common.MessageConstant;
import com.example.springboot.controller.dto.ShoppingCartDTO;
import com.example.springboot.entity.Dish;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.exception.OrderBusinessException;
import com.example.springboot.mapper.DishMapper;
import com.example.springboot.mapper.ShoppingCartMapper;
import com.example.springboot.service.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements IShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;


    /**
     * Add ShoppingCart
     * @param tableId
     * @param shoppingCartDTO
     */
    @Override
    @Transactional
    public void addShoppingCart(Integer tableId, ShoppingCartDTO shoppingCartDTO) {
        if (tableId == null) {
            throw new OrderBusinessException("Table number cannot be null");
        }
        if (shoppingCartDTO == null) {
            throw new OrderBusinessException("Shopping cart data cannot be null");
        }
        if (shoppingCartDTO.getDishId() == null) {
            throw new OrderBusinessException("Dish ID cannot be null");
        }
        Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
        if (dish == null) {
            throw new OrderBusinessException("Dish not exit");
        }

        BigDecimal price = dish.getPrice();
        if (price == null) {
            throw new OrderBusinessException("El dish price not exit");
        }

        ShoppingCart queryCart = new ShoppingCart();
        queryCart.setTableId(Long.valueOf(tableId));
        queryCart.setDishId(shoppingCartDTO.getDishId());

        List<ShoppingCart> shoppingCartsList = shoppingCartMapper.list(queryCart);

        if (shoppingCartsList != null && !shoppingCartsList.isEmpty()) {

            ShoppingCart existingCart = shoppingCartsList.get(0);
            Integer currentNumber = existingCart.getNumber();
            if (currentNumber == null) {
                currentNumber = 0;
            }
            Integer newNumber = currentNumber + 1;
            existingCart.setNumber(newNumber);

            BigDecimal newAmount = price.multiply(new BigDecimal(newNumber));
            existingCart.setAmount(newAmount);

            shoppingCartMapper.updateNumberById(existingCart);

        } else {

            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setTableId(Long.valueOf(tableId));
            shoppingCart.setDishId(shoppingCartDTO.getDishId());

            Integer number = shoppingCartDTO.getNumber() != null ? shoppingCartDTO.getNumber() : 1;
            shoppingCart.setNumber(number);

            shoppingCart.setAmount(price.multiply(new BigDecimal(number)));

            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());

            shoppingCartMapper.insert(shoppingCart);
        }
    }
    /**
     * Show ShoppingCart
     * @param tableId
     * @return ShoppingCart List
     */
    @Override
    public List<ShoppingCart> showShoppingCart(Integer tableId) {
        if (tableId == null) {
            throw new OrderBusinessException("Table number cannot be null");
        }
        ShoppingCart queryCart = new ShoppingCart();
        queryCart.setTableId(Long.valueOf(tableId));
        return shoppingCartMapper.list(queryCart);
    }

    /**
     * Clear ShoppingCart
     * @param tableId
     */
    @Override
    @Transactional
    public void cleanShoppingCart(Integer tableId) {
        shoppingCartMapper.deleteByTableId(Long.valueOf(tableId));
    }

    /**
     * Decrease product quantity in the shopping cart
     * @param tableId
     * @param shoppingCartDTO
     */
    @Override
    @Transactional
    public void subShoppingCart(Integer tableId, ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart queryCart = new ShoppingCart();
        queryCart.setTableId(Long.valueOf(tableId));
        queryCart.setDishId(shoppingCartDTO.getDishId());

        List<ShoppingCart> list = shoppingCartMapper.list(queryCart);

        if (list == null || list.size() == 0) {
            throw new OrderBusinessException(MessageConstant.CART_ITEM_NOT_FOUND);
        }

        ShoppingCart existingCart = list.get(0);
        Integer number = existingCart.getNumber();

        if (number == 1) {
            shoppingCartMapper.deleteById(existingCart.getId());
        } else {
            existingCart.setNumber(number - 1);
            shoppingCartMapper.updateNumberById(existingCart);
        }
    }

    /**
     * Remove item from shopping cart by ID
     * @param id shopping cart ID
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        shoppingCartMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateNumberById(ShoppingCart shoppingCart) {

        Dish dish = dishMapper.getById(shoppingCart.getDishId());
        if (dish != null) {
            BigDecimal price = dish.getPrice();
            shoppingCart.setAmount(price.multiply(new BigDecimal(shoppingCart.getNumber())));
        }
        shoppingCartMapper.updateNumberById(shoppingCart);
    }
}