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
            <div class="article-list">
              <div v-for="item in articleList" :key="item.id" @click="linkDetail(item)">
                <div class="article-list-item">
                  <div class="left">
                    <el-image class="image" :src="item.coverUrl" :alt="item.title" />
                  </div>
                  <div class="right">
                    <h1 class="title"><a :href="item.link" v-text="item.title" /></h1>
                    <p class="content" v-text="item.content" />
                    <div>
                      <i v-for="tag in item.tags" :key="tag" class="tag" v-text="tag" />
                      <span class="date" v-text="item.gmtPublish" />
                    </div>
                    <div style="clear: both;" />
                  </div>
                </div>
                <el-divider />
              </div>
              <div class="btn-container">
                <el-button round type="success" class="btn-load-more" @click="loadArticleList">查看更多</el-button>
              </div>
            </div>
            <FriendLinks />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RecommendListComponent from '@/components/RecommendListComponentView.vue'
import FriendLinks from '@/components/FriendLinks.vue'
import { formatDate } from '@/utils/index.js'
import { mapState, mapActions } from 'vuex'
export default {
  components: {
    RecommendListComponent,
    FriendLinks
  },
  data() {
    return {
      pageTitle: '',
      defaultSearchKeywords: '',
      recommendList: [],
      articleList: [],
      articleListPageNum: 0,
      articleListPageSize: 5,
      formatDate: formatDate
    }
  },
  computed: {
    ...mapState('article', ['menuId', 'categoryId'])
  },
  mounted() {
    this.loadPageTitle()
    this.loadDefaultSearchKeywords()
    this.loadArticleList()
    this.loadRecommendList()
  },
  methods: {
    ...mapActions('article', ['getContentListData', 'getHotContentList']),
    loadPageTitle() {
      this.pageTitle = '学茶商城网'
      document.title = this.pageTitle
    },
    loadDefaultSearchKeywords() {
      const defaultSearchKeywords = '黑茶怎么泡好喝'
      this.defaultSearchKeywords = defaultSearchKeywords
    },
    // 获取热点列表
    async loadRecommendList() {
      const data = await this.getHotContentList({
        categoryId: this.categoryId || 5,
        pageNum: 10
      })
      this.recommendList = data
    },
    // 获取文章列表
    async loadArticleList() {
      const data = await this.getContentListData({
        menuId: this.menuId || 1,
        pageNo: 1,
        pageSize: 99
      })
      this.articleList = data.objects
    },
    linkDetail(item) {
      this.$router.push({
        path: '/article/details',
        query: {
          articleId: item.id
        }
      })
    }
  }
}
</script>

<style>
</style>
