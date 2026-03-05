<template>
    <div class="mail-wrapper">
        <nav class="mail-sidebar">
            <button class="btn-write">메일쓰기</button>
            <ul class="menu-list">
                <li class="active">받은메일함 <span>15</span></li>
                <li>보낸메일함</li>
                <li>내게쓴메일함</li>
                <li>휴지통</li>
            </ul>
        </nav>

        <main class="mail-content">
            <div class="mail-toolbar">
                <input type="checkbox"> <button>삭제</button> <button>읽음</button>
            </div>
            <ul class="mail-list">
                <li v-for="i in 3" :key="i" class="mail-item">
                    <input type="checkbox">
                    <span class="sender">네이버 보안팀</span>
                    <span class="title">새로운 환경에서 로그인되었습니다.</span>
                    <span class="date">03.05</span>
                </li>
            </ul>
        </main>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import axios from 'axios';

onMounted(() => {
    console.log("1. 메일 요청을 시작합니다!");
    axios.get('/mail/list')
        .then(res => console.log("값이 뭘까요?", res.data))

        .catch(err => {
            console.error("인터셉터에 의해 거부됨!");
            window.location.href = '/login';
        });
});

</script>
<style scoped>
.mail-wrapper { display: flex; height: 100vh; background: #f5f6f7; }
.mail-sidebar { width: 250px; background: #fff; padding: 20px; border-right: 1px solid #ececec; }
.btn-write { width: 100%; padding: 10px; background: #03c75a; color: #fff; border: none; border-radius: 6px; cursor: pointer; margin-bottom: 20px; }
.menu-list { list-style: none; padding: 0; }
.menu-list li { padding: 10px; cursor: pointer; font-size: 14px; }
.menu-list li.active { font-weight: bold; color: #03c75a; }
.mail-content { flex: 1; padding: 20px; }
.mail-list { background: #fff; border: 1px solid #ececec; border-radius: 6px; list-style: none; padding: 0; }
.mail-item { display: flex; align-items: center; padding: 15px; border-bottom: 1px solid #f0f0f0; gap: 15px; }
.sender { width: 120px; font-weight: bold; }
.title { flex: 1; }
</style>