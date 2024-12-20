import { useAxios } from "../hooks/useAxios"
import { Player } from "../types/player";

const options: Player[] = await useAxios("/backend/api/v1/player/random");

export const TriviaContainer = () => {
  return (
    <div className="flex flex-col justify-center items-center w-1/3 h-5/6 rounded-xl bg-secondary-gold shadow-xl border-2 border-solid border-primary-blue">
      <div className="flex items-center justify-center h-2/6 w-2/3 bg-white my-10 rounded-xl border-2 border-solid border-primary-gold">
        <p className="text-secondary-blue italic font-bold text-xl">QUESTION</p>
      </div>
      {options.map((option) => (
        <button id={option.id} className="trivia-option-button">{option.fullName}</button>
      ))}
    </div>
  )
}
