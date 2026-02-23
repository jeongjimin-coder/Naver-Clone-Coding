<template>
  <div class="naver-clone">
    <header class="header">
      <div class="header-inner">
        <h1 class="logo" @click="$router.push('/')">NAVER</h1>

        <div class="search-area">
          <input type="text" class="search-input" placeholder="검색어를 입력해 주세요." />
          <button class="search-btn">
            <span class="search-icon">🔍</span>
          </button>
        </div>
      </div>
    </header>

    <nav class="gnb">
      <ul class="menu-list">
        <li>메일</li><li>카페</li><li>블로그</li><li>쇼핑</li><li>뉴스</li>
      </ul>
    </nav>

    <main class="content"> <section class="left-section">
        <div class="news-stand">뉴스스탠드 영역 (준비 중)</div>
      </section>

      <aside class="right-section">
        <div v-if="isJoinMode" class="login-box join-box">
          <div class="login-inputs">
            <input v-model="joinData.userId" type="text" placeholder="아이디" class="input-field" />
            <input v-model="joinData.userPw" type="password" placeholder="비밀번호" class="input-field" />
            <input v-model="joinData.userName" type="text" placeholder="이름" class="input-field" />
            <input v-model="joinData.email" type="text" placeholder="이메일" class="input-field" />
          </div>
          <button @click="handleJoin" class="login-btn">가입하기</button>
          <div class="login-sub">
            <span @click="isJoinMode = false" style="cursor:pointer">이미 계정이 있나요? 로그인</span>
          </div>
        </div>

        <div v-else-if="!user" class="login-box before-login">
          <div class="login-inputs">
            <input v-model="userId" type="text" placeholder="아이디" class="input-field" />
            <input v-model="userPw" type="password" placeholder="비밀번호" class="input-field" />
          </div>
          <button @click="handleLogin" class="login-btn">로그인</button>
          <div class="login-sub">
            <span>아이디 찾기</span> | <span>비밀번호 찾기</span> |
            <span @click="isJoinMode = true" style="cursor:pointer">회원가입</span>
          </div>
        </div>

        <div v-else class="login-box after-login">
          <div class="user-info">
            <strong>{{ user.userName }}</strong>님 환영합니다!
            <p>{{ user.email }}</p>
          </div>
          <button @click="handleLogout" class="logout-btn">로그아웃</button>
        </div>
      </aside>
    </main> </div> </template>


<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

const user = ref(null)     // 로그인 성공 시 유저 정보 저장
const userId = ref('')     // 아이디 입력값 바인딩
const userPw = ref('')     // 비밀번호 입력값 바인딩
const isJoinMode = ref(false); // 가입 화면인지 로그인 화면인지 상태 관리

/* 세션 */

onMounted(async () => {           // 페이지가 로드되자마자 실행
  try {
    const response = await axios.get('/api/user/check-session');
    if (response.data) {
      user.value = response.data; // 서버에 세션이 있다면 정보를 받아와서 로그인 상태 유지
    }
  } catch (error) {
    console.error('세션 확인 실패:', error);
  }
  })
;

/* 3. 로그인 로직 */
const handleLogin = async () => {
  if (!userId.value || !userPw.value) {
    alert('아이디와 비밀번호를 입력해주세요.');
    return;
  }

  try {
    const response = await axios.post('/api/user/login', {
      userId: userId.value,
      userPw: userPw.value
    });

    if (response.data) {
      user.value = response.data;
      alert(`${user.value.userName}님 환영합니다!`);
    } else {
      alert('아이디 또는 비밀번호가 틀립니다.');
    }
  } catch (error) {
    console.error('로그인 에러:', error);
  }
}

/* 4. 로그아웃 로직 */
const handleLogout = async () => {
  try {
  await axios.post('/api/user/logout');
  user.value = null;
  userId.value = '';
  userPw.value = '';

  alert('로그아웃 되었습니다.');

  } catch (error) {
    console.error('로그아웃 중 오류 발생:', error);
    alert('로그아웃 실패하였습니다.');
  }
};

/* 5. 회원가입 로직 */
const joinData = ref({
  userId: '',
  userPw: '',
  userName: '',
  email: ''
});

const handleJoin = async () => {
  if (!joinData.value.userId || !joinData.value.userPw || !joinData.value.userName) {
    alert('필수 정보를 모두 입력해주세요.');
    return;
  }
  try {
    const response = await axios.post('/api/user/join', joinData.value);
    if (response.data === 'success') {
      alert('회원가입이 완료되었습니다! 이제 로그인해주세요.');
      isJoinMode.value = false; // 가입 성공 후 로그인 화면으로 복귀
    } else {
      alert('회원가입에 실패했습니다.');
    }
  } catch (error) {
    console.error('회원가입 에러:', error);
  }
};

</script>


<style scoped>
/* 1. 전체 컨테이너: 배경색과 최소 높이 설정 */
.naver-clone {
  background-color: var(--bg-body);
  min-height: 100vh;
}

/* 2. 헤더 영역: 흰색 배경에 중앙 정렬 */
.header {
  background-color: #fff;
  border-bottom: 1px solid var(--border-line);
}

.logo {
  color: #03c75a; /* 네이버 공식 그린 컬러 */
  font-size: 36px;
  font-weight: 900;
  cursor: pointer;
  letter-spacing: -1.5px;
  margin: 0;
}

.header-inner {
  max-width: 1130px; /* 네이버 표준 너비 */
  margin: 0 auto;    /* 가로 가운데 정렬 */
  display: flex;
  align-items: center;
  padding: 20px 0;
  gap: 20px;
}

/* 검색창 영역: 중앙 배치 및 스타일링 */
.search-area {
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 580px; /* 검색창 너비 확장 */
  height: 52px;
  border: 2px solid #03c75a; /* 테두리를 시그니처 컬러로 */
  border-radius: 2px;
  background-color: #fff;
  margin-left: 20px;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 0 20px;
  font-size: 18px;
  font-weight: bold;
  background: transparent;
}

.search-btn {
  width: 56px;
  height: 100%;
  background-color: #03c75a; /* 버튼 배경도 그린 */
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-icon {
  font-size: 24px;
  color: #fff; /* 돋보기 아이콘은 흰색으로 */
}

/* 3. 메뉴바(GNB) 영역 */
.gnb {
  background-color: #fff;
  border-bottom: 1px solid var(--border-line);
}

.menu-list {
  max-width: 1130px;
  margin: 0 auto;
  display: flex;
  list-style: none;
  gap: 25px;
  padding: 12px 0;
  font-weight: bold;
}

/* 4. 메인 콘텐츠 영역: 왼쪽(뉴스) + 오른쪽(로그인) */
.content {
  max-width: 1130px;
  margin: 20px auto; /* 위아래 20px 여백, 좌우 가운데 */
  display: flex;
  gap: 20px;
  align-items: flex-start; /* 자식 요소 높이가 제각각이어도 위로 정렬 */
}

/* 왼쪽 섹션 (뉴스스탠드 등) */
.left-section {
  flex: 1; /* 남은 공간 모두 차지 */
  background-color: #fff;
  border: 1px solid var(--border-line);
  min-height: 400px;
  padding: 20px;
}

/* 오른쪽 섹션 (로그인 박스 등) */
.right-section {
  width: 350px; /* 네이버 로그인 박스 표준 너비 */
  display: flex;
  flex-direction: column;
  gap: 15px;
}

/* 로그인 박스 디자인 */
.login-box {
  background-color: #fff;
  border: 1px solid var(--border-line);
  padding: 25px;
  text-align: center;
}

.input-field {
  width: 100%;
  height: 48px;
  padding: 0 12px;
  margin-bottom: 7px;
  border: 1px solid #dadada;
  box-sizing: border-box; /* 패딩 포함 너비 계산 */
  outline: none;
}

.login-btn {
  width: 100%;
  height: 48px;
  background-color: var(--naver-green);
  color: #fff;
  border: none;
  font-size: 15px;
  font-weight: bold;
  cursor: pointer;
  margin: 10px 0;
}
</style>