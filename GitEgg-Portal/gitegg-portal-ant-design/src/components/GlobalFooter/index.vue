<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://cloud.gitegg.com" target="_blank">GitEgg Portal</a>
      <a href="https://github.com/wmz1930/GitEgg" target="_blank">Github</a>
      <a href="https://github.com/wmz1930" target="_blank">@GitEgg</a>
    </template>
    <template v-slot:copyright>
      <a href="https://cloud.gitegg.com" target="_blank">GitEgg</a>
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
