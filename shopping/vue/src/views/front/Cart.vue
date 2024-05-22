<template>
  <div class="main-content">
    <div>
      <div class="main-table-box">
        <div style="height: 50px; line-height: 50px; font-weight: bold; font-size: 15px">我的购物车</div>
        <el-table style="width: 100%" :data="cartData" size="small" strip header-cell-class-name="tableHeaderClass" >
          <el-table-column label="商品">
            <template v-slot="scope">
              <el-image :src="scope.row.img" :preview-src-list="[scope.row.img]" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="商品名称" prop="goodsName"></el-table-column>
          <el-table-column label="价格" prop="price"></el-table-column>
          <el-table-column label="数量" prop="num"></el-table-column>
          <el-table-column label="操作" fixed="right">
            <template v-slot="scope">
              <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                <el-button size="small" type="danger" slot="reference">删除</el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 20px; text-align: right">
          <el-button type="primary" @click="placeOrder">下单</el-button>
        </div>
        <div style="width: 30%">
          <el-select size="small" style="width: 100%" placeholder="请选择收货地址" v-model="addressId">
            <el-option v-for="item in addressList" :value="item.id" :label="item.address" :key="item.id"></el-option>
          </el-select>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {

  data() {
    return {
      user: JSON.parse(localStorage.getItem("user") || {}),
      cartData: [],
      addressId: null,
      addressList: []
    }
  },
  mounted() {
    this.load()
    request.get("/address").then(res => {
      if (res.code === '0') {
        this.addressList = res.data
      } else {
        this.$notify.error(res.msg)
      }
    })
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load() {
      request.get("/cart").then(res => {
        if (res.code === '0') {
          this.cartData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    del(id) {
      request.delete("/cart/" + id).then(res => {
        if (res.code === '0') {
          this.$notify.success("删除成功")
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    placeOrder() {
      if (this.cartData.length === 0) {
        this.$notify.warning("您的购物车空空如也")
        return
      }
      if (!this.addressId) {
        this.$notify.warning("请选择收货地址")
        return
      }
      let data = {list: this.cartData, addressId: this.addressId}
      request.post("/order/place", data).then(res => {
        if (res.code === '0') {
          this.$notify.success("下单成功")
          this.$router.push("/front/payOrder")
        } else {
          this.$notify.error(res.msg)
        }
      })
    },

  }
}
</script>
