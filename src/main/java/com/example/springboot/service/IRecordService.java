package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Record;
import com.example.springboot.entity.RecordRes;


public interface IRecordService extends IService<Record> {
    Page<RecordRes> findRecordPage(Page<RecordRes> page, QueryWrapper<Record> queryWrapper);
}
