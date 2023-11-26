<script setup lang="ts">
  import { reactive } from 'vue'
  // import { reactive, onMounted } from 'vue'
  import axios from 'axios';
  import router from './../router'
  import { useConfigStore } from './../state'
  import { ApiEndpoints } from './../types/common.types'

  const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : 'api/'

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
      url: ApiEndpoints.Login,
      baseURL: BaseURL,
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
      url: ApiEndpoints.Me,
      baseURL: BaseURL,
      withCredentials: true
    }).then(response => {
      useConfigStore().setUser({
        id: response.data.id,
        roles: Object.values(response.data.roles),
        email: response.data.email
      })

      getInfo()

      router.push('/complaints')
    })
  }

  function getInfo() {
    axios({
      method: 'get',
      url: ApiEndpoints.Info,
      baseURL: BaseURL,
      withCredentials: true
    }).then(response => {
      useConfigStore().setInfo({
        complaintStates: response.data.complaintStates,
        countries: response.data.countries,
        complaintTypes: response.data.complaintTypes
      })
    })
  }
</script>

<template>
  <div class="login">
    <h2>Вход</h2>
    <input placeholder="email" v-model="data.credentials.email" />
    <input placeholder="пароль" v-model="data.credentials.pass" />
    <p class="error" v-if="data.errorMessage">{{ data.errorMessage }}</p>
    <button @click.prevent="login()">Войти</button>
    <div class="login-links">
      <router-link to="/registration">Регистрация</router-link>
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
