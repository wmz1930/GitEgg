<template>
  <div class="content-inline">
    <a-input v-model="uploadForm.dfsCode" type="hidden" />
    <a-upload v-model="uploadForm.uploadFile"
              list-type="picture-card"
              :file-list="fileList"
              :remove="handleRemove"
              @preview="handlePreview"
              @change="handleChange"
              :before-upload="beforeUpload">
      <div v-if="fileList.length < imgNumber">
        <a-icon type="plus" />
        <div class="ant-upload-text">
          {{ uploadBtnName }}
        </div>
      </div>
    </a-upload>
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </a-modal>
  </div>
</template>
<script>
import { queryDefaultDfs } from '@/api/system/extension/dfs/dfs'
import { dfsUpload } from '@/api/system/extension/dfs/dfs_upload'
export default {
  name: 'UploadImage',
  props: {
    value: {
      type: String,
      default: ''
    },
    uploadBtnName: {
      type: String,
      default: '上传图片'
    },
    imgType: {
      type: String,
      default: ''
    },
    imgNumber: {
      type: Number,
      default: 1
    },
    dfsType: {
      type: String,
      default: undefined
    },
    dfsCode: {
      type: String,
      default: undefined
    }
  },
  data () {
    return {
      previewVisible: false,
      previewImage: '',
      tempUrl: '',
      dfsConfig: {
        dfsType: '',
        dfsCode: ''
      },
      uploadForm: {
        dfsType: '',
        dfsCode: '',
        uploadFile: null
      },
      fileList: [],
      imgUrlList: [],
      uploading: false,
      uploadedFileName: '',
      progress: 0
    }
  },
  computed: {

  },
  watch: {
    value () {
      this.initImg(this.value)
    },
    dfsType () {
      if (this.dfsType && !this.dfsConfig.dfsType) {
        this.dfsConfig.dfsType = this.dfsType
      }
    },
    dfsCode () {
      if (this.dfsCode && !this.dfsConfig.dfsCode) {
        this.dfsConfig.dfsCode = this.dfsCode
      }
    }
  },
  created () {
    this.initImg(this.value)
  },
  methods: {
   emitInput (val) {
      // 将值回传给父组件
      this.$emit('input', val)
   },
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
    handleCancel () {
      this.previewVisible = false
    },
    handleChange (e) {
      if (e.file.status === 'removed') {
          return
      }
      const that = this
      if (!that.dfsConfig.dfsCode || that.dfsConfig.dfsCode === '') {
        that.getDefaultDfs().then(function () {
           that.handleUploadFile()
        })
      } else {
          that.handleUploadFile()
      }
    },
    handleUploadFile () {
        this.uploading = false
        this.uploadForm.dfsType = this.dfsConfig.dfsType
        this.uploadForm.dfsCode = this.dfsConfig.dfsCode
        this.uploadForm.uploadFile = null
        this.handleUpload()
    },
    handleRemove (file) {
        const index = this.fileList.indexOf(file)
        const newFileList = this.fileList.slice()
        newFileList.splice(index, 1)
        this.fileList = newFileList
        this.imgUrlList = []
        if (this.fileList && this.fileList.length > 0) {
          for (let i = 0; i < this.fileList.length; i++) {
            this.imgUrlList.push(this.fileList[i].url)
            this.emitInput(this.imgUrlList.join())
          }
        } else {
            this.emitInput('')
        }
    },
    beforeUpload (file) {
        this.fileList = [...this.fileList, file]
        return false
    },
    handleUpload () {
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
    getBase64 (file) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader()
            reader.readAsDataURL(file)
            reader.onload = () => resolve(reader.result)
            reader.onerror = error => reject(error)
        })
    },
    initImg (imgUrls) {
      if (imgUrls) {
        this.fileList = []
        this.imgUrlList = imgUrls.split(',')
        for (let i = 0; i < this.imgUrlList.length; i++) {
          this.fileList.push({ uid: i, name: 'img-' + i, status: 'done', url: this.imgUrlList[i] })
        }
      }
    }
  }
}
</script>
<style scoped>
.content-inline{
  display: inline;
}
</style>
