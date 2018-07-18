
# -falafreud-float-widget

## Getting started

`$ npm install -falafreud-float-widget --save`

### Mostly automatic installation

`$ react-native link -falafreud-float-widget`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.falafreud.FalafreudFloatWidgetPackage;` to the imports at the top of the file
  - Add `new FalafreudFloatWidgetPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':-falafreud-float-widget'
  	project(':-falafreud-float-widget').projectDir = new File(rootProject.projectDir, 	'../node_modules/-falafreud-float-widget/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':-falafreud-float-widget')
  	```


## Usage
```javascript
import FalafreudFloatWidget from '-falafreud-float-widget';

// TODO: What to do with the module?
FalafreudFloatWidget;
```
  