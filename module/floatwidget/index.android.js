/**
* @author Haroldo Shigueaki Teruya <haroldo.s.teruya@gmail.com>
* @version 1.0.0
*/

//==========================================================================
// IMPORTS

/**
* This class requires:
* @class
* @requires DeviceEventEmitter from react-native
* @requires NativeModules from react-native
*/

import { DeviceEventEmitter, NativeModules } from 'react-native';

//==========================================================================

/**
* @class
* @classdesc
*/
class FloatWidgetManager {

    //==========================================================================
    // GLOBAL VARIABLES

    //==========================================================================
    // CONSTRUCTOR

    constructor() {
    }

    handleStartService(count : int) : void {
        NativeModules.FloatWidgetManagerModule.handleStartService(count);
    }

    showWhenApplicationInactive(enable : boolean) : void {
        NativeModules.FloatWidgetManagerModule.showWhenApplicationInactive(enable);
    }

    isToShowWhenApplicationInactive() : boolean {
        return NativeModules.FloatWidgetManagerModule.isToShowWhenApplicationInactive();
    }

    //==========================================================================
    // METHODS
}

//==============================================================================
// EXPORTS

module.exports = new FloatWidgetManager();
