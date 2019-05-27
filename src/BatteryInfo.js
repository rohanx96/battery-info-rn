import React, { Component } from "react";
import { View, Text, Image, StyleSheet, StatusBar } from "react-native";
import colors from "./colors";

export default class BatteryInfo extends Component {
  constructor(props) {
    super(props);
    this.state = {
      percentage: undefined,
      status: "Charging"
    };
  }

  componentDidMount() {
    const percentage = Math.floor(Math.random() * (100 - 0) + 0);
    this.setState({
      ...this.getImageAndColorForPercentage(percentage),
      percentage: percentage
    });
  }

  getImageAndColorForPercentage = percentage => {
    let image = require("./assets/battery-full2.png");
    let color = colors.full_blue;
    if (percentage < 100 && percentage >= 75) {
      image = require("./assets/battery-high2.png");
      color = colors.high_green;
    } else if (percentage < 75 && percentage >= 45) {
      image = require("./assets/battery-medium2.png");
      color = colors.medium_orange;
    } else if (percentage < 45 && percentage >= 15) {
      image = require("./assets/battery-low2.png");
      color = colors.low_orange;
    } else if (percentage < 15) {
      image = require("./assets/battery-empty2.png");
      color = colors.empty_red;
    }
    return { image, color };
  };

  render() {
    return (
      <View style={styles.container}>
        <StatusBar backgroundColor={this.state.color} />
        <Image
          source={this.state.image}
          style={{ width: 300, marginBottom : 64 }}
          resizeMode="contain"
        />
        <View style={{ flexDirection: "row", margin: 12 }}>
          <Text style={{ fontSize: 25 }}>Battery Percentage: </Text>
          <Text
            style={{
              fontSize: 25,
              color: this.state.color,
              fontWeight: "bold"
            }}
          >{`${this.state.percentage}%`}</Text>
        </View>
        <View style={{ flexDirection: "row" }}>
          <Text style={{ fontSize: 25 }}>Battery Status: </Text>
          <Text
            style={{
              fontSize: 25,
              fontWeight: "bold"
            }}
          >
            {this.state.status}
          </Text>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    justifyContent: "center",
    alignItems: "center"
  },
});
