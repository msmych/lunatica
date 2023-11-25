import { createApp, h } from 'vue'
import App from './App.vue'
import '@/styles/index.scss'
import router from './router'
import { createPinia } from 'pinia'
import axios from 'axios';

const app = createApp({
	render: () => h(App)
})

const pinia = createPinia()
app.use(pinia)
app.use(router)
// app.config.globalProperties.$BASE_URL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/' : ''

axios.interceptors.request.use(function (config) {
	// console.log(config)
	config.baseURL = import.meta.env.MODE === 'development' ? 'http://localhost:8080/' : ''
	return config;
}, function (error) {
	return Promise.reject(error);
});

app.mount('#app')
