package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.config.JwtTokenUtils;
import com.example.common.ResultCode;
import com.example.dao.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsService {

    @Resource
    private GoodsDao goodsDao;
    @Resource
    private UserDao userDao;
    @Resource
    private TypeDao typeDao;

    public Goods add(Goods goods) {
        Account user = getAccount();

        if (2 == user.getRole()){
            Integer id = user.getId();
            if (ObjectUtil.isNotEmpty(id)){
                User releaseUser = userDao.selectByPrimaryKey(id);
                String username = releaseUser.getUsername();
                String phone = releaseUser.getPhone();
                goods.setUserId(id);
                goods.setStatus("待出售");
                goods.setUserName(username);
                goods.setTelePhone(phone);
            }
        }
        goodsDao.insertSelective(goods);
        return goods;
    }

    public void delete(Integer id) {
        goodsDao.deleteByPrimaryKey(id);
    }

    public void update(Goods goods) {
        goodsDao.updateByPrimaryKeySelective(goods);
    }

    public Goods findById(Integer id) {
        //查询时 根据typeId查出type类型
        Goods goods = goodsDao.selectByPrimaryKey(id);
        Integer typeId = goods.getTypeId();
        if (ObjectUtil.isNotEmpty(typeId)){
            Type type = typeDao.selectByPrimaryKey(typeId);
            String name = type.getName();
            goods.setType(name);
        }
        goodsDao.updateByPrimaryKeySelective(goods);
        return goods;
    }

    public List<Goods> findAll() {
        return goodsDao.findBySearch(null);
    }

    public PageInfo<Goods> findPage(Goods search, Integer pageNum, Integer pageSize) {
        Account user = getAccount();
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> all = findByCondition(user, search);

        return PageInfo.of(all);
    }

    public List<Goods> findByCondition(Account user, Goods search) {
        Account account = getAccount();
        if (2 == account.getRole()){
            search.setUserName(account.getUsername());
        }
		return goodsDao.findBySearch(search);
    }

    public List<Goods> findByFlId(Integer id) {
        return goodsDao.findByTypeId(id);
    }


    private Account getAccount() {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        return user;
    }


    public List<Goods> search(String name ) {
        if (ObjectUtil.isNull(name)){
            return goodsDao.selectAll();
        }
        return goodsDao.findByName(name);
    }


}