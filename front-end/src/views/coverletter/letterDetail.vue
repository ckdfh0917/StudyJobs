<template>
  <b-container class="my-3" style="width: 900px;">
    <br>
    <b-row>
      <b-col md="3">
        <h4 class="border p-2">{{ category }}</h4>
      </b-col>
      <b-col  class="text-right mr-4 mt-4"><b-button router :to="{ name: 'LetterList'}" variant="success">목록</b-button></b-col>
    </b-row>
    <br>

    <b-row >
      <b-col class="ml-1">
        <h2 class="text-left"># {{ title }}</h2>
      </b-col>
    </b-row>
    <b-row>
      <b-col class="ml-1 mt-4">
        <h5 class="text-left">[{{ company }}]  |  {{ job }}</h5>
      </b-col>
      <b-col class="text-right mt-5 mr-2">작성자: {{ writer }}</b-col>
    </b-row>
    
    <br>
    <div class="resumeTabs">
      <b-card no-body>
        <b-tabs pills card vertical>
          <b-tab v-for="(item, index) in items" :key="item.id" :title="String(index+1)">
            <b-card-text>
              <b-row class="cardHeader">
                <b-col class="text-left">{{ item.title }}</b-col>
              </b-row>
              <hr>
              <b-row>
                <b-col class="text-left" style="height: 600px;" v-html="item.content"></b-col>
              </b-row>
            </b-card-text>
            <div class="d-flex justify-content-end" v-if="isWriter">
              <b-button variant="outline-warning" @click="updateQuest(item.id)" class="mx-2">항목 수정</b-button><b-button variant="outline-danger" @click="deleteQuestion(item.id)" class="mx-2">항목 삭제</b-button>
            </div>
          </b-tab>
        </b-tabs>
      </b-card>
    </div>
    <br>

    <div>
      <br>
      <div v-if="isWriter" class="d-flex justify-content-end my-3">
        <b-button @click="updateCover" variant="warning" class="mx-2">글 수정</b-button>
        <b-button @click="deleteCover" variant="danger" class="mx-2">글 삭제</b-button>
      </div>
      <b-form-textarea
        id="textarea"
        v-model="text"
        placeholder="댓글을 작성해주세요"
        rows="3"
        max-rows="6"
      ></b-form-textarea>
      <div class="my-2 d-flex justify-content-end">
        <b-button variant="primary" class="my-2" @click="createReply">댓글 제출</b-button>
      </div>
      <div v-for="reply in replies" :key="reply.id" style="border-top-width : 1px; border-top-style : dashed; border-top-color : black;">
        <b-row class="mt-3 mr-1">
          <b-col md="2" align-self="center">{{reply.writerName}}</b-col>
          <b-col md="10" class="text-left" v-html="printContent(reply.content)"></b-col>
        </b-row>
        <b-row v-if="reply.writerId == userId">
          <b-col class="text-right">
            <b-button class="m-2" @click="putReply(reply.id)" variant="outline-info">수정</b-button>
            <b-button class="m-2" @click="deleteReply(reply.id)" variant="outline-danger">삭제</b-button>
          </b-col>
        </b-row>
        <br>
      </div>
    </div>
    <b-row>
      <b-col  class="text-right mr-4 mt-4"><b-button router :to="{ name: 'LetterList'}" variant="success">목록</b-button></b-col>
    </b-row>
  </b-container>
</template>

<script>
import Axios from 'axios'
import { mapState } from "vuex"
const API_URL = process.env.VUE_APP_LOCAL_URL
export default {
  data () {
    return {
      isWriter: false,
      id: this.$route.params.id,
      title: "제목",
      writer: "작성자",
      company: null,
      job: null,
      category: null,
      items: [],
      isChanged: true,
      letterId: null,
      letterTitle: null,
      letterContent: null,
      isSelected: 0,
      userId: null,
      text: null,
      replies: [],
    }
  },
  computed: {
    ...mapState({
      email: (state) => state.moduleName.email,
    }),
  },
  created () {
    // 자소서 정보
    Axios({
      method: "GET",
      url: `${API_URL}resume/${this.id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      },
    })
    .then(res => {
      this.writer = res.data.name
      this.title = res.data.title
      this.company = res.data.company
      this.category = res.data.category
      this.job = res.data.job
      if (this.email == res.data.email) {
        this.isWriter = true
      }
    })
    // 해당 자소서의 세부 항목 가져오기
    Axios({
      method: "GET",
      url: `${API_URL}resumeitem/resume/${this.id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.items = res.data

      this.letterTitle = res.data[0].title
      this.letterId = res.data[0].id
      this.letterContent = res.data[0].content.split('\n').join('<br />')
      this.isChanged = true
      for (var i=0; i<this.items.length; i++) {
        this.items[i].content = this.items[i].content.split('\n').join('<br />')
      }
    })
    .catch(err => {
      alert(err.response.data.msg)
    })
    // 유저 id 가져오기
    Axios({
      method: "GET",
      url: `${API_URL}user/${this.email}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.userId = res.data.id
    })
    .catch(err => {
      alert(err.response.data.msg)
    })
    // 댓글 가져오기
    this.getReplies()
  },
  methods: {
    deleteCover () {
      Axios({
      method: "DELETE",
      url: `${API_URL}resume?resumeId=${this.id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      },
    })
    .then((res) => {
      alert(res.data)
      this.$router.push({ name: "LetterList"})
    })
    .catch( err => {
      alert(err.response.data)
    })
    },
    updateCover () {
      this.$router.push({ name: "UpdateCover", params: {id:this.id}})
      this.isChanged = true
    },
    deleteQuestion (id) {
      Axios({
        method: "DELETE",
        url: `${API_URL}resumeitem`,
        params: {'resumeitemId':id},
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then((res) => {
        var newQuest = []
        for (var i=0; i<this.items.length; i++) {
          if (this.items[i].id != id) {
            newQuest.push(this.items[i])
          }
        }
        this.items = newQuest
        this.letterTitle = this.items[0].title
        this.letterId = this.items[0].id
        this.letterContent = this.items[0].content.split('\n').join('<br />')
        this.isChanged = true
        alert(res.data)
      })
    },
    updateQuest (id) {
      this.isChanged = true
      this.$router.push({ name: "UpdateQuest", params: {articleId:this.id,id:id}})
    },
    changeLetter (index) {
      const item = this.items[index]
      this.letterTitle = item.title
      this.letterContent = item.content.split('\n').join('<br />')
      this.letterId = item.id
      this.isSelected = index
    },
    createReply () {
      const params = {
        'content': this.text,
        'resumeId': this.id,
        'userId': this.userId
      }
      Axios({
        method: "POST",
        url: `${API_URL}reply`,
        params: params,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        alert('댓글이 성공적으로 작성되었습니다')
        this.text = null
        this.getReplies()
      })
      .catch(err => {
        console.log(err)
      })
    },
    getReplies () {
      Axios({
        method: "GET",
        url: `${API_URL}reply/resume/${this.id}`,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(response => {
        this.replies = response.data
      })
      .catch(error => {
        alert(error.response.data.msg)
      })
    },
    deleteReply (replyId) {
      const params = {
        'replyId': replyId
      }
      Axios({
        method: "DELETE",
        url: `${API_URL}reply`,
        params: params,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        alert("성공적으로 삭제했습니다")
        this.getReplies()
      })
      .catch(err => {
        alert(err.response.data.msg)
      })
    },
    putReply (replyId) {
      this.isChanged = true
      this.$router.push({ name: "UpdateReply", params: {articleId:this.id,replyId:replyId}})
    },
    printContent (content) {
      return content.split('\n').join('<br />')
    }
  },
  beforeUpdate () {
    if (this.isChanged) {
    this.isChanged = false
    Axios({
      method: "GET",
      url: `${API_URL}resume/${this.id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      },
    })
    .then(res => {
      this.writer = res.data.name
      this.title = res.data.title
      this.company = res.data.company
      this.category = res.data.category
      if (this.email == res.data.email) {
        this.isWriter = true
      }
    })
    Axios({
      method: "GET",
      url: `${API_URL}resumeitem/resume/${this.id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.items = res.data
      for (var i=0; i<this.items.length; i++) {
        this.items[i].content = this.items[i].content.split('\n').join('<br />')
      }
    })
    .catch(err => {
      alert(err.response.data.msg)
    })
    this.getReplies()
  }
  }
}
</script>

<style>
.cardHeader {
  height: 78px;
}
</style>