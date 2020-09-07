<template>
<b-container class="my-3">
  <h1 class="my-3">항목 수정하기</h1>
  <form @submit.prevent="submitQuest">
      <b-form-input class="my-3" v-model="title" placeholder="Enter your name"></b-form-input>
      <b-form-textarea
      id="textarea-rows"
      placeholder="Tall textarea"
      rows="8"
      v-model="content"
    ></b-form-textarea>
  </form>
  <b-button @click="submitQuest" class="my-3">제출하기</b-button>
</b-container>
</template>

<script>
import Axios from 'axios'
import { mapState } from "vuex"
const API_URL = process.env.VUE_APP_LOCAL_URL
export default {
  data () {
    return {
      title: null,
      content: null,
      id: this.$route.params.id,
      articleId: this.$route.params.articleId,
      resumeId: null
    }
  },
  computed: {
    ...mapState({
      email: (state) => state.moduleName.email,
    })
  },
  methods: {
    submitQuest () {
      const params = {
        "content": this.content,
        "resumeitemId": this.id,
        "title": this.title
      }
      Axios({
        method: "PUT",
        url: `${API_URL}resumeitem`,
        params: params,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        alert("수정성공")
        this.$router.push({ name: "coverLetterDetail",params: {id:this.resumeId}})
      })
      .catch(err => {
        console.log(err)
        alert(err.response.data.msg)
      })
    }
  },
  created () {
    Axios({
      method: "GET",
      url: `${API_URL}resumeitem/email/${this.id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      },
    })
    .then(res => {
      if (res.data != this.email) {
        alert("작성자가 아닙니다")
        this.$router.push({ name: "LetterList"})
      }
    })
    .catch(err => {
      console.log(err)
      alert(err.response.data.msg)
    })

    Axios({
      method: "GET",
      url: `${API_URL}resumeitem/${this.id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      },
    })
    .then(res => {
      this.title = res.data.title
      this.content = res.data.content
      this.resumeId = res.data.resumeId
    })
  }
}
</script>

<style>

</style>