package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Record;
import com.example.springboot.entity.RecordRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RecordMapper  extends BaseMapper<Record> {

    Page<RecordRes> pageCC(Page<RecordRes> page, @Param("ew") QueryWrapper<Record> queryWrapper);
}
