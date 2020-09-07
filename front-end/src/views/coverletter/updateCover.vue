<template>
  <div class="my-3">
    
    <b-container>
      <br>
      <h1>자소서 수정</h1>
      <br>
      <b-row class="my-3">
        <b-col md="4" class="text-left">
          <b-form-select v-model="category" :options="options" required></b-form-select>
        </b-col>
        <b-col class="text-right">
          <b-button variant="primary" @click="submitCoverLetter">제출하기</b-button>
        </b-col>
      </b-row>
      <!-- <b-row>
        <b-col class="text-left"><p>[자소서 컨펌] 피드백 받고 싶은 자기소개서를 등록하세요</p></b-col>
      </b-row>
      <b-row>
        <b-col class="text-left"><p>[자소서 면접] 자기소개서 기반 면접 질문이 궁금하면 등록하세요</p></b-col>
      </b-row> -->
      <b-row class="my-3">
        <b-col class="pr-0">
          <b-form-input v-model="company" placeholder="회사를 입력해주세요"></b-form-input>
        </b-col>
        <b-col>
          <b-form-input v-model="job" placeholder="직무를 입력해주세요"></b-form-input>
        </b-col>
      </b-row>
      <b-row class="my-3">
        <b-col>
          <b-form-input v-model="title" placeholder="글 제목을 입력해주세요"></b-form-input>
        </b-col>
      </b-row>
      <!-- 제목까지 -->

      <div>
        <b-card no-body>
          <b-tabs card>
            <!-- Render Tabs, supply a unique `key` to each tab -->
            <b-tab v-for="item in items" :key="item.num" :title="`${item.num} 번`">
              <div :id="'my-'+item.num" class="my-3">
                <b-form-textarea 
                  v-model="item.title" 
                  placeholder="질문을 입력하세요" 
                  rows="5"
                  max-rows="9"
                  class="pt-3"
                ></b-form-textarea>
                <b-form-textarea
                  v-model="item.answer"
                  placeholder="답변을 입력하세요"
                  rows="19"
                  max-rows="30"
                  class="pt-3"
                ></b-form-textarea>
              </div>



              <b-button size="sm" variant="danger" class="float-right" @click="delQuestion(item.num)">
                Close tab
              </b-button>
              <br>
            </b-tab>

            <!-- New Tab Button (Using tabs-end slot) -->
            <template v-slot:tabs-end>
              <b-nav-item role="presentation" @click.prevent="addQuestion" href="#"><b>+</b></b-nav-item>
            </template>

            <!-- Render this if no tabs -->
            <template v-slot:empty>
              <div class="text-center text-muted">
                There are no open tabs<br>
                Open a new tab using the <b>+</b> button above.
              </div>
            </template>
          </b-tabs>
        </b-card>
      </div>
      <br>
      <b-row>
        <b-col class="text-right">
          <b-button variant="primary" @click="submitCoverLetter">제출하기</b-button>
        </b-col>
      </b-row>
    </b-container>

  </div>
</template>

<script>
import Axios from 'axios'
import { mapState } from "vuex"
const API_URL = process.env.VUE_APP_LOCAL_URL
export default {
  data () {
    return {
      id: this.$route.params.id,
      items: [],
      question:1,
      title: null,
      company: null,
      job: null,
      category: '자소서 컨펌',
      options: [
        { value: null, text: '항목을 선택해주세요' },
        { value: '자소서 컨펌', text: '자소서 컨펌'},
        { value: '자소서 면접', text: '자소서 면접'}
      ],
      isWritter: false,
      delItems: []
    }
  },
  computed: {
    ...mapState({
      email: (state) => state.moduleName.email,
    })
  },
  methods: {
    addQuestion () {
      var cover = new Object
      const numCover = this.question
      cover = {num: numCover, title: null, answer: null, id: null}
      this.question += 1
      this.items.push(cover)
    },
    delQuestion (num) {
      this.question -= 1
      var afterItem = []
      for (var i=0; i<this.items.length; i++) {
        if (this.items[i].num < num) {
          afterItem.push(this.items[i])
        } else if (this.items[i].num > num) {
          afterItem.push({'num':this.items[i].num-1,'title':this.items[i].title,'answer':this.items[i].answer})
        } else {
          if (this.items[i].id != null ) {
            this.delItems.push(this.items[i].id)
          }
        }
      }
      this.items = afterItem
    },
    submitCoverLetter () {
      if (this.category == null) {
        alert('카테고리를 선택해주세요')
        return false
      }
      const params = {
        'category': this.category,
        'company': this.company,
        'job': this.job,
        'resumeId': this.id,
        'title': this.title
      }
      Axios({
        method: "PUT",
        url: `${API_URL}resume`,
        params: params,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then((res) => {
        for (var j=0; j<this.delItems.length; j++) {
          this.deleteQuest(this.delItems[j])
        }
        for (var i=0; i<this.items.length; i++) {
          if (this.items[i].id == null) {
            this.submitQuestion(this.id,this.items[i])
          } else {
            this.updateQuestion(this.items[i])
          }
        }
        alert(res.data)
        this.$router.push({ name: "coverLetterDetail", params: {id: this.id}})
      })
      .catch(err => {
        console.log(err)
      })
    },
    submitQuestion (resumeId,quest) {
      const params = {
        'content': quest.answer,
        'resumeId': resumeId,
        'title': quest.title
      }
      Axios({
        method: "POST",
        url: `${API_URL}resumeitem`,
        params: params,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(() => {
      })
      .catch(err => {
        alert(err.response.data.msg)
      })
    },
    updateQuestion (quest) {
      const params = {
        'content': quest.answer,
        'resumeitemId': quest.id,
        'title': quest.title
      }
      Axios({
        method: "PUT",
        url: `${API_URL}resumeitem`,
        params: params,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(() => {
      })
      .catch(err => {
        console.log(err)
      })
    },
    deleteQuest (id) {
      Axios({
        method: "DELETE",
        url: `${API_URL}resumeitem`,
        params: {'resumeitemId':id},
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(() => {
        console.log()
      })
      .catch(err => {
        console.log(err)
      })
    }
  },
  created () {
    // 자소서 정보 가져오기
    Axios({
      method: "GET",
      url: `${API_URL}resume/${this.id}`,
      headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
    })
    .then(res => {
      if (res.data.email != this.email) {
        this.isWritter = false
        this.$router.push({ name: "coverLetterDetail", params: {id: this.id}})
      } else { 
        this.isWritter = true 
      }
      this.title = res.data.title
      this.category = res.data.category
      this.job = res.data.job
      this.company = res.data.company
    })
    .catch(err => {
      console.log(err)
    })
    // 자소서 항목 가져오기
    Axios({
      method: "GET",
      url: `${API_URL}resumeitem/resume/${this.id}`,
      headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
    })
    .then(res => {
      for (var i=0; i<res.data.length; i++) {
        const item = {}
        item.title = res.data[i].title
        item.answer = res.data[i].content
        item.num = this.question
        item.id = res.data[i].id
        this.question += 1
        this.items.push(item)
      }
    })
    .catch(err => {
      console.log(err)
    })
  }
}
</script>

<style>
.collapsed > .when-open,
.not-collapsed > .when-closed {
  display: none;
}
</style>