<script setup lang="ts">
  // import { reactive, onMounted } from 'vue'
  // import axios from 'axios';
  // import { ApiEndpoints } from './../types/common.types'
  import { ComplaintFull } from './../types/common.types'

  defineProps<{
    complaint?: ComplaintFull
  }>()

  // const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : 'api/'
  // // const complaints = reactive([])

  // const data = reactive({
  //   complaints: [],
  // })

  // function getComplaints() {
  //   axios({
  //     method: 'get',
  //     url: ApiEndpoints.Complaint,
  //     baseURL: BaseURL,
  //     headers: {'Content-Type': 'application/json'},
  //     withCredentials: true
  //   }).then(response => {
  //     data.complaints = response.data
  //   })
  // }

  // onMounted(() => {
	// 	getComplaints()
	// })
</script>

<template>
  <h1>Complaint</h1>
  <!-- {{ complaint.id }} -->
  <!-- <div class="complaints-filters">
    
  </div>
  <div class="complaints complaints-head">
    <div class="email">Email</div>
    <div class="status">Статус</div>
    <div class="country">Страна</div>
    <div class="date-created">Дата создания</div>
    <div class="type">Тип обращения</div>
    <div class="date-updated">Дата изменения</div>
  </div>
  <ul v-if="data.complaints" class="complaints complaints-list">
    <li v-for="complaint in (data.complaints as ComplaintFull[])" :key="complaint.id">
      <router-link :to="`/complaint/ + ${complaint.id}`">
        <div class="email">{{ complaint.account.email }}</div>
        <div class="status">{{ complaint.state.emoji }} {{ complaint.state.nameRu }}</div>
        <div class="country">{{ complaint.problemCountry.emoji }} {{ complaint.problemCountry.nameRu }}</div>
        <div class="date-created">{{ complaint.problemDate }}</div>
        <div class="type">{{ complaint.type.emoji }} {{ complaint.type.nameRu }}</div>
        <div class="date-updated">{{ complaint.updatedAt }}</div>
      </router-link>
    </li>
  </ul> -->
</template>

<style lang="scss" scoped>
  .complaints {
    
    &-head {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;

      div {
        flex-shrink: 0;
        padding: 4px;
        border-right: 1px solid #333;

        &:last-child {
          border-right: 0;
        }
      }
    }

    &-list {
      li {
        box-shadow: 0 0 3px #333;

        a {
          display: flex;

          div {
            flex-shrink: 0;
            padding: 4px;
            border-right: 1px solid #333;

            &:last-child {
              border-right: 0;
            }
          }
        }
      }
    }
  }
</style>
