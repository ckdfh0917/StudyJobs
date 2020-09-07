
<script src="https://kit.fontawesome.com/958a5175c9.js" crossorigin="anonymous"></script>
<template>
  <b-container class="bv-example-row my-5">
    <h1 class="mb-3">비밀번호 찾기</h1>
    <div
      style="max-width:700px;"
      class="mx-auto middle"
    > 
      <!-- 이메일 / 인증코드 발송 -->
      <b-form @submit.prevent="checkEmail">
        <b-input-group class="mb-3">
          <b-input-group-prepend is-text>
            <b-icon icon="envelope"></b-icon>
          </b-input-group-prepend>
          <b-form-input 
            v-model="email" 
            type="email" 
            required
            placeholder="ssafy@example.com"
          >
          </b-form-input>
          <b-button type="submit">인증코드 발송</b-button>
        </b-input-group>

        <!-- <b-input-group class="mb-3">
          <b-button block type="submit">인증코드 발송</b-button>
        </b-input-group> -->

      </b-form>
      <b-form @submit.prevent="changePassword(passwordInfo)">
        <!-- 인증 코드 -->
        <b-input-group class="mb-3">
          <b-input-group-prepend is-text>
            <b-icon icon="pencil-square"></b-icon>
          </b-input-group-prepend>
          <b-form-input type="text" v-model="code" required placeholder="인증 코드"></b-form-input>
        </b-input-group>
        <!-- 비밀번호 -->
        <b-input-group class="mb-3">
          <b-input-group-prepend is-text>
            <b-icon icon="unlock"></b-icon>
          </b-input-group-prepend>
          <b-form-input type="password" v-model="passwordInfo.password" required placeholder="비밀번호는 영문과 숫자가 적어도 1자 이상씩 포함된 8자이상으로 구성되어야 합니다."></b-form-input>
        </b-input-group>
        <!-- 비밀번호 확인 -->
        <b-input-group class="mb-3">
          <b-input-group-prepend is-text>
            <b-icon icon="unlock-fill"></b-icon>
          </b-input-group-prepend>
          <b-form-input type="password" v-model="passwordInfo.passwordConfirm" required placeholder="비밀번호 확인"></b-form-input>
        </b-input-group>
        


        <b-button class="mt-2" type="submit"  block variant="secondary">비밀번호 변경</b-button>
      </b-form>
    </div>
  </b-container>
</template>

<script>
import Axios from "axios"
import router from "@/router"
import firebase from 'firebase'

const API_URL = process.env.VUE_APP_LOCAL_URL

export default {
  data() {
    return {
      email: "",
      passwordInfo: {
        password: null,
        passwordConfirm: null,
      },
      password: "",
      passwordConfirm: "",
      code: "",
    };
  },
  methods: {
    changePassword(passwordInfo) {
      const code = this.code
      const email = this.email
      const password = this.passwordInfo.password
      const passwordConfirm = this.passwordInfo.passwordConfirm
      if (password != passwordConfirm) {
        alert("비밀번호 확인과 비밀번호가 다릅니다!");
        return false;
      } 
      if (password.length < 8) {
        alert("비밀번호는 8자 이상으로 구성되어야합니다.")
        return false
      } else {
        let numCheck = false
        let strCheck = false

        for (var i=0; i< password.length; i++) {
          const temp = password.charAt(i);
          if (!isNaN(temp)) {
            numCheck = true
          } else {
            strCheck = true
          }
        }
        if (!strCheck || !numCheck) {
          alert("비밀번호는 영문과 숫자가 적어도 1자 이상씩 포함되어야합니다.")
          return false
        }
      }
      const params = {
        'code': this.code,
        'email': this.email
      }
      // 이메일 인증 성공 유무
      Axios({
        method: "POST",
        url: `${API_URL}verify/help`,
        params: params,
        headers: { 
          "Content-Type": "application/json; charset=utf-8",
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        },
      })
      .then((res) => {
        const putParams = {
          'code': this.code,
          'email': this.email,
          'password': password
        }
        // 비밀번호 변경
        Axios({
          method: "PUT",
          url: `${API_URL}user/help`,
          params: putParams,
          headers: { 
            "Content-Type": "application/json; charset=utf-8",
            'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
            'user-email': sessionStorage.getItem('user-email') },
         })
        .then((res) => {

          const loginData = {
            'email': this.email,
            'password': password,
          }
          // 로그인
          alert(res.data)
          this.$store.dispatch('login', loginData)
          
        })
        .catch((err) => {
          // alert(err)
          console.log('err', err)
        })
      })
      .catch((err) => {
        alert(err.response.data)
        this.code = ''
        return false
      })      
    },
    checkEmail() {
      const params = {
        'email': this.email
      }
      Axios({
        method: "GET",
        url: `${API_URL}verify/help`,
        params: params,
        headers: { "Content-Type": "application/json; charset=utf-8" },
      })
      .then((res) => {
        alert(res.data)
      })
      .catch((err) => {
        alert(err.response.data)
        this.email = ''
        return false
      })
    },
  },
};
</script>

<style></style>
