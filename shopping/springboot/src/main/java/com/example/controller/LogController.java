package com.example.controller;

import com.example.common.Result;
import com.example.entity.Comment;
import com.example.entity.Log;
import com.example.entity.Params;
import com.example.service.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;


    @PostMapping
    public Result save(@RequestBody Log log) {
       logService.add(log);
        return Result.success();
    }

    @GetMapping("/search")
    public Result findBySearch(Params params) {
        PageInfo<Log> info = logService.findBySearch(params);
        return Result.success(info);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        logService.delete(id);
        return Result.success();
    }
    /*批量删除*/
    @PostMapping("delBatch")
    public Result delBatch(@RequestBody List<Log> list){
        for(Log log:list){
            logService.delete(log.getId());
        }
        return  Result.success();
    }
}
