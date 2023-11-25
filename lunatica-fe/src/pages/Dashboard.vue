<script setup lang="ts">
  // import { reactive } from 'vue'
  // import axios from 'axios';

  // const complains = reactive([])

  // function getComplains() {
  //   axios({
  //     method: 'get',
  //     url: 'http://localhost:8080/api/complains',
  //     headers: {'Content-Type': 'application/json'},
  //     withCredentials: true
  //   }).then(response => {
  //     console.log('response', response)
  //     complains === response.data
  //   })
  // }
</script>

<template>
  <h1>Dashboard</h1>
  <!-- <ul v-if="complains" class="dashboard-list">
    <li v-for="complain in complains" :key="complain.id">
      <router-link :to=`'/complain/' + complain.id`>
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
