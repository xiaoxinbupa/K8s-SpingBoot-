<template>
  <div class="add-or-edit-dialog">
    <el-dialog
      :title="dialogTitle"
      :visible.sync="addOrEditDialogVisible"
      width="520px"
      :modal="false"
      :show-close="false"
      @close="handleClose"
    >
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="ruleForm.title" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="ruleForm.sort" />
        </el-form-item>
        <el-form-item label="图标" required>
          <UpLoadImags ref="upLoadImages" :limit="1" :disabled="false" :tips="'只能上传jpg/png文件，且不超过1MB'" :file-list="imgList" />
        </el-form-item>
        <el-form-item label="跳转类型" prop="type">
          <div class="radio-container">
            <el-radio-group v-model="ruleForm.type">
              <el-radio :label="1">内部链接</el-radio>
              <el-radio :label="2">外部链接</el-radio>
            </el-radio-group>
          </div>
          <div class="select-container">
            <el-form-item v-if="ruleForm.type === 1" prop="categoryId">
              <el-cascader
                v-model="ruleForm.categoryId"
                :props="{children: 'child', value: 'id',label: 'name',checkStrictly: true, emitPath:false }"
                :options="bindList"
              />
            </el-form-item>
            <el-form-item v-if="ruleForm.type === 2" prop="url">
              <el-input v-model="ruleForm.url" />
            </el-form-item>
          </div>
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
import '@/styles/comm.css'
import UpLoadImags from '@/components/upLoadImags'
import { getBindList, getMenuDetail, postMenuSave } from '@/api/menu'
export default {
  components: {
    UpLoadImags
  },
  props: {
    addOrEditDialogVisible: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    },
    menuId: {
      type: [String, Number],
      default: 0
    }
  },
  data() {
    return {
      ruleForm: {
        type: 1,
        status: 1
      },
      bindList: [],
      imgList: [],
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        type: [{ required: true, message: '请选择跳转类型', trigger: 'change' }],
        code: [{ required: true, message: '类型链接', trigger: 'blur' }],
        status: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
        // categoryId: [{ required: true, message: '请选择类别', trigger: 'change' }],
        // url: [{ required: true, message: '请输入链接地址', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.getMenuDetail()
  },
  methods: {
    // 获取菜单详情
    async getMenuDetail() {
      if (this.menuId) {
        try {
          const res = await getMenuDetail({ menuId: this.menuId })
          const resBind = await getBindList()
          // 因为bind接口返回未绑定的一级类目数据，会显当前绑定的类目需要把当前详情返回的类目加到bind数据里面
          if (res.type === 1) {
            const bindData = [{
              id: res.categoryId,
              name: res.categoryName
            }]
            this.bindList = resBind.concat(bindData)
          } else {
            this.bindList = resBind
          }
          this.menuDetail = res
          this.ruleForm = res
          this.imgList = [{
            name: '图片',
            url: res.icon,
            id: res.fileId
          }]
          this.$forceUpdate()
        } catch (error) {
          this.$message.error(error)
        }
      } else {
        this.bindList = await getBindList()
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
          const image = this.$refs.upLoadImages.imgUrl
          if (image.length <= 0) {
            return this.$message.error('请上传icon')
          }
          const parmas = {
            icon: image[0].url,
            fileId: image[0].id,
            sort: this.ruleForm.sort,
            status: this.ruleForm.status,
            title: this.ruleForm.title,
            type: this.ruleForm.type
          }
          if (this.ruleForm.type === 1) {
            parmas.categoryId = this.ruleForm.categoryId
            parmas.url = ''
          } else {
            parmas.url = this.ruleForm.url
            parmas.categoryId = 0
          }
          if (this.menuId) {
            parmas.id = this.menuId
          }
          await postMenuSave(parmas)
          this.handleClose()
          this.$emit('loadList')
          console.log(1111)
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
.select-container{
  margin-top: 20px;
  .el-form-item {
  width: 320px;
}
.el-select{
  width: 320px;
}
}
.el-cascader{
  width: 100%;
}

</style>
