module.exports = {
  content: [
    './src/main/jte/**/*.jte',
    './src/main/java/**/*.java',
    './src/main/resources/static/js/**/*.js'
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif']
      },
      colors: {
        brand: '#10B981',
        primary: '#0F172A',
        secondary: '#64748B',
        surface: '#FFFFFF',
        background: '#F8FAFC'
      }
    }
  },
  plugins: []
};
