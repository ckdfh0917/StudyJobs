<script src="https://kit.fontawesome.com/958a5175c9.js" crossorigin="anonymous"></script>
<template>
  <div class="EditPasswordvue">
    <h1 id="EditPasswordTitle">비밀번호 변경</h1>
    <div id="EditPasswordGrid">
      <!-- 현재 비밀번호 -->
      <div class="PasswordEditControl">
        <span class="PasswordEditText">현재 비밀번호 :</span>
        <div>
          <b-input-group>
            <b-input-group-prepend>
              <b-input-group-text style="background-color:white; border-right:none;">
                <b-icon icon="lock" />
              </b-input-group-text>
            </b-input-group-prepend>

            <b-form-input
              style="border-left:0"
              v-model="nowPassword"
              type="password"
              class="form-control text-center"
              aria-label="Sizing example input"
              aria-describedby="inputGroup-sizing-default"
              placeholder="현재 비밀번호를 입력해 주세요"
            ></b-form-input>

            <b-input-group-append>
              <b-button @click="verifyPassword" variant="outline-secondary">확인</b-button>
            </b-input-group-append>
          </b-input-group>
        </div>
      </div>
      <!-- 새 비밀번호 -->
      <div @submit.stop.prevent="editPassword">
        <div class="PasswordEditControl" id="newPassword">
          <span class="PasswordEditText">새 비밀번호 :</span>
          <div>
            <b-input-group>
              <b-form-input
                v-model="newPassword"
                type="password"
                class="form-control text-center"
                aria-label="Sizing example input"
                aria-describedby="inputGroup-sizing-default"
                placeholder="새로운 비밀번호를 입력해 주세요"
              ></b-form-input>
            </b-input-group>
          </div>
        </div>
        <!-- 새 비밀번호 확인 -->
        <div class="PasswordEditControl" id="newConfirm">
          <span class="PasswordEditText">새 비밀번호 확인 :</span>
          <div>
            <b-input-group>
              <b-form-input
                v-model="newPasswordConfirm"
                type="password"
                class="form-control text-center"
                aria-label="Sizing example input"
                aria-describedby="inputGroup-sizing-default"
                placeholder="새로운 비밀번호를 확인해 주세요"
              ></b-form-input>
            </b-input-group>
          </div>
        </div>
        <div class="WarningText">
          <span>
            비밀번호는 영문과 숫자가 적어도 1자 이상씩 포함된
            8자 이상으로 구성되어야 합니다.
          </span>
        </div>
        <div id="buttonbox">
          <b-button id="button" @click="editPassword">변경하기</b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import Axios from "axios";
import router from "@/router";
const API_URL = process.env.VUE_APP_LOCAL_URL;

export default {
  data() {
    return {
      nowPasswordVerifyState: false,
      nowPassword: "",
      newPassword: "",
      newPasswordConfirm: "",
      sex: "",
      name: "",
      age: null,
    };
  },
  computed: {
    ...mapState({
      email: (state) => state.moduleName.email,
    }),
  },
  methods: {
    verifyPassword() {
      const params = {
        email: this.email,
        password: this.nowPassword,
      };
      var JsonForm = JSON.stringify(params);

      Axios({
        method: "POST",
        url: `${API_URL}login`,
        params: params,
        data: JsonForm,
        headers: {
          "Content-Type": "application/json; charset=utf-8",
        },
      })
        .then(() => {
          alert("비밀번호 인증되었습니다.");
          this.nowPasswordVerifyState = true;

          //get 요쳥
          Axios.get(`${API_URL}user/${this.email}`, {
            headers: {
              "jwt-auth-token": sessionStorage.getItem("jwt-auth-token"),
              "user-email": sessionStorage.getItem("user-email"),
            },
          })
            .then((res) => {
              this.name = res.data.name;
              this.age = res.data.age;
              this.sex = res.data.sex;
            })
            .catch((err) => {
              console.log(err);
            });
        })
        .catch((err) => {
          alert("비밀번호를 확인해주세요");
          console.log(err.response.data);
        });
    },
    editPassword() {
      if (this.newPassword !== this.newPasswordConfirm) {
        alert("비밀번호 확인과 비밀번호가 다릅니다!");
        return false;
      }

      if (!this.nowPasswordVerifyState) {
        alert("현재 비밀번호 확인을 해주세요!");
        return false;
      }

      if (this.newPassword === this.nowPassword) {
        alert("현재 비밀번호와 새 비밀번호가 같습니다.");
        return false;
      }

      const params = {
        name: this.name,
        sex: this.sex,
        age: this.age,
        email: this.email,
        password: this.newPassword,
      };

      Axios.put(`${API_URL}user`, params, {
        headers: {
          "jwt-auth-token": sessionStorage.getItem("jwt-auth-token"),
          "user-email": sessionStorage.getItem("user-email"),
        },
      })
        .then(() => {
          alert("비밀번호 수정을 완료하였습니다.");

          router.push({ name: "Mypage" });
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
  
<style>
.EditPasswordvue {
  text-align: center;
}
#EditPasswordTitle {
  text-align: center;
  margin-top: 50px;
  margin-left: 100px;
}
#EditPasswordGrid {
  max-width: 800px;
  margin: auto;
  display: block;
  padding: 30px;
}
.PasswordEditControl {
  display: grid;
  grid-template-columns: 200px 1fr;
  padding: 10px;
  margin: auto;
}
.PasswordEditText {
  text-align: right;
  font-size: 18px;
  font-family: "Franklin Gothic Medium", "Arial Narrow", Arial, sans-serif;
  font-weight: 900;
  margin-right: 25px;
  margin-top: 4px;
}
#newPassword {
  padding-top: 20px;
  padding-bottom: 0;
}
#newConfirm {
  padding-top: 0;
  padding-bottom: 2px;
  margin-top: 2px;
}
.WarningText {
  margin-left: 210px;
  margin-top: 5px;
  max-width: 600px;
  text-align: left;
  font-size: 12px;
  font-weight: 700;
  color: red;
}
#buttonbox {
  margin-top: 50px;
  margin-left: 100px;
  /* text-align: right; */
}
#button {
  background-color: rgb(25, 84, 241);
  color: white;
}
</style>