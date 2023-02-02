<template>
  <div id="userLayout" :class="['user-layout-wrapper', isMobile && 'mobile']">
    <background />
    <div class="container">
      <div class="user-layout-lang">
        <select-lang class="select-lang-trigger" />
      </div>
      <div class="login_wrap">
        <div class="login_left">
          <span class="circle">
            <img src="~@/assets/logo.svg" class="logo" alt="logo">
          </span>
          <span class="star">
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
          </span>
          <span class="fly_star">
            <span></span>
            <span></span>
          </span>
          <p id="title">
            {{ $t('layouts.userLayout.title') }}
          </p>
        </div>
        <div class="login_right">
          <div class="header">
            <img src="~@/assets/logo.svg" class="logo" alt="logo">
            <span class="title">GitEgg Cloud</span>
          </div>
          <router-view />
        </div>
        <div style="clear:both"></div>
      </div>
      <div class="footer">
        <div class="links">
          <a href="_self">帮助</a>
          <a href="_self">隐私</a>
          <a href="_self">条款</a>
          <a href="_self">友链</a>
          <a href="_self">声明</a>
          <a href="_self">授权</a>
        </div>
        <div class="copyright">
          Copyrights &copy; 2021 GitEgg All Rights Reserved.   苏ICP备15017208号-1
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { deviceMixin } from '@/store/device-mixin'
import SelectLang from '@/components/SelectLang'
import Background from '@/components/Background'

export default {
  name: 'UserLayout',
  components: {
    SelectLang,
    Background
  },
  mixins: [deviceMixin],
  mounted () {
    document.body.classList.add('userLayout')
  },
  beforeDestroy () {
    document.body.classList.remove('userLayout')
  }
}
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
    height: 100%;
    &.mobile {
      .container {
        .main {
          max-width: 368px;
          width: 98%;
        }
      }
    }

    .user-layout-lang {
      width: 100%;
      height: 40px;
      line-height: 44px;
      text-align: right;

      .select-lang-trigger {
        cursor: pointer;
        padding: 12px;
        margin-right: 24px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        vertical-align: middle;
      }
    }

    .container {
      width: 100%;
      min-height: 100%;
      background-size: 100%;
      position: relative;
      a {
        text-decoration: none;
      }

      // 响应式登录框 start
      .login_wrap{
          width: 980px;
          min-height: 450px;
          border-radius: 2px;
          font-family: "neo";
          overflow: hidden;
          -webkit-box-shadow: rgba(43, 190, 210,.4) 0px 0px 10px 1px;
          background: linear-gradient(45deg, #13607d, #196382);
          // box-shadow: -4px 5px 10px rgba(43, 190, 210,.4);
          box-shadow: rgba(43, 190, 210,.4) 0px 0px 10px 1px;
          position: fixed;
          top: 50%;
          right: 50%;
          margin-top: -250px;
          margin-right: -490px;
      }

      .login_right{
          width: 38%;
          height: 100%;
          min-height: 450px;
          position: relative;
          float: right;

          .header {
              display: none;
              position: absolute;
              margin-top: 10px;
              height: 40px;
              line-height: 40px;
              width: 100%;
              text-align: center;

              .logo {
                position: relative;
                height: 44px;
                vertical-align: top;
                margin-right: 16px;
                border-style: none;
              }

              .title {
                position: relative;
                font-size: 33px;
                color: rgba(255, 255, 255, 1);
                font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
                font-weight: 600;
                top: 2px;
              }
          }

          .main {
            height: 100%;
            min-height: 450px;
            min-width: 260px;
            width: 373px;
          }
      }

      .login_right #status{
          width: 90px;
          height: 35px;
          margin: 40px auto;
          color: #fff;
          font-size: 30px;
          font-weight: 600;
          position: relative;
          overflow: hidden;
      }

      .login_right #status i{
          font-style: normal;
          position: absolute;
          transition: .5s
      }

      .login_right span a{
          text-decoration: none;
          color: #fff;
          display: block;
          margin-top: 80px;
          font-size: 18px;
      }

      .login_left{
          width: 62%;
          min-height: 450px;
          float: left;
          position: relative;
          background-size: 100% 100%;
          opacity: 0.9;

          span{
              position: absolute;
              display: block;
          }
          .circle{
              width: 100px;
              height: 100px;
              border-radius: 17%;
              box-shadow: rgba(129, 176, 40,.8) 0px 0px 10px 1px;
              top: 30%;
              left: 40%;
              overflow: hidden;
              .logo {
                position: relative;
                width: 110px;
                height: 110px;
                margin-left: -5px;
                margin-top: -5px;
                vertical-align: top;
                margin-right: 16px;
                border-style: none;
              }
          }
          .circle span{
              width: 150px;
              height: 40px;
              border-radius: 50px;
              position: absolute;
          }
          .circle span:nth-child(1){
              top: 30px;
              left: -38px;
              background: #fff;
          }
          .circle span:nth-child(2){
              bottom: 20px;
              right: -35px;
              background: #fff;
          }
          .star span{
              background: radial-gradient(#fff 10%,#fff 20%,rgba(72, 34, 64, 0));
              border-radius: 50%;
              box-shadow: 0 0 7px #fff;
          }
          .star span:nth-child(1){
              width: 15px;
              height: 15px;
              top: 50px;
              left: 30px;
          }
          .star span:nth-child(2){
              width: 10px;
              height: 10px;
              left: 360px;
              top: 80px;
          }
          .star span:nth-child(3){
              width: 5px;
              height: 5px;
              top: 400px;
              left: 80px;
          }
          .star span:nth-child(4){
              width: 8px;
              height: 8px;
              top: 240px;
              left: 60px;
          }
          .star span:nth-child(5){
              width: 4px;
              height: 4px;
              top: 20px;
              left: 200px;
          }
          .star span:nth-child(6){
              width: 4px;
              height: 4px;
              top: 460px;
              left: 410px;
          }
          .star span:nth-child(7){
              width: 6px;
              height: 6px;
              top: 250px;
              left: 350px;
          }
          .fly_star span{
              width: 90px;
              height: 3px;
              background: -webkit-linear-gradient(left, rgba(255, 255, 255, 0.67), rgba(255,255,255,0));
              background: -o-linear-gradient(left, rgba(255, 255, 255, 0.67), rgba(255,255,255,0));
              background: linear-gradient(to right, rgba(255, 255, 255, 0.67), rgba(255,255,255,0));
              transform: rotate(-45deg);
          }
          .fly_star span:nth-child(1){
              top:60px;
              left: 80px;
          }
          .fly_star span:nth-child(2){
              top: 210px;
              left: 332px;
              opacity: 0.6;
          }
          p{
              text-align: center;
              color: #ffffff;
              font-weight: 600;
              margin-top: 350px;
              font-size: 25px;
          }
          p i{
              font-style: normal;
              margin-right: 45px;
          }
          p i:nth-last-child(1){
              margin-right: 0;
          }
       }

      @media screen and (max-width:1000px ) {
          .login_left{
              display: none;
          }
          .login_wrap{
              width: 490px;
              margin-right: -245px;
          }
          .login_right{
              width: 100%;
              .header {
                display: block;
              }
              .main {
                height: 100%;
                min-height: 450px;
                min-width: 100%;
                width: 100%;
              }
          }
      }
      @media screen and (max-width:560px ) {
          .login_wrap{
              width: 330px;
              margin-right: -165px;
          }
          .login_right{
              .header {
                line-height: 35px;
                .logo {
                  height: 35px;
                }
                .title{
                  font-size: 25px;
                  font-weight: 450;
                }
              }
          }
          .form input{
              width: 250px;
          }
          .btn{
              width: 113px;
          }
      }
      @media screen and (max-width:345px ) {
          .login_wrap {
              width: 290px;
              margin-right: -145px;
          }
      }
      // 响应式登录框 end

      .footer {
        position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;
          a {
            color: rgba(255, 255, 255, 0.85);
            transition: all 0.3s;
            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }
        .copyright {
          color: rgba(255, 255, 255, 0.85);
          font-size: 14px;
        }
      }
    }
  }
</style>
