<script setup lang="ts">
  import { reactive } from 'vue'
  // import { reactive, getCurrentInstance } from 'vue'
  import axios from 'axios';
  import router from './../router'
  import { useConfigStore } from './../state'

  // const app = getCurrentInstance()
  // const BASE_URL = app.appContext.config.globalProperties.$BASE_URL

  const data = reactive({
    credentials: {
      email: '',
      pass: ''
    },
    cookie: '',
    errorMessage: ''
  })

  function login() {
    axios({
      method: 'post',
      url: 'api/login',
      baseURL: 'http://localhost:8080/',
      headers: {'Content-Type': 'application/json'},
      withCredentials: true,
      data: data.credentials
    }).then(response => {
      if (response.status === 200) {
        data.cookie = response.data
        data.errorMessage = ''
        getUserData()
        useConfigStore().setCookie(data.cookie)
      }
    }).catch(error => {
      if (error.response.status === 401) {
        data.errorMessage = 'Неправильная почта или пароль'
      }
    })
  }

  function getUserData() {
    axios({
      method: 'get',
      url: 'api/me',
      baseURL: 'http://localhost:8080/',
      withCredentials: true
    }).then(response => {
      useConfigStore().setUser({
        id: response.data.id,
        role: response.data.role,
        email: response.data.email
      })

      router.push('/home')
    })
  }
</script>

<template>
  <div class="login">
    <h1>Login</h1>
    <input placeholder="email" v-model="data.credentials.email" />
    <input placeholder="password" v-model="data.credentials.pass" />
    <p class="error" v-if="data.errorMessage">{{ data.errorMessage }}</p>
    <button @click.prevent="login()">Login</button>
    <div class="login-links">
      <router-link to="/registration">registration</router-link>
      <!-- <router-link to="/forgot">forgot password</router-link> -->
    </div>
  </div>
</template>

<style lang="scss" scoped>
  .login {
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
      padding: 4px;
      margin-bottom: 10px;
      border-radius: 4px;
    }

    .error {
      color: red;
    }

    button {
      width: 100%;
      padding: 4px;
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
