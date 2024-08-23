<template>
  <div>
    <div class="mail-list-container">
      <Breadcrumb />
      <div>
        <div class="tea-type-list-container">
          <div v-for="k in listData.objects" :key="k.id" class="tea-type-list" @click="linkDetail(k)">
            <img :src="k.picture || '/images/01E48904F4CF61B68B8056BDF39F25ED.jpg'" alt="" class="list-image">
            <div class="list-infor">
              <div class="title">{{ k.title }}</div>
              <div class="tips">{{ k.tags.join(',') }}</div>
              <div class="price">{{ k.salePrice / 100 }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="pagination">
        <div v-if="!listData.firstPage" class="button-btn" @click="upPage()">上一页</div>
        <div v-if="!listData.lastPage" class="button-btn" @click="nextpage()">下一页</div>
      </div>
    </div>
  </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb.vue'
import { getGoodList } from '@/apis/mall'
export default {
  components: {
    Breadcrumb
  },
  data() {
    return {
      pageTitle: '',
      defaultSearchKeywords: '',
      listData: [],
      pageNo: 1
    }
  },
  mounted() {
    this.loadPageTitle()
    this.loadDefaultSearchKeywords()
    this.getGoodList()
  },
  methods: {
    loadPageTitle() {
      this.pageTitle = '茶叶的禁忌及副作用 - 学茶商城网'
      document.title = this.pageTitle
    },
    loadDefaultSearchKeywords() {
      const defaultSearchKeywords = '晚上泡的茶第二天早上能喝吗'
      this.defaultSearchKeywords = defaultSearchKeywords
    },
    async getGoodList() {
      try {
        const id = this.$route.query.id
        console.log(id)
        const res = await getGoodList({ frontCategoryId: id, pageNo: this.pageNo, pageSize: 1 })
        this.listData = res.data.data
        console.log(this.listData)
      } catch (error) {
        console.log(error)
      }
    },
    upPage() {
      this.pageNo--
      this.getGoodList()
    },
    nextpage() {
      this.pageNo++
      this.getGoodList()
    },
    // 跳转到详情
    linkDetail(item) {
      this.$router.push({
        path: '/malldetail',
        query: {
          id: item.spuId
        }
      })
    }
  }
}
</script>

<style scoped>
.mail-list-container{
  width: 1200px;
  margin: 0 auto;
}
.breadcrumb{
  margin: 10px 0px;
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
  .pagination{
    width: 620px;
    margin: 20px auto;
    display: flex;
  }
  .button-btn {
    background: #fff;
    height: 50px;
    line-height: 50px;
    width: 300px;
    display: block;
    border-radius: 20px;
    text-align: center;
    color: #666;
    -webkit-transition: all .15s linear;
    transition: all .15s linear;
    margin-right: 15px;
  }
  .button-btn:hover {
   background: #44B549;
   color: #fff;
  }
</style>
