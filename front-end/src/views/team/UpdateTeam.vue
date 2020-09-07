<template>
  <div @submit.prevent="studyUpdate" class="CreateTeamvue">
    <h1 id="CreateTeamTitle">스터디 수정</h1>
    <div id="CreateTeamGrid">
      <!-- 스터디 이름 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">스터디 이름:</span>
        <div>
          <input
            v-model="form.title"
            type="text"
            class="form-control text-center"
            placeholder="스터디 이름을 입력해주세요"
          />
        </div>
      </div>
      <!-- 분야 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">분야:</span>
        <div>
          <b-form-select v-model="form.categoryId" :options="category" required></b-form-select>
        </div>
      </div>
      <!-- 인원 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">인원:</span>
        <div>
          <b-form-spinbutton
            v-model="form.maxMembers"
            min="1"
            max="100"
            required
            placeholder="최대 인원수를 정하세요"
          ></b-form-spinbutton>
        </div>
      </div>
      <!-- 온/오프라인 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">온/오프라인:</span>
        <div>
          <b-form-select v-model="form.placeId" :options="place" required></b-form-select>
        </div>
      </div>
      <!-- 지역 -->
      <div class="CreateTeamControl" id="CreateTeamControl-location">
        <span class="CreateTeamText">지역:</span>
        <div>
          <b-form-select v-model="form.si" :options="siAreas" @change="changeGu" required></b-form-select>
        </div>
        <div>
          <b-form-select v-model="form.gu" :options="guAreas" required></b-form-select>
        </div>
      </div>
      <!-- 기간 -->
      <div class="CreateTeamControl" id="CreateTeamControl-Date">
        <span class="CreateTeamText">기간:</span>
        <div>
          <b-form-datepicker placeholder="시작 기간" v-model="form.startDate"></b-form-datepicker>
        </div>
        <div>
          <b-form-datepicker placeholder="마침 기간" v-model="form.endDate"></b-form-datepicker>
        </div>
      </div>
      <!-- 일정 -->
      <div class="CreateTeamControl" id="CreateTeamControl-Period">
        <span class="CreateTeamText">일정:</span>
        <div>
          <b-form-select v-model="form.periodId" :options="period" required></b-form-select>

          <!-- <b-form-radio-group
            :state="periodState"
            v-model="form.periodId"
            :options="period"
            required
          >
            <b-form-invalid-feedback :state="periodState">한 가지를 선택해주세요.</b-form-invalid-feedback>
            <b-form-valid-feedback :state="periodState">감사합니다.</b-form-valid-feedback>
          </b-form-radio-group>-->
        </div>
        <div>
          <b-form-spinbutton
            v-if="form.periodId === 1"
            class="myspinbutton"
            v-model="form.numMeetings"
            min="1"
            max="1"
            required
            placeholder="횟수를 입력해주세요"
          ></b-form-spinbutton>
          <b-form-spinbutton
            v-if="form.periodId === 2"
            class="myspinbutton"
            v-model="form.numMeetings"
            min="1"
            max="7"
            required
            placeholder="횟수를 입력해주세요"
          ></b-form-spinbutton>
          <b-form-spinbutton
            v-if="form.periodId === 3"
            class="myspinbutton"
            v-model="form.numMeetings"
            min="1"
            max="31"
            required
            placeholder="횟수를 입력해주세요"
          ></b-form-spinbutton>
          <b-form-spinbutton
            v-if="form.periodId === 4"
            class="myspinbutton"
            v-model="form.numMeetings"
            min="0"
            max="0"
            required
            placeholder="횟수를 입력해주세요"
          ></b-form-spinbutton>
          <b-form-spinbutton
            v-if="form.periodId === null"
            class="myspinbutton"
            v-model="form.numMeetings"
            min="0"
            max="0"
            required
            placeholder="횟수를 입력해주세요"
          ></b-form-spinbutton>
        </div>
      </div>
      <!-- 주중/주말 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">주중/주말:</span>
        <div>
          <!-- <b-form-select :state="weekState" v-model="form.weekId" :options="week" required></b-form-select> -->

          <b-form-radio-group v-model="form.weekId" :options="week">
            <b-form-invalid-feedback >한 가지를 선택해주세요.</b-form-invalid-feedback>
            <b-form-valid-feedback >감사합니다.</b-form-valid-feedback>
          </b-form-radio-group>
        </div>
      </div>
      <!-- 시간대 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">시간대:</span>
        <div>
          <!-- <b-form-select :state="shiftState" v-model="form.shiftId" :options="shift" required></b-form-select> -->

          <b-form-radio-group v-model="form.shiftId" :options="shift">
            <b-form-invalid-feedback >한 가지를 선택해주세요.</b-form-invalid-feedback>
            <b-form-valid-feedback >감사합니다.</b-form-valid-feedback>
          </b-form-radio-group>
        </div>
      </div>
      <!-- 소개글 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">소개글:</span>
        <div>
          <b-form-textarea
            v-model="form.content"
            placeholder="스터디에 관련된 소개글을 입력해주세요"
            rows="5"
            no-resize
          ></b-form-textarea>
        </div>
      </div>
      <!-- 생성버튼 -->
      <div id="CreateButton">
        <b-button
          id="button"
          type="submit"
          v-b-modal.modal-prevent-closing
          block
          size="lg"
          variant="info"
          @click="studyUpdate"
        >수정하기</b-button>
      </div>
    </div>
  </div>
</template>

<script>
// import VueSlider from "vue-slider-component";
// import "vue-slider-component/theme/default.css";
import { mapActions, mapState } from "vuex";
import Axios from "axios";
import router from "@/router";

const API_URL = process.env.VUE_APP_LOCAL_URL;

export default {
  data() {
    return {
      form: {
        studyId: this.$route.params.id,
        categoryId: null,
        content: null,
        endDate: null,
        gu: null,
        maxMembers: null,
        periodId: null,
        placeId: null,
        shiftId: null,
        si: null,
        startDate: null,
        title: null,
        weekId: null,
        numMeetings: null,
      },
      category: [
        { text: "원하는 분야를 선택해주세요", value: null },
        { text: "면접", value: 1 },
        { text: "인적성/NCS", value: 2 },
        { text: "코딩 테스트", value: 3 },
        { text: "기타", value: 4 },
      ],
      siAreas: [{ text: "시를 선택해주세요", value: null }],
      guAreas: [{ text: "구를 선택해주세요" }],
      place: [
        { text: "온/오프라인을 선택해주세요", value: null },
        { text: "온라인", value: 1 },
        { text: "오프라인", value: 2 },
        { text: "추후협의", value: 3 },
      ],
      period: [
        { text: "매일", value: 1 },
        { text: "매주", value: 2 },
        { text: "매월", value: 3 },
        { text: "추후협의", value: 4 },
      ],
      week: [
        { text: "평일", value: 1 },
        { text: "주말", value: 2 },
        { text: "혼합", value: 3 },
        { text: "추후협의", value: 4 },
      ],
      shift: [
        { text: "오전", value: 1 },
        { text: "오후", value: 2 },
        { text: "야간", value: 3 },
        { text: "추후협의", value: 4 },
      ],
    };
  },
  computed: {
    ...mapState({
      email: (state) => state.moduleName.email,
    }),
  },
  created() {
    // @@시 가져오기
    Axios.get(`${API_URL}address/`)
      .then((res) => {
        this.siAreas.push(res.data[0]);
      })
      .catch((err) => {
        console.log(err);
      });
    // 스터디 정보 가져오기
    Axios.get(`${API_URL}study/${this.form.studyId}`, {
      headers: {
        "jwt-auth-token": sessionStorage.getItem("jwt-auth-token"),
        "user-email": sessionStorage.getItem("user-email"),
      },
    })
      .then((res) => {
        if (this.email != res.data.mgrEmail) {
          alert("팀 매니저가 아닙니다");
          this.$router.push({ name: "Home" });
        }
        this.form.periodId = null;
        this.form.weekId = null;
        this.form.endDate = res.data.endDate;
        this.form.shiftId = null;
        this.form.numMeetings = res.data.numMeetings;
        this.form.title = res.data.title;
        this.form.content = res.data.content;
        this.form.gu = res.data.gu;
        this.form.numMembers = res.data.numMembers;
        this.form.maxMembers = res.data.maxMembers;
        this.form.si = res.data.si;
        this.form.studyId = this.$route.params.id;
        this.form.placeId = null;
        this.form.categoryId = null;
        this.form.startDate = res.data.startDate;

        for (var a = 0; a < this.period.length; a++) {
          if (this.period[a].text === res.data.period) {
            this.form.periodId = this.period[a].value;
          }
        }
        for (var b = 0; b < this.week.length; b++) {
          if (this.week[b].text === res.data.week) {
            this.form.weekId = this.week[b].value;
          }
        }
        for (var c = 0; c < this.shift.length; c++) {
          if (this.shift[c].text === res.data.shift) {
            this.form.shiftId = this.shift[c].value;
          }
        }
        for (var i = 0; i < this.category.length; i++) {
          if (this.category[i].text === res.data.category) {
            this.form.categoryId = this.category[i].value;
            break;
          }
        }
        for (var j = 0; j < this.place.length; j++) {
          if (this.place[j].text === res.data.place) {
            this.form.placeId = this.place[j].value;
            break;
          }
        }
        for (var k = 0; k < this.place.length; k++) {
          if (this.place[k].text === res.data.place) {
            this.form.placeId = this.place[k].value;
            break;
          }
        }

        this.changeGu();
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    ...mapActions(["createTeam"]),
    studyUpdate() {
      const params = {
        categoryId: this.form.categoryId,
        content: this.form.content,
        endDate: this.form.endDate,
        gu: this.form.gu,
        maxMembers: this.form.maxMembers,
        numMeetings: this.form.numMeetings,
        periodId: this.form.periodId,
        placeId: this.form.placeId,
        shiftId: this.form.shiftId,
        si: this.form.si,
        startDate: this.form.startDate,
        studyId: this.form.studyId,
        title: this.form.title,
        weekId: this.form.weekId,
      };
      
      const JsonParams = JSON.stringify(params);
      Axios({
        method: "PUT",
        url: `${API_URL}study`,
        params: params,
        data: JsonParams,
        headers: {
          "Content-Type": "application/json; charset=utf-8",
          "jwt-auth-token": sessionStorage.getItem("jwt-auth-token"),
          "user-email": sessionStorage.getItem("user-email"),
        },
      })
        .then(() => {
          alert("스터디 수정 성공");
          router.push({
            name: "StudyDetail",
            params: { id: this.form.studyId },
          });
        })
        .catch((err) => {
          console.log(err.response.data);
        });
    },

    changeGu() {
      Axios.get(`${API_URL}address/${this.form.si}`)
        .then((res) => {
          this.guAreas = res.data;
        })
        .catch((err) => {
          (this.guAreas = [{ text: "구를 선택해주세요" }]), console.log(err);
        });
    },
  },
};
</script>

<style></style>
