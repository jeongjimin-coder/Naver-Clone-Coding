import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import router from './router' // router 폴더의 index.ts를 가져옴

// main.ts
axios.defaults.withCredentials = true;

const app = createApp(App)
app.use(router) // 라우터 사용 선언!
app.mount('#app')