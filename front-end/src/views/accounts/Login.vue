<template>
  <div class="loginBackImage">
    <b-container
      style="max-width:450px; background-color:white; margin-left:300px; margin-top:80px"
    >
      <br />
      <h1>
        <img src="../../assets/logologo.png" style="height:80px" />Login
      </h1>
      <br />
      <b-form @submit.prevent="login(loginData)" style="padding:1rem;">
        <b-row>이메일</b-row>
        <b-row>
          <b-form-input
            id="input-1"
            v-model="loginData.email"
            type="email"
            required
            placeholder="이메일 아이디를 입력하세요"
          ></b-form-input>
        </b-row>
        <br />
        <b-row>비밀번호</b-row>
        <b-row>
          <b-form-input
            id="input-2"
            v-model="loginData.password"
            required
            type="password"
            placeholder="비밀번호를 입력하세요"
          ></b-form-input>
        </b-row>
        <b-row>
          <b-col class="text-right py-1 pr-1">
            <router-link :to="{ name: 'FindPassword' }">비밀번호 찾기</router-link>
          </b-col>
        </b-row>
        <br />
        <b-row>
          <!-- @click="update_email(loginData.email)" -->
          <b-button type="submit" block variant="info" size="lg">로그인</b-button>
        </b-row>
        <b-row>
          <p>Help Desk : 010-7374-5848</p>
        </b-row>
        <b-row>
          <p>평일 상담시간 : 24:00 ~ 25:00</p>
        </b-row>
        <b-row>
          <b-col class="text-right py-1 pr-1">
            <router-link :to="{ name: 'Signup' }">회원가입</router-link>
          </b-col>
        </b-row>
      </b-form>
    </b-container>
  </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  data() {
    return {
      loginData: {
        email: null,
        password: null,
      },
    };
  },
  methods: {
    ...mapActions(["login"]),
  },
};
</script>

<style>
.loginBackImage {
  background-image: url("../../assets/second.jpg");
  background-repeat: no-repeat;
  background-position: right center;
  /* background-size: cover; */
  max-width: 100%;
  height: 100vh;
  padding: 10rem;
}
</style>