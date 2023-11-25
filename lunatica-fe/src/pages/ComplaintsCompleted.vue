<script setup lang="ts">
  // import { reactive, onMounted } from 'vue'
  // import axios from 'axios';
  // import { ApiEndpoints } from './../types/common.types'

  // const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/' : ''
  // const complains = reactive([])

  // const data = reactive({
  //   complains: [],
  // })

  // function getComplains() {
  //   axios({
  //     method: 'get',
  //     url: ApiEndpoints.Complaints,
  //     baseURL: BaseURL,
  //     headers: {'Content-Type': 'application/json'},
  //     withCredentials: true
  //   }).then(response => {
  //     data.complains = response.data
  //   })
  // }

  // onMounted(() => {
	// 	getComplains()
	// })
</script>

<template>
  <h1>Completed Complaints</h1>
  <!-- <ul v-if="complains" class="dashboard-list">
    <li v-for="complain in complains" :key="complain.id">
      <router-link :to="`/complain/ + ${complain.id}`">
        <div class="number">{{ complain.number }}</div>
        <div class="name">{{ complain.userName }}</div>
        <div class="type">{{ complain.type }}</div>
        <div class="status">{{ complain.status }}</div>
      </router-link>
    </li>
  </ul> -->
</template>

<style scoped>
</style>
