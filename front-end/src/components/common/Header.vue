<template>
  <div>
    <b-navbar toggleable="lg" style="background-color: rgb(25, 84, 241)" type="dark">
      <router-link to="/" class="ml-2 mr-5 text-decoration-none text-light">
        <img src="../../assets/logologo.png" style="height:40px" />StudyJobs
      </router-link>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <b-nav-item-dropdown
          id="my-nav-dropdown"
          text="스터디"
          toggle-class="nav-link-custom text-decoration-none text-light"
          right
        >
          <b-dropdown-item :to="{ name: 'StudyList' }">스터디 목록</b-dropdown-item>
          <b-dropdown-item :to="{ name: 'CreateTeam'}">스터디 만들기</b-dropdown-item>
        </b-nav-item-dropdown>
        <b-nav-item-dropdown
          id="my-nav-dropdown"
          text="채용 커뮤니티"
          toggle-class="nav-link-custom text-decoration-none text-light"
          right
        >
          <b-dropdown-item :to="{ name: 'LetterList'}">자소서 게시판</b-dropdown-item>
          <b-dropdown-item :to="{ name: 'ApplyCalander'}">채용 달력</b-dropdown-item>
        </b-nav-item-dropdown>
        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-form @submit.prevent="search">
            <b-form-input size="sm" style="width: 300px" class="mr-sm-2" placeholder="제목을 검색해보세요" v-model="searchData"></b-form-input>
            <b-button size="sm" class="my-2 my-sm-0" type="submit">검색</b-button>
          </b-nav-form>

          <!-- Using 'button-content' slot -->
          <div v-if="isLoggedIn" class="ml-3">
            <router-link class="text-light mx-1" :to="{ name: 'Logout' }">Logout</router-link>
            <router-link class="text-light ml-2" :to="{ name: 'Mypage' }">마이페이지</router-link>
          </div>
          <div v-else class="ml-3">
            <router-link class="text-light mx-1" to="/login">Login</router-link>
            <router-link class="text-light ml-2" to="/signup">Signup</router-link>
          </div>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  data() {
    return {
      searchData: null,
    };
  },
  computed: {
    ...mapGetters(["isLoggedIn"]),
  },
  methods: {
    ...mapActions(['delete_email']),
    search() {
      this.$router.push({
        name: "Search",
        params: { search: this.searchData },
      });
      this.searchData = null
    },
  },
};
</script>

<style>
li {
  list-style: none;
}
</style>