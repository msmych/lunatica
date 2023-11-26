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
  console.log('BaseURL', BaseURL)
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
      baseURL: 'http://ec2-13-41-186-167.eu-west-2.compute.amazonaws.com/api',
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

  function changeState(complaint, value) {
    console.log(complaint, value)
  }

  const allEmails = computed(() => {
    return [...new Set(data.complaints.map(complaint => complaint.account.email))]
  })
</script>

<template>
  <h1>Обращения</h1>

  <h3>Фильтры:</h3>
  <ul class="complaints-filters">
    <li class="complaints-filters__item">
      <label>Email</label>
      <br />
      <select v-model="data.complaintFilter.email" class="input" @change="filter('email', data.complaintFilter.email)">
        <option value="all" key="0" selected>Email</option>
        <option v-for="(email, index) in allEmails" :key="index" :value="email">
          {{ email }}
        </option>
      </select>
    </li>
    <li class="complaints-filters__item">
      <label>Статус</label>
      <br />
      <select v-model="data.complaintFilter.state" class="input" @change="filter('state', data.complaintFilter.state)">
        <option value="all" key="0" selected>Статус</option>
        <option v-for="(state, index) in info.complaintStates" :key="index" :value="state.code">
          {{ state.emoji }} {{ state.nameRu }}
        </option>
      </select>
    </li>
    <li class="complaints-filters__item">
      <label>Страна</label>
      <br />
      <select v-model="data.complaintFilter.problemCountry" class="input" @change="filter('problemCountry', data.complaintFilter.problemCountry)">
        <option value="all" key="0" selected>Все страны</option>
        <option v-for="(country, index) in info.countries" :key="index" :value="country.code">
          {{ country.emoji }} {{ country.nameRu }}
        </option>
      </select>
    </li>
    <li class="complaints-filters__item">
      <label>Тип обращения</label>
      <br />
      <select v-model="data.complaintFilter.type" class="input" @change="filter('type', data.complaintFilter.type)">
        <option value="all" key="0" selected>Все типы обращения</option>
        <option v-for="(type, index) in info.complaintTypes" :key="index" :value="type.code">
          {{ type.emoji }} {{ type.nameRu }}
        </option>
      </select>
    </li>
  </ul>

  <div class="complaints-table">
    <div class="complaints complaints-head">
      <div class="go-in"></div>
      <div class="email">Email</div>
      <div class="status">Статус</div>
      <div class="country">Страна</div>
      <div class="date-created">Дата создания</div>
      <div class="type">Тип обращения</div>
      <div class="date-updated">Дата изменения</div>
    </div>

    <ul v-if="data.complaints" class="complaints complaints-list">
      <li v-for="complaint in (data.complaints as ComplaintFull[])" :key="complaint.id">
        <div class="go-in">
          <router-link :to="`/complaints/${complaint.id}`">
            Зайти в заявку
          </router-link>
        </div>
        <div class="email">
          <label>Email:</label>
          {{ complaint.account.email }}
        </div>
        <div class="status">
          <!-- <select v-model="data.complaintFilter.type" class="input" @change="changeState(complaint, value)">
            <option value="all" key="0" :selected="true">{{ complaint.state.emoji }} {{ complaint.state.nameRu }}</option>
            <option v-for="(type, index) in info.complaintStates" :key="index" :value="type.code" :selected="false">
              {{ type.emoji }} {{ type.nameRu }}
            </option>
          </select> -->
          <label>Статус:</label>
          {{ complaint.state.emoji }} {{ complaint.state.nameRu }}
        </div>
        <div class="country">
          <label>Страна:</label>
          {{ complaint.problemCountry.emoji }} {{ complaint.problemCountry.nameRu }}
        </div>
        <div class="date-created">
          <label>Дата происшествия:</label>
          {{ complaint.problemDate }}
        </div>
        <div class="type">
          <label>Тип обращения:</label>
          {{ complaint.type.emoji }} {{ complaint.type.nameRu }}
        </div>
        <div class="date-updated">
          <label>Дата изменения заявки:</label>
          {{ moment(String(complaint.updatedAt)).format('DD-MM-YYYY') }}
        </div>
      </li>
    </ul>
  </div>
</template>

<style lang="scss" scoped>
  .complaints {


    .go-in {
      width: 140px;
    }

    .email {
      width: 300px;
    }

    .status {
      width: 140px;
    }

    .country {
      width: 120px;
    }

    .date-created {
      width: 100px;
    }

    .type {
      width: 426px;
    }

    .date-updated {
      width: 100px;
    }

    &-table {
      height: 80%;
      border: 1px solid #333;
      overflow-y: auto;
    }

    &-filters {
      display: flex;
      margin-bottom: 16px;

      select {
        margin-right: 10px;
      }

      @media only screen and (max-width: 1024px) {
        display: block;

        select {
          width: 100%;
        }
      }
    }
    
    &-head {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;

      @media only screen and (max-width: 1024px) {
        display: none;
      }

      div {
        min-height: 50px;
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
        display: flex;
        border-bottom: 1px solid #333;

        div {
          flex-shrink: 0;
          padding: 4px;
          border-right: 1px solid #333;

          label {
            display: none;
          }

          &:last-child {
            border-right: 0;
          }
        }

        @media only screen and (max-width: 1024px) {
          display: block;
          margin-bottom: 16px;

          div {
            width: 100% !important;
            border: 0;
            margin-bottom: 4px;

            label {
              display: block;
            }
          }
        }
      }
    }
  }
</style>
