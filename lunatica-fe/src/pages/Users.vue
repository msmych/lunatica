<script setup lang="ts">
  import { reactive, onMounted } from 'vue'
  import axios from 'axios';
  import { ApiEndpoints } from './../types/common.types'

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
      data.accounts[role] = response.data
    })
  }

  onMounted(() => {
		getAccounts('admin')
    getAccounts('worker')
    getAccounts('client')
	})
</script>

<template>
  <h1>Users</h1>
  <ul v-if="data.accounts.admins" class="accounts accounts-admins">
    <li v-for="account in data.accounts" :key="account.id">
      {{ account.name }}
    </li>
  </ul>
</template>

<style scoped>
</style>
