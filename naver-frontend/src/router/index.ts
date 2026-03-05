import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import JoinView from '../views/JoinView.vue'
import FindIdView from '../views/FindIdView.vue'
import FindPwView from '../views/FindPwView.vue'
import MailView from '../views/MailView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/join',
      name: 'join',
      component: JoinView
    },
    {
      path: '/find-id',
      name: 'find-id',
      component: FindIdView
    },
    {
        path: '/find-pw',
        name: 'find-pw',
        component: FindPwView
    },
    {
        path: '/mail',
        name: 'mail',
        component: MailView
    }
  ]
})

export default router