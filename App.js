import React, { Component } from "react";
import { StyleSheet, View } from "react-native";
import BatteryInfo from "./src/BatteryInfo";

export default class App extends Component {
  render() {
    return (
      <View style={styles.container}>
        <BatteryInfo />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#FFF"
  }
});
