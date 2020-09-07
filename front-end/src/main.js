import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";

// vue cookies
import VueCookies from "vue-cookies";
Vue.config.productionTip = false;
Vue.use(VueCookies);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

// bootstrap
import { BootstrapVue, IconsPlugin } from "bootstrap-vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
Vue.use(BootstrapVue);
Vue.config.productionTip = false;

//daterangepicker
import VMdDateRangePicker from "v-md-date-range-picker";
Vue.use(VMdDateRangePicker);

//vue-slider
import VueSlider from "vue-slider-component";
import "vue-slider-component/theme/default.css";
Vue.component("VueSlider", VueSlider);

//fontAwesome
import "../fontAwesomeIcon.js"; // fontAwesomeIcon.js 불러옴

//firebase
import firebase from 'firebase'

var firebaseConfig = {
  apiKey: "AIzaSyDuVnkxHS-vWwE0Wo_XKcG-u_djB9sB2-w",
  authDomain: "ssafy-ssbal.firebaseapp.com",
  databaseURL: "https://ssafy-ssbal.firebaseio.com",
  projectId: "ssafy-ssbal",
  storageBucket: "ssafy-ssbal.appspot.com",
  messagingSenderId: "813066731926",
  appId: "1:813066731926:web:2de5c7b68b2d359e479db7",
  measurementId: "G-CHB9G6C06P"
};

firebase.initializeApp(firebaseConfig)
firebase.analytics()

Vue.config.productionTip = false


 
new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
