<template>
  <div class="login-container">
    <div class="form-box">
      <div style="text-align: center; font-weight: bold; color: #333; margin-bottom: 20px; font-size: 20px">欢迎登录</div>
      <el-form>
        <el-form-item>
          <el-input size="small" prefix-icon="el-icon-user" placeholder="管理员输入用户名/学生输入学号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input size="small" show-password prefix-icon="el-icon-lock" placeholder="请输入密码" v-model="form.password"></el-input>
        </el-form-item>
          <el-form-item>
              <div style="display: flex; justify-content: center">
                  <el-input v-model="form.verCode" prefix-icon="el-icon-user" size="small" placeholder="请输入验证码"></el-input>
                  <img :src="captchaUrl" @click="clickImg()"  width="140px" height="33px" />
              </div>
          </el-form-item>
        <el-form-item>
          <el-select size="small" style="width: 100%" placeholder="请选择角色" v-model="form.role">
						<el-option value="1" label="管理员"></el-option>
						<el-option value="2" label="用户"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%;background-color: #f3a70f;border-color: #f3a70f;color: #ffffff" size="small"   @click="login()">登录</el-button>
        </el-form-item>
				<el-form-item class="regBox">
					还没有账号？去 <a href="javascript:void(0)" @click="navRegister()">注册</a>
				</el-form-item>

      </el-form>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "LoginView",
  data() {
    return {
      form: {},
        key:'',
        captchaUrl:''
    }
  },
  // 页面加载的时候，做一些事情，在created里面
  mounted() {
      this.key = Math.random();
      this.captchaUrl= 'http://localhost:8080/captcha?key=' + this.key;
  },
  // 定义一些页面上控件出发的事件调用的方法
  methods: {
    // 登录按钮点击事件入口
    login() {
      // 页面表单数据的输入校验
      if (!this.form.username) {
        this.$notify.error("请输入用户名");
        return;
      }
      if (!this.form.password) {
        this.$notify.error("请输入密码");
        return;
      }
      if (!this.form.verCode) {
        this.$notify.error("请输入验证码");
        return;
      }
      if (!this.form.role) {
        this.$notify.error("请选择角色");
        return;
      }
      // 调用后台登录接口
      request.post('/login?key=' + this.key, this.form).then(res => {
        if(res.code === '0') {
          // 将用户信息保存一份到浏览器缓存里
          let user = res.data.user;
          let token = res.data.token;
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('token', token);
          this.$notify.success("登录成功");
          if (user.role === 1) {
            location.href = "/end/pAdmin"
          } else {
            location.href = "/front/index"
          }
        } else {
          this.$notify.error(res.msg);
          this.key=Math.random();
          this.captchaUrl= 'http://localhost:8080/captcha?key=' + this.key;
          this.form.verCode=''
        }
      });

    },
    navRegister() {
      this.$router.push("/register")
    },
      clickImg(){
          this.key = Math.random();
          this.captchaUrl = 'http://localhost:8080/captcha?key=' + this.key;
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