<template>
  <div class="tea-type-container">
    <div v-if="showTitle" class="tea-type-title">
      <div class="type-title">{{ item.categoryName }}</div>
      <div class="more-list" @click="linkList(item)">更多<i class="el-icon-arrow-right" /></div>
    </div>
    <div class="tea-type-list-container">
      <div v-for="k in item.list" :key="k.id" class="tea-type-list" @click="linkDetail(k)">
        <img :src="k.picture || '/images/01E48904F4CF61B68B8056BDF39F25ED.jpg'" alt="" class="list-image">
        <div class="list-infor">
          <div class="title">{{ k.title }}</div>
          <div class="tips">{{ k.tags.join(',') }}</div>
          <div class="price">{{ (k.salePrice / 100).toFixed(2) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    showTitle: {
      type: Boolean,
      default: true
    },
    item: {
      type: Object,
      default: () => { }
    }
  },
  methods: {
    // 跳转到详情
    linkDetail(item) {
      this.$router.push({
        path: '/malldetail',
        query: {
          id: item.spuId
        }
      })
    },
    // 跳转到当前品类列表
    linkList(item) {
      this.$router.push({
        path: '/malllist',
        query: {
          id: item.categoryId
        }
      })
    }
  }
}
</script>

<style scoped>
  .tea-type-title{
    margin: 50px 0px 20px 0px;
    display: flex;
    justify-content: space-between;
    cursor: pointer;
  }
  .type-title{
    font-weight: 700;
    font-size: 26px;
    color: #333;

  }
  .more-list{
    color: #999;
    font-size: 16px;
    margin-right: 16px;
  }
  .more-list:hover{
    color: #e6a23c;
  }
  .el-icon-arrow-right{
    font-size: 18px;
  }
  .tea-type-list-container{
    display: flex;
    flex-flow: wrap;
  }
  .tea-type-list{
    width: 286px;
    height: 390px;
    background: #fff;
    box-sizing: border-box;
    -webkit-transition: all .15s linear;
    transition: all .15s linear;
    border-radius: 5px;
    overflow: hidden;
    cursor: pointer;
    margin: 0 25px 25px 0;
    padding: 25px;
  }
  .tea-type-list:nth-child(4n) {
    margin: 0 0 30px 0;
  }
  .tea-type-list:hover {
    -webkit-box-shadow: 0 15px 30px rgb(0 0 0 / 10%);
    box-shadow: 0 15px 30px rgb(0 0 0 / 10%);
    -webkit-transform: translate3d(0,-2px,0);
    transform: translate3d(0,-2px,0);
  }
  .list-image{
    width: 230px;
    height: 230px;
    background: #f5f5f5;
  }
  .list-infor{
    padding: 10px
  }
  .title{
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    margin-bottom: 5px;
    font-size: 16px;
    color: #000000;
    font-weight: 500;
  }
  .tips{
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    color: #999;
    margin-bottom: 5px;
    font-size: 14px;
  }
  .price{
    color: #ff5000;
    font-size: 17px;
  }
  .price::before {
    content: "¥";
    margin-right: 5px;
    font-size: 14px;
    font-weight: 400;
  }
</style>
