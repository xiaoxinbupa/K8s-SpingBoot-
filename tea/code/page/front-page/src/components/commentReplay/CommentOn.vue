<template>
  <div class="comment-on">
    <h1 v-if="showHeader" class="title">评论</h1>
    <div class="replay-container">
      <el-avatar :size="40" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
      <el-input v-model="content" :placeholder="placeholderTips ? placeholderTips : '请输入内容'" class="replay-input" />
      <el-button type="primary" class="btn" size="mini" :disabled="content.length <= 0" @click="commentAdd">发 布</el-button>
    </div>
  </div>
</template>
<script>
import { commentAdd } from '@/apis/article'
export default {
  props: {
    showHeader: {
      type: Boolean,
      default: false
    },
    placeholderTips: {
      type: String,
      default: ''
    },
    isReplaySub: {
      type: Boolean,
      default: false
    },
    currentItemData: {
      type: Object,
      default: () => { }
    }
  },
  data() {
    return {
      content: ''
    }
  },
  methods: {
    // 发表评论
    async commentAdd() {
      try {
        const articleId = this.$router.currentRoute.query.articleId
        const parmas = {
          content: this.content,
          contentId: articleId,
          ip: '127.0.9.8'
        }
        // 如果是回复评论传入楼层id
        if (this.isReplaySub) {
          parmas.referenceIds = this.currentItemData.id || null
        }
        const res = await commentAdd(parmas)
        if (res.data.code == 0) {
          this.$message.success(res.data.message)
          this.content = ''
          // 成功后广播事件，重新获取detail数据
          this.$EventBus.$emit('loadDetail')
        }
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.comment-on{
    .replay-container{
    padding: 20px;
    display: flex;
    justify-content: space-between;
    .replay-input{
      margin: 6px 15px;
      flex: 1;
    }
    .btn{
      height: 32px;
      margin-top: 6px;
    }
  }
}

</style>
