package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.dao.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private CartDao cartDao;
    @Resource
    private UserDao userDao;
    @Resource
    private AddressDao addressDao;

    public void add(Order order) {
//        下单是一个一个下单的
        Double price = order.getPrice();
        Integer goodsId = order.getGoodsId();
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        User user = userDao.selectByPrimaryKey(currentUser.getId());
        //获取用户当前账户余额
        Double account = user.getAccount();
        if (account < price) {
            throw new CustomException("-1", "您的余额不足，请到个人中心充值");
        }
        user.setAccount(account - price);
        order.setStatus("待发货");
        order.setPaytime(DateUtil.now());
        orderDao.updateByPrimaryKeySelective(order);
        userDao.updateByPrimaryKeySelective(user);
        goods.setStatus("已售出");
        goodsDao.updateByPrimaryKeySelective(goods);
    }
    public void addOrder(OrderDto orderDto) {
        //下单后订单状态为待支付，点击支付按钮，再走pay接口完成支付
        List<Cart> list = orderDto.getList();
        Integer addressId = orderDto.getAddressId();
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        for (Cart cart : list) {
            Order order = new Order();
            order.setOrderId(DateUtil.format(new Date(), "yyyyMMddhhmmss"));
            order.setTime(DateUtil.now());
            order.setUserId(currentUser.getId());

            Goods goods = goodsDao.selectByPrimaryKey(cart.getGoodsId());
            String userName = goods.getUserName();//发布人
            order.setGoodsId(goods.getId());
            order.setReleaseUser(userName);
            order.setNum(cart.getNum());
            order.setPrice(goods.getPrice());
            order.setStatus("待支付");
            order.setAddressId(addressId);
            orderDao.insertSelective(order);
            // 删除购物车中该数据
            cartDao.deleteByPrimaryKey(cart.getId());
        }
    }

    private Double calculateTotalPrice(List<Cart> list) {
        double total = 0;
        for (Cart cart : list) {
            total += cart.getPrice() * cart.getNum();
        }
        return total;
    }

    public void delete(Integer id) {
        orderDao.deleteByPrimaryKey(id);
    }

    public void update(Order order) {
        orderDao.updateByPrimaryKeySelective(order);
    }

    public Order findById(Integer id) {
        return orderDao.selectByPrimaryKey(id);
    }

    public List<Order> findAll() {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        List<Order> list = findByCondition(currentUser, null);
        extracted(list);
        return list;
    }

    public PageInfo<Order> findPage(Order search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> all = findByCondition(user, search);
        for (Order order : all) {
            Address address = addressDao.selectByPrimaryKey(order.getAddressId());
            order.setName(address.getUsername());
            order.setAddress(address.getAddress());
            order.setPhone(address.getPhone());
        }
        return PageInfo.of(all);
    }
    public List<Order> findByCondition(Account user, Order search) {
        if (2 == user.getRole()) {
            List<Order> list = orderDao.findBySearch(search, user.getId(), user.getRole());
            extracted(list);
            return list;
        }
        List<Order> list = orderDao.findBySearch(search,null,null);
        extracted(list);
        return list;
    }

    private void extracted(List<Order> list) {
        for (Order order : list) {
            User user = userDao.selectByPrimaryKey(order.getUserId());
            order.setUserName(user.getUsername());
            // 处理订单中的商品
            Goods goods = goodsDao.selectByPrimaryKey(order.getGoodsId());
            order.setGoods(goods);

            // 处理收货
            Address address = addressDao.selectByPrimaryKey(order.getAddressId());
            order.setName(address.getUsername());
            order.setAddress(address.getAddress());
            order.setPhone(address.getPhone());
        }
    }

    public List<Order> findSellAll() {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        Order search = new Order();
        if (2 == currentUser.getRole()){
            search.setReleaseUser(currentUser.getUsername());
            List<Order> list  = orderDao.findBySearch(search, null, null);
            extracted(list);
            return list;

        }
        List<Order> list  = orderDao.findBySearch(search, null, null);
        extracted(list);
        return list;

    }

    public List<Order> findByMonAll() {
        return orderDao.findByMonAll();
    }

    public List<Order> findSexAll() {
        return orderDao.findBySexAll();
    }
}