<template>
  <div>
    <el-form>
      <el-upload
        ref="upload"
        :limit="limit"
        action
        accept="image/*"
        :on-change="uploadFile"
        list-type="picture-card"
        :auto-upload="false"
        :file-list="imgUrl"
        :on-exceed="handleExceed"
        :on-preview="handlePictureCardPreview"
        :on-remove="handleRemove"
        class="avatar-uploader"
        :class="{hide:showUpload}"
      >
        <i class="el-icon-plus" />
        <div slot="tip" style="color:#999">{{ tips }}</div>
      </el-upload>
      <el-dialog
        width="80%"
        :modal="false"
        :visible.sync="dialogVisible"
      >
        <img width="100%" :src="imgUrl.url" alt>
      </el-dialog>
    </el-form>
  </div>
</template>

<script>
// 引入上传图片接口
import { fileUpload } from '@/api/common'
export default {
  props: {
    limit: {
      type: Number,
      default: 1
    },
    fileList: {
      type: Array,
      default: () => {}
    },
    tips: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      showUpload: false, // 控制limit最大值之后 关闭上传按钮
      dialogVisible: false, // 查看图片弹出框
      imgUrl: []
    }
  },
  // 监听上传图片的数组(为了处理修改时,自动渲染问题,和上传按钮消失问题);
  watch: {
    fileList(newName, oldName) {
      this.imgUrl = newName
      if (newName.length == this.limit) this.showUpload = true
      else this.showUpload = false
    }
  },
  methods: {
    // 文件列表移除文件时的函数
    handleRemove(file) {
      const index = this.imgUrl.findIndex((item) => { return item.id === file.id })
      this.imgUrl.splice(index, 1)
      if (this.imgUrl.length < this.limit) this.showUpload = false
    },
    // 点击文件列表中已上传的文件时的函数
    handlePictureCardPreview(file) {
      this.imgUrl.url = file.url
      this.dialogVisible = true
    },
    // 这里是不需要直接上传而是通过按钮上传的方法
    submitUpload() {
      this.$refs.upload.submit()
    },
    // 文件状态改变时的函数(主要逻辑函数)
    uploadFile(e, imgUrl) {
      // 判断用户上传最大数量限制,来让上传按钮消失
      if (imgUrl.length >= this.limit) this.showUpload = true
      const file = e.raw
      const size = file.size / 1024 / 1024
      console.log(size)
      if (
        !(
          file.type === 'image/png' ||
          file.type === 'image/gif' ||
          file.type === 'image/jpg' ||
          file.type === 'image/jpeg'
        )
      ) {
        this.$notify.warning({
          title: '警告',
          message:
            '请上传格式为image/png, image/gif, image/jpg, image/jpeg的图片'
        })
      } else if (size > 30) {
        this.$notify.warning({
          title: '警告',
          message: '图片大小必须小于30M'
        })
      } else {
        if (this.limit == 1) this.imgUrl = [] // 此处判断为一张的时候需要清空数组
        const formData = new FormData()
        formData.append('file', file)
        fileUpload(formData).then((res) => {
          console.log(file)
          this.imgUrl.push({ ...res, name: res.id })
        })
      }
    },
    // 文件超出个数限制时的函数
    handleExceed() {
      this.$message.info(`最多只允许上传${this.limit}张图片`)
    }
  }
}
</script>
<style lang="scss" scoped>
.avatar-uploader{
  .el-upload{
    width: 120px;
    height: 120px;
    line-height: 120px;
    border-radius: 0px;
    background: #fff;
    border: 1px dashed #ccc;
    i {
        font-size: 28px;
        color: #ccc;
      }

    }
    .el-upload-list {
      display: block;
      .el-upload-tip{
        font-size: 14px;
        color: #ccc;
      }
      .el-upload-list__item {
        width: 120px;
        height: 120px;
        display: block;
        .el-upload-list__item {
          img {
            width: 120px;
            height: 120px;
            border-radius: 0px;
          }
        }
      }
    }
}
::v-deep{
  .el-dialog__header{
    background-color: #fff;
    border-bottom: none;
  }
}
</style>
