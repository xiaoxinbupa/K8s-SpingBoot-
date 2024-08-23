<template>
  <div class="publish-product">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span>基本信息</span>
      </div>
      <el-form ref="ruleForm" :model="form" :rules="rules" label-width="80px" label-position="right">
        <el-row>
          <el-col :md="16">
            <el-form-item label="商品名称" prop="title">
              <el-input
                v-model="form.title"
                clearable
                placeholder="请输入商品名称"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item label="导购标题" prop="brief">
              <el-input
                v-model="form.brief"
                clearable
                placeholder="请输入导购标题"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item label="商品类别" prop="categoryId">
              <el-cascader
                v-model="form.categoryId"
                clearable
                :props="{children: 'child', value: 'id',label: 'categoryName',checkStrictly: true, emitPath:false }"
                :options="categoryList"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item label="商品编码" prop="barCode">
              <el-input
                v-model="form.barCode"
                clearable
                placeholder="请输入商品编码"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item label="价格(元)" prop="salePrice">
              <el-input
                v-model="form.salePrice"
                clearable
                placeholder="请输入价格"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item label="排序" prop="sort">
              <el-input
                v-model="form.sort"
                clearable
                placeholder="请输入排序"
              />
            </el-form-item>
          </el-col>
          <el-col :md="16">
            <el-form-item label="关键词" prop="keywords">
              <el-input
                v-model="form.keywords"
                clearable
                placeholder="请输入关键词"
              />
            </el-form-item>
          </el-col>
          <el-col :md="24">
            <el-form-item label="商品图片" required>
              <UpLoadImags ref="upLoadImages" :limit="5" :file-list="imageList" :disabled="false" tips="800*800像素，最多上传 5 张" />
            </el-form-item>
          </el-col>
          <el-col :md="24">
            <el-form-item label="商品详情" required>
              <QuillEditor ref="quillEditor" :content="content" @editorChange="editorChange" />
            </el-form-item>
          </el-col>
          <el-col :md="12">
            <el-form-item align="right">
              <el-button v-if="!$route.query.view" type="primary" @click="submitForm()">{{ $route.query.id ? '编辑商品' : '创建商品' }}</el-button>
              <el-button plain @click="cancel()">取 消</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import UpLoadImags from '@/components/upLoadImags'
import QuillEditor from '@/components/quillEditor'
import { mapState, mapActions } from 'vuex'
import { goodsCreate, goodsDetailCreate, goodsUpdate, getgoodsBasicDetail, getgoodsDetail } from '@/api/goods'
export default {
  components: {
    QuillEditor,
    UpLoadImags
  },
  data() {
    return {
      form: {},
      content: '',
      goodsDetail: {},
      imageList: [],
      rules: {
        title: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        brief: [{ required: true, message: '请输入导购标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择商品类别', trigger: 'change' }],
        barCode: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
        salePrice: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
        keywords: [{ required: true, message: '请输入关键词', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapState('category', ['categoryList'])
  },
  mounted() {
    this.getCategoryList()
    this.getgoodsDetail()
  },
  methods: {
    // 获取商品类别
    ...mapActions('category', ['getCategoryList']),
    // 获取商品详情
    async getgoodsDetail() {
      if (this.$route.query.id) {
        try {
          const resBasic = await getgoodsBasicDetail({ id: this.$route.query.id })
          const res = await getgoodsDetail({ id: this.$route.query.id })
          this.goodsBasicDetail = resBasic
          this.goodsDetail = res
          this.form = this.goodsBasicDetail
          // 价格/100转换成元回显
          this.form.salePrice = (this.goodsBasicDetail.salePrice / 100).toFixed(0)
          if (this.goodsBasicDetail.attachList && this.goodsBasicDetail.attachList.length > 0) {
            this.goodsBasicDetail.attachList.forEach((item, index) => {
              this.imageList.push({
                name: index + 1,
                url: item.url,
                id: item.id
              })
            })
          }
          this.content = this.goodsDetail.detail
          this.$forceUpdate()
        } catch (error) {
          console.log(error)
        }
      }
    },
    // 富文本返回的html String
    editorChange(e) {
      this.content = e
    },
    // 创建商品
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
          const images = this.$refs.upLoadImages.imgUrl
          console.log(images)
          const attachIds = []
          if (images.length <= 0) {
            return this.$message.error('请上传商品图片')
          } else {
            images.forEach(item => {
              attachIds.push(item.id)
            })
          }
          console.log(attachIds)
          if (this.content.length <= 0) {
            return this.$message.error('请编辑商品详情')
          }
          try {
            const parmas = {
              ...this.form,
              salePrice: this.form.salePrice * 100,
              attachIds
            }
            if (this.$route.query.id) {
              parmas.id = this.$route.query.id
              await goodsUpdate(parmas)
              await goodsDetailCreate({ goodsId: this.goodsBasicDetail.id, detail: this.content })
              this.$message.success('编辑成功')
              this.$router.go(-1)
            } else {
              const res = await goodsCreate(parmas)
              await goodsDetailCreate({ goodsId: res, detail: this.content })
              this.$message.success('新建成功')
              this.$router.go(-1)
            }
          } catch (error) {
            console.log(error)
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 返回到上一页
    cancel() {
      this.$router.go(-1)
    }
  }
}
</script>
<style lang="scss" scoped>
.el-cascader{
  width: 100%;
}
</style>
