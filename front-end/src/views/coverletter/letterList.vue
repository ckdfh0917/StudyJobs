<template>
  <div>
    <h1 class="my-3">자소서 게시판</h1>
    <b-container class="d-flex justify-content-end">
      <b-button :to="{name:'createLetter'}" class="mx-3" variant="success">글쓰기</b-button>
      <div v-if="isLoggedIn">
        <b-button v-show="!mineCase" class="mx-3" @click="showMineCase" variant="primary">내 글 보기</b-button>
        <b-button v-show="mineCase" class="mx-3" @click="showAllCase" variant="dark">모두의 글 보기</b-button>
      </div>
      <div>
        <b-button v-show="nowCase != 0" @click="showAllCase" class="mx-3" variant="light">모든 분류 글 보기</b-button>
        <b-button v-show="nowCase != 1" @click="selectCategory(1)" class="mx-3" variant="warning">컨펌 질문 글만 보기</b-button>
        <b-button v-show="nowCase != 2" @click="selectCategory(2)" class="mx-3" variant="secondary">면접 질문 글만 보기</b-button>
      </div>
    </b-container>
    <br>
    <b-container class="my-3">
      <b-list-group>
        <b-row>
          <b-col cols="2" class="p-0"><b-list-group-item >분류</b-list-group-item></b-col>
          <b-col cols="3" class="p-0"><b-list-group-item >회사 / 직무</b-list-group-item></b-col>
          <b-col cols="7" class="p-0"><b-list-group-item >제목</b-list-group-item></b-col>
        </b-row>
        <hr>
        <b-row v-for="item in letters" :key="item.studyId">
          <b-col cols="2" class="p-0"><b-list-group-item >{{ item.category }}</b-list-group-item></b-col>
          <b-col cols="3" class="p-0"><b-list-group-item >{{ item.company }} / {{ item.job }}</b-list-group-item></b-col>
          <b-col cols="7" class="p-0 grow"><b-list-group-item route :to="{ name: 'coverLetterDetail', params: {id:item.id} }" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">{{ item.title }}</b-list-group-item></b-col>
        </b-row>
      </b-list-group>
    </b-container>
  </div>
</template>

<script>
import Axios from 'axios'
import {mapGetters,mapState} from "vuex"
const API_URL = process.env.VUE_APP_LOCAL_URL

export default {
  data () {
    return {
      letters: [],
      mineCase: false,
      saveLetters: [],
      userId: null,
      nowCase: 0,
    }
  },
  components: {
  },
  computed: {
    ...mapGetters(["isLoggedIn"]),
    ...mapState({
      email: state => state.moduleName.email,
    })
  },
  created () {
    // 자소서 전부 가져오기
    Axios({
        method: "GET",
        url: `${API_URL}resume/all`,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        },
      })
    .then(res => {
      this.letters = res.data.reverse()
      this.saveLetters = res.data
    })
    .catch(err => {
      const msg = err.response.data.msg
      if (msg == '토큰이 존재하지 않습니다.') {
        alert('로그인을 해주셔야 이용이 가능합니다')
      } else { alert(msg) }
    })
    // 유저 정보 가져오기
    if (this.isLoggedIn) {
      Axios({
        method: "GET",
        url: `${API_URL}user/${this.email}`,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        },
      })
      .then(res => {this.userId = res.data.id})
      .catch(err => {alert(err.response.data.msg)})
    }
  },
  methods: {
    showMineCase () {
      if (!this.isLoggedIn) {
        alert("로그인을 하셔야 합니다")
        return false
      }

      Axios({
        method: "GET",
        url: `${API_URL}resume/user/${this.userId}`,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        },
      })
      .then( res => {
        this.letters = res.data
        this.mineCase = true
        this.nowCase = 0
      })
      .catch( err => {alert(err.response.data.msg)})
    },
    showAllCase () {
      this.letters = this.saveLetters
      this.mineCase = false
      this.nowCase = 0
    },
    selectCategory (value) {
      var cat = 0
      if (value == 1) {
        cat = '자소서 컨펌'
      } else {cat = '자소서 면접'}
      this.letters = []
      for (var i=0; i<this.saveLetters.length; i++) {
        if (this.saveLetters[i].category == cat) {
          this.letters.push(this.saveLetters[i])
        }
      }
      this.nowCase = value
      this.mineCase = true
    },

  }
}
</script>

<style>
.grow { 
transition: all .2s ease-in-out; 
}

.grow:hover { 
transform: scale(1.05); 
}
</style>