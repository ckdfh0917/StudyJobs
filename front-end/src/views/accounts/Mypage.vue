<template>
  <b-container>
    <br>
    <h1 class="font-weight-bold">마이페이지</h1>
    <div class="myPage">
      <hr>
      <b-row>
        <b-col cols="4" id="mypageImage">
          <b-row>
            <b-col>
              <b-img id="myimg" :src="defaultImageUrl" width="300" height="300" rounded="circle" alt="프로필 이미지"></b-img>
            </b-col>
          </b-row>
          <br>
          <b-row>
            <b-col>
              <b-button variant="primary" class="btn_upload" type="button" @click="onPickFile">이미지 업로드</b-button>
              <input
                id="refresh"
                type="file"
                value="upload"
                style="display: none;"
                ref="fileInput"
                accept="image/*"
                @change="onFilePicked"
              />
            </b-col>
          </b-row>
        </b-col>
        <b-col cols="8" id="mypageInfo">
          <div id="userInfoList">
            <b-row>
              <b-col md="3" offset-md="1" class="text-left font-weight-bold">이메일</b-col>
              <b-col md="3" offset-md="3" class="text-right font-weight-bold">{{ email }}</b-col>
            </b-row>
            <hr>
            <b-row>
              <b-col md="3" offset-md="1" class="text-left font-weight-bold">이름</b-col>
              <b-col md="3" offset-md="3" class="text-right font-weight-bold">{{ userInfo.name }}</b-col>
            </b-row>
            <hr>
            <b-row>
              <b-col md="3" offset-md="1" class="text-left font-weight-bold">나이</b-col>
              <b-col md="3" offset-md="3" class="text-right font-weight-bold">{{ userInfo.age }}</b-col>
            </b-row>
            <hr>
            <b-row>
              <b-col md="3" offset-md="1" class="text-left font-weight-bold">성별</b-col>
              <b-col md="3" offset-md="3" class="text-right font-weight-bold" v-if="userInfo.sex == 1">남자</b-col>
              <b-col md="3" offset-md="3" class="text-right font-weight-bold" v-if="userInfo.sex == 2">여자</b-col>
            </b-row>
            <hr>
            <b-row>
              <b-col md="3" offset-md="1" class="text-left font-weight-bold">참여중인 스터디</b-col>
              <b-col md="3" offset-md="3" class="text-right font-weight-bold">{{ countStudy }}</b-col>
            </b-row>
            <hr>
            <b-row>
              <b-col md="3" offset-md="1" class="text-left font-weight-bold">개설한 스터디</b-col>
              <b-col md="3" offset-md="3" class="text-right font-weight-bold">{{ makingStudy }}</b-col>
            </b-row>
            <hr>
          </div>
          <div id="mypageBtn">
            <b-row align-h="end" class="text-right">
              <div class="badge text-wrap mr-2" style="width: 9rem;"><b-button route :to="{name: 'UpdateUserInfo'}" variant="warning">회원정보 수정</b-button></div>
              <div class="badge text-wrap mr-2" style="width: 9rem;"><b-button route :to="{name: 'EditPassword'}" variant="success">비밀번호 변경</b-button></div>
              <div class="badge text-wrap mr-2" style="width: 7rem;"><b-button variant="danger" v-b-modal.my-modal>회원탈퇴</b-button></div>
            </b-row>
          </div>
        </b-col>
      </b-row>
      <hr>
    </div>
    <div id="myStudying">
      <br>
      <b-row class="text-left ml-2">
        <h3>내 스터디</h3>
      </b-row>
      <br>
      <b-list-group>
        <b-row class="" style="line-height: 75px ">
          <b-col cols="4" class="p-0 font-weight-bold">
            <b-list-group-item style="height: 100px; color: white; font-size: 25px; background-color: rgb(25, 84, 241); border-color: white;">
              <b-icon icon="card-list" class="mr-2"></b-icon>
              스터디 이름
            </b-list-group-item>
          </b-col>
          <b-col cols="8" class="p-0 font-weight-bold">
            <b-list-group-item class="text-center" style="height: 100px; font-size: 25px;">
              <b-icon icon="layout-text-sidebar-reverse" class="mr-2"></b-icon> 
              스터디 소개
            </b-list-group-item>
          </b-col>
        </b-row>
        <b-row v-for="item in items" :key="item.studyId">

          <b-col cols="4" class="p-0"><b-list-group-item id="myStudyList" route :to="{ name: 'StudyDetail', params: {id:item.studyId} }">{{ item.title }} <i class="fas fa-book-reader" v-if="item.isMine"></i></b-list-group-item></b-col>
          <b-col cols="8" class="p-0"><b-list-group-item route :to="{ name: 'StudyDetail', params: {id:item.studyId} }">{{ item.content }} <i class="fas fa-book-reader" v-if="item.isMine"></i></b-list-group-item></b-col>
        </b-row>
      </b-list-group>
      <!-- 이 밑에 의미없는게 re rendering효과를 줘서 위에 표시를 나타나게 함-->
      <b-list-group>
        <b-row v-for="item in myStudy" :key="item.studyId">
        </b-row>
      </b-list-group>
      <!-- 의미 없지만 지우지는 말 것 -->
    </div>

    <div id="newStudy">
      <br>
      <b-row class="text-left ml-2">
        <h3>가입 요청</h3>
      </b-row>
      <br>
      <b-list-group>
        <b-row class="" style="line-height: 75px ">
          <b-col cols="4" class="p-0 font-weight-bold">
            <b-list-group-item style="height: 100px; color: white; font-size: 25px; background-color: rgb(25, 84, 241); border-color: white;">
              <b-icon icon="card-list" class="mr-2"></b-icon>
              스터디 이름
            </b-list-group-item>
          </b-col>
          <b-col cols="8" class="p-0 font-weight-bold">
            <b-list-group-item class="text-center" style="height: 100px; font-size: 25px;">
              <b-icon icon="layout-text-sidebar-reverse" class="mr-2"></b-icon> 
              스터디 소개
            </b-list-group-item>
          </b-col>
        </b-row>
        <b-row v-for="applying in applyingList" :key="applying.title">
          <b-col cols="4" class="p-0"><b-list-group-item id="myStudyList" route :to="{ name: 'StudyDetail', params: {id:applying.id} }">{{ applying.title }}</b-list-group-item></b-col>
          <b-col cols="8" class="p-0"><b-list-group-item route :to="{ name: 'StudyDetail', params: {id:applying.id} }">{{ applying.content }}</b-list-group-item></b-col>
        </b-row>
      </b-list-group>
    </div>

    <!-- 모달 -->
    <b-modal id="my-modal">
      <template v-slot:modal-header>
        <h5>회원탈퇴 하시겠습니까?</h5>
      </template>

      <template v-slot:default="">
        <b-form-input
          id="input-2"
          required
          type="password"
          v-model="confirmPassword"
          placeholder="비밀번호를 입력하세요."
        ></b-form-input>
      </template>

      <template v-slot:modal-footer="{ cancel }">
        <!-- Emulate built in modal footer ok and cancel button actions -->
        <b-button size="sm" variant="danger" @click="deleteUserAccount(confirmPassword)">
          회원탈퇴
        </b-button>
        <b-button size="sm" variant="primary" @click="cancel()">
          취소
        </b-button>
      </template>
    </b-modal>

  <br><br>
  </b-container>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import Axios from 'axios';
import router from "@/router";
import VueCookies from "vue-cookies";
import firebase from 'firebase'

const API_URL = process.env.VUE_APP_LOCAL_URL

export default {
  data() {
    return {
      mainProps: { blank: true, blankColor: '#777', width: 75, height: 75, class: 'm1' },
      value: 0,
      max: 100,
      items: [],
      countStudy: 0,
      confirmPassword: null,
      userInfo: {
        sex: null,
        age: null,
        name: null,
      },
      progressUpload: 0,
      ch: true,
      uploadTask: '',
      defaultImageUrl: 'https://previews.123rf.com/images/salamatik/salamatik1801/salamatik180100019/92979836-%ED%94%84%EB%A1%9C%ED%95%84-%EC%9D%B5%EB%AA%85%EC%9D%98-%EC%96%BC%EA%B5%B4-%EC%95%84%EC%9D%B4%EC%BD%98-%ED%9A%8C%EC%83%89-%EC%8B%A4%EB%A3%A8%EC%97%A3-%EC%82%AC%EB%9E%8C%EC%9E%85%EB%8B%88%EB%8B%A4-%EB%82%A8%EC%84%B1-%EA%B8%B0%EB%B3%B8-%EC%95%84%EB%B0%94%ED%83%80-%EC%82%AC%EC%A7%84-%EC%9E%90%EB%A6%AC-%ED%91%9C%EC%8B%9C-%EC%9E%90-%ED%9D%B0%EC%83%89-%EB%B0%B0%EA%B2%BD%EC%97%90-%EA%B3%A0%EB%A6%BD-%EB%B2%A1%ED%84%B0-%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8-%EB%A0%88%EC%9D%B4-%EC%85%98.jpg',
      myStudy: [],
      applyingList: [],
      makingStudy: 0,
    }
  },
  

  computed: {
    ...mapState({
      email: state => state.moduleName.email,
    }),
  },

  methods: {
    onPickFile() {
      this.$refs.fileInput.click()
    },
    onFilePicked(event) {
      firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
          // 업로드
  
          var file = event.target.files[0];
          var storageRef = firebase.storage().ref(`images/${user.email}/${user.email}`);
          var task = storageRef.put(file);
          // var uploader = document.getElementById('uploader');      
    
          task.on('state_changed',
            //progress Bar
            function progess(){
              // this.value = (snapshot.bytesTransferred / snapshot.totalBytes) * 10;
              // // uploader.value = pct;
              // var uploading = document.getElementById('myPro')
              // console.log(uploading)
              // uploading.value = this.value
              // console.log('aa', this.value)
            },
            // error
            function error(err){
              console.log(err)
            },
            // complete
            function (){
              task.snapshot.ref.getDownloadURL().then(function(downloadURL) {
                var img = document.getElementById('myimg');
                img.src = downloadURL;
              })
            }
          )
        } else {
          // User is signed out.
          // console.log('failed firebase')
          // ...
        }
      })

    },

    ...mapActions(['authDelete']),
    cancel() {
      // console.log('취소')
    },
    deleteUserAccount(data) {
      const params = {
        email: this.email,
        password: data,
      }
      
      Axios({method:'DELETE', url:`${API_URL}user`,params:params,headers:{'Content-Type': 'application/json; charset=utf-8',
                                                                          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
                                                                          'user-email': sessionStorage.getItem('user-email')}})
      .then(res => {
        var user = firebase.auth().currentUser;

        user.delete().then(function() {
          // User deleted.
        }).catch(function(error) {
          // An error happened.
          console.log(error)
        })
        this.$store.commit('SET_TOKEN', null)
        VueCookies.remove("auth-token")
        localStorage.clear();
        sessionStorage.clear();

        // commit("SET_TOKEN", null); // state 에서도 삭제

        // this.$store.dispatch('logout')
        alert(res.data)
        router.push({ name: "Home" })
      })
      .catch(err => {
        if (err.request.status === 500) {
          alert(`${this.userInfo.name} 님은 스터디 팀의 리더입니다.`)
        } else {
          alert('삭제 실패. 비밀번호를 확인해주세요!')
        }
      })
    },
    check (resItem,i) {
      Axios.get(`${API_URL}study/${resItem[i].studyId}`,{
        headers: {
          "Content-Type": "application/json; charset=utf-8",
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
          }
        })
        .then(respon => {
          if (respon.data.mgrEmail == this.email) {
            resItem[i]['isMine'] = true
            this.myStudy.push(resItem[i])
          } else {resItem[i]['isMine'] = false}
        })
        .catch(error => {console.log(error)})
    }
  },
  created () {
    // 해당 아이디에 대한 가입 신청한 스터디 정보 가져오기
    Axios.get(`${API_URL}study/mystudy/applying`,  {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.applyingList = res.data
    })
    .catch(err => {
      console.log(err)
    })
    // 해당 아이디에 대한 스터디 정보 가져오기
    Axios.get(`${API_URL}study/mystudy/join?email=${this.email}`,  {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      var resItem = res.data
      for (var i=0; i<resItem.length; i++) {
        this.check(resItem,i)
        if (resItem[i].mgrEmail === this.email) {
          this.makingStudy += 1
        }
      }
      this.items = resItem
      this.countStudy = resItem.length
    })
    .catch(err => {
      console.log(err)
    })


    // 유저 정보 가져오기
    Axios.get(`${API_URL}user/${this.email}`, {
      headers: {
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.userInfo = res.data
    })
    .catch(err => {
      console.log('err', err)
    })
    
    // firebase 유저정보 가져오기
    firebase.auth().onAuthStateChanged(function(user) {
      if (user) {
        var storageRef = firebase.storage().ref();
        //
        var listRef = storageRef.child(`images/${user.email}`);
        
        // Find all the prefixes and items.
        listRef.listAll().then(function(res) {
          res.items.forEach(function(itemRef) {
            const fullPath = itemRef.fullPath
            // 프로필 이미지 가져오기
            firebase.storage().ref(`${fullPath}`).getDownloadURL()
            .then(function(url) {
              var xhr = new XMLHttpRequest();
              xhr.responseType = 'blob';
              xhr.onload = function() {};
              xhr.open('GET', url);
              xhr.send();
              var img = document.getElementById('myimg');
              img.src = url;
            })
            .catch(function(err) {
              console.log(err)
            })
            // All the items under listRef.
          });
        }).catch(function(error) {
          // Uh-oh, an error occurred!
          console.log('eer', error)
        });


        
      } else {
        // User is signed out.
        // console.log('failed firebase')
        // ...
      }
    })
  },
}
</script>

<style>
#myStudyList {
  color: white; 
  /* background-color: rgb(25, 84, 241); */
  background-color: #1E90FF;
  border-color: white;
}

</style>