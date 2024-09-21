import React from "react";
import Chart from "react-apexcharts";
import "./Graph.css";
import PropagateLoader from "react-spinners/PropagateLoader";

function Graph({ array, color_line }) {
  if (!array) {
    return (
      <div>
        <h2>LOADING</h2>
        <PropagateLoader color={"#000"} size={10} />
      </div>
    );
  }
  //console.log("Array Inicial " + array);

  const color_line_hex = color_line === "red" ? "#FF0000" : "#00FF00";


  const time = array.map((entry) => {
    const date = new Date(entry[0]); // entry[0] is already in milliseconds
    const hours = date.getHours();
    const minutes = "0" + date.getMinutes();
    const seconds = "0" + date.getSeconds();
    return hours + ':' + minutes.slice(-2) + ':' + seconds.slice(-2);
    });
  const price = array.map((entry) => entry[1]);
  //console.log(time, price);
  return (
    <Chart options={{
        chart: {
            id: 'apexchart-example',
            type: 'area'
        },
        grid: {
            show:false},
        dataLabels: {
            enabled: false
        },
        yaxis: {
            show:false
        },
        toolbar: {
            show: false
        },
        stroke: {
            curve: 'straight',
            width: 2,
            colors: [color_line_hex],
        },
        tooltip: {
            enabled: true,
            followCursor: false,
            fixed: {
                enabled: true,
                position: 'topRight',
                offsetX: 150,
                offsetY: 0,
            },
            x: {
                show: true,
            },
        },
        xaxis: {
            categories: time,
            axisBorder: {
                show: false
            },
            axisTicks: {
                show: false
            },
            labels: {
                show: false
            }
        },
        }}
        series={[{
            name: 'price',
            data: price,
            stroke: {
                color: color_line_hex,
            },

        }]}
        type="area"
        height={160}
        width={260}
    />
  );
}

export default Graph;
