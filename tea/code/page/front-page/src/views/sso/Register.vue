<template>
  <div class="login-container">
    <div class="t-box">
      <h3 style="text-align: center; padding: 15px;">用户注册</h3>
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        status-icon
        :rules="rules"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="ruleForm.nickname" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="ruleForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="ruleForm.email" />
        </el-form-item>
        <el-form-item
          label="密码"
          prop="password"
          class="is-required"
        >
          <el-input
            v-model="ruleForm.password"
            type="password"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item
          label="确认密码"
          prop="actPassword"
          class="is-required"
        >
          <el-input
            v-model="ruleForm.actPassword"
            type="password"
            autocomplete="off"
          />
        </el-form-item>

        <div style="text-align: center; padding-left: 15px;">
          <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
          <el-button type="text" class="login-btn" @click=" $router.push({path: '/login'})">立即登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { validEmail } from '@/utils/validate.js'
import { register } from '@/apis/passport.js'

export default {
  name: 'Login',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.actPassword !== '') {
          this.$refs.ruleForm.validateField('actPassword')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    var validateEmail = (rule, value, callback) => {
      if (validEmail(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式错误!'))
      }
    }
    var validatePhone = (rule, value, callback) => {
      if (/^1\d{10}$/.test(value)) {
        callback()
      } else {
        callback(new Error('手机号格式错误!'))
      }
    }
    return {
      ruleForm: {
        actPassword: '',
        password: '',
        nickname: '',
        username: '',
        phone: '',
        email: ''
      },
      rules: {
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],
        actPassword: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { validator: validatePhone, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ]
      },
      redirect: undefined
    }
  },
  watch: {
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          register(this.ruleForm).then(res => {
            this.$message.success('注册成功')
            this.$router.push({ path: '/login' })
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
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
  border: 1px solid #ddd;
  border-radius: 6px;
  background: #fff;
}
.login-btn{
  float: right;
}
</style>
