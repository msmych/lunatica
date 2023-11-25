<script setup lang="ts">
import { onMounted } from 'vue'
import axios from 'axios';

onMounted(() => {
  axios.get('http://localhost:8080/healthcheck')
});

</script>

<template>
  <p>home page</p>
</template>

<style scoped>
</style>
