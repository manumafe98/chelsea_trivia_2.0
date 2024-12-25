import { getQuestion } from "../hooks/getQuestion";
import { Question } from "../types/question";

const question: Question = await getQuestion();

export const TriviaContainer = () => {
  return (
    <div className="flex flex-col justify-center items-center w-1/3 h-5/6 rounded-xl bg-secondary-gold shadow-xl border-2 border-solid border-primary-blue">
      <div className="flex items-center text-center justify-center h-2/6 w-2/3 bg-white my-10 rounded-xl border-2 border-solid border-primary-gold">
        {question.attribute === "profileImageUrl" && question.type === "AttributeRelatedQuestions" ? (
          <div className="flex flex-col justify-center align-center">
            <p className="p-5 text-secondary-blue italic font-bold text-xl">{question.question.split("?")[0] + "?"}</p>
            <div className="flex justify-center">
              <img className="rounded-md border-solid border-2 border-primary-gold w-12 h-12" src={question.question.split("?")[1] as string} alt="profile image url"/>
            </div>
          </div>
        ) : (
          <p className="p-5 text-secondary-blue italic font-bold text-xl">{question.question}</p>
        )}
      </div>
      {question.options.map((option, index) => (
        <button key={index} className="flex justify-center items-center trivia-option-button">
          {question.attribute === "profileImageUrl" && question.type === "PlayerRelatedQuestions" ? (
            <img className="rounded-md border-solid border-2 border-primary-gold w-12 h-12" src={option as string} alt="profile image url"/>
          ) : (
            <p>{option}</p>
          )}
        </button>
      ))}
    </div>
  )
}
