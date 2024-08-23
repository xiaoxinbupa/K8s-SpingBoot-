<template>
  <div class="add-or-edit-dialog">
    <el-dialog
      :title="dialogTitle"
      :visible.sync="addOrEditDialogVisible"
      width="520px"
      :show-close="false"
      @close="handleClose"
    >
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="name">
          <el-input v-model="ruleForm.name" />
        </el-form-item>
        <el-form-item label="上级类别" prop="parentId">
          <el-cascader
            v-model="ruleForm.parentId"
            :props="{children: 'child', value: 'id',label: 'name',checkStrictly: true, emitPath:false }"
            :options="treeData"
            @change="handleChange"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="ruleForm.sort" />
        </el-form-item>
        <el-form-item label="链接类型" prop="code">
          <el-input v-model="ruleForm.code" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="ruleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">关 闭</el-button>
        <el-button type="primary" @click="submitForm">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { categorySave, getCategoryDetail } from '@/api/categoryManagement'
export default {
  props: {
    addOrEditDialogVisible: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    },
    treeData: {
      type: Array,
      default: () => []
    },
    categoryId: {
      type: [Number, String],
      default: ''
    }
  },
  data() {
    return {
      ruleForm: {
        status: 1
      },
      parentId: '',
      rules: {
        name: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        parentId: [{ required: true, message: '请选择上级类别', trigger: 'change' }],
        code: [{ required: true, message: '类型链接', trigger: 'blur' }],
        status: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.getCategoryDetail()
  },
  methods: {
    // 关闭弹窗
    handleClose() {
      this.$emit('closeDialog')
    },
    // 上级类别改变
    handleChange(value) {
      console.log(value)
    },
    async getCategoryDetail() {
      if (this.categoryId) {
        try {
          const res = await getCategoryDetail({ categoryId: this.categoryId })
          this.detailData = res
          this.ruleForm = this.detailData
          this.ruleForm.parentId = this.detailData.parentId
          this.parentId = res.parentId
        } catch (error) {
          console.log(error)
        }
      }
    },
    // 提交
    async submitForm() {
      if (process.env.VUE_APP_PACKAGE === 'externalization') {
        try {
          await this.$confirm('亲，这是演示版，你没有权限!,想获得所有权限，请联系咨询老师，获取代码', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
        } catch (error) {
          console.log(error)
        }
        return
      }
      this.$refs['ruleForm'].validate(async(valid) => {
        if (valid) {
          try {
            const parmas = {
              detailUrl: '/detailUrl',
              listUrl: '/listUrl',
              newUrl: '/newUrl',
              name: this.ruleForm.name,
              code: this.ruleForm.code,
              parentId: this.ruleForm.parentId,
              sort: this.ruleForm.sort,
              status: this.ruleForm.status
            }
            if (this.categoryId) {
              if (this.dialogTitle === '添加子集') {
                parmas.parentId = this.ruleForm.parentId
              } else {
                parmas.id = this.categoryId
                parmas.parentId = this.parentId
              }
            }
            const res = await categorySave(parmas)
            this.$message.success('成功')
            this.$emit('loadList')
            this.handleClose()
          } catch (error) {
            this.$message.error(error.message)
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.el-form-item {
  width: 420px;
}
.el-cascader{
  width: 100%;
}
.el-select{
  width: 100%;
}
</style>
