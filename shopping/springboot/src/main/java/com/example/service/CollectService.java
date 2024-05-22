package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.dao.CollectDao;
import com.example.dao.GoodsDao;
import com.example.entity.*;
import com.example.entity.Collect;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CollectService {

    @Resource
    private CollectDao collectDao;
    @Resource
    private GoodsDao goodsDao;

    public void collect(Integer goodsId) {
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        Integer collection = goods.getCollection();
        Account user = getAccount();
        Collect collect = collectDao.findByRelIdAndUserIdAndRole(goodsId, user.getId(), user.getRole());
        if (ObjectUtil.isNull(collect)) {
            Collect tmp = new Collect();
            tmp.setUserId(user.getId());
            tmp.setRole(user.getRole());
            tmp.setGoodsId(goodsId);
            tmp.setNum(goods.getNum());
            collectDao.insertSelective(tmp);
            goods.setCollection(collection + 1);
        } else {
            goods.setCollection(collection - 1);
            collectDao.deleteByRelIdAndUserIdAndRole(goodsId, user.getId(), user.getRole());
        }
        goodsDao.updateByPrimaryKeySelective(goods);

    }

    public boolean collectCheck(Integer goodsId) {
        Account user = getAccount();
        Collect collect = collectDao.findByRelIdAndUserIdAndRole(goodsId, user.getId(), user.getRole());
        return ObjectUtil.isNotEmpty(collect);
    }


    public void delete(Integer id) {
        Collect collect = collectDao.selectByPrimaryKey(id);
        collectDao.deleteByPrimaryKey(id);
        //更新商品表中收藏数的数据
        Integer goodsId = collect.getGoodsId();
        if (ObjectUtil.isNotEmpty(goodsId)){
            Goods goods = goodsDao.selectByPrimaryKey(goodsId);
            Integer collection = goods.getCollection();
            goods.setCollection(collection - 1);
            goodsDao.updateByPrimaryKeySelective(goods);
        }
    }


    public Collect findById(Integer id) {
        return collectDao.selectByPrimaryKey(id);
    }

    public List<Collect> findAll() {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        List<Collect> list = collectDao.findByUserId(Objects.requireNonNull(currentUser).getId());
        List<Collect> collects = new ArrayList<>();
        for (Collect collect : list) {
            Goods goods = goodsDao.selectByPrimaryKey(collect.getGoodsId());
            if (ObjectUtil.isNotEmpty(goods)) {
                collect.setImg(goods.getImg());
                collect.setGoodsName(goods.getName());
                collect.setPrice(goods.getPrice());
                collects.add(collect);
            }
        }
        return collects;
    }

    public PageInfo<Collect> findPage(Collect search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> all = findByCondition(user, search);
        return PageInfo.of(all);
    }

    public List<Collect> findByCondition(Account user, Collect search) {
		return collectDao.findBySearch(search);
    }
    private Account getAccount() {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        return user;
    }

}