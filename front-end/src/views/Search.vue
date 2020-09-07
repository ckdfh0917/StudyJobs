<template>
  <div class="my-3">
    <h1>{{searchData}} 검색결과</h1>
    <hr>
    <h2 class="my-2">스터디 검색 결과</h2>
    <h2 v-if="!emptyTeam">{{teamSearchResult}}</h2>
    <b-container v-else>
      <b-row>
        <TeamListItem class="col-4 my-2" v-for="team in TeamList" :key="team.studyId" v-bind:team="team"></TeamListItem>
      </b-row>
    </b-container>
    <hr>
    <h2>자소서 검색 결과</h2>
    <h2 v-if="!emptyLetters">{{letterSearchResult}}</h2>
    <b-container class="my-3" v-else>
      <b-list-group>
        <b-row>
          <b-col cols="4" class="p-0"><b-list-group-item >분류</b-list-group-item></b-col>
          <b-col cols="8" class="p-0"><b-list-group-item >제목</b-list-group-item></b-col>
        </b-row>
        <hr>
        <b-row v-for="item in letters" :key="item.studyId">
          <b-col cols="4" class="p-0"><b-list-group-item route :to="{ name: 'coverLetterDetail', params: {id:item.id} }" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">{{ item.category }}</b-list-group-item></b-col>
          <b-col cols="8" class="p-0"><b-list-group-item route :to="{ name: 'coverLetterDetail', params: {id:item.id} }" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">{{ item.title }}</b-list-group-item></b-col>
        </b-row>
      </b-list-group>
    </b-container>
  </div>
</template>

<script>
import TeamListItem from '../components/TeamlistItem.vue'
import Axios from 'axios'
const API_URL = process.env.VUE_APP_LOCAL_URL
export default {
  data () {
    return {
      searchData: this.$route.params.search,
      TeamList: [],
      letters: [],
      SearchTeam: [],
      SearchLetters: [],
      teamSearchResult: '검색중입니다',
      letterSearchResult: '검색중입니다'
    }
  },
  computed: {
    emptyTeam () {if (this.TeamList.length==0) {return false} else {return true}},
    emptyLetters () {if (this.letters.length==0) {return false} else {return true}}
  },
  components: {
    TeamListItem
  },
  created () {
    Axios.get(`${API_URL}study/all`)
    .then(res => {
      for (var i=0; i<res.data.length; i++) {
        if (res.data[i].title.indexOf(this.searchData) != -1 || res.data[i].content.indexOf(this.searchData) != -1) {
          this.TeamList.push(res.data[i])
        }
      }
      if (this.TeamList.length == 0) {this.teamSearchResult = '검색 결과가 없습니다'}
    })
    .catch(err => {
      console.log(err)
    })
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
      for (var i=0; i<res.data.length; i++) {
        if (res.data[i].title.indexOf(this.searchData) != -1) {
          this.letters.push(res.data[i])
        }
      }
      if (this.letters.length == 0) {this.letterSearchResult = '검색 결과가 없습니다'}
    })
    .catch(err => {
      const msg = err.response.data.msg
      if (msg == '토큰이 존재하지 않습니다.') {
        alert('로그인을 해주셔야 이용이 가능합니다')
      } else { alert(msg) }
    })
  }
}
</script>

<style>

</style>