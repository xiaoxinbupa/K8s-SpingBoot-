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
        <el-form-item label="标签" prop="tagType">
          <el-select v-model="ruleForm.tagType" placeholder="请选择">
            <el-option label="无上级标签" :value="0" />
            <el-option
              v-for="item in treeData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="标签名称" prop="name">
          <el-input v-model="ruleForm.name" />
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
import { saveTag } from '@/api/tag'
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
    tagData: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      ruleForm: {},
      rules: {
        tagType: [{ required: true, message: '请选择上级类别', trigger: 'change' }],
        name: [{ required: true, message: '请输入标签名称标题', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.echoTagData()
  },
  methods: {
    echoTagData() {
      if (Object.keys(this.tagData).length > 0) {
        this.$set(this.ruleForm, 'tagType', this.tagData.tagType)
        this.$set(this.ruleForm, 'name', this.tagData.name)
      }
    },
    // 关闭弹窗
    handleClose() {
      this.$emit('closeDialog')
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
              name: this.ruleForm.name,
              tagType: this.ruleForm.tagType
            }
            if (this.tagData.id) {
              parmas.id = this.tagData.id
            }
            await saveTag(parmas)
            this.$emit('loadList')
            this.handleClose()
          } catch (error) {
            console.log(error)
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
.el-select{
  width: 100%;
}
</style>
