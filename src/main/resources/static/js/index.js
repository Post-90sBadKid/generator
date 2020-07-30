window.onload = function () {

    var vm = new Vue({
        el: '#app',
        data: {
            visible: false,
            tableData: [],
            form: {
                tableName: null,
                tables: ""
            },
            multipleSelection: [],
            pageNum: 1,
            pageSize: 10,
            total: 0,
            activeNames: ['1']
        },
        mounted() {
            this.fetchData();
        },
        methods: {
            fetchData() {
                const _this = this;
                axios({
                    method: 'post',
                    url: '/generator/list',
                    params: {
                        pageNum: _this.pageNum,
                        pageSize: _this.pageSize,
                        total: _this.total,
                        tableName: _this.form.tableName
                    }
                }).then(function (response) {
                    _this.tableData = response.data.list;
                    _this.pageNum = response.data.pageNum;
                    _this.pageSize = response.data.pageSize;
                    _this.total = response.data.total;

                    console.log(response);
                }).catch(function (error) {
                    console.log(error);
                });
            },
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            },
            handleSelect(key, keyPath) {
                if (key=="doc"){
                    this.visible=false
                }else{
                    this.visible=true
                }
                console.log(key, keyPath);
            },
            handleGeneratorCode() {
                let a = document.createElement("a");
                a.href = `/generator/code?tables=${this.form.tables}`;
                document.body.appendChild(a);
                a.click(); //下载
                URL.revokeObjectURL(a.href); // 释放URL 对象
                document.body.removeChild(a);
            },
            handleSizeChange(size) {
                this.pageSize = size;
                this.fetchData();
            },
            handleCurrentChange(val) {
                this.pageNum = val;
                this.fetchData();
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;

                let arr3 = this.multipleSelection.map(function (item, index) {
                    return item.tableName;
                });
                this.form.tables = arr3.join(",");
            },

        }
    })
}
