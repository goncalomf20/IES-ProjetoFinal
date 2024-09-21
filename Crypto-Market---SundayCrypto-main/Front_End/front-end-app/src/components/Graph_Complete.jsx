import React from "react";
import Chart from "react-apexcharts";
import PropagateLoader from "react-spinners/PropagateLoader";

function Graph_Complete({ array, color_line }) {
  if (!array) {
    return (
      <div style={{height:"600px"}}>
        <h2>LOADING</h2>
        <PropagateLoader color={"#000"} size={10} />
      </div>
    );
  }
  console.log("Array Inicial " + array);

  const color_line_hex = color_line === "red" ? "#FF0000" : "#00FF00";

  const time = array.map((entry) => {
    const date = new Date(entry[0]); // entry[0] is already in milliseconds
    const monthNames = [
      "Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];
    const month = monthNames[date.getMonth()];
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = "0" + date.getMinutes();
    const seconds = "0" + date.getSeconds();
    return `${month} ${day} ${hours}:${minutes.slice(-2)}:${seconds.slice(-2)}`;
  });
  const price = array.map((entry) => entry[1]);
  console.log(time, price);
  const gradientColors = ["#969595", "rgb(240,186,52)"];
  return (
    <Chart
      options={{
        chart: {
          id: "chart",
          type: "area",
        },
        stroke: {
          curve: "straight",
          width: 3,
          colors: ["rgb(240,186,52)"],
        },
        tooltip: {
          enabled: true,
        },
        toolbar: {
          show: true,
        },
        fill: {
          type: "gradient",
          gradient: {
            shadeIntensity: 1,
            opacityFrom: 0.7,
            opacityTo: 0.9,
            stops: [0, 100],
            colorStops: [
              {
                offset: 0,
                color: "rgb(240,186,52)", // Start with the color of the line
                opacity: 1,
              },
              {
                offset: 100,
                color: "rgba(0, 0, 0, 0)", // End with a transparent color or another color
                opacity: 0,
              },
            ],
          },
        },
        xaxis: {
          categories: time,
          tickAmount: 11,
        },
        dataLabels: {
          enabled: false,
        },
      }}
      series={[
        {
          name: "price",
          data: price,
          stroke: {
            color: "rgb(240,186,52)",
          },
        },
      ]}
      width={"100%"}
      type="area"
    />
  );
}

export default Graph_Complete;
