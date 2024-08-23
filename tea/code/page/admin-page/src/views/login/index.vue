<template>
  <div class="container">
    <div class="login-container">
      <div class="system-title">
        <div class="logo-iamge">
          <img src="@/assets/images/logo.png" class="logo">
        </div>
        <div class="logo-system-name">学茶网后台管理系统</div>
      </div>
      <div class="container-login">
        <div class="container-left">
          <div class="system-name">学茶网后台管理系统</div>
          <div class="system-infor">通过门店商品、会员、营销、订单、售后的在线化管理，<br>实现门店线上业务全面数字化和智能化</div>
        </div>
        <div class="container-right">
          <div class="user-login-title">用户登录</div>
          <el-form ref="ruleForm" :model="ruleForm" :rules="rules">
            <el-form-item prop="username" class="user-item">
              <el-input v-model="ruleForm.username" placeholder="用户名" size="medium" prefix-icon="el-icon-user-solid" />
            </el-form-item>
            <el-form-item prop="password" class="user-item">
              <el-input v-model="ruleForm.password" placeholder="登录密码" type="password" autocomplete="off" prefix-icon="el-icon-lock" />
            </el-form-item>
            <el-form-item
              prop="vcode"
            >
              <div class="pass-box">
                <el-input
                  v-model="ruleForm.vcode"
                  autocomplete="off"
                  placeholder="验证码"
                  maxlength="4"
                  prefix-icon="el-icon-key"
                />
                <img
                  class="vode-box"
                  :src="imgSrc"
                  @click="refresh"
                >
              </div>
            </el-form-item>
            <div class="btn">
              <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
            </div>
          </el-form>
          <div class="title">(体验账号：test004 密码：123456)</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>

export default {
  name: 'Login',
  data() {
    return {
      imgSrc: '/to_passport/sso/valid/code',
      ruleForm: {
        username: '',
        password: '',
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
<style lang="scss" scoped>
.container{
  width: 100%;
  height: 100%;
  background: url('../../assets/images/login-background.png') no-repeat center center;
  background-size: 100% 100%;
  .login-container{
    height: 100%;
    margin: 0px 150px;
    .system-title{
      display: flex;
      justify-content: flex-start;
      padding-top: 40px;
      .logo-iamge{
        width: 28px;
        height: 28px;
        margin-right: 13px;
        .logo {
          width: 100%;
          height: 100%;
      }
    }
    .logo-system-name{
        font-weight: 600;
        font-size: 24px;
        line-height: 30px;
        color: #FFFFFF;
      }
    }
    .container-login{
      height: 570px;
      margin-top: 95px;
      background: rgba(6, 19, 31, 0.4);
      box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.06);
      backdrop-filter: blur(10.5px);
      border-radius: 20px;
      padding: 65px 80px;
      display: flex;
      justify-content: space-between;
      .container-left{
        flex: 1;
        .system-name{
          margin-top: 122px;
          font-weight: 600;
          font-size: 36px;
          line-height: 50px;
          color: #FFFFFF;
        }
        .system-infor{
          margin-top: 16px;
          font-weight: 400;
          font-size: 16px;
          line-height: 32px;
          color: #FFFFFF;
        }
      }
      .container-right{
        width: 376px;
        height: 440px;
        background: #FFFFFF;
        box-shadow: 0px 2px 16px 2px rgba(15, 19, 51, 0.1);
        border-radius: 12px;
        padding: 40px;
        .user-login-title{
          font-weight: 600;
          font-size: 20px;
          line-height: 20px;
          color: #333333;
          margin-bottom: 24px;
        }
        .user-item{
          ::v-deep  .el-input__inner{
            width: 296px;
            height: 48px;
            border: 0.5px solid #C0C4CC;
            border-radius: 4px;
            padding-left: 40px;
          }
        }
        .pass-box{
          display: flex;
          ::v-deep  .el-input__inner{
            width: 196px;
            height: 48px;
            border: 0.5px solid #C0C4CC;
            border-radius: 4px;
            padding-left: 40px;
            margin-right: 10px;
          }
        }
        .btn{
          ::v-deep .el-button{
            width: 296px;
            height: 48px;
            background: #35B4EF;
            box-shadow: 0px 2px 4px rgba(251, 72, 72, 0.2);
            border-radius: 4px;
            border: none;
            font-weight: 400;
            font-size: 16px;
            text-align: center;
            color: #FFFFFF;
            cursor: pointer;
          }
        }
        .title{
          margin-top: 20px;
          font-weight: 400;
          font-size: 14px;
          color: #666;
          text-align: center;
        }
      }
    }
  }
}
</style>
