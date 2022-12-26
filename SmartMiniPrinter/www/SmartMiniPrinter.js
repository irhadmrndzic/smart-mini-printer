
var exec = require('cordova/exec');

var PLUGIN_NAME = 'SmartMiniPrinter';

var smartmini = {
    
    init: function() {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, 'init', []);
        });
    },

    printText: function(text) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, 'printText', [text]);
        });
    },

    printBitmap: function(text) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, 'printBitmap', [text]);
        });
    },

    printBarCode: function(text) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, 'printBarCode', [text]);
        });
    },

    printQrcode: function(text) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, 'printQrcode', [text]);
        });
    },

    feedPaper: function(lines) {
        return new Promise(function(resolve, reject) {
            exec(resolve, reject, PLUGIN_NAME, 'feedPaper', [lines]);
        });
    }
};

module.exports = smartmini;