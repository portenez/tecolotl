module.exports = (grunt)->
  
  grunt.loadNpmTasks 'grunt-contrib-coffee'
  grunt.loadNpmTasks 'grunt-contrib-watch'
  grunt.registerTask 'default', 'coffee'
  
  grunt.initConfig
    #grunt coffee
    coffee:
      compile:
        expand:true
        cwd: 'src'
        src: ['**/*.coffee']
        dest: 'lib'
        ext: '.js'
    #grunt watch
    watch:
      coffee:
        files: 'src/*.coffee'
        tasks: ['coffee:compile']
      
  
  
