package com.example.springboot.controller.user;

import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.ShoppingCartDTO;
import com.example.springboot.entity.ShoppingCart;
import com.example.springboot.service.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO dto,
                      @RequestHeader("X-Table-Id") Integer tableId) {
        shoppingCartService.addShoppingCart(tableId, dto);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list(@RequestHeader("X-Table-Id") Integer tableId) {
        List<ShoppingCart> cartList = shoppingCartService.showShoppingCart(tableId);
        return Result.success(cartList);
    }

    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO dto,
                      @RequestHeader("X-Table-Id") Integer tableId) {
        shoppingCartService.subShoppingCart(tableId, dto);
        return Result.success();
    }

    @DeleteMapping("/clear")
    public Result clear(@RequestHeader("X-Table-Id") Integer tableId) {
        shoppingCartService.cleanShoppingCart(tableId);
        return Result.success();
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
        return Result.success();
    }


}
