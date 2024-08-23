<template>
  <div>
    <quill-editor
      ref="myQuillEditor"
      v-model="value"
      :options="editorOption"
      @change="onEditorChange($event)"
    />
    <input id="upload" type="file" style="display:none;" @change="change">
  </div>
</template>

<script>
import { fileUpload } from '@/api/common'
import { quillEditor, Quill } from 'vue-quill-editor'
import { ImageExtend } from 'quill-image-extend-module'
Quill.register('modules/ImageExtend', ImageExtend)
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
export default {
  components: {
    quillEditor
  },
  props: {
    content: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      value: this.content,
      // 富文本框参数设置
      editorOption: {
        modules: {
          ImageExtend: {
            loading: true, // 可选参数 是否显示上传进度和提示语
            name: 'img', // 图片参数名
            size: 30, // 可选参数 图片大小，单位为M，1M = 1024kb
            action: fileUpload, // 服务器地址, 如果action为空，则采用base64插入图片
            response: (res) => {
              return res
            },
            headers: (xhr) => {}, // 可选参数 设置请求头部
            start: () => {}, // 可选参数 自定义开始上传触发事件
            end: () => {}, // 可选参数 自定义上传结束触发的事件，无论成功或者失败
            error: () => {}, // 可选参数 自定义网络错误触发的事件
            change: (xhr, formData) => {} // 可选参数 选择图片触发，也可用来设置头部，但比headers多了一个参数，可设置formData
          },
          toolbar: {
            container: [
              ['bold', 'italic', 'underline', 'strike'], // 加粗，斜体，下划线，删除线
              ['blockquote', 'code-block'],
              [{ header: 1 }, { header: 2 }], // 几级标题
              [{ list: 'ordered' }, { list: 'bullet' }], // 有序列表，无序列表
              [{ script: 'sub' }, { script: 'super' }], // 下角标，上角标
              [{ indent: '-1' }, { indent: '+1' }], // 缩进
              [{ direction: 'rtl' }], // 文字输入方向
              [{ size: ['small', false, 'large', 'huge'] }], // 字体大小
              [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
              [{ color: [] }, { background: [] }], // 颜色选择
              [{ font: ['SimSun', 'SimHei', 'Microsoft-YaHei', 'KaiTi', 'FangSong', 'Arial'] }], // 字体
              [{ align: [] }], // 居中
              ['clean'], // 清除样式,
              ['link', 'image'] // 上传图片、上传视频
            ],
            handlers: {
              image: function(value) {
                if (value) {
                  document.querySelector('#upload').click() // 劫持原来的图片点击按钮事件
                } else {
                  this.quill.format('image', false)
                }
              }
            }
          }
        },
        placeholder: '请输入...'
      }
    }
  },
  watch: {
    'content'(val) {
      this.value = val
    }
  },
  created() {},
  mounted() {},
  methods: {
    change(e) {
      const file = e.target.files[0]
      const formData = new FormData()
      formData.append('file', file)
      fileUpload(formData)
        .then(res => {
          const quill = this.$refs.myQuillEditor.quill
          const length = quill.getSelection().index// 光标位置
          // 插入图片  图片地址
          quill.insertEmbed(length, 'image', res.url)
          // 调整光标到最后
          quill.setSelection(length + 1)// 光标后移一位
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 富文本编辑器内容发生改变的时候
    // 将富文本编辑器输入的文本发送给父组件，父组件涉及提交添加或者更改
    onEditorChange({ quill, html, text }) {
      this.$emit('editorChange', html)
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep{
  .ql-container{
    min-height: 300px;
  }
}

</style>
