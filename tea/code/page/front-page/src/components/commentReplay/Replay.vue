<template>
  <div class="replay">
    <CommentOn ref="content" :show-header="true" :is-replay-sub="false" />
    <ReplayTree :comment-list="commentList" />
  </div>
</template>
<script>

import ReplayTree from './ReplayTree.vue'
import CommentOn from '@/components/commentReplay/CommentOn.vue'
import { formatDate } from '@/utils/index'

export default {
  components: {
    CommentOn,
    ReplayTree
  },
  props: {
    commentList: {
      type: Array,
      default: () => { [] }
    }
  },
  data() {
    return {
      formatDate: formatDate
    }
  },
  methods: {
    lodeMore() {
      this.$emit('loadDetail')
    }
  }
}
</script>

<style lang="scss" scoped>
.replay{
  margin-top: 20px;
  background: #fff;
  border-radius: 5px;
  padding: 35px;
  .see-more{
    font-size: 12px;
    color: #999;
    padding-left: 30px;
    cursor: pointer;
  }
}

</style>
