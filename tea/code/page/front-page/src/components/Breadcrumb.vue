<template>
  <div class="mail-list-breadcrumb">
    <el-breadcrumb class="breadcrumb">
      <el-breadcrumb-item v-for="item in breadlitst" :key="item.path">{{ item.title }}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>
<script>
export default {
  data() {
    return {
      breadlitst: []// 面包屑导航
    }
  },
  watch: {
    $route: {
      handler(route) {
        console.log(route)
        const list = route.matched
        list.forEach(item => {
          this.breadlitst.push({
            title: item.meta.title,
            path: item.path ? item.path : '/'
          })
        })
      },
      immediate: true
    }
  }
}
</script>

<style>
.mail-list-breadcrumb{
  margin: 20px 0px 30px 0px;
}
</style>
