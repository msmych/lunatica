<script setup lang="ts">
  import { computed } from 'vue'
  import Auth from './layout/Auth.vue'
  import Default from './layout/Default.vue'
  import { useConfigStore } from './state'

  const isAuthorized = computed(() => {
    return useConfigStore().cookie
  })
</script>

<template>
  <div class="layout">
    <Auth v-if="!isAuthorized">
      <router-view />
    </Auth>

    <Default v-else>
      <router-view />
    </Default>
  </div>
</template>

<style lang="scss" scoped>
  .layout {
    display: flex;
    width: 100%;
    height: 100%;

    @media only screen and (max-width: 1024px) {
      flex-direction: column;
    }
  }
</style>
