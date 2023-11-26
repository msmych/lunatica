<script setup lang="ts">
  import { reactive, onMounted } from 'vue'
  import axios from 'axios';
  import { ApiEndpoints } from './../types/common.types'
  import { User } from './../types/common.types'

  const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : 'api/'

  const data = reactive({
    accounts: {
      admins: [],
      workers: [],
      clients: []
    }
  })

  function getAccounts(role: string) {
    axios({
      method: 'get',
      url: ApiEndpoints.Accounts,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true,
      params: {role: role}
    }).then(response => {
      data.accounts.admins = response.data
    })
  }

  // function mapRolesNames(roles) {
  //   return '123'
  // }

  onMounted(() => {
		getAccounts('ADMIN')
    // getAccounts('worker')
    // getAccounts('client')
	})
</script>

<template>
  <h1>Пользователи</h1>
  <ul v-if="data.accounts.admins" class="accounts accounts-admins">
    <li v-for="account in (data.accounts.admins as User[])" :key="account.id">
      <div class="">{{ account.email }}</div>
      <!-- <div class="">{{ mapRolesNames(account.roles) }}</div> -->
      <div class="">{{ account.roles }}</div>
    </li>
  </ul>
</template>

<style lang="scss" scoped>
  .accounts {
    li {
      box-shadow: 0 0 3px #333;
      margin-bottom: 16px;
    }
  }
</style>
