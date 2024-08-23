<template>
  <div class="add-or-edit-dialog">
    <el-dialog
      :title="dialogTitle"
      :visible.sync="setUpStockDialog"
      width="520px"
      :show-close="false"
      @close="handleClose"
    >
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px">
        <el-form-item v-if="['setUpStock','batchInitSkuStock'].includes(type)" label="可用库存" prop="num">
          <el-input v-model="ruleForm.num" />
        </el-form-item>
        <el-form-item v-if="type === 'manualReplenishment'" label="补货原因" prop="reason">
          <el-select
            v-model="ruleForm.reason"
          >
            <el-option
              v-for="item in reasonOpts"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="type === 'manualReplenishment'" label="补货数量" prop="num">
          <el-input v-model="ruleForm.num" />
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
import { initSkuStock, batchInitSkuStock, addSkuStock } from '@/api/stock'
export default {
  props: {
    dialogTitle: {
      type: String,
      default: ''
    },
    setUpStockDialog: {
      type: Boolean,
      default: false
    },
    rowData: {
      type: Object,
      default: () => {}
    },
    ids: {
      type: Array,
      default: () => {}
    },
    type: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      ruleForm: {},
      reasonOpts: [
        { label: '活动大促', value: 1 },
        { label: '大客户预定', value: 2 },
        { label: '战略储备', value: 3 },
        { label: '区域紧缺', value: 4 },
        { label: '其他', value: 5 }
      ],
      rules: {
        num: [{ required: true, message: '请输入数量', trigger: 'blur' }],
        reason: [{ required: true, message: '请选择补货原因', trigger: 'change' }]
      }
    }
  },
  methods: {
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
          if (this.type === 'setUpStock') {
            try {
              await initSkuStock({ num: this.ruleForm.num, skuId: this.rowData.id })
              this.$emit('loadList')
              this.handleClose()
            } catch (error) {
              console.log(error)
            }
          } else if (this.type === 'batchInitSkuStock') {
            try {
              await batchInitSkuStock({ skuIds: this.ids, num: this.ruleForm.num })
              this.$emit('loadList')
              this.handleClose()
            } catch (error) {
              console.log(error)
            }
          } else {
            try {
              await addSkuStock({ num: this.ruleForm.num, reason: this.ruleForm.reason, skuId: this.rowData.id })
              this.$emit('loadList')
              this.handleClose()
            } catch (error) {
              console.log(error)
            }
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
.table-data{
  width: 100%;
  margin: 30px 0px;
}
.el-select{
  width: 100%;
}
.pagination{
  text-align: right;
}
</style>
