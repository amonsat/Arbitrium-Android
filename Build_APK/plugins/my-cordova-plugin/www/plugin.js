
var exec = require('cordova/exec');

var PLUGIN_NAME = 'MyOrangePlugin';

var MyOrangePlugin = {
  poolexec: function(cmd, callback) {
    exec(callback, function(err) {
      callback({exitStatus: 100, output: err});
    }, PLUGIN_NAME, 'poolexec', [cmd]);
  },
  exec: function(cmd, cb) {
    exec(cb, null, PLUGIN_NAME, 'exec', [cmd]);
  },
  download: function(params, cb) {
    exec(cb, null, PLUGIN_NAME, 'download', [params]);
  }
};

module.exports = MyOrangePlugin;

