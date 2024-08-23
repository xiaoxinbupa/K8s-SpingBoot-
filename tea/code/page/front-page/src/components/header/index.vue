<template>
  <header class="header">
    <div class="header-content">
      <div class="header-wrap  header-left-wrap">
        <p>欢迎来到学茶商城</p>
        <template v-if="$store.state.user.name">
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              {{ $store.state.user.name }}<i class="el-icon-arrow-down el-icon--right" />
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
        <template v-else>
          <p style="color: #FF0030;">
            <router-link to="/login">请登录</router-link>
          </p>
          <!-- <p><router-link to="/register">注册</router-link></p> -->
        </template>

      </div>
      <div class="header-wrap header-right-wrap">
        <p><router-link to="/myorder">个人中心</router-link></p>
        <i>|</i>
        <p><router-link to="/carpage">购物车</router-link></p>
        <i>|</i>
        <p style="cursor: pointer;" @click="$showNoAuthMsg">消息中心</p>
        <i>|</i>
        <p style="cursor: pointer;" @click="$showNoAuthMsg">
          <span>手机版</span>
        </p>
        <i>|</i>
        <p style="cursor: pointer;" @click="$showNoAuthMsg">商务合作</p>
      </div>
    </div>
  </header>
</template>

<script>

export default {
  name: 'Header',
  data() {
    return {}
  },
  methods: {
    async logout() {
      await this.$store.dispatch('user/resetToken')
      location.reload()
    }
  }
}
</script>

<style scoped lang="scss">
.header {
  min-width: 1220px;
  height: 26px;
  background: #f0f0f0;
  margin-bottom: 10px;
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 26px;
    line-height: 26px;
    width: 700px;
    margin: 0 auto;
    font-weight: 400;
    color: #999999;

    .header-wrap {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      p {
        padding: 0 10px;
        font-size: 12px;
        margin: 0;
      }
      svg {
        width: 12px;
        height: 12px;
        vertical-align: text-top;
        margin: 0 3px;
      }
    }
    .header-right-wrap {
      svg {
        margin-right: -2px;
        vertical-align: text-bottom;
      }
      i {
        display: inline-block;
        height: 14px;
        width: 1px;
        background: #c5c5c5;
        font-size: 0;
      }
    }
  }
}
</style>
