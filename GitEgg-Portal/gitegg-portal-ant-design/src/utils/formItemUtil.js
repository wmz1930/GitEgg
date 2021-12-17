export const formItemUtil = {
  INPUT_TEXT: function (item) {
      return <a-form-model-item label={item.comment} prop={item.prop}>
                <a-input
                  v-model_trim={item.model}
                  placeholder={item.comment}
                  max-length={item.maxLength}
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()}
                  />
             </a-form-model-item>
  },
  TEXTAREA: function (item) {
      return <a-form-model-item label={item.comment} prop={item.prop}>
                <a-textarea
                  v-model_trim={item.model}
                  placeholder={item.comment}
                  auto-size="{ minRows: 3, maxRows: 5 }"
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()}
                  />
            </a-form-model-item>
  },
  INPUT_NUMBER: function (item) {
      return <a-form-model-item label={item.comment} prop={item.prop}>
                <a-input-number
                  v-model_trim={item.model}
                  placeholder={item.comment}
                  min={item.min}
                  max={item.max}
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()}
                  />
              </a-form-model-item>
  },
  RADIO: function (item, dictList) {
      return <a-form-model-item label={item.comment} prop={item.prop}>
                <a-radio-group
                  v-model_trim={item.model}
                  name={item.model + 'Radio'}
                  >
                  {
                    dictList.map((item, index) => {
                      return <a-radio key={item.id + index} value={item.dictCode}>{ item.dictName }</a-radio>
                    })
                  }
                </a-radio-group>
              </a-form-model-item>
  },
  CHECKBOX: function (item, dictList) {
      return <a-form-model-item label={item.comment} prop={item.prop}>
                <a-checkbox-group
                  v-model_trim={item.model}
                  name={item.model + 'RadioCheckbox'}
                  >
                  {
                    dictList.map((item, index) => {
                      return <a-radio key={item.id + index} value={item.dictCode}>{ item.dictName }</a-radio>
                    })
                  }
                </a-checkbox-group>
              </a-form-model-item>
  },
  SELECT: function (item, dictList) {
      return <a-form-model-item label={item.comment} prop={item.prop}>
                  <a-select v-model_trim={item.model}
                          placeholder={item.comment}
                          show-search
                          mode="default"
                          filter-option="filterOption"
                          nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()} >
                  {
                    dictList.map((item, index) => {
                      return <a-select-option key={item.id + index} label={ item.dictName } value={item.dictCode}>
                              { item.dictName }
                              </a-select-option>
                    })
                  }
                </a-select>
              </a-form-model-item>
  },
  SELECT_MULTI: function (item, dictList) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
                <a-select v-model_trim={item.model}
                        placeholder={item.comment}
                        show-search
                        mode="multiple"
                        filter-option="filterOption"
                        nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()} >
                {
                  dictList.map((item, index) => {
                    return <a-select-option key={item.id + index} label={ item.dictName } value={item.dictCode}>
                            { item.dictName }
                            </a-select-option>
                  })
                }
              </a-select>
            </a-form-model-item>
  },
  DTAE_TIME_PICKER: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-date-picker v-model_trim={item.model}
                    placeholder={item.comment}
                    show-time
                    valueFormat="YYYY-MM-DD HH:mm:ss"
                    style="width:100%;"/>
            </a-form-model-item>
  },
  DTAE_PICKER: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-date-picker v-model_trim={item.model}
                    placeholder={item.comment}
                    style="width:100%;"/>
            </a-form-model-item>
  },
  PROVINCE_SELECT: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-cascader v-model_trim={item.model}
                  options="provinceOptions"
                  placeholder={'输选择' + item.comment}
                  style="width:100%;"
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()} />
            </a-form-model-item>
  },
  ORGANIZATION_TREE_SELECT: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-cascader v-model_trim={item.model}
                  options="provinceOptions"
                  placeholder={'输选择' + item.comment}
                  style="width:100%;"
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()} />
            </a-form-model-item>
  },
  ROLE_SELECT: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-cascader v-model_trim={item.model}
                  options="provinceOptions"
                  placeholder={'输选择' + item.comment}
                  style="width:100%;"
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()} />
            </a-form-model-item>
  },
  RESOURCE_TREE_SELECT: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-cascader v-model_trim={item.model}
                  options="provinceOptions"
                  placeholder={'输选择' + item.comment}
                  style="width:100%;"
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()} />
            </a-form-model-item>
  },
  MENU_TREE_SELECT: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-cascader v-model_trim={item.model}
                  options="provinceOptions"
                  placeholder={'输选择' + item.comment}
                  style="width:100%;"
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()} />
            </a-form-model-item>
  },
  RATE: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <a-rate v-model_trim={item.model}
                  placeholder={'输选择' + item.comment}
                  nativeOn-keyup={e => e.keyCode === 13 && this.handleFilter()}/>
            </a-form-model-item>
  },
  UPLOAD_FILE: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <upload-file v-model_trim={item.model} uploadBtnName="上传文件" fileNumber="1"></upload-file>
            </a-form-model-item>
  },
  UPLOAD_IMG: function (item) {
    return <a-form-model-item label={item.comment} prop={item.prop}>
              <upload-image v-model_trim={item.model} uploadBtnName="上传图片" imgNumber="1"></upload-image>
            </a-form-model-item>
  },
  getFormItem: function (fn, item, dictList) {
      return fn && fn(item, dictList)
  }
}
