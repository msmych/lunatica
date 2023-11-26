<script setup lang="ts">
</script>

<template>
  <div class="content">
    <slot />
  </div>
</template>

<style lang="scss" scoped>
.content {
  height: 100%;
  width: 100%;
  padding: 40px;
  overflow: auto;

  :deep(h1) {
    margin-bottom: 20px;
  }
}
</style>
