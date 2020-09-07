<template>
  <div>
    <b-container>
      <h1 class="my-3">[{{ team.title }}]의 게시판</h1>
      <b-row align-h="end">
        <b-col cols="2">
          <b-button variant="primary" v-b-modal.modal-1 class="my-3">글쓰기</b-button>
          <b-modal id="modal-1" title="글쓰기" hide-footer>
            <TextEditor v-on:endSubmit="closeModal" v-bind:propsData="{ studyId, writerId }"></TextEditor>
          </b-modal>
        </b-col>
      </b-row>
      <hr>  
      <b-row>
        <b-col cols="2"></b-col>
        <b-col cols="5">제목</b-col>
        <b-col cols="2">글쓴이</b-col>
        <b-col cols="3">작성일</b-col>
      </b-row>
      <hr>
      <b-row v-for="article in articles" :key="article.studyId" class="my-3">
        <!-- <b-col cols="2">{{ article.id }}</b-col> -->
        <b-col cols="2">{{ article.newId }}</b-col>
        <b-col cols="5" class="text-left"><b-link route :to="{ name: 'ArticleDetail', params: {studyid:studyId, articleid:article.id} }">{{ article.title }}</b-link></b-col>
        <b-col cols="2">{{ article.writer }}</b-col>
        <b-col cols="3">{{ article.date }}</b-col>
        <hr>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import Axios from 'axios'
import TextEditor from '../../components/common/TextEditor.vue'
const API_URL = process.env.VUE_APP_LOCAL_URL
export default {
  data () {
    return {
      studyId: this.$route.params.id,
      isMember: false,
      teamTitle: null,
      articles: {},
      writer: null,
      writerName: null,
      writerId: null,
      articlesInfo: [],
      articlesName: [],
    }
  },
  computed: {
    ...mapState({
      email: state => state.moduleName.email,
    })
  },
  props: {
    team: Object,
  },
  created () {
    // 게시판의 모든 글
    Axios({
      method: "GET",
      url: `${API_URL}post/study/${this.studyId}`,
      headers: { "Content-Type": "application/json; charset=utf-8",
                'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                'user-email': sessionStorage.getItem('user-email')},
      }
    )
    .then(res => {
      this.articles = res.data
      this.articles.sort(function (a, b){
        return b.id - a.id
      })
      for (var i=0; i < res.data.length; i++) {
        this.articles[i].newId = res.data.length - i
        this.findWriterName(this.articles[i].writer, i)
      }
    })
    .catch(() => {
      alert('스터디팀 정보를 불러올 수 없습니다')
    })
    // 게시판의 모든 글
    Axios({
      method: "GET",
      url: `${API_URL}user/${this.email}`,
      headers: { "Content-Type": "application/json; charset=utf-8",
                'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                'user-email': sessionStorage.getItem('user-email')},
      }
    )
    .then(res => {
      this.writerId = res.data.id
    })
    .catch(() => {
      alert('스터디팀 정보를 불러올 수 없습니다')
    })


  },
  components: {
    TextEditor
  },
  methods: {
    // 글쓰기 모달
    closeModal () {
      Axios({
        method: "GET",
        url: `${API_URL}post/study/${this.studyId}`,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(res => {
        this.articles = res.data
        this.articles.sort(function (a, b){
          return b.id - a.id
        })
        for (var i=0; i < res.data.length; i++) {
          this.articles[i].newId = res.data.length - i
          this.findWriterName(this.articles[i].writer, i)
        }
      })
      .catch(() => {alert('스터디팀 정보를 불러올 수 없습니다')})
      this.$bvModal.hide('modal-1')
    },
    // 작성자 id로 name 찾기
    findWriterName(userId, index) {
      Axios({
        method: "GET",
        url: `${API_URL}user/id/${userId}`,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(res => {
        this.articles[index].writer = res.data.name
      })
      .catch(() => {alert('스터디팀 정보를 불러올 수 없습니다')})
    }
  }
}
</script>

<style>

</style>