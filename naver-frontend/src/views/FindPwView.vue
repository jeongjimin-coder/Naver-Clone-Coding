<template>
    <div class="find-container">
        <h1 class="find-logo" @click="$router.push('/')">NAVER</h1>

        <div class="find-box">
            <div v-if="step === 1">
                <h2 class="find-title">비밀번호 찾기</h2>
                <p class="find-guide">아이디를 입력해 주세요. <span class="info-icon">ⓘ</span></p>

                <div class="input-group" :class="{ 'is-focused': focusField === 'id' }">
                    <input
                            type="text"
                            v-model="userId"
                            @focus="focusField = 'id'"
                            @blur="focusField = ''"
                            placeholder="아이디"
                            class="dark-input"
                    >
                </div>

                <div class="find-options">
                    <button class="btn-next" @click="verifyUser">다음</button>
                </div>
            </div>

            <div v-else-if="step === 2">
                <h2 class="find-title">비밀번호 재생성</h2>
                <p class="find-guide">새로운 비밀번호를 입력해 주세요.</p>

                <div class="input-group" :class="{ 'is-focused': focusField === 'pw' }">
                    <input
                            type="password"
                            v-model="newPassword"
                            @focus="focusField = 'pw'"
                            @blur="focusField = ''"
                            placeholder="비밀번호"
                            class="dark-input"
                    >
                </div>
                <div class="input-group" :class="{ 'is-focused': focusField === 'pwConfirm' }">
                    <input
                            type="password"
                            v-model="pwConfirm"
                            @focus="focusField = 'pwConfirm'"
                            @blur="focusField = ''"
                            placeholder="비밀번호"
                            class="dark-input"
                    >
                </div>
                <button class="btn-next" @click="resetPassword">비밀번호 변경</button>

            </div>

            <div class="find-footer-links">
                <span class="bottom-link">아이디가 기억나지 않나요?</span>
            </div>
        </div>


        <div class="lang-select">🌐 한국어 ▾</div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const step = ref(1);
const focusField = ref('');

const userId = ref('');
const newPassword = ref('');
const pwConfirm = ref('');

const verifyUser = async () => {
    if (!userId.value.trim()) {
        alert("아이디를 입력해 주세요.");
        return;
    }

    try {
        const res = await axios.post('/api/user/verify-id', {
            userId: userId.value
        });

        if (res.data === "exist") {
            step.value = 2; // 아이디가 있으면 2단계로
        } else {
            alert("존재하지 않는 아이디입니다.");
        }
    } catch (e) {
        alert("서버 오류가 발생했습니다.");
    }
};

const resetPassword = async () => {
    if (newPassword.value !== pwConfirm.value) {
        alert("비밀번호가 일치하지 않습니다.");
    }

    try {
        const res = await axios.post('/api/user/reset-pw', {
            userId: userId.value,
            newPw: newPassword.value
        });

        if (res.data === "success") {
            alert("비밀번호가 성공적으로 변경되었습니다.");
            router.push('/');
         }
    } catch (e) {
        alert("변경 중 오류가 발생하였습니다.");
    }
};

</script>


<style scoped>
/* 다크 모드 테마 적용 */
.find-container {
  background-color: #1a1a1a;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 80px;
  color: #fff;
  font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", sans-serif;
}

.find-logo {
  color: #03c75a;
  font-weight: 900;
  font-size: 36px;
  margin-bottom: 50px;
  cursor: pointer;
  letter-spacing: -1.5px;
}

/* 찾기 박스 스타일 */
.find-box {
  background: #222;
  border: 1px solid #333;
  border-radius: 16px;
  width: 460px;
  padding: 45px 40px;
  box-sizing: border-box;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.find-title { font-size: 26px; font-weight: bold; margin-bottom: 12px; }
.find-guide { font-size: 14px; color: #888; margin-bottom: 35px; }
.info-icon { font-size: 12px; vertical-align: middle; cursor: help; }

/* 입력창 포커스 효과 디테일 */
.input-group {
  border: 1px solid #444;
  border-radius: 8px;
  padding: 18px;
  margin-bottom: 35px;
  transition: border-color 0.2s;
}

.input-group.is-focused {
  border-color: #03c75a; /* 이미지 속 초록색 테두리 재현 */
}

.dark-input {
  background: transparent;
  border: none;
  color: #fff;
  width: 100%;
  font-size: 16px;
  outline: none;
}

/* 옵션 및 버튼 */
.find-options { display: flex; justify-content: space-between; align-items: center; }
.auth-link { color: #fff; text-decoration: underline; font-size: 14px; cursor: pointer; opacity: 0.8; }
.auth-link:hover { opacity: 1; }

.btn-next {
  background: #03c75a;
  color: #fff;
  border: none;
  padding: 14px 30px;
  border-radius: 8px;
  font-weight: bold;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-next:hover { background: #02b350; }

.id-result-box {
  background: #333;
  padding: 30px;
  border-radius: 10px;
  margin: 20px 0 40px;
  text-align: center;
}

.highlight-id {
  display: block;
  font-size: 28px;
  color: #03c75a; /* 네이버 초록색 포인트 */
  margin-top: 15px;
  letter-spacing: 1px;
}

.find-footer-links { margin-top: 120px; border-top: 1px dotted #333; padding-top: 25px; text-align: left; }
.bottom-link { font-size: 13px; color: #666; text-decoration: underline; cursor: pointer; }
.lang-select { margin-top: 40px; color: #555; font-size: 13px; cursor: pointer; }
</style>