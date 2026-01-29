<template>
  <div class="naver-clone">
    <header class="header">
      <div class="header-inner">
        <h1 class="logo">NAVER</h1>
        <div class="search-area">
          <input type="text" class="search-input" placeholder="검색어를 입력해 주세요." />
          <button class="search-btn">🔍</button>
        </div>
      </div>
    </header>

    <nav class="gnb">
      <ul class="menu-list">
        <li>메일</li><li>카페</li><li>블로그</li><li>쇼핑</li><li>뉴스</li>
      </ul>
    </nav>

<main class="content">
      <section class="left-section">
        <div class="news-stand">뉴스스탠드 영역 (준비 중)</div>
      </section>

      <aside class="right-section">
        <div v-if="!user" class="login-box before-login">
          <div class="login-inputs">
            <input v-model="userId" type="text" placeholder="아이디" class="input-field" />
            <input v-model="userPw" type="password" placeholder="비밀번호" class="input-field" />
          </div>
          <button @click="handleLogin" class="login-btn">로그인</button>
          <div class="login-sub">
            <span>아이디 찾기</span> | <span>비밀번호 찾기</span> | <span>회원가입</span>
          </div>
        </div>

        <div v-else class="login-box after-login">
          <div class="user-info">
            <strong>{{ user.userName }}</strong>님 환영합니다!
            <p>{{ user.email }}</p>
          </div>
          <button @click="handleLogout" class="logout-btn">로그아웃</button>
        </div>
      </aside></main>
  </div>
</template>


<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

const user = ref(null)     // 로그인 성공 시 유저 정보 저장
const userId = ref('')     // 아이디 입력값 바인딩
const userPw = ref('')     // 비밀번호 입력값 바인딩

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
const handleLogout = () => {
  user.value = null;
  userId.value = '';
  userPw.value = '';
};
</script>


<style scoped>
/* 헤더 & 메뉴바 영역 */
.header, .gnb {
  background-color: var(--bg-card);
  border-bottom: 1px solid var(--border-line);
}

.logo { color: var(--naver-green); font-weight: bold; font-size:30px; flex-shrink: 0;}

.menu-list { display: flex; list-style: none; gap: 20px; padding: 10px 0; }
.menu-list li {
  color: var(--color-sub); /* 라이트일 땐 회색, 다크일 땐 흰색에 가깝게 */
  font-weight: bold;
}

/* 검색창 영역 (이미지에서 가장 어색했던 부분) */
.search-area {
  flex: 1;
  margin: 0 auto;
  width: 100%;
  max-width: 600px;
  border: 2px solid var(--naver-green);
  background-color: var(--bg-card); /* 박스 배경색 따라가기 */
  border-radius: 2px;
}
.search-input {
  border: none;
  padding: 13px;
  flex: 1;
  outline: none;
  background-color: transparent; /* 배경을 투명하게 해서 부모 색상 노출 */
  color: var(--color-main);       /* 글자색이 배경에 맞춰 자동 조절됨 */
}
.search-btn { background: var(--naver-green); border: none; padding: 0 15px; cursor: pointer; }

/* 메인 콘텐츠 영역 */
.content { display: flex; gap: 20px; max-width: 1130px; margin: 20px auto; }    /* 자식 요소들을 가로로 나열 */

.left-section, .login-box {
  background-color: var(--bg-card);
  border: 1px solid var(--border-line);
  padding: 20px;
  color: var(--color-main);
}

.header-inner, .inner {
  width: 90%;          /* 화면 가로의 90% 사용 */
  max-width: 1400px;   /* 너무 무한정 커지는 것 방지 (네이버보다 조금 더 넓게 설정) */
  margin: 0 auto;      /* 가운데 정렬 */
  display: flex;
  align-items: center;
  padding: 20px 0;
  gap: 10px; /* 로고와 검색창 사이의 간격을 직접 조절 */
}

/* 2. 메인 콘텐츠 영역 너비 조절 */
.content {
  width: 90%;          /* 화면 가로의 90% 사용 */
  max-width: 1400px;
  margin: 20px auto;   /* 위아래 여백 20px, 좌우 가운데 정렬 */
  display: flex;
  gap: 20px;           /* 왼쪽 콘텐츠와 오른쪽 사이드바 간격 */
}

/* 3. 왼쪽과 오른쪽 비율 조정 */
.left-section {
  flex: 3;             /* 왼쪽(뉴스 등)이 3만큼 차지 */
}

.right-section {
  flex: 1;             /* 오른쪽(로그인 등)이 1만큼 차지 */
  min-width: 300px;    /* 로그인 박스가 너무 작아지지 않게 최소 너비 고정 */
}

.login-box {
  background-color: var(--bg-card);
  border: 1px solid var(--border-line);
  padding: 20px;
  text-align: center;
}

.login-msg {
  font-size: 13px;
  margin-bottom: 15px;
  color: var(--color-sub);
}

.login-btn {
  width: 100%;
  padding: 15px;
  background-color: var(--naver-green);
  color: white;
  border: none;
  font-weight: bold;
  font-size: 16px;
  cursor: pointer;
  border-radius: 4px;
}

.login-sub {
  margin-top: 15px;
  font-size: 12px;
  color: var(--color-sub);
}

.logout-btn {
  margin-top: 10px;
  width: 100%;
  padding: 8px;
  background: var(--bg-body);
  border: 1px solid var(--border-line);
  cursor: pointer;
  color: var(--color-main);
}

</style>