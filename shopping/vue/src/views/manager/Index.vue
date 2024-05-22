<template>
  <div>
    <div class="main-body-header">欢迎进入二手交易商城</div>
    <div class="main-body-content">
      <div style="margin-bottom: 100px; width: 100%">
        <div style="margin-bottom: 15px; font-weight: bold; font-size: 18px">系统公告</div>
        <el-collapse v-model="activeName" accordion>
          <el-collapse-item v-for="item in notices" :title="item.name" :name="item.id">
            <div style="padding: 0 20px">{{ item.content }}</div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div id="bar" style="height: 400px"></div>
          </el-col>
          <el-col :span="12">
            <div id="bie" style="height: 400px"></div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import * as echarts from "echarts";



export default {
  name: 'IndexView',

  data() {
    return {
      activeName: '',
      notices: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  mounted() {
    this.initEcharts();
    request.get("/notice").then(res => {
      if (res.code === '0') {
        this.notices = res.data;
      } else {
        this.$message.error(res.msg)
      }
    })
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    initEcharts() {
      request.get("/order/echarts/bie").then(res => {
        if (res.code === '0') {
          // 开始去渲染饼图数据啦
          this.initBie(res.data.data)
        } else {
          this.$message.error(res.msg)
        }
      })
      request.get("/order/echarts/bar").then(res => {
        if (res.code === '0') {
          let map = res.data;
          // 开始去渲染柱状图数据啦
          this.initBar(map.xAxis, map.yAxis)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    initBie(data) {
      let chartDom = document.getElementById('bie');
      let myChart = echarts.init(chartDom);
      let option;

      option = {
        title: {
          text: '订单分类总销售额（饼图）',
          subtext: '统计维度：购买用户性别',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '性别',
            type: 'pie',
            radius: '50%',
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      option && myChart.setOption(option);
    },
    initBar(xAxis, yAxis) {
      let chartDom = document.getElementById('bar');
      let myChart = echarts.init(chartDom);
      let option;

      option = {
        title: {
          text: '月份销量统计（柱状图）',
          subtext: '统计维度：月份',
          left: 'center'
        },
        xAxis: {
          type: 'category',
          data: xAxis
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: yAxis,
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)'
            }
          }
        ]
      };

      option && myChart.setOption(option);
    },
  }
}
</script>
