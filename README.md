# react-native-react-wis

## Getting started

`$ npm install react-native-react-wis --save`

### Mostly automatic installation

`$ react-native link react-native-react-wis`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-react-wis` and add `ReactWis.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libReactWis.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.ReactWisPackage;` to the imports at the top of the file
  - Add `new ReactWisPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-react-wis'
  	project(':react-native-react-wis').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-react-wis/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-react-wis')
  	```


## Usage
```javascript
import ReactWis from 'react-native-react-wis';

// TODO: What to do with the module?
ReactWis;
```
