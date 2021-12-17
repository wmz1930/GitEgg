
import store from '@/store'

export default {
  inserted (el, binding, vnode) {
    const { value } = binding

    const permissionList = store.getters && store.getters.permissions

    if (value && value instanceof Array && value.length > 0) {
      const permissions = value

      const hasAnyPerms = permissionList.some(permission => {
        return permissions.includes(permission)
      })

      if (!hasAnyPerms) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`need permissions! Like v-hasAnyPerms="['org:create','org:update']"`)
    }
  }
}
