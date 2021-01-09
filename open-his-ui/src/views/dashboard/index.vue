<template>
  <div class="dashboard-container">
    <!-- <component :is="currentRole" /> -->
    <div class="bg" />
  </div>
</template>

<script>
// import adminDashboard from './admin'
// import editorDashboard from './editor'

// export default {
//   name: 'Dashboard',
//   components: { adminDashboard, editorDashboard },
//   data() {
//     return {
//       currentRole: 'adminDashboard'
//     }
//   }
// }
</script>

<style  scoped>
  .bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: url("../../assets/dashboard.jpg") no-repeat;
    background-size: cover;
  }
</style>
