<template>
  <div class="banner">
    <!-- 轮播图 -->
    <el-carousel height="450px" arrow="never">
      <el-carousel-item>
        <div>
          <img :src="banner1" alt="banner">
        </div>
      </el-carousel-item>
      <el-carousel-item>
        <div>
          <img :src="banner2" alt="banner">
        </div>
      </el-carousel-item>
    </el-carousel>
    <div class="search-container">
      <div class="search">
        <el-input v-model="search" placeholder="7572普洱茶" class="search-input" />
        <div class="btn" @click="$showNoAuthMsg()">搜 茶</div>
      </div>
      <div class="hot-search-box">
        <ul class="hot-search">
          <li class="hot-search-list">热门搜索</li>
          <li v-for="(item,index) in searchList" :key="index" class="hot-search-list hot-hover" @click="$showNoAuthMsg()">{{ item }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import banner1 from '@/assets/images/banner1.jpg'
import banner2 from '@/assets/images/banner2.jpg'
export default {
  data() {
    return {
      banner1,
      banner2,
      search: '',
      searchList: ['茯砖茶', '小猪茶宠', '大益普洱', '滇红', '水仙']
    }
  },
  mounted() {},
  methods: {}
}
</script>

<style lang="scss" scoped>
.banner{
  position: relative;
  .search-container{
    width: 720px;
    height: 60px;
    position: absolute;
    bottom: 80px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 666;
    .search{
      position: relative;
      ::v-deep {
        .el-input__inner{
          text-indent: 40px;
          border: none;
          width: 720px;
          height: 60px;
          line-height: 60px;
          position: relative;
          border: none;
          border-radius: 50px;
          font-size: 16px;
          color: #666;
          opacity: 0.85;
          outline: none;
        }
      }
      .btn{
        position: absolute;
        top: 0;
        right: 0;
        background: #6A70E9;
        box-shadow: 0 20px 50px rgb(106 112 233 / 50%);
        width: 100px;
        height: 60px;
        color: #fff;
        font-weight: bold;
        position: absolute;
        display: inline-block;
        text-align: center;
        line-height: 60px;
        border-radius: 50px;
        transition: all 0.2s;
        font-size: 16px;
        cursor: pointer;
        letter-spacing: 2px;
      }
    }
    .hot-search-box{
      width: 720px;
      text-align: center;
      color: #fff;
      margin-top: 20px;
      display: flex;
      .hot-search{
        display: flex;
        margin: 0 auto;
        .hot-search-list{
          margin-right: 18px;
          cursor: pointer;
        }
      }
    }
  }
}

.hot-hover:hover{
  color: #6A70E9;
}
.el-carousel__indicators--horizontal{
  display: none;
}
</style>
