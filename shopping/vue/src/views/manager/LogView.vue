<template>
  <div>
    <div class="main-body-header">日志管理</div>
    <div class="main-body-content">
    <div style="margin-bottom: 15px">
      <el-input v-model="params.name" style="width: 200px" placeholder="请输入操作内容"></el-input>
      <el-input v-model="params.username" style="width: 200px;margin-left: 10px" placeholder="请输入操作人"></el-input>
      <el-button type="info" style="margin-left: 10px" @click="findBySearch()">查询</el-button>
      <el-popconfirm title="确定删除这些数据吗" @confirm="delBatch()">
        <el-button slot="reference" type="danger" style="margin-left: 5px">批量删除
        </el-button>
             </el-popconfirm>
      <el-button type="warning" style="margin-left: 10px" @click="reset()">清空</el-button>
    </div>
    <div>
      <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="操作内容"></el-table-column>
        <el-table-column prop="time" label="操作时间"></el-table-column>
        <el-table-column prop="username" label="操作人"></el-table-column>
        <el-table-column prop="ip" label="ip"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">

            <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
              <el-button slot="reference" type="danger" style="margin-left: 5px">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-top: 10px">
      <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="params.pageNum"
              :page-sizes="[5, 10, 15, 20]"
              :page-size="params.pageSize"
              layout="total, sizes, prev, pager, next"
              :total="total">
      </el-pagination>
    </div>
  </div>
  </div>
</template>

<script>
  import request from "@/utils/request";

  export default {
    data() {
      return {
        params: {
          name: '',
          username:'',
          phone: '',
          pageNum: 1,
          pageSize: 10,
          search: {},
        },
        pageNum: 1,
        search: {},
        tableData: [],
        total: 0,
        dialogFormVisible: false,
        form: {},
        multipleSelection: []
      }
    },
    // 页面加载的时候，做一些事情，在created里面
    created() {
      this.findBySearch();
    },
    // 定义一些页面上控件出发的事件调用的方法
    methods: {
      findBySearch() {
        request.get("/log/search", {
          params: this.params
        }).then(res => {
          if (res.code === '0') {
            this.tableData = res.data.list;
            this.total = res.data.total;
          } else {
            this.$message({
              message: res.msg,
              type: 'success'
            });
          }
        })
      },
      reset() {
        this.params = {
          pageNum: 1,
          pageSize: 10,
          name: '',
          username: ''
        }
        this.findBySearch();
      },
      handleSizeChange(pageSize) {
        this.params.pageSize = pageSize;
        this.findBySearch();
      },
      handleCurrentChange(pageNum) {
        this.params.pageNum = pageNum;
        this.findBySearch();
      },
      submit() {
        request.post("/log", this.form).then(res => {
          if (res.code === '0') {
            this.$message({
              message: '操作成功',
              type: 'success'
            });
            this.dialogFormVisible = false;
            this.findBySearch();
          } else {
            this.$message({
              message: res.msg,
              type: 'error'
            });
          }
        })
      },
      del(id) {
        request.delete("/log/" + id).then(res => {
          if (res.code === '0') {
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.findBySearch();
          } else {
            this.$message({
              message: res.msg,
              type: 'success'
            });
          }
        })
      },
      //批量删除
      delBatch(){
        if(this.multipleSelection.length===0 ){
          this.$message.warning("请勾选您要删除的项")
          return
        }
        console.log(this.multipleSelection)
        request.post("log/delBatch",this.multipleSelection).then(res=>{
          if (res.code === '0') {
            this.$notify.success('批量删除成功');
            this.findBySearch();
          } else {
            this.$notify.error(res.msg);
          }
        })
      },
      handleSelectionChange(val){
        this.multipleSelection = val;
      },

    }
  }
</script>
