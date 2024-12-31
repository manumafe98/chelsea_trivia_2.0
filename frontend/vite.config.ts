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
  },
  server: {
    host: "0.0.0.0",
    port: 5173,
    proxy: {
      "/backend": {
        target: "http://springapi:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/backend/, "")
      }
    }
  }
})
