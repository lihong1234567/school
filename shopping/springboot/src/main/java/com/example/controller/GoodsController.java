package com.example.controller;

import com.example.common.Result;
import com.example.common.config.AutoLog;
import com.example.entity.*;
import com.example.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.awt.SystemColor.info;

/**
*  描述：商品管理相关接口
*/
@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 描述：新增
     */
    @AutoLog("添加了商品")
    @PostMapping
    public Result add(@RequestBody Goods goods) {
        goodsService.add(goods);
        return Result.success(goods);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    @AutoLog("删除了商品")
    public Result delete(@PathVariable Integer id) {
        goodsService.delete(id);
        return Result.success();
    }
    /*批量删除*/
    @PostMapping("delBatch")
    public Result delBatch(@RequestBody List<Goods> list){
        for(Goods goods:list){
            goodsService.delete(goods.getId());
        }
        return  Result.success();
    }
    /**
     * 描述：更新
     */
    @AutoLog("更新了商品")
    @PutMapping
    public Result update(@RequestBody Goods goods) {

        goodsService.update(goods);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Goods goods = goodsService.findById(id);
        return Result.success(goods);
    }

    /**
     * 描述：根据分类ID查询
     */
    @GetMapping("/type/{id}")
    public Result type(@PathVariable Integer id) {
        List<Goods> list = goodsService.findByFlId(id).stream().filter(item -> "上架".equals(item.getOnStatus())).collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(goodsService.findAll());
    }


    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Goods search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(goodsService.findPage(search, pageNum, pageSize));
    }


    /**
     * 描述：搜索
     */
    @GetMapping("/search/{name}")
    public Result search(@PathVariable String name) {
        return Result.success(goodsService.search(name));
    }


}
