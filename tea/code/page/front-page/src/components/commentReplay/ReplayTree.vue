<template>
  <div class="replay-tree">
    <div v-for="item in commentList" :key="item.id" class="replay-list">
      <div class="reference-list">
        <div class="header-replay">
          <el-avatar :size="25" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
          <div class="name">{{ item.createUserName }}</div>
          <div class="infor-text">{{ item.content }}</div>
        </div>
        <div class="replay-infor">
          <div class="infor-time">
            <div class="time">{{ formatDate(new Date(item.gmtCreate),"yyyy-MM-dd hh:mm") }}</div>
            <div class="time" @click.stop="commentUp(item)"><i class="iconfont icon-dianzan1" /><span class="num">{{ item.up }}</span></div>
            <div class="time" @click.stop="commentDown(item)"><i class="iconfont icon-cai1" /><span class="num">{{ item.down }}</span></div>
            <div class="replay-btn" @click="showReplay(item)">回复</div>
          </div>
        </div>
        <div v-if="item.showReplay">
          <CommentOn ref="contentReplay" :show-header="false" :placeholder-tips="placeholderTips" :is-replay-sub="isReplaySub" :current-item-data="currentItemData" />
        </div>
      </div>
      <div v-if="item.reCommentList && item.reCommentList.length > 0">
        <Tree :comment-list="item.reCommentList" />
      </div>
    </div>
  </div>
</template>
<script>
import CommentOn from '@/components/commentReplay/CommentOn.vue'
import { formatDate } from '@/utils/index'
import { commentUp, commentDown } from '@/apis/article'
export default {
  name: 'Tree',
  components: {
    CommentOn
  }, // 递归组件，定义自身组件名称
  props: {
    commentList: {
      type: Array,
      default: () => { [] }
    }
  },
  data() {
    return {
      formatDate: formatDate,
      placeholderTips: null,
      isReplaySub: false,
      currentItemData: {},
      showReplayInput: false
    }
  },
  methods: {
    // 显示评论的输入框
    showReplay(item) {
      item.showReplay = !item.showReplay
      this.placeholderTips = `回复@${item.createUserName}:`
      this.isReplaySub = true
      this.currentItemData = item
      this.$forceUpdate()
    },
    // 点赞
    async commentUp(item) {
      try {
        const formData = new FormData()
        formData.append('commentId', item.id)
        const res = await commentUp(formData)
        if (res.data.code == 0) {
          this.$message.success(res.data.message)
          this.$EventBus.$emit('loadDetail')
        }
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    // 踩
    async commentDown(item) {
      try {
        const formData = new FormData()
        formData.append('commentId', item.id)
        const res = await commentDown(formData)
        if (res.data.code == 0) {
          this.$message.success(res.data.message)
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
.replay-tree{
  .replay-list{
    padding: 0px 25px;
    .reference-list{
      .header-replay{
        display: flex;
        .name{
          margin: 0 10px;
          line-height: 28px;
          color: #999;
        }
        .infor-text{
          color: #666;
          line-height: 28px;
          font-size: 14px;
        }
      }
      .replay-infor{
        padding: 0 35px;
        .infor-time{
          color: #999;
          font-size: 12px;
          display: flex;
          padding: 5px 0px;
          line-height: 30px;
          .time{
            margin-right: 10px;
            .iconfont{
              font-size: 16px;
            }
            .icon-cai1{
              font-size: 14px;
            }
            .num{
              font-size: 12px;
              margin-left: 5px;
            }
          }
          .replay-btn{
            cursor: pointer;
          }
        }
      }
    }
  }
}

</style>
