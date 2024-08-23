<template>
  <div class="add-or-edit-dialog">
    <el-dialog
      title="驳回"
      :visible.sync="dialogVisible"
      width="520px"
      :show-close="false"
      @close="handleClose"
    >
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px">
        <el-form-item label="驳回原因" prop="remark">
          <el-input v-model="ruleForm.remark" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">关 闭</el-button>
        <el-button type="primary" @click="submitForm">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { contentAudit } from '@/api/content'
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      ruleForm: {
        status: 1
      },
      rules: {
        remark: [{ required: true, message: '请输入驳回原因', trigger: 'blur' }]
      }
    }
  },
  mounted() {},
  methods: {
    handleClose() {
      this.$emit('closeDialog')
    },
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
              auditStatus: 2,
              contentId: this.$route.query.id,
              remark: this.ruleForm.remark
            }
            await contentAudit(parmas)
            this.$message.success('成功')
            this.handleClose()
            this.$router.go(-1)
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
