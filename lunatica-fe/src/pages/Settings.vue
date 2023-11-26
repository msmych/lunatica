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
  <h1>Настройки</h1>
  <p>Сменить пароль:</p>
  <br />
  <input :v-model="data.pass" />
  <br />
  <br />
  <button @click.prevent="changePassword">
    Сменить
  </button>
</template>

<style scoped>
</style>
