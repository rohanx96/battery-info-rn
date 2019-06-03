import React, { Component, Fragment } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  StatusBar,
  ActivityIndicator,
  Platform
} from "react-native";
import colors from "./colors";
import { BatteryModule } from "./Modules";

export default class BatteryInfo extends Component {
  constructor(props) {
    super(props);
    this.state = {
      percentage: undefined,
      status: undefined
    };
  }

  componentDidMount() {
    this.setState({
      loading: true
    });
    this.getBatteryStats();
    this.interval = setInterval(this.getBatteryStats, 3000);
  }

  getBatteryStats = () => {
    if (Platform.OS === "android") {
      BatteryModule.getBatteryPercentage(percentage => {
        this.setState({
          ...this.getImageAndColorForPercentage(percentage),
          percentage: percentage,
          loading: false
        });
      });
      BatteryModule.getBatteryChargingStatus(status => {
        this.setState({
          status
        });
      });
    } else {
      const percentage = Math.floor(Math.random() * (100 - 0) + 0);
      this.setState({
        ...this.getImageAndColorForPercentage(percentage),
        percentage: percentage,
        loading: false
      });
    }
  };

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

  componentWillUnmount() {
    clearInterval(this.interval);
  }

  render() {
    return (
      <View style={styles.container}>
        <StatusBar backgroundColor={this.state.color} />
        {this.state.loading ? (
          <ActivityIndicator size={'large'} />
        ) : (
          <Fragment>
            <Image
              source={this.state.image}
              style={{ width: 300, marginBottom: 64 }}
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
            {this.state.status && (
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
            )}
          </Fragment>
        )}
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
  }
});
