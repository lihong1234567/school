<template>
  <div class="login-container">
    <div class="form-box">
      <div style="text-align: center; font-weight: bold; color: #333; margin-bottom: 20px; font-size: 20px">欢迎注册</div>
      <el-form>
        <el-form-item>
          <el-input size="small" prefix-icon="el-icon-user" placeholder="请输入昵称" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input size="small" prefix-icon="el-icon-reading" placeholder="请输入学号" v-model="form.studyNum"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input size="small" prefix-icon="el-icon-phone-outline" placeholder="请输入手机号" v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input size="small" prefix-icon="el-icon-message" placeholder="请输入邮箱" v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select size="small" style="width: 100%" placeholder="请选择性别" v-model="form.sex">
            <el-option value="男" label="男"></el-option>
            <el-option value="女" label="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input size="small" show-password prefix-icon="el-icon-lock" placeholder="请输入密码" v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input size="small" show-password prefix-icon="el-icon-lock" placeholder="请确认密码" v-model="form.nmm"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select size="small" style="width: 100%" placeholder="请选择角色" v-model="form.role">
						<el-option value="2" label="用户"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%;background-color: #f3a70f;border-color: #f3a70f;color: white" size="small"  @click="register()">注册</el-button>
        </el-form-item>
				<el-form-item class="regBox">
					已有账号？去 <a href="javascript:void(0)" @click="navLogin()">登录</a>
				</el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "Register",
  data() {
    return {
      form: {}
    }
  },
  // 页面加载的时候，做一些事情，在created里面
  created() {
  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
  // 注册按钮点击事件入口
    register() {
      // 页面表单数据的输入校验
      if (!this.form.username) {
        this.$notify.error("请输入用户名");
        return;
      }
      if (!this.form.studyNum) {
        this.$notify.error("请输入学号");
        return;
      }
      if (!this.form.phone) {
        this.$notify.error("请输入手机号");
        return;
      }
      if (!this.form.email) {
        this.$notify.error("请输入邮箱");
        return;
      }
      if (!this.form.sex) {
        this.$notify.error("请选择性别");
        return;
      }
      if (!this.form.password) {
        this.$notify.error("请输入密码");
        return;
      }
      if (this.form.password !== this.form.nmm) {
        this.$notify.error("两次密码输入不一致");
        return;
      }
      if (!this.form.role) {
        this.$notify.error("请选择角色");
        return;
      }

      // 调用后台注册接口
      request.post('/register', this.form).then(res => {
        if(res.code === '0') {
        // 将用户信息保存一份到浏览器缓存里
          this.$notify.success("注册成功！");
          this.$router.push("/login")
        } else {
        this  .$notify.error(res.msg);
        }
      });
    },
    navLogin() {
      this.$router.push("/login")
    }
  }
}
</script>
<style scoped>
  .login-container {
    height: 100vh;
    overflow: hidden;
    background-image: url("@/assets/imgs/login_bg.jpg");
    background-size: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .form-box {
    width: 350px;
    padding: 20px 40px;
    border-radius: 2px;
    background-color: #ffffff;
    opacity: .9;
  }
  /deep/.el-form-item {
    margin-bottom: 5px;
  }
  /deep/.el-input__inner {
    border-color: #ccc !important;
    border-radius: 2px;
    background-color: #ffffff !important;
    color: #333;
  }
  /deep/.el-input__icon {
    color: #666;
  }

  a {
    text-decoration: none;
    color: blue;
    font-weight: bold;
  }
  .regBox {
    font-size: 13px;
    margin-top: 20px;
    text-align: right;
  }
</style>