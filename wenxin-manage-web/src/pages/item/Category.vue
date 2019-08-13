<template>
  <v-card>
      <v-flex xs12 sm10>
        <v-tree url="/item/category/list"
                :isEdit="isEdit"
                @handleAdd="handleAdd"
                @handleEdit="handleEdit"
                @handleDelete="handleDelete"
                @handleClick="handleClick"
        />
      </v-flex>
      
  </v-card>
</template>

<script>
  export default {
    name: "category",
    data() {
      return {
        isEdit:true,
        
        
      }
      
       
    },
    methods: {
      handleAdd(node) {
        this.$http.post('/item/category', {
          "name": node.name,
          "sort": node.sort,
          "parentId": node.parentId,
          "isParent": node.isParent
        }).then().catch();
      },
      handleEdit(id, name) {
          
        this.$http.put('/item/category', {
          "id": id,
          "name": name,
          
        }).then(() => {
          this.$message.info("修改成功！");
        }).catch(() => {
          this.$message.info("修改失败！");
        });

      },
      handleDelete(id) {
        console.log("delete ... " + id)
        this.$http.delete("/item/category/cid/"+id).then(() =>{
        this.$message.info("删除成功！");
        }).catch(() =>{
        this.$message.info("删除失败！");
  })
      },
      handleClick(node) {
        console.log(node)
      },
      closeWindow(){
        // 重新加载数据
        //this.getDataFromServer();
        // 关闭窗口
        this.show = false;
      }
    }
  };
</script>

<style scoped>

</style>
