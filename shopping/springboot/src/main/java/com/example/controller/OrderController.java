package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.common.config.AutoLog;
import com.example.entity.*;
import com.example.service.GoodsService;
import com.example.service.OrderService;
import com.example.service.TypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
*  描述：订单相关接口
*/
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private TypeService typeService;

    /**
     * 描述：支付
     */
    @PostMapping("/buy")
    @AutoLog("支付")
    public Result add(@RequestBody Order order) {
        orderService.add(order);
        return Result.success();
    }

    /**
     * 描述：下单
     */
    @PostMapping("/place")
    @AutoLog("删除地址")
    public Result addOrder(@RequestBody OrderDto order) {
        orderService.addOrder(order);
        return Result.success();
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        orderService.delete(id);
        return Result.success();
    }
    /**
    *批量删除
     */
    @PostMapping("delBatch")
    public Result delBatch(@RequestBody List<Order> list){
        for(Order order:list){
            orderService.delete(order.getId());
        }
        return  Result.success();
    }
    /**
     * 描述：更新
     */
    @PutMapping
    public Result update(@RequestBody Order order) {

        orderService.update(order);
        return Result.success();
    }

    /**
     * 描述：根据ID查询
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        Order order = orderService.findById(id);
        return Result.success(order);
    }

    /**
     * 描述：查询所有购买订单
     */
    @GetMapping
    public Result all() {
        return Result.success(orderService.findAll());
    }

    /**
     * 描述：查询所有出售订单
     */
    @GetMapping("/sell/all")
    public Result sellAll() {

        return Result.success(orderService.findSellAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Order search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(orderService.findPage(search, pageNum, pageSize));
    }

    /**
     * 描述：统计订单柱状图按月份统计销量
     */
    @GetMapping("/echarts/bar")
    public Result bar() {
        //数据在sql里处理过了，这里的月份只有月份
        List<Order> list = orderService.findByMonAll();
        TreeMap<String, Object> mapList = new TreeMap<>();
        if (CollectionUtil.isNotEmpty(list)) {
            Map<String, Long> collect = list.stream().filter(item -> ObjectUtil.isNotEmpty(item.getMonth())).collect(Collectors.groupingBy(Order::getMonth, Collectors.counting()));
            List<Object> xAxis = new ArrayList<>();
            List<Long> yAxis = new ArrayList<>();
            for (String key : collect.keySet()) {
                xAxis.add(key + "月");
                yAxis.add(collect.get(key));
            }
            mapList.put("xAxis", xAxis);
            mapList.put("yAxis", yAxis);
        }
        // 最后返回给前端的数据结构
        return Result.success(mapList);
    }
    /**
     * 描述：统计订单性别统计饼图
     */
    @GetMapping("/echarts/bie")
    public Result bie() {
        //sql关联用户表查出下单人的性别
        List<Order> orderList = orderService.findSexAll();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderList)) {
            Map<String, Long> collect = orderList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getSex())).collect(Collectors.groupingBy(Order::getSex, Collectors.counting()));
            for (Object key : collect.keySet()) {
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("name", key);
                tmpMap.put("value", collect.get(key));
                list.add(tmpMap);
            }
        }
        map.put("data", list);
        return Result.success(map);
    }

}
