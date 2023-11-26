<script setup lang="ts">
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-nocheck

  import { reactive, onMounted } from 'vue'
  import axios from 'axios';
  import { ApiEndpoints } from './../types/common.types'
  import { ComplaintFull } from './../types/common.types'

  import { useConfigStore } from './../state'
  import ComplaintNew from './ComplaintNew.vue';

  const currentUser = useConfigStore().user
  const info = useConfigStore().info

  const BaseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/api' : 'api/'
  // const complaints = reactive([])

  console.log('BaseURL', BaseURL)
  console.log('ApiEndpoints.Complaints', ApiEndpoints.Complaints)

  const data = reactive({
    complaint: {},
    message: '',
    messages: []
  })

  function getComplaint() {
    axios({
      method: 'get',
      url: ApiEndpoints.Complaints + '/' + location.pathname.split('/').pop(-1),
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true
    }).then(response => {
      data.complaint = response.data as ComplaintFull
      getMessages()
    })
  }

  onMounted(() => {
		getComplaint()
	})

  function getMessages() {
    axios({
      method: 'get',
      url: ApiEndpoints.Messages,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true,
      params: { complaintId: location.pathname.split('/').pop(-1) }
    }).then(response => {
      data.messages = response.data
    })
  }

  function sendMessage() {
    axios({
      method: 'post',
      url: ApiEndpoints.Messages,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true,
      data: {
        complaintId: location.pathname.split('/').pop(-1),
        content: data.message
      }
    }).then(response => {
      data.messages.push({author: { email: currentUser.email }, content: data.message})
      data.message = ''
    })
  }

  function changeState() {
    axios({
      method: 'patch',
      url: ApiEndpoints.Complaints + '/' + data.complaint.id,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true,
      data: { state: data.state.code, assignedTo: null }
    }).then(response => {
      data.complaint.state = data.state
    })
  }

</script>

<template>
  <h1>Обращение</h1>

  <!-- <div class="email">{{ data.complaint.account.email }}</div> -->


  <div v-if="data.complaint?.state" class="status">Статус: {{ data.complaint.state.emoji }} {{ data.complaint.state.nameRu }}</div>
  <label>Сменить статус:</label>
  <br />
  <select v-model="data.state" class="input">
    <option v-for="(state, index) in info.complaintStates" :key="index" :value="state">
      {{ state.emoji }} {{ state.nameRu }}
    </option>
  </select>
  <br />
  <button @click.prevent="changeState(data.state)">Сменить статус</button>




  <!-- <div class="country">{{ data.complaint.problemCountry.emoji }} {{ data.complaint.problemCountry.nameRu }}</div>
  <div class="date-created">{{ data.complaint.problemDate }}</div>
  <div class="type">{{ data.complaint.type.emoji }} {{ data.complaint.type.nameRu }}</div>
  <div class="date-updated">{{ data.complaint.updatedAt }}</div> -->

  <div class="chat">
    <div class="chat-window">
      <!-- {{ data.messages }} -->
      <ul class="messages" v-if="data.messages">
        <li
          class="messages__item"
          v-for="message in data.messages"
          :key="message?.id"
        >
          <div class="name">
            {{ message?.author.email }}
          </div>
          <div class="content">
            {{ message?.content }}
          </div>
        </li>
      </ul>
    </div>
    <textarea class="textarea" v-model="data.message"></textarea>
    <br />
    <button @click.prevent="sendMessage()">Отправить</button>
  </div>
</template>

<style lang="scss" scoped>

  .status {
    margin-bottom: 8px;
  }

  label {
    margin-bottom: 8px;
  }

  select {
    margin-bottom: 8px;
  }

  button {
    margin-bottom: 8px;
  }

  .chat {
    height: 76%;

    &-window {
      margin-top: 10px;
      margin-bottom: 10px;
      width: 100%;
      height: 70%;
      border: 1px solid #333;      
      border-radius: 4px;
      overflow-y: auto;
    }

    .textarea {
      width: 100%;
    }

    .messages {
      margin: 10px;
      
      &__item {
        padding: 4px;
        margin-bottom: 10px;
        border: 1px solid #333;
        border-radius: 4px;

        .name {
          font-weight: 600;
          font-size: 12px;
          margin-bottom: 4px;
        }

        .content {
          font-size: 16px; 
        }
      }
    }
  }

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
