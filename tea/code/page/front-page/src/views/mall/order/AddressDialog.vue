<template>
  <div class="add-or-edit-dialog">
    <el-dialog
      :title="dialogTitle"
      :visible.sync="addressDialog"
      width="470px"
      :show-close="false"
      @close="handleClose"
    >
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="left" label-width="100px">
        <el-form-item label="所在地区" prop="addressArea">
          <el-cascader
            ref="cascaderAddress"
            v-model="ruleForm.addressArea"
            :options="cityTree"
            :props="{ value: 'id',label: 'name' ,emitPath:false }"
            @change="handleChange"
          />
        </el-form-item>
        <el-form-item label="收货人姓名" prop="consigneeName">
          <el-input v-model="ruleForm.consigneeName" />
        </el-form-item>
        <el-form-item label="电话" prop="consigneeMobile">
          <el-input v-model.number="ruleForm.consigneeMobile" maxlength="11" />
        </el-form-item>
        <el-form-item label="详细地址" prop="consigneeDetailAddress">
          <el-input v-model="ruleForm.consigneeDetailAddress" type="textarea" maxlength="150" show-word-limit />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">关 闭</el-button>
        <el-button type="success" @click="submitForm">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCityTree, createAddress, addressUpdate, getAddressById } from '@/apis/mall'
import { mapState } from 'vuex'
export default {
  props: {
    addressDialog: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    },
    addressId: {
      type: [Number, String],
      default: ''
    },
    userId: {
      type: [Number, String],
      default: 1
    }
  },
  data() {
    // 手机号校验
    const checkPhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入手机号码'))
      } else if (!/^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        addressArea: []
      },
      rules: {
        addressArea: [{ required: true, message: '请选择所在地区', trigger: 'change' }],
        consigneeName: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
        consigneeMobile: [{ required: true, validator: checkPhone, trigger: 'blur' }],
        consigneeDetailAddress: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
      },
      options: []
    }
  },
  computed: {
    ...mapState('address', ['cityTree'])
  },

  mounted() {
    this.getCityTree()
    this.getAddressById()
  },
  methods: {
    // 编辑会填表单

    // 获取城市树行结构
    async getCityTree() {
      try {
        const res = await getCityTree()
        this.options = res
      } catch (error) {
        console.log(error)
      }
    },
    // 跟据ID获取地址详情并给省市区赋值
    async getAddressById() {
      if (this.addressId) {
        try {
          const res = await getAddressById({ id: this.addressId })
          this.addressDetail = res
          this.ruleForm = this.addressDetail
          this.ruleForm.province = this.addressDetail.province || null
          this.ruleForm.provinceCode = this.addressDetail.provinceCode || null
          this.ruleForm.city = this.addressDetail.city || null
          this.ruleForm.cityCode = this.addressDetail.cityCode || null
          this.ruleForm.area = this.addressDetail.area || null
          this.ruleForm.areaCode = this.addressDetail.areaCode || null
          this.ruleForm.addressArea = Number(this.addressDetail.areaCode)
        } catch (error) {
          console.log(error)
        }
      }
    },
    // 关闭弹窗
    handleClose() {
      this.$emit('closeDialog')
    },
    // 城市选择框改变触发并给省市区赋值
    handleChange() {
      this.$nextTick(() => {
        const selectCategory = this.$refs['cascaderAddress'].getCheckedNodes()[0]
        const pathNodes = selectCategory.pathNodes
        // 设置省市区
        this.ruleForm.province = pathNodes[0].label || null
        this.ruleForm.provinceCode = pathNodes[0].value || null
        this.ruleForm.city = pathNodes[1].label || null
        this.ruleForm.cityCode = pathNodes[1].value || null
        this.ruleForm.area = pathNodes[2].label || null
        this.ruleForm.areaCode = pathNodes[2].value || null
      })
    },
    // 提交
    submitForm() {
      this.$refs['ruleForm'].validate(async(valid) => {
        if (valid) {
          try {
            const parmas = {
              province: this.ruleForm.province,
              provinceCode: this.ruleForm.provinceCode,
              city: this.ruleForm.city,
              cityCode: this.ruleForm.cityCode,
              area: this.ruleForm.area,
              areaCode: this.ruleForm.areaCode,
              ...this.ruleForm
            }
            if (!this.addressId) {
              parmas.userId = 1
              parmas.isDefault = false
              const res = await createAddress(parmas)
              this.$emit('loadAddress')
              this.$message.success('新建成功')
              this.handleClose()
            } else {
              parmas.userId = this.userId
              parmas.id = false
              const res = await addressUpdate(parmas)
              this.$emit('loadAddress')
              this.$message.success('更新成功！')
              this.handleClose()
            }
          } catch (error) {
            this.$message.error('失败！')
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
::v-deep {
  .el-textarea__inner {
    resize: none;
  }
}
</style>
