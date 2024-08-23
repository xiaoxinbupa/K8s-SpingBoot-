<template>
  <div>
    <el-card shadow="never" class="detail-container">
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px">
        <el-form-item label="文章标题" prop="title" class="ordinary-item">
          <el-input v-model="ruleForm.title" />
        </el-form-item>
        <el-form-item label="文章分类" prop="categoryId" class="ordinary-item">
          <el-cascader
            v-model="ruleForm.categoryId"
            :props="{children: 'child', value: 'id',label: 'name',checkStrictly: true, emitPath:false }"
            :options="treeData"
          />
        </el-form-item>
        <el-form-item label="文章标签" prop="tagIds" class="ordinary-item">
          <el-select v-model="ruleForm.tagIds" multiple placeholder="请选择">
            <el-option-group
              v-for="group in tagTreeData"
              :key="group.label"
              :label="group.label"
            >
              <el-option
                v-for="item in group.options"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="文章摘要" prop="brief" class="ordinary-item">
          <el-input v-model="ruleForm.brief" />
        </el-form-item>
        <el-form-item label="文章封面" required>
          <UpLoadImags ref="upLoadImages" :limit="1" :disabled="false" :tips="'只能上传jpg/png文件，且不超过1MB'" :file-list="imgList" />
        </el-form-item>
        <el-form-item label="文章详情" required>
          <QuillEditor ref="quillEditor" :content="content" @editorChange="editorChange" />
        </el-form-item>
      </el-form>
    </el-card>
    <div class="footer-btn">
      <el-button @click="goBack">返 回</el-button>
      <el-button type="primary" @click="submitForm">保 存</el-button>
    </div>
  </div>
</template>

<script>
import '@/styles/comm.css'
import QuillEditor from '@/components/quillEditor'
import { getCategoryTree } from '@/api/categoryManagement'
import { tagTypeTree } from '@/api/tag'
import UpLoadImags from '@/components/upLoadImags'
import { contentSave, getContentDetail } from '@/api/content'
export default {
  components: {
    QuillEditor,
    UpLoadImags
  },
  data() {
    return {
      ruleForm: {
        tagIds: []
      },
      treeData: [],
      tagTreeData: [],
      content: '',
      imgList: [],
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择类别', trigger: 'change' }],
        tagIds: [{ required: true, message: '请选择文章标签', trigger: 'change' }],
        brief: [{ required: true, message: '请填写文章摘要', trigger: 'blur' }],
        status: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.getCategoryTree()
    this.tagTypeTree()
    this.getContentDetail()
  },
  methods: {
    // 获取类别树列表
    async getCategoryTree() {
      try {
        const res = await getCategoryTree()
        this.treeData = res
      } catch (error) {
        console.log(error)
      }
    },
    async tagTypeTree() {
      try {
        const res = await tagTypeTree()
        const tagTreeData = []
        for (const k in res) {
          tagTreeData.push({
            label: k,
            options: res[k]
          })
        }
        this.tagTreeData = tagTreeData
      } catch (error) {
        console.log(error)
      }
    },
    // 获取文章详情
    async getContentDetail() {
      if (this.$route.query.id) {
        try {
          const res = await getContentDetail({ id: this.$route.query.id })
          this.ruleForm = res
          this.content = res.detail
          // 文章标签反显
          const keywords = JSON.parse(res.keywords)
          const tagIds = []
          if (keywords && keywords.length > 0) {
            keywords.map(item => {
              tagIds.push(item.id)
            })
          }
          this.$set(this.ruleForm, 'tagIds', tagIds)
          this.imgList = [{
            name: '图片', url: res.coverUrl
          }]
        } catch (error) {
          console.log(error)
        }
      }
    },
    // 返回
    goBack() {
      this.$router.go(-1)
    },
    editorChange(e) {
      this.content = e
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
          const id = this.$route.query ? this.$route.query.id : null
          const image = this.$refs.upLoadImages.imgUrl
          if (image.length <= 0) {
            return this.$message.error('请上传封面图！')
          }
          if (this.content.length <= 0) {
            return this.$message.error('请填写文章详情！')
          }
          const parmas = {
            title: this.ruleForm.title, // 标题
            categoryId: this.ruleForm.categoryId, // 类别id
            tagIds: this.ruleForm.tagIds, // 标签id
            brief: this.ruleForm.brief, // 摘要
            coverUrl: image[0].url, // 封面
            detail: this.content, // 详情内容
            editor: 'editor'
          }
          if (id) {
            parmas.id = id
          }
          const res = await contentSave(parmas)
          this.$message.success('success')
          this.$router.go(-1)
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
.detail-container{
  padding-bottom: 150px;
  .ordinary-item {
    width: 420px;
    .el-cascader{
      width: 100%;
    }
    .el-select{
      width: 100%;
    }
  }
}
.footer-btn{
  z-index: 500;
  position: fixed;
  left: 0px;
  bottom: 0;
  width: 100%;
  height: 80px;
  background: #fff;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  text-align: center;
  line-height: 80px;
}
</style>
