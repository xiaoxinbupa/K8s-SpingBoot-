<template>
  <div>
    <Banner />
    <div>
      <div v-for="(item,index) in dataList" :key="index" class="md-bg-wrap">
        <TeaTypeList :item="item" />
      </div>
    </div>
  </div>
</template>

<script>
import Banner from '../components/Banner.vue'
import TeaTypeList from '../components/TeaTypeList.vue'
import { getGoodTtopNodes, getGoodList } from '@/apis/mall'
export default {
  components: {
    Banner,
    TeaTypeList
  },
  data() {
    return {
      categoryIds: [],
      dataList: []
    }
  },
  mounted() {
    this.getGoodTtopNodes()
  },
  methods: {
    async getGoodTtopNodes() {
      try {
        const res = await getGoodTtopNodes()
        this.categoryIds = res
        res.forEach(item => {
          this.getGoodList(item)
        })
      } catch (error) {
        console.log(error)
      }
    },
    async getGoodList(item) {
      try {
        const res = await getGoodList({ frontCategoryId: item.id, pageSize: 8 })
        const data = res.data.data
        const dataObj = {}
        dataObj['categoryName'] = item.categoryName
        dataObj['categoryId'] = item.id
        dataObj['list'] = data.objects
        this.dataList.push(dataObj)
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.md-bg-wrap {
    width: 1220px;
    margin: 0 auto;
    background: #F2F2F2;
  }
  </style>
