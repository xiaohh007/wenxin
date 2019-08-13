<template>
  <v-card>
    <v-card-title>
      <v-btn color="primary" @click="addBrand">新增品牌</v-btn>
      <!--搜索框，与search属性关联-->
      <v-spacer />
      <v-text-field label="输入关键字搜索" v-model.lazy="search" append-icon="search" hide-details />
    </v-card-title>
    <v-divider />
    <v-data-table
      :headers="headers"
      :items="brands"
      :search="search"
      :pagination.sync="pagination"
      :total-items="totalBrands"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td>{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center">
          <img :src="props.item.image" />
        </td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="justify-center layout">
          <v-btn icon @click="editBrand(props.item)" color="info">
            <i class="el-icon-edit" />
          </v-btn>
          <v-btn icon @click="deleteBrand(props.item)" color="warning">
            <i class="el-icon-delete" />
          </v-btn>
        </td>
      </template>
    </v-data-table>

    <!--弹出的对话框-->
    <v-dialog max-width="500" v-model="show" persistent>
      <v-card>
        <!--对话框的标题-->
        <v-toolbar dense dark color="primary">
          <v-toolbar-title>{{isAdd ? "新增" :"修改"}}品牌</v-toolbar-title>
          <v-spacer />
          <!--关闭窗口的按钮-->
          <v-btn icon @click="closeWindow">
            <v-icon>close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text class="px-5" style="height:400px">
          <my-brand-form @reload="reload" v-bind:isEdit="isAdd" v-bind:oldBrand="oldBrand"></my-brand-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script>
import MyBrandForm from './MyBrandForm';
export default {
  name: "my-brand",
  data() {
    return {
      search: "", // 搜索过滤字段
      totalBrands: 0, // 总条数
      brands: [], // 当前页品牌数据
      loading: true, // 是否在加载中
      pagination: {}, // 分页信息
      headers: [
        { text: "id", align: "center", value: "id" },
        { text: "名称", align: "center", sortable: false, value: "name" },
        { text: "LOGO", align: "center", sortable: false, value: "image" },
        { text: "首字母", align: "center", value: "letter", sortable: true },
        { text: "操作", align: "center", value: "id", sortable: false }
      ],
      show: false,
      isAdd:false,
      oldBrand:{}
    };
  },
  mounted() {
    // 渲染后执行
    // 查询数据
    this.getDataFromServer();
  },
  watch: {
    pagination: {
      //监视pagination属性的变化
      deep: true, //deep为true,会监视pagination的属性及属性中的对象属性变化
      handler() {
        //变化后的回调函数,这里我们在次调用getDateFromServer即可
        this.getDataFromServer();
      },
      search: {
        //监视搜索字段
        handler() {
          this.getDataFromServer();
        }
      }
    }
  },
  methods: {
    getDataFromServer() {
      // 从服务的加载数的方法。
      this.$http
        .get("/item/brand/page", {
          params: {
            key: this.search, // 搜索条件
            page: this.pagination.page, // 当前页
            rows: this.pagination.rowsPerPage, // 每页大小
            sortBy: this.pagination.sortBy, // 排序字段
            desc: this.pagination.descending // 是否降序
          }
        })
        .then(resp => {
          // 这里使用箭头函数
          // 将得到的数据赋值给本地属性
          this.brands = resp.data.items;
          this.totalBrands = resp.data.total;
          // 完成赋值后，把加载状态赋值为false
          this.loading = false;
        });
    },
    addBrand() {
      this.isAdd = true;
      this.show = true;
    },
    editBrand(oldBrand){
      this.isAdd = false;
      this.show = true;
      this.oldBrand = oldBrand;
    },
    closeWindow(){
      //关闭窗口
      this.show = false;
      //重新加载数据
      this.getDataFromServer();
    }
  },
  clear(){
    //重置表单
    this.brand.name="";
    this.brand.letter="";
    this.brand.image="";
    this.brand.categories=[];
    this.$refs.image_url._data.dialogImageUrl="";

  },
  whtch:{
    oldBrand:{
      deep:true,
      handler(val){
        if(val){
          this.brand = Object.deepCopy(val);
        }else{
          this.clear();
        }
      }
    }
  },
  components: {
    MyBrandForm
  }
};
</script>
<style scoped>
</style>
