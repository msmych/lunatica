<script setup lang="ts">
  import { reactive } from 'vue'
  import axios from 'axios';

  const credentials = reactive({
    email: '',
    pass: ''
  })

  function registration() {
    axios({
      method: 'post',
      url: 'http://localhost:8080/api/login',
      headers: {'Content-Type': 'application/json'},
      withCredentials: true,
      data: credentials
    }).then(response => {
      console.log('response', response)
    })
  }
</script>

<template>
  <div class="registration">
    <h1>Registration</h1>
    <input placeholder="email" v-model="credentials.email" />
    <input placeholder="password" v-model="credentials.pass" />
    <button @click="registration()">Login</button>
    <div class="login-links">
      <router-link to="/">login</router-link>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .registration {
    width: 400px;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 0 3px #333;

    h1 {
      text-align: center;
      margin-bottom: 6px;
    }

    input {
      display: block;
      width: 100%;
      margin-bottom: 10px;
      border-radius: 4px;
    }

    button {
      width: 100%;
      margin-top: 20px;
      margin-bottom: 6px;
      border-radius: 4px;
    }

    &-links {
      display: flex;
      width: 100%;
      justify-content: space-between;
    }
  }
</style>
