<template>
  <div class="product-category">
    <el-card shadow="never">
      <!-- <el-form ref="searchForm" :model="form" label-width="80px" label-position="right">
        <el-row :gutter="32">
          <el-col :md="8">
            <el-form-item label="类别名称">
              <el-input
                v-model="form.name"
                clearable
                placeholder="请输入类别名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item label="状态">
              <el-select v-model="form.region" placeholder="请选择状态">
                <el-option label="区域一" value="shanghai" />
                <el-option label="区域二" value="beijing" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :md="8">
            <el-form-item align="right">
              <el-button type="primary" @click="$showNoAuthMsg()">查 询</el-button>
              <el-button plain @click="$showNoAuthMsg()">重 置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form> -->
      <div class="supplierBtn">
        <el-button type="primary" @click="createCategory()">新建</el-button>
      </div>
      <el-table
        ref="treeTable"
        :data="categoryList"
        row-key="id"
        :expand-row-keys="expandID"
        stripe
        :tree-props="{children: 'child'}"
        :header-cell-style="{background:'#eef1f6',color:'#606266'}"
        class="table-data"
      >
        <el-table-column type="index" width="50" label="编号" align="center" />
        <el-table-column prop="categoryName" label="类别名称" align="center" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column prop="status" label="状态" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              :active-value="1"
              :inactive-value="0"
              @change="changeStatus(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="gmtCreate" label="创建日期" align="center" :show-overflow-tooltip="true" min-width="150">
          <template slot-scope="scope">
            <div>
              {{ scope.row.gmtCreate ? formatTime(scope.row.gmtCreate, 'YYYY-MM-DD h:m:s') : '' }}
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
            <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" :disabled="scope.row.parentId !=0" @click="addSublevel(scope.row)">添加下级</el-button>
            <el-button type="text" size="small" @click="categoryDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <AddOrEditDialog
      v-if="addOrEditDialogVisible"
      :add-or-edit-dialog-visible="addOrEditDialogVisible"
      :dialog-title="dialogTitle"
      :tree-data="treeData"
      :category-id="categoryId"
      @closeDialog="closeDialog"
      @loadList="loadList"
    />
  </div>
</template>

<script>
import AddOrEditDialog from './addOrEditDialog'
import { formatTimes } from '@/utils'
import { mapState, mapActions } from 'vuex'
import { categoryDisable, categoryEnable, categoryDelete } from '@/api/goods'
export default {
  components: {
    AddOrEditDialog
  },
  data() {
    return {
      form: {
        name: ''
      },
      formatTime: formatTimes,
      expandID: [],
      addOrEditDialogVisible: false,
      dialogTitle: '',
      categoryId: ''
    }
  },
  computed: {
    ...mapState('category', ['categoryList']),
    // eslint-disable-next-line vue/return-in-computed-property
    treeData() {
      const deepCloneCategoryList = JSON.parse(JSON.stringify(this.categoryList))
      deepCloneCategoryList.unshift({ id: 0, categoryName: '无上级品类' })
      return deepCloneCategoryList
    }
  },
  mounted() {
    this.getCategoryList()
  },
  methods: {
    // 获取商品类别
    ...mapActions('category', ['getCategoryList']),
    // 改变状态
    async changeStatus(row) {
      if (process.env.VUE_APP_PACKAGE === 'externalization') {
        return
      }
      try {
        if (row.status == 0) {
          await categoryDisable({ id: row.id })
        } else {
          await categoryEnable({ id: row.id })
        }
        this.getCategoryList()
      } catch (error) {
        console.log(error)
      }
    },
    async categoryDelete(row) {
      try {
        const formData = new FormData()
        formData.append('id', row.id)
        await categoryDelete(formData)
        this.getCategoryList()
      } catch (error) {
        console.log(error)
      }
    },
    // 新建
    createCategory() {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '新建类别'
      this.categoryId = ''
    },
    // 编辑
    edit(row) {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '编辑类别'
      this.categoryId = row.id
    },
    // 添加下级
    addSublevel(row) {
      this.addOrEditDialogVisible = true
      this.dialogTitle = '添加子集'
      this.categoryId = row.id
    },
    // 关闭弹窗
    closeDialog() {
      this.addOrEditDialogVisible = false
    },
    loadList() {
      this.getCategoryList()
    }
  }
}
</script>
<style lang="scss" scoped>
  .table-data{
    margin: 30px 0px;
  }
  .el-cascader{
    width: 100%;
  }
  .el-select{
    width: 100%;
  }
</style>
