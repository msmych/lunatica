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

  const data = reactive({
    complaint: {},
    message: '',
    messages: []
  })

  function getComplaint() {
    axios({
      method: 'get',
      url: ApiEndpoints.Complaints,
      baseURL: BaseURL,
      headers: {'Content-Type': 'application/json'},
      withCredentials: true
    }).then(response => {
      data.complaint = response.data[0] as ComplaintFull
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
      params: {complaintId: data.complaint.id}
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
        complaintId: data.complaint.id,
        content: data.message
      }
    }).then(response => {
      data.messages.push({author: { email: currentUser.email }, content: data.message})
      data.message = ''
    })
  }
</script>

<template>
  <h1>Обращение</h1>

  <!-- <div class="email">{{ data.complaint.account.email }}</div>

  <div class="status">{{ data.complaint.state.emoji }} {{ data.complaint.state.nameRu }}</div> -->

  <label>Сменить статус</label>
  <select v-model="data.state" class="input" @change="changeState(data.state)">
    <option v-for="(state, index) in info.complaintStates" :key="index" :value="state.code">
      {{ state.emoji }} {{ state.nameRu }}
    </option>
  </select>



  <!-- <div class="country">{{ data.complaint.problemCountry.emoji }} {{ data.complaint.problemCountry.nameRu }}</div>
  <div class="date-created">{{ data.complaint.problemDate }}</div>
  <div class="type">{{ data.complaint.type.emoji }} {{ data.complaint.type.nameRu }}</div>
  <div class="date-updated">{{ data.complaint.updatedAt }}</div> -->

  <div class="chat">
    Чатег
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

  .chat {
    height: 90%;

    &-window {
      margin-top: 10px;
      margin-bottom: 10px;
      width: 100%;
      height: 80%;
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
