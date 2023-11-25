<script setup lang="ts">
</script>

<template>
  <div class="nav">
    <a class="logo">
      <img sr="" alt="logo" />
    </a>
    <ul>
      <li>
        <router-link to="/active">Active complains</router-link>
      </li>
      <li>
        <router-link to="/completed">Completed complains</router-link>
      </li>
      <li>
        <router-link to="/settings">Settings</router-link>
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
      margin-bottom: 10px;

      a {
        color: #fff;
        font-size: 14px;
      }
    }
  }
}
</style>
