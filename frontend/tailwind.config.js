/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        "primary-blue": "#042457",
        "secondary-blue": "#0d4579",
        "primary-gold": "#7d7045",
        "secondary-gold": "#bba150"
      }
    },
  },
  plugins: [],
}
