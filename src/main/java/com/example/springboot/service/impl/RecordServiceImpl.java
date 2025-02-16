package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Record;
import com.example.springboot.entity.RecordRes;
import com.example.springboot.mapper.RecordMapper;
import com.example.springboot.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Autowired
    private RecordMapper recordMapper;


    @Override
    public Page<RecordRes> findRecordPage(Page<RecordRes> page, QueryWrapper<Record> queryWrapper) {
        return recordMapper.pageCC(page, queryWrapper);
    }

}
