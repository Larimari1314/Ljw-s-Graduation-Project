<template>
  <div class="sidebar">
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="sidebar.collapse" background-color="#324157"
             text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.index">
            <template #title>
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                <template #title>{{ subItem.title }}</template>
                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.index">{{ subItem.title }}
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.index">
            <i :class="item.icon"></i>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import {computed} from "vue";
import {useSidebarStore} from '../store/sidebar'
import {useRoute} from "vue-router";

export default {
  setup() {
    const items = [
      {
        icon: "el-icon-lx-home",
        index: "/dashboard",
        title: "系统首页",
      },{
        icon: "el-icon-setting",
        index: "/user",
        title: "个人中心",
      }
    ];

    const route = useRoute();
    const onRoutes = computed(() => {
      return route.path;
    });

    const sidebar = useSidebarStore();

    return {
      items,
      onRoutes,
      sidebar,
    };
  },created() {
  //  根据医生所在部门动态显示界面
    let doctor = JSON.parse(localStorage.getItem("doctor"));
    if(doctor.did==='DE006'){
      this.items=[
        {
          icon: "el-icon-lx-home",
          index: "/dashboard",
          title: "系统首页",
        }, {
          icon: "el-icon-goods",
          index: "/medicalTechnologyCurrentAppointment",
          title: "患者医技就诊",
        }, {
          icon: "el-icon-setting",
          index: "/user",
          title: "个人中心",
        },
        {
          icon: "el-icon-lx-calendar",
          index: "3",
          title: "预约相关",
          subs: [
            {
              index: "/medicalTechnologyHistoryAppointment",
              title: "历史预约",
            }
          ]
        }
      ]
    }else{
      this.items=[
        {
          icon: "el-icon-lx-home",
          index: "/dashboard",
          title: "系统首页",
        }, {
          icon: "el-icon-goods",
          index: "/currentAppointment",
          title: "患者就诊",
        }, {
          icon: "el-icon-setting",
          index: "/user",
          title: "个人中心",
        },
        {
          icon: "el-icon-lx-calendar",
          index: "3",
          title: "预约相关",
          subs: [
            {
              index: "/historyAppointment",
              title: "历史预约",
            },
            {
              index: "/dspensingMedicine",
              title: "患者发药",
            }
          ]
        }
      ]
    }
  }
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 250px;
}

.sidebar > ul {
  height: 100%;
}
</style>
