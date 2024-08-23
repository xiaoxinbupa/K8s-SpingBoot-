<template>
  <div>
    <div class="recommend-list">
      <ul v-if="recommendList && recommendList.length > 0">
        <li v-for="item in recommendList" :key="item.id" @click="$showNoAuthMsg()">
          <h1 class="title"><a :href="item.link" v-text="item.title" /></h1>
          <div>
            <a>
              <el-image class="image" :src="item.image" :alt="item.title" />
            </a>
          </div>
          <el-divider />
        </li>
      </ul>
      <el-empty v-else :image-size="165" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      recommendList: [
        {
          id: 20,
          title: '正山小种',
          image: 'http://dev.img.p.yufeiworld.com/tea_attach/3EC8F9F8D4534E19BEDBCC9B8FDA4795.jpeg'
        },
        {
          id: 19,
          title: '祁门红茶',
          image: 'http://dev.img.p.yufeiworld.com/tea_attach/3E1639EE78D74D139569451CA1182380.jpg'
        }
      ]
    }
  },
  methods: {}
}
</script>

<style scoped>
.recommend-list {
  border-radius: 5px;
  background: #fff;
  padding: 20px;
}

.recommend-list .title {
  font-weight: normal;
  font-size: 16px;
  margin-bottom: 10px;
}

.recommend-list .title a {
  color: #000;
}

.recommend-list .title a:hover {
  color: #44B549;
}

.recommend-list .image {
  width: 260px;
  height: 155px;
  border-radius: 5px;
}
</style>
