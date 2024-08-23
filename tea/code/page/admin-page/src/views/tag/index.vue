<template>
  <div>
    <el-card shadow="never">
      <div class="supplier-btn">
        <el-button type="primary" @click="createTag()">新建</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        :header-cell-style="{background:'#f2f3f5',color:'#606266'}"
        class="table-data"
      >
        <el-table-column prop="name" label="标签类型" align="center" :show-overflow-tooltip="true" min-width="150" />
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
            <el-button type="text" size="small" @click="viewTag(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="edit(scope.row)">修 改</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <CreateTag
      v-if="addOrEditDialogVisible"
      :add-or-edit-dialog-visible="addOrEditDialogVisible"
      :dialog-title="dialogTitle"
      :tag-data="tagData"
      :tree-data="tableData"
      @closeDialog="closeDialog"
      @loadList="loadList"
    />
  </div>
</template>

<script>
import CreateTag from './createTag'
import { getTagList, tagEnable } from '@/api/tag'
import { formatTimes } from '@/utils'
export default {
  components: {
    CreateTag
  },
  data() {
    return {
      formatTime: formatTimes,
      form: {},
      loading: false,
      tableData: [],
      addOrEditDialogVisible: false,
      dialogTitle: '',
      tagData: {}
    }
  },
  mounted() {
    this.getTagList()
  },
  methods: {
    // 获取标签列表
    async getTagList() {
      this.loading = true
      try {
        const res = await getTagList()
        this.tableData = res
        this.loading = false
      } catch (error) {
        this.loading = false
        console.log(error)
      }
    },
    // 改变标签启用状态
    async changeStatus(row) {
      if (process.env.VUE_APP_PACKAGE === 'externalization') {
        return
      }
      try {
        await tagEnable({ ids: [row.id], status: row.status })
        console.log('状态改变成功')
        // 成功后重新获取列表数据
        this.getTagList()
      } catch (error) {
        console.log(error)
      }
      console.log(row.status)
    },
    // 新建标签
    createTag() {
      this.addOrEditDialogVisible = true
      this.tagData = {}
      this.dialogTitle = '新建标签'
    },
    // 编辑标签
    edit(row) {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '编辑标签'
      this.tagData = row
    },
    // 查看标签
    viewTag(row) {
      this.$router.push({ name: 'TagTypeList', query: { id: row.id, name: row.name }})
    },
    // 关闭弹窗
    closeDialog() {
      this.addOrEditDialogVisible = false
    },
    // 创建更新后重新拉取列表数据
    loadList() {
      this.getTagList()
    }
  }
}
</script>
<style lang="scss" scoped>
.supplier-btn{
  margin: 25px 0px;
}
</style>
