<template>
  <div>
    <div>
      <b-card no-body>
        <b-tabs v-model="tabIndex" pills card vertical>
          <b-tab title="상세보기">
            <b-card-text>
              <b-container>
              <br>
              <b-row>
                <b-col class="totheleft text-center" cols="12"><h1>{{ team.title }}</h1></b-col>
              </b-row>
              <b-row  align-h="end">
                <b-col v-if="isLoggedIn" class="totheright my-3 text-right" offset="9" cols="3">
                  <!-- <b-button v-if="isMember" @click="toAricle" class="mr-1">게시판 보기</b-button> -->
                  <!-- 탈퇴 -->
                  <b-button v-if="isMember && email !== team.mgrEmail"  v-b-modal.modal-secession variant="info">탈퇴신청</b-button>
                </b-col>
                <b-col>
                  <b-modal
                    id="modal-secession"
                    ref="modal"
                    title="스터디를 탈퇴하시겠습니까?"
                    @show="resetModal"
                    @hidden="resetModal"
                    @ok="secessionOk"
                  >
                    <form ref="form" @submit.stop.prevent="secessionSubmit">
                      <b-form-group
                        label="다음 글을 옮겨 적으세요"
                        label-for="name-input"
                        invalid-feedback="message is required"
                      >
                        <div class="input-group input-group">
                          <p class="form-control text-center" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">해당 스터디를 탈퇴하겠습니다.</p>
                        </div>
                        <b-form-input
                          id="name-input"
                          v-model="checkDelete"
                          class="text-center"
                          required
                        ></b-form-input>
                      </b-form-group>
                    </form>
                    <template v-slot:modal-footer="{ ok, cancel }">
                      <!-- <b>Custom Footer</b> -->
                      <!-- Emulate built in modal footer ok and cancel button actions -->
                      <b-button size="sm" variant="success" @click="ok()">
                        신청
                      </b-button>
                      <b-button size="sm" variant="danger" @click="cancel()">
                        취소
                      </b-button>
                      <!-- Button with custom close trigger value -->
                    </template>
                  </b-modal>
                  <!-- 여기까지 -->
                  <b-row align-h="end" v-if="isLoggedIn">
                    <b-col md="2" class="text-right" v-if="!isMember">
                      <b-button v-if="!isReady" v-b-modal.modal-prevent-closing variant="info">가입신청</b-button>
                      <b-button v-if="isReady" variant="danger" @click="cancleSubmit">가입 신청 취소</b-button>
                    </b-col>
                  </b-row>
                  <br>
                  <b-modal
                    id="modal-prevent-closing"
                    ref="modal"
                    title="스터디를 신청하시겠습니까?"
                    @show="resetModal"
                    @hidden="resetModal"
                    @ok="joinOk"
                  >
                    <form ref="form" @submit.stop.prevent="handleSubmit">
                      <b-form-group
                        label="가입 신청을 하시려면 신청 버튼을 클릭해주세요"
                        label-for="name-input"
                        invalid-feedback="message is required"
                        class="text-center"
                      >
                      </b-form-group>
                    </form>
                    <template v-slot:modal-footer="{ ok, cancel }">
                      <!-- <b>Custom Footer</b> -->
                      <!-- Emulate built in modal footer ok and cancel button actions -->
                      <b-button size="sm" variant="success" @click="ok()">
                        신청
                      </b-button>
                      <b-button size="sm" variant="danger" @click="cancel()">
                        취소
                      </b-button>
                      <!-- Button with custom close trigger value -->
                    </template>
                  </b-modal>
                </b-col>
              </b-row>
            </b-container>
            
            <b-container>
              <b-card-group deck>
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header >
                    <div class="text-white">
                      <b-icon icon="flag-fill" aria-hidden="true"></b-icon> 리더
                    </div>
                  </template>
                  <b-card-text>{{team.mgrName}}</b-card-text>
                </b-card>

                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="people" aria-hidden="true"></b-icon> 인원
                    </div>
                  </template>
                  <b-card-text>{{ team.numMembers }} / {{ team.maxMembers }} 명</b-card-text>
                </b-card>
                
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="hand-thumbs-up" aria-hidden="true"></b-icon> 온/오프라인
                    </div>
                  </template>
                  <b-card-text>{{ team.place }}</b-card-text>
                </b-card>
                
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="file-text" aria-hidden="true"></b-icon> 분야
                    </div>
                  </template>
                  <b-card-text>{{ team.category }}</b-card-text>
                </b-card>
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="house" aria-hidden="true"></b-icon> 진행장소
                    </div>
                  </template>
                  <b-card-text>{{ team.si }} {{ team.gu }}</b-card-text>
                </b-card>
              </b-card-group>
              <br>
              <b-card-group deck>
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="calendar3" aria-hidden="true"></b-icon> 진행기간
                    </div>
                  </template>
                  <b-card-text>{{team.startDate}} ~ {{team.endDate}}</b-card-text>
                </b-card>
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="list-check" aria-hidden="true"></b-icon> 일정
                    </div>
                  </template>
                  <b-card-text>{{ team.period }} / {{ team.numMeetings }}회</b-card-text>
                </b-card>
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="alarm" aria-hidden="true"></b-icon> 주중/시간
                    </div>
                  </template>
                  <b-card-text>{{ team.week }} / {{ team.shift }}</b-card-text>
                </b-card>
              </b-card-group>
              <br>
              <b-card-group deck>
                <b-card header-tag="header" footer-tag="footer" header-bg-variant="primary">
                  <template v-slot:header>
                    <div class="text-white">
                      <b-icon icon="info-square-fill" aria-hidden="true"></b-icon> 스터디 소개
                    </div>
                  </template>
                  <b-card-text v-html="team.content"></b-card-text>
                </b-card>
              </b-card-group>
              <br>
              <b-row>
                <b-col v-if="email == team.mgrEmail" class="totheright my-3 text-right" offset="8" cols="4">
                  <b-button router :to="{ name: 'UpdateTeam', params: {id:study_id} }" variant="info" tag="b-button">스터디 수정</b-button>
                  <b-button v-if="isLoggedIn" class="ml-2" @click="$bvModal.show('modal-scoped')" variant="danger">스터디 삭제</b-button>

                  <b-modal
                    id="modal-scoped"
                    ref="modal"
                    title="스터디를 삭제 하시겠습니까?"
                    @show="resetDeleteModal"
                    @hidden="resetDeleteModal"
                    @ok="deleteOk"
                  >
                    <form ref="form" @submit.stop.prevent="deleteSubmit">
                      <b-form-group
                        label="다음 글을 옮겨 적으세요"
                        label-for="delete-input"
                        invalid-feedback="message is required"
                      >
                        
                        <div class="input-group input-group">
                          <p class="form-control text-center" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default">해당 스터디를 삭제하겠습니다.</p>
                        </div>
                        <b-form-input
                          id="delete-input"
                          v-model="checkDelete"
                          required
                          class="text-center"
                        ></b-form-input>
                      </b-form-group>
                    </form>
                    <template v-slot:modal-footer="{ ok, cancel }">
                      <!-- <b>Custom Footer</b> -->
                      <!-- Emulate built in modal footer ok and cancel button actions -->
                      <b-button size="sm" variant="success" @click="ok()">
                        신청
                      </b-button>
                      <b-button size="sm" variant="danger" @click="cancel()">
                        취소
                      </b-button>
                      <!-- Button with custom close trigger value -->
                    </template>
                  </b-modal>
                </b-col>
              </b-row>

              

            </b-container>
            </b-card-text>
          </b-tab>
          
          <b-tab title="스터디원" v-if="isMember">
            <b-card-text>
              <div class="my-2">
                <b-container>
                  <h1>{{ team.title }}의 스터디원</h1>
                  <hr>
                  <b-row>
                    <b-col cols="4" class="p-0">
                      <b-list-group-item style="height:50px;" class="d-flex justify-content-center py-1">
                        <b-button variant="primary">리더</b-button>
                      </b-list-group-item>
                    </b-col>
                    <b-col cols="8" class="p-0">
                      <b-list-group-item style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">
                        {{ team.mgrName }}
                      </b-list-group-item>
                    </b-col>
                  </b-row>
                  <!-- 스터디원 -->
                  <b-row v-for="item in memberList" :key="item.name">
                    <b-col cols="4" class="p-0">
                      <b-list-group-item v-if="item.email != team.mgrEmail" style="height:50px;" class="d-flex justify-content-center py-1">
                        <b-button variant="success" >멤버</b-button>
                      </b-list-group-item>
                    </b-col>
                    <b-col cols="8" class="p-0">
                      <b-list-group-item v-if="item.email != team.mgrEmail" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">
                        {{ item.name }}
                      </b-list-group-item>
                    </b-col>
                  </b-row>
                </b-container>
              </div>
            </b-card-text>
          </b-tab>
          <b-tab title="게시판" v-if="isMember">
            <b-card-text>
              <TeamArticle :key="this.study_id" v-bind:team="team"></TeamArticle>
            </b-card-text>
          </b-tab>
          <b-tab title="탈퇴하기" v-if="isMember && email !== team.mgrEmail" v-b-modal.modal-secession>
            <b-card-text>
              <b-button v-if="isMember" v-b-modal.modal-secession variant="info">탈퇴신청</b-button>
            </b-card-text>
          </b-tab>
          <b-tab title="스터디원 관리" v-if="team.mgrEmail === this.email && isLoggedIn">
            <b-card-text>
              <div class="my-2" v-if="email == team.mgrEmail">
                <b-container>
                  <h1>스터디원 관리</h1>
                  <br>
                  <!-- 스터디 리더 -->
                  <b-row>
                    <b-col cols="8" class="p-0">
                      <b-list-group-item style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">
                        {{ team.mgrName }}
                      </b-list-group-item>
                    </b-col>
                    <b-col cols="4" class="p-0">
                      <b-list-group-item style="height:50px;" class="d-flex justify-content-center py-1">
                        <b-button variant="primary">리더</b-button>
                      </b-list-group-item>
                    </b-col>
                  </b-row>
                  <!-- 스터디원 -->
                  <b-row v-for="item in memberList" :key="item.name">
                    <b-col cols="8" class="p-0">
                      <b-list-group-item v-if="item.email != team.mgrEmail" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">
                        {{ item.name }}
                      </b-list-group-item>
                    </b-col>
                    <b-col cols="4" class="p-0">
                      <b-list-group-item v-if="item.email != team.mgrEmail" style="height:50px;" class="d-flex justify-content-center py-1" @click="banMember(item.email)">
                        <b-button variant="danger" >강퇴</b-button>
                      </b-list-group-item>
                    </b-col>
                  </b-row>
                </b-container>
              </div>
            </b-card-text>
          </b-tab>
          <b-tab title="가입신청자" v-if="team.mgrEmail === this.email && isLoggedIn">
            <b-card-text>
              <div class="my-2" v-if="email == team.mgrEmail">
                <b-container>
                  <h1>가입 신청자</h1>
                  <hr>
                  <b-row>
                    <b-col cols="6" class="p-0"><b-list-group-item >이름</b-list-group-item></b-col>
                    <b-col cols="3" class="p-0"><b-list-group-item >승인</b-list-group-item></b-col>
                    <b-col cols="3" class="p-0"><b-list-group-item >거부</b-list-group-item></b-col>
                  </b-row>
                  <b-row v-for="apply in applyList" :key="apply.name">
                      <b-col cols="6" class="p-0"><b-list-group-item style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; height:50px;">{{ apply.name }}</b-list-group-item></b-col>
                      <b-col cols="3" class="p-0"><b-list-group-item style="height:50px;" class="d-flex justify-content-center py-2"><b-button variant="success" @click="approveMember(apply.email,apply.name)">승인</b-button></b-list-group-item></b-col>
                      <b-col cols="3" class="p-0"><b-list-group-item style="height:50px;" class="d-flex justify-content-center py-2"><b-button variant="danger" @click="disapproveMember(apply.email)">거부</b-button></b-list-group-item></b-col>

                  </b-row>
                </b-container>
              </div>
            </b-card-text>
          </b-tab>
        </b-tabs>
      </b-card>
    </div>
</div>
</template>


<script>
import Axios from 'axios'
import { mapGetters, mapState } from 'vuex'
import TeamArticle from "../../components/common/TeamArticle.vue";
const API_URL = process.env.VUE_APP_LOCAL_URL

export default {
  data() {
    return {
      joinMsg: '',
      study_id: this.$route.params.id,
      team: {},
      checkDelete: '',
      checkDeleteForm: '해당 스터디를 삭제하겠습니다.',
      checkSecessionForm: '해당 스터디를 탈퇴하겠습니다.',
      memberList: [],
      applyList: [],
      isMember: false,
      isReady: false,
      tabIndex: 1,
    }
  },
  computed: {
    ...mapState({
      email: state => state.moduleName.email,
    }),
    ...mapGetters(['isLoggedIn']),
  },
  methods: {
    // 삭제 모달
    deleteOk(bvModalEvt) {
      bvModalEvt.preventDefault()
      this.deleteSubmit()
    },
    deleteSubmit() {
      if (this.checkDelete === this.checkDeleteForm) {
        var params = new URLSearchParams()
        params.append('studyId',this.study_id)
        Axios({method:'DELETE',url:`${API_URL}study`,params:params,headers:{'Content-Type': 'application/json; charset=utf-8','jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),'user-email': sessionStorage.getItem('user-email')}})
        .then(() => {
          alert('스터디가 삭제되었습니다.')
          this.checkDelete = ''
          this.$router.push({ name: "StudyList" })
        })
        .catch(err => {
          console.log(err)
          this.$router.push({ name: "NotFound" })
        })
      } else {
        alert('일치하지 않습니다.')
      }
    },
    resetDeleteModal() {
      this.checkDelete = ''
    },
    // 가입 신청 모달
    resetModal() {
      this.joinMsg = ''
    },
    joinOk(bvModalEvt) {
      // Prevent modal from closing
      bvModalEvt.preventDefault()
      // Trigger submit handler
      this.joinSubmit()
    },
    joinSubmit() {
      const params = {
        email: this.email,
        studyId: this.study_id
      }

      Axios({
        method: "POST",
        url: `${API_URL}study/member/apply`,
        params: params,
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        },
      })
      .then(res => {
        this.isReady = true
        alert(res.data)
      })
      .catch(err => {
        alert(err.response.data)
      })


      this.$nextTick(() => {
        this.$bvModal.hide('modal-prevent-closing')
      })
    },
    toAricle () {
      this.$router.push({ name: "StudyArticle", params: {id:this.study_id}})
    },
    secessionSubmit () {
      if (this.checkDelete === this.checkSecessionForm) {
        Axios({method:'DELETE',url:`${API_URL}study/${this.study_id}/`,headers:{'Content-Type': 'application/json; charset=utf-8','jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),'user-email': sessionStorage.getItem('user-email')}})
        .then(() => {
          this.checkDelete = ''
          this.isMember = false
          this.isReady = false
          this.team.numMembers -= 1
          this.$nextTick(() => {
            this.$bvModal.hide('modal-secession')
          })
          alert('탈퇴가 성공적으로 진행되었습니다.')
        })
        .catch(err => {
          alert(err.response.data)
        })
      } else {
        alert('일치하지 않습니다.')
      }
    },
    secessionOk(bvModalEvt) {
      // Prevent modal from closing
      bvModalEvt.preventDefault()
      // Trigger submit handler
      this.secessionSubmit()
    },

    cancleSubmit () {
      Axios({
        method: "DELETE",
        url: `${API_URL}study/member/disapply`,
        params: {'email':this.email,'studyId':this.study_id},
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        this.isReady = false
        alert('가입 신청을 취소하였습니다.')
      })
      .catch(err => {
        alert(err.response.msg)
      })
    },

    banMember (banEmail) {
      if (banEmail == this.team.mgrEmail) {
        alert('팀장님 탈주하시면 안됩니다')
        return false
      }
      Axios({
        method: "DELETE",
        url: `${API_URL}study/member`,
        params: {'email':banEmail,'studyId':this.study_id},
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        var newMember = []
        for (var i=0; i<this.memberList.length; i++) {
          if (this.memberList[i].email != banEmail) {
            newMember.push(this.memberList[i])
          }
        }
        this.memberList = newMember
        this.team.numMembers -= 1
        alert('성공적으로 탈퇴시켰습니다')
      })
      .catch(err => {
        alert(err.response.data)
      })
    },

    approveMember (approveEmail,approveName) {
      Axios({
        method: "POST",
        url: `${API_URL}study/member/approve`,
        params: {'tgtEmail':approveEmail,'studyId':this.study_id},
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        var newMember = []
        for (var i=0; i<this.applyList.length; i++) {
          if (this.applyList[i].email != approveEmail) {
            newMember.push(this.applyList[i])
          }
        }
        this.applyList = newMember
        this.memberList.push({'name':approveName,'email':approveEmail})
        
        this.team.numMembers += 1
        alert("성공적으로 승인했습니다")
      })
      .catch(err => {
        alert(err.response.data.msg)
      })
    },

    disapproveMember (disEmail) {
      Axios({
        method: "DELETE",
        url: `${API_URL}study/member/disapprove`,
        params: {'email':disEmail,'studyId':this.study_id},
        headers: { 
          "Content-Type": "application/json; charset=utf-8", 
          'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
          'user-email': sessionStorage.getItem('user-email')
        }
      })
      .then(() => {
        var newMember = []
        for (var i=0; i<this.applyList.length; i++) {
          if (this.applyList[i].email != disEmail) {
            newMember.push(this.applyList[i])
          }
        }
        this.applyList = newMember
        alert('성공적으로 거부했습니다')
      })
      .catch(err => {
        alert(err.response.data)
      })
    },


  },
  mounted() {
    // setTimeout(() => {
    //   this.tabIndex = 2;
    // }, 200)
  },
  created() {
    // 스터디 디테일 정보 가져오기
    Axios({
      method: "GET",
      url: `${API_URL}study/${this.study_id}`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.team = res.data
      this.team.content = this.team.content.split('\n').join('<br />')
    })
    .catch(err => {
      console.log(err)
      this.$router.push({ name: "NotFound" })
    })


    if (!this.isLoggedIn) { 
      return false 
    }

    // 해당 스터디의 스터디원 정보 가져오기
    Axios({
      method: "GET",
      url: `${API_URL}study/${this.study_id}/list`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.memberList = res.data
      for (var i=0; i < this.memberList.length; i++) {
        if (this.email === this.memberList[i].email) {
          this.isMember  = true
          break
        }
      }
    })
    .catch(err => {
      alert(err.response.msg)
    })
    // 해당 스터디의 가입 요청한 인원 정보 가져오기
    Axios({
      method: "GET",
      url: `${API_URL}study/${this.study_id}/approvelist`,
      headers: { 
        "Content-Type": "application/json; charset=utf-8", 
        'jwt-auth-token': sessionStorage.getItem('jwt-auth-token'),
        'user-email': sessionStorage.getItem('user-email')
      }
    })
    .then(res => {
      this.applyList = res.data
      for (var i=0; i < this.applyList.length; i++) {
        if (this.email === this.applyList[i].email) {
          this.isReady = true
          break
        }
      }
    })
    .catch(err => {
      alert(err.response.msg)
    })
    
  },
  components: {
    TeamArticle,
  },
}
</script>

<style>
  .totheright{
    text-align: right;
  }
  .totheleft{
    text-align: left;
  }
</style>
