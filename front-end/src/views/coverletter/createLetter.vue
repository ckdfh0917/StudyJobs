<template>
  <div class="my-3">
    <b-container>
      <br>
      <h1>자소서 작성</h1>
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
          <b-form-input v-model="company" placeholder="회사를 입력해주세요" required></b-form-input>
        </b-col>
        <b-col>
          <b-form-input v-model="job" placeholder="직무를 입력해주세요" required></b-form-input>
        </b-col>
      </b-row>
      <b-row class="my-3">
        <b-col>
          <b-form-input v-model="title" placeholder="글 제목을 입력해주세요" required></b-form-input>
        </b-col>
      </b-row>
      <!-- 제목까지 -->

      <div>
        <b-card no-body>
          <b-tabs card>
            <!-- Render Tabs, supply a unique `key` to each tab -->
            <b-tab v-for="item in items" :key="item.num" :title="`${item.num} 번`">
              <div class="my-3" style="height: 630px">
                <b-form-textarea 
                  v-model="item.title" 
                  required
                  placeholder="질문을 입력하세요" 
                  rows="5"
                  max-rows="9"
                  class="pt-3"
                ></b-form-textarea>
                <b-form-textarea
                  v-model="item.answer"
                  required
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
      items: [{num: 1, title: null, answer: null}],
      question:1,
      title: null,
      company: null,
      job: null,
      category: null,
      options: [
        { value: null, text: '항목을 선택해주세요' },
        { value: '자소서 컨펌', text: '자소서 컨펌'},
        { value: '자소서 면접', text: '자소서 면접'},
      ]
    }
  },
  computed: {
    ...mapState({
      email: (state) => state.moduleName.email,
    })
  },
  methods: {
    addQuestion () {
      this.question += 1
      const numCover = this.question
      var cover = {num: numCover, title: null, answer: null}
      this.items.push(cover)
    },
    delQuestion(x) {
      var changeItems = []
      for (let i = 0; i < this.items.length; i++) {
        if (this.items[i].num < x){
          changeItems.push({num:i+1,title:this.items[i].title,answer:this.items[i].answer})
        } else if (this.items[i].num > x) {
          changeItems.push({num:i,title:this.items[i].title,answer:this.items[i].answer})
        }
      }
      this.question -= 1
      this.items = changeItems
    },
    submitCoverLetter () {
      if (this.category == null) {
        alert('카테고리를 선택해주세요')
        return false
      }
      if (this.company == null) {
        alert('회사를 입력하세요')
        return false
      }
      if (this.job == null) {
        alert('직무를 입력하세요')
        return false
      }
      if (this.title == null) {
        alert('글 제목을 입력하세요')
        return false
      }
      var items = []
      for (var i=0;i<this.items.length; i++) {
        items.push({'title':this.items[i].title,'content':this.items[i].answer})
      }
      const body = {
        'title':this.title,
        'company': this.company,
        'job':this.job,
        'category': this.category,
        'resumeItems': items
      }
      
      var flag = false
      for (var j=0; j<items.length; j++) {
        if (!items[j].title || !items[j].content) {
          flag = true
          break
        }
      }
      if (flag) {
        alert('질문과 답변을 입력하세요')
        return false
      }
      Axios({
        method: "POST",
        url: `${API_URL}resume/resumeandresumeitem`,
        data: body,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(() => {
        alert('작성이 완료되었습니다')
        this.$router.push({ name: "LetterList"})
      })
      .catch(err => {
        console.log('err', err)
      })
    },

    submitQuestion (resumeId,quest) {
      var params = []
      var trigger = false
      for (var i=0; i<quest.length; i++) {
        params.push({'resume':resumeId,'title':quest[i].title,'content':quest[i].answer})
        if (i==quest.length-1) {trigger = true}
      }
      
      if (trigger) {
        Axios({
          method: "POST",
          url: `${API_URL}resumeitem?list=${params}`,
          headers: { "Content-Type": "application/json; charset=utf-8",
                    'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                    'user-email': sessionStorage.getItem('user-email')},
        })
        .then(() => {
          
        })
        .catch(err => {
          alert(err.response.data.msg)
        })
        trigger = false
      }
    }
  }
}
</script>

<style>
.collapsed > .when-open,
.not-collapsed > .when-closed {
  display: none;
}
</style>