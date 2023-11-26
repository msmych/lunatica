<script setup lang="ts">
  import { useConfigStore } from './../state'
  import { UserType } from './../types/common.types'

  const roles = Object.values(useConfigStore().user.roles)
</script>

<template>
  <div class="nav">
    <router-link class="logo" to="/">
      <img src="" alt="logo" />
    </router-link>
    <ul>
      <li>
        <router-link to="/complaint-new">Create new complaint</router-link>
      </li>
      <li>
        <router-link to="/complaints">Active complaints</router-link>
      </li>
      <li v-if="roles.includes(UserType.Admin) || roles.includes(UserType.Worker)">
        <router-link to="/complaints-completed">Completed complaints</router-link>
      </li>
      <li>
        <router-link to="/settings">Settings</router-link>
      </li>
      <li v-if="roles.includes(UserType.Admin)">
        <router-link to="/users">Users</router-link>
      </li>
    </ul>
  </div>
</template>

<style lang="scss" scoped>
.nav {
  display: flex;
  flex-direction: column;
  width: 80px;
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
