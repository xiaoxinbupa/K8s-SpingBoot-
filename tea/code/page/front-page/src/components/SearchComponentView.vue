<template>
  <div>
    <div class="search">
      <i class="el-icon-search" />
      <input v-model="searchKeywords" class="inp-search" :placeholder="defaultSearchKeywords">
      <a class="btn-search" href="javascript:;" @click="$showNoAuthMsg">搜索</a>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    defaultSearchKeywords: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      searchKeywords: ''
    }
  },
  methods: {
    search() {
      let keywords = this.searchKeywords
      if (!keywords) {
        keywords = this.defaultSearchKeywords
      }
      this.$message({
        message: '您正在尝试搜索【' + keywords + '】，抱歉，此功能尚未实现……',
        type: 'warning'
      })
    }
  }
}
</script>

<style scoped>
.search {
  height: 40px;
  background: #fff;
  margin-bottom: 10px;
  position: relative;
}

.search i {
  font-size: 24px;
  color: #999;
  position: absolute;
  width: 30px;
  line-height: 40px;
  left: 10px;
}

.search .inp-search {
  font-size: 14px;
  color: #666;
  border: none;
  outline: none;
  height: 40px;
  position: absolute;
  top: 0;
  left: 30px;
  padding: 0 15px;
  width: 160px;
}

.search a.btn-search {
  background: #44B549;
  font-size: 16px;
  color: #fff;
  width: 80px;
  text-align: center;
  line-height: 40px;
  position: absolute;
  top: 0;
  right: 0;
}
</style>
