package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Product;
import com.example.springboot.mapper.ProductMapper;
import com.example.springboot.service.IProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl  extends ServiceImpl<ProductMapper, Product> implements IProductService {
}
