<script setup lang="ts">
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-nocheck
  
  import { useConfigStore } from './../state'
  import { UserType } from './../types/common.types'

  // import Gear from './../components/icons/Gear.svg'

  const roles = Object.values(useConfigStore().user.roles)
</script>

<template>
  <div class="nav">
    <router-link class="logo" to="/">
      <img src="" alt="logo" />
    </router-link>
    <ul>
      <li>
        <!-- <Gear color="#fff" /> -->
        <router-link to="/complaint-new">Create new complaint</router-link>
      </li>
      <li>
        <!-- <Gear color="#fff" /> -->
        <router-link to="/complaints">Active complaints</router-link>
      </li>
      <li v-if="roles.includes(UserType.Admin) || roles.includes(UserType.Worker)">
        <!-- <Gear color="#fff" /> -->
        <router-link to="/complaints-completed">Completed complaints</router-link>
      </li>
      <li>
        <!-- <Gear color="#fff" /> -->
        <router-link to="/settings">Settings</router-link>
      </li>
      <li v-if="roles.includes(UserType.Admin)">
        <!-- <Gear color="#fff" /> -->
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
