import React, { Component } from 'react';
import {Text, View} from "react-native";
import { WebView } from 'react-native-webview';

export default class WisInline extends Component {
    constructor() {
        super();
        this.state = {
            isHidden: false,
        };
    }

    render() {
        var visibleInline = null;
        if(!this.state.isHidden){
            visibleInline = <Text id='selininline'>Hello Selin !!</Text>;
        }
        return (
            <WebView
                originWhitelist={['*']}
                source={{ html: '<h1>This is a static HTML source!</h1>' }}
            />
        );
    }

}
