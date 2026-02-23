<template>
  <div class="join-container">
    <h1 class="join-logo" @click="$router.push('/')">NAVER</h1>
    <div class="join-form">
      <div class="input-group">
        <label>아이디</label>
        <input v-model="joinData.userId" type="text" class="join-input">
      </div>
      <div class="input-group">
        <label>비밀번호</label>
        <input v-model="joinData.userPw" type="password" class="join-input">
      </div>
      <div class="input-group">
        <label>이름</label>
        <input v-model="joinData.userName" type="text" class="join-input">
      </div>
      <button @click="handleJoin" class="submit-btn">가입하기</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const joinData = ref({ userId: '', userPw: '', userName: '', email: '' })

const handleJoin = async () => {
  const response = await axios.post('/api/user/join', joinData.value)
  if (response.data === 'success') {
    alert('가입 성공!')
    router.push('/') // 가입 후 메인으로 이동
  }
}
</script>

<style scoped>
.join-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 60px;
  background-color: #f5f6f7;
  min-height: 100vh;
}
.join-logo { color: #03c75a; cursor: pointer; margin-bottom: 40px; }
.join-form { width: 460px; }
.join-input { width: 100%; padding: 15px; margin-bottom: 15px; border: 1px solid #dadada; }
.submit-btn { width: 100%; padding: 15px; background: #03c75a; color: white; border: none; font-weight: bold; cursor: pointer; }
</style>