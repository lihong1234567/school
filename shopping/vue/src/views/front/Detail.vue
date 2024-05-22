<template>
  <div class="detail-container">
    <div class="detail-title">详情信息</div>
    <div class="detail-content">
      <div class="detail-left-img-fields">
        <div style="flex: 1;"><img :src="goods.img" style="width: 100%; height: 250px; margin-top: 10px; border-radius: 5px; border: #e3e3e3 solid 1px"></div>
        <div style="flex: 2; margin-left: 50px">
          <div class="detail-fields-item overflowShow">商品名称：{{goods.name}}</div>
          <div class="detail-fields-item overflowShow">商品分类：{{goods.type}}</div>
          <div class="detail-fields-item overflowShow" v-if="goods.type !== '求购'">售出状态：{{goods.status}}</div>
          <div class="detail-fields-item overflowShow">商品品牌：{{goods.brand}}</div>
          <div class="detail-fields-item overflowShow">商品成色：{{goods.almostNew}}</div>
          <div class="detail-fields-item overflowShow">商品规格：{{goods.specifications}}</div>
          <!--<div class="detail-fields-item overflowShow">商品数量：{{goods.num}}</div>-->
          <div class="detail-fields-item overflowShow"  v-if="goods.type !== '求购'">商品价格：{{goods.price}}</div>
          <div class="detail-fields-item overflowShow"  v-if="goods.type !== '求购'">收藏：{{goods.collection}}
            <img src="@/assets/imgs/点赞-active.png" v-if="collectCheck" alt="" style="margin-left: 10px; width: 20px" @click="collect">
            <img src="@/assets/imgs/点赞.png" v-else alt="" style="margin-left: 10px; width: 20px" @click="collect">
          </div>
          <div>
            <el-button type="warning" style="margin-left: 5px" v-if="goods.type !== '求购'" @click="addCart">加入购物车</el-button>
          </div>
        </div>
      </div>
      <!--   富文本详情页部分   -->
      <div class="detail-richtext">
        <div class="detail-richtext-title">{{goods.name}}</div>
        <!--<div>
          <video :src="goods.goodsVideo"  style="width: 100%; height: 250px; margin-top: 10px" controls></video>
        </div>-->
        <div class="detail-richtext-content w-e-text" v-html="goods.description"></div>

      </div>
    </div>
    <div class="detail-title" v-if="this.goods.typeId === 1||this.goods.typeId===3||this.goods.typeId===4||this.goods.typeId===13
||this.goods.typeId===16||this.goods.typeId===17||this.goods.typeId===18||this.goods.typeId===19||this.goods.typeId===21||this.goods.typeId===22||this.goods.typeId===23||this.goods.typeId===24" >评论信息</div>
    <div class="detail-comment" v-if="this.goods.typeId === 1||this.goods.typeId===3||this.goods.typeId===4||this.goods.typeId===13
||this.goods.typeId===16||this.goods.typeId===17||this.goods.typeId===18||this.goods.typeId===19||this.goods.typeId===21||this.goods.typeId===22||this.goods.typeId===23||this.goods.typeId===24">
      <div>
        <el-row v-for="item in comments" style="margin-bottom: 10px">
          <el-col :span="4" style="display: flex; align-items: center;">
            <img :src="item.avatar" style="width: 50px; height: 50px; border-radius: 50%;">
            <div style="margin-left: 10px; flex: 1" class="overflowShow">{{item.userName}}</div>
          </el-col>
          <el-col :span="20">
            <el-row>
              <el-col :span="19" style="height: 50px; line-height: 50px">{{item.content}}</el-col>
              <el-col :span="5" style="height: 50px; line-height: 50px; text-align: right">{{item.time}}</el-col>
            </el-row>
            <el-row :span="3" style="margin-top: 20px" v-for="child in item.children">
              <el-col :span="4" style="display: flex; align-items: center;">
                <img :src="child.avatar" style="width: 50px; height: 50px; border-radius: 50%;">
                <div style="margin-left: 10px; flex: 1" class="overflowShow">{{child.userName}} 回复：</div>
              </el-col>
              <el-col :span="15" style="height: 50px; line-height: 50px">{{child.content}}</el-col>
              <el-col :span="5" style="height: 50px; line-height: 50px; text-align: right">{{child.time}}</el-col>
            </el-row>

            <el-row :span="3" style="margin-top: 20px">
              <el-col :span="15">
                <el-input v-model="item.reply"></el-input>
              </el-col>
              <el-col :span="4" style="margin-left: 10px">
                <el-button type="primary" @click="submit(item.id, item.reply)">回复</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {

  data() {
    let goodsId = this.$route.query.id
    return {
      user: JSON.parse(localStorage.getItem("user") || {}),
      goods: {},
      goodsId: goodsId,
      buyNum: 1,
      comments: [],
      dialogFormVisible: false,
      num: 1,
      content: '',
      collectCheck: false
    }
  },
  mounted() {
    this.load()
    this.loadComment()
    this.checkCollect()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load() {
      request.get('/goods/' + this.goodsId).then(res => {
        this.goods = res.data
        console.log(this.goods)
      })
    },
    loadComment() {
      request.get("/comment/" + this.goodsId).then(res => {
        if (res.code === '0') {
          this.comments = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    addCart() {
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
          this.$router.push("/front/cart")
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    submit(parentId, content) {
      if (!content) {
        this.$notify.warning("请输入内容")
      }
      let data = {content: content, userId: this.user.id, role: this.user.role, parentId: parentId, relId: this.goodsId}
      request.post("/comment", data).then(res => {
        if (res.code ==='0') {
          this.$notify.success("评论成功")
          this.loadComment()
          this.content = ""
        } else {
          this.$notify.error(res.msg)
        }
      })
    },

    checkCollect() {
      request.get("/collect/check/" + this.goodsId).then(res => {
        if (res.code === '0') {
          this.collectCheck = res.data
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    collect() {
      request.get("/collect/front/" + this.goodsId).then(res => {
        if (res.code === '0') {
          this.$notify.success("操作成功")
          this.checkCollect();
          this.load();
        } else {
          this.$notify.error(res.msg)
        }
      })
    },


  }
}
</script>
