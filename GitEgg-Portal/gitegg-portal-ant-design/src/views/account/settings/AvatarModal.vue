<template>

  <a-modal
    title="修改头像"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    :width="800"
    :footer="null"
    @cancel="cancelHandel">
    <a-row>
      <a-col :xs="24" :md="12" :style="{height: '350px'}">
        <vue-cropper
          ref="cropper"
          :img="options.img"
          :info="true"
          :autoCrop="options.autoCrop"
          :autoCropWidth="options.autoCropWidth"
          :autoCropHeight="options.autoCropHeight"
          :fixedBox="options.fixedBox"
          @realTime="realTime"
        >
        </vue-cropper>
      </a-col>
      <a-col :xs="24" :md="12" :style="{height: '350px'}">
        <div class="avatar-upload-preview">
          <img :src="previews.url" :style="previews.img"/>
        </div>
      </a-col>
    </a-row>
    <br>
    <a-row>
      <a-col :lg="2" :md="2">
        <a-upload name="file" :beforeUpload="beforeUpload" :showUploadList="false">
          <a-button icon="upload">选择图片</a-button>
        </a-upload>
      </a-col>
      <a-col :lg="{span: 1, offset: 2}" :md="2">
        <a-button icon="plus" @click="changeScale(1)"/>
      </a-col>
      <a-col :lg="{span: 1, offset: 1}" :md="2">
        <a-button icon="minus" @click="changeScale(-1)"/>
      </a-col>
      <a-col :lg="{span: 1, offset: 1}" :md="2">
        <a-button icon="undo" @click="rotateLeft"/>
      </a-col>
      <a-col :lg="{span: 1, offset: 1}" :md="2">
        <a-button icon="redo" @click="rotateRight"/>
      </a-col>
      <a-col :lg="{span: 2, offset: 6}" :md="2">
        <a-button type="primary" @click="finish('blob')">保存</a-button>
      </a-col>
    </a-row>
  </a-modal>

</template>
<script>
import { queryDefaultDfs } from '@/api/system/extension/dfs/dfs'
import { dfsUpload } from '@/api/system/extension/dfs/dfs_upload'
export default {
  name: 'AvatarModal',
  data () {
    return {
      visible: false,
      id: null,
      confirmLoading: false,
      fileList: [],
      uploading: false,
      options: {
        img: '',
        autoCrop: true,
        autoCropWidth: 200,
        autoCropHeight: 200,
        fixedBox: true
      },
      previews: {},
      dfsConfig: {
        dfsType: '',
        dfsCode: ''
      },
      uploadForm: {
        dfsType: '',
        dfsCode: '',
        uploadFile: null
      }
    }
  },
  created () {
    this.getDefaultDfs()
  },
  methods: {
    async getDefaultDfs () {
      await queryDefaultDfs().then(response => {
        this.dfsConfig = response.data
      })
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await this.getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    edit (id) {
      this.visible = true
      this.id = id
      /* 获取原始头像 */
    },
    close () {
      this.id = null
      this.visible = false
    },
    cancelHandel () {
      this.close()
    },
    changeScale (num) {
      num = num || 1
      this.$refs.cropper.changeScale(num)
    },
    rotateLeft () {
      this.$refs.cropper.rotateLeft()
    },
    rotateRight () {
      this.$refs.cropper.rotateRight()
    },
    beforeUpload (file) {
      const reader = new FileReader()
      // 把Array Buffer转化为blob 如果是base64不需要
      // 转化为base64
      reader.readAsDataURL(file)
      reader.onload = () => {
        this.options.img = reader.result
      }
      // 转化为blob
      // reader.readAsArrayBuffer(file)

      return false
    },

    // 上传图片（点击上传按钮）
    finish (type) {
      const that = this
      // 输出
      if (type === 'blob') {
        that.$refs.cropper.getCropBlob((data) => {
          const img = window.URL.createObjectURL(data)
          const file = new window.File([data], data.name, { type: data.type })
          that.model = true
          that.modelSrc = img
          that.uploadForm.dfsType = that.dfsConfig.dfsType
          that.uploadForm.dfsCode = that.dfsConfig.dfsCode
          const formData = new FormData()
          formData.append('dfsCode', that.uploadForm.dfsCode)
          formData.append('uploadFile', file)
          that.uploading = true
          dfsUpload(formData).then(response => {
              that.uploading = false
              if (response.data && response.data.length > 0) {
                  that.$emit('ok', response.data[0].fileUrl)
                  that.visible = false
                  that.$message.success('上传成功')
              }
          }).catch(err => {
            console.log('uploading', err)
            that.$message.error('上传失败')
          })
        })
      } else {
        that.$refs.cropper.getCropData((data) => {
          that.model = true
          that.modelSrc = data
        })
      }
    },
    handleUpload () {
        this.uploadForm.dfsType = this.dfsConfig.dfsType
        this.uploadForm.dfsCode = this.dfsConfig.dfsCode
        this.uploadedFileName = ''
        const { fileList } = this
        const file = fileList[fileList.length - 1]
        const formData = new FormData()
        formData.append('dfsCode', this.uploadForm.dfsCode)
        formData.append('uploadFile', file)
        this.uploading = true
        dfsUpload(formData).then(response => {
            this.uploading = false
            console.log(formData)
            if (response.data && response.data.length > 0) {
                file.url = response.data[0].fileUrl
                this.imgUrlList.push(file.url)
                this.emitInput(this.imgUrlList.join())
                this.$message.success('上传成功')
            }
        }).catch(err => {
          console.log('uploading', err)
          this.$message.error('上传失败')
        })
    },
    okHandel () {
      const vm = this

      vm.confirmLoading = true
      setTimeout(() => {
        vm.confirmLoading = false
        vm.close()
        vm.$message.success('上传头像成功')
      }, 2000)
    },

    realTime (data) {
      this.previews = data
    }
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-preview {
    position: absolute;
    top: 50%;
    transform: translate(50%, -50%);
    width: 180px;
    height: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
    }
  }
</style>
