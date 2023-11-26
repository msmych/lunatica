<script setup lang="ts">
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-nocheck

  import axios from 'axios';
  import router from './../router'
  import { reactive, onMounted } from 'vue'
  import { ApiEndpoints, Country, ComplaintType } from './../types/common.types'
  import { useConfigStore } from './../state'

  const info = useConfigStore().info

  const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : 'api/'

  const data = reactive({
    complaint: {
      problemCountry: '',
      type: '',
      problemDate: '',
      content: ''
    },
    success: false,
    errorMessage: ''
  })

  function createComplaint() {
    axios({
      method: 'post',
      url: ApiEndpoints.Complaints,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true,
      data: data.complaint
    }).then(response => {
      if (response.status === 201) {
        data.success = true
        router.push(`/complaints/${response.data.id}`)
      }
    }).catch(error => {
      if (error.response.status === 401) {
        data.errorMessage = error
      }
    })
  }
</script>

<template>
  <h1>Новое обращение</h1>
  <select v-model="data.complaint.problemCountry" class="input">
    <option value="" selected disabled hidden>Выберите страну</option>
    <option v-for="(country, index) in (info.countries as Country)" :key="index" :value="country.code">
      {{ country.emoji }} {{ country.nameRu }}
    </option>
  </select>
  <select v-model="data.complaint.type" class="input">
    <option value="" selected disabled hidden>Выберите тип обращения</option>
    <option v-for="(type, index) in (info.complaintTypes as ComplaintType)" :key="index" :value="type.code">
      {{ type.emoji }} {{ type.nameRu }}
    </option>
  </select>
  <input class="input" type="date" v-model="data.complaint.problemDate" />
  <textarea v-model="data.complaint.content" class="input" placeholder="Текст обращения" />
  <button @click.prevent="createComplaint">Создать</button>
</template>

<style lang="scss" scoped>

textarea {
  min-height: 100px;
}

.input {
  display: block;
  width: 100%;
  margin-bottom: 16px;
  padding: 4px;
}

button {
  width: 100%;
  padding: 10px;
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
</style>