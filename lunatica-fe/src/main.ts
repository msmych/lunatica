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

const BaseURL = import.meta?.env?.MODE === 'development' ? 'http://localhost:8080/' : ''
console.log('BaseURL', BaseURL)

axios.interceptors.request.use(function (config) {
	config.baseURL = BaseURL
	return config;
}, function (error) {
	return Promise.reject(error);
});

app.mount('#app')
