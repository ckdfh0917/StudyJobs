<template>
  <b-container class="rounded p-2 grow" style="cursor:pointer;" @click="clickCard">
    <div class="card">
      <div v-if="team.category === '코딩 테스트'" class="mycard-header1 text-left pt-2">
        <b-row>
          <b-col>
            <b-icon icon="person-circle" font-scale="1"></b-icon> {{ team.numMembers }}/{{ team.maxMembers }}
          </b-col>
          <b-col class="text-right font-weight-bold">{{ team.category }}</b-col>
        </b-row>
      </div>
      <div v-if="team.category === '인적성/NCS'" class="mycard-header2 text-left pt-2">
        <b-row>
          <b-col>
            <b-icon icon="person-circle" font-scale="1"></b-icon> {{ team.numMembers }}/{{ team.maxMembers }}
          </b-col>
          <b-col class="text-right font-weight-bold">{{ team.category }}</b-col>
        </b-row>
      </div>
      <div v-if="team.category === '면접'" class="mycard-header3 text-left pt-2">
        <b-row>
          <b-col>
            <b-icon icon="person-circle" font-scale="1"></b-icon> {{ team.numMembers }}/{{ team.maxMembers }}
          </b-col>
          <b-col class="text-right font-weight-bold">{{ team.category }}</b-col>
        </b-row>
      </div>
      <div v-if="team.category === '기타'" class="mycard-header4 text-left pt-2">
        <b-row>
          <b-col>
            <b-icon icon="person-circle" font-scale="1"></b-icon> {{ team.numMembers }}/{{ team.maxMembers }}
          </b-col>
          <b-col class="text-right font-weight-bold">{{ team.category }}</b-col>
        </b-row>
      </div>
      <div class="card-body text-center">
        <img :id="team.studyId" :src="defaultImageUrl" style="width:140px; height:140px; margin-top:-85px" alt="User" class="studyimg img-fluid img-thumbnail rounded-circle border-0 mb-2">
        <b-row class="my-1">
          <b-col><h5 class="font-weight-bold">{{ team.title }}</h5></b-col>
        </b-row>
        <b-row class="my-1">
          <b-col md="1" offset-md="1"><b-icon icon="building" font-scale="1"></b-icon></b-col>
          <b-col md="8" offset-md="1" class="text-left"><b-badge pill variant="secondary" class="studyCard">{{ team.si }} {{ team.gu }}</b-badge></b-col>
        </b-row>
        <b-row class="my-1">
          <b-col md="1" offset-md="1"><b-icon icon="calendar2-check-fill" font-scale="1"></b-icon></b-col>
          <b-col md="8" offset-md="1" class="text-left"> <b-badge pill variant="secondary" class="studyCard">{{ team.period }} / {{ team.numMeetings }}회</b-badge></b-col>
        </b-row>
        <b-row class="my-1">
          <b-col md="1" offset-md="1"><b-icon icon="tv-fill" font-scale="1"></b-icon></b-col>
          <b-col md="8" offset-md="1" class="text-left"><b-badge pill variant="secondary"  class="studyCard">{{ team.place }}</b-badge></b-col>
        </b-row>
      </div>
      <!-- <div class="card-footer">
        <button class="btn btn-light btn-sm bg-white has-icon btn-block" type="button"><i class="material-icons">add</i>Follow</button>
        <button class="btn btn-light btn-sm bg-white has-icon ml-2" type="button"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-message-circle"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"></path></svg></button>
      </div> -->
    </div>
  </b-container>
</template>

<script>
import router from "@/router";
import firebase from 'firebase'

export default {
  data () {
    return {
      defaultImageUrl: 'https://previews.123rf.com/images/salamatik/salamatik1801/salamatik180100019/92979836-%ED%94%84%EB%A1%9C%ED%95%84-%EC%9D%B5%EB%AA%85%EC%9D%98-%EC%96%BC%EA%B5%B4-%EC%95%84%EC%9D%B4%EC%BD%98-%ED%9A%8C%EC%83%89-%EC%8B%A4%EB%A3%A8%EC%97%A3-%EC%82%AC%EB%9E%8C%EC%9E%85%EB%8B%88%EB%8B%A4-%EB%82%A8%EC%84%B1-%EA%B8%B0%EB%B3%B8-%EC%95%84%EB%B0%94%ED%83%80-%EC%82%AC%EC%A7%84-%EC%9E%90%EB%A6%AC-%ED%91%9C%EC%8B%9C-%EC%9E%90-%ED%9D%B0%EC%83%89-%EB%B0%B0%EA%B2%BD%EC%97%90-%EA%B3%A0%EB%A6%BD-%EB%B2%A1%ED%84%B0-%EC%9D%BC%EB%9F%AC%EC%8A%A4%ED%8A%B8-%EB%A0%88%EC%9D%B4-%EC%85%98.jpg',
      profileEmail: null,
    }
  },
  props: {
    team: Object,
  },
  created() {
    
      var storageRef = firebase.storage().ref();
      //
      var listRef = storageRef.child(`images/${this.team.mgrEmail}`);
      
      const studyId = this.team.studyId
      // Find all the prefixes and items.
      listRef.listAll().then(function(res) {
        res.items.forEach(function(itemRef) {
          const fullPath = itemRef.fullPath
          firebase.storage().ref(`${fullPath}`).getDownloadURL()
          .then(function(url) {
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            xhr.onload = function() {};
            xhr.open('GET', url);
            xhr.send();
            var img = document.getElementById(studyId);
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

  },
  computed: {},
  methods: {
    clickCard() {
      router.push({ name: "StudyDetail", params: { id: this.team.studyId } });
    },
  },
};
</script>

<style>
.studyCard {
  padding-right: 10px !important;
  width: 150px;
}

.mycard-header1 {
	background-color: rgb(46, 204, 113);
	padding: 5px 10px;
  height: 120px;
  cursor:pointer;
}
.mycard-header2 {
	background-color: rgb(242, 142, 240);
	padding: 5px 10px;
  height: 120px;
  cursor:pointer;
}
.mycard-header3 {
	background-color: rgb(243, 156, 18);
	padding: 5px 10px;
  height: 120px;
  cursor:pointer;
}
.mycard-header4 {
	background-color: #00BFFF;
	padding: 5px 10px;
  height: 120px;
  cursor:pointer;
}

.card-body {
  z-index: 1;
}

.grow { 
transition: all .2s ease-in-out; 
}

.grow:hover { 
transform: scale(1.05); 
}
</style>
