package com.example.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.User;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;



import com.example.springboot.service.IVipService;
import com.example.springboot.entity.Vip;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ada
 * @since 2023-06-29
 */
@RestController
@RequestMapping("/vip")
        public class VipController {

    @Resource
    private IVipService vipService;

    @PutMapping
    public Result save(@RequestBody Vip vip){
       vipService.saveOrUpdate(vip);
          return Result.success();
        }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
       vipService.removeById(id);
          return Result.success();
        }

    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
       vipService.removeByIds(ids);
          return Result.success();
        }

    @GetMapping
    public Result findALL(){
          return Result.success(vipService.list());

        }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(vipService.getById(id));

        }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }

        queryWrapper.orderByDesc("id");
        return Result.success(vipService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }


    /**
     * export interface
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Vip> list = vipService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("VipInformation", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /**
     * excel import
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Vip> list = reader.readAll(Vip.class);
        vipService.saveBatch(list);
        return Result.success(true);
    }

    @GetMapping("/recent")
    public Result getRecentVisitors(@RequestParam String period) {
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current date and time: " + now.format(formatter));

        if ("lastMonth".equalsIgnoreCase(period)) {
            LocalDateTime oneMonthAgo = now.minusMonths(1);
            queryWrapper.ge("last_Visit", oneMonthAgo);
            System.out.println("Querying VIPs who visited in the last month since: " + oneMonthAgo.format(formatter));
        } else if ("longTimeNoVisit".equalsIgnoreCase(period)) {
            LocalDateTime sixMonthsAgo = now.minusMonths(6);
            queryWrapper.le("last_Visit", sixMonthsAgo);
            System.out.println("Querying VIPs who haven't visited in the last 6 months since: " + sixMonthsAgo.format(formatter));
        }
        List<Vip> vipList = vipService.list(queryWrapper);
        return Result.success(vipList);
    }
}

