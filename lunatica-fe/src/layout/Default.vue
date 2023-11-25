<script setup lang="ts">
  import LeftNav from './../components/LeftNav.vue'
  import Content from './../components/Content.vue'
</script>

<template>
  <div class="layout">
    <LeftNav />
    <Content>
      <router-view />
    </Content>
  </div>
</template>

<style lang="scss" scoped>
</style>
