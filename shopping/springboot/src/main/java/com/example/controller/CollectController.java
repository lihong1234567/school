package com.example.controller;

import com.example.common.Result;
import com.example.common.config.AutoLog;
import com.example.dao.GoodsDao;
import com.example.entity.Collect;
import com.example.service.CollectService;
import com.example.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：收藏相关接口
*/
@RestController
@RequestMapping(value = "/collect")
public class CollectController {

    @Resource
    private CollectService collectService;
    @Resource
    private GoodsService goodsService;



    /**
     * 描述：收藏
     */
    @AutoLog("收藏了商品")
    @GetMapping("/front/{goodsId}")
    public Result collect(@PathVariable Integer goodsId) {
        collectService.collect(goodsId);
        return Result.success();
    }


    /**
     * 描述：查看收藏状态
     */
    @GetMapping("/check/{goodsId}")
    public Result praiseCheck(@PathVariable Integer goodsId) {
        return Result.success(collectService.collectCheck(goodsId));
    }



    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        collectService.delete(id);
        return Result.success();
    }



    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Collect collect = collectService.findById(id);
        return Result.success(collect);
    }

    /**
     * 描述：查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(collectService.findAll());
    }


    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Collect search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(collectService.findPage(search, pageNum, pageSize));
    }




}
