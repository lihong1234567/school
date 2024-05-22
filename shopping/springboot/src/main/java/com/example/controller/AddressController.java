package com.example.controller;

import com.example.common.Result;
import com.example.common.config.AutoLog;
import com.example.entity.Address;
import com.example.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
*  描述：收货地址相关接口
*/
@RestController
@RequestMapping(value = "/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * 描述：新增
     */
    @PostMapping
    @AutoLog("新增收货地址")
    public Result add(@RequestBody Address address) {
        addressService.add(address);
        return Result.success(address);
    }

    /**
     * 描述：根据ID删除
     */
    @DeleteMapping("/{id}")
    @AutoLog("删除收货地址")
    public Result delete(@PathVariable Integer id) {
        addressService.delete(id);
        return Result.success();
    }

    /**
     * 描述：查询所有
     */
    @GetMapping()
    public Result all() {
        return Result.success(addressService.findAll());
    }

    /**
     * 描述：分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Address search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(addressService.findPage(search, pageNum, pageSize));
    }
}
