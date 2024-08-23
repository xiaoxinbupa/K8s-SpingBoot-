<template>
  <div class="login-container">
    <div class="t-box">
      <h3 style="text-align: center; padding: 15px;">用户登录</h3>
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        status-icon
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model.trim="ruleForm.username"
            autocomplete="off"
            placeholder="test004"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="ruleForm.password"
            type="password"
            placeholder="123456"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item
          label="验证码"
          prop="vcode"
        >
          <div class="pass-box">
            <el-input
              v-model="ruleForm.vcode"
              autocomplete="off"
              maxlength="4"
            />
            <img
              class="vode-box"
              :src="imgSrc"
              @click="refresh"
            >

          </div>
        </el-form-item>

        <div style="text-align: center; padding-left: 15px;">
          <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
          <el-button type="text" class="login-btn" @click=" $router.push({path: '/register'})">免费注册</el-button>
        </div>
        <div style="text-align: center; padding: 10px 0; font-size: 14px; color: #666;">(体验账号：test004  密码：123456)</div>
      </el-form>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Login',
  data() {
    return {
      imgSrc: 'passport-api/sso/valid/code',
      ruleForm: {
        username: 'test004',
        password: '123456',
        vcode: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        vcode: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
        ]
      },
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$store.dispatch('user/login', this.ruleForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
          }).catch(() => {})
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    refresh() {
      this.imgSrc = this.$options.data().imgSrc + '?' + Math.random()
    }
  }
}
</script>
<style scoped>
.login-container{
  width: 100%;
  height: 100vh;
  background: url('../../assets/images/login-background.png') no-repeat center center;
  background-size: 100% 100%;
  display:flex;
  justify-content:center;
  align-items:center;
}
.t-box{
  padding: 10px 25px 20px 10px;
  width: 500px;
  border-radius: 6px;
  background: #fff;
}
.pass-box{
  display: flex;
}
.vode-box{
  width: 100px;
  height: 36px;
}
.login-btn{
  float: right;
}
</style>
