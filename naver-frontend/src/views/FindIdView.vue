<template>
    <div class="find-container">
        <h1 class="find-logo" @click="$router.push('/')">NAVER</h1>

        <div class="find-box">
            <div v-if="!foundId">
            <h2 class="find-title">아이디 찾기</h2>
            <p class="find-guide">전화번호나 이메일을 입력해 주세요. <span class="info-icon">ⓘ</span></p>


            <div class="input-group" :class="{ 'is-focused': isFocused }">
                <input
                        type="text"
                        v-model="inputVal"
                        @focus="isFocused = true"
                        @blur="isFocused = false"
                        placeholder="전화번호 또는 본인확인 이메일"
                        class="dark-input"
                >
            </div>

            <div class="find-options">
                <button class="btn-next" @click="handleNext">다음</button>
            </div>
            </div>

            <div v-else class="result-area">
                <h2 class="find-title">아이디 찾기 완료</h2>
                <div class="id-result-box">
                    <strong class="highlight-id">{{ foundId }}</strong>
                </div>
                <button class="btn-next" @click="$router.push('/')">로그인하러 가기</button>
            </div>

            <div class="find-footer-links">
                <span class="bottom-link">로그인 전용 아이디 해제</span>
            </div>
        </div>


        <div class="lang-select">🌐 한국어 ▾</div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const inputVal = ref('');
const isFocused = ref(false);
const foundId = ref('');    // 찾은 아이디를 담을 변수

const handleNext = async () => {
  if (!inputVal.value.trim()) {
    alert("정보를 입력해 주세요.");
    return;
  }

  try {
    const res = await axios.post('/api/user/find-id', {
        info: inputVal.value    // 사용자가 입력한 전화번호 또는 이메일
    });

  if (res.data && res.data !== "") {
    foundId.value = res.data;
  } else {
    alert("일치하는 사용자 정보가 없습니다.");
  }
  } catch (e) {
    console.error("조회 중 오류 발생:", e);
    alert("서버 통신 중 에러가 발생하였습니다.");
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