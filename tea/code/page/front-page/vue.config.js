'use strict'
const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  // productionSourceMap: process.env.NODE_ENV !== 'development',
  productionSourceMap: false,
  devServer: {
    // before: require('./mock/mock-server.js'),
    port: 8081,
    // host: '',
    open: false,
    // https: true,
    // disableHostCheck: "all",
    // overlay: {
    //   warnings: false,
    //   errors: true
    // },
    proxy: {
      '/api/': {
        target: 'http://120.46.197.40:9021/',
        changeOrigin: true,
        logLevel: 'debug',
        pathRewrite: {
          '^/api/': ''
        }
      },
      '/passport-api/': {
        target: 'https://dev-passport-cs-mall.tctm.life/',
        changeOrigin: true,
        logLevel: 'debug',
        pathRewrite: {
          '^/passport-api/': ''
        }
      }

    }

  },
  configureWebpack: {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    name: '学茶网',
    resolve: {
      extensions: ['.js', '.vue', '.json'],
      alias: {
        '@': resolve('src')
      }
    }
  },
  chainWebpack(config) {
    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()

    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 3, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          config.optimization.runtimeChunk('single')
        }
      )
    config.when(process.env.NODE_ENV === 'development',
      config => config.devtool('source-map')
    )
    config
      .when(process.env.NODE_ENV === 'production',
        config => {
          config.optimization.minimizer('terser').tap(options => {
            options[0].terserOptions.compress.drop_console = true
            return options
          })
        }
      )
  }
}
