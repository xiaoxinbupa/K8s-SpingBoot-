<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <img src="~@/assets/images/find.png" alt="" class="commen-img">
      <img src="~@/assets/images/message.png" alt="" class="commen-img">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img
            :src="avatar ? avatar : defaultAvatar"
            class="user-avatar"
          >
          <span class="user-name">{{ name }}</span>
          <i class="el-icon el-icon-arrow-down" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item>
              首页
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <img src="~@/assets/images/collect.png" alt="" class="commen-img">
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import defaultAvatar from '@/assets/images/logo.png'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data() {
    return {
      defaultAvatar
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'name'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  // box-shadow: 0 1px 4px rgba(0,21,41,.08);
  box-shadow: 0px 1px 10px 0px rgba(0,21,41,0.12);
  border-bottom: 1px solid rgba(0, 21, 41, 0.12);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 1;
    line-height: 50px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding-right: 28px;
    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }
    .commen-img:not(:last-child){
      width: 14px;
      height: 14px;
      margin-right: 24px;
      cursor: pointer;
    }
    .avatar-container {
      margin-right: 24px;

      .avatar-wrapper {
        display: flex;
        align-items: center;
        .user-avatar {
          cursor: pointer;
          width: 24px;
          height: 24px;
          border-radius: 10px;
          vertical-align: middle;
          margin-right: 6px;
        }
        .user-name{
          display: inline-block;
          font-size: 14px;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: normal;
          color: rgba(0,0,0,0.65);
          margin-right: 5px;
        }
        .el-icon-arrow-down{
          margin-top: 6px;
          // margin-right: 24px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
