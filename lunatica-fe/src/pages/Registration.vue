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
    <h2>Регистрация</h2>
    <input placeholder="email" v-model="credentials.email" />
    <input placeholder="пароль" v-model="credentials.pass" />
    <button @click="registration()">Зарегестрироваться</button>
    <div class="registration-links">
      <router-link to="/login">Вход</router-link>
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .registration {
    width: 400px;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 0 3px #333;

    @media only screen and (max-width: 1024px) {
      width: 100%;
    }

    h2 {
      text-align: center;
      margin-bottom: 6px;
    }

    input {
      display: block;
      width: 100%;
      padding: 4px;
      margin-bottom: 10px;
      border-radius: 4px;
      border: 1px solid #333;
    }

    .error {
      font-size: 12px;
      color: red;
    }

    button {
      width: 100%;
      padding: 8px;
      margin-top: 10px;
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

    &-links {
      display: flex;
      width: 100%;
      justify-content: space-between;

      a {
        color: #266796;
      }
    }
  }
</style>
