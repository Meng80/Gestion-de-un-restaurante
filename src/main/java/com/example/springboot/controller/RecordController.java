package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Product;
import com.example.springboot.entity.Record;
import com.example.springboot.entity.RecordRes;
import com.example.springboot.service.IProductService;
import com.example.springboot.service.IRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    private IRecordService recordService;

    @Resource
    private IProductService productService;

    @PutMapping
    public Result save(@RequestBody Record record) {
        LocalDateTime dataTime = LocalDateTime.now();
        record.setCreateTime(dataTime);
        Product product = productService.getById(record.getProduct());
        int n = record.getCount();
        if("2".equals(record.getAction())){
            n = -n;
            record.setCount(n);
        }
        int num = product.getCount() + n ;
        product.setCount(num);
        productService.updateById(product);
        recordService.saveOrUpdate(record);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(recordService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(recordService.getById(id));
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam(required = false, defaultValue = "") String product,
                           @RequestParam(required = false, defaultValue = "") String category,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("sr.product = sp.id and sp.category = sc.id");

        if (!product.isEmpty()) {
            queryWrapper.like("sp.name", product );
        }
        if (!category.isEmpty()) {
            queryWrapper.eq("sc.id", category );
        }

        Page<RecordRes> page = new Page<>(pageNum, pageSize);
        Page<RecordRes> recordPage = recordService.findRecordPage(page, queryWrapper);

        return Result.success(recordPage);
    }

}

