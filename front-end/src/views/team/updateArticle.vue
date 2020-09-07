<template>
  <b-container>
    <br>
    <h1>게시글 수정 페이지</h1>
    <h1>{{ articleIdd }}</h1>
    <b-row align-h="start" class="mb-3">
      <b-col md="2" offset-md="1" class="text-left">게시글</b-col>
    </b-row>
    <b-row >
      <b-col md="10" offset-md="1" class="">
        <b-form-input class="text-left" v-model="title" placeholder="제목을 입력해주세요"></b-form-input>
      </b-col>
    </b-row>
    <br>
    <br>
    <b-row>
      <b-col md="10" offset-md="1" class="border py-3">
        <b-row>
          <b-col md="10" offset-md="1" class="sample-toolbar border py-2">
            <a href="javascript:void(0)" id="Bold" @click="format('bold')" class="text-decoration-none text-dark"><span class="fa fa-bold fa-fw"></span></a>
            <a href="javascript:void(0)" id="Italic" @click="format('italic')" class="text-decoration-none text-dark"><span class="fa fa-italic fa-fw"></span></a>
            <a href="javascript:void(0)" id="Underline" @click="format('underline')" class="mr-3 text-decoration-none text-dark"><span class="fas fa-underline fa-fw"></span></a>
            <select name="job" id='test' v-model="size" @click="changeSize" class="mr-3">
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
            </select>
            글자색: <input name="Color Picker" type="color" v-model="fontColor" @change="changeColor" class="mr-3"/>
            배경색: <input name="Color Picker" type="color" v-model="backColor" @change="changeBackColor" class="mr-3"/>
          </b-col>
        </b-row>
        <br>
        <b-row style="height: 500px">
          <!-- {{ this.content }} -->
          <b-col md="10" offset-md="1" class="py-3 border" >
            <p
              v-for="(value, index) in content"
              id="paragraph"
              :key="index"
              contenteditable
              @input="event => onInput(event, index)"
              @keyup.delete="onRemove(index)"
              class="editor text-left p-2"
              style="height: 450px"
            />
            
          </b-col>
        </b-row>
      </b-col>
    </b-row>
    <br>
    <b-row align-h="end">
      <b-col md="3" offset-md="1" class="text-right">
        <b-button variant="primary" @click.prevent='Submit'>수정하기</b-button>
      </b-col>
      <b-col md="1"></b-col>
    </b-row>
  </b-container>
</template>

<script>
import { mapState } from "vuex";
import Axios from 'axios';
const API_URL = process.env.VUE_APP_LOCAL_URL;
export default {
  data() {
    return {
      content: [
        { value: '여기에 글을 쓰세요' },
      ],
      cont: null,
      test: [],
      size: 3,
      title: null,
      BV: false,
      IV: false,
      UV: false,
      fontColor: '#000000',
      backColor: '#ffffff',
      id: this.$route.params.articleid,
      articleId: null,
      studyId : null,
    };
  },
  computed: {
    ...mapState({
      email: state => state.moduleName.email,
    })
  },
  created () {
    // article 정보 가져오긴
    Axios({
      method: "GET",
      url: `${API_URL}post/${this.id}`,
      headers: { "Content-Type": "application/json; charset=utf-8",
                'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                'user-email': sessionStorage.getItem('user-email')},
    })
    .then(res => {
      this.articleId = res.data.id
      this.title = res.data.title
      this.studyId = res.data.study_id
      this.check(res.data.writer)
    })
  },
  mounted() {
    document.getElementById('paragraph').setAttribute('contenteditable', 'true');
    this.updateAllContent();
  },
  methods: {
    onInput(event, index) {
      const value = event.target.innerText;
      this.content[index].value = value;
    },
    onRemove(index) {
      if (this.content.length > 1 && this.content[index].value.length === 0) {
        this.$delete(this.content, index);
        this.updateAllContent();
      }
    },
    updateAllContent() {
      this.content.forEach((c) => {
        const el = document.getElementById('paragraph');
        el.innerText = c.value;
      });
    },
    format(command, value) {
      if (command == 'bold') {
        this.BV = !this.BV
      } else if (command == 'italic') {
        this.IV = !this.IV
      } else if (command == 'underline') {
        this.UV = !this.UV
      }

      if (this.BV) {
        document.getElementById('Bold').className = 'text-decoration-none text-danger'
      } else {document.getElementById('Bold').className = 'text-decoration-none text-dark'}
      if (this.IV) {
        document.getElementById('Italic').className = 'text-decoration-none text-danger'
      } else {document.getElementById('Italic').className = 'text-decoration-none text-dark'}
      if (this.UV) {
        document.getElementById('Underline').className = 'mr-3 text-decoration-none text-danger'
      } else {document.getElementById('Underline').className = 'mr-3 text-decoration-none text-dark'}
		document.execCommand(command, false, value);
    },
    changeSize() {
      this.size *= 1
      document.execCommand('fontSize', false,this.size)
    },
    changeColor () {
      document.execCommand('foreColor', false, this.fontColor)
    },
    changeBackColor () {
      document.execCommand('backColor', false, this.backColor)
    },
    Submit () {
      const articleData = document.querySelector('#paragraph')
      const params = {
        'content': articleData.innerHTML,
        'id': this.articleId,
        'title': this.title,
      }
      
      Axios({
        method: "PUT",
        url: `${API_URL}post`,
        params: params,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(res => {
        alert(res.data)
        this.$router.push({ name: "ArticleDetail" ,params:{ studyid:this.studyId, articleid:this.articleId }})
      })
      .catch(err => {
        alert(err.response.data)
      })
    },
    check(userId) {
      Axios({
        method: "GET",
        url: `${API_URL}user/id/${userId}`,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(resp => {
        if (resp.data.email != this.email) {
          alert('다른 유저의 글은 수정할 수 없습니다.')
          this.$router.push({ name: "ArticleDetail", params: {studyid:this.studyId,articleid:this.articleId}})
        }
      })
      .catch(() => {alert('스터디팀 정보를 불러올 수 없습니다')})
    }
  },
};
</script>

<style>

</style>