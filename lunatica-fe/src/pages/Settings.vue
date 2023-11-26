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
  <div class="settings">
    <h1>Настройки</h1>
    <p>Сменить пароль:</p>
    <br />
    <input :v-model="data.pass" />
    <br />
    <br />
    <button @click.prevent="changePassword">
      Сменить
    </button>
  </div>
</template>

<style lang="scss" scoped>
.settings {
  max-width: 400px;

  input {
    padding: 6px;
    width: 100%;
  }

  button {
    width: 100%;
    padding: 10px;
    margin-bottom: 6px;
    border-radius: 4px;
    border: 0;
    background: #266796;
    color: #fff;
    transition: all .3s ease;
    cursor: pointer;

    &:hover {
      background: #1a4a6d;
      transition: all .2s ease;
    }
  }
}
</style>
