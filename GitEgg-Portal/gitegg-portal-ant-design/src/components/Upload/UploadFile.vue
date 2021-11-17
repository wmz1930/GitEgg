<template>
  <div class="content-inline">
    <a-input v-model="uploadForm.dfsCode" type="hidden" />
    <a-upload v-model="uploadForm.uploadFile"
              :file-list="fileList"
              :remove="handleRemove"
              @change="handleChange"
              :before-upload="beforeUpload">
      <a-button v-if="fileList.length < fileNumber"> <a-icon type="upload" /> {{ uploadBtnName }} </a-button>
    </a-upload>
  </div>
</template>
<script>
import { queryDefaultDfs } from '@/api/system/extension/dfs/dfs'
import { dfsUpload } from '@/api/system/extension/dfs/dfs_upload'

export default {
  name: 'UploadFile',
  props: {
    value: {
      type: String,
      default: ''
    },
    uploadBtnName: {
      type: String,
      default: '选择文件'
    },
    fileType: {
      type: String,
      default: ''
    },
    fileNumber: {
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
      valueUrlList: [],
      uploading: false,
      uploadedFileName: '',
      progress: 0
    }
  },
  computed: {

  },
  watch: {
    value () {
      this.initFile(this.value)
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
    this.initFile(this.value)
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
        this.valueUrlList = []
        if (this.fileList && this.fileList.length > 0) {
          for (let i = 0; i < this.fileList.length; i++) {
            this.valueUrlList.push(this.fileList[i].url)
            this.emitInput(this.valueUrlList.join())
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
                this.valueUrlList.push(file.url)
                this.emitInput(this.valueUrlList.join())
                this.$message.success('上传成功')
            }
        }).catch(err => {
          console.log('uploading', err)
          this.$message.error('上传失败')
        })
    },
    initFile (fileUrls) {
      if (fileUrls) {
        this.fileList = []
        this.valueUrlList = fileUrls.split(',')
        for (let i = 0; i < this.valueUrlList.length; i++) {
          this.fileList.push({ uid: i, name: 'file-' + i, status: 'done', url: this.valueUrlList[i] })
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
