import { getQuestion } from "../hooks/getQuestion";
import { Question } from "../types/question";

const question: Question = await getQuestion();

export const TriviaContainer = () => {
  return (
    <div className="flex flex-col justify-center items-center w-1/3 h-5/6 rounded-xl bg-secondary-gold shadow-xl border-2 border-solid border-primary-blue">
      <div className="flex items-center justify-center h-2/6 w-2/3 bg-white my-10 rounded-xl border-2 border-solid border-primary-gold">
        <p className="m-auto p-5 text-secondary-blue italic font-bold text-xl">{question.question}</p>
      </div>
      {question.options.map((option, index) => (
        <button key={index} className="trivia-option-button">{option}</button>
      ))}
    </div>
  )
}
