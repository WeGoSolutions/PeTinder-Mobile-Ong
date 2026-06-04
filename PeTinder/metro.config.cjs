// Habilita importar arquivos .svg como componentes React (react-native-svg),
// para que SVGs renderizem no nativo (Expo Go) — o <Image> do React Native NAO
// renderiza SVG no celular, so no navegador.
//
// IMPORTANTE: este projeto usa "type": "module" no package.json, entao o arquivo
// de config do Metro precisa ser .cjs (CommonJS) — um metro.config.js seria
// interpretado como ES module e quebraria o require/module.exports.
const { getDefaultConfig } = require('expo/metro-config');

const config = getDefaultConfig(__dirname);

config.transformer = {
  ...config.transformer,
  babelTransformerPath: require.resolve('react-native-svg-transformer'),
};

config.resolver = {
  ...config.resolver,
  assetExts: config.resolver.assetExts.filter((ext) => ext !== 'svg'),
  sourceExts: [...config.resolver.sourceExts, 'svg'],
};

module.exports = config;
