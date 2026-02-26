import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // 이따가 만들 router 폴더의 index.ts를 가져옴

const app = createApp(App)
app.use(router) // 라우터 사용 선언!
app.mount('#app')

axios.defaults.withCredentials = true;