<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true" :model="search">
        <el-form-item>
          <el-input v-model="search.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="findByName">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="success" size="small" @click="uploadServer">导入数据</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" size="small" @click="dataTemplateDownload">数据模板下载</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" size="small" @click="exportData">导出全部数据</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!--导入数据到服务器解析-->
    <el-dialog title="上传表单" v-model="uploadServerVisible" :close-on-click-modal="false">
      <el-alert
          title="上传须知"
          type="warning"
          description="上传表单格式务必按照模板，一次仅限上传一个文件"
          show-icon>
      </el-alert>
      <!--最多上传1个文件，点击之后不立即上传-->
      <el-upload
          class="upload-demo"
          ref="upload"
          drag
          :limit="1"
          action="http://localhost:8080/patient/uploadToServer"
          :file-list="fileList"
          :on-exceed="handleExceed"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx文件，且文件大小不宜过大</div>
      </el-upload>
      <el-button type="success" plain @click="uploadToServer">确认提交</el-button>
    </el-dialog>
    <!--列表-->
    <el-table :data="patientInformation" highlight-current-row v-loading="listLoading" @selection-change="selsChange"
              style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" label="序号" width="100">
      </el-table-column>
      <el-table-column prop="patientName" label="真实姓名" width="130" sortable>
      </el-table-column>
      <el-table-column prop="webName" label="网站昵称" width="120" sortable>
      </el-table-column>
      <el-table-column prop="patientPhone" label="电话" width="140" sortable>
      </el-table-column>
      <el-table-column prop="patientEmail" label="电子邮件" width="250" sortable>
      </el-table-column>
      <el-table-column prop="identityNumber" label="身份证号" width="200" sortable>
      </el-table-column>
      <el-table-column prop="password" label="密码" min-width="100" sortable>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total"
                     style="float:right;">
      </el-pagination>
    </el-col>

    <!--编辑界面-->
    <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
      <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
        <el-form-item label="网站昵称" prop="webName">
          <el-input v-model="editForm.webName" auto-complete="off" placeholder="不填写则默认为真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话号码" prop="patientPhone" :rules="[
                      { pattern:  /^1[3456789]\d{9}$/,
                        message: '请填写正确的电话号码',
                        trigger: 'blur'}
                      ]">
          <el-input v-model="editForm.patientPhone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电子邮件" prop="patientEmail" :rules="[
            { pattern: /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
              message: '请填写正确的邮箱地址',
              trigger: 'blur'},
            { required: true, message: '电子邮件不能为空'}
            ]">
          <el-input v-model="editForm.patientEmail" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="editForm.patientAddr"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>

    <!--    新增界面-->
    <el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">
      <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
        <el-form-item label="真实姓名" prop="patientName" :rules="[
                      { required: true, message: '真实姓名不能为空'}
                      ]">
          <el-input v-model="addForm.patientName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="网站昵称" prop="webName">
          <el-input v-model="addForm.webName" auto-complete="off" placeholder="不填写则默认为真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话号码" prop="patientPhone" :rules="[
                      { pattern:  /^1[3456789]\d{9}$/,
                        message: '请填写正确的电话号码',
                        trigger: 'blur'}
                      ]">
          <el-input v-model="addForm.patientPhone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电子邮件" prop="patientEmail" :rules="[
            { pattern: /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
              message: '请填写正确的邮箱地址',
              trigger: 'blur'},
            { required: true, message: '电子邮件不能为空'}
            ]">
          <el-input v-model="addForm.patientEmail" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="identityNumber"
                      :rules="[
                           { pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}$)/,
                            message: '请填写正确的身份证号码',
                            trigger: 'blur'},
                          { required: true, message: '身份证号码不能为空'}
                          ]">
          <el-input v-model="addForm.identityNumber" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="生日" prop="patientBirth">
          <el-date-picker
              v-model="addForm.patientBirth"
              type="date"
              placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="addForm.patientSex">
            <el-radio class="radio" :label="1">男</el-radio>
            <el-radio class="radio" :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="登陆密码" :rules="[
                          { required: true, message: '登陆密码不能为空'}
                          ]">
          <el-input type="password" v-model="addForm.password"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="addForm.patientAddr"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import util from '../../common/js/util'
//import NProgress from 'nprogress'
import {
  addPatient,
  analyseFile,
  batchRemovePatient,
  editPatient,
  exportData,
  findByName,
  getUserListPage,
  removeUser,
  templateDownload
} from '../../api/api';

export default {
  data() {
    return {
      fileList: [],
      search: {
        name: ''
      },
      patientInformation: [],
      total: '',
      page: 1,
      uploadServerVisible: false,
      listLoading: false,
      sels: [],//列表选中列

      editFormVisible: false,//编辑界面是否显示
      editLoading: false,
      editFormRules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'}
        ]
      },

      //编辑界面数据
      editForm: {
        id: 0,
        patientName: '',
        webName: '',
        patientPhone: '',
        patientEmail: '',
        identityNumber: '',
        avatarUrl: '',
        password: '',
        patientSex: -1,
        patientBirth: '',
        patientAddr: '',
        patientAge: '0'
      },

      addFormVisible: false,//新增界面是否显示
      addLoading: false,
      addFormRules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'}
        ]
      },
      //新增界面数据
      addForm: {
        id: 0,
        patientName: '',
        webName: '',
        patientPhone: '',
        patientEmail: '',
        identityNumber: '',
        avatarUrl: '',
        password: '',
        patientSex: -1,
        patientBirth: '',
        patientAddr: '',
        patientAge: '0'
      }

    }
  },
  methods: {
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    passwordValidation() {
      if (this.addForm.password !== this.checkPass) {
        callback(new Error('两次密码不一致'));
      }
    },

    //性别显示转换
    formatSex: function (row, column) {
      return row.sex == 1 ? '男' : row.sex == 0 ? '女' : '未知';
    },
    handleCurrentChange(val) {
      this.page = val;
      this.getUsers();
    },
    //获取用户列表
    getUsers() {
      let para = {
        page: this.page
      };
      this.listLoading = true;
      //NProgress.start();
      getUserListPage(para).then((res) => {
        let data = JSON.parse(res.data);
        this.total = data.total;
        this.patientInformation = data.list;
        this.listLoading = false;
        //NProgress.done();
      }).catch((res)=>{
        if(res.response.status===505){
          this.$router.push('/login');
        }
      });
    },
    findByName() {
      let para = {
        page: this.page,
        name: this.search.name
      };
      let _this = this;
      findByName(para).then(function (req) {
        let data = JSON.parse(req.data);
        _this.total = data.total;
        _this.patientInformation = data.list;
        _this.listLoading = false;
      })
    },
    //删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        //NProgress.start();
        let para = {id: row.id};
        removeUser(para).then((res) => {
          this.listLoading = false;
          //NProgress.done();
          this.$message({
            message: res.data,
            type: res.data
          });
          this.getUsers();
        });
      }).catch(() => {

      });
    },
    //显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true;
      this.editForm = Object.assign({}, row);
    }, uploadServer: function () {
      this.uploadServerVisible = true;
    },
    //显示新增界面
    handleAdd: function () {
      this.addFormVisible = true;
      this.addForm = {
        id: 0,
        patientName: '',
        webName: '',
        patientPhone: '',
        patientEmail: '',
        identityNumber: '',
        avatarUrl: '',
        password: '',
        patientSex: -1,
        patientBirth: '',
        patientAddr: '',
        patientAge: 0
      }
    },
    //编辑
    editSubmit: function () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true;
            //NProgress.start();
            let para = Object.assign({}, this.editForm);
            editPatient(para).then((res) => {
              this.editLoading = false;
              //NProgress.done();
              this.$message({
                message: '提交成功',
                type: res.data
              });
              this.$refs['editForm'].resetFields();
              this.editFormVisible = false;
              this.getUsers();
            });
          });
        }
      });
    },
    //新增
    addSubmit: function () {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true;
            //NProgress.start();
            let para = Object.assign({}, this.addForm);
            this.addForm.patientBirth = (!para.patientBirth || para.patientBirth == '') ? '' : util.formatDate.format(new Date(para.patientBirth), 'yyyy-MM-dd');
            let _this = this
            addPatient(_this.addForm).then((res) => {
              this.addLoading = false;
              //NProgress.done();
              this.$message({
                message: '提交成功',
                type: 'success'
              });
              this.$refs['addForm'].resetFields();
              this.addFormVisible = false;
              this.getUsers();
            });
          });
        }
      });
    },
    selsChange: function (sels) {
      this.sels = sels;
    },
    //批量删除
    batchRemove: function () {
      var ids = this.sels.map(item => item.id).toString();
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        //NProgress.start();
        let para = {ids: ids};
        let _this = this
        batchRemovePatient(para).then((res) => {
          _this.listLoading = false;
          //NProgress.done();
          _this.$message({
            message: '删除成功',
            type: res.data
          });
          _this.getUsers();
        }).catch(function (error) {
          _this.$message({
            message: '删除失败，请稍后尝试',
            type: 'error'
          });
        })
      }).catch(() => {
      });
    }, exportData() {
      exportData().then((res) => {
        const disposition = res.headers['content-disposition'];
        let fileName = disposition.match(/=(.*)$/)[1];
        let blob = new Blob([res.data])
        let downloadElement = document.createElement('a')
        let href = window.URL.createObjectURL(blob)
        downloadElement.href = href
        downloadElement.download = fileName
        document.body.appendChild(downloadElement)
        downloadElement.click()
        document.body.removeChild(downloadElement)
        window.URL.revokeObjectURL(href)
      })
    }, dataTemplateDownload() {
      templateDownload().then((res) => {
        const disposition = res.headers['content-disposition'];
        let fileName = disposition.match(/=(.*)$/)[1];
        let blob = new Blob([res.data])
        let downloadElement = document.createElement('a')
        let href = window.URL.createObjectURL(blob)
        downloadElement.href = href
        downloadElement.download = fileName
        document.body.appendChild(downloadElement)
        downloadElement.click()
        document.body.removeChild(downloadElement)
        window.URL.revokeObjectURL(href)
      })
    }, uploadToServer: function () {
      //将数据提交到后端执行解析方法
      // this.$refs.upload.submit();
      this.analyseFile()
    }, analyseFile: function () {
      analyseFile().then((res) => {
        if(res.data.message==="error"){
          this.$message.error(res.data.displayInformation);
        }else if(res.data.message==="success"){
          this.$message({
            message: res.data.displayInformation,
            type: 'success'
          });
        }
      })
    }
    ,
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    }
  },
  created() {
    this.getUsers();
  }
}

</script>

<style scoped>

</style>