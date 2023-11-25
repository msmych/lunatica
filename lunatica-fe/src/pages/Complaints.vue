<script setup lang="ts">
  import { reactive, onMounted } from 'vue'
  import axios from 'axios';
  import { ApiEndpoints } from './../types/common.types'

  const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : 'api/'
  const complaints = reactive([])

  const data = reactive({
    complains: [],
  })

  function getComplaints() {
    axios({
      method: 'get',
      url: ApiEndpoints.Complaints,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true
    }).then(response => {
      data.complains = response.data
    })
  }

  onMounted(() => {
		getComplaints()
	})
</script>

<template>
  <h1>Complaints</h1>
  <ul v-if="complaints" class="dashboard-list">
    <li v-for="complaint in (complaints as Complaint[])" :key="complaint.id">
      <router-link :to="`/complain/ + ${complaint.id}`">
        <div class="name">{{ complaint.problemCountry }}</div>
        <div class="type">{{ complaint.type }}</div>
        <div class="status">{{ complaint.status }}</div>
      </router-link>
    </li>
  </ul>
</template>

<style scoped>
</style>
