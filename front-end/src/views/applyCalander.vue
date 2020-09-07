<template>
  <div>
    <b-container class="my-3">
      <b-row>
        <b-form-select v-model="volumn" :options="volumnOptions" @change="changeCalander" class="col-6"></b-form-select>
        <b-form-select v-model="selected" :options="options" @change="changeCalander" class="col-6"></b-form-select>
    <iframe id="pageFrame" name="pageFrame" class="col-12 my-3" src="http://www.jobkorea.co.kr/Starter/calendar/sub/month" frameborder="0"
    height="1500px" scrolling="auto"></iframe>
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  data () {
    return {
      volumn: 'month',
      selected: '',
      options: [
        { value: null, text: '어떤 학력 채용을 보고 싶으신가요' },
        { value: '', text: '전체' },
        { value: '?edu=3', text: '고졸' },
        { value: '?edu=4', text: '초대졸' },
        { value: '?edu=5', text: '대졸' }
      ],
      volumnOptions: [
        { value: null, text: '달력형태를 선택해 주세요'},
        { value: 'month', text: '한달씩 보기'},
        { value: 'week', text: '주간으로 보기'}
      ]
    }
  },
  methods: {
    changeCalander () {
      const calendar = document.getElementById('pageFrame')
      if ( this.volumn == null ) { this.volumn = 'month' }
      if ( this.selected == null ) { this.selected = '' }
      calendar.src = `http://www.jobkorea.co.kr/Starter/calendar/sub/${this.volumn}${this.selected}`
    }
  }
}
</script>

<style>

</style>