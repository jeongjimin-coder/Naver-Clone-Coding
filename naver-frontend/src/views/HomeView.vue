<template>
  <div class="naver-container">
    <header class="naver-header">
      <div class="header-inner">
        <div class="search-section">
          <h1 class="main-logo" @click="$router.push('/')">NAVER</h1>

          <div class="search-bar-container">
            <div class="search-bar">
              <input
                      type="text"
                      v-model="searchQuery"
                      @focus="isSearchHistoryOpen = true"
                      @blur="setTimeout(() => isSearchHistoryOpen = false, 200)"
                      @keyup.enter="handleSearch"
                      placeholder="검색어를 입력해 주세요."
              >
              <button class="search-btn" @click="handleSearch">🔍</button>
            </div>

            <div class="recent-search-layer" v-if="isSearchHistoryOpen && recentSearches.length > 0">
              <div class="layer-header">최근 검색어</div>
              <ul class="search-list">
                <li v-for="(word, idx) in recentSearches" :key="idx" @mousedown="handleRecentClick(word)">
                  <span>{{ word }}</span>
                  <button class="btn-delete" @mousedown.stop="deleteSearch(idx)">✕</button>
                </li>
              </ul>
            </div>
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
        <div class="banner-ad"><p>대웅제약 에너시슬 프리미엄 | 공식몰 초특가 ~68% ></p></div>
        <section class="news-stand">
          <div class="news-tab">
            <span v-for="tab in ['뉴스스탠드', '언론사편집', '엔터', '쇼핑투데이', '스포츠', '경제']"
                  :key="tab" :class="{ active: selectedCategory === tab }" @click="selectedCategory = tab">{{ tab }}</span>
          </div>
          <div class="news-grid">
            <div v-for="(news, index) in filterNewsList" :key="index" class="news-item" @click="goToSearch(news.TITLE)">
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
              <input v-model="loginPw" type="password" placeholder="비밀번호" class="input-pw" @keyup.enter="handleLogin">
            </div>
            <button @click="handleLogin" class="btn-naver-login">NAVER 로그인</button>
            <div class="login-footer">
              <span @click="$router.push('/find-id')" class="find-link">아이디 찾기</span> |
              <span @click="$router.push('/find-pw')" class="find-link">비밀번호 찾기</span> |
              <span @click="$router.push('/join')" class="join-link">회원가입</span>
            </div>
          </div>

          <div v-else class="login-after">
            <div class="profile-top">
              <div class="avatar-wrapper">
                <div class="avatar-circle">👤</div>
                <div class="icon-setting">⚙️</div>
              </div>
              <div class="profile-info">
                <p class="user-id">
                  <span class="user-name">{{ loginUser.userName }}</span>님
                  <span class="badge-plus">PLUS</span>
                </p>
                <p class="user-email">{{ loginUser.email }}</p>
              </div>
            </div>
            <div class="profile-actions">
              <div class="action-item"><strong>15</strong><span>메일</span></div>
              <div class="action-item"><strong>3</strong><span>쪽지</span></div>
              <div class="action-item"><strong>99+</strong><span>알림</span></div>
            </div>
            <button @click="handleLogout" class="btn-logout-naver">로그아웃</button>
          </div>
        </div>

        <div class="weather-box" v-if="weatherData.icon">
          <div class="weather-inner">
            <img :src="`https://openweathermap.org/img/wn/${weatherData.icon}@2x.png`" alt="날씨">
            <div class="weather-info">
              <span class="current-temp">{{ weatherData.temp }}°</span>
              <span class="weather-desc">{{ weatherData.description }}</span>
            </div>
          </div>
        </div>
      </aside>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

const recentSearches = ref([]);
const isSearchHistoryOpen = ref(false);
const searchQuery = ref('');
const isLoggedIn = ref(false);
const loginUser = ref(null);
const loginId = ref('');
const loginPw = ref('');
const newsList = ref([]);
const selectedCategory = ref('뉴스스탠드');
const weatherData = ref({ temp: 0, description: '', icon: '' });

onMounted(async () => {
  try {
    const res = await axios.get('/api/user/session');
    if (res.data) { isLoggedIn.value = true; loginUser.value = res.data; }
  } catch (e) { console.error("세션 없음"); }

  const saved = localStorage.getItem('naver_recent_searches');
  if (saved) { recentSearches.value = JSON.parse(saved); }

  try {
    const res = await axios.get('/api/news/list');
    newsList.value = res.data;
  } catch (e) { console.error("뉴스 로딩 실패"); }

  fetchWeather();
});

// 🚀 통합 검색 로직
const performSearch = (keyword) => {
  if (!keyword || !keyword.trim()) { alert("검색어를 입력해 주세요!"); return; }

  const updated = [keyword, ...recentSearches.value.filter(s => s !== keyword)];
  recentSearches.value = updated.slice(0, 5);
  localStorage.setItem('naver_recent_searches', JSON.stringify(recentSearches.value));

  searchQuery.value = keyword;
  isSearchHistoryOpen.value = false;
  window.location.href = `https://search.naver.com/search.naver?query=${encodeURIComponent(keyword)}`;
};

const handleSearch = () => performSearch(searchQuery.value);
const handleRecentClick = (word) => performSearch(word);

const deleteSearch = (idx) => {
  recentSearches.value.splice(idx, 1);
  localStorage.setItem('naver_recent_searches', JSON.stringify(recentSearches.value));
};

const goToSearch = (title) => {
  if (!title) return;
  window.open(`https://search.naver.com/search.naver?query=${encodeURIComponent(title)}`, '_blank');
};

const filterNewsList = computed(() => {
  if (selectedCategory.value === '뉴스스탠드') return newsList.value;
  return newsList.value.filter(news => news.CATEGORY === selectedCategory.value);
});

const handleLogin = async () => {
  if (!loginId.value || !loginPw.value) { alert("아이디와 비밀번호를 입력해주세요."); return; }
  try {
    const res = await axios.post('/api/user/login', { userId: loginId.value, userPw: loginPw.value });
    if (res.data) { isLoggedIn.value = true; loginUser.value = res.data; }
    else { alert("로그인 정보가 틀립니다."); }
  } catch (e) { console.error("로그인 실패"); }
};

const handleLogout = async () => {
  await axios.post('/api/user/logout');
  isLoggedIn.value = false; loginUser.value = null; loginId.value = ''; loginPw.value = '';
};

const fetchWeather = () => {
  weatherData.value = { temp: 5, description: '맑음', icon: '01d' };
};
</script>

<style scoped>
.naver-container { background-color: #f5f6f7; min-height: 100vh; color: #202020; }
.naver-header { backgroun: #fff; box-shadow: 0 1px 3px 0 rgba(0,0,0,0,06); position: sticky; top: 0; z-index: 1000; }

.header-inner { background: #fff; padding: 40px 0 20px; display: flex; flex-direction: column; align-items: center; border-bottom: 1px solid #ebebeb; }

.search-section { display: flex; align-items: center; width: 1130px; gap: 25px; margin-bottom: 30px; }
.main-logo { color: #03c75a; font-size: 38px; font-weight: 900; cursor: pointer; letter-spacing: -1px; margin: 0; }

.search-bar-container { position: relative; width: 600px; }
.search-bar { height: 58px; border: 2px solid #03c75a; border-radius: 30px; display: flex; align-items: center; padding: 0 20px; background: #fff; transition: box-shadow 0.2s; }
.search-bar:focus-within { box-shadow: 0 2px 4px 0 rgba(3,199,90,0.12); /* 포커스 시 은은한 초록 빛 */}
.search-bar input { border: none; outline: none; width: 100%; font-size: 19px; font-weight: bold; }
.search-btn { background: none; border: none; font-size: 24px; cursor: pointer; color: #03c75a; }

.recent-search-layer {
  position: absolute; top: 62px; left: 0; width: 100%; background: #fff;
  border: 1px solid #dadada; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); z-index: 1000; padding: 15px 0;
}
.layer-header { padding: 0 20px 10px; font-size: 12px; color: #888; border-bottom: 1px solid #f5f5f5; text-align: left; }
.search-list { list-style: none; padding: 0; margin: 0; }
.search-list li { display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; cursor: pointer; font-size: 14px; text-align: left;}
.search-list li:hover { background-color: #f8f9fa; }
.btn-delete { background: none; border: none; color: #ccc; cursor: pointer; }

.service-nav { display: flex; gap: 35px; }
.menu-item { display: flex; flex-direction: column; align-items: center; gap: 8px; font-size: 13px; cursor: pointer; }
.icon-circle { width: 44px; height: 44px; border-radius: 50%; background-color: #f4f7f8; }

.naver-content { display: flex; width: 1130px; margin: 20px auto; gap: 20px; align-items: flex-start; }
.left-side { width: 750px; }
.right-side { width: 350px; display: flex; flex-direction: column; gap: 15px; }

/* 광고 박스 디자인 복구 */
.banner-ad { background: #fff; border: 1px solid #dadada; padding: 20px; border-radius: 8px; margin-bottom: 15px; text-align: left; font-weight: bold; font-size: 14px; cursor: pointer; }

.news-stand { background: #fff; border: 1px solid #dadada; border-radius: 8px; overflow: hidden; }
.news-tab { padding: 15px; border-bottom: 1px solid #f0f0f0; display: flex; gap: 20px; font-size: 14px; font-weight: bold; color: #666; }
.news-tab span { cursor: pointer; padding-bottom: 5px; border-bottom: 2px solid transparent; }
.news-tab span.active { color: #000; border-bottom: 2px solid #000; }

.news-grid { display: grid; grid-template-columns: repeat(3, 1fr); border-top: 1px solid #f0f0f0; }
.news-item { height: 80px; padding: 15px; border-right: 1px solid #f0f0f0; border-bottom: 1px solid #f0f0f0; display: flex; align-items: center; cursor: pointer; }
.news-item:hover { background-color: #f8f9fa; box-shadow: inset 0 0 0 1px #03c75a; }
.press-name { font-size: 13px; font-weight: bold; color: #333; margin-bottom: 5px; display: block; }
.news-title-hover { font-size: 12px; color: #666; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

/* 로그인 박스 테두리 및 스타일 교정 */
.login-card { background: #fff; border: 1px solid #ebebeb; padding: 25px; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04);}
.login-guide { font-size: 12px; color: #666; margin-bottom: 15px; text-align: center; }
.login-inputs { margin-bottom: 10px; display: flex; flex-direction: column; gap: 5px; }
.login-inputs input { width: 100%; height: 38px; border: 1px solid #dadada; padding: 0 10px; box-sizing: border-box; font-size: 14px; border-radius: 4px; }
.btn-naver-login { width: 100%; height: 50px; background: #03c75a; color: #fff; border: none; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 16px; }
.login-footer { margin-top: 15px; font-size: 12px; color: #888; text-align: center; }
.login-footer span { cursor: pointer; transition: all 0.2s ease; position: relative; color: #888; }
.login-footer span:hover { color: #555; text-decoration: underline; }
.join-link:hover { color: #03c75a !important; font-weight: bold; }
.login-after {padding: 5px; background: #fff;}
.profile-top { display: flex; align-items: center; gap: 15px; margin-bottom: 20px; }
.avater-wrapper { position: relative; }
.avatar-circle {
  width: 56px;
  height: 56px;
  background: #f0f0f0;
  border-radius: 50%;
  border: 1px solid rgba(0,0,0,0.05);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
}

.icon-setting {
  position: absolute;
  bottom: 0;
  right: 0;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* 이름 스타일: 네이버 시그니처 폰트 느낌 */
.user-name {
  font-size: 16px;
  font-weight: 800; /* 아주 두껍게 */
  color: #000;
  letter-spacing: -0.5px;
}

.user-id {
  font-size: 14px;
  color: #333;
  margin-bottom: 2px;
}

.badge-plus {
  font-size: 10px;
  background: #03c75a;
  color: #fff;
  padding: 1px 4px;
  border-radius: 3px;
  margin-left: 5px;
  vertical-align: middle;
}

.user-email {
  font-size: 12px;
  color: #888;
  margin: 0;
}

/* 활동 수치 영역: 네이버 실무 UI 스타일 */
.profile-actions {
  display: flex;
  border-top: 1px solid #f2f2f2;
  border-bottom: 1px solid #f2f2f2;
  padding: 12px 0;
  margin-bottom: 15px;
}

.action-item {
  flex: 1;
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: #666;
  border-right: 1px solid #f2f2f2;
}

.action-item:last-child { border-right: none; }

.action-item strong {
  color: #03c75a; /* 강조 숫자는 초록색으로! */
  font-family: 'Apple SD Gothic Neo', sans-serif; /* 폰트 디테일 */
}

/* 로그아웃 버튼: 네이버 기본 버튼 스타일 */
.btn-logout-naver {
  width: 100%;
  height: 34px;
  background: #fff;
  border: 1px solid #d1d3d6;
  color: #666;
  font-size: 12px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
}

.btn-logout-naver:hover {
  background: #f8f9fa;
}

.weather-box { background: #fff; border: 1px solid #dadada; border-radius: 8px; padding: 15px 20px; display: flex; align-items: center; }
.weather-inner { display: flex; align-items: center; gap: 15px; }
.weather-inner img { width: 45px; height: 45px; background-color: #f4f7f8; border-radius: 50%; }
.current-temp { font-size: 22px; font-weight: bold; color: #03c75a; }
.weather-desc { font-size: 13px; color: #666; }
</style>