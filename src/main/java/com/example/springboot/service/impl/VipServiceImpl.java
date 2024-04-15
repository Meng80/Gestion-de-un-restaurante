package com.example.springboot.service.impl;

import com.example.springboot.entity.Vip;
import com.example.springboot.mapper.VipMapper;
import com.example.springboot.service.IVipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @author ada
 * @since 2023-06-29
 */
@Service
public class VipServiceImpl extends ServiceImpl<VipMapper, Vip> implements IVipService {

}
