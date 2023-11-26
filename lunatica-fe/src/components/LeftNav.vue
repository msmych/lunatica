<script setup lang="ts">
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-nocheck
  
  import { useConfigStore } from './../state'
  import { UserType } from './../types/common.types'

  const roles = Object.values(useConfigStore().user.roles)
</script>

<template>
  <div class="nav">
    <router-link class="logo" to="/complaints">
      <img src="/public/logo.png" alt="logo" />
    </router-link>
    <ul>
      <li>
        <router-link to="/complaint-new">Новое обращение</router-link>
      </li>
      <li>
        <router-link to="/complaints">Обращения</router-link>
      </li>
      <li>
        <router-link to="/settings">Настройки</router-link>
      </li>
      <li v-if="roles.includes(UserType.Admin)">
        <router-link to="/users">Пользователи</router-link>
      </li>
    </ul>
  </div>
</template>

<style lang="scss" scoped>
.nav {
  display: flex;
  flex-direction: column;
  width: 100px;
  height: 100%;
  padding: 10px;
  background: #266796;

  .logo {
    margin-bottom: 20px;
  }

  ul {
    li {
      text-align: center;
      margin-bottom: 20px;
      border-bottom: 1px solid #fff;

      a {
        color: #fff;
        font-size: 14px;
      }
    }
  }
}
</style>
