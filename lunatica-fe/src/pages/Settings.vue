<script setup lang="ts">
  import { reactive } from 'vue'

  const data = reactive({
    pass: '',
  })

  function changePassword() {
    console.log('1111')
  }
</script>

<template>
  <h1>Settings</h1>
  <p>Change password:</p>
  <input :v-model="data.pass" />
  <button @click.prevent="changePassword">
    Change
  </button>
</template>

<style scoped>
</style>
