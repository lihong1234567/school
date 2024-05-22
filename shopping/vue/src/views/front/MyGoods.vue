<template>
  <div class="main-content">
    <div>
      <div class="main-body-content">
        <div style="margin-left: 500px;margin-top: 50px;margin-bottom: 50px;" >
					<el-button size="small" type="primary" style="width:150px; height:50px;border-radius:2px" @click="add" plain>发布商品</el-button>

        </div>
        <div class="main-table-box">
          <el-table style="width: 100%" :data="tableData" size="small" strip header-cell-class-name="tableHeaderClass" >
						<el-table-column label="商品图片">
							<template v-slot="scope">
								<el-image :src="scope.row.img" :preview-src-list="[scope.row.img]" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
							</template>
						</el-table-column>
           <!-- <el-table-column label="商品视频" prop="goodsVideo">
              <template v-slot="scope">
                <video :src="scope.row.goodsVideo" style="height: 80px; width: 100px" controls></video>
              </template>
            </el-table-column>-->
						<el-table-column label="商品名称" prop="name"></el-table-column>
						<el-table-column label="商品分类" prop="type"></el-table-column>
						<el-table-column label="商品规格" prop="specifications"></el-table-column>
						<el-table-column label="商品品牌" prop="brand"></el-table-column>
						<el-table-column label="商品成色" prop="almostNew"></el-table-column>
						<!--<el-table-column label="商品数量" prop="num"></el-table-column>-->
						<el-table-column label="商品价格" prop="price"></el-table-column>
						<el-table-column label="商品描述">
							<template v-slot="scope">
								<el-button size="small" type="primary" @click="initEditorView(scope.row)">查看</el-button>
							</template>
						</el-table-column>
            <el-table-column label="发布人" prop="userName"></el-table-column>
            <el-table-column label="发布人电话" prop="telePhone"></el-table-column>
            <el-table-column label="出售状态" prop="status"></el-table-column>
            <el-table-column label="上架状态" prop="onStatus"></el-table-column>
            <el-table-column label="操作" :width="colWidth" fixed="right">
              <template v-slot="scope">
								<el-button size="small" type="primary" @click="edit(scope.row)">编辑</el-button>
								<el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
									<el-button size="small" type="danger" slot="reference">删除</el-button>
								</el-popconfirm>

              </template>
            </el-table-column>
          </el-table>
        </div>
        <div style="margin: 20px 0">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-size="10"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
    <el-dialog title="请填写信息" :visible.sync="dialogVisible" width="55%">
      <el-form :model="form" label-position="right" label-width="100px" style="padding-right: 40px">
				<el-form-item label="商品图片">
					<el-upload action="http://localhost:8080/files/upload" ref="jdtp" :on-success="jdtpSuccessUpload">
						<el-button size="small" type="success">点击上传</el-button>
					</el-upload>
				</el-form-item>
        <!--<el-form-item label="商品视频">
          <el-upload action="http://localhost:8080/files/upload" ref="avatar" :on-success="avatarSuccessUpload">
            <el-button size="small" type="success">点击上传</el-button>
          </el-upload>
        </el-form-item>-->
				<el-form-item label="商品名称">
					<el-input size="small" v-model="form.name" placeholder="请输入商品名称"></el-input>
				</el-form-item>
				<el-form-item label="商品分类">
					<el-select size="small" style="width: 100%" v-model="form.typeId">
						<el-option v-for="item in typeData" :value="item.id" :label="item.name" :key="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="商品规格">
					<el-input size="small" v-model="form.specifications" placeholder="请输入商品规格"></el-input>
				</el-form-item>
        <el-form-item label="商品品牌">
          <el-input size="small" v-model="form.brand" placeholder="请输入商品品牌"></el-input>
        </el-form-item>
        <el-form-item label="商品成色">
          <el-input size="small" v-model="form.almostNew" placeholder="请输入商品成色"></el-input>
        </el-form-item>
        <!--<el-form-item label="商品数量">
          <el-input size="small" v-model="form.num" placeholder="请输入商品数量"></el-input>
        </el-form-item>-->
        <el-form-item label="商品价格">
          <el-input size="small" v-model="form.price" placeholder="请输入商品价格"></el-input>
        </el-form-item>
        <el-form-item label="上架状态">
          <el-select size="small" style="width: 100%" placeholder="上架状态" v-model="form.onStatus">
            <el-option value="上架">上架</el-option>
            <el-option value="下架">下架</el-option>
          </el-select>
        </el-form-item>
				<el-form-item label="商品描述">
					<div id="wang-editor"></div>
				</el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button size="small" @click="dialogVisible = false">取 消</el-button>
          <el-button size="small" type="primary" @click="save">保 存</el-button>
      </div>
    </el-dialog>
	<el-dialog title="商品描述" :visible.sync="richTextDialogVisible">
		<div class="w-e-text" v-html="form.description"></div>
	</el-dialog>

  </div>
</template>

<script>
import request from "@/utils/request";
import wangEditor from "wangeditor";

// 商品管理模块接口请求统一前缀
let baseUrl = "/goods/"

// 富文本
let editor
function initWangEditor(content) {	setTimeout(() => {
		if (!editor) {
			editor = new wangEditor('#wang-editor')
			editor.config.placeholder = '请输入内容'
            editor.config.uploadImgMaxSize = 10 * 1024 * 1024 // 10M
			editor.config.uploadFileName = 'file'
			editor.config.uploadImgServer = 'http://localhost:8080/files/wang/upload'
			editor.create()
		}
		editor.txt.html(content)
	}, 0)
}
export default {
  data() {
    return {
      user: {},
      colWidth: 150,
      tableData: [],
      pageNum: 1,
      pageSize:10,
      dialogVisible: false,
      search: {},
      form: {},
      total: 0,
      typeData: [],
			richTextDialogVisible: false,

    }
  },
  // 页面加载的时候，做一些事情，在created里面
  mounted() {
		// 加载商品分类
		this.loadType();
    this.load()
  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
     operationSpace() {
      let width = 0
      let _this = this
      setTimeout(() => {
        let rows = document.getElementsByClassName('el-table__row');
        if (rows && rows.length) {
          let tds = rows[0].getElementsByTagName('td')
          let btns = tds[tds.length - 1].getElementsByTagName('button')
          for (let i = 0; i <btns.length; i++) {
            width += btns[i].offsetWidth + 5
          }
          _this.colWidth = width
        }
      }, 0)
    },
    // 分页加载表格数据
    load() {

      request.post(baseUrl + "page?pageNum=" + this.pageNum, this.search).then(res => {
        if (res.code === '0') {
          this.tableData = res.data.list;
          this.total = res.data.total;
          this.operationSpace();
        } else {
          this.$notify.error(res.msg);
        }
      });
    },
    // 新增初始化对话框
    add() {
      this.form = {};
			initWangEditor('');
			this.$nextTick(() => {
				this.$refs.jdtp.clearFiles()
			});

      this.dialogVisible = true;
    },
    // 编辑初始化模态框
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row));
			initWangEditor(row.description);
      this.$nextTick(() => {
				this.$refs.jdtp.clearFiles()
			});

      this.dialogVisible = true;
    },
		initEditorView(o) {
			this.form = JSON.parse(JSON.stringify(o));
			this.richTextDialogVisible = true;
		},

    save() {
			this.form.description = editor.txt.html();

      if (!this.form.id) {
        // 没有ID，说明是新增操作
        request.post(baseUrl, this.form).then(res => {
          if (res.code === '0') {
            this.$notify.success("添加成功");
            this.dialogVisible = false;

            this.load();
          } else {
            this.$notify.error(res.msg);
          }
        })
      } else {
        // 有ID，说明是更新操作
        request.put(baseUrl, this.form).then(res => {
          if (res.code === '0') {
            this.$notify.success('更新成功');
            this.dialogVisible = false;

            this.load();
          } else {
            this.$notify.error(res.msg);
          }
        })
      }
    },
    // 根据ID删除某条数据
    del(id) {
      request.delete(baseUrl + id).then(res => {
        if (res.code === '0') {
          this.$notify.success('删除成功');
          this.load();
        } else {
          this.$notify.error(res.msg);
        }
      });
    },
		jdtpSuccessUpload(res) {
			this.form.img = res.data;
		},
		// 加载商品分类
    loadType() {
			request.get("/type").then(res => {
				if (res.code === '0') {
					this.typeData = res.data;
				} else {
					this.$notify.error(res.msg);
				}
			})
		},

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    avatarSuccessUpload(res) {
      this.form.goodsVideo = res.data;
    },
  }
}
</script>
