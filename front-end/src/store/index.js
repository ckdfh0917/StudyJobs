import Vue from "vue";
import Vuex from "vuex";
import Axios from "axios";
import router from "@/router";
import VueCookies from "vue-cookies";

import createPersistedState from "vuex-persistedstate";
import moduleName from "./test_moduleName";
import firebase from 'firebase'

Vue.use(Vuex);

const modules = {
  moduleName,
};
const plugins = [
  createPersistedState({
    paths: ["moduleName"],
  }),
];
const API_URL = process.env.VUE_APP_LOCAL_URL;
export default new Vuex.Store({
  state: {
    authToken: VueCookies.get("auth-token"),
  },
  getters: {
    // auth
    isLoggedIn: (state) => !!state.authToken,
    // auth, articles
    config: (state) => ({
      headers: { Authorization: `Token ${state.authToken}` },
    }),
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.authToken = token;
      VueCookies.set("auth-token", token);
    },
  },
  actions: {
    login({ commit }, loginData) {
      const params = {
        'email' : loginData.email,
        'password' : loginData.password
      }
      var JsonForm = JSON.stringify(params)
      Axios({method:'POST',url:`${API_URL}login`,params:params,data:JsonForm,headers:{'Content-Type': 'application/json; charset=utf-8'}})
      .then(res => {
        commit('SET_TOKEN', res.data.token)
        commit('UPDATE_EMAIL', loginData.email)

        sessionStorage.setItem('jwt-auth-token', res.data.token);
        sessionStorage.setItem('user-email', loginData.email);

        // 로그인 시간 저장
        let date = new Date()
        var loginH = date.getHours()
        var loginM = date.getMinutes()
        var loginS = date.getSeconds()

        if (loginH < 10) {
          loginH = '0' + loginH
        }
        if (loginM < 10) {
          loginM = '0' + loginM
        }
        if (loginS < 10) {
          loginS = '0' + loginS
        }

        const loginTime = loginH + ":" + loginM + ":" + loginS
        commit('UPDATE_LOGIN_TIME', loginTime)

        var fbpassword = res.data.fbpassword
        // firebase 사용자 로그인
        firebase.auth().signInWithEmailAndPassword(loginData.email, fbpassword)
        .catch(function(error) {
          // Handle Errors here.
          var errorMessage = error.message;
          console.log('파이어베이스 로그인 에러')
          console.log(errorMessage)
          // ...
        })
        
        
        alert("로그인")

        router.push({ name: 'Home' })
      })
      .catch(err => {
        alert(err.response.data)
      })
    },
    
    initSignUp({ commit }, loginData) {
      const params = {
        'email' : loginData.email,
        'password' : loginData.password
      }
      var JsonForm = JSON.stringify(params)
      Axios({method:'POST',url:`${API_URL}login`,params:params,data:JsonForm,headers:{'Content-Type': 'application/json; charset=utf-8'}})
      .then(res => {
        commit('SET_TOKEN', res.data.token)
        commit('UPDATE_EMAIL', loginData.email)

        sessionStorage.setItem('jwt-auth-token', res.data.token);
        sessionStorage.setItem('user-email', loginData.email);

        // 로그인 시간 저장
        let date = new Date()
        var loginH = date.getHours()
        var loginM = date.getMinutes()
        var loginS = date.getSeconds()

        if (loginH < 10) {
          loginH = '0' + loginH
        }
        if (loginM < 10) {
          loginM = '0' + loginM
        }
        if (loginS < 10) {
          loginS = '0' + loginS
        }

        const loginTime = loginH + ":" + loginM + ":" + loginS
        commit('UPDATE_LOGIN_TIME', loginTime)
        
        var fbpassword = res.data.fbpassword
        // firebase 사용자 로그인
        firebase.auth().signInWithEmailAndPassword(loginData.email, fbpassword)
        .then(() => {
        
        })
        .catch(function(error) {
          // Handle Errors here.
          var errorMessage = error.message;
          console.log('파이어베이스 로그인 에러')
          console.log(errorMessage)
          // ...
        })

        alert("로그인")

        router.push({ name: 'Home' })
      })
      .catch(err => {
        alert(err.response.data)
      })
    },

    signup({ dispatch }, { code, age, email, nickname, password, sex }) {
      var params = new URLSearchParams();
      params.append("code", code);
      var form = {
        age: age,
        email: email,
        name: nickname,
        password: password,
        sex: sex,
      };
      var JsonForm = JSON.stringify(form);
      Axios({
        method: "POST",
        url: `${API_URL}user/signUp`,
        params: params,
        data: JsonForm,
        headers: { "Content-Type": "application/json; charset=utf-8" },
      })
      .then((res) => {
        var fbPassword = res.data.fbPassword
        // firebase 회원가입
        firebase.auth().createUserWithEmailAndPassword(email, fbPassword)
        .catch(function (error) {
          // Handle Errors here.
          var errorMessage = error.message;
          console.log("firebase 인증 에러");
          console.log(errorMessage);
          // ...
        })
        //
        const loginData = {
          'email': email,
          'password': password,
        }
        
        dispatch("initSignUp", loginData)

        //
        alert(res.data.msg);
        // router.push({ name: "Login" });
      })
      .catch((err) => {
        alert(err.response.data);
        console.log(err);
      });
    },

    logout({ commit }) {
      commit("SET_TOKEN", null); // state 에서도 삭제
      VueCookies.remove("auth-token"); // cookie 에서는 삭제
      
      localStorage.clear();
      sessionStorage.clear();
      
      // 파이어베이스 로그아웃
      firebase.auth().signOut().then(function() {
        // Sign-out successful.
      }).catch(function(error) {
        // An error happened.
        console.log(error)
      });

      alert("로그아웃");
      router.push({ name: "Home" });
    },
    createTeam(state, form) {
      Axios.post(`${API_URL}study/create`, form)
        .then(() => {
          alert("스터디 생성");
        })
        .catch((err) => {
          alert(err.response.data);
        });
    },
  },
  modules,
  plugins,
  namespaced: true,
});
