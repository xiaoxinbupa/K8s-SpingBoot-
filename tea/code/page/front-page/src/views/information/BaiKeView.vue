<template>
  <div>
    <!-- 浮动导航 -->
    <!-- <div class="float-link-container">
      <FloatSubjectLinkComponent/>
    </div> -->
    <!-- 页面主体 -->
    <div class="main-container">
      <div class="main-wrap">
        <!-- 右侧的列表 -->
        <div class="right-aside-container">
          <div class="right-aside-wrap">
            <!-- 搜索区域 -->
            <!-- <SearchComponent :defaultSearchKeywords="defaultSearchKeywords"/> -->
            <!-- 推荐列表 -->
            <RecommendListComponent :recommend-list="recommendList" />
          </div>
        </div>
        <!-- 左侧主体 -->
        <div class="left-aside-container">
          <div class="left-aside-wrap">
            <!-- 左侧的文章列表 -->
            <div v-if="categoryList && categoryList.length > 0" class="article-list">
              <div v-for="(item,index) in categoryList" :key="index" class="key-list">
                <div class="title">{{ item.name }}</div>
                <div class="key-item-list">
                  <div v-for="k in item.child" :key="k.id" class="key-item" @click="linkList(item)">{{ k.name }}</div>
                </div>
              </div>
            </div>
            <el-empty v-else :image-size="200" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RecommendListComponent from '@/components/RecommendListComponentView.vue'
import { getCategoryList } from '@/apis/article.js'
import { mapState, mapActions } from 'vuex'
export default {
  components: {
    RecommendListComponent
  },
  data() {
    return {
      recommendList: [],
      categoryList: []
    }
  },
  computed: {
    ...mapState('article', ['menuId', 'categoryId'])
  },
  mounted() {
    this.loadRecommendList()
    this.getCategoryList()
  },
  methods: {
    ...mapActions('article', ['getHotContentList']),
    // 获取类别列表
    async getCategoryList() {
      console.log(this.menuId)
      try {
        const res = await getCategoryList(this.menuId)
        this.categoryList = res.child
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    // 获取热点列表
    async loadRecommendList() {
      const data = await this.getHotContentList({
        categoryId: this.categoryId || 5,
        pageNum: 10
      })
      this.recommendList = data
    },
    linkList(item) {
      console.log(item)
      this.$router.push({
        path: '/typelist',
        query: {
          articleId: item.id
        }
      })
    }
  }
}
</script>

<style scoped>
.main-layout{
  text-align: left !important;
}
.title{
  font-weight: 700;
  margin-bottom: 30px;
  font-size: 18px;
}
.key-item-list{
  display: flex;
}
.key-item{
  background: #F5F5F5;
  padding: 8px 15px;
  border-radius: 5px;
  margin: 0 0 20px 30px;
  color: #444;
  text-align: center;
  width: 100px;
  cursor: pointer;
  font-size: 12px;
}
.key-item:hover {
  background: #44B549;
  color: #fff;
}
.el-empty{
  background: #fff;
}
</style>
