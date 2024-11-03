package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Vip;
import com.example.springboot.service.IVipService;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IVipService vipService;
    private UserService userService;

    @Autowired
    public EchartsController(IVipService vipService, UserService userService) {
        this.vipService = vipService;
        this.userService = userService;
    }

    @GetMapping("/totalEmployees")
    public Result getTotalEmployees() {
        int totalEmployees = (int) userService.count();
        return Result.success(totalEmployees);
    }

    @GetMapping("/totalVIPCustomers")
    public Result getTotalVIPCustomers() {
        int totalVIPCustomers = (int) vipService.count();
        return Result.success(totalVIPCustomers);
    }

    @GetMapping("/members")
    public Result members() {
        List<Vip> list = vipService.list();
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for (Vip vip : list) {
            Date createTime = vip.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;

        }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));

    }
}
