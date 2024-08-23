<template>
  <div class="detail">
    <!-- 面包屑导航 -->
    <Breadcrumb />
    <!-- 图片和信息价格 -->
    <el-card shadow="never">
      <DetailImageAndInfor :detail-data="detailData" />
    </el-card>
    <div class="detail-information">
      <div>
        <!-- 搜索区域 -->
        <SearchComponent :default-search-keywords="defaultSearchKeywords" />
        <!-- 推荐列表 -->
        <RecommendListComponent />
      </div>
      <!-- 解析富文本编辑器 html String -->
      <el-card shadow="never" class="infor-detail-image">
        <div v-if="content" v-html="content" />
        <el-empty v-else description="暂无详细资料" />
      </el-card>
    </div>
  </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb.vue'
import DetailImageAndInfor from '../components/DetailImageAndInfor.vue'
import SearchComponent from '@/components/SearchComponentView.vue'
import RecommendListComponent from '@/components/RecommendListComponentView.vue'
import { getGoodDetail, getGoodDetailTxt } from '@/apis/mall'
export default {
  components: {
    Breadcrumb,
    DetailImageAndInfor,
    SearchComponent,
    RecommendListComponent
  },
  data() {
    return {
      defaultSearchKeywords: '',
      recommendList: [],
      detailData: {},
      content: null
    }
  },
  mounted() {
    this.getGoodDetail()
    this.getGoodDetailTxt()
  },
  methods: {
    // 获取商品详情
    async getGoodDetail() {
      try {
        const id = this.$route.query.id
        const res = await getGoodDetail({ id: id })
        this.detailData = res
      } catch (error) {
        console.log(error)
      }
    },
    // 获取商品详情富文本
    async getGoodDetailTxt() {
      try {
        const id = this.$route.query.id
        const res = await getGoodDetailTxt({ id })
        this.content = res
      } catch (error) {
        console.log(error)
      }
    }

  }
}
</script>

<style scoped>
.detail{
  width: 1200px;
  margin: 0 auto;
}
.detail-information{
  display: flex;
  margin-top: 20px;
}
.infor-detail-image{
  flex: 1;
  margin-left: 20px;
}
</style>
