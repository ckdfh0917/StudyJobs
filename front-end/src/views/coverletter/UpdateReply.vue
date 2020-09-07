<template>
  <b-container class="my-3">
    <b-form-textarea
      id="textarea"
      v-model="content"
      placeholder="댓글을 작성해주세요"
      rows="3"
      max-rows="6"
    ></b-form-textarea>
    <b-button class="my-2" @click="updateReply">댓글 수정하기</b-button>
  </b-container>
</template>

<script>
import Axios from 'axios'
import { mapState } from "vuex"
const API_URL = process.env.VUE_APP_LOCAL_URL
export default {
  data () {
    return {
      content: null,
      replyId: this.$route.params.replyId,
      articleId: this.$route.params.articleId
    }
  },
  computed: {
    ...mapState({
      email: (state) => state.moduleName.email,
    })
  },
  created () {
    Axios({
      method: "GET",
      url: `${API_URL}reply/${this.replyId}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.content = res.data.content
      if (res.data.writerEmail != this.email) {
        alert("다른 사람의 댓글은 수정할 수가 없어요")
        this.$router.push({ name: "LetterList"})
      }
    })
    .catch(err => {
      alert(err.response.data.msg)
      this.$router.push({ name: "LetterList"})
    })
  },
  methods: {
    updateReply () {
      const params = {
        'content': this.content,
        'replyId': this.replyId
      }
      Axios({
        method: "PUT",
        url: `${API_URL}reply`,
        params: params,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        alert('수정되었습니다')
        this.$router.push({ name: "coverLetterDetail", params: {id: this.articleId}})
      })
      .catch(err => {
        alert(err.response.data.msg)
      })
    }
  }
}
</script>

<style>

</style>