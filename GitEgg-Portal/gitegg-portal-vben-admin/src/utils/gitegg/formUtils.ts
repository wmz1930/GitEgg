import { h } from 'vue';
import { Tag, Switch } from 'ant-design-vue';
import { isEmpty } from '/@/utils/is';
import { getDictCacheOnly, getDictBusinessCacheOnly, Dict } from '/@/utils/gitegg/dictUtils';
import { useMessage } from '/@/hooks/web/useMessage';
import { Rule } from '/@//components/Form';

// 渲染字典数据
export function renderDict(value, code, dictBusiness = true, renderTag = false, tagColorCode = '') {
  let text = value;
  let color = '';
  let dict = { dictCode: code } as Dict;
  let dictColor = { dictCode: tagColorCode === '' ? code + '_TAG_COLOR' : tagColorCode } as Dict;
  if (dictBusiness) {
    dict = getDictBusinessCacheOnly(dict);
    if (renderTag) {
      dictColor = getDictBusinessCacheOnly(dictColor);
    }
  } else {
    dict = getDictCacheOnly(dict);
    if (renderTag) {
      dictColor = getDictCacheOnly(dictColor);
    }
  }

  if (!isEmpty(value) && dict.filterMap && dict.filterMap[value]) {
    text = dict.filterMap[value];
  }

  if (!isEmpty(value) && dictColor.filterMap && dictColor.filterMap[value]) {
    color = dictColor.filterMap[value];
  }
  return isEmpty(text) || !renderTag ? h('span', text) : h(Tag, { color: color }, () => text);
}

// 渲染状态数据
export function renderStatusSwitch(record, updateStatusApi, statusField = '', statusText = '') {
  if (!Reflect.has(record, 'pendingStatus')) {
    record.pendingStatus = false;
  }
  const field = statusField === '' ? 'status' : statusField;
  const text = statusText === '' ? '' : statusText;

  return h(Switch, {
    checked: Number(Reflect.get(record, field)) === 1,
    checkedChildren: '已启用',
    unCheckedChildren: '已禁用',
    loading: record.pendingStatus,
    onChange(checked: boolean) {
      record.pendingStatus = true;
      const newStatus = checked ? 1 : 0;
      const { createMessage, createConfirm } = useMessage();
      createConfirm({
        iconType: 'warning',
        title: () => h('span', '操作提示'),
        content: () => h('span', newStatus === 1 ? `确定要启用${text}？` : `确定要禁用${text}？`),
        onOk: async () => {
          await updateStatusApi(record.id, newStatus)
            .then(() => {
              Reflect.set(record, field, newStatus);
              createMessage.success(newStatus === 1 ? `已成功启用${text}` : `已成功禁用${text}`);
            })
            .catch(() => {
              record.pendingStatus = false;
              createMessage.error(`修改${text}状态失败`);
            })
            .finally(() => {
              record.pendingStatus = false;
            });
        },
        onCancel: async () => {
          record.pendingStatus = false;
        },
      });
    },
  });
}

// 渲染是否唯一检查
export function renderCheckExistRules(model, checkExistApi, field = '', text = ''): Rule[] {
  return [
    {
      required: true,
      message: `请输入${text}`,
    },
    {
      message: `${text}已存在`,
      validator: (_, value) => {
        return new Promise((resolve, reject) => {
          const keyData = {
            id: model.id,
            checkField: field,
            checkValue: value,
          };
          checkExistApi(keyData)
            .then((response) => {
              if (!response) {
                reject(`${text}已存在`);
              } else {
                resolve();
              }
            })
            .catch((err) => {
              reject(err.message || '验证失败');
            });
        });
      },
    },
  ];
}
