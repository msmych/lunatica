import { createApp, h } from 'vue'
import App from './App.vue'
import '@/styles/index.scss'
import router from './router'
import { createPinia } from 'pinia'

const app = createApp({
	render: () => h(App)
})

const pinia = createPinia()
app.use(pinia)
app.use(router)

app.mount('#app')
