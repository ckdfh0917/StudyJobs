<template>
  <div @submit.prevent="studyCreate" class="CreateTeamvue">
    <h1 id="CreateTeamTitle">스터디 생성</h1>
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
          <b-form-select :state="periodState" v-model="form.periodId" :options="period" required></b-form-select>

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

          <b-form-radio-group :state="weekState" v-model="form.weekId" :options="week">
            <b-form-invalid-feedback :state="weekState">한 가지를 선택해주세요.</b-form-invalid-feedback>
            <b-form-valid-feedback :state="weekState">감사합니다.</b-form-valid-feedback>
          </b-form-radio-group>
        </div>
      </div>
      <!-- 시간대 -->
      <div class="CreateTeamControl">
        <span class="CreateTeamText">시간대:</span>
        <div>
          <!-- <b-form-select :state="shiftState" v-model="form.shiftId" :options="shift" required></b-form-select> -->

          <b-form-radio-group :state="shiftState" v-model="form.shiftId" :options="shift">
            <b-form-invalid-feedback :state="shiftState">한 가지를 선택해주세요.</b-form-invalid-feedback>
            <b-form-valid-feedback :state="shiftState">감사합니다.</b-form-valid-feedback>
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
          @click="studyCreate"
        >생성하기</b-button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import Axios from "axios";
import router from "@/router";

const API_URL = process.env.VUE_APP_LOCAL_URL;

export default {
  components: {},
  data() {
    return {
      form: {
        email: this.email,
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
        { text: "추후협희", value: 4 },
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
    periodState() {
      return Boolean(this.form.periodId);
    },
    weekState() {
      return Boolean(this.form.weekId);
    },
    shiftState() {
      return Boolean(this.form.shiftId);
    },
  },
  methods: {
    ...mapActions(["createTeam"]),
    studyCreate() {
      const params = {
        categoryId: this.form.categoryId,
        content: this.form.content,
        endDate: this.form.endDate,
        gu: this.form.gu,
        maxMembers: this.form.maxMembers,
        periodId: this.form.periodId,
        placeId: this.form.placeId,
        shiftId: this.form.shiftId,
        si: this.form.si[0],
        startDate: this.form.startDate,
        title: this.form.title,
        numMeetings: this.form.numMeetings,
        weekId: this.form.weekId,
      };
      

      const JsonParams = JSON.stringify(params);
      Axios({
        method: "POST",
        url: `${API_URL}study`,
        params: params,
        data: JsonParams,
        headers: {
          "Content-Type": "application/json; charset=utf-8",
          "jwt-auth-token": sessionStorage.getItem("jwt-auth-token"),
          "user-email": sessionStorage.getItem("user-email"),
        },
      })
        .then((res) => {
          alert(res.data)
          router.push({ name: "StudyList" });
        })
        .catch((err) => {
          // alert(err.response.data);
          console.log(err);
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
  created() {
    Axios.get(`${API_URL}address/`)
      .then((res) => {
        this.siAreas.push(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>

<style>
.CreateTeamvue {
  text-align: center;
}
#CreateTeamTitle {
  text-align: center;
  margin-top: 50px;
  margin-left: 100px;
  font-weight: 800;
}
#CreateTeamGrid {
  max-width: 800px;
  margin: auto;
  display: block;
  padding: 30px;
}
.CreateTeamControl {
  display: grid;
  grid-template-columns: 200px 1fr;
  padding: 10px;
  margin: auto;
}
#CreateTeamControl-location {
  grid-template-columns: 200px 0.5fr 0.5fr;
}
#CreateTeamControl-Date {
  grid-template-columns: 200px 0.5fr 0.5fr;
}
#CreateTeamControl-Period {
  grid-template-columns: 200px 0.5fr 0.5fr;
}
.CreateTeamText {
  text-align: right;
  font-size: 18px;
  font-family: "Franklin Gothic Medium", "Arial Narrow", Arial, sans-serif;
  font-weight: 900;
  margin-right: 25px;
  margin-top: 4px;
}
#CreateButton {
  margin-top: 50px;
  margin-left: 200px;
  text-align: right;
}
#button {
  background-color: rgb(25, 84, 241);
  color: white;
}
</style>
