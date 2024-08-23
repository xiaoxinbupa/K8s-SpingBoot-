<template>
  <div class="friend-links">
    <h1 class="title">学茶商城合作伙伴</h1>
    <div class="content">
      <img :src="ncwPcJoin">
    </div>
  </div>
</template>
<script>
import ncwPcJoin from '@/assets/images/ncw-pc-join.jpg'
export default {
  data() {
    return {
      ncwPcJoin
    }
  }
}
</script>
<style>
.friend-links {
  margin-top: 20px;
  background: #fff;
  border-radius: 5px;
  padding: 35px;
}

.friend-links .title {
  margin: 0px auto 20px auto;
  text-align: center;
}

.friend-links .content img {
  width: 810px;
}
</style>
