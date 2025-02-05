import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import tailwindcss from "tailwindcss";
import autoprefixer from "autoprefixer";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  css: {
    postcss: {
      plugins: [
        tailwindcss(),
        autoprefixer(),
      ],
    },
  },
  define: {
    "import.meta.env.API_KEY": JSON.stringify(process.env.API_KEY),
    "import.meta.env.BACKEND_URL": JSON.stringify(process.env.BACKEND_URL)
  }
})
