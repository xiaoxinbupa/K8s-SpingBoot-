<template>
  <div>
    <!-- 浮动导航 -->
    <!-- <div class="float-link-container">
      <FloatSubjectLinkComponent/>
    </div> -->
    <!-- 页面主体 -->
    <div class="main-container">
      <div class="main-wrap">
        <div class="nav-breadcrumb">
          <!-- 面包屑导航 -->
          <Breadcrumb />
        </div>
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
            <!-- 左侧的文章详情 -->
            <div class="article-details">
              <h1 class="title" v-text="articleDetails.title" />
              <div class="publish-info">
                <span class="author" v-text="articleDetails.author" />
                <span>发布时间：</span>
                <span class="publish-time">{{ formatDate(new Date(articleDetails.gmtCreate),"yyyy-MM-dd hh:mm") }}</span>
              </div>
              <div v-if=" articleDetails.brief" class="brief">{{ articleDetails.brief }}</div>
              <div class="content" v-html="articleDetails.detail" />
              <div class="crown-or-step">
                <el-badge :value="articleDetails.up" class="item">
                  <i class="iconfont icon-dianzan1" @click="contentUp" />
                </el-badge>
                <el-badge :value="articleDetails.down" class="item">
                  <i class="iconfont icon-cai1" @click="contentDown" />
                </el-badge>
              </div>
            </div>
            <Replay ref="replayMore" :comment-list="commentList" @loadDetail="loadDetail" />
            <FriendLinks />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb.vue'
import RecommendListComponent from '@/components/RecommendListComponentView.vue'
import FriendLinks from '@/components/FriendLinks.vue'
import Replay from '@/components/commentReplay/Replay.vue'
import { getContentDetail, contentClick, contentUp, contentDown } from '@/apis/article'
import { formatDate, disabledTree } from '@/utils/index.js'
import { mapState, mapActions } from 'vuex'
export default {
  components: {
    Breadcrumb,
    RecommendListComponent,
    FriendLinks,
    Replay
  },
  data() {
    return {
      pageTitle: '',
      recommendList: [],
      articleDetails: {},
      commentList: [],
      formatDate: formatDate
    }
  },
  computed: {
    ...mapState('article', ['categoryId'])
  },
  mounted() {
    this.loadRecommendList()
    this.loadArticleDetails()
    this.contentClick()
    this.$EventBus.$on('loadDetail', () => {
      this.loadDetail()
    })
  },
  methods: {
    ...mapActions('article', ['getHotContentList']),
    loadPageTitle() {
      this.pageTitle = '学茶商城网'
      document.title = this.pageTitle
    },

    // 获取文章详情
    async loadArticleDetails() {
      try {
        const articleId = this.$router.currentRoute.query.articleId
        const res = await getContentDetail({ contentId: articleId, pageNo: 1, pageSize: 99 })
        this.articleDetails = res.contentDTO // 文章详情
        this.commentList = disabledTree(res.commentList) // 评论列表
      } catch (error) {
        this.$message.error(error.message)
      }
      this.loadPageTitle()
    },

    async contentClick() {
      try {
        const articleId = this.$router.currentRoute.query.articleId
        const formData = new FormData()
        formData.append('contentId', articleId)
        const res = await contentClick(formData)
        console.log('点击查看成功')
      } catch (error) {
        console.log('点击查看失败')
      }
    },
    // 内容顶
    async contentUp() {
      this.$refs.replayMore.showMore = true
      try {
        const articleId = this.$router.currentRoute.query.articleId
        const formData = new FormData()
        formData.append('contentId', articleId)
        const res = await contentUp(formData)
        if (res.data.code == 0) {
          this.$message.success(res.data.message)
          // 成功后更新数据
          this.loadArticleDetails()
        }
      } catch (error) {
        this.$message.success(error.data.message)
      }
    },
    // 内容踩
    async contentDown() {
      this.$refs.replayMore.showMore = true
      try {
        const articleId = this.$router.currentRoute.query.articleId
        const formData = new FormData()
        formData.append('contentId', articleId)
        const res = await contentDown(formData)
        if (res.data.code == 0) {
          this.$message.success(res.data.message)
          // 成功后更新数据
          this.loadArticleDetails()
        }
      } catch (error) {
        this.$message.success(error.data.message)
      }
    },
    // 获取热门列表
    async loadRecommendList() {
      const data = await this.getHotContentList({
        categoryId: this.categoryId || 5,
        pageNum: 10
      })
      this.recommendList = data
    },
    // 评论点赞或踩成功后重新获取详情数据
    loadDetail() {
      this.loadArticleDetails()
    }
  }
}
</script>

<style>
.crown-or-step{
  text-align: center;
}
.item{
  margin-right: 40px;
}
.iconfont{
  font-size: 32px;
  cursor: pointer;
}
.icon-dianzan{
  color: #61666d;
}
.icon-dianzan1{
  color: #00AEEC;
}
.icon-cai1{
  color: #61666d;
  font-size: 28px;
}
.icon-cai{
  color: #00AEEC;
}
.article-details {
  background: #fff;
  border-radius: 5px;
  padding: 35px;
}

.article-details .title {
  margin: 0px auto 20px auto;
}

.article-details .publish-info {
  color: #999;
  font-size: 14px;
  margin: 0px auto 20px auto;
  padding-bottom: 20px;
  border-bottom: 1px solid #ddd;
}

.article-details .publish-info .author {
  margin-right: 30px;
}
.brief{
  padding: 20px;
  background: #f9f9f9;
  border-radius: 4px;
}
.article-details .publish-info .publish-time {
}

.article-details .content {
  margin: 0px auto;
  padding: 20px 20px 0px 20px;
  text-align: left;
  font-size: 18px;
  color: #333;
}
.article-details .content img{
  width: 760px;
  height: 100%;
}
.article-details .content p {
  margin: 24px auto;
}
</style>
