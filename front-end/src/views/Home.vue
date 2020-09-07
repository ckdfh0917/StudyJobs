<template>
  <div class="home">
    <img
      class="d-block img-fluid"
      style="width:100%; height:90vh;"
      src="../assets/second.jpg"
      alt="image slot"
    />
    <br>
    <div id="bot-div">
      <b-container class="bv-example-row">
        <b-row>
          <h2 class="col-12">지금도 {{TeamCnt}}개의 스터디 팀이 활동하는 중!</h2>
          <h2 class="col-12">최근 만들어진 팀들</h2>
          <Teamlistitem
            class="col-4"
            v-for="team in TeamList"
            :key="team.studyId"
            v-bind:team="team"
          ></Teamlistitem>
        </b-row>
      </b-container>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import Teamlistitem from "../components/TeamlistItem.vue";
// 팀 리스트 조회 뜨면 밑에 주석 제거 하고 axios과정 추가
import Axios from "axios";
const API_URL = process.env.VUE_APP_LOCAL_URL;
export default {
  name: "Home",
  components: {
    Teamlistitem,
  },
  methods: {
    scrollMeTo(refName) {
      var element = this.$refs[refName];
      var top = element.offsetTop;
      window.focus();
      window.scrollTo({ top: top, left: 0, behavior: "smooth" });
    },
    onSlideStart() {
      this.sliding = true;
    },
    onSlideEnd() {
      this.sliding = false;
    },
  },
  mounted() {},
  data() {
    return {
      TeamList: [],
      TeamCnt: 0,
      slide: 0,
      sliding: null,
    };
  },
  created() {
    Axios.get(`${API_URL}study/all`)
      .then((res) => {
        this.TeamList = res.data.reverse();
        this.TeamCnt = this.TeamList.length;
        if (this.TeamList.length >= 6) {
          this.TeamList = this.TeamList.slice(0, 6);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>
