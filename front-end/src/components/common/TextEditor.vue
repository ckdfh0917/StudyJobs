<template>
  <div>
    <b-form-input v-model="title" placeholder="제목을 입력해주세요"></b-form-input>
    <hr>
    <div class="sample-toolbar">
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
    </div>
    <hr>
    <p
      v-for="(value, index) in content"
      id="paragraph"
      :key="index"
      contenteditable
      @input="event => onInput(event, index)"
      @keyup.delete="onRemove(index)"
      class="editor"
    />
    <hr>
    <button class='btn btn-success' @click.prevent='Submit'>제출</button>
  </div>
</template>

<script>
import Axios from 'axios';
const API_URL = process.env.VUE_APP_LOCAL_URL;
export default {
  data() {
    return {
      content: [
        { value: '여기에 글을 쓰세요' },
      ],
      size: 3,
      title: null,
      BV: false,
      IV: false,
      UV: false,
      fontColor: '#000000',
      backColor: '#ffffff',
    };
  },
  props: {
    propsData: Object,
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
        'studyId': this.propsData.studyId,
        'title': this.title,
        "writer": this.propsData.writerId
      }
      Axios({
        method: "POST",
        url: `${API_URL}post`,
        params: params,
        headers: { "Content-Type": "application/json; charset=utf-8",
                  'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                  'user-email': sessionStorage.getItem('user-email')},
      })
      .then(() => {
        this.$emit('endSubmit')
      })
      .catch(err => {
        console.log('err', err)
        alert(err.response.data)
      })
    }
  },
};
</script>