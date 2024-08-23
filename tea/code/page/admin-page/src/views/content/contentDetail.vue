<template>
  <div>
    <el-card shadow="never">
      <div class="header">
        <div class="infor-container">
          <div class="title">{{ detailData.title }}</div>
          <div class="infor">
            <div class="infor-list">{{ detailData.gmtCreate ? formatTime(detailData.gmtCreate, 'YYYY-MM-DD h:m:s') : '' }}</div>
            <div class="infor-list">{{ detailData.createUserName }}</div>
            <div class="infor-list">{{ detailData.categoryName }}</div>
            <div class="infor-list">{{ detailData.keywords && filterTags(JSON.parse(detailData.keywords)).join(',') }}</div>
          </div>
        </div>

        <div class="image">
          <el-image
            style="width: 100px; height: 100px"
            :src="url"
            :preview-src-list="srcList"
          />
        </div>
      </div>
      <el-divider />
      <div class="content" v-html="detailData.detail" />
    </el-card>
    <div v-if="$route.query.audit" class="footer-btn">
      <el-button @click="reject">驳回</el-button>
      <el-button type="primary" @click="contentAudit(1)">通 过</el-button>
    </div>
    <ProcessDialog v-if="dialogVisible" :dialog-visible="dialogVisible" @closeDialog="closeDialog" />
  </div>
</template>

<script>
import { getContentDetail, contentAudit } from '@/api/content'
import ProcessDialog from './processDialog'
import { formatTimes } from '@/utils'
export default {
  components: {
    ProcessDialog
  },
  data() {
    return {
      url: '',
      srcList: [],
      detailData: {},
      dialogVisible: false,
      formatTime: formatTimes
    }
  },
  mounted() {
    this.getContentDetail()
  },
  methods: {
    async getContentDetail() {
      try {
        const res = await getContentDetail({ id: this.$route.query.id })
        this.detailData = res
        this.url = res.coverUrl
        this.srcList = [res.coverUrl]
      } catch (error) {
        console.log(error)
      }
    },
    reject() {
      this.dialogVisible = true
    },
    closeDialog() {
      this.dialogVisible = false
    },
    async contentAudit(status) {
      if (process.env.VUE_APP_PACKAGE === 'externalization') {
        try {
          await this.$confirm('亲，这是演示版，你没有权限!,想获得所有权限，请联系咨询老师，获取代码', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        } catch (error) {
          console.log(error)
        }
        return
      }
      try {
        const parmas = {
          auditStatus: status,
          contentId: this.$route.query.id
        }
        await contentAudit(parmas)
        this.$message.success('success')
        this.$router.go(-1)
      } catch (error) {
        console.log(error)
      }
    },
    filterTags(data) {
      if (data && data.length > 0) {
        const tags = []
        data.map(item => {
          tags.push(item.name)
        })
        return tags
      } else {
        return ''
      }
    }
  }
}
</script>
<style lang="scss" scoped>
  .header{
    display: flex;
    justify-content: space-between;
    .infor-container{
      .title{
      font-size: 18px;
      font-weight: 700;
    }
    .infor{
      display: flex;
      justify-content: flex-start;
      color: #666;
      margin: 15px 0px;
      .infor-list{
        margin-right: 10px;
      }
    }
    }
  }
  .footer-btn{
    z-index: 500;
    position: fixed;
    left: 0px;
    bottom: 0;
    width: 100%;
    height: 80px;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    text-align: center;
    line-height: 80px;
  }
</style>
