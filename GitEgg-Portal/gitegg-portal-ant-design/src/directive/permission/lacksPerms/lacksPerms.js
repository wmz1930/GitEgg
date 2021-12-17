
import store from '@/store'

export default {
  inserted (el, binding, vnode) {
    const { value } = binding
    const permissionList = store.getters && store.getters.permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissions = value

      const lacksPerms = permissionList.some(permission => {
        return permissions.includes(permission)
      })

      if (lacksPerms) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`lack permissions! Like v-lacksPerms="['org:create','org:update']"`)
    }
  }
}
