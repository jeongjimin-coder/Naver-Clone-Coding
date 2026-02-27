<template>
  <div class="join-wrapper">
    <header class="join-header">
      <h1 class="logo" @click="$router.push('/')">NAVER</h1>
    </header>

    <main class="join-container">
      <div class="row-group">
        <div class="join-row" :class="{ 'is-error': idMessage && !isIdAvailable, 'is-focused': focusField === 'id' }">
          <div class="input-box icon-id">
            <input
                    v-model="joinData.userId" type="text" placeholder="아이디" class="join-input"
                    @focus="focusField = 'id'" @blur="checkIdDuplication(); focusField = ''"
            >
            <span class="input-suffix">@naver.com</span>
          </div>
        </div>

        <div class="join-row" :class="{ 'is-focused': focusField === 'pw' }">
          <div class="input-box icon-pw">
            <input
                    v-model="joinData.userPw" :type="isPwVisible ? 'text' : 'password'" placeholder="비밀번호" class="join-input"
                    @focus="focusField = 'pw'" @blur="focusField = ''"
            >
            <button type="button" class="btn-toggle-pw" @click="isPwVisible = !isPwVisible">
              <i :class="isPwVisible ? 'icon-show' : 'icon-hide'"></i>
            </button>
          </div>
        </div>

        <div class="join-row" :class="{ 'is-focused': focusField === 'email' }">
          <div class="input-box icon-mail">
            <input
                    v-model="joinData.email" type="text" placeholder="[선택] 이메일주소" class="join-input"
                    @focus="focusField = 'email'" @blur="focusField = ''"
            >
          </div>
        </div>
      </div>

      <p v-if="idMessage" class="val-msg" :class="{ 'success': isIdAvailable, 'error': !isIdAvailable }">
        · {{ idMessage }}
      </p>


      <div class="row-group">
        <div class="join-row">
          <div class="input-box icon-user">
            <input v-model="joinData.userName" type="text" placeholder="이름" class="join-input">
          </div>
        </div>
        <div class="join-row">
          <div class="input-box icon-birth">
            <input v-model="joinData.birth" type="text" placeholder="생년월일 8자리 (예: 19980101)" class="join-input">
          </div>
        </div>
        <div class="join-row">
          <div class="input-box icon-telecom">
            <select v-model="joinData.telecom" class="join-select">
              <option value="" disabled selected>통신사 선택</option>
              <option value="SKT">SKT</option>
              <option value="KT">KT</option>
              <option value="LG">LG U+</option>
              <option value="AL">알뜰폰</option>
            </select>
          </div>
        </div>
        <div class="join-row gender-nat-row">
          <div class="btn-group">
            <button type="button" :class="{active: joinData.gender==='M'}" @click="joinData.gender='M'">남자</button>
            <button type="button" :class="{active: joinData.gender==='F'}" @click="joinData.gender='F'">여자</button>
          </div>
          <div class="btn-group">
            <button type="button" :class="{active: joinData.isLocal==='L'}" @click="joinData.isLocal='L'">내국인</button>
            <button type="button" :class="{active: joinData.isLocal==='O'}" @click="joinData.isLocal='O'">외국인</button>
          </div>
        </div>
      </div>

      <div class="row-group">
        <div class="join-row">
          <div class="input-box">
            <input v-model="joinData.phone" type="text" placeholder="휴대전화번호" class="join-input">
            <button type="button" @click="handleVerifySend" :disabled="isVerified" class="btn-verify">
              {{ isVerifySent ? '재발송' : '인증요청' }}
            </button>
          </div>
        </div>
        <div v-if="isVerifySent" class="join-row">
          <div class="input-box">
            <input v-model="userInputVerifyCode" type="text" placeholder="인증번호 입력" class="join-input" :disabled="isVerified">
            <button v-if="!isVerified" type="button" @click="handleVerifyCheck" class="btn-check">확인</button>
            <span v-if="!isVerified" class="timer">{{ timeLeft }}</span>
            <span v-else class="verified-text">✅ 인증완료</span>
          </div>
        </div>
      </div>

      <button @click="handleJoin" class="submit-btn">가입하기</button>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const idMessage = ref('');
const isIdAvailable = ref(false);
const isVerified = ref(false);
const isPwVisible = ref(false);



// 1. 전체 가입 데이터 상태 관리
const joinData = ref({
  userId: '',
  userPw: '',
  userName: '',
  email: '',
  birth: '',
  telecom: '',
  gender: 'M',  // 기본값 남자
  isLocal: 'L', // 기본값 내국인
  phone: ''
});

const isVerifySent = ref(false);  // 입력창 노출 여부
const verifyCode = ref('');   // 최종 인증 성공 여부
const userInputVerifyCode = ref('');
const timeLeft = ref('03:00');

const checkIdDuplication = async () => {
  if (!joinData.value.userId) return;
  try {
    const res = await axios.get(`/api/user/check-id?userId=${joinData.value.userId}`);
    if (res.data === "available") {
      idMessage.value = "사용 가능한 아이디입니다.";
      isIdAvailable.value = true;
    } else {
      idMessage.value = "이미 사용 중인 아이디입니다.";
      isIdAvailable.value = false;
    }
  } catch (e) {
    console.error("진짜 에러: " + e);
    idMessage.value = "통신 오류가 발생했습니다.";
  }
};

// 2. 인증번호 발송 및 타이머 로직
const handleVerifySend = async () => {
  const res = await axios.post('/api/user/verify-send', { phone: joinData.value.phone });
  if (res.data === 'success') {
    isVerifySent.value = true; // 여기서 true가 되어야 아래 입력창이 나타납니다!
    alert("콘솔에서 인증번호를 확인하고 입력해 주세요.");
    startTimer();
  }
};

const handleVerifyCheck = async () => {
  const res = await axios.post('/api/user/verify-check', { code: userInputVerifyCode.value });
  if (res.data === true) {
    alert("인증되었습니다.");
    isVerified.value = true;

  } else {
    alert("인증번호가 일치하지 않습니다.");
  }
};

const startTimer = () => {
  let time = 180;
  const timer = setInterval(() => {
    const min = String(Math.floor(time / 60)).padStart(2, '0');
    const sec = String(time % 60).padStart(2, '0');
    timeLeft.value = `${min}:${sec}`;
    if (--time < 0) clearInterval(timer);
  }, 1000);
};

// 3. 최종 회원가입 제출 (Service의 join 메서드 호출)
const handleJoin = async () => {
  try {
    const res = await axios.post('/api/user/join', joinData.value);
    if(res.data === 'success') {
      alert("네이버 클론 회원이 되신 것을 축하합니다!");
      router.push('/');
    } else {
      alert("가입 실패. 입력 정보를 확인하세요.");
    }
  } catch(e) { console.error("가입 에러", e); }
};
</script>

<style scoped>
.join-wrapper { background-color: #f5f6f7; min-height: 100vh; padding-bottom: 50px; display: flex; flex-direction: column; align-items: center; }
.join-header { padding: 60px 0 20px; }
.logo { color: #03c75a; font-size: 40px; font-weight: 900; cursor: pointer; }
.join-container { width: 460px; }

.row-group { border: 1px solid #dadada; background: #fff; border-radius: 6px; overflow: hidden; margin-bottom: 10px;}
.join-row { position: relative; padding: 15px; border-bottom: 1px solid #dadada; border-bottom: 1px solid #dadada; /* 기본 구분선 */ }
.join-row:last-child { border-bottom: none; }
.join-row.is-error, .join-row.is-focused {
  border: 1px solid #ff5a5a; /* 에러 시 빨간색 */
  z-index: 10; /* 선이 위로 올라오게 하여 인접한 칸의 선을 가림 */
  margin: -1px; /* 겹친 테두리 두께를 보정 */
}
.join-row.is-focused {
  border-color: #03c75a; /* 포커스 시 초록색 */
}
.input-box { display: flex; align-items: center; }
.join-input { border: none; outline: none; flex: 1; font-size: 15px; }
.input-suffix { color: #8e8e8e; font-size: 15px; }

.join-select { border: none; outline: none; width: 100%; color: #777; font-size: 15px; }

.gender-nat-row { display: flex; gap: 10px; padding: 10px; }
.btn-group { flex: 1; display: flex; border: 1px solid #dadada; border-radius: 4px; overflow: hidden; }
.btn-group button { flex: 1; padding: 10px; border: none; background: #fff; color: #8e8e8e; cursor: pointer; }
.btn-group button.active { border: 1px solid #03c75a; color: #03c75a; background: #f0fff6; z-index: 1; }

.btn-verify { background: #03c75a; color: #fff; border: none; padding: 7px 12px; border-radius: 3px; cursor: pointer; white-space: nowrap; }
.timer { color: #ff0000; font-size: 13px; font-weight: bold; }

.submit-btn { width: 100%; padding: 15px; background: #03c75a; color: #fff; border: none; border-radius: 6px; font-size: 18px; font-weight: bold; cursor: pointer; margin-top: 20px; }
.val-msg {
  font-size: 12px;
  padding: 5px 15px;
  text-align: left;
}
.val-msg {
  font-size: 12px;
  margin-top: 8px;
  text-align: left;
  padding-left: 5px;
}
.val-msg.success { color: #03c75a; }
.val-msg.error { color: #ff5a5a; }
</style>