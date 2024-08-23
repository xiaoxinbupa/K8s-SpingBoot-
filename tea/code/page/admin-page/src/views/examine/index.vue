<template>
  <el-card shadow="never">
    <div class="category-management">
      <el-menu :default-active="auditStatus" mode="horizontal" @select="handleSelect">
        <el-menu-item index="0">待审批</el-menu-item>
        <el-menu-item index="1">通过</el-menu-item>
        <el-menu-item index="2">驳回</el-menu-item>
      </el-menu>
      <div class="examine-data">
        <el-form ref="searchForm" :model="form" label-width="40px" label-position="right">
          <el-row :gutter="32">
            <el-col :md="8">
              <el-form-item label="名称">
                <el-input
                  v-model="form.title"
                  clearable
                  placeholder="请输入名称"
                />
              </el-form-item>
            </el-col>
            <el-col :md="16">
              <el-form-item align="right">
                <el-button type="primary" @click="getContentList()">查 询</el-button>
                <el-button plain @click="resetSearch()">重 置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
          class="table-data"
        >
          <el-table-column type="index" label="序号" align="center" width="55" />
          <el-table-column prop="title" label="标题" align="center" :show-overflow-tooltip="true" min-width="150" />
          <el-table-column prop="createUserName" label="创建人" align="center" :show-overflow-tooltip="true" min-width="150" />
          <el-table-column prop="categoryName" label="类型" align="center" :show-overflow-tooltip="true" min-width="150" />
          <!-- <el-table-column prop="description" label="标签" align="center" :show-overflow-tooltip="true" min-width="150" /> -->
          <el-table-column prop="gmtCreate" label="发布时间" align="center" :show-overflow-tooltip="true" min-width="150">
            <template slot-scope="scope">
              <div>
                {{ scope.row.gmtCreate ? formatTime(scope.row.gmtCreate, 'YYYY-MM-DD h:m:s') : '' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="auditStatus" label="审批状态" align="center" :show-overflow-tooltip="true" min-width="150">
            <template slot-scope="scope">
              <div>
                {{ auditStatusData[scope.row.auditStatus] }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作"
            width="150"
            align="center"
          >
            <template slot-scope="scope">
              <el-button v-if="auditStatus == 0" type="text" size="small" @click="audit(scope.row)">审批</el-button>
              <el-button v-if="auditStatus == 1 || auditStatus == 2" type="text" size="small" @click="viewExamine(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination
            layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
            :page-size="page.pageSize"
            :page-sizes="[10, 20, 30, 40, 50, 100]"
            :current-page.sync="page.pageNo"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </el-card>

</template>

<script>

import { getContentList } from '@/api/content'
import { formatTimes } from '@/utils'
export default {

  data() {
    return {
      auditStatusData: {
        0: '待审批',
        1: '通过',
        2: '驳回'
      },
      formatTime: formatTimes,
      auditStatus: '0',
      form: {
        title: ''
      },
      tableData: [],
      loading: false,
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.getContentList()
  },
  methods: {
    handleSelect(key, keyPath) {
      this.page.pageNo = 1
      this.auditStatus = key
      this.getContentList()
    },
    async getContentList() {
      try {
        const parmas = {
          auditStatus: this.auditStatus,
          title: this.form.title,
          pageNo: this.page.pageNo,
          pageSize: this.page.pageSize
        }
        const res = await getContentList(parmas)
        this.tableData = res.objects
        this.page.total = res.total
      } catch (error) {
        console.log(error)
      }
    },
    // 重置
    resetSearch() {
      this.form.title = ''
    },
    handleSizeChange(val) {
      this.page.pageSize = val
      this.getContentList()
    },
    handleCurrentChange(val) {
      this.page.pageNo = val
      this.getContentList()
    },
    audit(row) {
      this.$router.push({ name: 'contentDetail', query: { id: row.id, audit: true }})
    },
    viewExamine(row) {
      this.$router.push({ name: 'contentDetail', query: { id: row.id, audit: false }})
    }
  }
}
</script>
<style lang="scss" scoped>
  .examine-data{
    padding:30px 0;
    .table-data{
      margin: 30px 0px;
    }
    .pagination{
      text-align: right;
    }
  }
</style>
