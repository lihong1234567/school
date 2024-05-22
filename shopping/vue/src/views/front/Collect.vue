<template>
  <div class="main-content">
    <div>
      <div class="main-table-box">
        <div style="height: 50px; line-height: 50px; font-weight: bold; font-size: 15px">我的收藏</div>
        <el-table style="width: 100%" :data="collectData" size="small" strip header-cell-class-name="tableHeaderClass" >
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
              <el-button type="primary"@click="addCart(scope.row)" style="margin-right: 5px">加入购物车</el-button>
              <el-popconfirm title="确定取消吗？" @confirm="del(scope.row.id)">
                <el-button size="small" type="danger" slot="reference">取消收藏</el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>

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
      collectData: [],
      addressId: null,
      addressList: [],
      goods:{}
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
      request.get("/collect").then(res => {
        if (res.code === '0') {
          this.collectData = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    loadGoods() {
      request.get('/goods/' + this.goodsId).then(res => {
        this.goods = res.data
      })
    },
    del(id) {
      request.delete("/collect/" + id).then(res => {
        if (res.code === '0') {
          this.$notify.success("取消成功")
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    addCart(row) {
      //先根据传进来的收藏表里的goodsId查出商品数据
      console.log(row.goodsId)
      request.get('/goods/' + row.goodsId).then(res => {
        this.goods = res.data
        if (!this.user && !this.user.username) {
          this.$notify.error("登录失效，请重新登陆")
          this.$router.push("/login")
          return;
        }
        if (this.user.id === this.goods.userId){
          this.$notify.warning("您自己发布的商品，不支持购买")
          return;
        }
        if (this.goods.status === '已售出'){
          this.$notify.warning("商品已售出，不支持该操作")
          return;
        }
        if (this.user.role === 1) {
          this.$notify.warning("您是管理员，不支持该操作")
          return;
        }
        let data = {
          userId: this.user.id,
          goodsId: this.goods.id,
          num: this.num,
        }
        request.post("/cart", data).then(res => {
          if (res.code === '0') {
            this.$notify.success("加入成功")
          } else {
            this.$notify.error(res.msg)
          }
        })
      })

    },

  }
}
</script>
