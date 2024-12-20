import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { ChelseaTriviaApp } from './ChelseaTriviaApp'
import './index.css'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ChelseaTriviaApp/>
  </StrictMode>,
)
