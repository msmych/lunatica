<script setup lang="ts">
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-nocheck

  import { computed, reactive, onMounted } from 'vue'
  import axios from 'axios';
  import moment from 'moment';
  import { ApiEndpoints } from './../types/common.types'
  import { ComplaintFull } from './../types/common.types'
  import { useConfigStore } from './../state'

  const info = useConfigStore().info
  const complaints = useConfigStore().complaints

  const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : 'api/'
  // const complaints = reactive([])

  const data = reactive({
    complaints: [],
    complaintFilter: {
      email: '',
      problemCountry: '',
      type: '',
      problemDate: '',
      state: ''
    },
  })

  function getComplaints() {
    axios({
      method: 'get',
      url: ApiEndpoints.Complaints,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true
    }).then(response => {
      data.complaints = response.data
      useConfigStore().setComplaints(response.data)
    })
  }

  onMounted(() => {
		getComplaints()
	})

  function filter(typeName: string, type: string) {
    if (typeName === 'email') {
      if (type === 'all') {
        return data.complaints = complaints
      } else {
        return data.complaints = complaints.filter(complaint => {
          return complaint.account.email === type
        })
      }
    } else if (type === 'all') {
      return data.complaints = complaints
    } else {
      return data.complaints = complaints.filter(complaint => {
        return complaint[typeName].code === type
      })
    }
  }

  const allEmails = computed(() => {
    return [...new Set(data.complaints.map(complaint => complaint.account.email))]
  })
</script>

<template>
  <h1>Complaints</h1>

  <div class="complaints-filters">
    <h3>Фильтры:</h3>

    <select v-model="data.complaintFilter.email" class="input" @change="filter('email', data.complaintFilter.email)">
      <option value="all" key="0" selected>Email</option>
      <option v-for="(email, index) in allEmails" :key="index" :value="email">
        {{ email }}
      </option>
    </select>

    <select v-model="data.complaintFilter.state" class="input" @change="filter('state', data.complaintFilter.state)">
      <option value="all" key="0" selected>Статус</option>
      <option v-for="(state, index) in info.complaintStates" :key="index" :value="state.code">
        {{ state.emoji }} {{ state.nameRu }}
      </option>
    </select>

    <select v-model="data.complaintFilter.problemCountry" class="input" @change="filter('problemCountry', data.complaintFilter.problemCountry)">
      <option value="all" key="0" selected>Все страны</option>
      <option v-for="(country, index) in info.countries" :key="index" :value="country.code">
        {{ country.emoji }} {{ country.nameRu }}
      </option>
    </select>

    <select v-model="data.complaintFilter.type" class="input" @change="filter('type', data.complaintFilter.type)">
      <option value="all" key="0" selected>Все типы обращения</option>
      <option v-for="(type, index) in info.complaintTypes" :key="index" :value="type.code">
        {{ type.emoji }} {{ type.nameRu }}
      </option>
    </select>
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
        <div class="date-updated">{{ moment(String(complaint.updatedAt)).format('DD-MM-YYYY') }}</div>
      </router-link>
    </li>
  </ul>
</template>

<style lang="scss" scoped>
  .complaints {

    &-filters {
      margin-bottom: 16px;

      select {
        margin-right: 10px;
      }
    }
    
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
