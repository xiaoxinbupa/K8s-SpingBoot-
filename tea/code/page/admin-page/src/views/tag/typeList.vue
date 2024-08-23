<template>
  <div>
    <el-card shadow="never">
      <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row :gutter="32">
          <el-col :md="8">
            <el-form-item label="名称">
              <el-input
                v-model="form.name"
                clearable
                placeholder="请输入名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item align="right">
              <el-button type="primary" @click="tagList()">查 询</el-button>
              <el-button plain @click="resetSearch()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- <div class="supplier-btn">
        <el-button type="primary" @click="createTag()">新建</el-button>
      </div> -->
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
      >
        <el-table-column prop="name" label="标签名称" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="modifiedUserName" label="更新人" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="gmtModified" label="更新时间" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div>
              {{ scope.row.gmtModified ? formatTime(scope.row.gmtModified, 'YYYY-MM-DD h:m:s') : '' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              @change="changeStatus(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <CreateTag
      v-if="addOrEditDialogVisible"
      :add-or-edit-dialog-visible="addOrEditDialogVisible"
      :dialog-title="dialogTitle"
      :tag-data="tagData"
      :tree-data="treeData"
      @closeDialog="closeDialog"
      @loadList="loadList"
    />
  </div>
</template>

<script>
import { tagList, getTagList } from '@/api/tag'
import { formatTimes } from '@/utils'
import CreateTag from './createTag'
export default {
  components: {
    CreateTag
  },
  data() {
    return {
      form: {},
      loading: false,
      tableData: [],
      page: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      },
      formatTime: formatTimes,
      addOrEditDialogVisible: false,
      dialogTitle: '',
      tagData: {},
      treeData: []
    }
  },
  mounted() {
    this.tagList()
    this.getTagList()
  },
  methods: {
    async tagList() {
      try {
        this.loading = true
        const parmas = {
          name: this.form.name,
          tagType: this.$route.query.id,
          pageNo: this.page.pageNo,
          pageSize: this.page.pageSize
        }
        const res = await tagList(parmas)
        this.tableData = res.objects
        this.page.total = res.total
        this.loading = false
      } catch (error) {
        this.loading = true
        console.log(error)
      }
    },
    // 获取标签列表
    async getTagList() {
      try {
        const res = await getTagList()
        this.treeData = res
      } catch (error) {
        console.log(error)
      }
    },
    // 重置
    resetSearch() {
      this.form.name = ''
    },
    changeStatus(row) {
      console.log(row)
    },
    // 编辑标签
    edit(row) {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '编辑标签'
      this.tagData = row
    },
    closeDialog() {
      this.addOrEditDialogVisible = false
    },
    loadList() {
      this.page.pageNo = 1
      this.tagList()
    }
  }
}
</script>
<style lang="scss" scoped>
.supplier-btn{
  margin: 25px 0px;
}
</style>
