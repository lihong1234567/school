<template>
    <div class="main-content">
        <div class="main-line">
            <div class="main-line-1">
                <div class="main-img">
                    <el-carousel height="50vh" :interval="2000">
                        <el-carousel-item v-for="(item,index) in carousel_1_1" :key="index">
                            <img :src="item" alt="">
                        </el-carousel-item>
                    </el-carousel>
                </div>
            </div>
        </div>
        <div>
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane v-for="item in typeData" :label="item.name" :name="item.id + ''" :key="item.id">
                    <div class="item-grid">
                        <div class="item-grid-item" v-for="obj in goodsData" @click="$router.push('/front/detail?id=' + obj.id)">
                            <img :src="obj.img" alt=""  style="width: 100%; height:225px; border-radius: 5px; border: #e3e3e3 solid 1px">
                            <div class="overflowShow" style="width: 70%; text-align: center; margin: 10px auto">{{obj.name}}</div>
                            <div class="overflowShow" style="width: 70%; text-align: center; margin: 10px auto">价格：￥{{obj.price}}</div>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>

    </div>
</template>

<script>
    import request from "@/utils/request";

    export default {

        data() {
            return {
                carousel_1_1: [
                    require('@/assets/imgs/lun-1.jpg'),
                    require('@/assets/imgs/lun-2.jpg'),
                ],
                activeName: '',
                typeData: [],
                goodsData: [],
            }
        },
        // 监听路由
        watch: {
            $route(to, from) {
                if(to.path === '/front/index') {
                    console.log("hahah")
                    location.reload()
                }
            }
        },
        mounted() {
            request.get("/type").then(res => {
                if (res.code === '0') {
                    this.typeData = res.data;
                    this.load(this.typeData[0].id)
                } else {
                    this.$notify.error(res.msg)
                    if (res.code === '401') {
                        this.$router.push("/login")
                    }
                }
            })
        },
        // methods：本页面所有的点击事件或者其他函数定义区
        methods: {
            load(typeId) {
                this.activeName = typeId + ''
                request.get("/goods/type/" + typeId).then(res => {
                    if (res.code === '0') {
                        this.goodsData = res.data
                    } else {
                        this.$notify.error(res.msg)
                    }
                })
            },
            handleClick(tab) {
                this.load(tab.name)
            },
        }
    }
</script>
