<template>
  <div>
    <el-row :span="10" style="margin-bottom: 40px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid"/> Número total de empleados</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalEmployees }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #FC466B">
          <div><i class="el-icon-money"/> Número total de clientes VIP</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalVIPCustomers }}
          </div>
        </el-card>
      </el-col>


    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 450px"></div>
      </el-col>

      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 450px"></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
    return{
      isAdmin: false,
      totalEmployees: 0,
      totalVIPCustomers: 0,
    }
  },
  mounted() {
    this.request.get("/echarts/totalEmployees").then(res => {
      this.totalEmployees = res.data;
    }).catch(error => {
      console.error("Error fetching total employees:", error);
    });

    this.request.get("/echarts/totalVIPCustomers").then(res => {
      this.totalVIPCustomers = res.data;
    }).catch(error => {
      console.error("Error fetching total employees:", error);
    });

    var option = {
      title: {
        text: 'VIP customers',
        subtext: 'Statistical data',
        left: 'center'

      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ['1-3', '4-6', '7-9', '10-12']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name:"VIP customer",
          data: [],
          type: 'line'
        },
        {
          name:"",
          data: [],
          type: 'bar'
        }
      ]
    };

    var pieOption = {
      title: {
        text: 'VIP customers',
        subtext: 'ratio graph',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: "123",
          type: 'pie',
          radius: '60%',
          label: {
            show: true,
            position: 'inner',
            fontWeight: 300,
            fontSize: 14,
            color: "#fff",
            formatter: '{d}%'
          },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/members").then(res => {
      const xAxisLabels = ['1-3', '4-6', '7-9', '10-12'];
      option.xAxis.data= xAxisLabels;
      option.series[0].data = res.data
      option.series[1].data = res.data
      myChart.setOption(option);


      pieOption.series[0].data = [
        {name:"the first season", value: res.data[0]},
        {name:"the second season", value: res.data[1]},
        {name:"the third season", value: res.data[2]},
        {name:"the fourth season", value: res.data[3]},
      ]
      pieChart.setOption(pieOption)
    })
  }
}
</script>

<style scoped>

</style>