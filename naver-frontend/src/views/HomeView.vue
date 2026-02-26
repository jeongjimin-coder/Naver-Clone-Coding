<template>
  <div class="naver-container">
    <header class="naver-header">
      <div class="header-inner">
        <div class="search-section">
          <h1 class="main-logo" @click="$router.push('/')">NAVER</h1>
          <div class="search-bar">
            <input type="text" v-model="searchQuery" placeholder="검색어를 입력해 주세요.">
            <button class="search-btn">🔍</button>
          </div>
        </div>

        <nav class="service-nav">
          <div class="menu-item"><div class="icon-circle mail"></div><span>메일</span></div>
          <div class="menu-item"><div class="icon-circle cafe"></div><span>카페</span></div>
          <div class="menu-item"><div class="icon-circle blog"></div><span>블로그</span></div>
          <div class="menu-item"><div class="icon-circle store"></div><span>쇼핑</span></div>
          <div class="menu-item"><div class="icon-circle news"></div><span>뉴스</span></div>
          <div class="menu-item"><div class="icon-circle stock"></div><span>증권</span></div>
          <div class="menu-item"><div class="icon-circle map"></div><span>지도</span></div>
          <div class="menu-item"><div class="icon-circle more"></div><span>더보기</span></div>
        </nav>
      </div>
    </header>

    <main class="naver-content">
      <div class="left-side">
        <div class="banner-ad">
          <p>대웅제약 에너시슬 프리미엄 | 공식몰 초특가 ~68% ></p>
        </div>

        <section class="news-stand">
          <div class="news-tab">
          <span v-for="tab in ['뉴스스탠드', '언론사편집', '엔터', '쇼핑투데이', '스포츠', '경제']"
            :key="tab"
            :class="{ active: selectedCategory === tab }"
            @click="selectedCategory = tab"
          >
          {{ tab }}
           </span>
          </div>

          <div class="news-grid">
            <div v-for="(news, index) in newsList" :key="index" class="news-item" @click="goToSearch(news.TITLE)">
              <div class="press-logo-box">
                <span class="press-name">{{ news.PRESS_NAME }}</span>
                <p class="news-title-hover">{{ news.TITLE }}</p>
            </div>
          </div>
          </div>
        </section>
      </div>

      <aside class="right-side">
        <div class="login-card">
          <div v-if="!isLoggedIn" class="login-before">
            <p class="login-guide">네이버를 더 안전하고 편리하게 이용하세요</p>
            <div class="login-inputs">
              <input v-model="loginId" type="text" placeholder="아이디" class="input-id">
              <input v-model="loginPw" type="password" placeholder="비밀번호" class="input-pw">
            </div>
            <button @click="handleLogin" class="btn-naver-login">NAVER 로그인</button>
            <div class="login-footer">
              <span>아이디 찾기</span> | <span>비밀번호 찾기</span> | <span @click="$router.push('/join')" class="join-link">회원가입</span>
            </div>
          </div>

          <div v-else class="login-after">
            <div class="profile-area">
              <div class="avatar-circle">👤</div>
              <div class="profile-info">
                <p class="user-name"><strong>{{ loginUser.userName }}</strong>님</p>
                <p class="user-email">{{ loginUser.email }}</p>
              </div>
            </div>
            <button @click="handleLogout" class="btn-logout">로그아웃</button>
          </div>
        </div>

        <div class="side-ad">
          <div class="ad-content">
            <p class="ad-title">추운 날씨에 혼자 남겨졌던 지후</p>
            <button class="btn-more">더 알아보기 ></button>
          </div>
        </div>
      </aside>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

const isLoggedIn = ref(false);
const loginUser = ref(null);
const loginId = ref('');
const loginPw = ref('');
const searchQuery = ref('');
const newsList = ref([]);
const selectedCategory = ref('뉴스스탠드');

// 세션 체크 (새로고침 유지)
onMounted(async () => {
  try {
    const res = await axios.get('/api/user/session');
    if (res.data) {
      isLoggedIn.value = true;
      loginUser.value = res.data;
    }
  } catch (e) { console.error("세션 없음"); }
  try {
    const res = await axios.get('/api/news/list');
    newsList.value = res.data;
    console.log("뉴스 데이터: ", res.data);
    } catch (e) { console.error("뉴스 로딩 실패"); }
});

const goToSearch = (title) => {
  if (!title) return;
  const searchUrl = `https://search.naver.com/search.naver?query=${encodeURIComponent(title)}`;
  window.open(searchUrl, '_blank'); // 새 탭으로 열기
};

const filterNewsList = computed(() => {
  if (selectedCategory.value === '뉴스스탠드') return newsList.value;
  return newsList.value.filter(news => news.CATEGORY === selectedCategory.value);
});

const handleLogin = async () => {
  if (!loginId.value || !loginPw.value) { alert("입력해주세요!"); return; }
  try {
    const res = await axios.post('/api/user/login', { userId: loginId.value, userPw: loginPw.value });
    if (res.data) {
      isLoggedIn.value = true;
      loginUser.value = res.data;
    } else { alert("정보가 틀립니다."); }
  } catch (e) { console.error("로그인 실패"); }
};

const handleLogout = async () => {
  await axios.post('/api/user/logout');
  isLoggedIn.value = false;
  loginUser.value = null;
  loginId.value = '';
  loginPw.value = '';
};
</script>

<style scoped>
.naver-container { background-color: #f5f6f7; min-height: 100vh; color: #202020; }
.header-inner { background: #fff; padding: 40px 0 20px; display: flex; flex-direction: column; align-items: center; border-bottom: 1px solid #ebebeb; }

/* 검색창 디자인 (올려주신 이미지의 둥근 형태) */
.search-section { display: flex; align-items: center; width: 1130px; gap: 25px; margin-bottom: 30px; }
.main-logo { color: #03c75a; font-size: 38px; font-weight: 900; cursor: pointer; letter-spacing: -1px; }
.search-bar { flex: 1; max-width: 600px; height: 58px; border: 2px solid #03c75a; border-radius: 30px; display: flex; align-items: center; padding: 0 20px; }
.search-bar input { border: none; outline: none; width: 100%; font-size: 19px; font-weight: bold; }
.search-btn { background: none; border: none; font-size: 24px; cursor: pointer; color: #03c75a; }

/* 서비스 메뉴 아이콘 */
.service-nav { display: flex; gap: 35px; }
.menu-item { display: flex; flex-direction: column; align-items: center; gap: 8px; font-size: 13px; cursor: pointer; }
.icon-circle { width: 44px; height: 44px; border-radius: 50%; background-color: #f4f7f8; }

/* 메인 레이아웃 */
.naver-content { display: flex; width: 1130px; margin: 20px auto; gap: 20px; }
.left-side { width: 750px; }
.right-side { width: 350px; }

/* 배너 및 뉴스스탠드 */
.banner-ad { background: #fff; border: 1px solid #dadada; padding: 25px; border-radius: 8px; margin-bottom: 15px; text-align: center; font-weight: bold; }
.news-stand { background: #fff; border: 1px solid #dadada; border-radius: 8px; overflow: hidden; }
.news-tab { padding: 15px; border-bottom: 1px solid #f0f0f0; display: flex; gap: 20px; font-size: 14px; font-weight: bold; color: #666; }
.news-tab .active { color: #000; border-bottom: 2px solid #000; }
.news-grid { display: grid; grid-template-columns: repeat(3, 1fr);  border-top: 1px solid #f0f0f0;}
.news-item { height: 80px; padding: 15px; border-right: 1px solid #f0f0f0; border-bottom: 1px solid #f0f0f0; display: flex; align-items: center; justify-content: center; font-size: 12px; color: #999; transition: background-color 0.2s; cursor: pointer; transition: all 0.2 ease; }
.news-item:hover { background-color: #f8f9fa; box-shadow: inset 0 0 0 1px #03c75a; }
.press-name { font-size: 13px; font-weight: bold; color: #333; margin-bottom: 5px; }
.news-title-hover { font-size: 12px; color: #666; white-space: nowrap; overflow: hidden; text-anchor: middle; text-overflow: ellipsis; }
.news-title-hover:hover { text-decoration: underline; }


/* 로그인 박스 */
.login-card { background: #fff; border: 1px solid #dadada; padding: 25px; border-radius: 8px; margin-bottom: 15px; }
.login-guide { font-size: 13px; color: #666; margin-bottom: 15px; text-align: center; }
.login-inputs { margin-bottom: 10px; }
.login-inputs input { width: 100%; height: 35px; border: 1px solid #ddd; margin-bottom: 5px; padding: 5px; box-sizing: border-box; }
.btn-naver-login { width: 100%; height: 50px; background: #03c75a; color: #fff; border: none; border-radius: 4px; font-weight: bold; font-size: 16px; cursor: pointer; }
.login-footer { margin-top: 15px; font-size: 12px; color: #888; text-align: center; }
.join-link { color: #03c75a; font-weight: bold; cursor: pointer; }

/* 로그인 후 프로필 */
.profile-area { display: flex; align-items: center; gap: 15px; margin-bottom: 20px; }
.avatar-circle { width: 50px; height: 50px; background: #f0f0f0; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 24px; }
.user-name { margin: 0; font-size: 15px; }
.user-email { margin: 0; font-size: 12px; color: #888; }
.btn-logout { width: 100%; padding: 10px; border: 1px solid #ddd; background: #fff; cursor: pointer; border-radius: 4px; }

/* 사이드 캠페인 */
.side-ad { background: #fff; border: 1px solid #dadada; border-radius: 8px; height: 200px; padding: 20px; }
.ad-title { font-weight: bold; margin-bottom: 10px; }
.btn-more { border: none; background: none; color: #888; cursor: pointer; font-size: 12px; }
</style>